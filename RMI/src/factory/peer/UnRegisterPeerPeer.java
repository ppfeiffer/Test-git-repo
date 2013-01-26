package factory.peer;

import peer.commands.operations.UnRegisterPeerCommandImpl;
import server.conmanager.CommandWrapper;

import LogManager.LogManager;
import factory.products.UnRegisterPeer;
/**
 * Concrete product for peer unregister peer
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class UnRegisterPeerPeer extends UnRegisterPeer{
 
	public UnRegisterPeerPeer(LogManager lm) {
		super(lm);
	}
	 
	/**
	 * Method that will set the unregister command for the peer.
	 * 
	 * @param command commandwrapper witht the unRegisgerPeer to send over the the network
	 * @return
	 */
	public CommandWrapper unregister(CommandWrapper command) {
		System.out.println("Peer will disconnect now....");
		// set the tcp peer port
		command.setWrapp(new UnRegisterPeerCommandImpl(command.getTcpPort()));
		return command;
	}
}
 
