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
 * C1 controls E0,E1,E2
 * C2 controls E3,E4,E5
 * C3 controls E6,E7,E8
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
	private static ArrayList<Controller> controllers;
	private static ArrayList<ENodeB> eNodeBs;
	public static final long maxTime = 30; // this is in seconds

	public static void main(String[] args) {
		System.out.println("Simulation of failover for Distributed SDN Controllers");

		// setup
		setup();

		// system architecture
		system();

		// run simulation
		run();

		printNewSection();
		System.out.println("SIMULATION COMPLETE");
	}

	/**
	 * Initialization of the program.
	 */
	private static void setup() {
		controllers = new ArrayList<Controller>();
		eNodeBs = new ArrayList<ENodeB>();
	}

	/**
	 * Initialization of the system architecture. Creates system components:
	 * Controllers eNodeBs X2 connections
	 */
	private static void system() {
		long failTime = 10; // this is the fail time for Controller1
		int numOfeNodeBs = 9;
		int numOfControllers = 3;

		printNewSection();
		System.out.println("INITIALIZE SYSTEM\n");

		/* Create Controllers */
		System.out.println("Create Controllers");
		for (int i = 0; i < numOfControllers; i++) {
			if (i == 1) {
				Controller c = new Controller(i, failTime);
				controllers.add(c);
			} else {
				Controller c = new Controller(i, maxTime);
				controllers.add(c);
			}

		}

		/* Create eNodeBs */
		System.out.println("\nCreate eNodeBs");
		for (int i = 0, j = 0; i < numOfeNodeBs; i++) {
			ENodeB B = new ENodeB(i, maxTime);
			eNodeBs.add(B);

			Controller C = controllers.get(j);
			C.addENodeB(B);
			if (i % 3 == 2) {
				j++;
			}

		}

		/* Creates connections between ENodeBs */
		System.out.println("\nCreate Connections");
		
		Xtwo c0 = new Xtwo("connection0", eNodeBs.get(0), eNodeBs.get(1), 100);
		Xtwo c1 = new Xtwo("connection1", eNodeBs.get(1), eNodeBs.get(2), 100);
		Xtwo c2 = new Xtwo("connection2", eNodeBs.get(0), eNodeBs.get(3), 100);
		Xtwo c3 = new Xtwo("connection3", eNodeBs.get(3), eNodeBs.get(4), 100);
		Xtwo c4 = new Xtwo("connection4", eNodeBs.get(4), eNodeBs.get(5), 100);
		Xtwo c5 = new Xtwo("connection5", eNodeBs.get(3), eNodeBs.get(5), 100);
		Xtwo c6 = new Xtwo("connection6", eNodeBs.get(6), eNodeBs.get(7), 100);
		Xtwo c7 = new Xtwo("connection7", eNodeBs.get(7), eNodeBs.get(8), 100);
		Xtwo c8 = new Xtwo("connection8", eNodeBs.get(6), eNodeBs.get(8), 100);
		Xtwo c9 = new Xtwo("connection9", eNodeBs.get(2), eNodeBs.get(3), 100);
		Xtwo c10 = new Xtwo("connection10", eNodeBs.get(5), eNodeBs.get(6), 100);

	}

	/**
	 * Runs the simulation
	 */
	private static void run() {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		long startTime = System.currentTimeMillis();

		printNewSection();
		System.out.println("RUN SIMULATION\n");

		// create threads components
		for (Controller c : controllers) {
			c.setStartTime(startTime);
			Thread t = new Thread(c);
			threads.add(t);
			t.start();
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for (ENodeB b : eNodeBs) {
			b.setStartTime(startTime);
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
				System.out.println("Cannot join threads");
				e.printStackTrace();
			}
		}

		// System.out.println("finished main");
	}

	/**
	 * Creates a new section in the console
	 */
	private static void printNewSection() {
		System.out.println("\n**************************************************");
	}

}
