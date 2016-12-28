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
	private long startTime;
	private ArrayList<Xtwo> connections; // a list of connections to other eNodeBs
	private Controller controller;

	public ENodeB(int name, long maxTime, ArrayList<String> log) {
		super(("eNodeB" + Integer.toString(name)), maxTime, log);
		connections = new ArrayList<Xtwo>();
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
	
	public void setController(Controller c) {
		controller = c;
	}
	
	public boolean hasController() {
		return !controller.equals(null);
	}

	@Override
	public void run() {
		System.out.println("Running " + name);
		
		while (checkTime(System.currentTimeMillis())) {
			
			if( hasController() ) {
				//log.add( getTime(System.currentTimeMillis()) + ": " + name + " is an orphan");
				System.out.println( getTime(System.currentTimeMillis()) + ": " + name + " is an orphan");
				//orphanNode();
				
			}
			
			// Let the thread sleep for between 1-5 seconds
			try {
				Thread.sleep(random());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println( getTime(System.currentTimeMillis()) + ": " + name + " finished");
	}

	/**
	 * call out to other controllers through
	 * connected eNodeBs
	 */
	private void orphanNode() {
		for (Xtwo x2: connections) {
			ENodeB b = x2.getEndpoint(this);
			if (b.hasController()) {
				
			}
		}		
	}
}
