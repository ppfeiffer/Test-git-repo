package server.registry;

import java.io.Serializable;


public class PeerFile implements Serializable, Comparable<PeerFile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;
	private long size;

	public PeerFile(String fileName, long size) {
		this.fileName = fileName;
		this.size = size;

	}

	public String getKey() {
		return this.fileName + ":" + this.size;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public int compareTo(PeerFile arg0) {
		return this.getKey().compareTo(arg0.getKey());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeerFile) {
			PeerFile pf = (PeerFile) obj;
			
			if (this.fileName.equals(pf.getFileName())
					&& this.size == pf.getSize()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.getKey().hashCode();
	}

}
