package sdn;

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





}
