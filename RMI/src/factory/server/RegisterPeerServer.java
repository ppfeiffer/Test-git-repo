package factory.server;

import java.rmi.RemoteException;

import peer.commands.CommandInterface;
import peer.commands.operations.RegisterPeerCommandImpl;

import server.conmanager.CommandWrapper;
import server.registry.Registry;
import LogManager.LogManager;
import factory.products.RegisterPeer;
/**
 * Concrete implementation for the register peer on the sever side.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class RegisterPeerServer extends RegisterPeer {

	private Registry reg;

	public RegisterPeerServer(Registry reg, LogManager lm, boolean debug) {
		super(lm);

		this.reg = reg;
	}

	/**
	 * This method will register the peer on the server.
	 * 
	 * @param outToClient
	 * @param tcpPort
	 * @return 1 if the Registration was successful, 0 if the client is already
	 *         registred, -1 if the registration failed
	 */
	public CommandWrapper registerPeer(CommandWrapper cw) throws RemoteException {
		// get the command object
		RegisterPeerCommandImpl rp = (RegisterPeerCommandImpl) cw.getWrapp();

		// get the tcp port of the client
		int tcpPort = rp.getPort();
		cw.setTcpPort(rp.getPort());
		
		
		//System.out.println("RegisterPeerServer called");
		//System.out.println("Writing back to client!");

		String host = cw.getHost();

		// if no peer with this key was already connected
		if (!reg.findPeer(host, tcpPort)) {

			if (host != null && tcpPort > 0 && tcpPort < 65536) 
			{
				System.out.println("Adding peer...");
				if (reg.addPeer(host, tcpPort) )
				{
					//System.out.println("PEER NOW IN REGISTRY");
				}

				// set new register peer command wrapper 
				cw.setWrapp((CommandInterface) new RegisterPeerCommandImpl(cw.getUdpPort(), cw.getPeerTimeout()));
				return cw;
			}else
			{
				//System.out.println("Invalid port given!" + cw.getTcpPort());
				throw new RemoteException("The given port was invalid ;(. Must be between 0 and 65536!");
			}
		} else {
			//System.out.println("Peer already registred! Don't even dare...");
			super.getLm().write("Peer already registred!");

			throw new RemoteException("Peer already registred! Don't even dare...");
		}
	}
}
