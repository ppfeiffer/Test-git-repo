package factory.products;

import LogManager.LogManager;

/**
 * Abstract product for print files
 * 
 * @author Paul Pfeiffer-VOgl
 * @version 22-Jan-2013
 */
public abstract class PrintFiles {
	private boolean debug = false;
	private LogManager lm = null;
	
	public PrintFiles(LogManager lm) {
		this.lm = lm;
	}
	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the lm
	 */
	public LogManager getLm() {
		return lm;
	}
}
 
