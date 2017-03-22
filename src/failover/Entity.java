package failover;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An Entity in LTE.
 * This is a parent object
 * 
 * e.g. Controller, eNodeB
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.Random;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Entity {
	protected String name;
	protected static long startTime;
	private long maxTime;
	protected static DecimalFormat decFor;
	protected ArrayList<Connection> connections; // a list of connections to other eNodeBs
	private int load;

	public Entity(String name, long maxTime, int load) {
		this.name = name;
		this.maxTime = maxTime;
		this.load = load;
		connections = new ArrayList<Connection>();
		Entity.decFor = new DecimalFormat("#0.000");
		decFor.setRoundingMode(RoundingMode.CEILING);
	}

	/**
	 * Gets the name of the Entity.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the start time of the simulation
	 * 
	 * @param startTime
	 *            is in milliseconds
	 */
	public void setStartTime(long startTime) {
		Entity.startTime = startTime;
	}

	/**
	 * Checks to see if the simulation has reached it maximum time. This will
	 * end the thread.
	 * 
	 * @param currentTime
	 *            the current time in milliseconds
	 * @return false if the simulation has reached its maximum time
	 */
	public boolean checkTime(long currentTime) {
		if (time(currentTime) > maxTime) {
			return false;
		}
		return true;
	}

	/**
	 * Random number creation for time in milliseconds.
	 * where
	 * 25% load is between 0 - 250 milliseconds
	 * 50% load is between 0 - 500 milliseconds
	 * 75% load is between 0 - 750 milliseconds
	 * 95% load is between 0 - 950 milliseconds
	 * 
	 * @param load is the percent load 
	 * @return a number in milliseconds
	 */
	public int random() {
		int time = load * 10;
		
		Random r = new Random();
		return r.nextInt(time);
	}

	/**
	 * Converts timestamps into time elapsed.
	 * 
	 * @param currentTime
	 *            in milliseconds
	 * @return elapsed time in seconds
	 */
	public double time(long currentTime) {
		return ((double) (currentTime - startTime)) / 1000;
	}

	/**
	 * Gets the time elapsed in a string format.
	 * 
	 * @param currentTime
	 *            in milliseconds
	 * @return String of time formatted to 2 decimal points rounded to the
	 *         ceiling
	 */
	public String getTime() {
		double t = time(System.currentTimeMillis());
		return decFor.format(t);
	}

	/**
	 * Adds a connection to other eNodeBs or controllers
	 * 
	 * @param c the connection between eNodeBs
	 */
	public void addConnection(Connection c) {
		connections.add(c);
	}
	
	/**
	 * Removes a connection
	 * 
	 * @param c
	 */
	public void removeConnection(Connection c) {
		connections.remove(c);
	}

	/**
	 * THIS REQUIRES AN OVERRIDE 
	 * @param orphanBoardcast
	 */
	public void messageController(Message orphanBroadcast) {
		
	}
}
