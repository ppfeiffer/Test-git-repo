package peer.commands.conretecommand;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.FilesOfPeerPeerCommandImpl;
/**
 * This class represents a concrete command for the peer. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FilesOfPeerPeerCommand implements Command {
	
	private String host;
	private int port;
	private int peerTcp;
	/**
	 * The constructor initialize the attributes.
	 * @param filesOfPeerPeer CommandImpl of FilesOfPeerPeer
	 * @param host host ip
	 * @param port port
	 */

	public FilesOfPeerPeerCommand(String host, int port, int peerTcp) {
		this.host = host;
		this.port = port;
		this.peerTcp = peerTcp;
	}
	/**
	 * The method creates an object of the implementation to list all files of a peer.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new FilesOfPeerPeerCommandImpl(this.host, this.port, this.peerTcp);
	}
}
