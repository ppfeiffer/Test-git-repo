package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import LogManager.LogManager;
import server.UDPServer;

/**
 * This class will test the UDPServer on the server side
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 23-Jan-2013
 */
public class UDPServerTest {
	
	/**
	 * Test what will happen if two udp server start with the same parameters at the same time. The
	 * second server shouldn't be able to start
	 */
	@Test
	public void testMultipleUDPServerStart()
	{
		// UDP port start on server side: 2999; checkperiod: 2000; Peer Timeout: 3000
		UDPServer udpServer = new UDPServer(null, 2999, 2999, 2000,
			3000, true, new LogManager(true));
		
		// start the socket server
		if( udpServer.initializeSocket() )
		{
			new Thread(udpServer).start();
		}
		
		// create a second udp server
		UDPServer udpServer2 = new UDPServer(null, 2999, 2999, 2000,
				3000, true, new LogManager(true));
		
		// the second udp server shouldn't be able to starts
		assertEquals(udpServer2.initializeSocket(), false);
	}
}
