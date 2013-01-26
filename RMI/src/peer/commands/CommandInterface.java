package peer.commands;

import java.io.Serializable;

/**
 * This interface is uesed by all Command Implemenations.
 * The port is handled.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public abstract class CommandInterface implements Serializable {

	private static final long serialVersionUID = 2392253902567136639L;
	
	private int port;
	
	/**
	 * Overwrite default constructor
	 * 
	 * @param port		set port
	 */
	public CommandInterface(int port) {
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
