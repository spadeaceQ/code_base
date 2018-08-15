package UI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GeneralConfig  extends JFrame implements ActionListener {

	JLabel config;
	JLabel program;
	JLabel spec;
	JLabel entry;
	JLabel output;
	JLabel cpascript;
	JLabel origverilog;
	JLabel circuitname;
	
	JButton button;
	JButton buttonspec;
	JButton buttonprog;
	JButton buttondeffile;
	JButton buttonoutput;
	JButton buttoncpascript;
	JButton buttonverilogfiles;
	
	JButton[] btnarray;
	
	JTextField textentry;
	JTextField textspec;
	JTextField textprog;
	JTextField textdeffile;
	JTextField textoutput;
	JTextField textcpascript;
	JTextField textverilogpath;
	JTextField textcircuit;
	
	JComboBox<String> configbox;
	UiMain main;

	JPanel[] pane3;
	
	String filepath;
	String absolutepath;
	
	JPanel thispanel;
	
	public GeneralConfig() {
		// TODO Auto-generated constructor stub
	}
	public void setGeneralConfigPane(JPanel pane)
	{
		buttonspec = new JButton("Open File");
		buttonspec.addActionListener(this);
		buttonprog = new JButton("Open File");
		buttonprog.addActionListener(this);
		buttondeffile = new JButton("Open File");
		buttondeffile.addActionListener(this);
		buttonoutput = new JButton("Open File");
		buttonoutput.addActionListener(this);
		buttoncpascript = new JButton("Provide path of cpa.sh");
		buttoncpascript.addActionListener(this);
		buttonverilogfiles = new JButton("Provide verilog files");
		buttonverilogfiles.addActionListener(this);

		textspec = new JTextField();
		textprog = new JTextField();
		textdeffile  = new JTextField();
		textoutput  = new JTextField();
		textcpascript = new JTextField();
		textverilogpath = new JTextField();
		textcircuit = new JTextField();

		pane3 = new JPanel[6];
		
		pane3[0] =new JPanel();
		pane3[0].setLayout(new BoxLayout(pane3[0], BoxLayout.X_AXIS));
		pane3[0].add(textcpascript);
        pane3[0].add(buttoncpascript);
		
	
		pane3[1] = new JPanel();
		pane3[1].setLayout(new BoxLayout(pane3[1], BoxLayout.X_AXIS));
		pane3[1].add(textprog);
		pane3[1].add(buttonprog);
		
		pane3[2] = new JPanel();
		pane3[2].setLayout(new BoxLayout(pane3[2], BoxLayout.X_AXIS));
		pane3[2].add(textspec);
		pane3[2].add(buttonspec);
		
		pane3[3] = new JPanel();
		pane3[3].setLayout(new BoxLayout(pane3[3], BoxLayout.X_AXIS));
		pane3[3].add(textoutput);
		pane3[3].add(buttonoutput);
		
		pane3[4] = new JPanel();
		pane3[4].setLayout(new BoxLayout(pane3[4], BoxLayout.X_AXIS));
		pane3[4].add(textverilogpath);
		pane3[4].add(buttonverilogfiles);
		
		//!<configuration label
		config=new JLabel("Configuration"); 
		String[] configvalues = new String[]{"signAnalysis", "predicateAnalysis"};
		configbox = new JComboBox<>(configvalues);
		configbox.addActionListener(this);
		
		//! < give program path
		cpascript = new JLabel("Path of CPAChecker");
		//!<program file layout
		program = new JLabel("Program File");

		//!< specification file
		spec = new JLabel("Specification File");

		//!< entry function
		entry = new JLabel("Entry Funtion");
		textentry  = new JTextField();

		//!< output path 
		output = new JLabel("Output Path");
		
		circuitname = new JLabel("Provide circuit name");
		origverilog = new JLabel("Path of Verilog Files");
		
		this.addGeneralConfig(pane);
	}
	public void addGeneralConfig(JPanel pane)
	{
		pane.add(cpascript);
		pane3[0].add(textcpascript);
		pane3[0].add(buttoncpascript);
		pane.add(pane3[0]);
		
		pane.add(config); 
		pane.add(configbox);
		
		pane.add(program);
		pane3[1].add(textprog);
		pane3[1].add(buttonprog);
		pane.add(pane3[1]);
		
		pane.add(spec);
		pane3[2].add(textspec);
		pane3[2].add(buttonspec);
		pane.add(pane3[2]);
		
		pane.add(output);
		pane3[3].add(textoutput);
		pane3[3].add(buttonoutput);
		pane.add(pane3[3]);
		
		pane.add(circuitname);
		pane.add(textcircuit);
		
		pane.add(origverilog);
		pane3[4].add(textverilogpath);
		pane3[4].add(buttonverilogfiles);
		pane.add(pane3[4]);
		
		pane.add(entry);
		pane.add(textentry);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//UiMain main = new UiMain();
		if(e.getSource()== configbox && configbox.getItemAt(configbox.getSelectedIndex()) == "predicateAnalysis")
		{
			try {
				
			} catch (Exception NullPointerException) {
			}main.getSignFlowPanel().setVisible(false);
			 main.getPredicateFlowPanel().setVisible(true);
		}
		else if(e.getSource()== configbox && configbox.getItemAt(configbox.getSelectedIndex()) == "signAnalysis")
		{
			//main.getSignFlowPanel().setVisible(true);
			main.getSignFlowPanel().setVisible(true);
			main.getPredicateFlowPanel().setVisible(false);
		}
		else
		{
			if(e.getSource() == buttonspec)
			{
				main.openFile(textspec, "spc", "Spec files", "FILES_ONLY");	
			}
			else if(e.getSource() == buttonprog)
			{
				main.openFile(textprog, "c", "c Files", "FILES_ONLY");
			}
			else if(e.getSource() == buttonoutput)
			{
				main.openFile(textoutput, "", "",  "DIRECTORIES_ONLY");
			}
			else if (e.getSource() == buttoncpascript)
			{
				main.openFile(textcpascript, "sh", "shell script", "FILES_ONLY");
			}
			else if(e.getSource() == buttonverilogfiles)
			{
				main.openFile(textverilogpath, "v", "verilog files", "FILES_ONLY");
			}
		}
	}
	public void addActionEvent()
	{
		//configbox.addActionListener(this);
	}
	
	//!getters
	public JComboBox<String> getConfigBox()
	{
		return configbox;
	}
	public JTextField getOutputPath()
	{
		return textoutput;
	}
	public JTextField getProgramPath()
	{
		return textprog;
	}
	public JTextField getSpecPath()
	{
		return textspec;
	}
	
	public JTextField getCpaScriptPath()
	{
		return textcpascript;
	}
	public JTextField getProgOutput()
	{
		return textoutput;
	}
	public void setUiMain(UiMain ui) {
        main = ui;		
	}
	
	public JTextField getVerilogPath()
	{
		return textverilogpath;
	}
	
	public JTextField getCircuitName()
	{
		return textcircuit;
	}
}
