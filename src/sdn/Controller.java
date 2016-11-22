package sdn;

import entity.QoS;
import lte.SimplifiedEPC;

/**
 * Software Defined Network Controller
 * Management of packets and QoS
 * 
 * @author Nathan Kong
 * @since Nov 2017
 */

public class Controller {

    private SimplifiedEPC epc;

    //This is where the logic for handling requests and data occurs
    //This is also where QoS can be adjust in terms of bandwidth
    //This is where allocation of bandwidth can be done
    //This is where the list of requests are processed

    //Controller also manages the OvEnodeBs

    //Must directly relate to an EPC
    public Controller(SimplifiedEPC epc)
    {
        this.epc = epc;
    }

    public void addNodeB(String name, OvEnodeB node)
    {
        epc.addNodeB(name, node);
    }

    public void addToQueue(QoS type)
    {
        epc.addToQueue(type);
    }

    public void addService(QoS serviceType, int bandwidth)
    {
        if(bandwidth > epc.RemainingBandwidth) {
            //throw an error
        }
        else {
            epc.addService(serviceType, bandwidth);
        }

    }

    public int getBandwidthFree()
    {
        return epc.RemainingBandwidth;
    }









}
