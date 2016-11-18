package entity;

/**
 * 
 * @author Nathan Kong
 * @since Nov 2017
 *
 */

public class packet {
	private QoS qos;

	packet(QoS qos) {
		this.qos = qos;
	}

	public QoS getQoS() {
		return qos;
	}
}
