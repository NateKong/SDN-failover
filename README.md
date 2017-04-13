# Design Concept for a Failover Mechanism in Distributed SDN Controllers

###  Master's Project for Master of Science in Computer Science from San Jose State University.
https://natekong.github.io/

___
Description:

This simulation shows a reactive greedy mechanism for controller failover in a LTE network. When Controllers fail the mechanism reacts and adopts the closest controller.

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

1) Each controller connects to 3 eNodeBs.

2) One controller fails. (Controller1)

3) Orphan eNodeBs from failed controller send a signal to neighboring eNodeBs.

4) neighbor eNodeBs tell their controller.

5) Controllers use greedy mechanism to adopt Orphan eNodeBs.
