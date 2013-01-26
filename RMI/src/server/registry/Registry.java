package server.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class handels alle the registered peer. The peer is saved in a
 * ConcurrentHashMap. For the key we use the port of the peer and for the value
 * we use a object from the class SessionHandler.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 2013-01-06
 */

public class Registry {
	CopyOnWriteArrayList<PeerIdentification> peers;
	// this Attribute saves all the registered peers
	private ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> peerRegistery;

	/**
	 * Default constructor will be overwritten
	 */
	public Registry() {
		// create a new Object of our peer registery
		peerRegistery = new ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>>();
		peers = new CopyOnWriteArrayList<PeerIdentification>();
	}

	/**
	 * @return the peerRegistery
	 */
	public ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> getPeerFileRegistery() {
		return peerRegistery;
	}

	/**
	 * @param peerRegistery
	 *            the peerRegistery to set
	 */
	public void setPeerFileRegistery(
			ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> peerRegistery) {
		this.peerRegistery = peerRegistery;
	}

	/**
	 * This class add a peer to the registery, if the peer not already exists in
	 * the registery. The method is synchronized to be save from reading and
	 * writing changes during the saving.
	 * 
	 * @param port
	 *            port of the peer
	 * @param sh
	 *            SessionHandler class
	 */
	public synchronized void addPeerFile(PeerFile key, PeerIdentification pf) {
		// check if parameter valuses are valid
		if (key != null && pf != null) {
			// check if the new peer not already exists in the registery
			if (!peerRegistery.containsKey(key)) {
				// adding new peer file
				// Wrap it in a wrapper that adds synchronization to the
				// ArrayList
				
				CopyOnWriteArrayList<PeerIdentification> pfi = new CopyOnWriteArrayList<PeerIdentification>();
				pfi.add(pf);
				// if not, then add the new peer
				peerRegistery.putIfAbsent(key, pfi);
				
			} else if (peerRegistery.containsKey(key)) {
				//System.out.println("Adding another peer!");
				if( peerRegistery.containsValue(pf))
				{
					// adding new peer
					peerRegistery.get(key).add(pf);
				}
			} else {
				// when the peer existst in the registery, then the informaiton
				// will be printed
				System.out.println("This Peer withe the key " + key
						+ " already exists in the registery.");
			}
		}
		// if the entered parameters are not valid, then print a message
		else {
			System.out
					.println("Couldnt add Peer to the registery. Check the parameters.");
		}
	}

	/**
	 * This method delete a peer from the registery. The parameter value is the
	 * key and so it is very easy to find the specific peer.
	 * 
	 * @param port
	 *            port pf the peer, which schould be deleted
	 * @return
	 */
	public synchronized boolean deletePeerFile(String host, int port) {
		
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			
			CopyOnWriteArrayList<PeerIdentification> temp = entry.getValue();

			// synchronized (temp) {
			Iterator<PeerIdentification> it = temp.iterator();

			while (it.hasNext()) {
				PeerIdentification pfi = it.next();
				// if the SessionHandler objec is not null and the tcp
				// socket is up
				if (pfi.getHost() != null && pfi.getPort() > 0) {
					if (pfi.getKey().equals(host + ":" + port)) {
						//System.out.println("File of peer found: " + host + ":"
						//		+ port + ". Deleting File: "
						//		+ entry.getKey().getKey());

						if (entry.getValue().size() > 1) {
							//System.out
							//		.println("just remvoing one seeder of this file( ArrayList size: "
							//				+ entry.getValue().size() + ")");
							if(temp.remove(new PeerIdentification(host, port)))
							{
								//System.out.println("file successfuly removed");
							}
							
						} else if (entry.getValue().size() > 0) {
							// System.out.println("last owner");
							peerRegistery.remove(entry.getKey());

						} else {
							//System.out
							//		.println("THIS FILE SHOULD ALREADY BE REMOVED!");
						}

					} //else
						//System.out.println("Key wasnt equal");
				}
				// if the object is null or something else, then print a
				// message
				else {
					System.out
							.println("Error in PeerFileIdentification object of key "
									+ entry.getKey().getKey());
					return false;
				}
			}
			// }
		}
		return true;
	}

	/**
	 * This method prints all peers in the registery.
	 */
	public void listAllPeersFile() {
		// iterate through the peer registery
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// print the properties from each peer
			System.out.println("Peer with the key " + entry.getKey().getKey()
					+ " is in the registery.");
		}
		// if no peer is registered
		if (peerRegistery.isEmpty()) {
			// then we print a message
			//System.out.println("No Peers in the registery.");
		}
	}

	/**
	 * This method finds a peer in the peer registery. The peer is identified
	 * through the parameter value, which is also the key of our peer registery.
	 * 
	 * @param port
	 *            peer port, that schould be find
	 */
	public PeerFile findPeerFile(String key) {
		// this variable contains the success progress
		boolean success = false;
		// iterate through the peer registery
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// if we found the peer with the searched port
			if (entry.getKey().getKey().equals(key)) {
				// then we set the success progress to true
				success = true;
				// now we print the informaiton from the searched and found peer
				//System.out.println("Peer was found with the given key "
				//		+ entry.getKey().getKey());
				// returning peer file
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * This method finds a peer in the peer registery. The peer is identified
	 * through the parameter value, which is also the key of our peer registery.
	 * 
	 * @param port
	 *            peer port, that schould be find
	 */
	public ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> findByFileName(
			String fileName, boolean exact) {

		ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> ret = new ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>>();

		// iterate through the peer registery
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// if we found the peer with the searched port
			if (exact) {
				if (entry.getKey().getFileName().equals(fileName)) {
					ret.put(entry.getKey(), entry.getValue());

					// now we print the informaiton from the searched and found
					// peer
					//System.out.println("Peer was found with the given key "
					//		+ entry.getKey().getKey());
					// returning peer file

				}
			} else {
				if (entry.getKey().getFileName().contains(fileName)) {
					ret.put(entry.getKey(), entry.getValue());

					// now we print the informaiton from the searched and found
					// peer
					//System.out.println("Peer was found with the given key "
					//		+ entry.getKey().getKey());
					// returning peer file

				}
			}
		}

		return ret;
	}

	/**
	 * This method deletes all the peers in the registery.
	 */
	public void clearPeerFileRegistery() {
		// delete all peers in the registery
		peerRegistery.clear();
		// print a message
		System.out.println("All Peers in the register was deleted.");
	}

	/**
	 * This method prints from a specific peer the SessionHandler properties.
	 * 
	 * @param port
	 *            peer, that schould be pirnted in detail
	 */
	public void printPeerFileInformaiton(String key) {
		// set a helper variable to false
		boolean success = false;
		// iterate through the peer registery
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// if we found the serached peer
			if (entry.getKey().getKey().equals(key)) {
				// set the helper variable to true
				success = true;
				// now we create a SessionHandler object
				List<PeerIdentification> temp = entry.getValue();
				Iterator<PeerIdentification> it = temp.iterator();
				while (it.hasNext()) {
					PeerIdentification pfi = it.next();
					// if the SessionHandler objec is not null and the tcp
					// socket is up
					if (pfi.getHost() != null && pfi.getPort() > 0) {
						// then we print its status
						System.out.println("Peer's hostaddress: "
								+ pfi.getHost());
						System.out.println("Peer's tcp port: " + pfi.getPort());
					}
					// if the object is null or something else, then print a
					// message
					else {
						System.out
								.println("Error in PeerFileIdentification object of key "
										+ entry.getKey().getKey());
					}
				}

				PeerFile pf = entry.getKey();
				// if the SessionHandler objec is not null and the udp socket is
				// up
				if (pf.getKey() != null && pf.getSize() >= 0) {
					// then we print its status
					System.out.println("Peer Filename: " + pf.getFileName());
					System.out.println("Peer Filesize: " + pf.getSize());
				} 
			}
		}
	}

	public boolean deletePeer(String host, int port) {

		this.peers.remove(new PeerIdentification(host, port));

		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// print the properties from each peer
			CopyOnWriteArrayList<PeerIdentification> temp = entry.getValue();

			// synchronized (temp) {
			Iterator<PeerIdentification> it = temp.iterator();

			while (it.hasNext()) {
				PeerIdentification pfi = it.next();
				// if the SessionHandler objec is not null and the tcp
				// socket is up
				if (pfi.getHost() != null && pfi.getPort() > 0) {
					if (pfi.getKey().equals(host + ":" + port)) {
						// check if file is already stored in the registry
						if (entry.getValue().size() > 1) {
							temp.remove(new PeerIdentification(host, port));
						} else if (entry.getValue().size() > 0) {
							peerRegistery.remove(entry.getKey());
						}

					}
				}
				// if the object is null or something else, then print a
				// message
				else {
					System.out
							.println("Error in PeerFileIdentification object of key "
									+ entry.getKey().getKey());
					
					return false;
				}
			}
			// }
		}

		return true;
	}

	public boolean findPeer(String host, int port) {
		// iterate entire hashmap, check if the host is registred to any
		// peerfile
		if (this.peers.contains(new PeerIdentification(host, port))) {
			//System.out.println("In Reg: " + host + ":" + port);
			return true;
		}

		// if that peer wasn't found
		// System.out.print("Couldn't find Peer!");
		return false;
	}

	public boolean addPeer(String host, int port) {
		PeerIdentification pfi = new PeerIdentification(host, port);

		if (!this.peers.contains(pfi)) {
			peers.add(pfi);

			return true;
		} else {
			return false;
		}
	}

	public void printDetailedPeerFileInformaiton() {
		// iterate through the peer registery
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// print the properties from each peer

			System.out.println(entry.getKey().getFileName() + " ("
					+ ((float) (entry.getKey().getSize()) / 1024.0) + " KB)");
			// MISSIN LOOP FOR ALL PEERS THAT HAVE THE SAME FILE
			List<PeerIdentification> temp = entry.getValue();
			Iterator<PeerIdentification> it = temp.iterator();

			while (it.hasNext()) {
				PeerIdentification pfi = it.next();
				System.out.println('\t' + pfi.getHost() + ":" + pfi.getPort());
			}
		}
		// if no peer is registered
		if (peerRegistery.isEmpty()) {
			// then we print a message
			//System.out.println("No Peers in the registery.");
		}
	}

	/**
	 * @return the peerRegistery
	 */
	public ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> getPeerRegistery() {
		return peerRegistery;
	}

	/**
	 * @param peerRegistery
	 *            the peerRegistery to set
	 */
	public void setPeerRegistery(
			ConcurrentHashMap<PeerFile, CopyOnWriteArrayList<PeerIdentification>> peerRegistery) {
		this.peerRegistery = peerRegistery;
	}
	
	/**
	 * Find the file of a specific peer. This method will 
	 * 
	 * @param host the hostname to search for
	 * @param port the port to search for
	 * @return 
	 */
	public ArrayList<String> findByPeer(String host, int port) {
		ArrayList<String> ret = new ArrayList<String>();
		for (ConcurrentHashMap.Entry<PeerFile, CopyOnWriteArrayList<PeerIdentification>> entry : peerRegistery
				.entrySet()) {
			// print the properties from each peer
			
			//System.out.println("Peer with the key " + entry.getKey().getKey()
			//		+ " is in the registery.");
			List<PeerIdentification> temp = entry.getValue();
			Iterator<PeerIdentification> it = temp.iterator();
			// iterate the array
			while (it.hasNext()) {
				PeerIdentification pfi = it.next();
				// if the SessionHandler objec is not null and the tcp
				// socket is up
				if (pfi.getHost() != null && pfi.getPort() > 0) {
					if (pfi.getKey().equals(host + ":" + port)) {
						//System.out.println("Found peer holding that file:");

						ret.add(entry.getKey().getFileName() + " ("
								+ (entry.getKey().getSize() / 1024) + " KB)");

					}
				}
				// if the object is null or something else, then print a
				// message
				else {
					System.out
							.println("Error in PeerFileIdentification object of key "
									+ entry.getKey().getKey());
				}
			}
		}

		return ret;
	}
	
	/**
	 * List the entire list of peers.
	 */
	public void listAllPeers() {

		// iterate the list of registred peers and print them to console
		for(PeerIdentification x: this.peers)
		{
			System.out.println("Host: " + x.getHost() + " Port: " + x.getPort());
		}
	}
	
}
