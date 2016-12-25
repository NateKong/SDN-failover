package failover;

/**
 * A X2 connection between two eNodeBs.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

public class Xtwo {
	private int bandwidth;
	private ENodeB[] endpoints;

	public Xtwo() {
		endpoints = new ENodeB[2];
	}

}
