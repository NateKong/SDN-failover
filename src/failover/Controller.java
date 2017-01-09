package failover;

import java.util.ArrayList;

/**
 * A Software Defined Network controller.
 * A controller for a LTE network
 * over eNodeBs.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.HashMap;

public class Controller extends Entity implements Runnable {
	private HashMap<ENodeB, Integer> eNodeBs;
	private HashMap<ENodeB, int[]> orphans; // orphan eNodeBs
	private boolean isAlive;

	public Controller(int name, long maxTime) {
		super(("Controller" + Integer.toString(name)), maxTime);
		eNodeBs = new HashMap<ENodeB, Integer>();
		orphans = new HashMap<ENodeB, int[]>();
		isAlive = false;
		System.out.println(getName() + " is created");
	}

	/**
	 * Adds an eNodeB to the controllers database.
	 * Sets the controller to the eNodeB
	 * 
	 * @param e an eNodeB (LTE tower)
	 * @param hop is the number of x2 connections from the eNodeB to the controller
	 * @param bw is the minimum throughput (Mbps) between the controller and the eNodeB
	 */
	public void addENodeB(ENodeB e, int hop, int bw) {
		e.setController(this, hop, bw);
		eNodeBs.put(e,hop);
		System.out.println(name + " adopts " + e.getName() + "\thop: " + hop + "\tbw " + bw);
	}
	
	/**
	 * Adds an eNodeB to the controllers database
	 * without knowing hops or bw
	 * 
	 * @param e an eNodeB (LTE tower)
	 */
	public void addENodeB(ENodeB e) {
		int bw = -100;
		int numOfHops = -100;
		
		for (ENodeB b: eNodeBs.keySet()) {
			for (Xtwo x: b.getConnections()) {
				if (x.getEndpoint(b).equals(e)) {
					int hops = b.getCHops() + 1;
					if ( hops < numOfHops || numOfHops < 0) {
						numOfHops = hops;  
						bw = ( x.getBW() > b.getCbw() ) ? b.getCbw() : x.getBW();	
					}
				}
			}
		}
		
		eNodeBs.put(e, numOfHops);
		e.setController(this, numOfHops, bw);
		System.out.println(name + " adopts " + e.getName() + "\thop: " + numOfHops + "\tbw " + bw);
	}

	/**
	 * Adds to a list of orphan nodes
	 */
	public void addOrphan(ENodeB b, int hops, int bw) {
		int[] newStats = {hops, bw};
		if (orphans.containsKey(b)) {
			int[] oldStats = orphans.get(b);
			if (newStats[0] <= oldStats[0] && newStats[1] > oldStats[1]) {
				orphans.put(b, newStats);
			}
		} else {
			orphans.put(b, newStats);
		}
	}
	
	/**
	 * Adds to a list of orphan nodes for backup
	 */
	public void addBackup(ENodeB b, int hops, int bw) {
		/*
		if (b.getName().equals("eNodeB4")){
		 
			System.out.println(name + " receives " + b.getName() +"\tnew hops: "+hops+"\tnew bw: "+ bw+ "\thas backup controller " + b.hasBackupController());
		}
		*/
		if ( !b.hasBackupController() ) {
			b.setBackupController(this, hops, bw, false);
		} else if ( b.hasBackupController() ) {
			int oldHops = b.getBackupHops();
			int oldBW = b.getBackupBw();
			/*if (b.getName().equals("eNodeB0")){
				System.out.println("old hops:" + oldHops +"\tnew hops: "+hops+"\told bw: "+ oldBW +"\tnew bw: "+ bw);
			}*/
			if (hops < oldHops) {
				b.setBackupController(this, hops, bw, true);
			}else if (hops == oldHops && bw > oldBW) {
				b.setBackupController(this, hops, bw, true);
			}
		} 
	}

	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime(System.currentTimeMillis()) + ": Running thread " + name);
		isAlive = true;
		
		try {
			while (checkTime(System.currentTimeMillis())) {
				Thread.sleep(random());

				if (!orphans.isEmpty()) {
					adoptOrphans();
				}
				
				
				if ( !eNodeBs.isEmpty() ) {
					ArrayList<ENodeB> remove = new ArrayList<ENodeB>(); 
					for (ENodeB e: eNodeBs.keySet() ){
						if (!e.sameController(this)) {
							remove.add(e);
						}
					}
					for (ENodeB e: remove) {
						eNodeBs.remove(e);
					}
				}
			}
		} catch (InterruptedException e) {
			System.out.println(name + " interrupted.");
		}

		if (name.equals("Controller1")){
			removeController();
			System.out.println();
			System.out.println(getTime(System.currentTimeMillis()) + ": "+ name + " failed\n" );
		}else {
			System.out.println(getTime(System.currentTimeMillis()) + ": Closing thread " + name);	
		}
		
		isAlive = false;
	}

	/**
	 * Adopts orphan eNodeBs when controller fails
	 */
	private void adoptOrphans() {
		for (ENodeB b: orphans.keySet()) {
			int[] stats = orphans.get(b);
			int hop = stats[0];
			int bw = stats[1];
			System.out.print( getTime(System.currentTimeMillis()) + ": ");
			addENodeB(b, hop, bw);
		}
		orphans.clear();
	}

	/**
	 * Removes the controller from the eNodeBs This acts as controller failure
	 */
	private void removeController() {
		for (ENodeB b : eNodeBs.keySet()) {
			b.setController(null, -100, -100);
		}
		eNodeBs.clear();

	}

	public boolean isAlive() {
		return isAlive;
	}
}
