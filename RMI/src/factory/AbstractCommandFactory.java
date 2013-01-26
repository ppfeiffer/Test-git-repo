package factory;

import factory.products.*;

/**
 * Abstract product factory. Defines methods to get an abstract product
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public abstract class AbstractCommandFactory {
	
	private boolean debug = false;
	
	public abstract FindFile createFindFile();
	public abstract RemoveFiles createRemoveFiles();
	public abstract UnRegisterPeer createUnRegisterPeer();
	public abstract RegisterPeer createRegisterPeer();
	public abstract AddFiles createAddFiles();
}

