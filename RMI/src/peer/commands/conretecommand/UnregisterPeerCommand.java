package peer.commands.conretecommand;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.UnRegisterPeerCommandImpl;
/**
 * This class represents a concrete command for the peer. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class UnregisterPeerCommand implements Command {

	private int port;
	/**
	 * The constructor initialize the attributes.
	 * @param port peer port
	 */
	public UnregisterPeerCommand(int port) {
		this.port = port;
	}
	/**
	 * The method creates an object of the implementation to remove files.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new UnRegisterPeerCommandImpl(this.port);
	}	
}
