# Design Concept for a Failover Mechanism in Distributed SDN Controllers

### Master's Project for Master of Science in Computer Science from San Jose State University.
https://natekong.github.io/

___
Description:

This simulation shows a proactive hop and bandwidth based failover (hbf) mechanism for controller failover in a LTE network. Before Controllers fail the mechanism determines best controller to adopt.

___
Simulation1: evenly distributed architecture
- 3 controllers
- 5 eNodeBs

![alt text](https://github.com/NateKong/SDN-failover/blob/master/images/sim1.png "System Architecture")
  
  Controller0 controls eNodeB0
  
  Controller1 controls eNodeB1, eNodeB2, eNodeB3
  
  Controller3 controls eNodeB4

___
Simulation3: uneven architecture
- 3 controllers 
- 8 eNodeBs

![alt text](https://github.com/NateKong/SDN-failover/blob/master/images/sim3.png "System Architecture")
  
  Controller0 controls eNodeB0
  
  Controller1 controls eNodeB1, eNodeB2, eNodeB3, eNodeB4, eNodeB5, eNodeB6
  
  Controller3 controls eNodeB7
___
Simulation:

1) eNodeBs determine their backup controller through the hbf algorithm. eNodeBs will initially adopt the first controller as their backup controller but can update to a controller with less hops or equal hops with more bandwidth.

2) Controller1 fails.

3) Orphan eNodeBs sends a discovery message to it's backup controller.

4) Backup Controller adopts orphan.

5) Orphan finds new backup controller.