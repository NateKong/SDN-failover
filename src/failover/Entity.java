package failover;

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
import java.util.ArrayList;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Entity {
	protected String name;
	protected static long startTime;
	private long maxTime;
	protected static ArrayList<String> log;
	protected static DecimalFormat decFor;

	public Entity(String name, long maxTime, ArrayList<String> log) {
		this.name = name;
		this.maxTime = maxTime;
		Entity.log = new ArrayList<String>();
		Entity.log = log;
		Entity.decFor = new DecimalFormat("#0.00");
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
	 * @param startTime is in milliseconds
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
		double t = time(currentTime);

		if (time(currentTime) > maxTime) {
			// log.add(decFor.format(t) + ": " + name + " stopped");
			return false;
		}
		// log.add(decFor.format(t) + ": " + name + " continues");
		return true;
	}

	/**
	 * Random number creation for time in milliseconds.
	 * 
	 * @return a number between 1000 - 5000
	 */
	public int random() {
		Random r = new Random();
		return r.nextInt(4000) + 1000;
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
	public String getTime(long currentTime) {
		double t = time(currentTime);
		return decFor.format(t);
	}
}
