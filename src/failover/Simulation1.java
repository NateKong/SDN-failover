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
		
		/* Create eNodeBs */
		System.out.println("\nCreate eNodeBs");

		ENodeB B0 = new ENodeB(0, maxTime);
		eNodeBs.add(B0);
		ENodeB B1 = new ENodeB(1, maxTime);
		eNodeBs.add(B1);
		ENodeB B2 = new ENodeB(2, maxTime);
		eNodeBs.add(B2);
		ENodeB B3 = new ENodeB(3, maxTime);
		eNodeBs.add(B3);
		ENodeB B4 = new ENodeB(4, maxTime);
		eNodeBs.add(B4);
		ENodeB B5 = new ENodeB(5, maxTime);
		eNodeBs.add(B5);
		ENodeB B6 = new ENodeB(6, maxTime);
		eNodeBs.add(B6);
		ENodeB B7 = new ENodeB(7, maxTime);
		eNodeBs.add(B7);
		ENodeB B8 = new ENodeB(8, maxTime);
		eNodeBs.add(B8);

		/* Creates connections between ENodeBs */
		System.out.println("\nCreate Connections");
		
		Connection x0 = new Connection("connection0", eNodeBs.get(0), eNodeBs.get(1));
		Connection x1 = new Connection("connection1", eNodeBs.get(1), eNodeBs.get(2));
		Connection x2 = new Connection("connection2", eNodeBs.get(0), eNodeBs.get(2));
		Connection x3 = new Connection("connection3", eNodeBs.get(3), eNodeBs.get(4));
		Connection x4 = new Connection("connection4", eNodeBs.get(4), eNodeBs.get(5));
		Connection x5 = new Connection("connection5", eNodeBs.get(3), eNodeBs.get(5));
		Connection x6 = new Connection("connection6", eNodeBs.get(6), eNodeBs.get(7));
		Connection x7 = new Connection("connection7", eNodeBs.get(7), eNodeBs.get(8));
		Connection x8 = new Connection("connection8", eNodeBs.get(6), eNodeBs.get(8));
		Connection x9 = new Connection("connection9", eNodeBs.get(2), eNodeBs.get(3));
		Connection x10 = new Connection("connection10", eNodeBs.get(5), eNodeBs.get(6));

		/* Create Controllers */
		System.out.println("\nCreate Controllers");
		
		Controller c0 = new Controller(0, maxTime);
		controllers.add(c0);
		c0.addENodeB(B1);
		c0.addENodeB(B0);
		c0.addENodeB(B2);
		Controller c1 = new Controller(1, failTime);
		controllers.add(c1);
		c1.addENodeB(B4);
		c1.addENodeB(B3);
		c1.addENodeB(B5);
		Controller c2 = new Controller(2, maxTime);
		controllers.add(c2);
		c2.addENodeB(B7);
		c2.addENodeB(B6);
		c2.addENodeB(B8);
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
