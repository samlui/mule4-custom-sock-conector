package com.mule.custom.connector.socket.internal;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class SocketConnection {

	  private final String host;
	  private final int port;
	  private final int timeout;
	  
	  public SocketConnection(String host, int port, int timeout) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	  }

	  public void invalidate() {
		    // do something to invalidate this connection!
	  }
	  
	  public String getHost() {
			return host;
	  }

	  public int getPort() {
		  return port;
	  }
			
	  public int getTimeout() {
		  return timeout;
	  }
}
