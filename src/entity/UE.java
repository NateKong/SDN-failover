package entity;

import simulations.Message;
import sdn.OvEnodeB;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

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

//	for (Map.Entry<String, OvEnodeB> entry: nodes.entrySet()) {
//		System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//		Map<String, UE> UeMap = entry.getValue().getUeMap();
//		if (UeMap.containsKey(dest)) {
//			UeMap.get(dest);
//			entry.getValue();
//			Map<String, UE> result = new HashMap<>();
//			result.put(entry.getValue().getName(), UeMap.get(dest));
//			return result;
//		}
//		//Convert this to map for much better efficiency
//	}
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

	public OvEnodeB findclosestNode(Map<String, OvEnodeB> enbs) {
		ArrayList<Double> distances = new ArrayList<Double>();
		int size = enbs.size();
		String loc = "";
		double smallest = 100000;
		for (Map.Entry<String, OvEnodeB> enb: enbs.entrySet()){
			double x = (double) enb.getValue().getLocation().getX();
			double y = (double) enb.getValue().getLocation().getY();
			double distance = Math.pow((Math.pow(location.getX()-x, 2) + Math.pow(location.getY()-y, 2)), 0.5);
			if (distance < smallest) {
				smallest = distance;
				loc = enb.getValue().getName();
			}
		}

		return enbs.get(loc);
	}

	public String getTower() {
		return tower.getName();
	}

	//Should automatically send to tower
	public void sendMessage(Message message)
	{
		if (tower != null)
		{
			//Get tower name and pass that on to controller
			tower.relayMessageUp(message);
		}
		else {
			System.out.println("Error relaying message. UE is not connected to Tower");
		}

	}

}
