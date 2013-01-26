package peer.commands.operations;

import peer.commands.CommandInterface;
/**
 * This class is the implementation to unregister a peer.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class UnRegisterPeerCommandImpl extends CommandInterface {
	
	private static final long serialVersionUID = 1L;
	
	private int port;
	/**
	 * The constructor initialize the attributes.
	 * @param port port
	 */
	public UnRegisterPeerCommandImpl(int port) {
		super(port);
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
