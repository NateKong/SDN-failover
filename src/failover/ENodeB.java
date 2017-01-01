package failover;

/**
 * An eNodeB in a LTE
 * architecture. The eNodeB
 * is controlled using SDN.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class ENodeB extends Entity implements Runnable {
	private ArrayList<Xtwo> connections; // a list of connections to other eNodeBs
	private Controller controller;
	private int cHops; // the number of hops from the eNodeB to the controller
	private int cBW; // the lowest throughput (Mbps) from the eNodeB to the controller

	public ENodeB(int name, int hops, long maxTime) {
		super(("eNodeB" + Integer.toString(name)), maxTime);
		connections = new ArrayList<Xtwo>();
		cHops = hops;
		System.out.println(getName() + " is created");
	}

	/**
	 * Adds a connection to other eNodeBs
	 * 
	 * @param x2 the connection between eNodeBs
	 */
	public void addConnection(Xtwo x2) {
		connections.add(x2);
	}

	public ArrayList<Xtwo> getConnections() {
		return connections;
	}
	
	public int getCHops() {
		return cHops;
	}
	
	public int getCbw() {
		return cBW;
	}
	
	/**
	 * Sets the controller for the eNodeB
	 * 
	 * @param c is the new controller for the eNodeB
	 */
	public void setController(Controller c, int hops, int bw) {
		controller = c;
		cHops = hops;
		cBW = bw;
		
		
		/*
		if(c == null) {
			controller = c;
			cHops = 0;
			cBW = 0;
		}else if (controller == null) {
			System.out.println("New Controller - setting " + c.getName() + " for " + name + "\thops: " + hops + "\tbw: "+ bw);
			controller = c;
			cHops = hops;
			cBW = bw;
			//System.out.println( name +" is null, " + c.getName() + " has bw: " + bw);
			//c.addENodeB(this, hops, bw);
		} else if (controller != null) {
			System.out.println("Upgrade Controller - setting " + c.getName() + " for " + name + "\thops: " + hops + "\tbw: "+ bw);
			if (cHops > hops && cBW < bw) {
				controller = c;
				cHops = hops;
				cBW = bw;
				System.out.println( name +" not null, "+c.getName()+" has bw: " + bw);
				c.addENodeB(this, hops, bw);
			}
		}
		*/
		
		
		
		
	}
	
	/**
	 * determines if the eNodeB has a controller
	 * 
	 * @return true if there is a controller
	 */
	public boolean hasController() {
		return !(controller == null);
	}
	
	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime(System.currentTimeMillis()) + ": Running thread " + name);
		
		//pauses the system to start at the same time
		while ( time(System.currentTimeMillis() ) < 1.0 ) {	}

		try {
			while (checkTime(System.currentTimeMillis())) {
				// Let the thread sleep for between 1-5 seconds
				Thread.sleep(random());

				if ( !hasController() ) {
					System.out.println(getTime(System.currentTimeMillis()) + ": " + name + " is an orphan");
					orphanNode();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(getTime(System.currentTimeMillis()) + ": " + "Closing thread " + name);
	}

	/**
	 * call out to other connected eNodeBs and inform them this eNodeB is an
	 * orphan.
	 */
	private void orphanNode() {
		for (Xtwo x2 : connections) {
			ENodeB b = x2.getEndpoint(this);
			//System.out.println(name + " sends message to: " + b.getName() + "\tX2 bw: " + x2.getBW() );
			b.messageController(this, x2.getBW());
		}
	}

	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(ENodeB eNodeB, int X2bw) {
		if ( hasController() ) {
			int bw = (X2bw > cBW) ? cBW : X2bw;
			//System.out.println(name + "\tcurrent bw: " + cBW + "\tasking bw:" + X2bw + "\tcalculated: " + bw);
			controller.addOrphan(eNodeB, cHops + 1, bw  );
		}
	}
}
