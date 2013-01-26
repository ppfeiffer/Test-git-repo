package factory.products;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import LogManager.LogManager;

/**
 * abstract product for add files
 * 
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public abstract class AddFiles {
	private boolean debug = false;
	private LogManager lm = null;
	
	public AddFiles(LogManager lm, boolean debug) {
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
	
	// add files method definition
	public abstract CommandWrapper addFiles(CommandWrapper cw) throws RemoteException;
}
 
