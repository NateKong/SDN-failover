package lte;

import sdn.OvEnodeB;

import java.util.*;

import communication.QoS;

/**
 * This is the aggregate of the SGW, PGW, and MME but will only implement a very
 * small portion of the functionalities Also will be treating this class as a
 * datacenter in physical sense
 * 
 * @author Gary Su
 * @since Nov 2017
 *
 */

public class SimplifiedEPC {

	// Assume that knowledge/data of UEs are mapped here and controller must
	// send information to which UE is to be contacted
	// Assuming that there are only two entities, each request involves two UEs
	// This means that each request involves two UEs

	// Has list of request for the controller to handle

	// private Map<String, String> UEList;
	// private ArrayList<OvEnodeB> eNodeBList;
	public static final int MAX_BANDWIDTH = 300; // Has a maximum bandwidth
	public int RemainingBandwidth;
	private Map<String, OvEnodeB> eNodeBMap; // This is only for faster access. name should come from node
	private Queue<QoS> requestQueue;

	// Has a set bandwidth for different types of QoS traffic
	// Should have a bandwidth for each QoS
	private ArrayList<Map<QoS, Integer>> services; // Will need to create a
													// Bandwidth class with name
													// and value

	// Assume that initialization of simplified EPC is empty and information is
	// added through controller
	public SimplifiedEPC() {
		eNodeBMap = new HashMap<>();
		// implementation of priorities can be done later. For now assume order
		// of request is priority.
		requestQueue = new PriorityQueue<>();
		services = new ArrayList<>();
		RemainingBandwidth = MAX_BANDWIDTH;
	}

	// Assumes unique string name
	public void addNodeB(String name, OvEnodeB n) {
		eNodeBMap.put(name, n);
	}

	public void addToQueue(QoS type) {
		requestQueue.add(type);
	}

	public void addService(QoS serviceType, int bandwidth) {
		Map<QoS, Integer> service = new HashMap<>();
		service.put(serviceType, bandwidth);
		allocateBandwidth(bandwidth);
		services.add(service);
	}

	public ArrayList getServices() {
		return services;
	}

	public void allocateBandwidth(int i) {
		RemainingBandwidth = RemainingBandwidth - i;
	}

}
