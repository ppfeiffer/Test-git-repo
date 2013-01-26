package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import LogManager.LogManager;
import factory.ServerCommandFactory;

import peer.transfer.IsAlive;
import server.UDPServer;
/**
 * This class will test if the the is alive function 
 * is working correctly
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 
 */
public class IsAliveTest {
	@Before
	public void setUp(){}
	
	/**
	 * This method will test what will happen if a peer times out.
	 * 
	 */
	@Test
	public void testIsAlive()
	{
		IsAlive keepAlive = new IsAlive(null, "127.0.0.1",
				0, 7777, 7776,
				10000, 5000, true, new LogManager(true));
		// starte the isalive thread
		assertEquals(keepAlive.initializeSocket(), true);
		// start the thread
		new Thread(keepAlive).start();
		
		// create the objects
		LogManager lm = new LogManager(true);
		ServerCommandFactory abst = new ServerCommandFactory(lm);
		// add the peer to registry 
		abst.getReg().addPeer("127.0.0.1", 7776);
		
		UDPServer server = new UDPServer(abst, 7777, 7776, 2500, 5000, false, lm);
		// initializing the udp server
		assertEquals( server.initializeSocket(), true);
		new Thread(server).start();
		
		
		// peer should be in registry
		assertEquals( abst.getReg().findPeer("127.0.0.1", 7776), true);
		
		// set is alive thread stopped
		keepAlive.setStopped(true);
		// wait some time to let the peer time out
		try {
			Thread.sleep(23000);
		} catch (InterruptedException e) {
			System.out.println("Error while thread sleep");
		}
		
		// peer should be timed out
		assertEquals( abst.getReg().findPeer("127.0.0.1", 7776), false);
		
		// close the sockets
		server.getUdpSocket();
		keepAlive.close();
	}
}
