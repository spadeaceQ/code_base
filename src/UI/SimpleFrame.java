package UI;
/**
 * 
 */

/**
 * @author tasneem
 *
 */
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cpacheckerAnalysis.GenerateCmdOpt;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
public class SimpleFrame extends JFrame implements ActionListener{

	JButton button;
	JButton buttonspec;
	JButton buttonprog;
	JButton buttondeffile;
	JButton buttonoutput;
	JButton[] btnarray;
	JTextField textspec;
	JTextField textprog;
	JTextField textdeffile;
	JTextField textoutput;
	JTextField abstracttext;
	JPanel[] pane3;
	String filepath;
	String absolutepath;
	String dir;
	JTextField[] text;
	JComboBox<String> configbox;
	JComboBox<String> mergeprop;
	JComboBox<String> cienable;
	JTextArea errortext;
	JButton generatebutton;
	JPanel pane;
	JPanel flowpanelgeneral;
	JPanel pane2;
	JPanel cipane;
	JPanel flowpanel;
	JPanel flowpanel2;
	JPanel errorpane;
	boolean foundFolder = false;

	public void start() 
	{ 
		//! panel for general configuration
		pane = new JPanel();
		GridLayout grid = new GridLayout(5, 2, 0, 0);
		grid.setHgap(1);
		grid.setVgap(1);
		pane.setLayout(grid);

		//! panel for holding general config panel
		flowpanelgeneral = new JPanel();
		flowpanelgeneral.setLayout(new FlowLayout(FlowLayout.LEFT));

		//! panel for sign analysis
		pane2 = new JPanel();
		pane2.setLayout(new GridLayout(1,2,0,0)) ;
		//pane2.setLayout(new FlowLayout(FlowLayout.LEFT));

		cipane = new JPanel();
		GridLayout gridci = new GridLayout(3, 2, 0, 0);
		grid.setHgap(1);
		grid.setVgap(1);
		cipane.setLayout(gridci);

		errorpane = new JPanel();
		GridLayout griderror = new GridLayout(2, 1,0,0);
		//BoxLayout griderror = new BoxLayout(errorpane, BoxLayout.Y_AXIS);
		errorpane.setLayout(griderror);

		//! panel for holding sign analysis pane
		flowpanel = new JPanel();
		flowpanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		//! panel for holding ci extraction panel
		flowpanel2 = new JPanel();
		flowpanel2.setLayout(new FlowLayout(FlowLayout.LEFT));

		buttonspec = new JButton("Open File");
		buttonspec.addActionListener(this);
		buttonprog = new JButton("Open File");
		buttonprog.addActionListener(this);
		buttondeffile = new JButton("Open File");
		buttondeffile.addActionListener(this);
		buttonoutput = new JButton("Open File");
		buttonoutput.addActionListener(this);

		textspec = new JTextField();
		textprog = new JTextField();
		textdeffile  = new JTextField();
		textoutput  = new JTextField();

		//! button array for dialog box not used
		btnarray = new JButton[6];
		for(int i =0; i< btnarray.length; i++)
		{
			btnarray[i] = new JButton("open File");
			btnarray[i].setPreferredSize(new Dimension(20, 15));
			btnarray[i].addActionListener(this);
		}

		//! not used
		text = new JTextField[6];
		for(int i =0; i<text.length; i++)
		{
			text[i] = new JTextField();
			text[i].setSize(10, 10);
		}

		//!panel for holding file dialog
		pane3 = new JPanel[6];
		/*for(int i =0; i < pane3.length; i++)
		{
			pane3[i] = new JPanel();
			pane3[i].setLayout(new BoxLayout(pane3[i], BoxLayout.X_AXIS));
			pane3[i].add(text[i]);
			pane3[i].add(btnarray[i]);
		}*/
		pane3[0] = new JPanel();
		pane3[0].setLayout(new BoxLayout(pane3[0], BoxLayout.X_AXIS));
		pane3[0].add(textspec);
		pane3[0].add(buttonspec);

		pane3[1] = new JPanel();
		pane3[1].setLayout(new BoxLayout(pane3[1], BoxLayout.X_AXIS));
		pane3[1].add(textprog);
		pane3[1].add(buttonprog);

		pane3[2] = new JPanel();
		pane3[2].setLayout(new BoxLayout(pane3[2], BoxLayout.X_AXIS));
		pane3[2].add(textoutput);
		pane3[2].add(buttonoutput);

		pane3[3] = new JPanel();
		pane3[3].setLayout(new BoxLayout(pane3[3], BoxLayout.X_AXIS));
		pane3[3].add(textdeffile);
		pane3[3].add(buttondeffile);

		//!<configuration label
		JLabel config=new JLabel("Configuration"); 
		String[] configvalues = new String[]{"signAnalysis", "predicateAnalysis"};
		configbox = new JComboBox<>(configvalues);
		//!<program file layout
		JLabel program = new JLabel("Program File");

		//!< specification file
		JLabel spec = new JLabel("Specification File");

		//!< entry function
		JLabel entry = new JLabel("Entry Funtion");
		JTextField entrytext  = new JTextField();

		//!< output path 
		JLabel output = new JLabel("Output Path");

		JLabel merge = new JLabel("Merge Property");
		String[] property = new String[]{"JOIN", "SEPERATE"};
		mergeprop  = new JComboBox<>(property);

		JLabel ciextract = new JLabel("Enable Custom Instruction");
		String[] cibool = new String[]{"true","false"};
		cienable = new JComboBox<>(cibool);

		JLabel abstractname = new JLabel("Abstract Class Name");
		abstracttext = new JTextField();

		JLabel deffile = new JLabel("Definition File");

		errortext = new JTextArea();
		errortext.setEnabled(false);

		generatebutton = new JButton("Start Verification");
		generatebutton.addActionListener(this);

		//!<creating list of JLabel to create empty space in between not used
		ArrayList<JLabel> label = new ArrayList<JLabel>();
		label.add(config);
		label.add(program);
		label.add(spec);
		label.add(entry);
		label.add(output);
		label.add(merge);
		label.add(abstractname);
		label.add(ciextract);
		label.add(deffile);

		/*for (int i=0; i < label.size(); i++)
		{
			label.get(i).setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 15));
		}*/
		//mergeprop.setEnabled(false);
		TitledBorder p1Tborder= new TitledBorder("General Config");
		TitledBorder p2Tborder= new TitledBorder("Sign Analysis");
		TitledBorder p3Tborder= new TitledBorder("CI Extraction");
		TitledBorder p4Tborder = new TitledBorder("Errors");
		pane.add(config); 
		pane.add(configbox);
		/*pane3[0].add(text[0]);
		pane3[0].add(btnarray[0]);
		pane.add(pane3[0]);*/
		pane.add(program);
		pane3[0].add(textprog);
		pane3[0].add(buttonprog);
		pane.add(pane3[0]);
		pane.add(spec);
		pane3[1].add(textspec);
		pane3[1].add(buttonspec);
		pane.add(pane3[1]);
		pane.add(output);
		pane3[2].add(textoutput);
		pane3[2].add(buttonoutput);
		pane.add(pane3[2]);
		pane.add(entry);
		pane.add(entrytext);

		pane2.add(merge);
		pane2.add(mergeprop);

		cipane.add(ciextract);
		cipane.add(cienable);
		cipane.add(abstractname);
		cipane.add(abstracttext);
		cipane.add(deffile);
		pane3[3].add(textdeffile);
		pane3[3].add(buttondeffile);
		cipane.add(pane3[3]);

		errorpane.add(errortext);
		errorpane.add(new JScrollPane(errortext));
		errorpane.setSize(5, 5);

		pane.setBorder(p1Tborder);
		pane2.setBorder(p2Tborder);
		cipane.setBorder(p3Tborder);
		errorpane.setBorder(p4Tborder);

		flowpanelgeneral.add(pane);
		flowpanel.add(pane2);
		flowpanel2.add(cipane);

		setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS )); 
		add(pane);
		add(flowpanel);
		add(cipane);
		add(errorpane);
		add(generatebutton);
		if(cienable.getItemAt(cienable.getSelectedIndex()) == "false")
		{
			cipane.setEnabled(false);
		}
		configbox.addActionListener(this);
		cienable.addActionListener(this);
		pack();
		this.setSize(500,500); 
		setVisible(true); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public static void main(String[] args) 
	{

		new SimpleFrame().start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== configbox && configbox.getItemAt(configbox.getSelectedIndex()) != "signAnalysis")
		{
			flowpanel.setVisible(false);
		}
		else if(e.getSource()== configbox && configbox.getItemAt(configbox.getSelectedIndex()) == "signAnalysis")
		{
			flowpanel.setVisible(true);
		}

		else if(e.getSource() == cienable && cienable.getItemAt(cienable.getSelectedIndex()) == "false")
		{
			for(int i =0 ; i< cipane.getComponentCount(); i++)
			{
				cipane.getComponent(i).setEnabled(false);
				if(cipane.getComponent(i).equals(cienable))
				{
					cipane.getComponent(i).setEnabled(true);
				}
				if(cipane.getComponent(i).equals(pane3[3]))
				{
					pane3[3].getComponent(0).setEnabled(false);
					pane3[3].getComponent(1).setEnabled(false);
				}
			}		
		}
		else if(e.getSource() == cienable && cienable.getItemAt(cienable.getSelectedIndex()) == "true")
		{
			for(int i =0 ; i< cipane.getComponentCount(); i++)
			{
				cipane.getComponent(i).setEnabled(true);
				if(cipane.getComponent(i).equals(cienable))
				{
					cipane.getComponent(i).setEnabled(true);
				}
				if(cipane.getComponent(i).equals(pane3[3]))
				{
					pane3[3].getComponent(0).setEnabled(true);
					pane3[3].getComponent(1).setEnabled(true);
				}				
			}
		}
		else
		{
			if(e.getSource() == buttonspec)
			{
				openFile(textspec, "spc", "Spec files", "FILES_ONLY");	
			}
			else if(e.getSource() == buttonprog)
			{
				openFile(textprog, "c", "c Files", "FILES_ONLY");
			}
			else if(e.getSource() == buttondeffile)
			{
				openFile(textdeffile, "txt", "text files", "FILES_ONLY");
			}
			else if(e.getSource() == buttonoutput)
			{
				openFile(textoutput, "", "",  "DIRECTORIES_ONLY");
			}
			else if(e.getSource() == generatebutton)
			{
				ArrayList<String> cmdlist = new ArrayList<String>();
				String setprop = "-setprop ";
				
				String config = "-" + configbox.getItemAt(configbox.getSelectedIndex());
				String merge = mergeprop.getItemAt(mergeprop.getSelectedIndex()); 
				if(merge.contains("SEPERATE"))
				{
					merge = setprop + "cpa.sign.merge=SEP";
				}
				else
				{
					merge = setprop + "cpa.sign.merge=JOIN";
				}
					
				String outputpath = setprop + "output.path="+textoutput.getText();
				String prog = textprog.getText();
				String spec = "-spec " + textspec.getText();
				String ci_enable = setprop + "analysis.extractRequirements.customInstruction="+cienable.getItemAt(cienable.getSelectedIndex());
				
				String ci_deffile = setprop + "custominstructions.definitionFile="+textdeffile.getText();
				String ci_abstractfile = setprop + "custominstructions.requirementsStateClassName="+abstracttext.getText();
				cmdlist.add(config);
				cmdlist.add(merge);
				cmdlist.add(spec);
				cmdlist.add(ci_abstractfile);
				cmdlist.add(ci_deffile);
				cmdlist.add(ci_enable);
				cmdlist.add(prog);
				cmdlist.add(outputpath);
			    GenerateCmdOpt genopt = new GenerateCmdOpt();
				genopt.GenerateOpt(cmdlist);
				
			}
		}
	}
	public void openFile(JTextField txt, String files, String filename,String type)
	{
		JFileChooser fc=new JFileChooser("user.home");  
		if(type == "FILES_ONLY")
		{
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//FileNameExtensionFilter filter = new FileNameExtensionFilter("spec FILES", "spc");	   
		}
		else
		{
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		if(!files.isEmpty() && !filename.isEmpty())
		{
			FileNameExtensionFilter filter = new FileNameExtensionFilter(filename, files);
			fc.setFileFilter(filter);
		}
		//findDirectory(new File(System.getProperty("user.dir")));
		//fc.setCurrentDirectory(new File("user.home"+ "\\work"));
		//fc.setCurrentDirectory(fc.getCurrentDirectory());
		int i=fc.showOpenDialog(this);  		          
		if(i==JFileChooser.APPROVE_OPTION){  
			filepath=fc.getSelectedFile().getName();
			absolutepath = fc.getSelectedFile().getAbsolutePath();
			txt.setText(absolutepath);
			txt.setEnabled(false);
		}
	}

	public void findDirectory(File parentDirectory) {
		if(foundFolder) {
			return;
		}
		File[] files = parentDirectory.listFiles();
		System.out.println(files.length);
		for (int i=0; i< files.length; i++) {
			System.out.println(files[i].getName());
			if (files[i].isFile()) {
				continue;
			}
			if (files[i].getName().equals("src")) {
				foundFolder = true;
				dir = files[i].getName();
				break;
			}
			if(files[i].isDirectory()) {
				findDirectory(files[i]);
			}
		}
	}
}
