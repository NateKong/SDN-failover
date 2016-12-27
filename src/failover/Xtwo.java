package failover;

/**
 * A X2 connection between two eNodeBs.
 * 
 * @author Nathan Kong
 * @since Jan 2017
 */

public class Xtwo {
	private int bw;
	private ENodeB[] endpoints;

	public Xtwo(ENodeB endpt0, ENodeB endpt1, int bw) {
		endpoints = new ENodeB[2];
		endpoints[0] = endpt0;
		endpoints[1] = endpt1;
		this.bw = bw;
		endpt0.addConnection(this);
		endpt1.addConnection(this);
	}

	/**
	 * Gets the maximum available bandwidth between eNodeBs (X2 connection)
	 * 
	 * @return maximum bandwidth
	 */
	public int getBW() {
		return bw;
	}

	/**
	 * Used to for an eNodeB to figure out the other eNodeB thats attached to
	 * it.
	 * 
	 * @param me
	 *            the endpoint that is asking
	 * @return the endpoint of the other eNodeB
	 */
	public ENodeB getEndpoint(ENodeB me) {
		if (endpoints[0].equals(me)) {
			return otherEndpoint(1);
		} else if (endpoints[1].equals(me)) {
			return otherEndpoint(0);
		}
		return null;
	}

	private ENodeB otherEndpoint(int i) {
		return endpoints[i];
	}

}
