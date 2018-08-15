package UI;
import UI.GeneralConfig;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import UI.CIExtraction;
import UI.SignAnalysis;
import UI.PredicateAnalysis;
import UI.UserDefinedProp;
import cpacheckerAnalysis.GenerateCmdOpt;
import hwtranslate.GenerateHwTranslate;
import runHwTools.HwVerification;
import shellScriptGenerator.GenScript;

public class UiMain extends JFrame implements ActionListener {

	JPanel pane;
	JPanel flowpanelgeneral;
	JPanel pane2; //sign analysis pane
	JPanel cipane;
	JPanel flowpanel; //holder for sign analysis
	JPanel flowpanel2; // holder  for ci extraction
	JPanel predicatepane;
	JPanel flowpanelpredicate;
	JPanel errorpane;
	JPanel errorpanecpachecker;
	JPanel errorpanehwtranslate;
	JPanel errorpanerunhw;
	JButton generatebutton;
	String filepath;
	String absolutepath;
	GeneralConfig genconfig;
	SignAnalysis sign;
	CIExtraction ci;
	ErrorPane error;
	GenerateCmdOpt cmdopt;
	GenerateHwTranslate genhwtranslate;
	GenScript genscript;
	HwVerification hwverify;
	PredicateAnalysis predicateanalysis;
	public UiMain() {
		// TODO Auto-generated constructor stub

	}

	public void uipane()
	{
		//main panel
		pane = new JPanel();
		GridLayout grid = new GridLayout(8, 2, 0, 0);
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

		//! panel for holding sign analysis pane
		flowpanel = new JPanel();
		flowpanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		predicatepane = new JPanel();
		predicatepane.setLayout(new GridLayout(3,2,0,0));

		flowpanelpredicate = new JPanel();
		flowpanelpredicate.setLayout(new FlowLayout(FlowLayout.LEFT));

		//pane for ci extraction
		cipane = new JPanel();
		GridLayout gridci = new GridLayout(3, 2, 0, 0);
		grid.setHgap(1);
		grid.setVgap(1);
		cipane.setLayout(gridci);

		//! panel for holding ci extraction panel
		flowpanel2 = new JPanel();
		flowpanel2.setLayout(new FlowLayout(FlowLayout.LEFT));

		//pane for  general error messages
		errorpane = new JPanel();
		GridLayout griderror = new GridLayout(2, 1,0,0);
		//BoxLayout griderror = new BoxLayout (errorpane, BoxLayout.Y_AXIS);
		errorpane.setLayout(griderror);

		//pane for  cpa checker output/error messages
		errorpanecpachecker = new JPanel();
		GridLayout griderrorcpa = new GridLayout(2, 1,0,0);
		errorpanecpachecker.setLayout(griderrorcpa);
		
		// pane for hw translate error msgs
		errorpanehwtranslate = new JPanel();
		GridLayout griderrorhwtrans = new GridLayout(2, 1,0,0);
		errorpanehwtranslate.setLayout(griderrorhwtrans);
		
		// pane for hw toolflow error msgs
		errorpanerunhw = new JPanel();
		GridLayout griderrorrunhw = new GridLayout(2, 1,0,0);
		errorpanerunhw.setLayout(griderrorrunhw);


		generatebutton = new JButton("Start Verification");
		generatebutton.addActionListener(this);

		TitledBorder p1Tborder = new TitledBorder("General Config");
		TitledBorder p2Tborder = new TitledBorder("Sign Analysis");
		TitledBorder p3Tborder = new TitledBorder("CI Extraction");
		TitledBorder p4Tborder = new TitledBorder("General Errors");
		TitledBorder p5Tborder = new TitledBorder("Verification output from CPAChecker");
		TitledBorder p6TBorder = new TitledBorder("Predicate Analysis");
		TitledBorder p7Tborder = new TitledBorder("Output from hw translation process");
		TitledBorder p8Tborder = new TitledBorder("Output from hw verification toolflow");


		genconfig.setGeneralConfigPane(pane);

		sign.setSignConfigPane(pane2);
		predicateanalysis.setPredicateConfigPanel(predicatepane);

		ci.setCiConfigPane(cipane);

		error.setErrorPane(errorpane);
		error.setErrorPaneCpa(errorpanecpachecker);
		error.setErrorPaneHwTrans(errorpanehwtranslate);
		error.setErrorPaneRunHw(errorpanerunhw);

		//UserDefinedProp user = new UserDefinedProp();

		pane.setBorder(p1Tborder);
		pane2.setBorder(p2Tborder);
		cipane.setBorder(p3Tborder);
		errorpane.setBorder(p4Tborder);
		errorpanecpachecker.setBorder(p5Tborder);
		predicatepane.setBorder(p6TBorder);
		errorpanehwtranslate.setBorder(p7Tborder);
		errorpanerunhw.setBorder(p8Tborder);

		//! setting all pane to their respective holders
		flowpanelgeneral.add(pane);
		flowpanel.add(pane2);
		flowpanelpredicate.add(predicatepane);
		flowpanel2.add(cipane);

		//! adding the panel to the frame
		setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS )); 
		add(pane);
		add(flowpanel);
		add(flowpanelpredicate);
		add(cipane);
		add(errorpane);
		add(errorpanecpachecker);
		add(errorpanehwtranslate);
		add(errorpanerunhw);
		add(generatebutton);

		pack();
		this.setSize(600,600); 
		setVisible(true); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		flowpanelpredicate.setVisible(false);

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
		int i=fc.showOpenDialog(this)	;	          
		if(i==JFileChooser.APPROVE_OPTION){  
			filepath=fc.getSelectedFile().getName();
			absolutepath = fc.getSelectedFile().getAbsolutePath();
			System.out.println("absolutepath:" + absolutepath);
			txt.setText(absolutepath);
			txt.setEditable(false);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UiMain ui =new UiMain();
		GenScript script = new GenScript();
		GeneralConfig gen = new GeneralConfig();
		CIExtraction ciext = new CIExtraction();
		SignAnalysis signan = new SignAnalysis();
		ErrorPane error = new ErrorPane();
		GenerateHwTranslate hwtrans = new GenerateHwTranslate();
		GenerateCmdOpt cmd = new GenerateCmdOpt();
		HwVerification verify = new HwVerification();
		PredicateAnalysis predicate = new PredicateAnalysis();

		gen.setUiMain(ui);

		ciext.setUiMain(ui);

		signan.setUiMain(ui);

		error.setUiMain(ui);

		predicate.setUiMain(ui);

		script.setGenCmdOpt(cmd);
		script.setGenConfig(gen);

		hwtrans.setGenConfig(gen);
		hwtrans.setGenScript(script);
		hwtrans.setHwVerify(verify);
		hwtrans.setErrorPaneHwTrans(error);

		verify.setGenConfig(gen);
		verify.setErrorPaneRunHw(error);

		cmd.setErrorPane(error);
		cmd.setGeneralConfig(gen);
		cmd.setGenerateHwTranslate(hwtrans);
		cmd.setGenScript(script);

		ui.setGenConfig(gen);
		ui.setCiExtraction(ciext);
		ui.setSignAnalysis(signan);
		ui.setPredicateAnalysis(predicate);
		ui.setErrorPane(error);
		ui.setGenerateCmdOptions(cmd);
		ui.setGenerateHwTranslate(hwtrans);
		ui.setGenerateScript(script);
		ui.setHwVerify(verify);
		ui.uipane();

	}

	public boolean checkForEmptyComponents()
	{
		boolean status = true;
		if( genconfig.getOutputPath().getText().isEmpty() )
		{
			error.getErrorText().setText("Output path is empty give path !!");
			status = false;
			System.out.println("output empty");
		}
		if(genconfig.getProgramPath().getText().isEmpty())
		{
			error.getErrorText().setText("Program path empty give path !!");
			status = false;
			System.out.println("program path empty");
		}
		if(genconfig.getSpecPath().getText().isEmpty())
		{
			error.getErrorText().setText("Specification path empty give path !!");
			status = false;
			System.out.println("spec path empty");

		}
		if(ci.getCiAbstractClass().getItemAt(ci.getCiEnable().getSelectedIndex()).matches("false"))
		{
			error.getErrorText().setText("Enable Custom Intruction..Verification will not take place !!");
			status = false;
			System.out.println("ci not enable");
		}
		if(ci.getCiAbstractClass().getItemAt(ci.getCiEnable().getSelectedIndex())=="true"
				&& ci.getCiDefFile().getText().isEmpty())
		{
			error.getErrorText().setText("Definition path empty give path !!");
			status = false;
			System.out.println("def path empty");
		}
		if(predicateanalysis.getThresholdTextField().isShowing()
				&& predicateanalysis.getThresholdProperty().isEmpty())
		{
			status = false;
			System.out.println(" thershold prop empty");
		}
		if(genconfig.getCircuitName().getText().isEmpty())
		{
			error.getErrorText().setText("Circuit name not specified");
			status = false;
			System.out.println("circuit name empty");
		}
		return status;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if(e.getSource() == generatebutton)
		{
			error.getErrorCpaText().selectAll();
			error.getErrorCpaText().replaceSelection("");

			error.getErrorTextHwTrans().selectAll();
			error.getErrorTextHwTrans().replaceSelection("");

			error.getErrorTextRunHw().selectAll();
			error.getErrorTextRunHw().replaceSelection("");

			error.getErrorText().selectAll();
			error.getErrorText().replaceSelection("");

			if(checkForEmptyComponents())
			{
				ArrayList<String> cmdlist = new ArrayList<String>();
				String setprop = "-setprop ";
				String merge = null;
				String threshold = null;
				String solver = null;
				String usemultiedges = null;
				boolean signanalysis = true;

				String config = "-" + genconfig.getConfigBox().getItemAt(genconfig.getConfigBox().getSelectedIndex());
				if(genconfig.getConfigBox().getItemAt(genconfig.getConfigBox().getSelectedIndex()) == "signAnalysis")
				{
					merge = sign.getMergeProperty().getItemAt(sign.getMergeProperty().getSelectedIndex()); 
					if(merge.contains("SEPERATE"))
					{
						merge = setprop + "cpa.sign.merge=SEP";
					}
					else
					{
						merge = setprop + "cpa.sign.merge=JOIN";
					}
					signanalysis = true;
				}
				else
				{
					threshold = setprop + "cpa.predicate.blk.threshold="+ predicateanalysis.getThresholdProperty();
					solver = setprop + "solver.solver="+predicateanalysis.getSolverProperty();
					usemultiedges = setprop + "cfa.useMultiEdges="+predicateanalysis.getUseMultiEdgesProperty();
					signanalysis = false;
				}

				String outputpath = setprop + "output.path="+genconfig.getOutputPath().getText();
				String prog = genconfig.getProgramPath().getText();
				String spec = "-spec " + genconfig.getSpecPath().getText();
				String ci_enable = setprop + "analysis.extractRequirements.customInstruction="+ci.getCiEnable().getItemAt(ci.getCiEnable().getSelectedIndex());
				String ci_deffile = setprop + "custominstructions.definitionFile="+ci.getCiDefFile().getText();
				String stateprefix = "org.sosy_lab.cpachecker.cpa.";
				String abstractstate = stateprefix + ci.getCiAbstractClass().getItemAt(ci.getCiAbstractClass().getSelectedIndex());
				String ci_abstractfile = setprop + "custominstructions.requirementsStateClassName="+ abstractstate;
				cmdlist.add(config);
				if(signanalysis)
				{
					cmdlist.add(merge);
				}
				else
				{
					cmdlist.add(threshold);
					cmdlist.add(solver);
					cmdlist.add(usemultiedges);
				}
				cmdlist.add(spec);
				cmdlist.add(ci_abstractfile);
				cmdlist.add(ci_deffile);
				cmdlist.add(ci_enable);
				cmdlist.add(prog);
				cmdlist.add(outputpath);
				//backend script generation
				



				generatebutton.setEnabled(false);
				cmdopt.GenerateOpt(cmdlist);
				genhwtranslate.GenerateVerilog();
				hwverify.runHwTools();
				genscript.GenerateShellScript();

				generatebutton.setEnabled(true);
			}
			else
			{
				error.getErrorText().setText("Verification not statrted because of empty fields"
						+ "Mandatory fileds- Outputpath, program path, sepcification file path, definition file path, enable custom instruction true !!");
				error.getErrorText().setEditable(false);
			}
		}

	}
	//! setters and getters
	public JPanel getSignFlowPanel()
	{
		return flowpanel;
	}

	public JPanel getPredicateFlowPanel()
	{
		return flowpanelpredicate;
	}
	public JPanel getCiExtractPanel()
	{
		return cipane;
	}
	public JButton getGenerteButton()
	{
		return generatebutton;
	}

	public void setGenConfig(GeneralConfig gen)
	{
		genconfig = gen;
	}

	public void setSignAnalysis(SignAnalysis signanalysis)
	{
		sign = signanalysis;
	}
	public void setPredicateAnalysis(PredicateAnalysis predicate)
	{
		predicateanalysis = predicate;
	}
	public void setCiExtraction(CIExtraction ciextract)
	{
		ci = ciextract;
	}
	public void setErrorPane(ErrorPane errorpane)
	{
		error = errorpane;
	}

	public void setGenerateCmdOptions(GenerateCmdOpt opt)
	{
		cmdopt = opt;
	}
	public void setGenerateHwTranslate(GenerateHwTranslate genhw)
	{
		genhwtranslate = genhw;
	}

	public void setGenerateScript(GenScript script)
	{
		genscript = script;
	}

	public void setHwVerify(HwVerification verify)
	{
		hwverify = verify;
	}
}
