package threadhandling;

import java.io.IOException;
import java.net.Socket;

import peer.transfer.PeerSocketWorker;
import LogManager.LogManager;
/**
 * This is one of two specific ThreadPooling Classes. It is specified on TCP ThreadPooling.
 * It adds TSPServerWorkers to the Pool.
 * 
 * @author Ribarich Michael
 * @version 2013-01-17
 */
public class ThreadPoolingFileTrans extends ThreadPooling {
	private PeerSocketWorker ssw = null;
	private String sharedFolder = "";

	/**
	 * The specific Constructor for an TCP-ThreadPool
	 * 
	 * @param sharedFolder The path to the sharedFolder
	 * @param debug Debug-state
	 * @param lm Reference to the LogManager Object
	 */
	public ThreadPoolingFileTrans(String sharedFolder, boolean debug, LogManager lm) {
		super(null, debug, lm);
		this.sharedFolder = sharedFolder;
	}

	/**
	 * This Method adds new Assignments to the Pool, which creates a new Thread, if necessary.
	 * If theres an running idle Thread it will prefer using this one instead of creating a new one.
	 * 
	 * @param socket Reference to the Socket Object
	 */
	public void addServerWorker(Socket socket) {
		// New Worker gets created
		ssw = new PeerSocketWorker(socket, this.sharedFolder, super.isDebug(), lm);
		// This line gives the Runnable to the ThreadPool(ExecutorService)
		super.getTpool().execute(ssw);
	}

	/**
	 * Getter-Method for the ServerSocketWorker
	 * 
	 * @return the ssw
	 */
	public PeerSocketWorker getSsw() {
		return ssw;
	}
	
	/**
	 * This Class will close the delivered Socket(Socket) and shuts the ThreadPool(ExecuterService) down.
	 * In case of an occurring Exception it will put a ErrorMessage in the ServerLog.
	 * 
	 * @param socket Reference to the Socket Object
	 */
	public void closeSocket(Socket socket)
	{
		try {
			// Closes the Socket
			socket.close();
			// The ThreadPool(ServiceExecutor) will be shutdown

			// if isShutdown true = threadpooling is already shutting down
			// so... dont call shutdownNow -> will result in an nullpointer exception
			if (!super.getTpool().isShutdown()) {
				super.getTpool().shutdownNow();
			}
		} catch (IOException e) {
			this.lm.write("Fehler beim Schlieﬂen des Sockets(IO)");
		}
	}
}