package peer.commands.conretecommand;

import java.util.ArrayList;

import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.AddFilesCommandImpl;
import peer.directory.FileManager;
import server.registry.PeerFile;

/**
 * This class represents a concrete command. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class AddFilesCommand implements Command {

	private ArrayList<PeerFile> files;
	private int port;
	private FileManager fm;
	
	/**
	 * Constructor of the class
	 * 
	 * @param files list of files to add
	 * @param port of the peer server
	 */
	public AddFilesCommand(ArrayList<PeerFile> files, int port) {
		this.files = files;
		this.port = port;
	}
	
	/**
	 * Constructor of the lass
	 * 
	 * @param fm Filemanager that has access to the shared folder. 
	 * @param port the port of the 
	 */
	public AddFilesCommand(FileManager fm, int port) {
		this.fm = fm;
		this.port = port;
	}
	
	/**
	 * The method creates an object of the implementation to add files.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		if( fm == null)
		{
			return new AddFilesCommandImpl(this.files, this.port);
		}
		else
			return new AddFilesCommandImpl(this.fm, this.port);
	}
}