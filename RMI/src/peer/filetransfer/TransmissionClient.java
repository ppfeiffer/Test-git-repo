package peer.filetransfer;

import java.io.*;
import java.net.*;
import peer.directory.*;

/**
 * This class is responsable for the receiving a file from a another client.
 * The class implements from the interface Runnable and so it is possible to
 * create a Thread of this class. This is needed for the data listening from
 * the other peer, who sends the requested file. 
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-22
 */

public class TransmissionClient implements Runnable {

	// define the private attributes
	private Socket socket;
	private String sharedFolder;
	private String fileName;
	private FileManager fm;
	public static final int BUFFER_SIZE = 1024 * 50;
	private byte[] buffer;

	/**
	 * Overwrite the default constructor and set the paramter values to the global
	 * defined variables.
	 * 
	 * @param sharedFolder		path to the shared folder on the client side
	 * @param fileName			name of the requested file
	 * @param socket			the socket connection for the file transfer
	 */
	public TransmissionClient(String sharedFolder, String fileName, Socket socket) {
		System.out.println("Client Filetransfer: Setting variables in the constructor");
		this.buffer = new byte[BUFFER_SIZE];
		this.sharedFolder = sharedFolder;
		this.fileName = fileName;
		this.socket = socket;
		// create a new FileManager with the given shared folder
		fm = new FileManager(sharedFolder);
	}
	
	/**
	 * We overwrite the run-method. All the needed streams for the file transmission will be
	 * defined in this method and the received file will be written in the shared folder
	 * with the given file name. The file will be readed in blocks, that means that the
	 * requested file will be send in seperate pieces.
	 */
	@Override
	public void run() {
		try {
			// create a new file object with the given shared folder and file name
			File recieveFile = new File(this.sharedFolder + File.separator + this.fileName);
			// define the streams from the socket
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(recieveFile));
			// define helper variables for a well output
			int len = 0;
			int chars = 1;
			System.out.println("Started to receive the requested file:");
			// now write the readed pieces from the socket into the file with the
			// bufferefoutputstream
			while ((len = in.read(buffer)) > 0) {
				// write the pieces to the stream
				out.write(buffer, 0, len);
				// if the value of the helper variable is 10
				// then create a line break -> for a better view in the console
				if (chars %100 == 0) {
					System.out.println();
					// set the value of the helper variable to default
					chars = 1;
				}
				// print a char as progressbar
				System.out.print("#");
				// increment the helper variable
				chars++;
			}
			// System.out.println("\nFiletransfer succesfully. Now closing all the streams.");
			// close the streams and the socket itself
			in.close();
			out.flush();
			out.close();
			socket.close();
			// finally print a information message for the done work
			System.out.println("\nDone!");
		// if any error occurs, then catch it and print a error message
		} catch (IOException e) {
			System.err.println("Transferfile Client: Error while accepting socke or creating the requested filet!");
		}
	}

	/**
	 * @return the fm
	 */
	public FileManager getFm() {
		return fm;
	}

	/**
	 * @param fm	 the fm to set
	 */
	public void setFm(FileManager fm) {
		this.fm = fm;
	}
}
