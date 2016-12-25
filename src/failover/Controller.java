package failover;

/**
 * A Software Defined Network
 * Controller.
 * A controller for a LTE network
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

import java.util.ArrayList;

public class Controller implements Runnable{
	private ArrayList<ENodeB> eNodeBs;

	public Controller() {
		eNodeBs = new ArrayList<ENodeB>();
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
