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
import java.util.HashMap;

public class ENodeB extends Entity implements Runnable {
	private ArrayList<Connection> connections; // a list of connections to other eNodeBs
	private Controller controller;
	private int cHops; // the number of hops from the eNodeB to the controller
	private int cBW; // the lowest throughput (Mbps) from the eNodeB to the controller
	private HashMap<ENodeB, Integer> message;

	public ENodeB(int name, int hops, long maxTime) {
		super(("eNodeB" + Integer.toString(name)), maxTime);
		connections = new ArrayList<Connection>();
		message = new HashMap<ENodeB, Integer>();
		cHops = hops;
		System.out.println(getName() + " is created");
	}

	/**
	 * Adds a connection to other eNodeBs
	 * 
	 * @param x2 the connection between eNodeBs
	 */
	public void addConnection(Connection x2) {
		connections.add(x2);
	}

	public ArrayList<Connection> getConnections() {
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
				
				if (!message.isEmpty() && hasController() ) {
					for (ENodeB b: message.keySet()) {
						int X2bw = message.get(b);
						int bw = (X2bw > cBW) ? cBW : X2bw;
						controller.addOrphan(b, cHops + 1, bw);
					}
					message.clear();
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
		for (Connection connection : connections) {
			ENodeB b = connection.getEndpoint(this);
			b.messageController(this, connection.getBW());
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
			controller.addOrphan(eNodeB, cHops + 1, bw  );
		} else {
			message.put(eNodeB, X2bw);
		}
	}
}
