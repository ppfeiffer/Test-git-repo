package factory.peer;

import java.rmi.RemoteException;

import peer.commands.operations.FilesOfPeerPeerCommandImpl;
import peer.commands.operations.FindFilePeerCommandImpl;

import server.conmanager.CommandWrapper;

import LogManager.LogManager;
import factory.products.FindFile;
/**
 * Concrete product for peer find file
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class FindFilePeer extends FindFile {

	public FindFilePeer(LogManager lm, boolean debug) {
		super(lm);
	}

	/**
	 * write log to logfile
	 * 
	 * @param cw commandwrapper object
	 * @return
	 * @throws RemoteException
	 */
	public CommandWrapper findFiles(CommandWrapper cw) throws RemoteException {
		super.getLm().write("Called findFils from peer");
		return cw;
	}

	/**
	 * write log to logfile
	 * 
	 * @param cw commandwarpper object
	 * @return
	 * @throws RemoteException
	 */
	public CommandWrapper findFilesOfPeer(CommandWrapper cw) throws RemoteException{
		// get object
		super.getLm().write("Called find files of peer from peer");
		return cw;
	}

}
