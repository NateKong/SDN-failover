package communication;

/**
 * A generic message sent by between entities and the network.
 * This can include transmissions for control or messages to 
 * other UEs.
 * e.g. IoT device send to enodeB, enodeb sends to controller
 * 
 * @author Nathan Kong
 * @since Nov 2017
 *
 */


public class Message {
	private QoS qos;
	private String message;

	Message(QoS qos, String message) {
		this.qos = qos;
		this.message = message;
	}

	public QoS getQoS() {
		return qos;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
}
