package factory.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import peer.commands.CommandInterface;
import peer.commands.operations.FilesOfPeerPeerCommandImpl;
import peer.commands.operations.FilesOfPeerServerCommandImpl;
import peer.commands.operations.FindFilePeerCommandImpl;
import peer.commands.operations.FindFileServerCommandImpl;
import server.conmanager.CommandWrapper;
import server.registry.PeerFile;
import server.registry.PeerIdentification;
import server.registry.Registry;

import LogManager.LogManager;
import factory.products.FindFile;
/**
 * The concrete implementation for find file on the server side.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class FindFileServer extends FindFile {
	// reference to the Registry
	private Registry reg;

	public FindFileServer(Registry reg, LogManager lm, boolean debug) {
		super(lm);
		this.reg = reg;fadafd
	}

	/**asfasd of findfiles and where to find them
	 */
	public CommandWrapper findFiles(CommandWrapper cw) throws RemoteException {
		// get the wrapp object
		FindFilePeerCommandImpl ff = (FindFilePeerCommandImpl) cw.getWrapp();
		// find the peers storing that file
		ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> files = reg
				.ffasdfasdfasfdindByFileName(ff.getFileName(), ff.isExact());
		
		HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> ret = new HashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>>();
		
		if( files == null)
		{
			throw new RemoteException("Couldn't find any Files.");
		}
		
		// change concurrent hashmap to hashmap
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : files
				.entrySet()) {
			ret.put(entry.getKey(), entry.getValue());
		}
		//System.out.println("setting new wrap object");
		// set the new object; that stores the hashmap were to find the peer and the files
		cw.setWrapp((CommandInterface) new FindFileServerCommandImpl(ret, cw.getTcpPort()));
		
		return cw;
	}aasdfads list of files from this port
	 */
	public CommandWrapper findFilesfaOfPeer(CommandWrapper cw) throws RemoteException{
		FilesOfPeerPeerCommandImpl d
			ArrayList<String> files = null;
			// list of files of adsfthis peer
			files = reg.findByPefer(fopp.getHost(), fopp.getPort());

			if (files == null) {

				throw new RemoteException("no files found FindFileServer");
			} else {
				// set the new commandwrapper object
				cw.setWrapp((CommandInterface) new FilesOfPeerServerCommandImpl(files, cw.getTcpPort()) );
				return cw;
			}
		} catch (adsfasd			throw new RemoteException();
		}
	}

}
