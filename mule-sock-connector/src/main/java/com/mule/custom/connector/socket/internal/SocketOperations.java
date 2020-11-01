package com.mule.custom.connector.socket.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;

public class SocketOperations {

	@MediaType(value = ANY, strict = false)
	  public String sendReceive(String message, @Config SocketConfiguration configuration, @Connection SocketConnection connection){
	    System.out.println( "Sending and receiving message from host : [" + connection.getHost() + " : " + connection.getPort() + "]");
	    return processSendReceive(connection.getHost(), connection.getPort(), connection.getTimeout(), message);
	  }
	
	private String processSendReceive(String host, int port, int timeout, String inMsg) {
		
		
			Socket socket = null;
			
			DataOutputStream out = null;
	            
            DataInputStream in = null;
	        String receivedMsg ="";
	        
	        System.out.println( "calling method processSendReceive : " + host + " : " + port + " : " + inMsg );

		 try
	        {
	            // Connect to the server
	            socket = new Socket( host, port );
	            socket.setSoTimeout( timeout );
	     
	            in = new DataInputStream(socket.getInputStream()); 
	            out = new DataOutputStream(socket.getOutputStream()); 
	            
	            System.out.println("");
	            System.out.println("=========================================================================\n");
	            System.out.println("");
				
	            out.writeUTF(inMsg); 
	            out.flush();

	            System.out.print( "Msg Sent: " + inMsg);
	            System.out.println("");
	            System.out.println("");
	            
	            receivedMsg = in.readUTF(); 
	            System.out.println("Msg Received: " + receivedMsg);
	            	 
	            System.out.println("");
	            
	            System.out.println("=========================================================================\n");
	            
	            System.out.println("");
				
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	            
	        } finally {
	        	try {
		        	in.close();
		            out.close();
		            socket.close();
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        	}
	        }
		 return receivedMsg;
		 
	}
}
