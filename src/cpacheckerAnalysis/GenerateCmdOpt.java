package cpacheckerAnalysis;

import java.io.*;
//import java.io.IOException;
import java.util.*;

import UI.ErrorPane;
import UI.GeneralConfig;
import hwtranslate.GenerateHwTranslate;
import shellScriptGenerator.GenScript;

import java.lang.Runtime;


public class GenerateCmdOpt {

	//public GenerateCmdOpt() {
		// TODO Auto-generated constructor stub
	//}
	ErrorPane errorpane;
	GeneralConfig genconfig;
	GenerateHwTranslate genhwtrans;
	GenScript genscript;
	public void GenerateOpt(ArrayList<String> cmdopt) 
	{
		try
        {    
			String str = " ";
			for(int i = 0; i< cmdopt.size(); ++i)
			{
				str=str + cmdopt.get(i) + " ";
				System.out.println("str:"+str);
			}
            //String target = new String(" bash /home/tasneem/work/ShkWork/CPAChecker/trunk/scripts/cpa.sh" + " " + str);
			String target = new String("bash"+" "+ genconfig.getCpaScriptPath().getText() + " " + str);
			genscript.GenCpaScript(target);
            System.out.println(target);
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
            errorpane.getErrorCpaText().setText(output.toString());
            errorpane.getErrorCpaText().setEditable(false);
            //genhwtrans.GenerateVerilog();

        } catch (Throwable t)
        {
            t.printStackTrace();
        }    
	}
	
	public void setErrorPane(ErrorPane error)
	{
		errorpane = error;
	}
	
	public void setGeneralConfig(GeneralConfig generalconfig)
	{
		genconfig = generalconfig;
	}
	public void setGenerateHwTranslate(GenerateHwTranslate genhw)
	{
        genhwtrans = genhw;		
	}
	
	public void setGenScript(GenScript script)
	{
       genscript =script;		
	}
	
	
}
