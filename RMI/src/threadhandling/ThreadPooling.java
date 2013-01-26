package threadhandling; 

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import factory.AbstractCommandFactory;
import LogManager.LogManager;

/**
 * This is the super ThreadPooling Class. It contains the main Methods used by the 2 specific ThreadPooling Classes(TCP, UDP).
 * It creates an ExecuterService and uses one Executor which implements an newCachedThreadPool.
 * 
 * @author Ribarich Michael
 * @version 2013-01-17
 */
public class ThreadPooling
{
	private ExecutorService tpool = null;
	private int timeOut = 0;
	private int checkPeriode = 0;
	private boolean debug = false;
	protected LogManager lm;
	private AbstractCommandFactory comm = null;
	
	/**
	 * This Constructor creates the newCachedThreadPool
	 * 
	 * @param comm Reference to the AbstractCommandFactory Object
	 * @param timeOut The range in which a Peer has time to send an isAlive package
	 * @param checkPeriode The time in which the TimerThread tests if an isAlive packages was receive
 	 * @param debug Debug-state
	 * @param lm Reference to the LogManager Object
	 */
	public ThreadPooling(AbstractCommandFactory comm, int timeOut, int checkPeriode, boolean debug, LogManager lm)
	{
		this.comm = comm;
		this.timeOut = timeOut;
		this.checkPeriode = checkPeriode;
		this.debug = debug;
		this.lm = lm;
		
		// This creates a CachedThreadPool
		tpool = Executors.newCachedThreadPool();
	}
	
	/**
	 * This Constructor calls the other Constructor, because some Pools dont need and/or have the timeout and checkPeriode
	 * 
	 * @param comm Reference to the AbstractCommandFactory Object
	 * @param debug Debug-State
	 * @param lm Reference to the LogManager Object
	 */
	public ThreadPooling(AbstractCommandFactory comm, boolean debug, LogManager lm)
	{
		this(comm, -1, -1, debug, lm);
	}

	/**
	 * Getter-Method for the ThreadPool(ExectuerService)
	 * 
	 * @return the tpool
	 */
	public ExecutorService getTpool() {
		return tpool;
	}

	/**
	 * Getter-Method for the LogManager
	 * 
	 * @return the lm
	 */
	public LogManager getLm() {
		return lm;
	}

	/**
	 * Setter-Method for the LogManager
	 * 
	 * @param lm the lm to set
	 */
	public void setLm(LogManager lm) {
		this.lm = lm;
	}

	/**
	 * Setter-Method for the ThreadPool(ExecuterService)
	 * 
	 * @param tpool the tpool to set
	 */
	public void setTpool(ExecutorService tpool) {
		this.tpool = tpool;
	}

	/**
	 * Getter-Method for the TimeOut
	 * 
	 * @return the timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Setter-Method for the TimeOut
	 * 
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * Getter-Method for the CheckPeriode
	 * 
	 * @return the checkPeriode
	 */
	public int getCheckPeriode() {
		return checkPeriode;
	}

	/**
	 * Setter-Method for the CheckPeriode
	 * 
	 * @param checkPeriode the checkPeriode to set
	 */
	public void setCheckPeriode(int checkPeriode) {
		this.checkPeriode = checkPeriode;
	}


	/**
	 * Getter-Method for the Debug-state
	 * 
	 * @return the debug-state
	 */
	public boolean isDebug() {
		return debug;
	}


	/**
	 * Setter-method for the AbstractCommandFactory
	 * 
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	
	/**
	 * Getter-Method for the AbstractCommandFactory
	 * 
	 * @return the comm
	 */
	public AbstractCommandFactory getComm() {
		return comm;
	}

	/**
	 * Setter-Method for the AbstractCommandFactory
	 * 
	 * @param comm the comm to set
	 */
	public void setComm(AbstractCommandFactory comm) {
		this.comm = comm;
	}

}
