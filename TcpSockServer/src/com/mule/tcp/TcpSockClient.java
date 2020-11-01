package com.mule.tcp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;


public class TcpSockClient {

	public static void main( String[] args )
    {
		

        if( args.length < 2 )
        {
            System.out.println( "Usage: SimpleSocketClientExample <server> <port>" );
            System.exit( 0 );
        }
        String server = args[ 0 ];
        int port = Integer.valueOf(args[ 1 ]);
        
        System.out.println( "connecting to : " + server + " : " + port);

        try
        {
            // Connect to the server
            Socket socket = new Socket( server, port );
     
            DataInputStream in = new DataInputStream(socket.getInputStream()); 
            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
            
            System.out.println("");
            System.out.println("=========================================================================\n");
            System.out.println("");
			
            System.out.print( "What do you want to send : ");
            
            
            Scanner scn = new Scanner(System.in); 
            
            String sendMsg = scn.nextLine(); 
            
            out.writeUTF(sendMsg); 
            out.flush();
            

            String receivedMsg = in.readUTF(); 
            System.out.println("Msg Received: " + receivedMsg);
            	 
            System.out.println("");
            System.out.println("=========================================================================\n");
            System.out.println("");
			
            // Close our streams
            in.close();
            out.close();
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
