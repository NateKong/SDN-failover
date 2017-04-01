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
	private Entity toController;
	private ConcurrentLinkedQueue<Message> orphanMessages;
	private ConcurrentLinkedQueue<Message> adoptionMessages;
	private int bw;
	private int hops;
	private boolean initialFailure;
	
	public ENodeB(int name, long maxTime, int load, int bw, int hops) {
		super(("eNodeB" + Integer.toString(name)), maxTime, load);
		this.bw = bw;
		this.hops = hops;
		orphanMessages = new ConcurrentLinkedQueue<Message>();
		adoptionMessages = new ConcurrentLinkedQueue<Message>();
		initialFailure = true;
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
		//System.out.println(getTime() + ": Running thread " + name);
		
		//pauses the system to start at the same time
		while ( time(System.currentTimeMillis() ) < 1.0 ) {	}

		try {
			while (checkTime(System.currentTimeMillis())) {
				Thread.sleep(random());
				//eNodeB becomes an orphan
				if ( controller == null ) {
					if (initialFailure){
						Thread.sleep(30000);
						initialFailure = false;
						System.out.println(getTime() + ": " + name + " is an orphan");
					}
					bw = 0;
					hops = 100;
					orphanNode();
				}
				
				// pass message from orphan to controller
				while ( !orphanMessages.isEmpty() && controller != null){
					Message m = orphanMessages.poll();
					//find the connection between this eNodeB and the next eNodeB
					for (Connection c: connections) {
						if (c.getEndpoint(this).equals(toController)){
							//update to lowest bandwidth
							int messageBw = m.getBw();
							int connectionBw = c.getBw();
							if (connectionBw < messageBw) {
								m.setBw(connectionBw);
							}
						}
					}
					toController.messageController(m);
					//if (m.getOrphan().getName().equals("eNodeB4")) {System.out.println(getTime() + ": " + name + " sends orphan message to " + toController.getName() + " from orphan " + m.getOrphan().getName() );}
				
				}
				
				// pass message from controller to orphan
				while ( !adoptionMessages.isEmpty() ) {
					Message m = adoptionMessages.poll();
					if( m.atOrphan() ){
						ENodeB orphan = m.getOrphan();
						orphan.acceptAdoption(m, this);
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

		//System.out.println(getTime() + ": Closing thread " + name);
	}

	/**
	 * call out to other connected eNodeBs and inform them this eNodeB is an
	 * orphan.
	 */
	private void orphanNode() {
		for (Connection c : connections) {
			Entity b = c.getEndpoint(this);
			Message orphanBroadcast = new Message(this);
			orphanBroadcast.setBw(c.getBw());
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
		//if(name.equals("eNodeB0") && orphanMessage.getOrphan().getName().equals("eNodeB2")){ System.out.println(name + " receives message from " + orphanMessage.getOrphan().getName()); }
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
	
	/**
	 * Accepts the adoption
	 * @param message
	 * @param e
	 */
	private void acceptAdoption(Message message, ENodeB e) {
		Controller c = message.getController();
		int messageBw = message.getBw();
		int messageHops = message.getHops();
		
		if (controller == null) {
			controller = c;
			bw = messageBw;
			hops = messageHops;
			System.out.println(getTime() + ": " + c.getName() + " adopts " + name + "\tBW: " + bw + "\thops: " +  hops);
			toController = e;
		}else if ( messageHops <= hops && messageBw > bw ) {
			controller = c;
			bw = messageBw;
			hops = messageHops;
			System.out.println(getTime() + ": " + c.getName() + " UPGRADES adoption for " + name + "\tBW: " + bw + "\thops: " +  hops);
			toController = e;
			for (Connection conn: connections) {
				Entity entity = conn.getEndpoint(this);
				if (entity != toController){
					((ENodeB) entity).upgrade(conn.getBw(), bw, hops+1, this);
				}
			}
		}
	}
	
	/**
	 * An upgrade message if another eNodeB got upgraded
	 * 
	 * @param connBw the bandwidth of the last connection
	 * @param upgradeBw the 
	 * @param upgradeHops
	 */
	public void upgrade(int connBw, int upgradeBw, int upgradeHops, ENodeB eNodeB) {
		int newBw = connBw;
		if (newBw > upgradeBw){
			newBw = upgradeBw;
		}
		if (upgradeHops <= hops && upgradeBw >= bw) {
			Message upgradeMessage = new Message(this);
			upgradeMessage.setBw(connBw);
			eNodeB.messageController(upgradeMessage);
			//System.out.println(name + " send upgrade message to " + eNodeB.getName());
		}
	}
}
