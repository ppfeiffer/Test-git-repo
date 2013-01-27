package threadhandling;

import java.util.concurrent.ConcurrentHashMap;

import LogManager.LogManager;
import server.registry.PeerIdentification;

/**
 * This Class controls every Timer from the Peers in the ConcurrentHashMap. 
 * It checks if the Timer of an Peer reaches 0. If one Timer is 0 or below it will
 * time that Peer out and removes it from the Registry. 
 * 
 * @author Ribarich Michael
 * @version 2013-01-17
 */
public class TimerThread implements Runnable {
	// TimeOut value, when a Peer times out.
	private int timeOut = 0;
	// The periode in which the Timer has to check adsf a Peer timed out.
	private int checkPeriode = 0;
	// Support-Variable for the checkPeriode
	private int count = 0;
	// ThreadPool(UDP) reference
	private ThreadPoolingUDP tpudp = null;asdfadsf
	// The HashMap in which the Peer time out will be handled
	private ConcurrentHashMap<PeerIdentification, ThreadPoolingRegValues> hm = null;
	// Variable for debug purposes
	private boolean debug = sdf;
	// Controlls the state of the Thread
	private boolean stopped = fasdf

	/**
	 * Main Constructor which implements all necessary Variables and 
	 * changes milliseconds into seconds.
	 * 
	 * @param tpudp Reference to the UDP-ThreadPool Object
	 * @param hm2 Reference to the ConcurrentHashMap Object in which the Peer-timeouts get handled
 	 * @param timeOut The range in which a Peer has time to send an isAlive package
	 * @param checkPeriode The time in which this Class tests if an isAlive packages was receive
	 * @param debug Debug-state
	 * @param lm Reference to the LogManager Object
	 */
	public TimerThread(ThreadPoolingUDP tpudp,
			ConcurrentHashMap<PeerIdentification, ThreadPoolingRegValues> hm2,
			int timeOut, int checkPeriode, boolean debug, LogManager lm) {
		this.tpudp = tpudp;
		this.hm = hm2;
		// +500 for the Leeway
		this.timeOut = timeOut+500;
		this.checkPeriode = checkPeriode;
		this.debug = debug;
		this.lm = lm;

	}

	// Threadlogic, which is responsible for the timing of each Peer and times out inactive Peers 
	@Override
	public void run() 
	{
		//System.out.println("timeout: " + timeOut + " check: " + checkPeriode);
		this.lm.write("TimerThread: timeout: " + timeOut + " check: " + checkPeriode);
		while (!stopped) {
			if (hm.keySet() != null) {
				// Goes through every element of the HashMap
				for (PeerIdentification pi : hm.keySet()) {
					
					ThreadPoolingRegValues tprv = hm.get(pi);
					
					//System.out.println("Peer: " + pi.getKey() + " " + hm.get(pi).getTimer() + " Count: " + this.count);
					// Checks every checkPeriode seconds if a Peer has send an isAlive package
					if(this.count>=this.checkPeriode)
					{
						//System.out.println("Check Peers");
						//System.out.println("Flag: " + tprv.isFlag());
						// Resets the counter which determins if checkPeriode seconds elapsed
						this.count = 0;
						// Checks if an Peer timed out
						if (tprv.getTimer() <= 0)
						{
							// Deletes the specific worker from the HashMap
							tpudp.delWorkerInReg(pi);
							
							System.out.println("Peer: " + pi.getKey() + " timed out!");
						}else if (tprv.isFlag() == true)
						{
							//System.out.println("Received isAlive from: " + pi.getKey());
							// Resets the time out Clock from a Peer
							tpudp.resetClockInReg(pi);
						}
					}
					// Decreases the Clock of the active selected Peer(foreach) 
					tprv.setTimer(tprv.getTimer() - 1000);
					// Increases the counter for the checkPeriode
					this.count = this.count + 1000;
				}
			}
			try {
				// Waits for 1 second
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				lm.write("TimerThread has been interrupted(Sleep)");
			}
		}
		this.lm.write("TimerThread stopped!");
		System.out.println("Threads stopped!");
		
	}

	/**
	 * Getter-Method for the bool, which controls the Thread
	 * 
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * Setter-Method for the bool, which controls the Thread
	 * 
	 * @param stopped
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * Setter-Method for debug purposes
	 * 
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}