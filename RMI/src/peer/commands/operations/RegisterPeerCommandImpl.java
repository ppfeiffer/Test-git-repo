package peer.commands.operations;

import peer.commands.CommandInterface;
/**
 * This class is the implementation to register a peer.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class RegisterPeerCommandImpl extends CommandInterface {

	private static final long serialVersionUID = 1L;

	private int timeout;
	/**
	 * The constructor initialize the attributes.
	 * @param port peer port
	 */
	public RegisterPeerCommandImpl(int port) {
		super(port);
	}
	/**
	 * The constructor initialize the attributes.
	 * @param port peer port
	 * @param timeout timeout
	 */
	public RegisterPeerCommandImpl(int port, int timeout) {
		super(port);
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
