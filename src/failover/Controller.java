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
import java.util.HashMap;

public class Controller extends Entity implements Runnable {
	private ArrayList<ENodeB> eNodeBs;
	private HashMap<ENodeB, String> orphans; // orphan eNodeBs

	public Controller(int name, long maxTime, int load) {
		super(("Controller" + Integer.toString(name)), maxTime, load);
		eNodeBs = new ArrayList<ENodeB>();
		orphans = new HashMap<ENodeB, String>();
		System.out.println(getName() + " is created");
	}

	/**
	 * Adds an eNodeB to the controllers database. Sets the controller to the
	 * eNodeB
	 * 
	 * @param e an eNodeB (LTE tower)
	 */
	public void addENodeB(ENodeB e) {
		e.setController(this);
		eNodeBs.add(e);
		System.out.println(name + " adopts " + e.getName());
	}

	/**
	 * Adds to a list of orphan nodes
	 */
	public void addOrphan(ENodeB b) {
		orphans.put(b, "");
	}

	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime(System.currentTimeMillis()) + ": Running thread " + name);
		try {
			while (checkTime(System.currentTimeMillis())) {
				Thread.sleep(random(load));

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
		}
		
		System.out.println(getTime(System.currentTimeMillis()) + ": Closing thread " + name);

	}

	private void adoptOrphans() {
		for (ENodeB b: orphans.keySet()) {
			if ( !b.hasController() ) {
				b.setController(this);
				System.out.println(getTime(System.currentTimeMillis()) + ": " + name + " adopts " + b.getName());
			}
		}
		orphans.clear();
	}

	/**
	 * Removes the controller from the eNodeBs This acts as controller failure
	 */
	private void removeController() {
		for (ENodeB b : eNodeBs) {
			b.setController(null);
		}
		eNodeBs.clear();

	}
}
