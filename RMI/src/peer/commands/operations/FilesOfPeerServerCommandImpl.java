package peer.commands.operations;

import java.util.ArrayList;
import java.util.Iterator;

import peer.commands.CommandInterface;
import peer.commands.FilesOfPeerInterface;
/**
 * This class is the implementation to list all files of a peer.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FilesOfPeerServerCommandImpl extends CommandInterface implements FilesOfPeerInterface {

	private static final long serialVersionUID = 1L;
	
	ArrayList<String> peerFiles;

	/**
	 * The constructor initialize the attributes.
	 * 
	 * @param files list of the files that will be shown
	 */
	public FilesOfPeerServerCommandImpl(ArrayList<String> files, int port) {
		super(port);
		peerFiles = new ArrayList<String>();
		Iterator<String> it = files.iterator();
		while (it.hasNext()) {
			peerFiles.add(it.next());
		}
	}
	
	public ArrayList<String> getPeerFiles() {
		return peerFiles;
	}

	public void setPeerFiles(ArrayList<String> peerFiles) {
		this.peerFiles = peerFiles;
	}
}
