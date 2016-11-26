package simulations;

import communication.QoS;

/**
 * A generic message sent by between entities and the network.
 * This can include transmissions for control or messages to 
 * other UEs.
 * e.g. IoT device send to enodeB, enodeb sends to controller
 * 
 * @author Nathan Kong; Gary Su
 * @since Nov 2016
 *
 */

public class Message {
	private QoS qos;
	private String message;
	private String dest; // Found through UE name string

	Message(String message, String dest) {
		this.message = message;
		this.dest = dest;
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

	public String getDest() {
		return dest;
	}

	//Destination must be set in order to be able to send a message
	public void setDest(String name) {
		dest = name;
	}


}
