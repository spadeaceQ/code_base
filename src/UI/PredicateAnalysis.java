package UI;

import javax.print.DocFlavor.STRING;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PredicateAnalysis {

	JLabel threshold;
	JTextField thresholdvalue;
	
	JLabel multiedges;
	JComboBox<String> multiedges_combobox;
	
	JLabel solver;
	JComboBox<String> solver_combobox; 

    UiMain main;
	public PredicateAnalysis() {
		// TODO Auto-generated constructor stub
	}
	
	public void setPredicateConfigPanel(JPanel pane)
	{
		
		threshold = new JLabel("Set Threshold Value");
		thresholdvalue = new JTextField();
		
		multiedges = new JLabel("Use Multiedges");
		String[] property = new String[]{"False", "True"};
		multiedges_combobox  = new JComboBox<>(property);
		
		solver = new JLabel("Set Solver");
		String [] solverString = new String[]{"MATHSAT5"};
		solver_combobox = new JComboBox<>(solverString);
		
		this.addPredicateConfig(pane);
	}
	public void addPredicateConfig(JPanel pane)
	{
		pane.add(threshold);
		pane.add(thresholdvalue);
		pane.add(multiedges);
		pane.add(multiedges_combobox);
		pane.add(solver);
		pane.add(solver_combobox);
	}
	public String getThresholdProperty()
	{
		return thresholdvalue.getText();
	}
	public JTextField getThresholdTextField()
	{
		return thresholdvalue;
	}
	public String getUseMultiEdgesProperty()
	{
		return multiedges_combobox.getItemAt(0);
	}
	public String getSolverProperty()
	{
		return solver_combobox.getItemAt(0);
	}
	
	public void setUiMain(UiMain ui) {
        main = ui;		
	}
}
