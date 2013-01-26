package peer.commands.operations;

import peer.commands.CommandInterface;

/**
 * This class is the implementation to find a file.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FindFilePeerCommandImpl extends CommandInterface {

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private boolean exact = false;
	/**
	 * The constructor initialize the attributes.
	 * @param fileName filename
	 * @param exact hte optional parameter [-e]
	 */
	public FindFilePeerCommandImpl(String fileName, boolean exact, int port) {
		super(port);
		this.fileName = fileName;
		this.exact = exact;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isExact() {
		return exact;
	}

	public void setExact(boolean exact) {
		this.exact = exact;
	}
}