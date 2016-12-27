package failover;

import java.util.Random;
import java.util.ArrayList;

public class Entity {
	protected int name;
	private static long startTime;
	private static long maxTime;
	protected static ArrayList<String> log;
	
	public Entity(int name, long startTime, long maxTime, ArrayList<String> log) {
		this.name = name;
		Entity.startTime = startTime;
		Entity.maxTime = maxTime;
		Entity.log = new ArrayList<String>();
		Entity.log = log;
	}
	
	public int getName() {
		return name;
	}
	
	public boolean checkTime(long currentTime) {
		if (currentTime - startTime > maxTime) {
			log.add("Finished " + name + " " + (currentTime - startTime) );
			return false;
		}
		log.add("Continue " + name + " " + (currentTime - startTime) );
		
		return true;
	}
	
	public int random() {
		Random r = new Random();
		return r.nextInt(4000) + 1000;
	}
}
