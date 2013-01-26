package factory.peer;

import java.rmi.RemoteException;

import peer.commands.CommandInterface;
import peer.commands.operations.RemoveFilesCommandImpl;
import server.conmanager.CommandWrapper;

import LogManager.LogManager;
import factory.products.RemoveFiles;
/**
 * Concrete product for peer remove files
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class RemoveFilesPeer extends RemoveFiles {
 
	public RemoveFilesPeer(LogManager lm) {
		super(lm,true);
		// commandName = "addFiles";
	}

	/**
	 * Check if the list to add is valid. 
	 * 
	 * @param cw commandwrapper with the removefilescommand object to send to the server
	 * @return
	 * @throws RemoteException
	 */
	public CommandWrapper removeFiles(CommandWrapper cw) throws RemoteException {
		// get the wrapper
		RemoveFilesCommandImpl rf = (RemoveFilesCommandImpl) cw.getWrapp();
		// if elements in list
		if (rf.getFileList().size() > 0) {
			// set the wrapper object
			cw.setWrapp((CommandInterface) rf);
			return cw;
		} else {
			// throw remoteexception on error
			System.out.println("No Files to delete.");
			throw new RemoteException("No Files to delete.");
		}
	}

}
 
