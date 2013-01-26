package testing;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Before;
import org.junit.Test;

import server.registry.PeerFile;
import server.registry.PeerIdentification;
import server.registry.Registry;

/**
 * This class will test the function of the registry.
 * 
 * @author Paul Pfeiffer-Vogl(ppfeiffer@student.tgm.ac.at)
 * @version 23-Jan-2013
 *
 */
public class RegistryTest {

	private Registry reg = null;

	@Before
	public void setUp() {
		reg = new Registry();
	}
	
	/**
	 * This method will write objects to the registry. The
	 * registry is thread safe, so there shouldnt be any
	 * ConcurrentModificationExceptions.
	 */
	@Test
	public void testConcurrentAccess() {
		Thread[] t = new Thread[800];

		// create new thread object
		for (int i = 0; i < t.length; i++) {
			t[i] = new Thread() {
				// add 100 000 peers 
				public void run() {
					// add some peers
					for( int j = 0; 0 < 50; j ++ )
					{
						// add a peer
						reg.addPeer("127.0.0."+j+Math.random(), j);
						
						// thread sleep
						try {
							Thread.sleep((long) (Math.random()*500));
						} catch (InterruptedException e) {
							System.out.println("Error while thread slepp");
						}
					}
				}
			};
			// start the server
			t[i].start();
			
		}
	}
	/**
	 * This Method will check if the the Registry save function is properly working.
	 */
	@Test
	public void testDeletePeers() {
		reg = new Registry();
		// add some peer
		reg.addPeer("127.0.0.2", 1123);
		reg.addPeer("127.0.0.3", 456);
		
		reg.addPeer("127.0.0.1", 123);
		reg.addPeer("127.0.0.4", 24653);
		reg.addPeer("127.0.0.7", 632);

		// delete some peer
		reg.deletePeer("127.0.0.7", 632);
		reg.deletePeer("127.0.0.4", 24653);
		reg.deletePeer("127.0.0.1", 123);

		// check if they sill exist
		assertEquals(reg.findPeer("127.0.0.2", 1123), true );
		assertEquals(reg.findPeer("127.0.0.3", 456), true );
		// shouldn't exist
		assertEquals(reg.findPeer("127.0.0.7", 632), false );
	}
	
	/**
	 * This method will test, if a file will be registred twice to one peer. if he tries to do it again.
	 */
	@Test
	public void testAddTwoEqualFiles()
	{
		reg = new Registry();
		
		reg.addPeer("127.0.0.1", 7777);
		// add the two peer files to the registry
		reg.addPeerFile(new PeerFile("test.log", 100), new PeerIdentification("127.0.0.1", 7777));
		reg.addPeerFile(new PeerFile("test.log", 100), new PeerIdentification("127.0.0.1", 7777));
		
		// iterate the list
		for (Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : reg.findByFileName("test.log", true).entrySet() ) {
			// get the iterator
			Iterator<PeerIdentification> it = entry.getValue().iterator();
			// check if size is 1 -> if yes it's working
			assertEquals(entry.getValue().size(), 1);
			// check if the registred peer is the peer that we added above
			while (it.hasNext()) {
				PeerIdentification obj = it.next();
				System.out.println("\t" + obj.getHost() + ":" + obj.getPort());
				assertEquals(obj, new PeerIdentification("127.0.0.1", 7777));
				break;
			}
		}
		
	}
}
