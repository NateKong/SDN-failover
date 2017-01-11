package failover;

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

	public Controller(int name, long maxTime) {
		super(("Controller" + Integer.toString(name)), maxTime);
		eNodeBs = new HashMap<ENodeB, Integer>();
		orphans = new HashMap<ENodeB, int[]>();
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
		int bw = 0;
		int numOfHops = 100;
		
		for (ENodeB b: eNodeBs.keySet()) {
			for (Xtwo x: b.getConnections()) {
				if (x.getEndpoint(b).equals(e)) {
					int hops = b.getCHops() + 1;
					if ( hops < numOfHops ) {
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
		int[] stats = {hops, bw};
		if (orphans.containsKey(b)) {
			int[] stat = orphans.get(b);
			if (stat[0] > hops && bw > stat[1]) {
				orphans.put(b, stats);
			}
		} else {
			orphans.put(b, stats);
		}
	}

	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime(System.currentTimeMillis()) + ": Running thread " + name);
		try {
			while (checkTime(System.currentTimeMillis())) {
				Thread.sleep(random());

				if (!orphans.isEmpty()) {
					adoptOrphans();
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
	}

	/**
	 * Adopt Orphans nodes that call out
	 * as orphans
	 */
	private void adoptOrphans() {
		for (ENodeB b: orphans.keySet()) {
			if ( !b.hasController() ) {
				int [] stats = orphans.get(b);
				addENodeB(b, stats[0], stats[1]);
			} else {
				int hops = b.getCHops(); // current hops
				int bw = b.getCbw(); // current bw
				int [] stats = orphans.get(b); // proposed hops and bw 
				
				if (stats[0] <= hops && stats[1] > bw) {
					System.out.print("UPGRADE: ");
					addENodeB(b, stats[0], stats[1]);
				}
			}
		}
		orphans.clear();
	}

	/**
	 * Removes the controller from the eNodeBs This acts as controller failure
	 */
	private void removeController() {
		for (ENodeB b : eNodeBs.keySet()) {
			b.setController(null, 1000, 1000);
		}
		eNodeBs.clear();

	}
}
