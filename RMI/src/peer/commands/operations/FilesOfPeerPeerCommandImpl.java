package peer.commands.operations;

import peer.commands.CommandInterface;
import peer.commands.FilesOfPeerInterface;
/**
 * This class is the implementation to list all files of a peer.
 * 
 * @author Schneider Kevin (kschneider@student.tgm.ac.at)
 * @version 2013.01.04
 */
public class FilesOfPeerPeerCommandImpl extends CommandInterface implements FilesOfPeerInterface {

	private static final long serialVersionUID = 1L;
	
	private String host;
	private int port;

	/**
	 * The constructor initialize the attributes.
	 * @param host host ip
	 * @param port host port
	 */
	public FilesOfPeerPeerCommandImpl(String host, int port, int peerPort) {
		super(peerPort);
		this.host = host;
		this.port = port;
	}

	public void filesOfPeerPeer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
