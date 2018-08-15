package UI;
import javax.swing.*;

public class SignAnalysis {
    JLabel merge;
    JComboBox<String> mergeprop;
    UiMain main;
	public SignAnalysis() {
		// TODO Auto-generated constructor stub
	}
	
	public void setSignConfigPane(JPanel pane)
	{
		merge = new JLabel("Merge Property");
		String[] property = new String[]{"JOIN", "SEPERATE"};
		mergeprop  = new JComboBox<>(property);
		this.addSignConfig(pane);
	}
	public void addSignConfig(JPanel pane)
	{
		pane.add(merge);
		pane.add(mergeprop);
	}
	public JComboBox<String> getMergeProperty()
	{
		return mergeprop;
	}
	public void setUiMain(UiMain ui) {
        main = ui;		
	}
}
