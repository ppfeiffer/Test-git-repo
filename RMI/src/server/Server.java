package server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import factory.AbstractCommandFactory;
import factory.ServerCommandFactory;

import LogManager.LogManager;

/**
 * This class will handle the tcp and udp worker threads.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 05-Jan-2013
 */
public class Server implements Runnable {
	// the tcp port of the server
	private int tcpPort;
	// the udp listening port of the server
	private int udpPort;
	// The period in ms each peer has to send an “isAlive” packet
	private int peerTimeout;
	// It specifies the time in ms between two peer checks
	private int checkPeriod;
	// will store the tcp server instance
	private TCPServer tcpServer = null;
	// will store the udp server instance
	private UDPServer udpServer = null;
	// show debug infos yes/no
	private boolean debug = false;
	// instance of the log mangager
	private LogManager lm = null;
	// stores the state if the tcp server was stopped or not
	private boolean tcpStopped;
	// stores the state if the udp server was stopped or not
	private boolean udpStopped;

	private Thread tcpThread = null;
	private Thread udpThread = null;
	
	private AbstractCommandFactory comm = null;

	/**
	 * Constructor of the class. It defines the local variables and creates the
	 * connection pool handler.
	 * 
	 * @param tcpPort
	 *            the tcp port on which the tcp server should start
	 * @param udpPort
	 *            the upd port on which the udp server should start
	 * @param checkPeriod
	 *            the period of time the server should check if a udp connection
	 *            timed out(in ms)
	 * @param peerTimeout
	 *            the time after a udp connection should be timed out if no
	 *            isalive was received(in ms)
	 */
	public Server(int tcpPort, int udpPort, int checkPeriod, int peerTimeout,
			boolean debug, LogManager lm) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.peerTimeout = peerTimeout;
		this.checkPeriod = checkPeriod;
		this.debug = debug;

		this.lm = lm;
	}

	/**
	 * This method will instalize the tcp and udp server instances.
	 * 
	 * @return true if successful, else false
	 */
	public boolean initializeServer() {
		this.comm = new ServerCommandFactory(lm);
		// create the command factory object
		comm.createRemoveFiles();
		comm.createRegisterPeer();
		comm.createAddFiles();
		comm.createFindFile();
		comm.createUnRegisterPeer();
		// create tcp server instance
		this.tcpServer = new TCPServer(comm, this.tcpPort, this.udpPort, this.peerTimeout, this.debug, lm);
		// create udp server instance
		this.udpServer = new UDPServer(comm, this.udpPort, this.udpPort, this.checkPeriod,
				this.peerTimeout, this.debug, lm);

		tcpThread = new Thread(tcpServer);
		udpThread = new Thread(udpServer);

		// Testing if any of the Server/Thread is null
		if (this.tcpServer == null || this.udpServer == null
				|| tcpThread == null || udpThread == null) {
			System.out.println("Coundn't create tcp server or udp server");

			if (this.debug) {
				this.lm.write("Coundn't create tcp server or udp server");
			}

			return false;
		} else {
			try {
				// initializing the tcp server and the the udp server
				if( !this.tcpServer.initializeSocket() ||  !this.udpServer.initializeSocket() ) 
					return false;
				
				// catch exceptions
			} catch (Exception e) {
				System.out
						.println("Coundn't create tcp server or udp server(Unknown Exception)."
								+ e.getMessage());
				this.lm.write("Coundn't create tcp server or udp server(Unknown Exception)."
						+ e.getMessage());

				if (this.debug) {
					e.printStackTrace();
				}

				return false;
			}

			// start the both servers
			tcpThread.start();
			udpThread.start();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				System.out
						.println("Error while trying to sleep after upd/tcp server start!");
				lm.write("Error while trying to sleep after upd/tcp server start!");
				// if debuging is enabled, print the debug information
				if (debug) {
					lm.write(e.getMessage());
					e.printStackTrace();
				}
			}

			if (this.debug) {
				System.out.println("Created tcp and udp server");
				this.lm.write("Created tcp and udp server");
			}
			// if both server were started successfully
			if (this.tcpThread.isAlive() && this.udpThread.isAlive()) {
				this.lm.write("User output: Server up. Hit enter to exit.");
			} else {
				// Coudn't start both servers correctly
				System.out.println("Coudn't start both servers correctly!");
				this.lm.write("Coudn't start both servers correctly!");

				return false;
			}
			return true;
		}
	}

	/**
	 * Run method, check if the tcp or udp server stopped. If that was the case,
	 * the server stopps and the program will exit
	 */
	public void run() {

		System.out.println("Server up. Hit enter to exit.");
		// open new scanner to read from console
		Scanner read = new Scanner(System.in);
		// read a line from the console
		read.nextLine();
		System.out.println("Stopping....");
		try {
			// check if socket server isn't null, if so, close it
			if( this.tcpServer.getSs() != null )
				this.tcpServer.getSs().close();
			
			// check if datagram server isn't null, if so, close it
			if(this.udpServer.getUdpSocket() != null )
				this.udpServer.getUdpSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method sets if the tcp server was stopped or wasn't
	 * 
	 * @param tcpStopped
	 *            true if stopped, else false
	 */
	public void setTCPStopped(boolean tcpStopped) {
		this.tcpStopped = tcpStopped;
	}

	/**
	 * This method sets if the udp server was stopped or wasn't
	 * 
	 * @param udpStopped
	 *            true if stopped, else false
	 */
	public void setUDPStopped(boolean udpStopped) {
		this.udpStopped = udpStopped;
	}

	/**
	 * @return the tcpPort
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * @param tcpPort
	 *            the tcpPort to set
	 */
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	/**
	 * @return the udpPort
	 */
	public int getUdpPort() {
		return udpPort;
	}

	/**
	 * @param udpPort
	 *            the udpPort to set
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	/**
	 * @return the peerTimeout
	 */
	public int getPeerTimeout() {
		return peerTimeout;
	}

	/**
	 * @param peerTimeout
	 *            the peerTimeout to set
	 */
	public void setPeerTimeout(int peerTimeout) {
		this.peerTimeout = peerTimeout;
	}

	/**
	 * @return the checkPeriod
	 */
	public int getCheckPeriod() {
		return checkPeriod;
	}

	/**
	 * @param checkPeriod
	 *            the checkPeriod to set
	 */
	public void setCheckPeriod(int checkPeriod) {
		this.checkPeriod = checkPeriod;
	}
}
