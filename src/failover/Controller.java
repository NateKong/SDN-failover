package failover;

/**
 * A Software Defined Network controller.
 * A controller for a LTE network
 * over eNodeBs.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class Controller extends Entity implements Runnable {
	private ArrayList<ENodeB> eNodeBs;
	private int load;


	public Controller(int name, int load, long startTime, long maxTime, ArrayList<String> log) {
		super( ("Controller" + Integer.toString(name)), startTime, maxTime, log);
		this.load = load;
		eNodeBs = new ArrayList<ENodeB>();
	}

	public void addENodeB(ENodeB e) {
		eNodeBs.add(e);
	}

	public void removeENodeB(ENodeB e) {
		eNodeBs.remove(e);
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
