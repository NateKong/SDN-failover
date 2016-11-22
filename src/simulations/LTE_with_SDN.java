package simulations;

import entity.QoS;
import entity.UE;
import lte.SimplifiedEPC;
import sdn.Controller;
import sdn.OvEnodeB;

/**
 * Created by GSU on 11/21/2016.
 */
public class LTE_with_SDN {

    //UEs do not NEED to communicate with each other in this scenario
    //For now, the handling of the OvEnodeB backhaul link to PGW with SDN is the main interest
    //Assume each link between SGW and SDN is where the capacity for each type of QoS is defined.
    //Should be noted that in the Evolved Packet System (EPS), the bearer model is first point of QoS

    public static void main(String args[])
    {

        //Initialize eNodeBs or OvEnodeB

        OvEnodeB node1 = new OvEnodeB();
        OvEnodeB node2 = new OvEnodeB();
        OvEnodeB node3 = new OvEnodeB();

        //Initialize UEs
        UE ue1 = new UE(node1);
        UE ue2 = new UE(node2);
        UE ue3 = new UE(node3);
        UE ue4 = new UE(node1);

        //Initialize simplified EPC (which is aggregate of SGW, PGW, and MME) anonymously only when controller exists
        //Initialize SDN on top of simplified EPC
        Controller control = new Controller(new SimplifiedEPC());

        control.addNodeB("LA", node1);
        control.addNodeB("SD", node2);
        control.addNodeB("SJ", node3);

        control.addService(QoS.Background, 50);
        control.addService(QoS.Conversational, 50);
        control.addService(QoS.Interactive, 50);
        control.addService(QoS.Streaming, 100);
        control.addService(QoS.transmission, 50);

        //run simulation
        //This involves timing mechanisms that would need to be implemented for a certain time

    }


}
