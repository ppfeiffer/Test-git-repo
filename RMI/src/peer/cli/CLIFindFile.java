package peer.cli;

import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * This class if a findFile command was entered in the
 * command line console.
 * 
 * @author Paul Pfeiffer-Vogl(ppfeiffer@student.tgm.ac.at)
 * @version 20-Jan-2013
 */
public class CLIFindFile {
	// the given main parameter will be stored to store the file names
	@Parameter(description="The main parameters.", required=true)
	private List<String> fileName;
	
	// exact search yes/no
	@Parameter(names="-e",description="Exact search yes/no", required=false)
	private boolean exact;
	
	// display help information yes/no
	@Parameter(names="-help", description="Display help information.", required=false, help=true)
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
	 * @return the exact
	 */
	public boolean isExact() {
		return exact;
	}

	/**
	 * @param exact the exact to set
	 */
	public void setExact(boolean exact) {
		this.exact = exact;
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
