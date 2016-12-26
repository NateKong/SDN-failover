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

public class ENodeB implements Runnable{
	private int name;
	private ArrayList<Xtwo> connections;

	public ENodeB(int name) {
		this.name = name;
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
