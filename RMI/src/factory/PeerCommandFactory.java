package factory;

import LogManager.LogManager;
import factory.peer.*;
import factory.products.*;

/**
 * Concrete Factory for Peer.
 * 
 * @author Paul Pfeiffer-Vogl
 * @version 22-Jan-2013
 */
public class PeerCommandFactory extends AbstractCommandFactory {
	
	private LogManager lm = null;
	
	// instances of the concrete products
	private FindFilePeer findFile = null;
	private RemoveFilesPeer removeFiles = null;
	private UnRegisterPeerPeer unRegisterPeer = null;
	private RegisterPeerPeer registerPeer = null;
	private AddFilesPeer addFiles = null;
	private boolean debug = true;

	public PeerCommandFactory(LogManager lm) {
		this.lm = lm;
	}

	/**
	 * create a concrete find file.
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public FindFile createFindFile() {
		return (findFile = new FindFilePeer(lm, debug));
	}

	/**
	 * create a concrete remove files.
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public RemoveFiles createRemoveFiles() {
		return (removeFiles = new RemoveFilesPeer(lm));
	}

	/**
	 * create a concrete unregisterPeeer
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public UnRegisterPeer createUnRegisterPeer() {
		return (this.unRegisterPeer = new UnRegisterPeerPeer(lm));
	}

	/**
	 * create a concrete remove files.
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public RegisterPeer createRegisterPeer() {
		return (this.registerPeer = new RegisterPeerPeer(lm));
	}

	/**
	 * create a concrete add files.
	 * 
	 * @param concrete prodcut
	 */
	@Override
	public AddFiles createAddFiles() {
		return (addFiles = new AddFilesPeer(lm));
	}

	/**
	 * @return the lm
	 */
	public LogManager getLm() {
		return lm;
	}

	/**
	 * @param lm
	 *            the lm to set
	 */
	public void setLm(LogManager lm) {
		this.lm = lm;
	}

	/**
	 * @return the findFile
	 */
	public FindFilePeer getFindFile() {
		return findFile;
	}

	/**
	 * @param findFile
	 *            the findFile to set
	 */
	public void setFindFile(FindFilePeer findFile) {
		this.findFile = findFile;
	}

	/**
	 * @return the removeFiles
	 */
	public RemoveFilesPeer getRemoveFiles() {
		return removeFiles;
	}

	/**
	 * @param removeFiles
	 *            the removeFiles to set
	 */
	public void setRemoveFiles(RemoveFilesPeer removeFiles) {
		this.removeFiles = removeFiles;
	}

	/**
	 * @return the unRegisterPeer
	 */
	public UnRegisterPeerPeer getUnRegisterPeer() {
		return unRegisterPeer;
	}

	/**
	 * @param unRegisterPeer
	 *            the unRegisterPeer to set
	 */
	public void setUnRegisterPeer(UnRegisterPeerPeer unRegisterPeer) {
		this.unRegisterPeer = unRegisterPeer;
	}

	/**
	 * @return the registerpeer
	 */
	public RegisterPeerPeer getRegisterPeer() {
		return registerPeer;
	}

	/**
	 * @param registerpeer
	 *            the registerpeer to set
	 */
	public void setRegisterPeer(RegisterPeerPeer registerPeer) {
		this.registerPeer = registerPeer;
	}

	/**
	 * @return the addFiles
	 */
	public AddFilesPeer getAddFiles() {
		return addFiles;
	}

	/**
	 * @param addFiles
	 *            the addFiles to set
	 */
	public void setAddFiles(AddFilesPeer addFiles) {
		this.addFiles = addFiles;
	}

}
