package failover;

/**
 * An eNodeB in a LTE
 * architecture. The eNodeB
 * is controlled using SDN.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class ENodeB extends Entity implements Runnable{
	private int name;
	private long startTime;
	private ArrayList<Xtwo> connections;

	public ENodeB(int name, long startTime, long maxTime, ArrayList<String> log) {
		super(name, startTime, maxTime, log);
		connections = new ArrayList<Xtwo>();
	}
	
	public int getName() {
		return name;
	}
	
	public void addConnection(Xtwo x2){
		connections.add(x2);
	}
	
	@Override
	public void run() {
		
	}
}
