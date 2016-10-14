// 
//                           SimuLTE
// 
// This file is part of a software released under the license included in file
// "license.pdf". This license can be also found at http://www.ltesimulator.com/
// The above file and the present reference are part of the software itself, 
// and cannot be removed from it.
// 
package lte.simulations.LTE_SDN;

import inet.networklayer.autorouting.ipv4.IPv4NetworkConfigurator;
import inet.networklayer.ipv4.RoutingTableRecorder;
import inet.nodes.ethernet.Eth100G;
import inet.nodes.ethernet.Eth10G;
import inet.nodes.ethernet.Eth10M;
import inet.nodes.inet.Router;
import inet.nodes.inet.StandardHost;
import lte.epc.SgwStandard;
import lte.stack.phy.ChannelControl.LteChannelControl;
import lte.corenetwork.binder.LteBinder;
import lte.corenetwork.nodes.Ue;
import lte.corenetwork.nodes.eNodeB;
import lte.epc.PgwStandardSimplified;


network SDN
{
    parameters:
        int numUe = default(1);
        @display("i=block/network2;bgb=991,558;bgi=background/budapest");
    submodules:
        channelControl: LteChannelControl {
            @display("p=50,25;is=s");
        }
        routingRecorder: RoutingTableRecorder {
            @display("p=50,75;is=s");
        }
        configurator: IPv4NetworkConfigurator {
            @display("p=50,125");
            config = xmldoc("demo.xml");
        }
        binder: LteBinder {
            @display("p=50,175;is=s");
        }
        server: StandardHost {
            @display("p=549,25;is=n;i=device/server");
        }
        router: Router {
            @display("p=381,28;i=device/smallrouter");
        }
        pgw: PgwStandardSimplified {
            nodeType = "PGW";
            @display("p=380,117;is=l");
        }
        eNodeB: eNodeB {
            @display("p=487,454;is=vl");
        }
        ue[numUe]: Ue {
            @display("p=596,484");
        }
        ue1[numUe1]: Ue {
            @display("p=836,241");
        }
        ue2[numUe2]: Ue {
            @display("p=207,353");
        }
        eNodeB1: eNodeB {
            @display("p=715,247;is=vl");
        }
        eNodeB2: eNodeB {
            @display("p=259,260;is=vl");
        }
        sgwStandard: SgwStandard {
            @display("p=506,117");
        }
    connections:
        server.pppg++ <--> Eth10G <--> router.pppg++;
        router.pppg++ <--> Eth10G <--> pgw.filterGate;
		sgwStandard.pppg++ <--> Eth10G <--> pgw.pppg++;
        sgwStandard.pppg++ <--> Eth10G <--> eNodeB.ppp;
        sgwStandard.pppg++ <--> Eth10G <--> eNodeB1.ppp;
        sgwStandard.pppg++ <--> Eth10G <--> eNodeB2.ppp;

        //# X2 connections
        eNodeB.ppp <--> Eth10G <--> eNodeB1.ppp;
        eNodeB.ppp <--> Eth10G <--> eNodeB2.ppp;
        eNodeB2.ppp <--> Eth10G <--> eNodeB1.ppp;
        
}
