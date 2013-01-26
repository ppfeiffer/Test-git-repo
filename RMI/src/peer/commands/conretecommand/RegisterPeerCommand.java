package peer.commands.conretecommand;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.RegisterPeerCommandImpl;
/**
 * This class represents a concrete command for the peer. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class RegisterPeerCommand implements Command {

	private int port;
	/**
	 * The constructor initialize the attributes.
	 * @param tcpPort TCP port of the peer
	 */
	public RegisterPeerCommand(int tcpPort) {
		this.port = tcpPort;
	}
	/**
	 * The method creates an object of the implementation to register a peer.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new RegisterPeerCommandImpl(this.port);
	}	
}

