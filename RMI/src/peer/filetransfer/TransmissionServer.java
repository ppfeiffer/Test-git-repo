package peer.filetransfer;

import java.io.*;
import java.net.*;
import peer.directory.*;

/**
 * This class is responsable for the sending a file from a peer server
 * to a peer client. The class has got a run-method which writes the
 * requested file from the client peer to the given socket. 
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-22
 */

public class TransmissionServer {

	// define private attributes
	private String requestedFile;
	private Socket socket;
	private FileManager fm;
	private String file;
	public static final int BUFFER_SIZE = 1024 * 50;
	private byte[] buffer;

	/**
	 * Overwrite the default constructor and set the paramter values to the global
	 * defined variables.
	 * 
	 * @param sharedFolder		path to the shared folder on the peer server side
	 * @param requestedFile		name of the requested file
	 * @param socket			the socket connection for the file transfer
	 */
	public TransmissionServer(String sharedFolder, String requestedFile, Socket socket) {
		System.out.println("Server Filetransfer: Setting variables in the constructor");
		this.buffer = new byte[BUFFER_SIZE];
		this.socket = socket;
		this.requestedFile = requestedFile;
		// create a new FileManager with the given shared folder
		System.out.println("SHHHARRRRRRRRRRRRRED FOLDER " + sharedFolder);
		fm = new FileManager(sharedFolder);
	}

	/**
	 * This method scans with the filemanager the shared folder on the peer server
	 * side and searches excpicit for the requested file from the peer client.
	 */
	public void getFileReady() {
		// scan the shared folder and update the file registery 
		fm.scanSharedFolder();
		fm.printAllFiles();
		// set the value of the requested file as global
		setFile(this.requestedFile);
	}

	/**
	 * This method searches the requested file in the file registery
	 * and saves the path value from that file to the global variable
	 * 
	 * @param file		name of the file, that should be searched
	 */
	public void setFile(String file) {
		this.file = fm.findFile(file);
	}

	/**
	 * In this method the peer server sends the requested file over the given socket.
	 * The file is written in the outputstream of the socket in pieces.
	 */
	public void run() {
		
		// get ready for the requested file
		getFileReady();
		// if the requested file doesnt exists on the peer server
		if(this.file == null ) {
			// 
			System.out.println("Server Filetransfer: Requested file was not found!");
			// end the method flow by a return statement
			return;
		}
		try {
			try {
				//System.out.println("Server Filetransfer: Initialize the connetion to the peer client");
				// create a new file object with the requested file
				File fileShare = new File(this.file);
				// define the streams from the socket
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileShare));
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
				// define helper variables for a well output
				int chars = 1;
				int len = 0;
				// now write the readed pieces from the socket into the file with the
				// bufferefoutputstream
				while ((len = in.read(buffer)) > 0) {
					// write the pieces to the stream
					out.write(buffer, 0, len);
					// if the value of the helper variable is 10
					// then create a line break -> for a better view in the console
					if (chars %10 == 0) {
						System.out.println();
						// set the value of the helper variable to default
						chars = 1;
					}
					// print a char as progressbar
					System.out.print("#");
					// increment the helper variable
					chars++;
				}
				System.out.println("\nFiletransfer succesfully. Now closing all the streams.");
				// close the streams and the socket itself
				in.close();
				out.flush();
				out.close();
				socket.close();
				// finally print a information message for the done work
				System.out.println("\nDone!");
			// if any error occurs, then catch it and print a error message
			} catch (IOException e) {
				System.err.println("Transferfile Server: Error while accepting socke or creating the requested filet!");
			}
		// if any error occurs, then catch it and print a error message
		} catch (Exception e) {
			System.err.println("Transferfile Server: An unknown error appeared.!");
		}
	}

	/**
	 * @return the requestedFile
	 */
	public String getRequestedFile() {
		return requestedFile;
	}

	/**
	 * @param requestedFile		the requestedFile to set
	 */
	public void setRequestedFile(String requestedFile) {
		this.requestedFile = requestedFile;
	}
}