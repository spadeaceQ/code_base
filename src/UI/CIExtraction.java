package UI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarException;

import javax.swing.*;


public class CIExtraction implements ActionListener {

	JButton buttondeffile;
	JTextField textdeffile;
	JComboBox<String> abstracttext;
	JPanel[] pane3;

	JLabel ciextract;
	JLabel abstractname;
	JLabel deffile;

	JComboBox<String> cienable;
    UiMain main;
	public CIExtraction() {
		// TODO Auto-generated constructor stub
	}
	
	public void setCiConfigPane(JPanel cipane)
	{
		buttondeffile = new JButton("Open File");
		buttondeffile.addActionListener(this);

		textdeffile  = new JTextField();

		pane3 = new JPanel[6];
		pane3[0] = new JPanel();
		pane3[0].setLayout(new BoxLayout(pane3[0], BoxLayout.X_AXIS));
		pane3[0].add(textdeffile);
		pane3[0].add(buttondeffile);

		ciextract = new JLabel("Enable Custom Instruction");
		String[] cibool = new String[]{"true","false"};
		cienable = new JComboBox<>(cibool);
		cienable.addActionListener(this);

		abstractname = new JLabel("Abstract Class Name");
		String[] abstractstate = new String[]{"sign.SignState", "predicate.PredicateAbstractState"};  //org.sosy_lab.cpachecker.cpa.sign.SignState
		abstracttext = new JComboBox<>(abstractstate);

		deffile = new JLabel("Definition File");
		this.addCiConfig(cipane);
	}

	public void addCiConfig(JPanel cipane)
	{
		cipane.add(ciextract);
		cipane.add(cienable);
		cipane.add(abstractname);
		cipane.add(abstracttext);
		cipane.add(deffile);
		pane3[0].add(textdeffile);
		pane3[0].add(buttondeffile);
		cipane.add(pane3[0]);

	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//UiMain ui = new UiMain();
		// TODO Auto-generated method stub
		if(e.getSource() == cienable && cienable.getItemAt(cienable.getSelectedIndex()) == "false")
		{
			for(int i =0 ; i< main.getCiExtractPanel().getComponentCount(); i++)
			{
				main.getCiExtractPanel().getComponent(i).setEnabled(false);
				if(main.getCiExtractPanel().getComponent(i).equals(cienable))
				{
					main.getCiExtractPanel().getComponent(i).setEnabled(true);
				}
				if(main.getCiExtractPanel().getComponent(i).equals(pane3[0]))
				{
					pane3[0].getComponent(0).setEnabled(false);
					pane3[0].getComponent(1).setEnabled(false);
				}
			}		
		}

		else if(e.getSource() == cienable && cienable.getItemAt(cienable.getSelectedIndex()) == "true")
		{
			for(int i =0 ; i< main.getCiExtractPanel().getComponentCount(); i++)
			{
				main.getCiExtractPanel().getComponent(i).setEnabled(true);
				if(main.getCiExtractPanel().getComponent(i).equals(cienable))
				{
					main.getCiExtractPanel().getComponent(i).setEnabled(true);
				}
				if(main.getCiExtractPanel().getComponent(i).equals(pane3[0]))
				{
					pane3[0].getComponent(0).setEnabled(true);
					pane3[0].getComponent(1).setEnabled(true);
				}				
			}
		}
		else if(e.getSource() == buttondeffile)
		{
			main.openFile(textdeffile, "txt", "text files", "FILES_ONLY");
		}
	}
	//! getters
	public JComboBox<String> getCiEnable()
	{
		return cienable;
	}
	public JTextField getCiDefFile()
	{
		return textdeffile;
	}
	public JComboBox<String> getCiAbstractClass()
	{
		return abstracttext;
	}
	public void setUiMain(UiMain ui)
	{
	   	main = ui;
	}
}
