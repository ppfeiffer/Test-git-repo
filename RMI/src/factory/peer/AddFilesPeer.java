package factory.peer;

import java.rmi.RemoteException;

import peer.commands.CommandInterface;
import peer.commands.operations.AddFilesCommandImpl;
import server.conmanager.CommandWrapper;

import LogManager.LogManager;
import factory.products.AddFiles;

/**
 * Concrete product for peer Add files
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class AddFilesPeer extends AddFiles {

	public AddFilesPeer(LogManager lm) {
		super(lm, true);
	}
	
	/**
	 * Setzten des AddFIles object das ueber das netzwerk gesendet wird
	 *
	 * @param return commandwrapper object
	 */
	public CommandWrapper addFiles(CommandWrapper cw) throws RemoteException {
		AddFilesCommandImpl af = (AddFilesCommandImpl) cw.getWrapp();

		if (af.getFileList().size() > 0) {
			cw.setWrapp((CommandInterface) af);
			
			return cw;
		} else {
			System.out.println("No Files to publish.");
			throw new RemoteException("No Files to publish.");
		}
	}

}
