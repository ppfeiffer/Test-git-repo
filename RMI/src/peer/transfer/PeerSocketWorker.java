package peer.transfer;

import java.net.*;
import java.io.*;
import LogManager.*;
import peer.commands.*;
import peer.commands.operations.*;
import peer.filetransfer.*;

/**
 * This class handels the concrete file transfer logic. This class
 * is the woker thread that will handel any file download request.
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-22
 */

public class PeerSocketWorker implements Runnable {

	// define the private attributes
	private Socket socket;
	private boolean debug = false;
	private String sharedFolder = "";
	private LogManager lm;

	/**
	 * Overwrite the default constuctor and set the value from the paramter attributes 
	 * to the global defined variables
	 * 
	 * @param socket			set the socket to get the required informations(objects)
	 * @param sharedFolder		path to the shared folder
	 * @param debug				if true, display debug informations
	 * @param lm				log manager for writeing all the actions to a log file
	 */
	public PeerSocketWorker(Socket socket, String sharedFolder, boolean debug, LogManager lm) {
		this.sharedFolder = sharedFolder;
		this.socket = socket;
		this.debug = debug;
		this.lm = lm;
		this.debug = debug;
	}

	/**
	 * In the run-method we create an objectinputstream and read the object from
	 * the socket. The readed object should be a type of the class DownloadCommandImpl.
	 * if the type is correct, then we determine the concrete file porpeties of the 
	 * received object 
	 */
	public void run() {
		// define an object of the abstract class CommandInterface
		CommandInterface received;
		// Server Commands
		try {
			// define the streams from the socket
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			try {
				// initialize the object with the received object from the socket
				received = (CommandInterface) in.readObject();
				
				// now we have to check the type of the received object..
				// if the received object is a type of the class DownloadCommandImpl, then continue
				if (received instanceof DownloadCommandImpl) {
					// create a new DownloadCommandImpl object from the received socket stream
					DownloadCommandImpl fileDownload = (DownloadCommandImpl) received;
					// now get the specific file informations, that are requestet to download
					fileDownload.getFileName();
					// check if the file properties are null and so an error occures
					if (fileDownload.getFileName() == null) {
						// write the error message to the log file
						lm.write("PeerSocketworker: Socket was null and will be closed.");
						// close the socket
						socket.close();
						// break the method flow by returning an empty return type
						return;
					}
		
					// create a new TransmissionServer for the file sharing
					TransmissionServer fileTranser = new TransmissionServer(sharedFolder, fileDownload.getFileName(), socket);
					// start concrete the file sharing over the network
					fileTranser.run();
				// if the received object is not a type of the class DownloadCommandImpl...
				} else {
					// print error messages and write the error into the log file
					System.out.println("Cannot cast " + received.getClass());
					lm.write("Cannot cast " + received.getClass());
				}
				// finally, after a successfull transaction, try to close the socket
				try {
					// closing the socket
					this.socket.close();
					// writing the status into the log file
					lm.write("Closing the Socket save");
				// if any error occurs, then catch it and print a error message
				} catch (IOException e) {
					System.out.println("Connection termination exception!");
					lm.write("Connection termination exception!");
					// if the debug options is on
					if (this.debug) {
						// then print a detailed error message to the console
						e.printStackTrace();
					}
				}
				System.out.println("Connection terminated by user!");
			// if any error occurs, then catch it and print a error message
			} catch (ClassNotFoundException e) {
				lm.write("Error: " + e.getMessage());
				// if the debug options is on
				if (this.debug) {
					// then print a detailed error message to the console
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			lm.write("Error: " + e.getMessage());
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
		}
		// finally print a message to inform the client about the current status
		System.out.println("Worker thread finished his work!");
	}
}
