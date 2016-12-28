# Design Concept for a Failover Mechanism in Distributed SDN Controllers (in progress)

### Master's Project for Master of Science in Computer Science from San Jose State University.
https://natekong.github.io/

___
Description:

This simulation shows a greedy mechanism for controller failover in a LTE network.

___
Architecture:
- 3 controllers (distributed)
- 9 eNodeBs

![alt text](https://github.com/NateKong/SDN-failover/blob/master/images/DN.png "System Architecture")
  
  C = controller
  
  E = eNodeB
  
  C1 controls E1,E2,E3
  
  C2 controls E4,E5,E6
  
  C3 controls E7,E8,E9

___
Simulation:

1) Each controller connects to 3 eNodeBs.

2) One controller fails. (Controller1)

3) Orphan eNodeBs from failed controller send a signal to neighboring eNodeBs.

4) neighbor eNodeBs tell their controller.

5) Controllers use greedy mechanism to adopt Orphan eNodeBs.

___
Branch:
- branch: homogeneous-greedy -> uses a greedy algorithm after failure in a homogeneous architecture.
- branch: homogeneous-partition -> uses a greedy algorithm before failure in a homogeneous architecture. (in progress)
- branch: heterogeneous-greedy -> uses a greedy algorithm after failure in a heterogeneous architecture. (in progress)
- branch: master -> uses a greedy algorithm after failure in a heterogeneous architecture. (in progress)
