package failover;

import java.util.Random;
import java.util.ArrayList;

public class Entity {
	protected String name;
	protected static long startTime;
	private static long maxTime;
	protected static ArrayList<String> log;

	public Entity(String name, long startTime, long maxTime, ArrayList<String> log) {
		this.name = name;
		Entity.startTime = startTime;
		Entity.maxTime = maxTime;
		Entity.log = new ArrayList<String>();
		Entity.log = log;
	}

	public String getName() {
		return name;
	}

	public boolean checkTime(long currentTime) {
		double t = time(currentTime);
		
		if (time(currentTime) > maxTime) {
			log.add("Finished " + name + " " + t);
			return false;
		}
		log.add("Continue " + name + " " + t);

		return true;
	}

	public int random() {
		Random r = new Random();
		return r.nextInt(4000) + 1000;
	}
	
	public double time(long currentTime){
		return ((double) (currentTime - startTime)) / 1000;
	}
}
