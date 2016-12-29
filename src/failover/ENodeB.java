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
	private Controller backupController;

	public ENodeB(int name, long maxTime) {
		super(("eNodeB" + Integer.toString(name)), maxTime);
		connections = new ArrayList<Xtwo>();
		System.out.println(getName() + " is created");
		backupController = null;
		setupBackup();
	}

	/**
	 * Adds a connection to other eNodeBsgit 
	 * 
	 * @param x2
	 *            the connection between eNodeBs
	 */
	public void addConnection(Xtwo x2) {
		connections.add(x2);
	}

	/**
	 * Sets the controller for the eNodeB
	 * 
	 * @param c is the new controller for the eNodeB
	 */
	public void setController(Controller c) {
		controller = c;
	}

	/**
	 * Sets the controller for the eNodeB
	 * 
	 * @param c is the new controller for the eNodeB
	 */
	public void setBackupController(Controller c) {
		System.out.println(c.getName() + " is the backup controller for " + name);
		backupController = c;
	}

	/**
	 * Determines if the eNodeB has a controller
	 * 
	 * @return true is the eNodeB has a controller
	 */
	public boolean hasController() {
		return controller != null;
	}
	
	
	/**************************************************************/
	
	/**
	 * Calls out to other connected eNodeBs and 
	 * determine backup controller 
	 */
	private void setupBackup(){
		while (backupController != null) {
			for (Xtwo x2 : connections) {
				ENodeB b = x2.getEndpoint(this);
				if (!b.sameController(controller) ) {
					b.messageController(this);					
				}
			}
		}
	}

	/**
	 * Determines if the eNodeB has a controller
	 * 
	 * @return true is the eNodeB has a controller
	 */
	public boolean sameController(Controller c) {
		return controller == c;
	}
	
	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(ENodeB eNodeB) {
		if ( hasController() ) {
			controller.addBackup(eNodeB);
		}
	}
	
	
	/**************************************************************/	
	
	
	/**
	 * Determines is the eNodeB has a controller
	 * 
	 * @return true is the eNodeB has a controller
	 */
	public boolean hasBackupController() {
		return backupController != null;
	}
	
	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime(System.currentTimeMillis()) + ": Running thread " + name);

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

		System.out.println(getTime(System.currentTimeMillis()) + ": " + name + " finished");
	}

	private void orphanNode() {
		//ask controller to adopt
			//method will send this enodeb
			//Controller will add enodeb to its list, make it the controller and null the backup controller  
		if (backupController != null) {
			backupController.addOrphan(this);
		}
	}
	
}
