package simulations;

import communication.QoS;
import entity.UE;
import lte.SimplifiedEPC;
import sdn.Controller;
import sdn.OvEnodeB;

import java.awt.*;
import java.util.ArrayList;

/**
 *  @author Gary Su
 *  @since Nov 2016
 */
public class LTE_with_SDN {

    //UEs do not NEED to communicate with each other in this scenario
    //For now, the handling of the OvEnodeB backhaul link to PGW with SDN is the main interest
    //Assume each link between SGW and SDN is where the capacity for each type of QoS is defined.
    //Should be noted that in the Evolved Packet System (EPS), the bearer model is first point of QoS

    public static void main(String args[])
    {

        //Initialize eNodeBs or OvEnodeB
        ArrayList<OvEnodeB> nodes = new ArrayList<>();
        OvEnodeB node1 = new OvEnodeB("LA", new Point(1, 1));
        nodes.add(node1);
        OvEnodeB node2 = new OvEnodeB("SD", new Point(2,2));
        nodes.add(node2);
        OvEnodeB node3 = new OvEnodeB("SJ", new Point(3,3));
        nodes.add(node3);

        Controller control = new Controller(new SimplifiedEPC());
        for (OvEnodeB node: nodes) {
            control.addNodeB(node.getName(), node);
        }

        //        * M denotes this is a mobile device
//        * I denotes this is an IoT device
        //MConversational, MStreaming, MInteractive, MBackground, transmission, IStreaming, IInteractive, IBackground, Itransmission
        control.addService(QoS.MConversational, 50);
        control.addService(QoS.MStreaming, 50);
        control.addService(QoS.MInteractive, 50);
        control.addService(QoS.IStreaming, 100);
        control.addService(QoS.IInteractive, 100);
        control.addService(QoS.transmission, 50);

        //Initialize UEs
        ArrayList<UE> UEs = new ArrayList<>();
        UEs.add(new UE("Hey", 1, new Point(3, 5)));
        UEs.add(new UE("Nana", 2, new Point(4,5)));
        UEs.add(new UE("Howie", 3, new Point(6,9)));
        UEs.add(new UE("Johnny", 4, new Point(7, 3)));

        for (UE u: UEs) {
            OvEnodeB closestNode = u.findclosestEB(nodes);
            u.requestAccess(closestNode);
        }

        //Initialize simplified EPC (which is aggregate of SGW, PGW, and MME) anonymously only when controller exists
        //Initialize SDN on top of simplified EPC

        //run simulation
        //This involves timing mechanisms that would need to be implemented for a certain time
        

        //UE sends connect signal to OvEnodeB
        //Connection is successful

        //UE wants to communicate with another UE
        //UE sends request to OvEnodeB
        //OvEnodeB forwards the message to SDN controller
        //Controller parses message and then logically determines path //simplify
        //Controller sends back the routing information to OvEnodeB //simplify
        //OvEnodeB simply follows the path it is given so it needs a simple forwarding method
        //Can be assumed controller already calculated path
        //UE established connection needs to be a class that shows this
        //Do established connections have an effect on the OvEnodeB to keep a communication line open?
        //Or does the established connection (IP-based) just rely purely on internet resource?
        //Focus should be on bandwidth adjustment due to traffic volume
        //Focus on mechanisms for resource/bandwidth allocation


    }


}
