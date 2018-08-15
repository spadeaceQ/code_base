

package shellScriptGenerator;  //generate shell script
import java.io.*;
//import java.io.IOException;
import java.util.*;

import UI.ErrorPane;
import UI.GeneralConfig;
import UI.UiMain;
import hwtranslate.GenerateHwTranslate;
import cpacheckerAnalysis.GenerateCmdOpt;

import java.io.*;
public class GenScript {
	GenerateCmdOpt gencmd;
	GenerateHwTranslate genhwtranslate;
	UiMain ui;
	GeneralConfig genconfig;
	String  cpascript;
	String hwtranscript;

	public void GenerateShellScript()
	{
		String scriptcontent = cpascript + hwtranscript;
		try{
			String filepath = new String(genconfig.getOutputPath().getText() + "/verification.sh");
			Writer output = new BufferedWriter(new FileWriter(filepath));
			output.write(scriptcontent);
			output.close();
			Runtime.getRuntime().exec("chmod u+x" + " " + filepath);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void GenCpaScript(String target)
	{
		target.replace("bash", "#!/bin/bash  \n" );
		String cpascriptcontent = new String(target + " " + "\n" );
		//return cpascriptcontent;
		cpascript = cpascriptcontent;
	}

	public void GenHwTranslateScript(String target)
	{
		String hwscriptcontent = new String(target + " " + "\n" );
		//return hwscriptcontent;
		hwtranscript = hwscriptcontent;
	}
	public void setGenCmdOpt(GenerateCmdOpt opt)
	{
		gencmd = opt;
	}
	public void setGenHwTranslate(GenerateHwTranslate genhw)
	{
		genhwtranslate = genhw;	
	}
	public void setGenConfig(GeneralConfig generalconfig)
	{
		genconfig = generalconfig;
	}
}
