package peer.commands.conretecommand;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.FindFilePeerCommandImpl;
/**
 * This class represents a concrete command for the peer. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FindFilePeerCommand implements Command{

	private boolean exact;
	private String fileName;
	private int port;
	
	/**
	 * The constructor initialize the attributes.
	 * @param fileName filename
	 * @param exact the optional paramter [-e]
	 */
	public FindFilePeerCommand(String fileName, boolean exact, int port) {
		this.fileName = fileName;
		this.exact = exact;
		this.port = port;
	}

	/**
	 * The method creates an object of the implementation to find a file.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new FindFilePeerCommandImpl(this.fileName, this.exact, this.port);
	}	
}
