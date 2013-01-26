package factory.products;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import LogManager.LogManager;

/**
 * Abstract product for remove files.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public abstract class RemoveFiles {
	private boolean debug = false;
	private LogManager lm = null;
	
	public RemoveFiles(LogManager lm, boolean debug) {
		// commandName = "addFiles";
		this.lm = lm;
		this.debug = debug;
	}
	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the lm
	 */
	public LogManager getLm() {
		return lm;
	}
	
	/**
	 * register peer abstract method
	 * @param CommandWrapper the object which wrapps the command
	 * @return CommandWrapper the object which wrapps the command
	 */
	public abstract CommandWrapper removeFiles(CommandWrapper cw) throws RemoteException;
}
 
