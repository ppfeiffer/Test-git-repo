package peer.commands.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import peer.commands.CommandInterface;
import server.registry.PeerFile;
/**
 * This class is the implementation to remove files.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class RemoveFilesCommandImpl extends CommandInterface {

	private static final long serialVersionUID = 1L;
	
	ArrayList<PeerFile> fileList;

	public RemoveFilesCommandImpl(ArrayList<PeerFile> fileList, int port) {
		super(port);
		this.fileList = fileList;
	}
	
	/**
	 * 
	 * @param files
	 */
	public void removeFiles(ArrayList<File> files) {
		fileList = new ArrayList<PeerFile>();
		Iterator<File> it = files.iterator();
		while (it.hasNext()) {
			File tmp = it.next();
			fileList.add(new PeerFile(tmp.getName(), tmp.length()));
		}
	}

	/**
	 * @return the fileList
	 */
	public ArrayList<PeerFile> getFileList() {
		return fileList;
	}

	/**
	 * @param fileList 	the fileList to set
	 */
	public void setFileList(ArrayList<PeerFile> fileList) {
		this.fileList = fileList;
	}
}
