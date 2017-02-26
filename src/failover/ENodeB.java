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
	private HashMap<ENodeB, HashMap> forwardMessage;

	public ENodeB(int name, int hops, long maxTime) {
		super(("eNodeB" + Integer.toString(name)), maxTime);
		connections = new ArrayList<Xtwo>();
		message = new HashMap<ENodeB, Integer>();
		forwardMessage = new HashMap<ENodeB, HashMap>();
		cHops = hops;
		backupController = null;
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
	
	/**
	 * Gets a list of X2 connections
	 * @return 
	 */
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
	 * Sets the backup controller for the eNodeB
	 * 
	 * @param c is the new backup controller for the eNodeB
	 */
	public void setBackupController(Controller c, int hops, int bw, boolean upgrade) {
		backupController = c;
		backupHops = hops;
		backupBW = bw;
		System.out.println(getTime(System.currentTimeMillis()) + ": " + c.getName() + 
				" is the backup controller for " + name + "\thop: " + hops + "\tbw " + bw);
		
		if(upgrade) {
			reSetupBackup();
		}
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
	 * Determines if the eNodeB controllers are the same
	 * 
	 * @return true if the eNodeB has a controller
	 */
	public boolean sameController(Controller c) {
		return controller == c;		
	}
	
	/**
	 * Determines is the eNodeB has a backup controller
	 * 
	 * @return true is the eNodeB has a backup controller
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
	 * Determines if the backup controller is alive
	 * @return true is the backup controller is alive
	 */
	public boolean backupControllerAlive() {
		return backupController.isAlive();
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
				//controller load
				else if(name.equals("eNodeB3") | name.equals("eNodeB4") | name.equals("eNodeB5")){
					System.out.println(getTime(System.currentTimeMillis()) + ": " + name + " checks if backup controller exists");
				}
				
				// Let the thread sleep for between 1-5 seconds
				Thread.sleep(random());
				
				if (!message.isEmpty() && hasController() ) {
					for (ENodeB b: message.keySet()) {
						findController(b, message.get(b));
					}
					message.clear();
				}
				
				if (!forwardMessage.isEmpty() && hasBackupController() ) {
					for (ENodeB e: forwardMessage.keySet()) {
						HashMap<Integer, Boolean> hm = forwardMessage.get(e);
						for (Integer bw: hm.keySet()) {
							boolean backup = hm.get(bw);
							messageController(e, bw, backup);
						}
					}
				}
				
				// check if has controller
				if ( !hasController() || !controller.isAlive()) {
					System.out.println(getTime(System.currentTimeMillis()) + ": " + name + " is an orphan");
					orphanNode();
				}
				
				if (controller != null && controller.equals(backupController)) {
					backupController = null;
				}
				
				if ( backupController != null && !backupController.isAlive() ) {
					backupController = null;
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(getTime(System.currentTimeMillis()) + ": " + "Closing thread " + name);
	}
	
	/**
	 * Resends messages if controller is upgraded.
	 */
	private void reSetupBackup() {
		for (Xtwo x2: connections) {
			ENodeB e = x2.getEndpoint(this);
			e.setupBackup();
		}
	}
	
	/**
	 * Calls out to other connected eNodeBs to 
	 * determine backup controller 
	 */
	public void setupBackup(){
		for (Xtwo x2 : connections) {
			findController(x2.getEndpoint(this), x2.getBW() );
		}
	}

	/**
	 * Determines which controller should be the backup controller 
	 */
	private void findController (ENodeB b, int bw) {
		//different main controllers; message the other main controller
		if ( b.hasController() && !b.sameController(controller) ) {
			b.messageController(this, bw, false);
		} 
		//same main controllers; message backup controller
		else if (b.hasBackupController() && !b.sameAsBackupController(controller) ) {
			b.messageController(this, bw, true);
		} else {
			//same main controller and backup is null
			message.put(b, bw); 
		}
	}
	
	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB is the eNodeB sending the message
	 * @param X2bw the bandwidth of the X2
	 * @param messageBackup is true if the backup controller should be messaged
	 */
	public void messageController(ENodeB eNodeB, int X2bw, boolean messageBackup) {
		if ( !messageBackup && controller.isAlive() ) {
			int bw = (X2bw > cBW) ? cBW : X2bw;
			controller.addBackup(eNodeB, cHops + 1, bw );
		} else if (messageBackup && backupController.isAlive() ) {
			int bw = (X2bw > backupBW) ? backupBW : X2bw;
			backupController.addBackup(eNodeB, backupHops + 1, bw );
		} else {
			HashMap<Integer, Boolean> hm = new HashMap<Integer, Boolean>();
			hm.put(X2bw, messageBackup);
			forwardMessage.put(eNodeB, hm);
		}
	}
	
	/**
	 * Calls controller if eNodeB is a orphan
	 */
	private void orphanNode() {
		if (backupController != null) {
			backupController.addOrphan(this, backupHops, backupBW);
		}
	}
	
}
