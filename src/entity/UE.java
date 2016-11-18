package entity;

import java.awt.Point;

public class UE {
	private int mac;
	private int ip;
	private Point point;
	
	public UE(int mac, Point point){
		this.mac = mac;
		this.point = point;
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
}
