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
	private int load; // the current load of the system


	public Controller(int name, int load, long startTime, long maxTime, ArrayList<String> log) {
		super( ("Controller" + Integer.toString(name)), startTime, maxTime, log);
		this.load = load;
		eNodeBs = new ArrayList<ENodeB>();
	}

	/**
	 * Adds an eNodeB to the controllers database.
	 * Sets the controller to the eNodeB
	 * 
	 * @param e an eNodeB (LTE tower)
	 */
	public void addENodeB(ENodeB e) {
		e.setController(this);
		eNodeBs.add(e);
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
		System.out.println( getTime(System.currentTimeMillis()) + ": " + name + " stopped");

	}

}
