package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import factory.AbstractCommandFactory;

import LogManager.LogManager;
import threadhandling.ThreadPooling;
import threadhandling.ThreadPoolingTCP;

/**
 * This class initialize the TCP Socket, also the socket can be stopped. The
 * Threadpool for tcp gets created.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 08-Jan-2013
 */
public class TCPServer implements Runnable {
	private ServerSocket ss = null;
	private boolean stopped = false;
	private ThreadPoolingTCP pool;
	private boolean debug;
	private LogManager lm = null;

	private int tcpPort;
	private int udpPort;
	private int peerTimeout;

	/**
	 * The constructor initialize the attributes.
	 * 
	 * @param comm
	 *            the factory for the command
	 * @param tcpPort
	 *            tcp port
	 * @param udpPort
	 *            udp port
	 * @param peerTimeout
	 *            timout
	 * @param debug
	 *            debug
	 * @param lm
	 *            logmanager
	 */
	public TCPServer(AbstractCommandFactory comm, int tcpPort, int udpPort,
			int peerTimeout, boolean debug, LogManager lm) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.peerTimeout = peerTimeout;
		this.debug = debug;
		this.lm = lm;

		// create a new threadpool
		this.pool = new ThreadPoolingTCP(comm, this.tcpPort, this.udpPort,
				this.peerTimeout, this.debug, lm);

	}

	/**
	 * This method creates an instance of the ServerSocket.
	 * 
	 * @return true if successful, else false
	 */
	public boolean initializeSocket() {
		try {
			// create new serversocket
			ss = new ServerSocket(this.tcpPort);
			
		}catch(java.lang.IllegalArgumentException e)
		{
			System.err.println("Invalid port given!");
			return false;
		} catch(java.net.BindException e)
		{
			System.err.println("The given tcp port is already in use!");
			return false;
		} catch (SocketException e) {
			//Error-Message output
			System.err
					.println("Coundn't create ServerSocket (SocketException)."
							+ e.getMessage());
			this.lm.write("Coundn't create ServerSocket (SocketException)."
					+ e.getMessage());

			if (this.debug) {
				e.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			// Error-Message output
			System.out
					.println("Coundn't create ServerSocket (Unknown Exception)."
							+ e.getMessage());
			this.lm.write("Coundn't create ServerSocket (Unknown Exception)."
					+ e.getMessage());

			if (this.debug) {
				e.printStackTrace();
			}

			return false;
		}

		// Checks if the ServerSocket is null, if so it will return false.
		// If the Server is initialized correctly the server will write that in the LogFile and return true
		if (ss != null) {
			//System.out.println("TCP Server Socket was successfully created!");
			this.lm.write("TCP Server Socket was successfully created!");
			return true;
		}

		return false;
	}

	@Override
	public void run() {
		lm.write(("TCP SERVER UP PORT(" + this.tcpPort + ")"
				+ " PEERTIMEOUT: " + this.peerTimeout));
		
		// Checks if the ServerSocket is null, if so it will quit the run method
		if (ss == null)
			return;
		
		Socket socket = null;
		
		// The loop for the Thread which is controlled by the stopped variable
		while (!this.stopped) {
			try {
				// Listens for incoming connections
				socket = ss.accept();
			} catch (java.net.SocketException e) {
				this.lm.write("Socket Server closed");
				
				// if isShutdown true = threadpooling is already shutting down
				// so... dont call shutdownNow -> will result in an nullpointer exception
				if (!this.pool.getTpool().isShutdown()) {
					this.pool.getTpool().shutdownNow();
				}
				break;
			} catch (IOException e) {
				// Error-Message output
				System.out.println("Error will accepting socket!");
				this.lm.write("Error will accepting socket!" + e.getMessage());

				if (debug) {
					e.printStackTrace();
				}
			}
			// Add socket to thread pooling
			if (socket != null) {
				pool.addServerWorker(socket);
			}
		}

		System.out.println("TCP Server stopped!");
		this.lm.write("TCP Server stopped!");
	}

	/**
	 * @return the ss
	 */
	public ServerSocket getSs() {
		return ss;
	}

	/**
	 * @param ss
	 *            the ss to set
	 */
	public void setSs(ServerSocket ss) {
		this.ss = ss;
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
	 * @return the pool
	 */
	public ThreadPooling getPool() {
		return pool;
	}

	/**
	 * @param pool
	 *            the pool to set
	 */
	public void setPool(ThreadPoolingTCP pool) {
		this.pool = pool;
	}

	public void stopped(boolean stopped) {
		this.stopped = stopped;
	}

}
