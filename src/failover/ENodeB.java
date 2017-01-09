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
	//private HashMap<ENodeB, HashMap> message;
	private HashMap<ENodeB, Integer> message;

	public ENodeB(int name, int hops, long maxTime) {
		super(("eNodeB" + Integer.toString(name)), maxTime);
		connections = new ArrayList<Xtwo>();
		//message = new HashMap<ENodeB, HashMap>();
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
	 * Sets the backup controller for the eNodeB
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
	 * Determines if the eNodeB controllers are the same
	 * 
	 * @return true if the eNodeB has a controller
	 */
	public boolean sameController(Controller c) {
		return controller == c;		
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
				
				// Let the thread sleep for between 1-5 seconds
				Thread.sleep(random());
				
				if (!message.isEmpty() && hasController() ) {
					for (ENodeB b: message.keySet()) {
						//if(b.getName().equals("eNodeB2") ) {System.out.println(name + " sends to: " + b.getName() + "\tX2 bw: " + message.get(b) );}
						findController(b, message.get(b));
					}
					message.clear();
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
					System.out.println("backup down");
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(getTime(System.currentTimeMillis()) + ": " + "Closing thread " + name);
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

	private void findController (ENodeB b, int bw) {
		//different main controllers; message the other main controller
		if ( b.hasController() && !b.sameController(controller) ) {
			//if(b.getName().equals("eNodeB2") ) {System.out.println(name + " sends to: " + b.getName() + "\tX2 bw: " + bw + " : 1");}
			b.messageController(this, bw, false);
		} 
		//same main controllers; message backup controller
		else if (b.hasBackupController() && !b.sameAsBackupController(controller) ) {
			//if(b.getName().equals("eNodeB2") ) {System.out.println(name + " sends to: " + b.getName() + "\tX2 bw: " + bw + " : 2");}
			b.messageController(this, bw, true);
		} else {
			//same main controller and backup is null
			message.put(b, bw); 
		}
	}
	
	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(ENodeB eNodeB, int X2bw, boolean messageBackup) {
		if ( !messageBackup && hasController() ) {
			int bw = (X2bw > cBW) ? cBW : X2bw;
			controller.addBackup(eNodeB, cHops + 1, bw );
		} else if (messageBackup && hasBackupController() ) {
			int bw = (X2bw > backupBW) ? backupBW : X2bw;
			
			/*if(eNodeB.getName().equals("eNodeB0") ) {
				System.out.println(name + " receives from: " + eNodeB.getName() + "\tbw: " + bw +"\thops: " + (backupHops+1) + "\tsends to controller: " + backupController.getName() );
			}*/
			backupController.addBackup(eNodeB, backupHops + 1, bw );
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
