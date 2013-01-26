package factory.products;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import LogManager.LogManager;
/**
 * Abstract product for un register peer
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public abstract class UnRegisterPeer{
	private boolean debug = false;
	private LogManager lm = null;
	
	public UnRegisterPeer(LogManager lm) {
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
	
	/**
	 * register peer abstract method
	 * @param CommandWrapper the object which wrapps the command
	 * @return CommandWrapper the object which wrapps the command
	 */
	public abstract CommandWrapper unregister(CommandWrapper cw) throws RemoteException;
}
 