package server.registry;

import java.io.Serializable;

public class PeerIdentification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;
	
	public PeerIdentification(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	public String getKey() {
		return this.host +":"+ this.port;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof PeerIdentification )
		{
			PeerIdentification pif = (PeerIdentification) o;
			if( this.host.equals(pif.getHost()) && port == pif.getPort())
				return true;
		}
		
		return false;
		
	}

	@Override
	public int hashCode(){
		return (host+":"+port).hashCode();
	}
}
