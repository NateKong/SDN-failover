package failover;

/**
 * A simulation of failover of a controller in a distributed architecture.
 * The architecture simulates a telecommunications network (LTE).
 * The controller manages eNodeBs (towers).
 * 
 * Architecture:
 *   C1       C2       C3
 *   
 *   E1       E4       E7
 * 	/  \     /  \     /  \
 * E0---E2--E3---E5--E6---E8
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
	public static final long maxTime = 30; //this is in seconds

	public static void main(String[] args) {
		System.out.println("Simulation of failover for Distributed SDN Controllers");

		// setup
		setup();

		// system architecture
		system();

		// run simulation
		run();

		// print statistics
		printStats();

		printNewSection();
		System.out.println("SIMULATION COMPLETE");
	}

	/**
	 * Initialization of the program.
	 */
	private static void setup() {
		// printNewSection();
		// System.out.println("SETUP\n");
		stats = new ArrayList<String>();
		controllers = new ArrayList<Controller>();
		eNodeBs = new ArrayList<ENodeB>();
	}

	/**
	 *  Initialization of the system architecture.
	 *  Creates system components:
	 *  Controllers
	 *  eNodeBs
	 *  X2 connections
	 */
	private static void system() {
		int numOfeNodeBs = 9;
		int numOfControllers = 3;
		long startTime = System.currentTimeMillis();

		printNewSection();
		System.out.println("INITIALIZE SYSTEM\n");

		/* Create Controllers */
		System.out.println("Create Controllers");
		for (int i = 0; i < numOfControllers; i++) {
			int load = 20;
			Controller c = new Controller(i, load, startTime, maxTime, stats);
			controllers.add(c);
			System.out.println(c.getName() + " is created");
		}

		/* Create eNodeBs */
		System.out.println("\nCreate eNodeBs");
		for (int i = 0, j = 0; i < numOfeNodeBs; i++) {
			ENodeB B = new ENodeB(i, startTime, maxTime, stats);
			eNodeBs.add(B);
			System.out.println(B.getName() + " is created");

			Controller C = controllers.get(j);
			C.addENodeB(B);
			System.out.println(C.getName() + " adopts " + B.getName());
			if (i % 3 == 2) {
				j++;
			}

		}

		/* Creates connections between ENodeBs */
		System.out.println("\nCreate Connections");

		for (int i = 1; i < numOfeNodeBs; i++) {
			int bw = 20;

			// creates a connection between the eNodeB and the previously
			// created one
			new Xtwo(eNodeBs.get(i - 1), eNodeBs.get(i), bw);
			System.out.println(eNodeBs.get(i - 1).getName() + " has a X2 connected to " + eNodeBs.get(i).getName());

			// if this is the third eNodeB in the set
			// e.g. eNodeB 2, 5, and 8
			if (i % 3 == 2) {
				new Xtwo(eNodeBs.get(i - 2), eNodeBs.get(i), bw);
				System.out.println(eNodeBs.get(i - 2).getName() + " has a X2 connected to " + eNodeBs.get(i).getName());
			}
		}

	}

	/**
	 * Runs the simulation
	 */
	private static void run() {
		ArrayList<Thread> threads = new ArrayList<Thread>();

		printNewSection();
		System.out.println("RUN SIMULATION\n");

		// start components
		for (Controller c : controllers) {
			Thread t = new Thread(c);
			threads.add(t);
			t.start();
		}
		
		for (ENodeB b : eNodeBs) {
			Thread t = new Thread(b);
			threads.add(t);
			t.start();
		}

		// joins all the threads and ensures the Main program doesn't continue
		// until the simulation is completed.
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//
		System.out.println("finished main");
	}

	/**
	 * Prints the time logs to console
	 */
	private static void printStats() {
		printNewSection();
		System.out.println("STATISTICS\n");
		for (String s : stats) {
			System.out.println(s);
		}
	}

	/**
	 * Creates a new section in the console
	 */
	private static void printNewSection() {
		System.out.println("\n**************************************************");
	}

}
