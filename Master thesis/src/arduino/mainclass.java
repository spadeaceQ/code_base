package arduino;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import arduino.Arduino_Connect1;
import raspberry.net.*;
import raspberry.pattern.*;
import ij.ImagePlus;
import ij.Prefs;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.*;
import slm.Main_View;
import slm.slmMain;
import slm.*;
import com.forthdd.*;
import com.forthdd.metrocon.*;

import mmcorej.*;

@SuppressWarnings("unused")
public class mainclass implements PlugIn{

	public static int config = 0;
	
	public static JTextArea display2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		mainclass cl = new mainclass();
	    cl.run("");
		
	    
	}
	


	@Override
	public void run(String arg0) {
		// TODO Auto-generated method stub
		
			new Thread(new ControlPanel()).start();
	}
	
	
	// small subclass to fork a control panel for UDP
		class ControlPanel implements Runnable, ActionListener {

			
			JButton testL,testR,testSX,testQX,testMM, testA, ardON, ardOFF, blink, quit, connect;
			JTextField power,talk;
			 JScrollPane logs2;
			//public JTextArea display2;

	
			// initialize the GUI
			{
				// tabs
				JTabbedPane tabbedPane = new JTabbedPane();

				// --------------------------------------------------------
				//
				// LASER tab
				// --------------

				JPanel patternContent = new JPanel(new GridLayout(5,2));


				
				testL = new JButton("Call");
				
				patternContent.add(new JLabel("Power : "));
	
				power = new JTextField("0 mW");
				patternContent.add( power );
				patternContent.add(new JLabel("Call Laser S/W:  "));
				patternContent.add( testL );

				testL.addActionListener( this );
				

				tabbedPane.addTab("Laser", patternContent);

				// --------------------------------------------------------

				// SLM tab
				// --------------

				JPanel SIMpatternContent = new JPanel(new GridLayout(5,2));


				
				testR = new JButton("Call");
				testSX = new JButton("Call");
				testQX = new JButton("Call");
				
				SIMpatternContent.add(new JLabel("SLM SIM(RaspPi):  "));

				SIMpatternContent.add( testR );
				
				
				SIMpatternContent.add(new JLabel("MicroCon(SXGA): "));
				SIMpatternContent.add( testSX );
				SIMpatternContent.add(new JLabel("MicroCon(QXGA): "));
				SIMpatternContent.add( testQX );

				testR.addActionListener( this );
				testSX.addActionListener( this );
				testQX.addActionListener( this );
				

				tabbedPane.addTab("SLM", SIMpatternContent);
				
				// --------------------------------------------------------

				// Micro Manager tab
				// --------------

				JPanel MMpatternContent = new JPanel(new GridLayout(5,2));


				
				testMM = new JButton("Call");
				
				MMpatternContent.add(new JLabel("Call Micro Manager:  "));

				MMpatternContent.add( testMM );
				
				testMM.addActionListener( this );

				

				tabbedPane.addTab("MicroM", MMpatternContent);
				
				// -----------------------------------------------------
				// Arduino tab
				// --------------

				JPanel YApatternContent = new JPanel(new GridLayout(6,2));
				JPanel lowRow     = new JPanel(new GridLayout(1,1));

				
				
				ardON = new JButton("+");
				ardOFF = new JButton("-");
				blink = new JButton("a");
				quit = new JButton("q");
				testA = new JButton("Send");
				connect = new JButton("Connect");
				display2 = new JTextArea(3,1);
				
				
				YApatternContent.add(new JLabel("Connect to Arduino:"));
				YApatternContent.add( connect );
				
				
				YApatternContent.add(new JLabel("ON"));
				YApatternContent.add( ardON );
				YApatternContent.add(new JLabel("off"));
				YApatternContent.add( ardOFF );
				YApatternContent.add(new JLabel("Blink"));
				YApatternContent.add( blink );
				YApatternContent.add(new JLabel("quit"));
				YApatternContent.add( quit );
				YApatternContent.add( display2 );
				YApatternContent.add(new JLabel("Send "));
				YApatternContent.add( testA );
				

				
				testA.addActionListener( this );
				ardON.addActionListener( this );
				ardOFF.addActionListener( this );
				blink.addActionListener( this );
				quit.addActionListener( this );
				connect.addActionListener( this );
				//talk.addActionListener( this );
				//display2.addActionListener( this );
				
				

				logs2 = new JScrollPane();
				logs2.setBounds(10, 168, 264, 117);
				//lowRow.getContentPane().add(logs2);
				lowRow.add( logs2 );
				
				//display2 = new JTextArea();
				logs2.setViewportView(display2);
				

				tabbedPane.addTab("Y_Arduino", YApatternContent);
				
	//* for main.cpp code :**************************************************************			
				// --------------------------------------------------------

				// Micro Manager tab
				// --------------

				JPanel AApatternContent = new JPanel(new GridLayout(5,2));
				AApatternContent.add(new JLabel("Direct Arduino talk:"));
				talk = new JTextField("");
				AApatternContent.add( talk );

				
				testA = new JButton("Send");
				connect = new JButton("Connect");
				testA.setAlignmentX(
					      Component.CENTER_ALIGNMENT);
				
				AApatternContent.add( connect );
				AApatternContent.add(new JLabel("q : disconnect"));
				AApatternContent.add( testA );
				
				
				testA.addActionListener( this );
				connect.addActionListener( this );
				

				tabbedPane.addTab("A_Ard", AApatternContent);
				
				// -----------------------------------------------------
	//* for main.cpp code :**************************************************************				
				
				
				
				
				ImageIcon img = new ImageIcon("x.png");
				// create the main window
				JFrame frame = new JFrame("SIMiCS");
				frame.setIconImage(img.getImage());
				frame.getContentPane().add( tabbedPane , BorderLayout.CENTER);
				frame.getContentPane().add( lowRow , BorderLayout.PAGE_END);
				frame.pack();
				frame.setVisible(true);	
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			}

			
			
			
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
	

				
				String laserPath = "C://Program Files (x86)//Qioptiq//NanoControl.exe";
				String mConPath = "C://Program Files//MetroCon-3.1//jre//bin//javaw.exe";
				String sxArg1 = "-DDisplayPlatform=R4";
				String sxArg2 = "-jar lib/MetroCon.jar";
				String qxArg = "-DDisplayPlatform=R11 -jar lib/MetroCon.jar";
				String mmPath = "C://Program Files//Micro-Manager-1.4//ImageJ.exe";
				String mCon = "C://Program Files//MetroCon-3.1//jre//bin//javaw.exe -jar C://Program Files//MetroCon-3.1//lib//MetroCon.jar -DDisplayPlatform=R4  ";
				
				Object who = e.getSource();
				
//				if ( who == test ) {
//					bbp2.net.Send_Arduino ard = new bbp2.net.Send_Arduino();
//					ard.run("control");
					
					
				if ( who == testR ) {
					setDisplay("Opening Raspberry software...");
					raspberry.net.Send_Raspberry ard = new raspberry.net.Send_Raspberry();
					ard.run("control");
				   
				}
				
				if ( who == testSX ) {
					try {
						
						setDisplay("Opening MicroCon(SXGA)...");
						
						
						//com.forthdd.replib.Config.DisplayPlatform.R4 ;
						
						slm.slmMain slm = new slm.slmMain();
						slm.run("control");
						setDisplay("here");
						
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
						new ProcessBuilder(mConPath,qxArg).start();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						setDisplay("Cannot reach software, check the path!");
						System.out.println("Cannot reach software, check the path!");
					}
					
				   
				}
				
				if ( who == testMM ) {
					try {
						setDisplay("Opening Micro Manager...");
						 new ProcessBuilder(mmPath).start();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("cannot open");
						//e1.printStackTrace();
						setDisplay("Cannot reach software, check the path!");
						System.out.println("Cannot reach software, check the path!");
					}
				}	
				
				if ( who == testL ) {
					try {
						setDisplay("Opening laser software...");
						 new ProcessBuilder(laserPath).start();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("cannot open");
						//e1.printStackTrace();
						setDisplay("Cannot reach software, check the path!");
						System.out.println("Cannot reach software, check the path!");
					}
				}		  
				
				
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
							
							if (talk.getText()==""){
								String word = getDisplay();
								setDisplay("Sending " + word + " to Arduino...");
								System.out.println("Sending " + word + " to Arduino...");
								Arduino(word);
							}else {
								String word = talk.getText();
								setDisplay("Sending " + word + " to Arduino...");
								System.out.println("Sending " + word + " to Arduino...");
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
													
							setDisplay("+");
							System.out.println(getDisplay());
								
							
							
						}
						
						if ( who == ardOFF ){
							
							setDisplay("-");
							System.out.println(getDisplay());
							
						}
						
						if ( who == blink ){
							
							setDisplay("a");
							System.out.println(getDisplay());
							
						}
						
						if ( who == quit ){
							
							setDisplay("q");
							System.out.println(getDisplay());
							


						}
						
						
					
			
					
				}

			Arduino_Connect1 ard;
			
			public void Arduino() throws Exception{
				
				 ard = new Arduino_Connect1();
			}

			public void Arduino(String data) throws Exception{
				
				config = ard.Arduino_Send(data);
				
				if (config==0){
					setDisplay("--Connections Closed--");
				}
				
			}

			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
				
				
		

		
			
		}


		public static void setDisplay(String inputLine) {
			display2.setText(inputLine);
			IJ.log(inputLine);
			// TODO Auto-generated method stub
			
		}	
		
		public static String getDisplay() {
			
			return display2.getText();
			
			// TODO Auto-generated method stub
			
		}
}
