package failover;

/**
 * An eNodeB in a LTE
 * architecture. The eNodeB
 * is controlled using SDN.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.HashMap;

public class ENodeB extends Entity implements Runnable {
	private Controller controller;
	private Entity toController;
	private HashMap<ENodeB, HashMap<ENodeB,ENodeB>> messages; // the first entity is where it originates from, the second is the messages sender

	public ENodeB(int name, long maxTime, int load) {
		super(("eNodeB" + Integer.toString(name)), maxTime, load);
		messages = new HashMap<ENodeB, HashMap<ENodeB,ENodeB>>();
		System.out.println(getName() + " is created");
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
	 * Emulates a table registry in the eNodeB
	 * to contact the controller the eNodeB knows to
	 * send all messages to Entity e, where e could be
	 * another eNodeB or the controller itself
	 * 
	 * @param e the Entity to the controller
	 */
	public void setEntity(Entity e) {
		toController = e;
	}
	
	/**
	 * determines if the eNodeB has a controller
	 * 
	 * @return true if there is a controller
	 */
	public boolean hasController() {
		return !(controller == null);
	}
	
	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime() + ": Running thread " + name);
		
		//pauses the system to start at the same time
		while ( time(System.currentTimeMillis() ) < 1.0 ) {	}

		try {
			while (checkTime(System.currentTimeMillis())) {
				//eNodeB becomes an orphan
				if ( !hasController() ) {
					System.out.println(getTime() + ": " + name + " is an orphan");
					orphanNode();
					Thread.sleep(random());
				}else {
					Thread.sleep(1);
				}
				// other eNodeBs pass message to controller
				if ( !messages.isEmpty() && hasController()){
					Thread.sleep(random());
					for (ENodeB e: messages.keySet()){
						toController.messageController(e, messages.get(e));
						System.out.println(getTime() + ": " + name + " sends " + e.getName() + " message to " + toController.getName());
					}
					messages.clear();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(getTime() + ": Closing thread " + name);
	}

	/**
	 * call out to other connected eNodeBs and inform them this eNodeB is an
	 * orphan.
	 */
	private void orphanNode() {
		for (Connection c : connections) {
			Entity b = c.getEndpoint(this);
			HashMap<ENodeB, ENodeB> map = new HashMap<ENodeB, ENodeB>();
			map.put(this, this);
			b.messageController(this, map);
			System.out.println(getTime() + ": " + name + " broadcasts message to " + b.getName());
		}
	}

	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(ENodeB orphan, HashMap<ENodeB,ENodeB> sender) {
			messages.put(orphan, sender);			
	}
}
