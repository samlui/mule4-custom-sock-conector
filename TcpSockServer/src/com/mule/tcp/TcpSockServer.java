package com.mule.tcp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Hex;


public class TcpSockServer extends Thread {
	 private ServerSocket serverSocket;
	    private int port;
	    private boolean running = false;

	    public TcpSockServer( int port )
	    {
	        this.port = port;
	    }

	    public void startServer()
	    {
	        try
	        {
	            serverSocket = new ServerSocket( port );
	            this.start();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }

	    public void stopServer()
	    {
	        running = false;
	        this.interrupt();
	    }

	    @Override
	    public void run()
	    {
	        running = true;
	        while( running )
	        {
	            try
	            {
	                System.out.println( "Listening for a connection" );

	                // Call accept() to receive the next connection
	                Socket socket = serverSocket.accept();

	                // Pass the socket to the RequestHandler thread for processing
	                RequestHandler requestHandler = new RequestHandler( socket );
	                requestHandler.start();
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	        }
	    }

	    public static void main( String[] args )
	    {
	        if( args.length == 0 )
	        {
	            System.out.println( "Usage: SimpleSocketServer <port>" );
	            System.exit( 0 );
	        }
	        int port = Integer.parseInt( args[ 0 ] );
	        System.out.println( "Start server on port: " + port );

	        TcpSockServer server = new TcpSockServer( port );
	        server.startServer();

	        // Automatically shutdown in 1 minute
	        try
	        {
	            Thread.sleep( 600000 );
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	        }

	        server.stopServer();
	    }
	}

	class RequestHandler extends Thread
	{
	    private Socket socket;
	    RequestHandler( Socket socket )
	    {
	        this.socket = socket;
	    }

	    @Override
	    public void run()
	    {

//	    	OutputStreamWriter out=null;
//	    	BufferedReader in=null ;
	    	DataInputStream in = null; 
            DataOutputStream out = null;
	    	try
	        {
	            System.out.println( "Received a connection" );

	            // Get input and output streams
	            
	            in = new DataInputStream(socket.getInputStream()); 
                out = new DataOutputStream(socket.getOutputStream()); 
                
                
                String receivedMsg = in.readUTF();
                String sendMsg = null;
                
				System.out.println("=========================================================================\n");
				System.out.println("");
				
				System.out.println("Recevied : " + receivedMsg);
				
				sendMsg = "Echo: "+ receivedMsg;
				out.writeUTF(sendMsg); 
							
				System.out.println("-------------------------------------------------------------------------\n");
				System.out.println("");
				
				System.out.println("Sent response : " + sendMsg); 
				
				System.out.println("");
				System.out.println("=========================================================================\n");
				System.out.println("");

	            out.flush();

	            // Close our connection
//	            in.close();
//	            out.close();
//	            socket.close();

	            System.out.println( "Connection closed" );
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	        } 
	        	
	            try {
	            	in.close();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
	    }
}