package peer.commands.conretecommand;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.DownloadCommandImpl;

/**
 * This class represents a concrete command. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class DownloadCommand implements Command {

	private String filename;
	private int port;

	public DownloadCommand(String filename, int port) {
		this.port = port;
		this.filename = filename;
	}
	
	/**
	 * The method creates an object of the implementation to add files.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new DownloadCommandImpl(this.filename, this.port);
	}
}
