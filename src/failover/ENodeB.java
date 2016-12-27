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

	public ENodeB(int name, long startTime, long maxTime, ArrayList<String> log) {
		super(("eNodeB" + Integer.toString(name)), startTime, maxTime, log);
		connections = new ArrayList<Xtwo>();
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

	@Override
	public void run() {
		System.out.println("Running " + name);
		try {
			int i = 0;
			while (checkTime(System.currentTimeMillis())) {
				log.add( getTime(System.currentTimeMillis()) + ": " + name + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(random());
				i++;
			}
		} catch (InterruptedException e) {
			System.out.println( name + " interrupted.");
		}
		System.out.println( getTime(System.currentTimeMillis()) + ": " + name + " finished");
	}
}
