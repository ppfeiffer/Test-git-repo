package peer.cli;

import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * This class check the user input in the command line.
 * 
 * @author Paul Pfeiffer-Vogl(ppfeiffer@student.tgm.ac.at)
 * @version 20-Jan-2013
 */
public class CLICommand {
	// the given main parameter will be stored to store the file names
	@Parameter(description="The main parameter. For more detailed information please type -help", required=true)
	private List<String> fileName;
	// display help information yes/no
	@Parameter(names="-help", description="Please type -help into console for information", required=false, help=true)
	private boolean help;

	/**
	 * @return the fileName
	 */
	public List<String> getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the help
	 */
	public boolean isHelp() {
		return help;
	}

	/**
	 * @param help the help to set
	 */
	public void setHelp(boolean help) {
		this.help = help;
	}	
}
