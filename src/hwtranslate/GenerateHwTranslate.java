package hwtranslate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;

import UI.ErrorPane;
import UI.GeneralConfig;
import runHwTools.HwVerification;
import shellScriptGenerator.GenScript;
import java.nio.file.Path;
import java.nio.file.Paths;
public class GenerateHwTranslate {

	GeneralConfig genconfig;
	GenScript genscript;
	HwVerification hwverify; 
	ErrorPane errorpanehwtrans;
	Boolean foundFolder = false;
	String dir;
	public GenerateHwTranslate() {
		// TODO Auto-generated constructor stub
	}

	public void GenerateVerilog()
	{
		String smt = " ";
		String spec = " ";
		//String outpath = genconfig.getOutputPath().getText();
		Path currentRelativePath = Paths.get("");
		String outpath = currentRelativePath.toAbsolutePath().toString();
		outpath = outpath + "/output";
		System.out.println("Current relative path is: " + outpath);
		File[] files = getCpaOutputFiles(outpath);
		for(int i = 0; i< files.length; i++)
		{
			smt = smt + " " +files[i].toString();
		}
		File [] specfile = getCpaSpecFiles(outpath);	
		for (int i = 0; i < specfile.length; i++)
		{
			spec = spec  + " " + specfile[i].toString();
		}
		String combine = spec + " " + smt;
		System.out.println(combine);
		runVerilogTool(combine);

	}

	public void runVerilogTool(String files)
	{
		try
		{
			String target = "java -jar " + " " + genconfig.getCpaScriptPath().getText().replace("cpa.sh", "") + "EPGenerator.jar" + " " + files +" " + "-o" + " " + genconfig.getOutputPath().getText() + "/out.v" ;
			String verilogout = genconfig.getOutputPath().getText() + "/out.v" ;
			hwverify.setHwTransVerilogOutPath(verilogout);
			System.out.println(target);
			genscript.GenHwTranslateScript(target);
			Runtime.getRuntime().exec(target);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(target);
			proc.waitFor();
			StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";                       
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			System.out.println("### " + output);
			errorpanehwtrans.getErrorTextHwTrans().setText(output.toString());
            errorpanehwtrans.getErrorTextHwTrans().setEditable(false);

		} catch (Throwable t)
		{
			t.printStackTrace();
		}    
	}

	public File[] getCpaOutputFiles(String output)
	{
		File dir = new File(output);
		File[] matches = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.startsWith("ci") && name.endsWith(".smt");
			}
		});
		for(File file : matches){
			System.out.println(file.getName());
		}


		return matches;
	}

	public File[] getCpaSpecFiles(String output)
	{
		File dir = new File(output);
		File[] matches = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.startsWith("ci") && name.endsWith(".txt");
			}
		});
		for(File file : matches){
			System.out.println(file.getName());
		}


		return matches;
	}


	public void findDirectory(File parentDirectory) {
		if(foundFolder) {
			return;
		}
		File[] files = parentDirectory.listFiles();
		System.out.println(files.length);
		for (int i=0; i< files.length; i++) 
		{
			System.out.println(files[i].getName());
			if (files[i].isFile()) {
				continue;
			}
			if (files[i].getName().equals("ci0.smt")) {
				foundFolder = true;
				dir = files[i].getName();
				break;
			}
			//if(files[i].isDirectory()) {
			//findDirectory(files[i]);
			//}
		}
	}

	public void setGenConfig(GeneralConfig gen)
	{
		genconfig = gen;
	}
	
	public void setGenScript(GenScript script)
	{
       genscript =script;		
	}
	
	public void setHwVerify(HwVerification verify)
	{
		hwverify = verify;
	}
	public void setErrorPaneHwTrans(ErrorPane error)
	{
		errorpanehwtrans = error;
	}

}
