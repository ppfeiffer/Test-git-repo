package peer;

import com.beust.jcommander.Parameter;

/**
 * In this class the attributes are defined, which are required to start
 * a peer sucessfully. The class works with the "jcommander" library. Now
 * it is very easy to check the correct input for the arguments from the
 * client.
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-20
 */
public class PeerArguments {

	// set the tcp port to communicate with the server
	@Parameter(names = "-tcpPort", required = true, description = "Enter the tcp port for the general communicaiton with the server")
	private int tcpPort;

	// set the udp port to send isAlive packets to the server 
	@Parameter(names = "-udpPort", required = true, description = "Enter the udp port for the isAlive packets")
	private int udpPort;

	// set the ip addres and port from the server
	@Parameter(names = "-serverData", required = true, description = "Enter the ip address and port from tthe server. e.g. 127.0.0.1:1234")
	private String serverConnection;

	// set the path to the shared folder on the peer
	@Parameter(names = "-sharedDir", required = true, description = "Enter the path to the shared folder")
	private String sharedDir;

	// set the debug parameter, if true debug infromation will be shown
	@Parameter(names = "-debug", description = "Show debug information")
	private boolean debug = false;
	
	// to display help infromation
	@Parameter(names = "-help", description = "Display the help menue.", help = true)
	private boolean help = false;

	/**
	 * @return the tcpPort
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * @param tcpPort the tcpPort to set
	 */
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	/**
	 * @return the udpPort
	 */
	public int getUdpPort() {
		return udpPort;
	}

	/**
	 * @param udpPort the udpPort to set
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	/**
	 * @return the sharedDir
	 */
	public String getSharedDir() {
		return sharedDir;
	}

	/**
	 * @param sharedDir the sharedDir to set
	 */
	public void setSharedDir(String sharedDir) {
		this.sharedDir = sharedDir;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return the help
	 */
	public boolean isHelp() {
		return help;
	}

	/**
	 * @param help the help to set
	 */
	public void setHelp(boolean help) {
		this.help = help;
	}

	/**
	 * @return the serverConnection
	 */
	public String getServerConnection() {
		return serverConnection;
	}

	/**
	 * @param serverConnection the serverConnection to set
	 */
	public void setServerConnection(String serverConnection) {
		this.serverConnection = serverConnection;
	}
}
