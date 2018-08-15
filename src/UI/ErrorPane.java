package UI;
import javax.swing.*;

public class ErrorPane {
	JTextArea errortext;
	JTextArea errortextcpa;
	JTextArea errortexthwtrans;
	JTextArea errortextrunhw;
	UiMain main;

	public ErrorPane() {
		// TODO Auto-generated constructor stub
	}

	public void setErrorPane(JPanel errorpane)
	{
		errortext = new JTextArea();
		//errortext.setEnabled(false);
		
		errortext.setWrapStyleWord(true);
		errortext.setLineWrap(true);
		this.addErrorPane(errorpane);
	}
	public void setErrorPaneCpa(JPanel errorpanecpa)
	{
		errortextcpa = new JTextArea();
		//errortext.setEnabled(false);
		
		errortextcpa.setWrapStyleWord(true);
		errortextcpa.setLineWrap(true);
		this.addErrorPaneCpa(errorpanecpa);
	}
	
	public void setErrorPaneHwTrans(JPanel errorpanehwtrans)
	{
		errortexthwtrans = new JTextArea();
		//errortext.setEnabled(false);
		
		errortexthwtrans.setWrapStyleWord(true);
		errortexthwtrans.setLineWrap(true);
		this.addErrorPaneHwTrans(errorpanehwtrans);
	}
	
	public void setErrorPaneRunHw(JPanel errorpanerunhw)
	{
		errortextrunhw = new JTextArea();
		//errortext.setEnabled(false);
		
		errortextrunhw.setWrapStyleWord(true);
		errortextrunhw.setLineWrap(true);
		this.addErrorPaneRunHw(errorpanerunhw);
	}
	public void addErrorPane(JPanel errorpane)
	{
		errorpane.add(errortext);
		errorpane.add(new JScrollPane(errortext));
		errorpane.setSize(5, 5);
	}
	
	public void addErrorPaneCpa(JPanel errorpane)
	{
		errorpane.add(errortextcpa);
		errorpane.add(new JScrollPane(errortextcpa));
		errorpane.setSize(5, 5);
	}
	
	public void addErrorPaneHwTrans(JPanel errorpane)
	{
		errorpane.add(errortexthwtrans);
		errorpane.add(new JScrollPane(errortexthwtrans));
		errorpane.setSize(5, 5);
	}
	
	public void addErrorPaneRunHw(JPanel errorpane)
	{
		errorpane.add(errortextrunhw);
		errorpane.add(new JScrollPane(errortextrunhw));
		errorpane.setSize(5, 5);
	}

	public void setUiMain(UiMain ui) 
	{
       main = ui;
	}
	
	public JTextArea getErrorText()
	{
		return errortext;
	}
	public JTextArea getErrorCpaText()
	{
		return errortextcpa;
	}
	
	public JTextArea getErrorTextHwTrans()
	{
		return errortexthwtrans;
	}
	public JTextArea getErrorTextRunHw()
	{
		return errortextrunhw;
	}
}
