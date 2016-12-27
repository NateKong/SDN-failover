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
	private ArrayList<Xtwo> connections;

	public ENodeB(int name, long startTime, long maxTime, ArrayList<String> log) {
		super(("eNodeB" + Integer.toString(name)), startTime, maxTime, log);
		connections = new ArrayList<Xtwo>();
	}

	public void addConnection(Xtwo x2) {
		connections.add(x2);
	}

	@Override
	public void run() {
		System.out.println("Running " + name);
		try {
			int i = 0;
			while (checkTime(System.currentTimeMillis())) {
				log.add(time(System.currentTimeMillis()) + ": " + name + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(random());
				i++;
			}
		} catch (InterruptedException e) {
			System.out.println( name + " interrupted.");
		}
		System.out.println(time(System.currentTimeMillis()) + ": " + name + " stopped");
	}
}
