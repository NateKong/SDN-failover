package entity;

import communication.Message;
import sdn.OvEnodeB;

import java.awt.Point;
import java.util.ArrayList;

import communication.QoS;

/**
 * A User Equipment
 *
 * @author Nathan Kong; Gary Su
 * @since Nov 2016
 *
 */
public class UE {
	private String name;
	private int mac;
	private int ip;
	private Point location;
	private ArrayList<Message> messages;
	private ArrayList<Message> finishedMessages;
	private OvEnodeB tower;

	public UE(String name, int mac, Point point) {
		this.name = name;
		this.mac = mac;
		this.ip = mac; //for CS258 only
		this.location = point;
		this.messages = new ArrayList<>();
		this.finishedMessages = new ArrayList<>();
		tower = null;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public Message removeFirstMessage() {
		return messages.remove(0);
	}

	public boolean messagesIsEmpty() {
		return messages.isEmpty();
	}

	/**
	 * Gets the final message from another UE
	 * @param m
	 */
	public void receiveMessage(Message m){
		finishedMessages.add(0,m);
	}

	/**
	 * Get location of UE
	 */
	public Point getLocation(){
		return location;
	}

	protected void setPoint(Point p){
		location = p;
	}

	public String getName(){
		return name;
	}

	public void requestAccess(OvEnodeB b){
		b.addUE(this);
		tower = b;
	}

	public int getIp(){
		return ip;
	}

	public OvEnodeB findclosestEB(ArrayList<OvEnodeB> enbs) {
		ArrayList<Double> distances = new ArrayList<Double>();
		int size = enbs.size();
		for (OvEnodeB enb: enbs){
			double x = (double) enb.getLocation().getX();
			double y = (double) enb.getLocation().getY();
			distances.add(Math.pow((Math.pow(location.getX()-x, 2) + Math.pow(location.getY()-y, 2)), 0.5));
		}
		double a=10000;
		int loc = 0;
		for (int i=0; i<size; i++){
			double b = distances.get(i);
			if (b < a){
				a = b;
				loc = i;
			}
		}
		return enbs.get(loc);
	}

	//Should automatically send to tower
	public void sendMessage(String message)
	{
		if (tower != null)
		{
			//Get tower name and pass that on to controller
			tower.relayMessage(message);
		}

	}

}
