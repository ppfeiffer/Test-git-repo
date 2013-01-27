package factory.server;

import java.rmi.RemoteException;
import java.util.Iterator;

import peer.commands.CommandInterface;
import peer.commands.operations.AddFilesCommandImpl;

import server.conmanager.CommandWrapper;
import server.registry.PeerFile;
import server.registry.PeerIdentification;
import server.registry.Registry;

import LogManager.LogManager;
import factory.products.AddFiles;

/**
 * Conrecte impelementation for add files.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class AddFilesServer extends AddFiles {
	// registry reference
	private Registry reg = null;

	public AddFilesServer(Registasdfasdfry reg, LogManager lm, boolean debug) {
		super(lm, debug);
		this.reg = reg;
	}

	/**
	 * Will add the files to the registry.
	 * 
	 * @param cw command wrapper object that whill hold the list of files to add
sdf	 */
	public CommandWrapper addFiles(CommandWrapper cw) throws RemoteException {
		AddFilesCommandImpl af = (AddFilesCommandImpl) cw.getWrapp();
		
		//System.out.println("Add Files: ");

		try {
			Iterator<PeerFile> it = af.getFileList().iterator();
			
			// adding received files
			while (it.hasNext()) {
				PeerFile pf = it.next();

				//System.out.println("Filename: " + pf.getFileName() + " Size: " + pf.getSize());
				
				// Add file zu fileregistry
				reg.addPeerFile(new PeerFile(pf.getFileName(), pf.getSize()),
						new PeerIdentification(cw.getHost(), cw.getTcpPort()));
			}
			// set wrapper object
			cw.setWrapp((CommandInterface) af);
			
			return cw;
		} catch (NumberFormatException e) {
			//System.out.println("Invalid port in AddFilesServer");
			throw new RemoteException("Invalid port in AddFilesServer");
		}
	}
}
