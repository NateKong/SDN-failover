package entity;

public class UE {
	private int mac;
	private int ip;
	
	public UE(int mac){
		this.mac = mac;
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
