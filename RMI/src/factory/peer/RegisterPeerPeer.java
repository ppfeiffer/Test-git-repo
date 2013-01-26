package factory.peer;

import java.rmi.RemoteException;

import server.conmanager.CommandWrapper;
import LogManager.LogManager;
import factory.products.RegisterPeer;
/**
 * Concrete product for peer register peer
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class RegisterPeerPeer extends RegisterPeer {

	public RegisterPeerPeer(LogManager lm) {
		super(lm);
	}

	/**
	 * Set the new registerpeer wrapper.
	 * 
	 * @param cw commandwrapper object
	 * @return the new commandwrapper object
	 * @throws RemoteException 
	 */
	public CommandWrapper registerPeer(CommandWrapper cw) throws RemoteException {
		
		// check if port is valid
		if( cw.getTcpPort() < 0 || cw.getTcpPort() > 65536 )
			throw new RemoteException("Invalid Register port");
		
		// set the tcp port
		cw.getWrapp().setPort(cw.getTcpPort());
		
		return cw;
	}
}
