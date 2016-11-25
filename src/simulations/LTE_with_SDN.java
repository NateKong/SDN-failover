package simulations;

import communication.QoS;
import entity.UE;
import lte.SimplifiedEPC;
import sdn.Controller;
import sdn.OvEnodeB;

import java.awt.*;

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

        OvEnodeB node1 = new OvEnodeB("hi", new Point(1, 1));
        OvEnodeB node2 = new OvEnodeB("hey", new Point(2,2));
        OvEnodeB node3 = new OvEnodeB("ho", new Point(3,3));

        //Initialize UEs
        UE ue1 = new UE("Hey", 1, new Point(3, 5));
        UE ue2 = new UE("Nana", 2, new Point(4,5));
        UE ue3 = new UE("Howie", 3, new Point(6,9));
        UE ue4 = new UE("Johnny", 4, new Point(7, 3));

        //Initialize simplified EPC (which is aggregate of SGW, PGW, and MME) anonymously only when controller exists
        //Initialize SDN on top of simplified EPC
        Controller control = new Controller(new SimplifiedEPC());

        control.addNodeB("LA", node1);
        control.addNodeB("SD", node2);
        control.addNodeB("SJ", node3);

//        control.addService(QoS.Background, 50);
//        control.addService(QoS.Conversational, 50);
//        control.addService(QoS.Interactive, 50);
//        control.addService(QoS.Streaming, 100);
        control.addService(QoS.transmission, 50);

        //run simulation
        //This involves timing mechanisms that would need to be implemented for a certain time


        //UE sends connect signal to OvEnodeB
        //Connection is successful

        //UE wants to communicate with another UE
        //UE sends request to OvEnodeB
        //OvEnodeB forwards the message to SDN controller
        //Controller parses message and then logically determines path
        //Controller sends back the routing information to OvEnodeB
        //OvEnodeB simply follows the path it is given so it needs a simple forwarding method
        //OvEnodeB may need a history of its path as it travels to establish the
        // connection but it can be assumed controller already calculated path
        //UE established connection needs to be a class that shows this
        //Do established connections have an effect on the OvEnodeB?


    }


}
