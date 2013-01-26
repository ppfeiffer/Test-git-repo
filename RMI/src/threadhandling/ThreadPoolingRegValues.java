package threadhandling;

/**
 * This Class contains all necessary variables for every Peer to handle the Timer.
 * 
 * @author Ribarich Michael
 * @version 2013-01-20
 */
public class ThreadPoolingRegValues 
{
	private boolean flag = false;
	private int timer = 0;
	
	
	/**
	 * This is main Constructor it takes the timeOut time and saves it with and boolean flag which contains 
	 * the AlivePackage state, if the server received one or not. It is used to set the current time a Peer has 
	 * left to send an isAlive package.
	 * 
	 * @param timer The value of the current time left of an Peer to send an isAlive package
	 */
	public ThreadPoolingRegValues(int timer)
	{
		this.timer = timer;
	}
	
	
	/**
	 * This Constructor is used to set an boolean in the HashMap
	 * 
	 * @param timer The value of the current time left of an Peer to send an isAlive package
	 * @param isAlive State of the isAlive flag, which detirmins if the Server got an isAlive package 
	 */
	public ThreadPoolingRegValues(int timer, boolean isAlive)
	{
		this.timer = timer;
		this.flag = isAlive;
	}
	
	/**
	 * Getter-Method for the boolean Flag(isAlive)
	 * 
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}
	
	/**
	 * Setter-Method for the boolean Flag(isAlive)
	 * 
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	/**
	 * Getter-Method for the Timer
	 * 
	 * @return the timer
	 */
	public int getTimer() {
		return timer;
	}

	/**
	 * Setter-Method for the Timer
	 * 
	 * @param timer the timer to set
	 */
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	
}
