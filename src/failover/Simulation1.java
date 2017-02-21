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
	//public static final long maxTime = 30; // this is in seconds
	public static final long maxTime = 60;
	//public static final long maxTime = 90;
	//public static final long maxTime = 120;

	public static void main(String[] args) {
		for (int i = 1; i<=10;i++){
			printNewSection();
			System.out.println("RUN "+i);
			start();
			System.out.println("\n");
			
		}
	}
	
	private static void start(){
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
		//long failTime = 10; // this is the fail time for Controller1
		long failTime = 20;
		//long failTime = 40;
		//long failTime = 60;
		
		int numOfeNodeBs = 9;
		int numOfControllers = 3;
		int remainingCap = 20;

		printNewSection();
		System.out.println("INITIALIZE SYSTEM\n");

		/* Create Controllers */
		System.out.println("Create Controllers");
		for (int i = 0; i < numOfControllers; i++) {
			if (i == 1) {
				Controller c = new Controller(i, remainingCap, failTime);
				controllers.add(c);
			} else {
				Controller c = new Controller(i, remainingCap, maxTime);
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

		for (int i = 1; i < numOfeNodeBs; i++) {
			int bw = 20;

			// creates a connection between the eNodeB and the previously
			// created one
			new Xtwo(eNodeBs.get(i - 1), eNodeBs.get(i), bw);

			// if this is the third eNodeB in the set
			// e.g. eNodeB 2, 5, and 8
			if (i % 3 == 2) {
				new Xtwo(eNodeBs.get(i - 2), eNodeBs.get(i), bw);
			}
		}

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
