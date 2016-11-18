package entity;

/**
 * A packet sent by between entities and the network
 * e.g. IoT device sends data to 
 * 
 * @author Nathan Kong
 * @since Nov 2017
 *
 */

public class packet {
	private QoS qos;
	private String message;

	packet(QoS qos, String message) {
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
