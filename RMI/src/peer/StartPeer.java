package peer;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import peer.transfer.CLI;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import LogManager.LogManager;

/**
 * This class starts a peer with the required parameters. If any paramter is
 * invalid or missing, the user gets a error message. So he can enter all the
 * required informations successfully.
 * 
 * Parameter:
 * 
 * -tcpPort: The port on which the file server will start. The file server waits
 * for incoming connection that request a file and will send it back.
 * 
 * -udpPort: The port on which the IsAlive packages will be sent from. The is
 * alive packages signal the server that the peer is still online.
 * 
 * -sharedDir: The Path to the folder where the shared directory is. All files
 * in this folder will be published to the server. If this folder doesn't exist,
 * it will be created.
 * 
 * -debug: show debug information
 * 
 * @author Koyuncu Harun(hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-20
 */

public class StartPeer {
	public static void main(String[] args) {
		///System.out.println("Welcome");
		// define the needed arguments
		//args = new String[] { "-tcpPort", "5555", "-udpPort", "1556","-serverData", "127.0.0.1:1121", "-sharedDir","D:" + File.separator + "sharedFolder", "-debug" };
		args = new String[] { "-tcpPort", "1000", "-udpPort", "1555","-serverData", "127.0.0.1:1121", "-sharedDir","D:" + File.separator + "sharedFolder2", "-debug" };

		// args = new String[]{"-help"};
		PeerArguments jct = new PeerArguments();
		JCommander jc = null;
		// define attributes
		String host = null;
		int port = -1;

		try {
			// create a new jcommander object
			jc = new JCommander(jct, args);
			// check if the parameters are valid
			jc.parse();

			// show the help menue
			if (jct.isHelp()) {
				// call the help menue and print it to the console
				jc.usage();
				// print a information for the client
				System.out
						.println("Paramters with a * are mandatory paramters!"
								+ "\n For further infromation supply -help param. Program will exit now!");
				// exit program
				System.exit(0);
			}
			// now we have to spilt the connection data from the server into the
			// ip address and the port
			String[] servrConndata = jct.getServerConnection().split(":");
			// save the ip address in a variable
			host = servrConndata[0];
			// convert the port into an Integer and save it in a variable
			port = Integer.parseInt(servrConndata[1]);
			// catch exceptions
		} catch (ParameterException ex) {
			System.out.println(ex.getMessage());
			if (jc != null) {
				// show the help menue and print it to the console
				jc.usage();
				// print a information message to the console, if the required
				// parameters wasn't well
				System.out
						.println("Paramters with a * are mandatory paramters!"
								+ "\n For further infromation supply -help param. Program will exit now!");
			}
			// if an error occured then the programm will exit
			System.exit(0);
			// catch exceptions
		} catch (Exception ex) {
			System.out.println("Fatal Error. Progamm will exit now.");
			// close the application
			System.exit(0);
		}

		// create a new logmanager
		LogManager lm = new LogManager(jct.isDebug());
		lm.write("####################################################################");
		lm.write("SERVER LOG STARTED:");

		// the following code checks if the shared folder contains at the end of
		// the path
		// a "slash". if there is a "slash" then it will be removed
		if (jct.getSharedDir().charAt(jct.getSharedDir().length() - 1) == File.separatorChar) {

			String tmp = jct.getSharedDir();
			tmp = tmp.substring(0, jct.getSharedDir().length() - 1);
			jct.setSharedDir(tmp);
		}

		// check if the given hostaddress was valid
		InetAddress inet = null;
		try {
			inet = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("Wrong host address:");
			lm.write("Wrong host address:");

		} catch (Exception e) {
			System.out.println("Error getting hostname " + host + " Error:"
					+ e.getMessage());
			System.out.println("Error getting hostname " + host + " Error:"
					+ e.getMessage());
		}

		if (inet == null) {
			System.out
					.println("The ip address was invalid. Program will exit now!");
			lm.write("The ip address was invalid. Program will exit now!");
			return;
		}

		// instaze des command line interfaces erstellen
		CLI peerInstance = new CLI(jct.getSharedDir(), host, port,
				jct.getTcpPort(), jct.getUdpPort(), jct.isDebug(), lm);
		// instanziate the socket
		if (peerInstance.initializeSocket()) {
			if (jct.isDebug()) {
				System.out
						.println("Successfully initialized the CLI Interface");
				lm.write("Successfully initialized the CLI Interface");
			}

		} else {
			System.out.println("Coudn't initializ server. Shutting down");
			lm.write("Coudn't initializ server. Shutting down");
			
			return ;
		}

		// initializ the tcp server for file incoming file requests
		if (peerInstance.initializeServer()) {
			if (jct.isDebug()) {
				System.out
						.println("Successfully initialized the UDP and TCP Server");
				lm.write("Successfully initialized the UDP and TCP Server");
			}
		} else {
			System.out.println("Coudn't initializ server. Shutting down");
			return;
		}

		// start the thread that will check if the tcp server was
		// stopped
		Thread serverThread = new Thread(peerInstance);
		serverThread.start();

		// check is the peer thread is alive
		while (serverThread.isAlive()) {
			try {
				// wait for two seconds
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				if (jct.isDebug()) {
					e.printStackTrace();
				}
				// write errors into the log file
				lm.write("Error: " + e.getMessage());
			}
		}
		// closing the log
		lm.close();
	}
}
