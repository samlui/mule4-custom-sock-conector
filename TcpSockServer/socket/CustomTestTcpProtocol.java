package com.mule.socket;

import static java.lang.System.arraycopy;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.mule.extension.socket.api.socket.tcp.TcpProtocol;
import org.slf4j.Logger;

//import com.mule.util.ASCIIEBCDIC;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;

import java.nio.charset.StandardCharsets;

public class CustomTestTcpProtocol implements TcpProtocol {

  protected static final int READ_ATTEMPTS = 50;
  protected static final int CUSTOM_BUFFER_SIZE = 512;
  private static final String HEADER = "This is my custom protocol.";
  private static final Logger LOGGER = getLogger(CustomTestTcpProtocol.class);

  protected int bufferSize;

  public CustomTestTcpProtocol() {
    this(CUSTOM_BUFFER_SIZE);
  }

  public CustomTestTcpProtocol(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public void printhex(byte[] inBytes) {
		
		for ( byte b : inBytes )
	        System.out.printf("%02x ", b);
		System.out.println("\n");
	}

	
  @Override
  public InputStream read(InputStream socketIs) throws IOException {
	  
	BufferedReader in = new BufferedReader( new InputStreamReader( socketIs, StandardCharsets.ISO_8859_1 ));
	

	String str = "";
	
	ByteBuffer bBuff = ByteBuffer.allocateDirect( 65000 );
	CharBuffer cbuff = bBuff.asCharBuffer();
	
	
	int size = in.read( cbuff );
	

    if (size <= 0) {
      throw new IOException("Number of read attempts exceeded! Failed to read any data from socket!");
    }
    
	//gets the message from the stream
	for ( int i = 0; i < size; i++ ) {
		str += cbuff.get( i );
	}
	
	byte[] myBytes = str.getBytes( StandardCharsets.ISO_8859_1 );
	
	System.out.println("response ios8859 : " + myBytes.length);
	printhex(myBytes);
	Charset ibmcharset = Charset.forName("IBM037");
	Charset iso88591charset = Charset.forName("ISO-8859-1");
	
	ByteBuffer bb = ByteBuffer.wrap(myBytes);
	
	CharBuffer cb = ibmcharset.decode(bb);

	System.out.println("received mainframe response : " + cb.length()  + " : " + cb);
	
	byte[] outBytes = cb.toString().getBytes();
//	
//	ByteBuffer bb1 = iso88591charset.encode(cb);
	
	System.out.println("response ebcdic encoded : " + outBytes.length);
	printhex(outBytes);
	
	
	//	System.out.println("received mainframe response : " + myBytes.length);

//	char[] tmpBytes = Hex.encodeHex( myBytes);
//	System.out.println( "mainframe Response (Hex-EBCDIC) " + tmpBytes.length +  " : " + "\"" + tmpBytes + "\"" );
	
	//converts the ebcdic to ascii
//	String msgResult = new String( ASCIIEBCDIC.convertStrToASCII(myBytes ) , StandardCharsets.ISO_8859_1 );
	
//	System.out.println( "mainframe ASCII Response (ASCII) " + msgResult.length() + " : " + "\"" + msgResult + "\"" );
//	 
	
	
//	byte[] buffer = new byte[bufferSize];
//
//    int bytesRead, attempts = 0;
//    while ((bytesRead = socketIs.read(buffer)) <= 0 && (attempts < READ_ATTEMPTS)) {
//      attempts++;
//    }

//    if (bytesRead <= 0) {
//      throw new IOException("Number of read attempts exceeded! Failed to read any data from socket!");
//    }
	in.close();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(outBytes.length);
    
    byteArrayOutputStream.write( outBytes, 0, outBytes.length);
    return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	

  }

  public String convertToEbcdic(byte[] inMsg) {
	  
	 String outMsg = new String( inMsg, StandardCharsets.ISO_8859_1 );
	 Charset ibmcharset = Charset.forName("IBM037");
	 Charset isocharset = Charset.forName("ISO-8859-1");
	 
	 ByteBuffer inputBuffer = ByteBuffer.wrap(inMsg);
	 
	 CharBuffer chars = isocharset.decode(inputBuffer);
	 
	 ByteBuffer data = ibmcharset.encode(chars);
	 
	 byte[] outputData = data.array();
	 String out = new String(outputData, StandardCharsets.ISO_8859_1 );
	 return out;
  }
  
  @Override
  public void write(OutputStream os, InputStream data) throws IOException {
    byte[] messageToSend = null;
    byte[] inputPayload = new byte[bufferSize];

    int dataLength = data.read(inputPayload);
    if (dataLength == bufferSize) {
      LOGGER.warn("Data length exceeds buffer size so data will be chunked.");
    }
    
//    arraycopy(HEADER.getBytes(), 0, messageToSend, 0, HEADER.length());

    if (dataLength >= 0) {
    	messageToSend = new byte[ dataLength];
//      arraycopy(inputPayload, 0, messageToSend, HEADER.length(), dataLength);
    	arraycopy(inputPayload, 0, messageToSend, 0, dataLength);
    }
    String outMsg = convertToEbcdic(messageToSend);
    try {
    	OutputStreamWriter out = new OutputStreamWriter( os, StandardCharsets.ISO_8859_1 );
//    	String str = new String (messageToSend);
//    	System.out.println("str out : " + str.getBytes().length + " : " + str.length() + " : " + str) ;
    	
//    	out.write(str);
    	out.write(outMsg);
        out.flush();
        
//    	BufferedOutputStream writer = new BufferedOutputStream(os);
//      writer.write(messageToSend, 0, HEADER.length() + dataLength);
//      writer.flush();
    } catch (Exception ex) {
    	LOGGER.error("Exception caught");
    	ex.printStackTrace();
    }
  }
}