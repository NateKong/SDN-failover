package failover;

/**
 * An eNodeB in a LTE
 * architecture. The eNodeB
 * is controlled using SDN.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.concurrent.ConcurrentLinkedQueue;

public class ENodeB extends Entity implements Runnable {
	private Controller controller;
	private Controller bkController;
	private Entity toController;
	private Entity toBkController;
	private ConcurrentLinkedQueue<Message> orphanMessages;
	private ConcurrentLinkedQueue<Message> replyMessages;
	private int domain;
	private int bw;
	private int hops;
	
	public ENodeB(int name, long maxTime, int load, int domain, int bw, int hops) {
		super(("eNodeB" + Integer.toString(name)), maxTime, load);
		orphanMessages = new ConcurrentLinkedQueue<Message>();
		replyMessages = new ConcurrentLinkedQueue<Message>();
		this.domain = domain;
		this.bw = bw;
		this.hops = hops;
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
	
	public boolean hasController(){
		return controller !=null;
	}
	
	public boolean hasBkController() {
		return bkController != null;
	}
	
	/**
	 * Gets the domain of the eNodeB
	 */
	public int getDomain(){
		return domain;
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
				if (controller == null) {
					orphanNode();
				}
				
				// needs a backup controller
				if ( bkController == null ) {
					//System.out.println(getTime() + ": " + name + " is an orphan");
					getBackup();
				}
				
				// processes messages from orphan to controller
				while (!orphanMessages.isEmpty() && controller != null){
					Message m = orphanMessages.poll();
					ENodeB e = m.getOrphan();
					if (domain == e.getDomain() && toBkController != null){
						toBkController.messageController(m);
					}else if (domain != e.getDomain()){
						toController.messageController(m);	
					}
				}
				
				// pass message from controller to orphan
				while ( !replyMessages.isEmpty() ) {
					Message m = replyMessages.poll();
					if( m.atOrphan() ){
						ENodeB orphan = m.getOrphan();
						if(!orphan.hasBkController()){
							orphan.acceptBackup(m, this);
							//if (m.getOrphan().getName().equals("eNodeB4")) {System.out.println(getTime() + ": " + name + " sends adoption message from " + m.getController().getName() + " to " + orphan.getName());}
						}else if (!orphan.hasController()){
							orphan.acceptAdoption(domain);
						}
					}else{
						ENodeB e = m.removeBreadcrumb();
						e.replyMessage(m);
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
	 * call out to backup controller
	 */
	private void orphanNode() {
		Message newController = new Message(this);
		toBkController.messageController(newController);
		//if (name.equals("eNodeB7")) {System.out.println(getTime() + ": " + name + " broadcasts message to " + b.getName());}	
	}
	
	/**
	 * calls out to other connected eNodeBs 
	 * and inform them this eNodeB needs
	 * a backup
	 */
	private void getBackup() {
		for (Connection c : connections) {
			Entity b = c.getEndpoint(this);
			if (!b.equals(controller)) {
			Message orphanBroadcast = new Message(this);
			b.messageController(orphanBroadcast);
			//if (name.equals("eNodeB7")) {System.out.println(getTime() + ": " + name + " broadcasts message to " + b.getName());}
			}
		}
	}

	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(Message orphanMessage) {
		//if(orphanMessage.getOrphan().getName().equals("eNodeB1") && name.equals("eNodeB2")){ System.out.println(name + " receives message from eNB1");; }
		orphanMessage.addBreadcrumb(this);
		orphanMessages.add(orphanMessage);
	}
	
	/**
	 * Adds messages for adoption
	 * @param adoptMessage
	 */
	public void replyMessage(Message adoptMessage) {
		//System.out.println(name + " receives message");
		replyMessages.add(adoptMessage);
	}
	
	/**
	 * Accepts the message from the Controller
	 * Assigns a backup controller
	 * @param c is the backup controller
	 * @param e is the eNodeB to get to the backup controller
	 */
	private void acceptBackup(Message m, ENodeB e) {
		Controller c = m.getController();
		if (bkController == null && !c.equals(controller) ) {
			bkController = c;
			System.out.println(getTime() + ": " + c.getName() + " is the back up for " + name);
			toBkController = e;
		}else if (bkController != null && !c.equals(controller) ) {
			if () {
				bkController = c;
				System.out.println(getTime() + ": " + c.getName() + " is the back up for " + name);
				toBkController = e;	
			}
		}
	}
	
	/**
	 * Accepts the adoption.
	 * Switches the backup controller
	 * to the controller
	 */
	private void acceptAdoption(int domainNum){
		controller = bkController;
		bkController = null;
		toController = toBkController;
		toBkController = null;
		domain = domainNum;
		System.out.println(getTime() + ": " + controller.getName() + " adopts " + name);
	}
}
