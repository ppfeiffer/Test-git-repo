package testing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import server.TCPServer;
import LogManager.LogManager;

/**
 * This class will test the TCP Server.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 23-Jan-2013
 */
public class TCPServerTest {
	@Before
	public void setUp(){}
	
	/**
	 * Test what will happen if severs start at the same time.
	 */
	@Test
	public void testMultipleServerStart()
	{
		// TCP port start on server side: 2999; checkperiod: 2000; Peer Timeout: 3000
		TCPServer tcpServer = new TCPServer(null, 8888, 8887, 3000,
			true, new LogManager(true));
		
		// create a second udp server
		TCPServer tcpServer2 = new TCPServer(null, 8888, 8887, 3000,
				true, new LogManager(true));
		// the first should be able to start
		assertEquals(tcpServer.initializeSocket(), true);
		// the second udp server shouldn't be able to starts
		assertEquals(tcpServer2.initializeSocket(), false);
		
	}
	
	/**
	 * This method will test what will happen if the tcp start server
	 * port is invalid.
	 */
	@Test
	public void testInvalidServerPort()
	{
		TCPServer tcpServer = new TCPServer(null, 90000, 8887, 3000,
				true, new LogManager(true));
		assertEquals(tcpServer.initializeSocket(), false);
	}
}
