package runHwTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import UI.ErrorPane;
import UI.GeneralConfig;
import UI.UiMain;
import shellScriptGenerator.GenScript;

public class HwVerification {
	GeneralConfig genconfig;
	GenScript genscript;
	ErrorPane errorpanerunhw;
	private String hwtransverilogfile;
	
	public void setHwTransVerilogOutPath(String out)
	{
		hwtransverilogfile = out;
		//return target;
	}
	
	public String getHwTransVerilogFile()
	{
		return hwtransverilogfile;
	}
	
	public String getVerilogPath()
	{
		String verilog = genconfig.getVerilogPath().getText();
		return verilog;
	}
	
	public void runHwTools()
	{
		try
		{
			File currentDirFile = new File(".");
			String circuitName = genconfig.getCircuitName().getText();
			String curfolder = currentDirFile.getAbsolutePath().replace("/.", "");
			String target = new String("bash" + " "  + curfolder + "/hwtoolflow/circuitToolflow.sh" + " "
                         + circuitName + " " + "32" + " " + getHwTransVerilogFile() + " " +  getVerilogPath() );
			String[] env = {"PATH=/bin:/usr/bin/"};
			System.out.println("project path " + target);
			//File dir = new File("/home/tasneem/workspace/HwSwCoVerificationFramework/hwtoolflow/"); //path
			File dir = new File(curfolder + "/hwtoolflow"); //path
			File dirbuild = new File(curfolder + "/hwtoolflow/build/");
			deleteFolder(dirbuild);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(target,null,dir);
			proc.waitFor();
			StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";                       
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
				//System.out.println("#######intermediate op" + output);

			}
			/*ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",target);
            Process p = pb.start();
            pb.inheritIO();
            p.waitFor();
            int shellExitStatus = p.exitValue();
            System.out.println(shellExitStatus);
            StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";                       
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}*/
			System.out.println("#######" + output);
			errorpanerunhw.getErrorTextRunHw().setText(output.toString());
			errorpanerunhw.getErrorTextRunHw().setEditable(false);


		} catch (Throwable t)
		{
			t.printStackTrace();
		}    

	   	
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    //folder.delete();
	}
	
	
	public void setGenConfig(GeneralConfig config)
	{
		genconfig = config;
	}
	public void setGenScript(GenScript scriptgen)
 	{
       genscript =scriptgen;		
	}
	public void setErrorPaneRunHw(ErrorPane error)
	{
		errorpanerunhw = error;
	}
			
}
