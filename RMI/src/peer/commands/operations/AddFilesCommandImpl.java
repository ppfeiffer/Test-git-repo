package peer.commands.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import peer.commands.CommandInterface;
import peer.directory.FileManager;
import server.registry.PeerFile;

/**
 * This class will hold the list of files that should be added
 * on the server.
 * 
 * @author Paul Pfeiffer-Vogl, Schneider Kevin
 * @version 20-Jan-2013
 */
public class AddFilesCommandImpl extends CommandInterface {

	private static final long serialVersionUID = 1L;
	// list of files that should be added on the server side
	ArrayList<PeerFile> fileList = null;
	
	/**
	 * @param fm object of the filemanger, all files that the filenamager has will be added
	 * @param tcp the peer tcp port
	 */
	public AddFilesCommandImpl( FileManager fm, int port) {
		super(port);
		addFileManager(fm);
	}

	/**
	 * @param files the files that should be added (a list)
	 * @param tcp the peer tcp port
	 */
	public AddFilesCommandImpl(	ArrayList<PeerFile> files, int port) {
		super(port);
		addFiles(files);
	}

	/**
	 * The method adds the files to the filelist.
	 * @param files files that will be added
	 */
	public void addFiles(ArrayList<PeerFile> files) {
		fileList = new ArrayList<PeerFile>();

		Iterator<PeerFile> it = files.iterator();

		while (it.hasNext()) {
			PeerFile tmp = it.next();
			fileList.add(new PeerFile(tmp.getFileName(), tmp.getSize()));
		}
	}
	
	/**
	 * The method adds the filemanager to the filelist.
	 * @param fm filemanager that will be added
	 */
	public void addFileManager(FileManager fm) {
		fileList = new ArrayList<PeerFile>();
		fm.scanSharedFolder();

		Iterator<File> it = fm.getFileList().iterator();

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
	 * @param fileList the fileList to set
	 */
	public void setFileList(ArrayList<PeerFile> fileList) {
		this.fileList = fileList;
	}
}
