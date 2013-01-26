package peer.transfer;

import java.io.*;
import java.net.*;
import factory.*;
import LogManager.*;

/**
 * This class will send an isAlive udp package to the server
 * in order to let the server know that the peer is still alive.
 * 
 * @author Paul Pfeiffer-Vogl (ppfeifer@student.tgm.ac.at)
 * @version 20-Jan-2013
 */
public class IsAlive implements Runnable {

	// define the private Attributes
	private int peerTimeout;
	private DatagramSocket datagramSocket = null;
	private boolean debug;
	private boolean stopped = false;
	private LogManager lm = null;
	private String serverHost;
	private int peerTcp;
	private int udpServerPort;
	private int udpPort;
	
	/**
	 * Overwrite the default constructor and set the paramter values to the global
	 * defined variables.
	 * 
	 * @param comm					set the abstractcommandfactory for each concrete command	
	 * @param serverHost			set the ip address of the host (server)
	 * @param tcpServerPort			set the tcp connection port to the host (server)
	 * @param udpServerPort			set the udp connection port to the host (server)
	 * @param peerTcp				set the peerTCP port from the peer
	 * @param peerTimeout			set the timeout, after a peers goes offline
	 * @param debug					if true, display debug informations
	 * @param lm					log manager for writeing all the actions to a log file
	 */
	public IsAlive(AbstractCommandFactory comm, String serverHost, int tcpServerPort, int udpServerPort, int peerTcp, int udpPort, int peerTimeout, boolean debug, LogManager lm) {
		this.serverHost = serverHost;
		this.peerTimeout = peerTimeout;
		this.debug = debug;
		this.lm = lm;
		this.peerTcp = peerTcp;
		this.udpServerPort = udpServerPort;
		this.udpPort = udpPort;
	}

	/**
	 * This method intializes the udp connection form the peer to the server.
	 * The peer have to send every chekPeriod a udp packet to the server
	 * and says that he is still online. 
	 * 
	 * @return		true, if the creation of a udp communication was successfully
	 */
	public boolean initializeSocket() {
		try {
			// create a new datagramsocket
			this.datagramSocket = new DatagramSocket();
		// if any error occurs, then catch it and print a error message
		} catch (SocketException e) {
			System.out.println("Coundn't create DatagramSocket (SocketException)." + e.getMessage());
			this.lm.write("Coundn't create DatagramSocket (SocketException)." + e.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
			// break the method flow by returning an empty return type
			return false;
		// if any error occurs, then catch it and print a error message
		} catch (Exception e) {
			System.out.println("Coundn't create DatagramSocket (Unknown Exception)." + e.getMessage());
			this.lm.write("Coundn't create DatagramSocket (Unknown Exception)." + e.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
			// break the method flow by returning an empty return type
			return false;
		}
		// if the initialization of the udp connection was successfully
		if (this.datagramSocket != null) {
			System.out.println("UDP Server Socket was successfully created!");
			this.lm.write("UDP Server Socket was successfully created!");
			// break the method flow by returning an empty return type
			return true;
		}
		// set the timeout
		try {
			this.datagramSocket.setSoTimeout(this.peerTimeout);
		// if any error occurs, then catch it and print a error message
		} catch (SocketException e) {
			System.out.println("Coundn't create DatagramSocket (SocketException)." + e.getMessage());
			this.lm.write("Coundn't create DatagramSocket (SocketException)." + e.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
			// break the method flow by returning false
			return false;
		}
		// finally return a return value
		return false;
	}

	/**
	 * This method overwrites the run-method and implements the sending
	 * of the udp packets to the server as "isAlive" messages. 
	 */
	@Override
	public void run() {
		DatagramSocket datagramSocket = null;
		try {
			// create a new datagrammsocket with the given udpPort
			datagramSocket = new DatagramSocket(this.udpPort);
		// if any error occurs, then catch it and print a error message
		} catch (SocketException e1) {
			lm.write("SocketException: " + e1.getMessage());
			System.err.println("SocketException: " + e1.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e1.printStackTrace();
			}
		}
		// initialize the buffer size 
		byte[] buffer = ("" + this.peerTcp).getBytes();
		// define a inteaddress object to get the needed network properties 
		InetAddress receiverAddress = null;
		try {
			// try to get the ip address of the server
			receiverAddress = InetAddress.getByName(this.serverHost);
		// if any error occurs, then catch it and print a error message
		} catch (UnknownHostException e1) {
			lm.write("UnknowHostException: " + e1.getMessage());
			System.err.println("UnknowHostException: " + e1.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e1.printStackTrace();
			}
		}
		
		// write the current status to the log file
		//System.out.println("sending udp packages to " +  this.udpServerPort);
		lm.write("sending udp packages to " +  this.udpServerPort);
		// create a new datagramm packet with the give parameters
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,receiverAddress, this.udpServerPort);
		
		// send isAlive (udp) packages as the client is online
		while (!this.stopped) {
			// write the current status to the log file
			lm.write("Sending udp package");
			//System.out.println("sending package");
			try {
				// wait as the checkPerid defined
				Thread.sleep(this.peerTimeout);
			// if any error occurs, then catch it and print a error message
			} catch (InterruptedException e1) {
				lm.write("InterruptedException: " + e1.getMessage());
				System.err.println("InterruptedException: " + e1.getMessage());
				// if the debug options is on
				if (this.debug) {
					// then print a detailed error message to the console
					e1.printStackTrace();
				}
			}
			try {
				// send the packet to the datagrammsocket
				datagramSocket.send(packet);
			// if any error occurs, then catch it and print a error message
			} catch( java.net.SocketException e ) {
				System.out.println("closed is alive socket!");
			// if any error occurs, then catch it and print a error message
			} catch (IOException e) {
				lm.write("IOException: " + e.getMessage());
				System.err.println("IOException: " + e.getMessage());
				// if the debug options is on
				if (this.debug) {
					// then print a detailed error message to the console
					e.printStackTrace();
				}
			}
		}
		// write the current status to the log file and print the information
		// System.out.println("UDP isAlive stopped!");
		this.lm.write("UDP isAlive stopped!");
	}
	
	/**
	 * This method closes the daragramm socket
	 */
	public void close() {
		this.datagramSocket.close();
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped		the stopped to set
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
