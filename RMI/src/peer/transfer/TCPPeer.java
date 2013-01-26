package peer.transfer;

import java.io.*;
import java.net.*;
import factory.*;
import LogManager.*;
import threadhandling.*;

/**
 * This class is the a listener for any file transfer actions.
 * If any file transfer is requested, then this class handels
 * the intecommunication beetween the two peers.
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-22
 */
public class TCPPeer implements Runnable {
	
	// define the private attributes
	private ServerSocket ss = null;
	private ThreadPoolingFileTrans pool;
	private LogManager lm = null;
	private boolean stopped = false;
	private boolean debug;
	private int tcpPort;

	/**
	 * Overwrite the default constuctor and set the value from the paramter attributes 
	 * to the global defined variables
	 * 
	 * @param comm					set the abstractcommandfactory for each concrete command
	 * @param sharedFolder			path to the shared folder
	 * @param tcpPort				set the tcp port from the peer client
	 * @param debug					if true, display debug informations
	 * @param lm					log manager for writeing all the actions to a log file
	 */
	public TCPPeer(AbstractCommandFactory comm, String sharedFolder, int tcpPort, boolean debug, LogManager lm) {
		this.tcpPort = tcpPort;
		this.debug = debug;
		this.lm = lm;
		this.pool = new ThreadPoolingFileTrans(sharedFolder, this.debug, lm);
	}

	/**
	 * This method creates an instance of the ServerSocket.
	 * 
	 * @return true if successful, else false
	 */
	public boolean initializeSocket() {
		try {
			// create new serversocket with the given tcp port of the peer client
			ss = new ServerSocket(this.tcpPort);
		}catch (BindException e)
		{
			System.out.println("Coudn't start TCP file server");
			return false;
		// if any error occurs, then catch it and print a error message
		} catch (SocketException e) {
			// print the error message to the console and write it to the log file
			System.out.println("Coundn't create ServerSocket (SocketException). "+ e.getMessage());
			this.lm.write("Coundn't create ServerSocket (SocketException)."+ e.getMessage());
			// if the debug options is on
			// return false cause of an error
			return false;
		// if any error occurs, then catch it and print a error message
		} catch (Exception e) {
			// print the error message to the console and write it to the log file
			System.out.println("Coundn't create ServerSocket (Unknown Exception)."+ e.getMessage());
			this.lm.write("Coundn't create ServerSocket (Unknown Exception)." + e.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
			// return false cause of an error
			return false;
		}
		// if the socket initialized sucessfully...
		if (ss != null) {
			// print the staus message to the console and write it to the log file
			//System.out.println("TCP Server Socket was successfully created!");
			this.lm.write("TCP Server Socket was successfully created!");
			// return true if the initialization was succesfully
			return true;
		}
		// return false cause of an error
		return false;
	}

	/**
	 * This method creates a socket, which is listening for any
	 */
	public void run() {		
		this.lm.write("TCP SERVER UP PORT(" + this.tcpPort + ")");
		// if the initialization wasnt successfully
		if (ss == null) {
			// break the method flow by returnin a return statement
			return;
		}
		// define a socket
		Socket socket = null;
		// if the peer isn't closed..
		while (!this.stopped) {
			try {
				// wait for incomming connections
				socket = ss.accept();
				// System.out.println("incoming connection");
			}
			// if any error occurs, then catch it and print a error message
			catch(java.net.SocketException e) {
				System.out.println("Socket closed");
				// if any error occurs, then catch it and print a error message
			} catch (IOException e) {
				System.out.println("Error will accepting socket!");
				this.lm.write("Error will accepting socket!" + e.getMessage());
				// if the debug options is on
				if(debug) {
					// then print a detailed error message to the console
					e.printStackTrace();
				}
			}
			// Add socket to thread pooling if the socket is not null
			if (socket != null) {
				// add this socket to the thread pool
				pool.addServerWorker(socket);
			}
		}
		// if the peer is disconnected, then print specific informations
		//System.out.println("TCP Server stopped!");
		// and write the status to the log file 
		this.lm.write("TCP Server stopped!");
	}

	/**
	 * @return the ss
	 */
	public ServerSocket getSs() {
		return ss;
	}
	
	/**
	 * @param ss	 the ss to set
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
}
