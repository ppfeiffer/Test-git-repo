package threadhandling;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentHashMap;

import factory.AbstractCommandFactory;
import factory.ServerCommandFactory;

import LogManager.LogManager;

import server.conmanager.DatagramSocketWorker;
import server.registry.PeerIdentification;

/**
 * This is one of two specific ThreadPooling Classes. It is specified on UDP ThreadPooling.
 * It adds UDPServerWorkers to the Pool.
 * 
 * @author Ribarich Michael
 * @version 2013-01-17
 */
public class ThreadPoolingUDP extends ThreadPooling {
	// For debbuging purposes 
	private boolean debug = false;
	// Object of the WorkerThread that will be added to the ThreadPool
	private DatagramSocketWorker dsw = null;
	// Object of the TimerThread that handles timeouts for each Peer
	private TimerThread tt = null;
	// The HashMap that contains all active UDP_PeerConnections(PeerIdentification) and their timer,isAlive-Flag
	private ConcurrentHashMap<PeerIdentification, ThreadPoolingRegValues> hm = null;

	private int tcpPort;
	/**
	 * The specific Constructor for an UDP-ThreadPool
	 * 
	 * @param comm Reference to the AbstractCommandFactory Object
	 * @param timeOut The range in which a Peer has time to send an isAlive package
	 * @param checkPeriode The time in which the TimerThread tests if an isAlive packages was receive
 	 * @param debug Debug-state
	 * @param lm Reference to the LogManager Object
	 */
	public ThreadPoolingUDP( AbstractCommandFactory comm, int timeOut, int checkPeriode, boolean debug, LogManager lm) {
		super(comm, (timeOut), checkPeriode, debug, lm);
		// Creates the HashMap which will be used by the TimerThread
		this.hm = new ConcurrentHashMap<PeerIdentification, ThreadPoolingRegValues>();
		tt = new TimerThread(this,hm,super.getTimeOut(),super.getCheckPeriode(), this.debug, super.getLm());
		//new Thread(tt).start();
		//if(!(this.count <= 50) ^ this.count == 0)
		// Starts one TimerThread
		this.addTimer();
	}

	/**
	 * This Method adds Worker in the Pool and calls addWorkerInReg
	 * 
	 * @param socket for the Identification of the Peer(DatagramSocket)
	 */
	public void addDatagramSocket(DatagramPacket packet) {
		
		dsw = new DatagramSocketWorker(((ServerCommandFactory)super.getComm()).getReg(), packet, this.debug, super.getLm());
		super.getTpool().execute(dsw);
		
		this.addWorkerInReg(packet);
	}

	/**
	 * Getter-Method for the DatagramSOcketWorker.
	 * 
	 * @return the dsw
	 */
	public DatagramSocketWorker getDsw() {
		return dsw;
	}
	
	/**
	 * This Method adds Worker in the HashMap
	 * 
	 * @param socket for the Identification of the Peer(DatagramSocket)
	 */
	public void addWorkerInReg(DatagramPacket packet)
	{
		hm.put(new PeerIdentification(packet.getAddress().getHostAddress(), Integer.parseInt(new String(packet.getData(), 0, packet.getLength())) ), new ThreadPoolingRegValues(super.getTimeOut()));
	}
	
	/**
	 * Resets the Timer which decides if the Peer timedOut.
	 * 
	 * @param pi for the Identification of the Peer(PeerIdentification)
	 */
	public void resetClockInReg(PeerIdentification pi)
	{
		hm.put(pi, new ThreadPoolingRegValues(super.getTimeOut()));
	}
	
	/**
	 * Setter-Method for the flag of an Peer in the ConcurrentHashMap
	 * 
	 * @param pi sets the flag(bool) for the Peer(PeerIdentification)
	 */
	public void setIsAlive(PeerIdentification pi)
	{
		hm.put(pi, new ThreadPoolingRegValues(hm.get(pi).getTimer(),true));
	}
	
	/**
	 * This Method deletes the deliverd Peer from the ConcurrentHashMap
	 * 
	 * @param pi for the Identification of the Peer(PeerIdentification)
	 */
	public void delWorkerInReg(PeerIdentification pi)
	{	
		hm.remove(pi);
		((ServerCommandFactory)super.getComm()).getReg().deletePeer(pi.getHost(), pi.getPort());
		//if(count>0)
		//	this.count--;
	}
	
	/**
	 * This Method starts one TimerThread
	 */
	public void addTimer()
	{
		new Thread(tt).start();
		//this.count = 1;
	}
	
	/**
	 * This Class will stop the TimerThread.
	 */
	public void stopTimer()
	 {
		tt.setStopped(true);
	 }
}
