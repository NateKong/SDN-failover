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
	private int backupBw;
	private int backupHops;
	private boolean initialFailure;

	public ENodeB(int name, long maxTime, int load, int domain, int bw, int hops) {
		super(("eNodeB" + Integer.toString(name)), maxTime, load);
		orphanMessages = new ConcurrentLinkedQueue<Message>();
		replyMessages = new ConcurrentLinkedQueue<Message>();
		this.domain = domain;
		this.bw = bw;
		this.hops = hops;
		this.backupBw = 0;
		this.backupHops = 100;
		initialFailure = true;
		// System.out.println(getName() + " is created");
	}

	/**
	 * Sets the controller for the eNodeB
	 * 
	 * @param c
	 *            is the new controller for the eNodeB
	 */
	public void setController(Controller c) {
		controller = c;
	}

	/**
	 * Emulates a table registry in the eNodeB to contact the controller the
	 * eNodeB knows to send all messages to Entity e, where e could be another
	 * eNodeB or the controller itself
	 * 
	 * @param e
	 *            the Entity to the controller
	 */
	public void setEntity(Entity e) {
		toController = e;
	}

	public boolean hasController() {
		return controller != null;
	}

	public boolean hasBkController() {
		return bkController != null;
	}

	/**
	 * Gets the domain of the eNodeB
	 */
	public int getDomain() {
		return domain;
	}

	/**
	 * Runs the thread ( thread.start() )
	 */
	@Override
	public void run() {
		//System.out.println(getTime() + ": Running thread " + name);

		// pauses the system to start at the same time
		while (time(System.currentTimeMillis()) < 1.0) {
		}

		try {
			while (checkTime(System.currentTimeMillis())) {
				Thread.sleep(random());

				// eNodeB becomes an orphan
				if (controller == null) {
					if(initialFailure){
						Thread.sleep(5000);
						initialFailure = false;
						System.out.println(getTime() + ": " + name  + " is an orphan");
					}
					orphanNode();
				}

				// needs a backup controller
				if (bkController == null) {
					// System.out.println(getTime() + ": " + name + " needs a backup");
					getBackup();
				}

				// processes messages from orphan to controller
				while (!orphanMessages.isEmpty() ) {
					Message m = orphanMessages.poll();
					ENodeB e = m.getOrphan();
					if (domain == e.getDomain() && toBkController != null) {
						m = findConnectionBw(m, true);
						toBkController.messageController(m);
					} else if (domain != e.getDomain() && controller != null) {
						m = findConnectionBw(m, false);
						toController.messageController(m);
					}
				}

				// pass message from controller to orphan
				while (!replyMessages.isEmpty()) {
					Message m = replyMessages.poll();

					if (m.atOrphan()) {
						ENodeB orphan = m.getOrphan();
						if (!orphan.hasController()) {
							orphan.acceptAdoption(domain);
						} else if (orphan.hasController()){
							orphan.acceptBackup(m, this);
						}
					} else {
						ENodeB e = m.removeBreadcrumb();
						e.replyMessage(m);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//System.out.println(getTime() + ": Closing thread " + name);
	}

	/**
	 * call out to backup controller
	 */
	private void orphanNode() {
		Message newController = new Message(this);
		toBkController.messageController(newController);
	}

	/**
	 * calls out to other connected eNodeBs and inform them this eNodeB needs a
	 * backup
	 */
	private void getBackup() {
		for (Connection c : connections) {
			Entity b = c.getEndpoint(this);
			if (!b.equals(controller)) {
				Message orphanBroadcast = new Message(this);
				orphanBroadcast.setBw(c.getBw());
				b.messageController(orphanBroadcast);
				// System.out.println(getTime() + ": " + name + " broadcasts message to " + b.getName());
			}
		}
	}

	/**
	 * Sends a message to the controller
	 * 
	 * @param eNodeB
	 */
	public void messageController(Message orphanMessage) {
		orphanMessage.addBreadcrumb(this);
		orphanMessages.add(orphanMessage);
		// System.out.println(name + " receives message from " + orphanMessage.getOrphan().getName());
	}

	/**
	 * Adds messages for adoption
	 * 
	 * @param adoptMessage
	 */
	public void replyMessage(Message adoptMessage) {
		replyMessages.add(adoptMessage);
		// System.out.println(name + " sends reply to " + adoptMessage.getOrphan().getName());
	}

	/**
	 * Finds the lowest bandwidth for the path
	 * 
	 * @param message
	 *            the message from the orphan
	 * @param isBackup
	 *            is a boolean to determine which direction to go to.
	 * @return the messages with the lowest bandwidth for the path
	 */
	private Message findConnectionBw(Message message, boolean isBackup) {
		Entity e = isBackup ? toBkController : toController;
		for (Connection c : connections) {
			if (c.getEndpoint(this).equals(e)) {
				// update to lowest bandwidth
				int messageBw = message.getBw();
				int connectionBw = c.getBw();
				if (connectionBw < messageBw) {
					message.setBw(connectionBw);
				}
			}
		}
		return message;
	}

	/**
	 * Accepts the message from the Controller Assigns a backup controller
	 * 
	 * @param c
	 *            is the backup controller
	 * @param e
	 *            is the eNodeB to get to the backup controller
	 */
	private void acceptBackup(Message m, ENodeB e) {
		Controller c = m.getController();
		int messageBw = m.getBw();
		int messageHops = m.getHops();

		if (!c.equals(controller)) {
			if (bkController == null) {
				bkController = c;
				backupBw = messageBw;
				backupHops = messageHops;
				System.out.println(getTime() + ": " + c.getName() + " is the backup for " + name + "\tBW: " + backupBw
						+ "\thops: " + backupHops);
				toBkController = e;
			} else if (messageHops <= backupHops && messageBw > backupBw) {
				bkController = c;
				backupBw = messageBw;
				backupHops = messageHops;
				System.out.println(getTime() + ": " + c.getName() + " UPGRADES for the backup position on " + name
						+ "\tBW: " + backupBw + "\thops: " + backupHops);
				toBkController = e;
				
				for (Connection conn: connections) {
					Entity entity = conn.getEndpoint(this);
					entity.upgrade(conn.getBw(), backupBw, backupHops+1, this);
				}
			}
		}
	}

	/**
	 * Accepts the adoption. Switches the backup controller to the controller
	 */
	private void acceptAdoption(int domainNum) {
		controller = bkController;
		bkController = null;
		toController = toBkController;
		toBkController = null;
		domain = domainNum;
		bw = backupBw;
		hops = backupHops;
		backupBw = 0;
		backupHops = 100;
		System.out.println(getTime() + ": " + controller.getName() + " adopts " + name + "\tBW:  " + bw + "\thops: " + hops);
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
		if (upgradeHops <= backupHops && newBw >= backupBw) {
			Message upgradeMessage = new Message(this);
			upgradeMessage.setBw(newBw);
			eNodeB.messageController(upgradeMessage);
			//System.out.println(name + " send upgrade message to " + eNodeB.getName());
		}
	}
	
}
