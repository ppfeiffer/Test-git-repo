package peer.commands.operations;

import peer.commands.CommandInterface;

/**
 * This command will be sent to the other peer and represents
 * the file that he wants to download
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class DownloadCommandImpl extends CommandInterface {

	private static final long serialVersionUID = 1L;
	// the filename
	private String fileName;
	
	/**
	 * Constructor of the class
	 * 
	 * @param fileName the filename that should be downloaded
	 * @param port the port of the tcp server that receivs connection
	 */
	public DownloadCommandImpl(String fileName, int port) {
		super(port);
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
