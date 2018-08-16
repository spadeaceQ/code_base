package gui;

import gnu.io.CommPortIdentifier;
import ij.IJ;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import arduino.Arduino_Connect1;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class CS_allinone implements ActionListener{

	private static final String PORT_NAMES[] = { 
		"/dev/tty.usbserial-A9007UX1", // Mac OS X
                   "/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		"COM3", "COM1","COM5","COM2","COM4","COM6","COM7","COM8","COM9" // Windows
};	
	 JFrame frame = new JFrame("Control software");
	
	public static int config = 0;
	
	public static JTextField display2;
	
	private JPanel panel;
		private JTextField textField_1;
		private JTextField textField_2;
		private JTextField textField_3;
		private JTextField textField_4;
		private JTextField textField_5;
		 JTextField expTime;	
	 @SuppressWarnings("rawtypes")
	 JComboBox modeBox, patternBox, rmodeBox;
	 JButton set,run,prop;
	 String mode, pattern, rmode;
	public static String ard_port;

	public static List<CommPortIdentifier> comPorts = null;
	String exp;
	 JScrollPane logs;
	 public static JTextArea display;
	 boolean q = false;
	 private JPanel main;
		
		
		JScrollPane logs2;
		JButton testL,testR,testSX,testQX,testMM, testA, ardON, ardOFF, blink, quit, connect;
		JTextField power,talk;
		
		String laserPath = "C://Program Files (x86)//Qioptiq//NanoControl//NanoControl.exe";
		String mConPath = "C://Program Files//MetroCon-3.1//jre//bin//javaw.exe";
		String sxArg1 = "-DDisplayPlatform=R4";
		String sxArg2 = "-jar lib/MetroCon.jar";
		String qxArg = "-DDisplayPlatform=R11 -jar lib/MetroCon.jar";
		String mmPath = "C://Program Files//Micro-Manager-1.4//ImageJ.exe";
		String mCon = "C://Program Files//MetroCon-3.1//jre//bin//javaw.exe -jar C://Program Files//MetroCon-3.1//lib//MetroCon.jar -DDisplayPlatform=R4  ";
		
		
		
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CS_allinone window = new CS_allinone();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CS_allinone() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		
		//----------------------------------------------

		// for display

					logs = new JScrollPane();
					logs.setBounds(55, 219, 264, 117);
					
					display = new JTextArea();
					display.setRows(5);
					logs.setViewportView(display);
			
					
					
					
		//----------------------------------------------
		//Main tab pane
					
		JPanel Main = new JPanel();
		Main.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Main", Main);
		tabbedPane.addTab("Main", Main);
		
		main = new JPanel();
		main.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		main.setBounds(10, 11, 301, 192);
		Main.add(main);
		main.setLayout(null);
		
		prop = new JButton("Properties");
		prop.setBounds(210, 147, 81, 23);
		//main.add(prop);
		prop.addActionListener(this);
		
		run = new JButton("RUN");
		run.setBounds(10, 147, 62, 23);
		main.add(run);
		run.addActionListener(this);
		
		

		set = new JButton("SET");
		set.setBounds(84, 147, 72, 23);
		main.add(set);
		set.addActionListener(this);
				
				
				JLabel lblModeOfOperation = new JLabel("Operation Mode :");
				lblModeOfOperation.setBounds(10, 13, 96, 17);
				main.add(lblModeOfOperation);
				
				modeBox = new JComboBox();
				modeBox.setBounds(118, 11, 106, 20);
				main.add(modeBox);
				modeBox.addItem("Wide-field");
				modeBox.addItem("2-beam SIM");
				modeBox.addItem("3-beam SIM");
				modeBox.addItem("Pseudo-widefield");
				
					patternBox = new JComboBox();
					patternBox.setBounds(118, 36, 70, 20);
					main.add(patternBox);
					patternBox.addItem("Pattern 1");
					patternBox.addItem("Pattern 2");
					patternBox.addItem("Pattern 3");
					
					JLabel lblSimPattern = new JLabel("SIM Pattern :");
					lblSimPattern.setBounds(10, 39, 89, 14);
					main.add(lblSimPattern);
					
					JLabel lblExposureTime = new JLabel("Exposure Time :");
					lblExposureTime.setBounds(10, 64, 96, 14);
					main.add(lblExposureTime);
					
					expTime = new JTextField();
					expTime.setBounds(118, 61, 86, 20);
					main.add(expTime);
					expTime.setText("0");
					expTime.setColumns(10);
					
					JLabel lblMs = new JLabel("ms.");
					lblMs.setBounds(207, 64, 30, 14);
					main.add(lblMs);
					
					rmodeBox = new JComboBox();
					rmodeBox.setBounds(118, 86, 79, 20);
					main.add(rmodeBox);
					rmodeBox.addItem("Single");
					rmodeBox.addItem("Continuous");
					rmodeBox.addItem("Timer");
					
					JLabel lblRunMode = new JLabel("Run Mode :");
					lblRunMode.setBounds(10, 89, 89, 14);
					main.add(lblRunMode);
					
					comboBox = new JComboBox();
					comboBox.setBounds(118, 112, 72, 20);
					main.add(comboBox);
					
					lblArduinoPort = new JLabel("Arduino Port :");
					lblArduinoPort.setHorizontalAlignment(SwingConstants.LEFT);
					lblArduinoPort.setBounds(10, 108, 96, 28);
					main.add(lblArduinoPort);
					
					label_1 = new JLabel("Ports: (COM1-9)");
					label_1.setBounds(200, 117, 92, 14);
					main.add(label_1);
		
		//----------------------------------------------
		// --------------------------------------------------------
		//
		// LASER tab
		// --------------

		JPanel patternContent = new JPanel();
		patternContent.setLayout(null);
		
		JLabel label = new JLabel("Power : ");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(0, 1, 62, 34);
		patternContent.add(label);

		power = new JTextField("0 mW");
		power.setBounds(76, 6, 87, 25);
		patternContent.add( power );
		

//		tabbedPane.addTab("Laser", patternContent);
//		
		// --------------------------------------------------------

		// Micro Manager tab
		// --------------

//		JPanel MMpatternContent = new JPanel();
//		MMpatternContent.setLayout(null);
//		
//		JLabel label_5 = new JLabel("Call Micro Manager:  ");
//		label_5.setBounds(0, 1, 109, 45);
//		MMpatternContent.add(label_5);
		
		// -----------------------------------------------------
		// Arduino tab
		// --------------

		JPanel YApatternContent = new JPanel();
		YApatternContent.setLayout(null);
		
		

		tabbedPane.addTab("Arduino", YApatternContent);
		
		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(260, 11, 209, 102);
		YApatternContent.add(panel);
		panel.setLayout(null);
		
		JLabel lblDirectTalk = new JLabel("Direct talk:");
		lblDirectTalk.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDirectTalk.setBounds(10, 11, 90, 28);
		panel.add(lblDirectTalk);
		
		display2 = new JTextField("");
		display2.setBounds(110, 11, 82, 28);
		panel.add(display2);
		testA = new JButton("Send");
		testA.setBounds(110, 50, 82, 38);
		panel.add(testA);
		

		
		testA.addActionListener( this );
		
		preinitialize();
		try{
			for (int i=0;i<comPorts.size(); i++){
				CommPortIdentifier port = comPorts.get(i);
				comboBox.addItem(port.getName());
			}
		}catch(Exception NullPointerException){
			
		}
		
		panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 11, 240, 169);
		YApatternContent.add(panel_2);
		panel_2.setLayout(null);
		connect = new JButton("Connect");
		connect.setBounds(130, 29, 87, 23);
		panel_2.add(connect);
		
		
		JLabel lblConnectToArduino = new JLabel("Connect to Arduino :");
		lblConnectToArduino.setBounds(10, 27, 110, 27);
		panel_2.add(lblConnectToArduino);
		lblConnectToArduino.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		JLabel lblOn = new JLabel("ON :");
		lblOn.setBounds(90, 54, 30, 27);
		panel_2.add(lblOn);
		lblOn.setHorizontalAlignment(SwingConstants.RIGHT);
		

		
		
		ardON = new JButton("d");
		ardON.setBounds(130, 56, 64, 23);
		panel_2.add(ardON);
		ardOFF = new JButton("e");
		ardOFF.setBounds(130, 80, 64, 23);
		panel_2.add(ardOFF);
		JLabel lblOff = new JLabel("OFF :");
		lblOff.setBounds(90, 78, 30, 27);
		panel_2.add(lblOff);
		lblOff.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel lblBlinkTwice = new JLabel("Blink twice :");
		lblBlinkTwice.setBounds(50, 96, 68, 38);
		panel_2.add(lblBlinkTwice);
		lblBlinkTwice.setHorizontalAlignment(SwingConstants.RIGHT);
		blink = new JButton("b");
		blink.setBounds(130, 104, 64, 23);
		panel_2.add(blink);
		quit = new JButton("q");
		quit.setBounds(130, 128, 64, 23);
		panel_2.add(quit);
		JLabel lblQuit = new JLabel("quit :");
		lblQuit.setBounds(90, 126, 30, 27);
		panel_2.add(lblQuit);
		lblQuit.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblQDisconnectnhi = new JLabel("--Test --");
		lblQDisconnectnhi.setBounds(10, 0, 98, 28);
		panel_2.add(lblQDisconnectnhi);
		lblQDisconnectnhi.setToolTipText("");
		lblQDisconnectnhi.setHorizontalAlignment(SwingConstants.LEFT);
		quit.addActionListener( this );
		blink.addActionListener( this );
		ardOFF.addActionListener( this );
		ardON.addActionListener( this );
		connect.addActionListener( this );

		

		//tabbedPane.addTab("MicroM", MMpatternContent);
		//talk.addActionListener( this );
		//display2.addActionListener( this );
		
		

	
		//lowRow.getContentPane().add(logs2);
		//lowRow.add( logs2 );
		
	
		
//* for main.cpp code :**************************************************************			
		// --------------------------------------------------------

		// Settings tab
		// --------------

		JPanel AApatternContent = new JPanel();
		AApatternContent.setLayout(null);
		
		//laser path 
		
		
		tabbedPane.addTab("Settings", AApatternContent);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(10, 11, 488, 145);
		AApatternContent.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblMetroconQxga = new JLabel("MetroCon QXGA:");
		lblMetroconQxga.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMetroconQxga.setBounds(10, 117, 117, 14);
		panel_1.add(lblMetroconQxga);
		
		JLabel lblMetroConSxga = new JLabel("MetroCon SXGA:");
		lblMetroConSxga.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMetroConSxga.setBounds(10, 92, 117, 14);
		panel_1.add(lblMetroConSxga);
		
		JLabel lblMicroManagerSoftware = new JLabel("Micro manager :");
		lblMicroManagerSoftware.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMicroManagerSoftware.setBounds(10, 67, 117, 14);
		panel_1.add(lblMicroManagerSoftware);
		
		JLabel lblSlmSoftware = new JLabel("SLM (RaspPi):");
		lblSlmSoftware.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSlmSoftware.setBounds(10, 42, 117, 14);
		panel_1.add(lblSlmSoftware);
		
		JLabel lblLaserSoftware = new JLabel("Laser:");
		lblLaserSoftware.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLaserSoftware.setBounds(10, 17, 110, 14);
		panel_1.add(lblLaserSoftware);
		
		textField_1 = new JTextField("--default--");
		textField_1.setBounds(137, 11, 191, 20);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField("--default--");
		textField_2.setColumns(10);
		textField_2.setBounds(137, 36, 191, 20);
		panel_1.add(textField_2);
		
		textField_3 = new JTextField("--default--");
		textField_3.setColumns(10);
		textField_3.setBounds(137, 61, 191, 20);
		panel_1.add(textField_3);
		
		textField_4 = new JTextField("--default--");
		textField_4.setColumns(10);
		textField_4.setBounds(137, 86, 191, 20);
		panel_1.add(textField_4);
		
		textField_5 = new JTextField("--default--");
		textField_5.setColumns(10);
		textField_5.setBounds(137, 111, 191, 20);
		panel_1.add(textField_5);
		
	
		
		testL = new JButton("Call");
		testL.setBounds(338, 11, 86, 20);
		panel_1.add(testL);
		
		testR = new JButton("Call");
		testR.setBounds(338, 36, 86, 20);
		panel_1.add(testR);
		
		testMM = new JButton("Call");
		testMM.setBounds(338, 61, 86, 20);
		panel_1.add(testMM);
		
		testSX = new JButton("Call");
		testSX.setBounds(338, 86, 86, 20);
		panel_1.add(testSX);
		
		testQX = new JButton("Call");
		testQX.setBounds(338, 110, 86, 20);
		panel_1.add(testQX);
		

		testR.addActionListener( this );
		testSX.addActionListener( this );
		testQX.addActionListener( this );
		testL.addActionListener( this );
		testMM.addActionListener( this );
		
		
		
		// -----------------------------------------------------
//* for main.cpp code :**************************************************************				
		
		
		
		
		
		frame.setBounds(100, 100, 529, 396);
		frame.getContentPane().add( tabbedPane , BorderLayout.CENTER);
		frame.getContentPane().add( logs , BorderLayout.PAGE_END);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		
		Object who = e.getSource();
		
//		if ( who == test ) {
//			bbp2.net.Send_Arduino ard = new bbp2.net.Send_Arduino();
//			ard.run("control");

		if ( who == set ) {
			
			mode = (String) modeBox.getSelectedItem();
			pattern = (String) patternBox.getSelectedItem();
			rmode = (String) rmodeBox.getSelectedItem();
			exp = expTime.getText();
			ard_port = (String) comboBox.getSelectedItem();
			
			String log = "\n-----\nParameters Set :-\nMode : " + mode+ "\nPattern : "+ pattern+ "\nExposure : "+ exp+ " ms"+"\nRun mode : " + rmode+ "\nArduino Port : " + ard_port+"\n-----";
			
			display.append(log);
			q = true;
			
			//sending the parameters further from here
			
		   
		}

		if ( who == run ) {
			
			if(q==false){
				
				display.append("\nParameters not set, Press SET first.");
				
			}else{
				
				display.append("\nrunning..\n(Nothing for now, try Arduino Direct talk!)"); 
				
				//Running logic here
			}
			
			
		}
		
//Settings tab Call buttons	
		
		if ( who == testL ) {
			try {
				setDisplay("Opening laser software...");
				
				if(textField_1.getText().contentEquals("--default--")){
					new ProcessBuilder(laserPath).start();
				}else{
					new ProcessBuilder(textField_1.getText()).start();
				}
				 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("cannot open");
				//e1.printStackTrace();
				setDisplay("Cannot reach software, check the path!");
				System.out.println("Cannot reach software, check the path!");
			}
		}	
		
		
		if ( who == testR ) {
			
			
			try {
				setDisplay("Opening Raspberry software...");
				
				if(textField_2.getText().contentEquals("--default--")){
					
					raspberry.net.Send_Raspberry ard = new raspberry.net.Send_Raspberry();
					ard.run("control");
				
				}else{
					new ProcessBuilder(textField_2.getText()).start();
				}
				 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("cannot open");
				//e1.printStackTrace();
				setDisplay("Cannot reach software, check the path!");
				System.out.println("Cannot reach software, check the path!");
			}
			
		   
		}
		
		
		
		if ( who == testMM ) {
			try {
				
				setDisplay("Opening Micro Manager...");
				
				if(textField_3.getText().contentEquals("--default--")){
					
					new ProcessBuilder(mmPath).start();
				
				}else{
					new ProcessBuilder(textField_3.getText()).start();
				}
				
				 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("cannot open");
				//e1.printStackTrace();
				setDisplay("Cannot reach software, check the path!");
				System.out.println("Cannot reach software, check the path!");
			}
		}
		
		
		
		
		if ( who == testSX ) {
			try {
				
				setDisplay("Opening MicroCon(SXGA)...");
				
				if(textField_4.getText().contentEquals("--default--")){
					
					slm.slmMain slm = new slm.slmMain();
					slm.run("control");
					setDisplay("here");
				
				}else{
					new ProcessBuilder(textField_4.getText()).start();
				}
				
				
			
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				setDisplay("Cannot reach software, check the path!");
				System.out.println("Cannot reach software, check the path!");
				e1.printStackTrace();
			}
			
		   
		}
		
		
		if ( who == testQX ) {
			try {
				setDisplay("Opening MicroCon(QXGA)...");
				setDisplay("Nothing for now...");
				if(textField_5.getText().contentEquals("--default--")){
					
					//try calling QXGA software here
					//new ProcessBuilder(mConPath,qxArg).start();
				}else{
					new ProcessBuilder(textField_5.getText()).start();
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				setDisplay("Cannot reach software, check the path!");
				System.out.println("Cannot reach software, check the path!");
			}
			
		   
		}

		
		
//Arduino tab buttons		
		
		
		if ( who == connect ) {
			
			
			try {
				
				Arduino();
				
				if(config==0){
					
					setDisplay("Arduino not found, please recheck connections.");
				
				} else {
				
					setDisplay("--Connection Started--");
					
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
				
		}
		
		
		if ( who == testA ) {
			
			
			try {
				
				if (config==1){
					
					if (display2.getText().isEmpty()){
//						String word = getDisplay();
//						setDisplay("Sending " + word + " to Arduino...");
//						System.out.println("Sending " + word + " to Arduino...");
//						Arduino(word);
						setDisplay("Type a char in the box to send...");
						
					}else {
						String word = display2.getText();
						setDisplay("Sending '" + word + "' to Arduino...");
						System.out.println("Sending '" + word + "' to Arduino...");
						Arduino(word);
						
					}
					
				} else {
					
					setDisplay("Arduino not connected, press 'Connect' first.");
					System.out.println("Arduino not connected, press 'Connect' first.");
					
				} 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
				
		}
		
				if ( who == ardON ){
											
					
					display2.setText("d");
					System.out.println(display2);
						
					
					
				}
				
				if ( who == ardOFF ){
					
				
					display2.setText("e");
					System.out.println(display2);
					
				}
				
				if ( who == blink ){
					
					display2.setText("b");
					System.out.println(display2);
					
				}
				
				if ( who == quit ){
					
					display2.setText("q");
					System.out.println(display2);
					


				}
				
				
			
	
			
		}

	
	
	Arduino_Connect1 ard;
	private JPanel panel_2;
	private static JComboBox<String> comboBox;
	private JLabel lblArduinoPort;
	private JLabel label_1;
	
	public void Arduino() throws Exception{
		
		ard = new Arduino_Connect1();
	}

	public void Arduino(String data) throws Exception{
		
		config = ard.Arduino_Send(data);
		
		if (config==0){
			setDisplay("--Connections Closed--");
		}
		
	}

	public static String getPort() throws Exception{
		
		
		return  ard_port;
		
	}


	@SuppressWarnings({ "rawtypes"})
	public void preinitialize() {
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
       // System.setProperty("gnu.io.rxtx.SerialPorts", "COM3");
        System.out.println("here");
	
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	System.out.println(portEnum);
	//First, Find an instance of serial port as set in PORT_NAMES.
	while (portEnum.hasMoreElements()) {
		CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
		for (String portName : PORT_NAMES) {
			System.out.println(currPortId.getName());
			if (currPortId.getName().equals(portName)) {
				comboBox.addItem(portName);
				//comPorts.add(portId);
				break;
			}
			
		}
	}
	}

	public static void setDisplay(String inputLine) {
		display.setText(inputLine);
		IJ.log(inputLine);
		// TODO Auto-generated method stub
	
	}	

	public static String getDisplay() {
	
	return display.getText();
	
	// TODO Auto-generated method stub
	
	}
}


