package factory.products;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import LogManager.LogManager;

/**
 * Abstract product for register peer.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public abstract class RegisterPeer{
	private boolean debug = false;
	private LogManager lm = null;
	
	public RegisterPeer(LogManager lm) {
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
	public abstract CommandWrapper registerPeer(CommandWrapper cw) throws RemoteException;
}
 
