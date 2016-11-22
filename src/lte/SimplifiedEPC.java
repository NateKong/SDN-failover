package lte;

import entity.QoS;
import sdn.OvEnodeB;

import java.util.*;

/**
 * Created by GSU on 11/22/2016.
 */
//This is the aggregate of the SGW, PGW, and MME but will only implement a very small portion
    //of the functionalities
//Also will be treating this class as a datacenter in physical sense
public class SimplifiedEPC {

    //Assume that knowledge/data of UEs are mapped here and controller must send information
    //to which UE is to be contacted
    //Assuming that there are only two entities, each request involves two UEs
    //This means that each request involves two UEs

    //Has list of request for the controller to handle

    //private Map<String, String> UEList;
    //private ArrayList<OvEnodeB> eNodeBList;
    private static final int MAX_BANDWIDTH = 300; //Has a maximum bandwidth
    private Map<String, OvEnodeB> eNodeBMap;
    private Queue<QoS> requestQueue;

    //Has a set bandwidth for different types of QoS traffic
    //Should have a bandwidth for each QoS
    private ArrayList<Map<String, Integer>> bandwidths; // Will need to create a Bandwidth class with name and value

    //Assume that initialization of simplified EPC is zero
    public SimplifiedEPC()
    {
        eNodeBMap = new HashMap<>();
        // implementation of priorities can be done later. For now assume order of request is priority.
        requestQueue = new PriorityQueue<>();
        bandwidths = new ArrayList<>();

    }







}
