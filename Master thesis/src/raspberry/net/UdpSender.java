package raspberry.net;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UdpSender {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
             System.out.println("Usage: java UdpSender <hostname> <filename>");
             return;
        }

	if (args[1].length()>255) {
	    System.out.println(" Filename longer than 255 chars");
	    return;
	}


	// get a datagram socket
        DatagramSocket socket = new DatagramSocket();

        // send request
        byte[] buf = new byte[255+24];
        InetAddress address = InetAddress.getByName(args[0]);

	String cmd = "LDFILE";

	ByteBuffer bb = ByteBuffer.wrap( buf );
	bb.order( ByteOrder.LITTLE_ENDIAN );

	System.arraycopy( cmd.getBytes(), 0, buf, 0, cmd.length());
	bb.putLong(8,123);
	bb.putDouble(16,234);
	
	System.arraycopy( args[1].getBytes(), 0, buf, 32, args[1].length());

    
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 32320);
        socket.send(packet);
    
        // get response
        packet = new DatagramPacket(buf, buf.length);

	socket.setSoTimeout(500);
	try {
	    socket.receive(packet);
	    // decode and display response
	    String  retState = BytesString.getString(buf, 0, 8);
	    Long    retSb    = bb.getLong(8);
	    Double  retMs    = bb.getDouble(16);
	    String  retAttr  = BytesString.getString(buf,24,255);
	    System.out.println("state: " + retState + " (took: "+retMs+" ms ) :: "+retAttr);
	}
	catch (SocketTimeoutException e) {
	    // timeout exception.
	    System.out.println("ERR: got no reply, timeout reached" + e);
	}

        socket.close();
    }
}




