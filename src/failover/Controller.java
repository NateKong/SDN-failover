package failover;

/**
 * A Software Defined Network controller.
 * A controller for a LTE network
 * over eNodeBs.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class Controller implements Runnable{
	private int name;
	private ArrayList<ENodeB> eNodeBs;
	private int load;

	public Controller(int name) {
		this.name = name;
		eNodeBs = new ArrayList<ENodeB>();
	}
	
	public int getName() {
		return name;
	}

	public void addENodeB(ENodeB e) {
		eNodeBs.add(e);
	}

	public void removeENodeB(ENodeB e) {
		eNodeBs.remove(e);
	}

	@Override
	public void run() {

		
	}

}
