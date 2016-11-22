package entity;

import sdn.OvEnodeB;

import java.awt.Point;

public class UE {
	private int mac;
	private int ip;
	private Point point;
	private OvEnodeB parent;
	
	public UE(int mac, Point point, OvEnodeB tower){
		this.mac = mac;
		this.point = point;
		this.parent = tower;
	}
	
	public int getMac(){
		return mac;
	}
	
	public int getIp(){
		return ip;
	}
	
	public void setIp(int ip){
		this.ip = ip;
	}

	//Sends request to its parent
	public String sendRequest(QoS type) {
		//parent.notify
		//Above method will notify controller the type of request that is sent out
		return "";
	}
}
