package entity;

/**
 * There are four different QoS classes: (celluar_QoS, Jain)
 * Conversational class - telephony speech, voice over IP and video conferencing.
 * Streaming class - listening to or looking at real time video (audio).
 * Interactive class - browsing the web, database retrieval, access of server.
 * Background class - SMS, download of databases, email program which is running in the background.
 * 
 * transmission - notification to elements for instructions
 * 
 * @author Nathan Kong
 * @since Nov 2017
 *
 */
public enum QoS {
	Conversational, Streaming, Interactive, Background, transmission
}
