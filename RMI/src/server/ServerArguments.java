package server;

import com.beust.jcommander.Parameter;

/**
 * Arguments that can be applied to the server. 
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 08-Jan-2013
 */
public class ServerArguments {
	
	// is tightly connected to the peerTimeout argument.
	// It specifies the time in ms between two peer checks
	// (checking whether the last “isAlive” packet was received more than
	// peerTimeout ms ago).
	@Parameter(names = "-checkPeriod", required = true, description = "The period of time to check if peers timed out. Max value 60000 | Min. for CheckPeriod")
	private int checkPeriod;
	// the period in ms each peer has to send an “isAlive” packet (only
	// containing the peer's TCP port).
	// If no such is received for that time the peer is assumed to be offline
	// and is removed.
	@Parameter(names = "-peerTimeout", required = true, description = "The time after a peer times out if he doesn't send an isAlive package. Max value 60000 | Min. for PeerTimeout Value 5000.")
	private int peerTimeout;
	// the port to be used for instantiating a java.net.DatagramSocket (handling
	// UDP requests).
	@Parameter(names = "-udpPort", required = true, description = "The port on which the udp server should be startet. This server receivs isAlive packages and check if the peer didn't time out.")
	private int udpPort;
	// the port to be used for instantiating a java.net.ServerSocket (handling
	// TCP requests).
	@Parameter(names = "-tcpPort", required = true, description = "The port on which the server tcp port should listen this is the port for the Server instance.")
	private int tcpPort;
	// set the debug parameter, if true debug infromation will be shown
	@Parameter(names = "-debug", description = "Show debug information yes/no")
	private boolean debug = false;
	// to display help infromation
	@Parameter(names = "-help", description = "Display the help menue.", help = true)
	private boolean help = false;

	/**
	 * @return the checkPeriod
	 */
	public int getCheckPeriod() {
		return checkPeriod;
	}

	/**
	 * @return the peerTimeout
	 */
	public int getPeerTimeout() {
		return peerTimeout;
	}

	/**
	 * @return the udpPort
	 */
	public int getUdpPort() {
		return udpPort;
	}

	/**
	 * @return the tcpPort
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the help
	 */
	public boolean isHelp() {
		return help;
	}

}
