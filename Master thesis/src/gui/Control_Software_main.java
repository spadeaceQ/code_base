

package gui;

import arduino.*;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

import arduino.mainclass;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;


public class Control_Software_main implements ActionListener {

	JFrame frame = new JFrame("Control software");
	 JTextField expTime;
	 @SuppressWarnings("rawtypes")
	 JComboBox modeBox, patternBox, rmodeBox;
	 JButton set,run,prop;
	 String mode, pattern, exp, rmode;
	 JScrollPane logs;
	public  JTextArea display;
	 boolean q = false;
	 
	/**
	 * Launch the application.
	 */
	 
	
	 
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Control_Software_main window = new Control_Software_main();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public Control_Software_main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		// initialize the GUI
					{
						// tabs
						JTabbedPane tabbedPane = new JTabbedPane();
//				
//		frame = new JFrame("Control Software");
//		frame.setBounds(100, 100, 517, 536);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(null);
						
		JPanel Main = new JPanel();
		Main.setBounds(500, 500, 430, 183);
		//Main.setLayout(new BoxLayout(Main, BoxLayout.X_AXIS));
//		frame.setBounds(100, 100, 517, 536);
//		frame.getContentPane().add(Main);
		//Main.setLayout(null);
		
		set = new JButton("SET");
		Main.add(set);
		set.addActionListener(this);
		
		
		run = new JButton("RUN");
		Main.add(run);
		
		prop = new JButton("Properties");
		Main.add(prop);
		
				
				
				JLabel lblModeOfOperation = new JLabel("Operation Mode :");
				Main.add(lblModeOfOperation);
				
				JLabel lblSimPattern = new JLabel("SIM Pattern :");
				Main.add(lblSimPattern);
				
					patternBox = new JComboBox();
					Main.add(patternBox);
					patternBox.addItem("Pattern 1");
					patternBox.addItem("Pattern 2");
					patternBox.addItem("Pattern 3");
					
					JLabel lblExposureTime = new JLabel("Exposure Time :");
					Main.add(lblExposureTime);
					
					expTime = new JTextField();
					Main.add(expTime);
					expTime.setText("0");
					expTime.setColumns(10);
					
					JLabel lblMs = new JLabel("ms.");
					Main.add(lblMs);
					
					modeBox = new JComboBox();
					Main.add(modeBox);
					modeBox.addItem("Wide-field");
					modeBox.addItem("2-beam SIM");
					modeBox.addItem("3-beam SIM");
					modeBox.addItem("Pseudo-widefield");
					
					rmodeBox = new JComboBox();
					Main.add(rmodeBox);
					rmodeBox.addItem("Single");
					rmodeBox.addItem("Continuous");
					rmodeBox.addItem("Timer");
					
					JLabel lblRunMode = new JLabel("Run Mode :");
					Main.add(lblRunMode);
					
					logs = new JScrollPane();
					logs.setBounds(55, 219, 264, 117);
					frame.getContentPane().add(logs);
					
					
					display = new JTextArea();
					logs.setViewportView(display);
		prop.addActionListener(this);
		run.addActionListener(this);
		
		
		mainclass ard =new mainclass();
		
		tabbedPane.addTab("Main", Main);
		
		ImageIcon img = new ImageIcon("x.png");
		// create the main window
		frame.setBounds(100, 100, 547, 504);
		
		frame.setIconImage(img.getImage());
		frame.getContentPane().add( tabbedPane , BorderLayout.CENTER);
		
		
		frame.getContentPane().add( logs , BorderLayout.PAGE_END);
		frame.pack();
		frame.setVisible(true);	
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
					}	
		
		
        		

		//System.out.println("Parameters Set-\nMode :" + mode+ "\nPattern : "+ raspberry.pattern+ "\nExposure : "+ exp+ "\nRun mode : " + rmode+ "\n");
		
		
	}

	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		 
		
		if ( obj == set ) {
			
			mode = (String) modeBox.getSelectedItem();
			pattern = (String) patternBox.getSelectedItem();
			rmode = (String) rmodeBox.getSelectedItem();
			exp = expTime.getText();
			
			String log = "\n-----\nParameters Set :-\nMode : " + mode+ "\nPattern : "+ pattern+ "\nExposure : "+ exp+ " ms"+"\nRun mode : " + rmode+ "\n-----";
			
			display.append(log);
			q = true;
			
			//sending the parameters further from here
			
		   
		}

		if ( obj == run ) {
			
			if(q==false){
				
				display.append("\nParameters not set, Press SET first.");
				
			}else{
				
				display.append("\nrunning..\n(Nothing for now)"); 
				
				//Running logic here
			}
			
			
		}
		
		if ( obj == prop ) {
			
			if(q==false){
				
				display.append("\nParameters not set, Press SET first.");
				
			}else{
				
				
				arduino.mainclass cl = new mainclass();
			    cl.run("");
				//display.append("\nrunning..\n(Nothing for now)"); 
				
				//Running logic here
			}
			
			
		}
		
	}
}
