package failover;

/**
 * A simulation of failover of a controller in a distributed architecture.
 * The architecture simulates a telecommunications network (LTE).
 * The controller manages eNodeBs (towers).
 * 
 * 
 * Simulation:
 * Controller1 fails
 * and the other controllers recover orphan nodes.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class Simulation1a {
	private static ArrayList<Controller> controllers;
	private static ArrayList<ENodeB> eNodeBs;
	public static long maxTime;
	public static int highLoad;
	public static int lowLoad;
	public static long failTime;

	public static void main(String[] args) {
		maxTime = 15;
		failTime = 5;
		// create different loads for different simulations
		highLoad = 17; // 95% load
		lowLoad = 4; // 25% load
		
		System.out.println("Simulation of failover for Distributed SDN Controllers");
		System.out.println("Greedy Reactive");
		
		for (int i = 1; i<=1;i++){
			printNewSection();
			System.out.println("RUN "+i);
			start();
			//System.out.println("\n");
			
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
		//System.out.println("SIMULATION COMPLETE");
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

		ENodeB B0 = new ENodeB(0, maxTime, highLoad);
		eNodeBs.add(B0);
		ENodeB B1 = new ENodeB(1, maxTime, lowLoad);
		eNodeBs.add(B1);
		ENodeB B2 = new ENodeB(2, maxTime, lowLoad);
		eNodeBs.add(B2);
		ENodeB B3 = new ENodeB(3, maxTime, lowLoad);
		eNodeBs.add(B3);
		ENodeB B4 = new ENodeB(4, maxTime, lowLoad);
		eNodeBs.add(B4);

		/* Create Controllers */
		//System.out.println("\nCreate Controllers");
		
		Controller c0 = new Controller(0, maxTime, highLoad);
		controllers.add(c0);
		c0.addENodeB(B0,c0);
		Controller c1 = new Controller(1, failTime, lowLoad);
		controllers.add(c1);
		c1.addENodeB(B1, B2);
		c1.addENodeB(B2, B3);
		c1.addENodeB(B3, c1);
		Controller c2 = new Controller(2, maxTime, lowLoad);
		controllers.add(c2);
		c2.addENodeB(B4, c2);
		
		/* Creates connections between ENodeBs */
		//System.out.println("\nCreate Connections");
		
		Connection x0 = new Connection("C0-E0", controllers.get(0), eNodeBs.get(0), 175);
		Connection x1 = new Connection("C1-E3", controllers.get(1), eNodeBs.get(3), 175);
		Connection x2 = new Connection("C2-E4", controllers.get(2), eNodeBs.get(4), 175);
		Connection x4 = new Connection("E0-E1", eNodeBs.get(0), eNodeBs.get(1), 100);
		Connection x6 = new Connection("E1-E2", eNodeBs.get(1), eNodeBs.get(2), 100);
		Connection x7 = new Connection("E2-E3", eNodeBs.get(2), eNodeBs.get(3), 80);
		Connection x8 = new Connection("E1-E4", eNodeBs.get(1), eNodeBs.get(4), 75);
	}

	/**
	 * Runs the simulation
	 */
	private static void run() {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		long startTime = System.currentTimeMillis();

		//printNewSection();
		System.out.println("SIMULATION BEGINS\n");

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

		System.out.println("\nSIMULATION COMPLETES\n");
		// System.out.println("finished main");
	}

	/**
	 * Creates a new section in the console
	 */
	private static void printNewSection() {
		System.out.println("\n**************************************************");
	}

}
