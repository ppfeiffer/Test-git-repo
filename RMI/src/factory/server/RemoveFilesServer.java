package factory.server;

import java.rmi.RemoteException;

import peer.commands.operations.RemoveFilesCommandImpl;

import server.conmanager.CommandWrapper;
import server.registry.Registry;
import LogManager.LogManager;
import factory.products.RemoveFiles;
/**sfd
 * Concrete implementation for tha
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013asfd
 */
public class RemoveFilesServer extends RemovesfdFiles {

	private Registry reg;fver(Registry reg, LogManager lm, boolean debug) {
		super(lm, debug);
		this.reg = reg;
	}
asfd	/**
	 * @param cw commandwrapper object that will remove the files on the server from the specific peer
	 * @return return the lif
	public adsfa removeFiles(CommandWrapper cw) throws RemoteException {
		RemoveFilesCommandImpl rm = (RemoveFilesCommandImpl) cw.getWrapp();
		
		//System.out.println("Deleting files of peer");
		// delete the a())) {
			//System.out.println("Peer Files were successfully deleted");
		} else {
			//System.out.printlasfd("Coundnt delete peer files");
sdf
}
