# SDN_LTE
This work in progress will be for my Master's Project in Computer Science from San Jose State University.

https://natekong.github.io/

# Design Concept for a Failover Mechanism in Distributed SDN Controllers

This simulation will show a weighted greedy mechanism for controllers when a controller fails in a LTE network.

Architecture:
- 3 controllers (distributed)
- 9 eNodeBs

Simulation:

1) Each controller connects to 3 eNodeBs.

2) One controller fails.

3) Orphan eNodeBs from failed controller send a signal to neighbor eNodeBs.

4) neighbor eNodeBs tell their controller.

5) Controllers use weighted greedy mechanism to adopt Orphan eNodeBs.


 Architecture:
 
    C1       C2       C3
    
    E1       E4       E7
   /  \     /  \     /  \
  E0---E2--E3---E5--E6---E8
  
  C = controller
  E = eNodeB
  
  C1 controls E1,E2,E3
  C2 controls E4,E5,E6
  C3 controls E7,E8,E9
