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
import java.util.concurrent.ConcurrentLinkedQueue;

public class ENodeB extends Entity implements Runnable {
	private Controller controller;
	private Entity toController;
	//private ArrayList<Message> orphanMessages;
	//private ArrayList<Message> adoptionMessages;
	private ConcurrentLinkedQueue<Message> orphanMessages;
	private ConcurrentLinkedQueue<Message> adoptionMessages;
	
	public ENodeB(int name, long maxTime, int load) {
		super(("eNodeB" + Integer.toString(name)), maxTime, load);
		orphanMessages = new ConcurrentLinkedQueue<Message>();
		adoptionMessages = new ConcurrentLinkedQueue<Message>();
		//System.out.println(getName() + " is created");
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
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		System.out.println(getTime() + ": Running thread " + name);
		
		//pauses the system to start at the same time
		while ( time(System.currentTimeMillis() ) < 1.0 ) {	}

		try {
			while (checkTime(System.currentTimeMillis())) {
				Thread.sleep(random());
				//eNodeB becomes an orphan
				if ( controller == null ) {
					//System.out.println(getTime() + ": " + name + " is an orphan");
					orphanNode();
				}
				
				// pass message from orphan to controller
				while (!orphanMessages.isEmpty() && controller != null){
					Message m = orphanMessages.poll();
					toController.messageController(m);
				}
				
				// pass message from controller to orphan
				while ( !adoptionMessages.isEmpty() ) {
					Message m = adoptionMessages.poll();
					if( m.atOrphan() ){
						ENodeB orphan = m.getOrphan();
						orphan.acceptAdoption(m.getController(), this);
						//if (m.getOrphan().getName().equals("eNodeB4")) {System.out.println(getTime() + ": " + name + " sends adoption message from " + m.getController().getName() + " to " + orphan.getName());}
					}else{
						ENodeB e = m.removeBreadcrumb();
						e.sendAdoptionMessage(m);
						//if (m.getOrphan().getName().equals("eNodeB4")) {System.out.println(getTime() + ": " + name + " sends adoption message from " + m.getController().getName() + " to " + e.getName());}
					}
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
			Message orphanBroadcast = new Message(this);
			b.messageController(orphanBroadcast);
			//if (name.equals("eNodeB7")) {System.out.println(getTime() + ": " + name + " broadcasts message to " + b.getName());}
		}
	}

	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(Message orphanMessage) {
		//if(orphanMessage.getOrphan().getName().equals("eNodeB4")){ System.out.println(name + " receives message from eNB4");; }
		orphanMessage.addBreadcrumb(this);
		orphanMessages.add(orphanMessage);			
	}
	
	/**
	 * Adds messages for adoption
	 * @param adoptMessage
	 */
	public void sendAdoptionMessage(Message adoptMessage) {
		adoptionMessages.add(adoptMessage);
	}
	
	private void acceptAdoption(Controller c, ENodeB e) {
		if (controller == null) {
			controller = c;
			System.out.println(getTime() + ": " + c.getName() + " adopts " + name);
			
			toController = e;
		}
	}
}
