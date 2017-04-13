# Design Concept for a Failover Mechanism in Distributed SDN Controllers
### Master's Project for Master of Science in Computer Science from San Jose State University.
https://natekong.github.io/

___
Description:

These simulations incorporate various failover mechanisms in a LTE network. The master branch does not work please see other branches.

___
Branch:
- branch: greedy-reactive -> uses a greedy algorithm after controller failure.
- branch: greedy-proactive -> uses a greedy algorithm before controller failure.
- branch: hbf-reactive -> uses the hop and bandwidth based failover algorithm after controller failure.
- branch: hbf-proactive -> uses the hop and bandwidth based failover algorithm before controller failure.
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