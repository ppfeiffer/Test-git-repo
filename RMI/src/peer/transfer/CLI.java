package peer.transfer;

import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.*;
import java.util.*;
import peer.cli.*;
import peer.directory.*;
import peer.filetransfer.*;
import peer.commands.CommandInvoker;
import peer.commands.conretecommand.AddFilesCommand;
import peer.commands.conretecommand.DownloadCommand;
import peer.commands.conretecommand.FindFilePeerCommand;
import peer.commands.conretecommand.RegisterPeerCommand;
import peer.commands.operations.*;
import factory.*;
import factory.peer.*;
import server.conmanager.*;
import LogManager.*;
import com.beust.jcommander.*;

/**
 * 
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-22
 */

public class CLI extends Thread {

	// define the private Attributes
	public boolean weiter = true;
	private String serverHost;
	private int tcpServerPort;
	private boolean debug;
	private LogManager lm;
	private Socket socket;
	private boolean stopped = false;
	private PeerCommandFactory comm;
	private int peerTcp;
	private TCPPeer tcpPeer;
	private Thread tcpThread;
	private int peerUdp;
	private String sharedFolder;
	private FileManager fm;

	/**
	 * Overwrite the default constructor and set the paramter values to the
	 * global defined variables.
	 * 
	 * @param sharedFolder
	 *            path to the shared folder
	 * @param serverHost
	 *            ip address of the server
	 * @param tcpServerPort
	 *            tcp port of the server
	 * @param peerTcp
	 *            udp port of the server
	 * @param peerUdp
	 *            peerUdp of the client
	 * @param debug
	 *            if true, display debug informations
	 * @param lm
	 *            log manager for writeing all the actions to a log file
	 */
	public CLI(String sharedFolder, String serverHost, int tcpServerPort,
			int peerTcp, int peerUdp, boolean debug, LogManager lm) {
		this.tcpServerPort = tcpServerPort;
		this.sharedFolder = sharedFolder;
		this.serverHost = serverHost;
		this.peerTcp = peerTcp;
		this.peerUdp = peerUdp;
		this.debug = debug;
		this.lm = lm;

		// create a new Commandfactory
		this.comm = new PeerCommandFactory(lm);
		// create the products of the command factory
		comm.createRemoveFiles();
		comm.createRegisterPeer();
		comm.createAddFiles();
		comm.createFindFile();
		comm.createUnRegisterPeer();

		// create the new file manager with the given shared folder
		fm = new FileManager(this.sharedFolder);
		fm.createDir();
		fm.printAllFiles();
		fm.scanSharedFolder();
		fm.printAllFiles();
	}

	/**
	 * This method will instalize the tcp and udp server instances.
	 * 
	 * @return true if successful, else false
	 */
	public boolean initializeServer() {
		// create a new tcp server instance
		this.tcpPeer = new TCPPeer(comm, this.sharedFolder, this.peerTcp,
				this.debug, lm);
		// add the TCPPeer object into a Thread
		tcpThread = new Thread(tcpPeer);
		// check if the TCPPeer object was created successfully...
		if (this.tcpPeer == null || tcpThread == null) {
			// if not, then print a error message to the console
			System.out.println("Coundn't create tcp server or udp server");
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the log file
				this.lm.write("Coundn't create tcp server or udp server");
			}
			// break the method flow by returning false
			return false;
		}
		// if the TCPPeer object was created successfully...
		else {
			try {
				// initializing the tcp server and the the udp server
				this.tcpPeer.initializeSocket();
				// catch exceptions
			} catch (Exception e) {
				// print a error message to the console and write it into the
				// log file
				System.out
						.println("Coundn't create tcp server or udp server(Unknown Exception)."
								+ e.getMessage());
				this.lm.write("Coundn't create tcp server or udp server(Unknown Exception)."
						+ e.getMessage());
				// if the debug options is on
				if (this.debug) {
					// then print a detailed error message to the log file
					e.printStackTrace();
				}
				// break the method flow by returning false
				return false;
			}

			// start the both servers
			tcpThread.start();
			// try to sleep 1 second
			try {
				Thread.sleep(1000);
				// catch exceptions
			} catch (InterruptedException e) {
				// print a error message to the console and write it into the
				// log file
				System.out
						.println("Error while trying to sleep after upd/tcp server start!");
				lm.write("Error while trying to sleep after upd/tcp server start!");
				// if debuging is enabled, print the debug information
				if (debug) {
					// then print a detailed error message to the log file
					e.printStackTrace();
				}
			}
			// if the debug operation is enabled
			if (this.debug) {
				// print a error message to the console and write it into the
				// log file
				this.lm.write("Created tcp and udp server");
			}
			// check if both server were started successfully
			if (this.tcpThread.isAlive()) {
				// write the current status into the log file
				System.out.println("File Server up.");
				this.lm.write("User output: Server up. Hit enter to exit.");
			}
			// if both servers coudn't start correctly
			else {
				// if any error occurs, then print an error message
				System.out.println("Coudn't start both servers correctly!");
				this.lm.write("Coudn't start both servers correctly!");
				// break the method flow by returning false
				return false;
			}
			// return true, if everything was successfully
			return true;
		}
	}

	/**
	 * This method will initializes the socket.
	 * 
	 * @return ture if the socket was initialized, false if failed
	 */
	public boolean initializeSocket() {
		try {
			// try to create a new scoket connection with the server ip and the
			// server tcp port
			socket = new Socket(serverHost, tcpServerPort);
			// if the creation was successfully, then retunr true
			return true;
		}
		// if any error occurs, then catch it and print a error message
		catch (java.net.ConnectException e) {
			System.out.println("Couldn't connect, maybe server not running?");
			lm.write("Couldn't connect, maybe server not running?");
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				// e.printStackTrace();
			}
			// break the method flow by returning false
			return false;
		}
		// if any error occurs, then catch it and print a error message
		catch (IOException e) {
			System.out.println("Error while creating server socket!");
			lm.write("Error while creating server socket!");
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
			// break the method flow by returning false
			return false;
		}
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		System.out.println("CLI ready for user input");

		// define needed attributes
		String input;
		RegisterPeerPeer rpp = comm.getRegisterPeer();
		AddFilesPeer afp = comm.getAddFiles();
		UnRegisterPeerPeer urp = comm.getUnRegisterPeer();
		FindFilePeer ffp = comm.getFindFile();
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		
		// execute a task in schedul
		TimerTask task = null;
		Timer timer = null;
		// invoker for the command pattern that will create the
		// concrete commands that will be sent over the network
		CommandInvoker invoker = new CommandInvoker();
		// create the input and output streams
		
		try {
			// check if the socket is not null
			if( this.socket != null)
			{
				// create the concrete streams out of the sockets
				out = new ObjectOutputStream(this.socket.getOutputStream());
				in = new ObjectInputStream(this.socket.getInputStream());
			}
		}
		// if any error occurs, then catch it and print a error message
		catch (IOException e) {
			lm.write("Couldn't create output or input stream.");
			System.out.println("Couldn't create output or input stream.");
			// if the debug options is on
			if (this.debug) {
				// then print a detailed error message to the console
				e.printStackTrace();
			}
		}
		// create new command wrapper object
		CommandWrapper command = new CommandWrapper();
		// set the tcp server port
		command.setTcpPort(this.peerTcp);
		// register peer
		try {
			// set the new command
			invoker.setCommand(new RegisterPeerCommand(command.getTcpPort()));
			// set the register als wrapper object
			command.setWrapp(invoker.action());
			// check the port and set it
			command = rpp.registerPeer(command);
		}
		// if any error occurs, then catch it and print a error message
		catch (RemoteException e) {
			System.out.println("Invalid tcp port given by user.");
			lm.write("Invalid port in registerPeer. " + e.getMessage());
		}

		// wirte the register command to the server
		try {
			out.writeObject(command.getWrapp());
		}
		// if any error occurs, then catch it and print a error message
		catch (IOException e2) {
			lm.write("Error while writing object. " + e2.getMessage());
			if (this.debug) {
				e2.printStackTrace();
			}
		}
		// reading the register answer
		try {
			Object o;
			// reding the answer
			o = in.readObject();
			// if no error occured
			if (o instanceof RegisterPeerCommandImpl) {
				// receiving udp port for isAlive packages
				RegisterPeerCommandImpl rp = (RegisterPeerCommandImpl) o;
				// set received tcp port of the udp is alive thread form the
				// server side
				command.setUdpPort(rp.getPort());
				if(command.getUdpPort() == 0)
				{
					System.err.println("Failed to register");
					System.out.println("Program will shut down");
					try{
						socket.close();
					}catch(IOException e)
					{
					}
					
					return ;
				}
				// set the received peer timeout; sets the period of time when
				// isalive
				// packages will be sent
				command.setPeerTimeout(rp.getTimeout());

				lm.write("received peer timeout: " + command.getPeerTimeout());
				// if an error occured, then the server will return an remote
				// exception
			} else if (o instanceof RemoteException) {
				System.out.println(((RemoteException) o).getMessage());
				// there shouldn't be received any other classes, but for the
				// case....
			} else {
				System.out.println("no cast found RegisterPeer " + o.getClass());
				lm.write("no cast found RegisterPeer " + o.getClass());
			}
		} catch (IOException e) {
			System.out
					.println("Error while sending/receiving the register peer answer");
		} catch (ClassNotFoundException e) {
			System.out
					.println("Coudn't find the specific cast class while waiting for the register command answer.");
		}

		// //// start sharedFolder listener
		// start the filelistener; will check if whetever a file was removed;
		// added; modified
		// on one of these events, it will tell write the changes to the server
		task = new FileListener(command, this.serverHost, this.tcpServerPort,
				comm, fm);
		// check all 5 Seconds if any files have changed
		timer = new Timer();
		timer.schedule(task, new Date(), 10000);

		// /
		// register files
		try {
			// if the sharedFolder is empty, there are no files to publish
			if (fm.getFileList().size() <= 0) {
				System.out.println("No Files to publish");
			} else {
				// create the new concrete command
				invoker.setCommand(new AddFilesCommand(fm, command.getTcpPort()));
				// create the concrete command
				// set the new wrapper
				command.setWrapp(invoker.action());
				// call the addfiles method
				command = afp.addFiles(command);

				// write the list of files to add to the server
				try {
					out.writeObject(command.getWrapp());
				} catch (IOException e) {
					System.out
							.println("Error while writing the add files command!");
				}

				// receive answer
				try {
					// CONTAINS LIST OF FILES THAT WERE ADDED
					// on server side -> no actual implementation
					// for checking if EVERY file was successfully added
					Object o = in.readObject();
					// instance of add files concret object
					if (o instanceof AddFilesCommandImpl) {
						// received object
						AddFilesCommandImpl af = (AddFilesCommandImpl) o;
						// check if there was something usefull returned
						if (af.getFileList() != null
								&& af.getFileList().size() > 0) {
							System.out.println("Files added!");
						} else {
							System.out
									.println("Return was null. Maybe no files added");
						}
						// if there was an exception received...
					} else if (o instanceof RemoteException) {
						System.out.println("Coudn't add files");
						throw (RemoteException) o;
					} else {
						lm.write("Unknown class: " + o.getClass());
					}

				} catch (ClassNotFoundException e) {
					lm.write("Unknown class: " + e.getCause());
				} catch (IOException e) {
					System.out.println("Error while reading the addfiles command answer");
				}

			}
		// catch exceptions and write in into the log file
		} catch (RemoteException e1) {
			lm.write("Error: " + e1.getMessage());
			System.err.println(e1.getMessage());
		}
		// create is alive object; will send every peerTime a isAlive package
		// to the server; to show him that the peer is still alive
		IsAlive keepAlive = new IsAlive(this.comm, this.serverHost,
				this.tcpServerPort, command.getUdpPort(), this.peerTcp,
				this.peerUdp, command.getPeerTimeout(), debug, lm);

		// start is alive in thread
		Thread r = new Thread(keepAlive);
		r.start();

		String[] eingabe;
		// read user input
		Scanner read = new Scanner(System.in);
		// close the socket that was used for the
		// registration and addfile commands
		try {
			// close the socket
			this.socket.close();
		// catch exceptions and handle them
		} catch (java.net.SocketException e) {
			System.out.println("Error while closing register/addfiles socket.");
		// catch exceptions and handle them
		} catch (IOException e) {
			System.out.println("Closed register connection");
		}

		// from now on, the user can interact with the peer
		// we will read the user inputs and execute the specific command, if
		// available
		while (!this.stopped) {
			System.out.print("\n:>");
			// read the next line
			input = read.nextLine();
			// if the input is to long, wirte a user message and
			// discard input
			if (input.length() > 255) {
				System.out.println("Input too long max 255 chars");
				// read again the user input
				continue;
			}

			// create a new socket and connect the the control server
			if (!initializeSocket()) {
				// if the connection was not successfully then print a message..
				System.out.println("Coundn't create connection");
				// and stop the application
				break;
			}

			// create the output and input streams to communicate with the
			// server
			try {
				// if the connection to the server was succesfully, then create 
				// the streams out of the socket
				out = new ObjectOutputStream(this.socket.getOutputStream());
				in = new ObjectInputStream(this.socket.getInputStream());
			// catch exeptions 
			} catch (IOException e) {
				System.out.println("Error while creating the Outputstream.");
				// read again a new user input
				continue;
			}
			// split the input line after spaces
			eingabe = input.split(" ");
			
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
			
			// now check the use input for the command "findFile"
			if (eingabe[0].equals("findFile")) {
				System.out.println("FindFile");
				
				// create a new CLIFindFile object where the needed parameters are defined
				// with jcommander
				CLIFindFile jct = new CLIFindFile();
				JCommander jc = null;
				try {
					// neues jcommander Objekt wird erstellt
					jc = new JCommander(jct, eingabe);
					// ueberpruefen ob das programm erfolgreich geparst wurde
					jc.parse();

					// Hilfe wird an den Benutzer ausgegeben und das Programm
					// beendet
					if (jct.isHelp()) {
						// aufrufen der Hilfe-AUsgabe
						jc.usage();
						// print the usage of the findFile command and read again the user input
						System.out
								.println("Paramters with a * are mandatory paramters!"
										+ "\n For further infromation supply -help param. Program will exit now!");
						
						continue;
					}
				// catch all the exceptions if the user enters an invalid command
				} catch (ParameterException ex) {
					// pirnt the exception
					System.out.println(ex.getMessage());
					if (jc != null) {
						// print a usage of the paramters of the findFile command
						jc.usage();
						System.out
								.println("Paramters with a * are mandatory paramters!"
										+ "\n For further infromation supply -help param. Program will exit now!");
					}
					// read again the user input
					continue;
				// catch exceptions and print a error message
				} catch (Exception ex) {
					System.out.println("Fatal Error. Progamm will exit now.");
					// read again the user input
					continue;
				}
				// now check if the command semantik was correct
				if (jct.getFileName().size() == 2 || jct.getFileName().size() == 3) {
					// searching for specific file
					try {
						// set the command
						command = ffp.findFiles(command);
						// get the filename and exact search and send it to the
						// server
						// list index: index 0 -> findFile; index 1 -> fileName
						command.setWrapp(new FindFilePeerCommandImpl(jct.getFileName().get(1), jct.isExact(), this.peerTcp));

						// set the new command
						invoker.setCommand(new FindFilePeerCommand(jct.getFileName().get(1), jct.isExact(),this.peerTcp));

						// create the concrete command and set it to the wrapper
						command.setWrapp(invoker.action());

						try {
							// send find files object to the server
							out.writeObject(command.getWrapp());
							try {
								// now receive the file list of the given peer
								FindFileServerCommandImpl ffs = (FindFileServerCommandImpl) in.readObject();
								// print the fileList of the peer
								ffs.printList();
								// if there is no file found
								if (ffs.getListOfFile().size() == 0) {
									System.out.println("No matching files found.");
								}
							// catch execptions and show them to the user
							} catch (IOException e) {
								System.out.println("Error while reading the find file server command");
							// catch execptions and show them to the user
							} catch (ClassNotFoundException e) {
								System.out.println("Coudn't find class for find file server.");
							}
						// catch execptions and show them to the user
						} catch (IOException e) {
							System.out.println("Error while sending the find file command");
						}
						// catch execptions and show them to the user
					} catch (RemoteException e1) {
						System.out.println("Error while setting the wrapper object for findfiles.");
					} 
				}
				// if the peer entered the findFile command not correctly
				else {
					// pirnt 
					System.out.println("Not all parameters given. Enter 'findFile -help' for further information");
				}
				
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////				
				
			// now check the use input for the command "filesOfPeer"
			} else if (eingabe[0].equals("filesOfPeer")) {
				
				System.out.println("filesOfPeer");
				// used to get user input with the required parameters
				CLIFindFile jct = new CLIFindFile();
				JCommander jc = null;
				try {
					// neues jcommander Objekt wird erstellt
					jc = new JCommander(jct, eingabe);
					// ueberpruefen ob das programm erfolgreich geparst wurde
					jc.parse();

					// Hilfe wird an den Benutzer ausgegeben und das Programm
					// beendet
					if (jct.isHelp()) {
						// aufrufen der Hilfe-AUsgabe
						jc.usage();
						System.out.println("Paramters with a * are mandatory paramters!" + "\n For further infromation supply -help param!");
						// return to the user input again
						continue;
					}
				// error while parsing parameters
				} catch (ParameterException ex) {
					// print the exception to the console
					System.out.println(ex.getMessage());
					// if the paramters of the command "filesOfPeers" not correct and the initalization
					// of the jcommander was successfully 
					if (jc != null) {
						// print the usage of the "filesOfPeers" command
						jc.usage();
						System.out.println("Paramters with a * are mandatory paramters!" + "\n For further infromation supply -help param!");
					}
					// return to the user input again
					continue;
				// catch exceptions 
				} catch (Exception ex) {
					System.out.println("Fatal Error.");
					// return to the user input again
					continue;
				}
				// check the commad of its semantik, the "filesOfPeer" command requires
				// two arguments: the command itself and the endpoint of the ohter peer
				// e.g: "filesOfPeer 127.0.0.1:1234"
				if (jct.getFileName().size() != 2) {
					System.out.println("Invalid number of parameters supplied!");
					// return to the user input again
					continue;
				}
				// now try to split the endpoint of the other peer into a ip address and a port
				try {
					String[] split = null;
					try {
						// spilt the endpoint of the other pper
						split = jct.getFileName().get(1).split(":");
					// catch exceptions and print the error message
					} catch (Exception e) {
						System.out.println("Invalid parameter given");
						// return to the user input again
						continue;
					}
					// get only the host address
					String host = split[0];
					// try to check the validation the ip address of the ohter peer
					try {
						// create new InetAddress object with the host of the other peer
						InetAddress inet = InetAddress.getByName(host);
					// catch exceptions 
					} catch (UnknownHostException e) {
						System.out.println("Invalid IP Address given.");
						// return to the user input again
						continue;
					}
					
					// now we check the port of the ohter peer
					int port;
					// check the range of the port and validate it
					try {
						// try to parse the port into a integer variable
						port = Integer.parseInt(split[1]);
						// now the the range of the entered port
						if (port < 1 || port >=  65535) {
							// if the range is invalid
							System.out.println("Port Range invalid!");
							// return to the user input again
							continue;
						}
					// if the parsing the port into an integer value return an error
					} catch (NumberFormatException e) {
						// print an exception 
						System.out.println("Error: Invalid port given");
						// return to the user input again
						continue;
					}

					// get the filename and exact search and send it to the server
					command.setWrapp(new FilesOfPeerPeerCommandImpl(host, port,command.getTcpPort()));
					// set the conrete command with the wrapp 
					command = ffp.findFilesOfPeer(command);
					// write the object to the socket
					out.writeObject(command.getWrapp());
					try {
						// read the reply as an object from the server
						Object o = in.readObject();
						// if the received object from the server is a type of a FilesOfPeerServerCommandImpl
						if (o instanceof FilesOfPeerServerCommandImpl) {
							// then cast the object to the concrete object
							FilesOfPeerServerCommandImpl fop = (FilesOfPeerServerCommandImpl) o;
							// iterate through the files of the peer and....
							for (String x : fop.getPeerFiles()) {
								// print each file to the console
								System.out.println(x);
							}
							if (fop.getPeerFiles().size() == 0) {
								System.out.println("No files found.");
							}
						// if the exception is a type of the RemoteException...
						} else if (o instanceof RemoteException) {
							// print the error message
							System.out.println(((RemoteException) o).getMessage());
						} else {
							// print the error message
							System.out.println("Couldn't cast: " + o.getClass());
						}
					// catch exceptions and show the errors to the client
					} catch (ClassNotFoundException e) {
						System.out.println("IO EXCPETION ");
						lm.write("IO EXCPETION " + e.getMessage());
						// if debug mode is enabled, show detailed error message
						if (debug) {
							e.printStackTrace();
						}
					}
				// catch exceptions and show the errors to the client
				} catch (RemoteException e) {
					try {
						// write the remote exception to the socket
						out.writeObject(e);
					// catch exceptions and show the errors to the client
					} catch (IOException e1) {
						System.out.println("Remote Exception by filesOfPeer command");
						lm.write("Remote Exception" + e1.getMessage());
						// if debug mode is enabled, show detailed error message
						if (debug) {
							e.printStackTrace();
						}
					}
				// catch exceptions and show the errors to the client
				} catch (IOException e) {
					System.out.println("IO EXCPETION by filesOfPeer command");
					lm.write("IO EXCPETION " + e.getMessage());
					// if debug mode is enabled, show detailed error message
					if (debug) {
						e.printStackTrace();
					}
				}
			}

/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
			
			// now check the user input for the command "stop"
			else if (eingabe[0].equals("stop")) {
				// print a message to the client
				System.out.println("See ya!");
				// set the stop variable to true and stop the running thread
				this.stopped = true;
				
				// create the unregister command
				command = urp.unregister(command);

				// send unregister object
				try {
					// write the unregister object to the socket
					out.writeObject(command.getWrapp());
				// catch exceptions and show the errors to the client
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
					lm.write("IOException: " + e.getMessage());
					// if the debug value is set... 
					if (debug) {
						// then print detailed error message
						e.printStackTrace();
					}
				}
				//closing the filetransferThread if a transfer is active
				catch (Exception e) {
					System.out.println("Cant close the transfer socket and the thread.");
					lm.write("Cant close the transfer socket and the thread.");
					// print a detailed error information
					if (debug) {
						e.printStackTrace();
					}
				}
				// wait for object
				try {
					// read the object from the socket
					Object o1 = in.readObject();
					// if the object is a type of the class UnRegisterPeerCommandImpl
					if (o1 instanceof UnRegisterPeerCommandImpl) {
						// then create the concrete object
						UnRegisterPeerCommandImpl unreg = (UnRegisterPeerCommandImpl) o1;
						// check if the unregistration was succesfully
						if (unreg.getPort() == this.peerTcp) {
							// print a sucess message to the client
							System.out.println("Unregistration successful!");
						}
					// if the object is a type of the class RemoteException 
					} else if (o1 instanceof RemoteException) {
						// then print the concrete error message
						System.err.println("RemoteException: "+ ((RemoteException) o1).getMessage());
					// if the concrete type of the received object is unknown
					} else
						// print a specific error message
						System.out.println("Unkown call at this point: "+ o1.getClass());
				// catch exceptions
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
					lm.write("IOException: " + e.getMessage());
					// if the debug value is set... 
					if (debug) {
						// then print detailed error message
						e.printStackTrace();
					};
				// catch exceptions
				} catch (ClassNotFoundException e) {
					System.out.println("Exception: " + e.getMessage());
					lm.write("Exception: " + e.getMessage());
					// if the debug value is set... 
					if (debug) {
						// then print detailed error message
						e.printStackTrace();
					}
				}
				// set the variable true, to stop sending isAlive packets to the server
				keepAlive.setStopped(true);
				try{
					// close anything else
					keepAlive.close();
				}catch(Exception e)
				{
					lm.write("error while closing isalive");
				}
	

				// close filelsitener
				timer.cancel();
				
				try {
					// set the staus of the tcp peer to stopping
					this.tcpPeer.setStopped(true);
					// close teh socket from the tcp peer
					this.tcpPeer.getSs().close();
				// catch exceptions
				} catch (IOException e) {
					System.out.println("Closing tcp server");
					lm.write("Exception: " + e.getMessage());
					// if the debug value is set... 
					if (debug) {
						// then print detailed error message
						e.printStackTrace();
					}
				}
				
				// stop the CLI thread at this point
				this.stopped = true;
				// finally print a closing message to the client
				System.out.println("Connection terminated by user!");
				// abord the programm
				break;

/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
			
			// now check the user input for the command "stop"
			} else if (eingabe[0].equals("downloadFile")) {

				System.out.println("downloadFile");
				
				// create a new class, where the parameters of the given command is checked
				CLICommand jct = new CLICommand();
				JCommander jc = null;
				try {
					// neues jcommander Objekt wird erstellt
					jc = new JCommander(jct, eingabe);
					// ueberpruefen ob das programm erfolgreich geparst wurde
					jc.parse();

					// Hilfe wird an den Benutzer ausgegeben und das Programm
					// beendet
					if (jct.isHelp()) {
						// aufrufen der Hilfe-AUsgabe
						jc.usage();
						System.out.println("Paramters with a * are mandatory paramters!"+ "\n For further infromation supply -help param!");
						// return to the user input again
						continue;
					}

				}
				// catch exceptions
				catch (ParameterException ex) {
					System.out.println(ex.getMessage());
					lm.write("Error: " + ex.getMessage());
					// check if the parameters are valid, which was entered by the client
					if (jc != null) {
						// print the user
						jc.usage();
						System.out.println("Paramters with a * are mandatory paramters!" + "\n For further infromation supply -help param!");
					}
					// return to the user input again
					continue;
				// catch exceptions
				} catch (Exception ex) {
					System.out.println("Fatal Error.");
					// return to the user input again
					continue;
				}
				// check the downloadFile command to its sematik
				if (jct.getFileName().size() != 3) {
					// if arguments are invalid
					System.out.println("Invalid number of parameters supplied!");
					// return to the user input
					continue;
				}
				
				// now we split the command at the : 
				String[] split = jct.getFileName().get(1).split(":");
				// get the host pasrt
				String host = split[0];
				// parse the port into a integer variable
				int port = 0;
				try{
					 port = Integer.parseInt(split[1]);
				}catch(NumberFormatException e)
				{
					System.out.println("Invalid port given!");
				}
				catch(Exception e)
				{
					System.out.println("Invalid port given");
				}
				// get the name of the requested file
				String fileName = jct.getFileName().get(2);
				// create the new concrete command
				System.out.println(fileName + " " + peerTcp);
				invoker.setCommand(new DownloadCommand(fileName, this.peerTcp));
				// create the concrete command
				// set the new wrapper
				command.setWrapp(invoker.action());
				
				Socket ft = null;
				try {
					// create the socket to the fileserver
					ft = new Socket(host, port);
				// catch exceptions
				} catch (UnknownHostException e3) {
					// print exceptions
					System.out.println("Invalid host given! Host not found!");
				// catch exceptions
				} catch (IOException e3) {
					// print exceptions
					System.out.println("Error while sending the download file command");
				}

				try {
					// open the stream
					ObjectOutputStream ftout = new ObjectOutputStream(ft.getOutputStream());
					// write the object
					ftout.writeObject(command.getWrapp());
				// catch exceptions
				} catch (IOException e) {
					// print exceptions
					System.out.println("Error while sending the Download File object to the fileserver");
				}
				catch(Exception e)
				{
					System.out.println("Couldn't connect to server");
					return ;
				}
				
				// create the tranmission client object
				TransmissionClient fileTranser = new TransmissionClient(this.sharedFolder, fileName, ft);
				fileTranser.run();
				
				File x = new File(this.sharedFolder + File.separator + fileName);
				if( x.exists() && x.length() > 0 )
				{
					System.out.println("File "+fileName+" was downloaded successfully");
				}
				// start the transmission client as a thread
				// in order to enable the stop feature even you when you download a file
				//Thread fileTransferThread = new Thread(fileTranser);
				//fileTransferThread.start();
				
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////		
				
			// check if user wants help information
			} else if (eingabe[0].equals("-help") || eingabe[0].equals("help") || eingabe[0].equals("?")) {
				System.out.println("HELP information");
				System.out.println("\n+--------------------------------------------------------------------------+");
				// show the findFile description
				System.out
						.println("| findFile <fileName> [-e]                                      "
								+ "\n|                                                                          "
								+ "\n|Queries the server for finding all files with the specified file name.    "
								+ "\n|The third parameter is optional, indicating whether only exact matches    "
								+ "\n|shall be included in the result. Otherwise all files with a name          "
								+ "\n|containing <fileName> case-insensitively must be included. The server     "
								+ "\n|returns all matching files (i.e. their names and sizes) and for each      "
								+ "\n|file the owning peers in form of IP addresses and ports. If there's no    "
								+ "\n|hit, \"No matching files found.\" shall be printed to the console,        "
								+ "\n|otherwise for each file the name and size (in KB), followed by the IP     "
								+ "\n|addresses and ports of the owning peers.                                  ");

				System.out
						.println("+--------------------------------------------------------------------------+");
				// show the filesOfpeer description
				System.out
						.println("| filesOfPeer <ipAddress:port>                                "
								+ "\n|                                                                        "
								+ "\n|Queries the server for finding all files of the specified peer. The     "
								+ "\n|server returns all files (i.e. their names and sizes) shared by the     "
								+ "\n|peer identified by this IP address and port. If no files were found     "
								+ "\n|(i.e. peer does not share any file at the moment or is not       		  "
								+ "\n|registered), print \"No files found.\" to the console.);                ");

				System.out
						.println("+--------------------------------------------------------------------------+");
				// show the downloadFile descriptio
				System.out
						.println("| downloadFile <ipAddress:port> <fileName>                      "
								+ "\n|                                                                          "
								+ "\n|Downloads the specified file from the specified peer. The file has to be  "
								+ "\n|saved to the peer's shared directory. In case a file with this name       "
								+ "\n|already exists, you should not download that file but instead print \"File"
								+ "\n|already exists in your shared directory.\" to the console.                "
								+ "\n|When the uploading peer receives this request it has to check whether the "
								+ "\n|peer who's trying to download is registered (therefore the downloading per"
								+ "\n|has to submit its TCP port) by contacting the server. If it's not   "
								+ "\n|registered,the uploading peer has to reject the upload. The uploading peer"
								+ "\n|also has to verify that it actually owns the requested file - if not, it  "
								+ "\n|has to inform the other peer appropriately. Also take care about \"..\"   "
								+ "\n|characters in file names, otherwise a peer can also access files outside  "
								+ "\n|your shared directory (you may simply tell the other peer you don't own   "
								+ "\n|that file in such a case).                                                ");

				System.out
						.println("+--------------------------------------------------------------------------+");
			// if the user enters a unsopperted command then print a specific error message
			} else {
				System.out
						.println("Error: Command not found. Type \"-help\", \"help\" or \"?\" to for further help. "
								+ "\nYou can get specifc documentation to every command by typing the command and concating -help.");
			}
			// close the socket again -> save resources
			try {
				// close the socket
				this.socket.close();
			// catch exceptions
			} catch (IOException e) {
				System.out.println("Closed connection to server");
			}
		}
	}
}