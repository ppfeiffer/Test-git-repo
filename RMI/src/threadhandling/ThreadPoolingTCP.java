package threadhandling;

import java.net.Socket;
import factory.AbstractCommandFactory;
import LogManager.LogManager;
import server.conmanager.ServerSocketWorker;

/**
 * This is one of two specific ThreadPooling Classes. It is specified on TCP ThreadPooling.
 * It adds TSPServerWorkers to the Pool.
 * 
 * @author Ribarich Michael
 * @version 2013-01-17
 */
public class ThreadPoolingTCP extends ThreadPooling {
	ServerSocketWorker ssw = null;
	private int udpPort;
	private int peerTimeout;

	/**
	 * The specific Constructor for an TCP-ThreadPool
	 * 
	 * @param comm Reference to the AbstractCommandFactory Object
	 * @param tcpPort Reference to the TCP-Port Object
	 * @param udpPort Reference to the UDP-Port Object
	 * @param peerTimeout The range in which a Peer has time to send an isAlive package
 	 * @param debug Debug-state
	 * @param lm Reference to the LogManager Object
	 */
	public ThreadPoolingTCP(AbstractCommandFactory comm, int tcpPort, int udpPort, int peerTimeout, boolean debug, LogManager lm) {
		super(comm, debug, lm);
		this.udpPort = udpPort;
		this.peerTimeout = peerTimeout;
	}

	/**
	 * This Method adds new Assignments to the Pool, which creates a new Thread, if necessary.
	 * If theres an running idle Thread it will prefer using this one instead of creating a new one.
	 * 
	 * @param socket Reference to the Socket Object
	 */
	public void addServerWorker(Socket socket) {
		ssw = new ServerSocketWorker(super.getComm(), socket, super.isDebug(), this.udpPort, peerTimeout);
		super.getTpool().execute(ssw);
	}

	/**
	 * Getter-Method for the ServerSocketWorker
	 * 
	 * @return the ssw
	 */
	public ServerSocketWorker getSsw() {
		return ssw;
	}
}
