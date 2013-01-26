package peer.commands;

/**
 * This class executes the execute - method of the command - object ( -> it triggers a command)
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class CommandInvoker {
	
	private Command command;

	/**
	 * @param command the command to set
	 */
	public void setCommand(Command command) {
		this.command = command;
	}
	
	/**
	 * This method executes the command object
	 */
	public CommandInterface action() {
		return command.execute();
	}
}
