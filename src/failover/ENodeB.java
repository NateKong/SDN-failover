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
	private ArrayList<Xtwo> connections; // a list of connections to other eNodeBs
	private Controller controller;
	private int cHops; // the number of hops from the eNodeB to the controller
	private int cBW; // the lowest throughput (Mbps) from the eNodeB to the controller
	private Controller backupController;
	private int backupHops; // backup hops
	private int backupBW;  // back up bw
	private HashMap<ENodeB, Integer> message;

	public ENodeB(int name, int hops, long maxTime) {
		super(("eNodeB" + Integer.toString(name)), maxTime);
		connections = new ArrayList<Xtwo>();
		message = new HashMap<ENodeB, Integer>();
		cHops = hops;
		backupController = null;
		System.out.println(getName() + " is created");
	}

	/**
	 * Adds a connection to other eNodeBs 
	 * 
	 * @param x2
	 *            the connection between eNodeBs
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
	
	public int getBackupHops() {
		return backupHops;
	}
	
	public int getBackupBw() {
		return backupBW;
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
	 * Sets the controller for the eNodeB
	 * 
	 * @param c is the new controller for the eNodeB
	 */
	public void setBackupController(Controller c, int hops, int bw) {
		backupController = c;
		backupHops = hops;
		backupBW = bw;
		System.out.println(getTime(System.currentTimeMillis()) + ": " + c.getName() + " is the backup controller for " + name + "\thop: " + hops + "\tbw " + bw);
	}

	/**
	 * Determines if the eNodeB has a controller
	 * 
	 * @return true is the eNodeB has a controller
	 */
	public boolean hasController() {
		return controller != null;
	}
	
	/**
	 * Calls out to other connected eNodeBs and 
	 * determine backup controller 
	 */
	public void setupBackup(){
		while (backupController == null) {
			for (Xtwo x2 : connections) {
				ENodeB b = x2.getEndpoint(this);
				if (!b.sameController(controller) ) {
					b.messageController(this);					
				} else if (b.hasBackupController() && !b.sameAsBackupController(controller)) {
					b.messageBackupController(this);
				}
			}
		}
	}

	/**
	 * Determines if the eNodeB controllers are the same
	 * 
	 * @return true if the eNodeB has a controller
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
		if ( hasController() && controller.isAlive() == true ) {
			controller.addBackup(eNodeB);
		}
	}
	
	/**
	 * Sends a message to the back up controller
	 * 
	 * @param eNodeB
	 */
	public void messageBackupController(ENodeB eNodeB) {
		if ( hasController() && backupController.isAlive() == true ) {
			backupController.addBackup(eNodeB);
		}
	}
	
	/**
	 * Determines is the eNodeB has a back up controller
	 * 
	 * @return true is the eNodeB has a back up controller
	 */
	public boolean hasBackupController() {
		return backupController != null;
	}	
	
	/**
	 * Determines if the eNodeB Controller c 
	 * is the same as the back up controller
	 * 
	 * @return true if they are
	 */
	public boolean sameAsBackupController(Controller c) {
		return backupController == c;		
	}
	
	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime(System.currentTimeMillis()) + ": Running thread " + name);
		
		while ( time(System.currentTimeMillis() ) < 1.0 ) {
			
		}
		
		try {
			while (checkTime(System.currentTimeMillis())) {				
				// check for backup controller
				if (backupController == null) {
					setupBackup();	
				}
				
				// Let the thread sleep for between 1-5 seconds
				Thread.sleep(random());
				
				// check if has controller
				if ( !hasController() ) {
					System.out.println(getTime(System.currentTimeMillis()) + ": " + name + " is an orphan");
					orphanNode();
				}
				
				if (controller != null && controller.equals(backupController)) {
					backupController = null;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(getTime(System.currentTimeMillis()) + ": " + "Closing thread " + name);
	}

	/**
	 * Calls controller if eNodeB is a orphan
	 */
	private void orphanNode() {
		if (backupController != null) {
			backupController.addOrphan(this);
		}
	}
	
	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(ENodeB eNodeB, int X2bw) {
		//System.out.print(eNodeB.getName() + " sends message\tbw " + X2bw +"\t\t");
		//System.out.println(name + " receives message\thops: " + cHops + "\tbw: " + cBW );
		
		if ( hasController() ) {
			//System.out.println(name + "\tcurrent bw: " + cBW + "\tasking bw:" + X2bw + "\tcalculated: " + bw);
			int bw = (X2bw > cBW) ? cBW : X2bw;
			controller.addOrphan(eNodeB, cHops + 1, bw  );
		} else {
			message.put(eNodeB, X2bw);
		}
	}
	
}
