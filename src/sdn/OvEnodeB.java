package sdn;

import entity.UE;

import java.awt.*;
import java.util.ArrayList;

/**
 * 
 * @author Nathan Kong; Gary Su
 * @since Nov 2016
 */

// Assume static discovery and connection. Strictly means device specifically connected to this tower and that's it.
public class OvEnodeB {

    //This is now strictly a data plane object
    //Means that its forwarding should be adjusted by the controller
    //This should have a list of UEs that is maintains in its domain
    private Point point;
    private String name;
    private ArrayList<UE> UEList;
    private int bandwidthGiven;
    private static final int MAX_BANDWIDTH = 100;
    private Controller controller; // Assumes that each OvEnodeB directly knows of its controller
    //private ArrayList<OvEnodeB> neighbors; Can implement this later if needed

    //Each of these objects should know the neighbors that it is connected to

    //Only handles connections and forwarding
    public OvEnodeB(String name, Point point)
    {
        this.name = name;
        this.point = point;
        UEList = new ArrayList<>();
        bandwidthGiven = 0;
        //neighbors = new ArrayList<>();
    }

    public void addUE(UE e)
    {
        UEList.add(e);
    }

    public void setBandwidth(int bw) {
        bandwidthGiven = bw;
    }

    public int getBandwidth()
    {
        return bandwidthGiven;
    }

    public int getMaxBandwidth()
    {
        return MAX_BANDWIDTH;
    }

    public Point getLocation()
    {
        return point;
    }

    public String getName()
    {
        return name;
    }

    //This resets the bandwidthGiven to 0 to simulate turning off OvEnodeB
    public void turnOff()
    {
        bandwidthGiven = 0;
    }

    //Behavior should mimick closer to that of a switch
    public void relayMessage(String message)
    {
        //relays to neighbors if paths are given and if not, to controller for path computation and then
        //relays to another EnodeB defined by the controller
        controller.processMessagePath(this, message);
    }

    //Probably should have a return after controller computes
    public void delegateLogic()
    {
        //controller.process()
    }


}
