# Design Concept for a Failover Mechanism in Distributed SDN Controllers

### This work in progress will be for my Master's Project in Computer Science from San Jose State University.

https://natekong.github.io/

___
Description:

This simulation shows a greedy mechanism for controller failover in a LTE network.

___
Architecture:
- 3 controllers (distributed)
- 9 eNodeBs

![alt text](https://github.com/NateKong/NateKong.github.io/blob/master/images/nate.jpg "System Architecture")
  
  C = controller
  
  E = eNodeB
  
  C1 controls E1,E2,E3
  
  C2 controls E4,E5,E6
  
  C3 controls E7,E8,E9

___
Simulation:

1) Each controller connects to 3 eNodeBs.

2) One controller fails.

3) Orphan eNodeBs from failed controller send a signal to neighbor eNodeBs.

4) neighbor eNodeBs tell their controller.

5) Controllers use weighted greedy mechanism to adopt Orphan eNodeBs.
