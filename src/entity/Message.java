package entity;

/**
 * A packet sent by between entities and the network
 * e.g. IoT device sends data to 
 * 
 * @author Nathan Kong
 * @since Nov 2017
 *
 */

//****************************************************
// This will need to be renamed we will need to figure
// out how we want to write the OSI layers
// QoS happens on layer 3
//****************************************************
/*
 * NOTES
 * LAYER 2: Datagram -> contains message(required), destination mac address (not required), and return mac address (required)
 * LAYER 3: Packet -> contains Datagram(required), destination ip addresss (required), and return ip address (required)
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
