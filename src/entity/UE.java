package entity;

import sdn.OvEnodeB;

import java.awt.Point;

public class UE {
	private OvEnodeB parent;
	
	public UE(OvEnodeB tower){
		this.parent = tower;
		tower.addUE(this);
	}

	//Sends request to its parent
	public String sendRequest(QoS type, int bandWeight) {
		//parent.notify
		//Above method will notify controller the type of request that is sent out
		//Then controller will handle accordingly
		return "";
	}
}
