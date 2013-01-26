package server.conmanager;

import peer.commands.CommandInterface;

/**
 * This class wrapps a command. It only stores the information about ports and timeout.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class CommandWrapper {
	private CommandInterface wrapp;
	private String host;
	private int tcpPort;
	private int udpPort;
	private int peerTimeout;

	/**
	 * The constructor initialize the attributes.
	 * @param wrapp the interface for a command
	 * @param host host ip
	 * @param tcpPort tcp port
	 * @param udpPort upd port
	 */
	public CommandWrapper(CommandInterface wrapp, String host, int tcpPort, int udpPort) {
		this.wrapp = wrapp;
		this.host = host;
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}

	public CommandWrapper() {
	}

	/**
	 * @return the wrapp
	 */
	public CommandInterface getWrapp() {
		return wrapp;
	}

	/**
	 * @param wrapp
	 *            the wrapp to set
	 */
	public void setWrapp(CommandInterface wrapp) {
		this.wrapp = wrapp;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

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
	 * @return the peerTimeout
	 */
	public int getPeerTimeout() {
		return peerTimeout;
	}

	/**
	 * @param peerTimeout the peerTimeout to set
	 */
	public void setPeerTimeout(int peerTimeout) {
		this.peerTimeout = peerTimeout;
	}
}
