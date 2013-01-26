package factory.products;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import LogManager.LogManager;

/**
 * Abstract product for find file
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public abstract class FindFile {
	private boolean debug = false;
	private LogManager lm = null;
	
	public FindFile(LogManager lm) {
		//commandName = "addFiles";
		this.lm = lm;
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
	
	// methods for the products to override
	public abstract CommandWrapper findFiles(CommandWrapper cw) throws RemoteException;
	public abstract CommandWrapper findFilesOfPeer(CommandWrapper cw) throws RemoteException;
}
 
