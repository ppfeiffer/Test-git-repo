package server.conmanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import peer.commands.CommandInterface;
import peer.commands.operations.AddFilesCommandImpl;
import peer.commands.operations.FilesOfPeerPeerCommandImpl;
import peer.commands.operations.FindFilePeerCommandImpl;
import peer.commands.operations.RegisterPeerCommandImpl;
import peer.commands.operations.RemoveFilesCommandImpl;
import peer.commands.operations.UnRegisterPeerCommandImpl;

import factory.AbstractCommandFactory;
import factory.ServerCommandFactory;
import factory.server.AddFilesServer;
import factory.server.FindFileServer;
import factory.server.RegisterPeerServer;
import factory.server.RemoveFilesServer;
import factory.server.UnRegisterPeerServer;

/**
 * This Class is the WorkerThread, which processes the requests from the Peer.
 * It gives the User an output what package it received.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 2013-01-05
 */
public class ServerSocketWorker implements Runnable {
	// will store the current connection
	private Socket socket;
	// show debug infos
	private boolean debug = false;
	private boolean stopped = false;
	private boolean DCstate = false;

	private ServerCommandFactory comm = null;

	private int udpPort;
	private int peerTimeout;

	public ServerSocketWorker(AbstractCommandFactory abstractCommandFactory,
			Socket socket, boolean debug, int udpPort, int peerTimeout) {
		this.socket = socket;
		this.debug = debug;
		this.comm = (ServerCommandFactory) abstractCommandFactory;
		this.udpPort = udpPort;
		this.peerTimeout = peerTimeout;
	}

	public void run() {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		try {
			// ois = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			CommandInterface received = null;
			//int tcpPort = 0;
			CommandWrapper command = new CommandWrapper();
			// set the host address of the client
			command.setHost(socket.getInetAddress().getHostAddress());
			command.setPeerTimeout(this.peerTimeout);
			command.setUdpPort(this.udpPort);

			// create the command factory object
			RemoveFilesServer rfs = comm.getRemoveFiles();
			RegisterPeerServer rps = comm.getRegPeer();
			AddFilesServer afs = comm.getAddFiles();
			FindFileServer ffs = comm.createFindFile();
			UnRegisterPeerServer urps = comm.getUnRegPeer();

			while ((received = (CommandInterface) in.readObject()) != null) {
				
				command.setWrapp(received);
				//System.out.println("Setting extend port: " + received.getPort());
				command.setTcpPort(received.getPort());
				// register if not registred yet
				if (command.getWrapp() instanceof RegisterPeerCommandImpl) {
					RegisterPeerCommandImpl rp = (RegisterPeerCommandImpl) command
							.getWrapp();

					if (debug) {
						//System.out.println("PeerRegister received " + rp.getPort());
					}
					try {
						// register the Peer, if he isn't already registred
						command = rps.registerPeer(command);
						
						if (command.getWrapp() == null) {
							throw new RemoteException();
						}
					} catch (RemoteException e) {
						out.writeObject(e);
					}

					out.writeObject(command.getWrapp());
				}
				
				// only allow commands if the peer registred before if
				if (comm.getReg().findPeer(
						socket.getInetAddress().getHostAddress(),
						command.getTcpPort())) {

					if (command.getWrapp() instanceof AddFilesCommandImpl) {
						
						command = afs.addFiles(command);
						try {
							// write back
							out.writeObject(command.getWrapp());
						} catch (RemoteException e) {
							out.writeObject(e);
							throw e;
						}
					} else if (command.getWrapp() instanceof FindFilePeerCommandImpl) { //
						// searching for specific file
						command = ffs.findFiles(command);

						try {
							// write back
							out.writeObject(command.getWrapp());
						
						} catch (RemoteException e) {
							out.writeObject(e);
							throw e;
						}

					} else if (received instanceof UnRegisterPeerCommandImpl) {

						//System.out
						//		.println("LISTING ALL FILLES BEFORE UNREGISTER:");
						//comm.getReg().listAllPeersFile();

						try {
							// rfs.removeFiles(command);
							urps.unregister(command);
						} catch (NumberFormatException e) {
							try {
								out.writeObject(new RemoteException(
										"Invalid port in RemoveFilesServer!"));
								throw e;
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (RemoteException e) {
							out.writeObject(e);
							throw e;
						}
						// write answer back
						out.writeObject(command.getWrapp());

						try {
							Thread.sleep(2000);
							// schlieﬂen des sockets
							socket.close();
						} catch (IOException e) {
							try {
								out.writeObject(new RemoteException());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.stopped = true;
						break;

					} else if (command.getWrapp() instanceof FilesOfPeerPeerCommandImpl) {
						try {
							command = ffs.findFilesOfPeer(command);

							out.writeObject(command.getWrapp());
						} catch (RemoteException e) {
							out.writeObject(e);
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("IO EXCPETION ");

						}
					}else if (command.getWrapp() instanceof RemoveFilesCommandImpl) {
						try {
							command = rfs.removeFiles(command);

							out.writeObject(command.getWrapp());
						} catch (RemoteException e) {
							out.writeObject(e);
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("IO EXCPETION ");
						}
					}
				} else {
					System.err.println("Please register first!" + command.getHost() + command.getTcpPort());
					//comm.getReg().listAllPeers();
					out.writeObject(new RemoteException("Register first! " + command.getHost() + command.getTcpPort()));
				}

				if (this.stopped)
					break;
			}
		}catch (java.net.SocketException e)
		{
			System.err.println("Socket Exception occured");
		} catch (java.io.EOFException e) {
			//System.out.println("Connection to peer closed unexcepted!");
		} catch (IOException e) {
			System.err.println("Socket IO Exception occured");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stopped = true;
	}

	public boolean recievedDC() {
		return this.DCstate;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

}
