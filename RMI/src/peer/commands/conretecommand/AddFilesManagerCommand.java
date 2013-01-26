package peer.commands.conretecommand;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.AddFilesCommandImpl;
import peer.directory.FileManager;

/**
 * Concrete command that will create the addFile object that will be sent 
 * over the network.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class AddFilesManagerCommand implements Command {

	private FileManager fm;
	private int port;
	
	/**
	 * Constructor of the class
	 * 
	 * @param fm FileManager object
	 * @param port peer port
	 */
	public AddFilesManagerCommand(FileManager fm, int port) {
		this.fm = fm;
		this.port = port;
	}

	/**
	 * Create a new AddFilesCommand Impl class that will be sent 
	 * over the network
	 */
	@Override
	public CommandInterface execute() {
		return new AddFilesCommandImpl(this.fm, this.port);
	}
}
