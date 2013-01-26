package factory.server;

import java.rmi.RemoteException;

import peer.commands.operations.RemoveFilesCommandImpl;

import server.conmanager.CommandWrapper;
import server.registry.Registry;
import LogManager.LogManager;
import factory.products.RemoveFiles;
/**
 * Concrete implementation for the remove files on the server side
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class RemoveFilesServer extends RemoveFiles {

	private Registry reg;

	public RemoveFilesServer(Registry reg, LogManager lm, boolean debug) {
		super(lm, debug);
		this.reg = reg;
	}
	
	/**
	 * @param cw commandwrapper object that will remove the files on the server from the specific peer
	 * @return return the list of files deleted
	 */
	public CommandWrapper removeFiles(CommandWrapper cw) throws RemoteException {
		RemoveFilesCommandImpl rm = (RemoveFilesCommandImpl) cw.getWrapp();
		
		//System.out.println("Deleting files of peer");
		// delete the files of the peer
		if (reg.deletePeerFile(cw.getHost(),
				cw.getTcpPort())) {
			//System.out.println("Peer Files were successfully deleted");
		} else {
			//System.out.println("Coundnt delete peer files");

			reg.listAllPeers();
		}

		return cw;
	}
}
