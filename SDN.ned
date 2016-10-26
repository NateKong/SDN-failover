// 
//                           SimuLTE
// 
// This file is part of a software released under the license included in file
// "license.pdf". This license can be also found at http://www.ltesimulator.com/
// The above file and the present reference are part of the software itself, 
// and cannot be removed from it.
// 
package lte.simulations.sdn;

import inet.networklayer.autorouting.ipv4.IPv4NetworkConfigurator;
import inet.networklayer.ipv4.RoutingTableRecorder;
import inet.nodes.ethernet.Eth1G;
import inet.nodes.inet.Router;
import inet.nodes.inet.StandardHost;
import lte.stack.phy.ChannelControl.LteChannelControl;
import lte.corenetwork.binder.LteBinder;
import lte.corenetwork.nodes.Ue;
import lte.corenetwork.nodes.eNodeB;
import lte.epc.PgwStandardSimplified;


network SDN
{
    parameters:
        int numExtCells = default(0);
        @display("i=block/network2;bgb=1536,1000;bgi=background/sf2");
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
            @display("p=1051,426;is=n;i=device/server");
        }
        router: Router {
            @display("p=879,431;i=device/smallrouter");
        }
        pgw: PgwStandardSimplified {
            nodeType = "PGW";
            @display("p=661,443;is=l");
        }
        eNodeB1: eNodeB {
            @display("p=379,610;is=vl");
        }
        eNodeB2: eNodeB {
            @display("p=391,259;is=vl");
        }
        eNodeB3: eNodeB {
            @display("p=760,125;is=vl");
        }
        eNodeB4: eNodeB {
            @display("p=1158,175;is=vl");
        }
        eNodeB5: eNodeB {
            @display("p=1099,590;is=vl");
        }
        ue1: Ue {
            @display("p=124,571");
        }
        ue2: Ue {
            @display("p=1275,459");
        }

    connections:
        server.pppg++ <--> Eth1G <--> router.pppg++;
        router.pppg++ <--> Eth1G <--> pgw.filterGate;
        pgw.pppg++ <--> Eth1G <--> eNodeB1.ppp;
        pgw.pppg++ <--> Eth1G <--> eNodeB2.ppp;
        pgw.pppg++ <--> Eth1G <--> eNodeB3.ppp;
        pgw.pppg++ <--> Eth1G <--> eNodeB4.ppp;
        pgw.pppg++ <--> Eth1G <--> eNodeB5.ppp;

        // X2 connections
        eNodeB1.x2++ <--> Eth1G <--> eNodeB2.x2++;
        eNodeB2.x2++ <--> Eth1G <--> eNodeB3.x2++;
        eNodeB3.x2++ <--> Eth1G <--> eNodeB4.x2++;
        eNodeB4.x2++ <--> Eth1G <--> eNodeB5.x2++;
        eNodeB1.x2++ <--> Eth1G <--> eNodeB5.x2++;

}
