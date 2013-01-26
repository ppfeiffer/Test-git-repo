package peer.commands.conretecommand;

import java.util.ArrayList;
import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.FilesOfPeerServerCommandImpl;

public class FilesOfPeerServerCommand implements Command {
	
	private ArrayList<String> peerFiles;
	private int port;
	/**
	 * This class represents a concrete command for the server. It creates an object of the implementation.
	 * 
	 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
	 * @version 2013.01.04
	 */
	public FilesOfPeerServerCommand(ArrayList<String> peerFiles, int port) {
		this.peerFiles = peerFiles;
		this.port = port;
	}
	
	/**
	 * The method creates an object of the implementation to list all files of a peer.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new FilesOfPeerServerCommandImpl(this.peerFiles, this.port);
	}
}
