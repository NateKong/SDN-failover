package failover;

/**
 * A simulation of failover of a controller in a distributed architecture.
 * The architecture simulates a telecommunications network (LTE).
 * The controller manages eNodeBs (towers).
 * 
 * Architecture:
 *   C0    C1    C2
 *   |     |      |
 *   |  E1-E4-E7  |
 *   |/ |  |  |  \|
 *   E0-E2-E5-E8-E10
 *    \ |  |  |  /
 *    	E3-E6-E9
 * 
 * C = controller
 * E = eNodeB
 * 
 * C0 controls E0
 * C1 controls E1,E2,E3,E4,E5,E6,E7,E8,E9
 * C2 controls E10
 * 
 * Simulation:
 * Using the above architecture, C2 fails
 * and the other controllers recover orphan nodes.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class Simulation2 {
	private static ArrayList<Controller> controllers;
	private static ArrayList<ENodeB> eNodeBs;
	public static long maxTime;
	public static int load;
	public static long failTime;

	public static void main(String[] args) {
		maxTime = 45;
		failTime = 5;
		// create different loads for different simulations
		int sim = 4;
		
		switch (sim) {
		case 1: load = 4; // 25% load
				break;
		case 2: load = 9; // 50% load
				break;
		case 3: load = 13; // 75% load
				break;
		default:load = 17; // 95% load
				break;
		}		
		
		System.out.println("Simulation of failover for Distributed SDN Controllers");
		System.out.println("Greedy Reactive");
		
		for (int i = 1; i<=25;i++){
			printNewSection();
			System.out.println("RUN "+i);
			start();			
		}
	}
	
	private static void start(){
		// setup
		setup();

		// system architecture
		system();

		// run simulation
		run();

		//printNewSection();
		System.out.println("\nSIMULATION COMPLETES");
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
		//printNewSection();
		//System.out.println("INITIALIZE SYSTEM\n");
		
		/* Create eNodeBs */
		//System.out.println("\nCreate eNodeBs");

		ENodeB B0 = new ENodeB(0, maxTime, load);
		eNodeBs.add(B0);
		ENodeB B1 = new ENodeB(1, maxTime, load);
		eNodeBs.add(B1);
		ENodeB B2 = new ENodeB(2, maxTime, load);
		eNodeBs.add(B2);
		ENodeB B3 = new ENodeB(3, maxTime, load);
		eNodeBs.add(B3);
		ENodeB B4 = new ENodeB(4, maxTime, load);
		eNodeBs.add(B4);
		ENodeB B5 = new ENodeB(5, maxTime, load);
		eNodeBs.add(B5);
		ENodeB B6 = new ENodeB(6, maxTime, load);
		eNodeBs.add(B6);
		ENodeB B7 = new ENodeB(7, maxTime, load);
		eNodeBs.add(B7);
		ENodeB B8 = new ENodeB(8, maxTime, load);
		eNodeBs.add(B8);
		ENodeB B9 = new ENodeB(9, maxTime, load);
		eNodeBs.add(B9);
		ENodeB B10 = new ENodeB(10, maxTime, load);
		eNodeBs.add(B10);

		/* Create Controllers */
		//System.out.println("\nCreate Controllers");
		
		Controller c0 = new Controller(0, maxTime, load);
		controllers.add(c0);
		c0.addENodeB(B0,c0);
		Controller c1 = new Controller(1, failTime, load);
		controllers.add(c1);
		c1.addENodeB(B1, B4);
		c1.addENodeB(B2, B1);
		c1.addENodeB(B3, B2);
		c1.addENodeB(B4, c1);
		c1.addENodeB(B5, B4);
		c1.addENodeB(B6, B5);
		c1.addENodeB(B7, B4);
		c1.addENodeB(B8, B7);
		c1.addENodeB(B9, B8);
		Controller c2 = new Controller(2, maxTime, load);
		controllers.add(c2);
		c2.addENodeB(B10, c2);
		
		/* Creates connections between ENodeBs */
		//System.out.println("\nCreate Connections");
		
		Connection x0 = new Connection("C0-E0", controllers.get(0), eNodeBs.get(0));
		Connection x1 = new Connection("C1-E4", controllers.get(1), eNodeBs.get(4));
		Connection x2 = new Connection("C2-E10", controllers.get(2), eNodeBs.get(10));
		Connection x4 = new Connection("E0-E2", eNodeBs.get(0), eNodeBs.get(2));
		Connection x6 = new Connection("E1-E4", eNodeBs.get(1), eNodeBs.get(4));
		Connection x7 = new Connection("E2-E5", eNodeBs.get(2), eNodeBs.get(5));
		Connection x8 = new Connection("E3-E6", eNodeBs.get(3), eNodeBs.get(6));
		Connection x9 = new Connection("E4-E7", eNodeBs.get(4), eNodeBs.get(7));
		Connection x10 = new Connection("E5-E8", eNodeBs.get(5), eNodeBs.get(8));
		Connection x11 = new Connection("E6-E9", eNodeBs.get(6), eNodeBs.get(9));
		Connection x13 = new Connection("E10-E8", eNodeBs.get(10), eNodeBs.get(8));
		Connection x15 = new Connection("E1-E2", eNodeBs.get(1), eNodeBs.get(2));
		Connection x16 = new Connection("E2-E3", eNodeBs.get(2), eNodeBs.get(3));
		Connection x17 = new Connection("E4-E5", eNodeBs.get(4), eNodeBs.get(5));
		Connection x18 = new Connection("E5-E6", eNodeBs.get(5), eNodeBs.get(6));
		Connection x19 = new Connection("E7-E8", eNodeBs.get(7), eNodeBs.get(8));
		Connection x20 = new Connection("E8-E9", eNodeBs.get(8), eNodeBs.get(9));
		
	}

	/**
	 * Runs the simulation
	 */
	private static void run() {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		long startTime = System.currentTimeMillis();

		//printNewSection();
		System.out.println("SIMULATION BEGINS");

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
