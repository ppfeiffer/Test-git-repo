package peer.commands.conretecommand;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import peer.commands.Command;
import peer.commands.CommandInterface;
import peer.commands.operations.FindFileServerCommandImpl;
import server.registry.PeerFile;
import server.registry.PeerIdentification;
/**
 * This class represents a concrete command for the server. It creates an object of the implementation.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FindFileServerCommand implements Command{
	
	private HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> listOfFile;
	private int port;

	/**
	 * The constructor initialize the attributes.
	 * @param listOfFile a hash map with <PeerFile, CopyOnWriteArrayList<PeerIdentification>
	 */
	public FindFileServerCommand(HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> listOfFile, int port) {
		this.listOfFile = listOfFile;
		this.port = port;
	}

	/**
	 * The method creates an object of the implementation to find a file.
	 * @return CommandInterface all concrete commands belong to the command interface.
	 */
	@Override
	public CommandInterface execute() {
		return new FindFileServerCommandImpl(new HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>>(), this.port);
	}
}
