/**
 * 
 */
package factory;

import LogManager.LogManager;
import factory.server.*;
import server.registry.Registry;

/**
 * Concrete Factory for server
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class ServerCommandFactory extends AbstractCommandFactory {
	private LogManager lm = null;
	private Registry reg = null;
	private boolean debug = false;
	// instances of the concrete products
	private RegisterPeerServer regPeer  = null;
	private AddFilesServer addFiles     = null;
	private FindFileServer findFile	  = null;
	private RemoveFilesServer removeFiles = null;
	private UnRegisterPeerServer unRegPeer = null;
	
	
	public ServerCommandFactory(LogManager lm)
	{
		this.lm = lm;
		this.reg = new Registry();
	}
	
	/**
	 * create a concrete find file.
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public FindFileServer createFindFile() {
		return this.findFile = new FindFileServer(reg, lm, debug);
	}

	/**
	 * create a concrete remove files
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public RemoveFilesServer createRemoveFiles() {
		return this.removeFiles = new RemoveFilesServer(reg, lm, debug);
	}

	/**
	 * create a concrete unregister peer
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public UnRegisterPeerServer createUnRegisterPeer() {
		return this.unRegPeer = new UnRegisterPeerServer(reg, lm, debug);
	}

	/**
	 * create a concrete register peer
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public RegisterPeerServer createRegisterPeer() {
		return this.regPeer = new RegisterPeerServer(reg, lm, debug);
	}
	
	/**
	 * create a concrete add files
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public AddFilesServer createAddFiles() {
		return this.addFiles = new AddFilesServer(reg, lm, debug);
	}
	
	/**
	 * @return the reg
	 */
	public Registry getReg() {
		return reg;
	}

	/**
	 * @param reg the reg to set
	 */
	public void setFileReg(Registry reg) {
		this.reg = reg;
	}

	/**
	 * @return the lm
	 */
	public LogManager getLm() {
		return lm;
	}

	/**
	 * @param lm the lm to set
	 */
	public void setLm(LogManager lm) {
		this.lm = lm;
	}

	/**
	 * @return the regPeer
	 */
	public RegisterPeerServer getRegPeer() {
		return regPeer;
	}

	/**
	 * @return the addFiles
	 */
	public AddFilesServer getAddFiles() {
		return addFiles;
	}

	/**
	 * @return the findFile
	 */
	public FindFileServer getFindFile() {
		return findFile;
	}

	/**
	 * @return the removeFiles
	 */
	public RemoveFilesServer getRemoveFiles() {
		return removeFiles;
	}

	/**
	 * @return the unRegPeer
	 */
	public UnRegisterPeerServer getUnRegPeer() {
		return unRegPeer;
	}
	
	/**
	 * @param reg the reg to set
	 */
	public void setReg(Registry reg) {
		this.reg = reg;
	}

	
}
