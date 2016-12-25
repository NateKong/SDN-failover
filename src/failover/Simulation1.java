package failover;

/**
 * A simulation of failover of a controller in a distributed architecture.
 * The architecture simulates a telecommunications network (LTE).
 * The controller manages eNodeBs (towers).
 * 
 * Architecture:
 *   C1       C2       C3
 *   
 *   E2       E4       E7
 * 	/  \     /  \     /  \
 * E1---E3--E5---E6--E8---E9
 * 
 * C = controller
 * E = eNodeB
 * 
 * C1 controls E1,E2,E3
 * C2 controls E4,E5,E6
 * C3 controls E7,E8,E9
 * 
 * Simulation:
 * Using the above architecture, C2 fails
 * and the other controllers recover orphan nodes.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class Simulation1 {
	private static ArrayList<String> stats;
	private static ArrayList<Controller> controllers;
	private static ArrayList<ENodeB> eNodeBs;
	
	public static void main(String[] args) {
		System.out.println("Simulation of failover for Distributed SDN Controllers");
		
		// setup
		setup();

		//system architecture
		system();
		
		// run simulation
		run();

		// print statistics
		printStats();
		
		System.out.println("\nSIMULATION COMPLETE");
	}

	private static void setup() {
		printNewSection();
		System.out.println("SETUP\n");
		stats = new ArrayList<String>();
		controllers = new ArrayList<Controller>();
		eNodeBs = new ArrayList<ENodeB>();
	}

	private static void system() {
		printNewSection();
		System.out.println("INITIALIZE SYSTEM\n");
		
		System.out.println("Create eNodeBs");
		
		int numOfeNodeBs = 9;
		for (int i = 0; i < numOfeNodeBs; i++) {
			ENodeB B = new ENodeB(i);
			eNodeBs.add(B);
		}
				
		System.out.println("Create connections");
		/* Creates connections between ENodeBs */
		
		
	 	System.out.println("Create controllers");
	 	/* One of the Controllers will fail */
	 	int numOfControllers = 3;
	}
	
	private static void run() {
		printNewSection();
		System.out.println("RUN SIMULATION\n");
	}

	private static void printStats() {
		printNewSection();
		System.out.println("STATISTICS\n");
		for (String s: stats){
			System.out.println(s);
		}
	}

	private static void printNewSection() {
		System.out.println("**************************************************");
	}
}
