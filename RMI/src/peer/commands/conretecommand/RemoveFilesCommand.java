package peer.commands.conretecommand;

import java.util.ArrayList;
import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.RemoveFilesCommandImpl;
import server.registry.PeerFile;
/**
 * This class represents a concrete command for the peer. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class RemoveFilesCommand implements Command {
	
	private ArrayList<PeerFile> files;
	private int port;
	/**
	 * The constructor initialize the attributes.
	 * @param removeFiles the implementation of to remove files
	 * @param files the files which should be removed (a list)
	 */
	public RemoveFilesCommand(ArrayList<PeerFile> files, int port) {
		this.files = files;
		this.port = port;
	}
	/**
	 * The method creates an object of the implementation to remove files.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new RemoveFilesCommandImpl(this.files, this.port);
	}
}
