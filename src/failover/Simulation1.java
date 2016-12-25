package failover;

/**
 * A simulation of failover of a controller in a distributed architecture.
 * 
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
