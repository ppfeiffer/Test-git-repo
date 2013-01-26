package peer.commands;

/**
 * Interface with with the execute method. All
 * Commands have to implement it.
 * 
 * @author Harun Koyuncu
 * @version 20-Jan-2013
 */
public interface Command {
	
	public CommandInterface execute();
	
}
