package sdn;

import simulations.Message;
import communication.QoS;
import entity.UE;
import lte.SimplifiedEPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, OvEnodeB> getNodes() {
        return epc.getNodes();
    }

    public void addNodeB(String name, OvEnodeB node)
    {
        epc.addNodeB(name, node);
        node.setController(this);
    }

    public void addToQueue(QoS type)
    {
        epc.addToQueue(type);
    }

    public void addService(QoS serviceType, int bandwidth)
    {
        if(bandwidth > epc.RemainingBandwidth) {
            //throw an error
            System.out.print("Error adding service! Not enough bandwidth");
        }
        else {
            epc.addService(serviceType, bandwidth);
        }
    }

    public void adjustService(QoS serviceType, int bandwidth)
    {
        Map<QoS, Integer> services = epc.getServices();
        if (services.containsKey(serviceType)) {
            int currentBW = services.get(serviceType);
            int diff = currentBW - bandwidth;
            if (epc.RemainingBandwidth > diff) {
                epc.allocateBandwidth(diff);
                services.put(serviceType, bandwidth);
            }
            else {
                System.out.print("Error adjusting service. Not enough bandwidth");
            }
        }
    }

    public ArrayList<String> processMessagePath (OvEnodeB towerOrigin, Message message) {
        Map<String, OvEnodeB> nodes = epc.getNodes();
        OvEnodeB origTower = nodes.get(towerOrigin.getName());
        String dest = message.getDest();
//        Map<String, UE> targetUE = findEnodeBWithUEName(dest);
        ArrayList<String> targetUEInfo = findEnodeBWithUEName(dest);
        if (targetUEInfo == null) {
            return null;
        }
        else {
            return targetUEInfo;
        }
    }

    public void relayMessageDown(String ueName, Message message) {
        epc.getUEs().get(ueName).receiveMessage(message);
    }


    //returns tuple if result exists. Changed to ArrayList because of access
    //Results <OvEnodeb name, UE name>
    public ArrayList<String> findEnodeBWithUEName(String dest) {
        Map<String, OvEnodeB> nodes = epc.getNodes();
        for (Map.Entry<String, OvEnodeB> entry: nodes.entrySet()) {
            Map<String, UE> UeMap = entry.getValue().getUeMap();
            if (UeMap.containsKey(dest)) {
                UeMap.get(dest);
                entry.getValue();
                Map<String, UE> result = new HashMap<>();
                ArrayList<String> results = new ArrayList<>();
//                result.put(entry.getValue().getName(), UeMap.get(dest));
                results.add(entry.getValue().getName());
                results.add(UeMap.get(dest).getName());
                return results;
            }
            //Convert this to map for much better efficiency
        }
        return null;
    }

    //Service priority levels defined -- Basic, Premium, Superstar
    //Service priority will be utilized in message processing method










}
