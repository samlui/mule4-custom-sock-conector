package com.mule.custom.connector.socket.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketConnectionProvider implements PoolingConnectionProvider<SocketConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(SocketConnectionProvider.class);

   @DisplayName("Host IP")
   @Parameter
   @Optional(defaultValue = "localhost")
   private String host;
   
   @DisplayName("Port")
   @Parameter
   @Optional(defaultValue = "9090")
   private int port;
   
   @Parameter
   @Optional(defaultValue = "10000")
   private int timeout;

  @Override
  public SocketConnection connect() throws ConnectionException {
    return new SocketConnection(host, port, timeout);
  }

  @Override
  public void disconnect(SocketConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getHost() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(SocketConnection connection) {
    return ConnectionValidationResult.success();
  }
}
