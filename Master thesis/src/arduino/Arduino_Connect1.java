package arduino;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;

import arduino.mainclass;
import gui.*;



public class Arduino_Connect1 implements SerialPortEventListener {
	SerialPort serialPort;
       /** The port we're normally going to use. */
	
	private static final String PORT_NAMES[] = { 
		"/dev/tty.usbserial-A9007UX1", // Mac OS X
                   "/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		"COM3", "COM1","COM5","COM2","COM4","COM6","COM7","COM8","COM9" // Windows
};
	
/*	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                       "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};*/
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;


	
	public Arduino_Connect1() throws Exception {
		
		int result = initialize();
		System.out.println("Result bit:"+ result);
		if (result==1){
			
			
			Thread t=new Thread() {
				
				public void run() {	
					
					//the following line will keep this app alive for 1000 seconds,
					//waiting for events to occur and responding to them (printing incoming messages to console).
					try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
				}
			};
			t.start();
			System.out.println("--Started--");
			mainclass.config = 1;
			CS_allinone.config = 1;
			
			
		} else {
			System.out.println("Please recheck the Arduino connections.");
			
					
		} 
			
	}
	
	
	
	public int Arduino_Send(String ip) throws Exception {

		int result;
		
		if(ip.contains("q")){
			
			close();
			System.out.println("--Connections Closed--");
			result = 0;
			
		} else {
			
			ioData(ip);
			result = 1;
		}
				
		return result;
		
	}
		
	//for inputing choice

	
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	public int initialize() throws Exception {
               // the next line is for Raspberry Pi and 
               // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
              // System.setProperty("gnu.io.rxtx.SerialPorts", "COM3");
             
               String port = CS_allinone.getPort();
               //System.out.println("port."+port+".");
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		System.out.println(portEnum);
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			 //System.out.println("currportId."+currPortId.getName()+".");
				if (currPortId.getName().equals(port)) {
					portId = currPortId;
					{break;}
					
				}
				
			
		}
		
		
		
		
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return 0;
		} else {
			
			System.out.println("Found portId: "+ portId);
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			System.out.println("Connecting to serial port: "+ serialPort);
			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			//output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return 1;
	}

	// to write data to the Arduino
	
	public synchronized void ioData(String ip) throws IOException {
		
//		System.out.println("[+: Laser on, -: laser off, a: Blink twice!, q: quit]\n Enter choice: ");
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String data = br.readLine();
		
		
		output = serialPort.getOutputStream();
		output.write( ip.getBytes() );
		
	

}
	
//	public synchronized String ioData() throws IOException {
//		
//				System.out.println("[+: Laser on, -: laser off, a: Blink twice!, q: quit]\n Enter choice: ");
//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				String data = br.readLine();
//				
//				output = serialPort.getOutputStream();
//				output.write( data.getBytes() );
//				return data;
//			
//		
//	}
//	
	
	
	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	
	
	public synchronized void close() {
		
		String x ="-";
		
		try {
			
			output = serialPort.getOutputStream();
			output.write( x.getBytes() );
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				
				String inputLine=input.readLine();
				
				System.out.println(inputLine);
				
				CS_allinone.display.append("\n\n"+inputLine);
				
				
			} catch (Exception e) {
				
				if(e.toString()=="java.lang.NullPointerException"){
					//System.out.println("Exception!!");
				}
				else{System.out.println(e.toString());}
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}




	
	
}