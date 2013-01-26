package peer.commands.operations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import peer.commands.CommandInterface;
import server.registry.PeerFile;
import server.registry.PeerIdentification;
/**
 * This class is the implementation to find a file.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FindFileServerCommandImpl extends CommandInterface {
	
	private static final long serialVersionUID = 1L;
	
	private HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> listOfFile;
	/**
	 * The constructor initialize the attributes.
	 * @param ret a list of <PeerFile, CopyOnWriteArrayList<PeerIdentification>
	 */
	public FindFileServerCommandImpl(HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> ret, int port) {
		super(port);
		this.listOfFile = ret;
	}

	/**
	 * @return the listOfFile
	 */
	public HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> getListOfFile() {
		return listOfFile;
	}

	/**
	 * @param listOfFile the listOfFile to set
	 */
	public void setListOfFile(
			HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> listOfFile) {
		this.listOfFile = listOfFile;
	}
	
	/**
	 * This method prints all peer with his files
	 */
	public void printList() {
		for (Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : listOfFile.entrySet()) {
			System.out.println(entry.getKey().getFileName() + " ("+ (entry.getKey().getSize() / 1024) + " KB)");
			Iterator<PeerIdentification> it = entry.getValue().iterator();
			while (it.hasNext()) {
				PeerIdentification obj = it.next();
				System.out.println("\t" + obj.getHost() + ":" + obj.getPort());
			}
		}
	}
}
