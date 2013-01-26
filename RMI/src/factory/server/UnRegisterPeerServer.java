package factory.server;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import server.registry.Registry;
import LogManager.LogManager;
import factory.products.UnRegisterPeer;

/**
 * Unregister the the peer from the server
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class UnRegisterPeerServer extends UnRegisterPeer {
	
	private Registry reg;

	public UnRegisterPeerServer(Registry reg, LogManager lm, boolean debug) {
		super(lm);
		this.reg = reg;
		// commandName = "unregisterPeer";
	}

	/**
	 * Unregister the peer from the server.
	 * 
	 * @param cw commandwrapper object with the host and port to delete the desire peer
	 * @return the commandwarpper object
	 */
	public CommandWrapper unregister(CommandWrapper cw) throws RemoteException {
		//System.out.println("Unregistring peer");
		
		if (reg.deletePeer(cw.getHost(),
				cw.getTcpPort())) {
			//System.out.println("Peer was successfully deleted from list");
		} else {
			
			//System.out.println("Coudn't delete peer from list!");
			throw new RemoteException("Coudn't delete peer from list!");
		}
		
		//System.out.println("Peer unregistred");
		return cw;
	}
}
