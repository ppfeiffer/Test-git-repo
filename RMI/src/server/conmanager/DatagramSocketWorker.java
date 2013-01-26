package server.conmanager;

import java.net.DatagramPacket;

import LogManager.LogManager;

import server.registry.Registry;
import threadhandling.ThreadPoolingTCP;

/**
 * This Class is one of two WorkerThreads, it is the specific Worker for the UDP
 * Port. It processes isAlive Packages from the Peer.
 * 
 * @author Ribarich Michael
 * @version 2013-01-20
 */
public class DatagramSocketWorker implements Runnable {
	private boolean stopped = false;
	private boolean isAlive = false;
	private boolean debug = false;
	private ThreadPoolingTCP tptcp = null;
	private LogManager lm = null;
	private DatagramPacket packet;
	private Registry registry;
	private int peerTcpPort;

	/**
	 * This Constructor is for the implementation of an DatagramSocket Worker.
	 * 
	 * @param registry
	 * @param packet
	 * @param debug
	 * @param lm
	 */
	public DatagramSocketWorker(Registry registry, DatagramPacket packet,
			boolean debug, LogManager lm) {
		this.packet = packet;
		this.debug = debug;
		this.lm = lm;
		this.packet = packet;
		this.registry = registry;
	}

	// Worker-Logic
	public void run() {
		int port = 0;

		try {
			// get the port
			port = Integer.parseInt(new String(this.packet.getData(), 0, packet
					.getLength()));
		} catch (NumberFormatException e) {
			System.out.println("invalid port sent on udp is alive");
		}
		
		// getting host address
		String host = this.packet.getAddress().getHostAddress();
		//registry.printDetailedPeerFileInformaiton();
		if (registry.findPeer(this.packet.getAddress().getHostAddress(), port)) {
			
			//System.out.println("Peer is registred");
			
			lm.write("UDP Output:");

			lm.write("UDP Worker Thread awaiting packages");
			lm.write("Data received: "
					+ new String(this.packet.getData(), 0, packet.getLength()));
			lm.write("PACKET FORM: host: " + host);
			lm.write("Packet FROM: port: " + port);
			lm.write("Packet size: " + packet.getLength());

			lm.write("--------------------");

		}else
		{
			lm.write("Peer not registred; stop sending pls");
		}
	}

	/**
	 * Getter-Method for the isAlive bool.
	 * 
	 * @return
	 */
	public boolean getIsAlive() {
		return this.isAlive;
	}

	/**
	 * Setter-Method for the isAlive bool
	 * 
	 * @param isAlive
	 */
	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	/**
	 * -Method for the bool, which controls the state of the Thread
	 * 
	 * @param stopped
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}