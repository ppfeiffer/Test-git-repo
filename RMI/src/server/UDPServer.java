package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import factory.AbstractCommandFactory;
import factory.ServerCommandFactory;

import LogManager.LogManager;

import server.conmanager.DatagramSocketWorker;
import server.registry.PeerIdentification;
import threadhandling.ThreadPooling;
import threadhandling.ThreadPoolingUDP;

/**
 * This class initialize the UDP Socket, also the socket can be stopped.
 * The Threadpool for udp is created. 
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 08-Jan-2013
 */
public class UDPServer implements Runnable {
	// port on which the upd worker threads should listen
	private int updServer;
	// The period in ms each peer has to send an “isAlive” packet
	private int peerTimeout;
	// It specifies the time in ms between two peer checks
	private int checkPeriod;
	// object that will store the datagram socket
	private DatagramSocket udpSocket = null;
	// display debug information
	private boolean debug;
	// will handle the threads
	private ThreadPoolingUDP pool;
	// stores if the thread was stopped or wasn't
	private boolean stopped = false;
	private LogManager lm = null;
	private AbstractCommandFactory comm = null;
	private int tcpPeer;

	/**
	 * The constructor initialize the attributes.
	 * 
	 * @param comm factory for the command
	 * @param updServer udp server port
	 * @param tcpPeer tcp peer port
	 * @param checkPeriod check period
	 * @param peerTimeout timeout
	 * @param debug debug
	 * @param lm logmanager
	 */
	public UDPServer(AbstractCommandFactory comm, int updServer, int tcpPeer, int checkPeriod, int peerTimeout, boolean debug, LogManager lm) {
		this.updServer = updServer;
		this.checkPeriod = checkPeriod;
		this.peerTimeout = peerTimeout;
		this.debug = debug;
		this.lm = lm;
		this.comm = comm;

		this.pool = new ThreadPoolingUDP(comm, this.peerTimeout, this.checkPeriod, this.debug, lm);
	}

	/**
	 * This method creates an instance of the DatagramSocket.
	 * 
	 * @return if the Socket could be created
	 * @throws SocketException
	 */
	public boolean initializeSocket()  {
		//System.out.println("udpworker on port" + this.updServer);
		try {
			// create the datagramsocket
			this.udpSocket = new DatagramSocket(this.updServer);
		} catch(java.net.BindException e)
		{
			// Error-Message output, and will write an Message into the LogFile/return false
			System.out.println("The given udp port is already in use!");
			return false;
		} catch (SocketException e) {
			// Error-Message output
			System.out
					.println("Coundn't create DatagramSocket (SocketException)."
							+ e.getMessage());
			this.lm.write("Coundn't create DatagramSocket (SocketException)."
					+ e.getMessage());

			if (this.debug) {
				e.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			// Error-Message output
			System.out
					.println("Coundn't create DatagramSocket (Unknown Exception)."
							+ e.getMessage());
			this.lm.write("Coundn't create DatagramSocket (Unknown Exception)."
					+ e.getMessage());

			if (this.debug) {
				e.printStackTrace();
			}

			return false;
		}

		// Checks if the UDP-Socket(DatagramSocket) is null, and return true if it isnt
		if (this.udpSocket != null) {
			//System.out.println("UDP Server Socket was successfully created!");
			this.lm.write("UDP Server Socket was successfully created!");
			return true;
		}
		
		return false;
	}
	
	@Override
	public void run() {
		lm.write("UDP SERVER UP PORT(" + this.updServer + ")");
		//System.out.println("UDP SERVER UP PORT(" + this.updServer + ")");

		// The loop for the Thread which is controlled by the stopped variable
		while (!this.stopped) {
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			try {
				udpSocket.receive(packet);
				//System.out.println("Packet: " + packet.getAddress().getHostAddress() + " " + (Integer.parseInt(new String(packet.getData(), 0, packet.getLength()))));
				
			} catch(java.net.SocketException e)
			{
				// Error-Message will be added to the LogFile
				this.lm.write("Datagram Socket closed");
				this.pool.stopTimer();
				// if isShutdown true = threadpooling is already shutting down
				// so... dont call shutdownNow -> will result in an nullpointer exception
				if (!this.pool.getTpool().isShutdown()) {
					this.pool.getTpool().shutdownNow();
				}

				this.stopped = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Checks if the received Package is null
			// if it isnt it will be forwarded to the UDPThreadPool
			if(packet.getAddress()!=null)
			{
				this.pool.addDatagramSocket(packet);
			}
			// Checks if the received Package is null
			// if it isnt it will set the IsAlive Flag true as a sign of an received isAlive-Package
			if(packet!=null  && packet.getAddress() != null)
				this.pool.setIsAlive(new PeerIdentification(packet.getAddress().getHostAddress(),(Integer.parseInt(new String(packet.getData(), 0, packet.getLength())))));
		}

		//System.out.println("UDP Server stopped!");
		this.lm.write("UDP Server stopped!");
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped
	 *            the stopped to set
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * @return the udpSocket
	 */
	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}
}
