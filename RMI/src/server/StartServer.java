package server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import LogManager.LogManager;

/**
 * This class will start the server.
 * 
 * Parameters: -tcpPort: the port on which the server tcp port should listen
 * this is the port for the Server instance.
 * 
 * -udpPort: the port on which the udp server should be startet This server
 * receivs isAlive packages and check if the peer didn't time out.
 * 
 * -checkPeriod: the period of time to check if peers timed out Max value 60000
 * 
 * -peerTimeout: the time after a peer times out if he doesn't send an isAlive
 * package Max value 60000
 * 
 * -debug: show debug information yes/no
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class StartServer {
	public static void main(String[] args) {
		args = new String[] { "-tcpPort", "1121", "-udpPort", "3000",
				"-checkPeriod", "2500", "-peerTimeout", "5000", "-debug" };
		// args = new String[]{"-help"};

		ServerArguments jct = new ServerArguments();
		JCommander jc = null;
		try {
			// neues jcommander Objekt wird erstellt
			jc = new JCommander(jct, args);
			// ueberpruefen ob das programm erfolgreich geparst wurde
			jc.parse();

			// Hilfe wird an den Benutzer ausgegeben und das Programm beendet
			if (jct.isHelp()) {
				// aufrufen der Hilfe-AUsgabe
				jc.usage();
				System.out
						.println("Paramters with a * are mandatory paramters!"
								+ "\n For further infromation supply -help param. Program will exit now!");
				// exit, wrong parameters supplied
				System.exit(0);
			}

		} catch (ParameterException ex) {
			
			if (jc != null) {
				jc.usage();
			}
				System.out.println("The following options are required: -tcpPort -udpPort -checkPeriod -peerTimeout ");
				System.out
						.println("Paramters with a * are mandatory paramters!"
								+ "\n For further infromation supply -help param. Program will exit now!");
			
			// if an error occured then the programm will exit
			System.exit(0);
		} catch (Exception ex) {
			System.out.println("Fatal Error. Progamm will exit now.");
			System.exit(0);
		}
		
		// check if the checkperiod was smaller than peer timeout
		if (jct.getCheckPeriod() > jct.getPeerTimeout()) {
			System.out
					.println("The checkperiod was invalid! Checkperiod cannot be greater or equals the peertimeout. Type -help for "
							+ "further information");
			return;
		}
		
		if (jct.getCheckPeriod() > 60000 || jct.getPeerTimeout() > 60000) {
			System.out
					.println("The checkperiod or Peertimeout was too big. Max. Value 60000." );
			return;
		}
		
		if (jct.getCheckPeriod() < 2000 || jct.getPeerTimeout() < 5000) {
			System.out
					.println("The checkperiod or Peertimeout was too small. \nMin. for PeerTimeout Value 5000.\nMin. for CheckPeriod 2000" );
			return;
		}
		
		// check the tcp and udp port are valid
		try {
			// get the ports
			int checktcp = jct.getTcpPort();
			int checkudp = jct.getUdpPort();
			
			// check if localhost was entered
			String checkip = args[5];
			checkip.startsWith("127.0.0.1");
			System.out.println("Notification: ip is set to localhost! Remote file downloads won't work.");
			
			if (checktcp < 1025 || checktcp > 65535 )
			{
				System.out.println("Tcp port not in range."+ " Range: 1024-65535");
				return ;
			}
			
			if(checkudp < 1025 || checkudp > 65535 )
			{
				System.out.println("UDP port not in range"+ " Range: 1024-65535");
				return ;
			}
		} catch (NumberFormatException ex) {
			System.out.println("Please insert numbers for ports!");
			return ;
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("paramter check fuction is out of bounds!");
			return ;
		} catch (IndexOutOfBoundsException ex) {
			System.out.println("paramter check fuction is out of bounds!");
			return ;
		} catch (Exception ex) {
			System.out.println("fatal error");
			return ;
		}
		
		// create logmangager object
		LogManager lm = new LogManager(jct.isDebug());
		// set first output in the log file
		lm.write("####################################################################");
		lm.write("SERVER LOG STARTED:");

		// create a serverinstance with the parameter provided by the user
		Server serverInstance = new Server(jct.getTcpPort(), jct.getUdpPort(),
				jct.getCheckPeriod(), jct.getPeerTimeout(), jct.isDebug(), lm);

		// initializ the server
		if (serverInstance.initializeServer()) {
			if (jct.isDebug()) {
				System.out
						.println("Successfully initialized the UDP and TCP Server");
				lm.write("Successfully initialized the UDP and TCP Server");
			}
		} else
			return;

		// start the thread that will check if the tcp or udp server were
		// stopped
		Thread serverThread = new Thread(serverInstance);
		serverThread.start();
		// wait until the server finished his work
		// this while is needed, because else the logmanager
		// would be closed immediately
		while (serverThread.isAlive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Server Interrupted");
			}
		}
		// closing the log
		lm.close();
	}
}
