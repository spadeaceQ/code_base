package slm;

import ij.plugin.PlugIn;
import raspberry.net.Send_Raspberry;
import gui.Control_Software_main;

import com.forthdd.commlib.core.CommLib;
import com.forthdd.commlib.exceptions.AbstractException;
import com.forthdd.commlib.r4.R4CommLib;
import com.forthdd.commlib.r11.R11CommLib;
import com.forthdd.metrocon.MainView;
import com.forthdd.replib.Config;

//import com.forthdd.commlib.*;


public class slmMain implements PlugIn{

	 
	
	public void run(String arg) {
	
	if(arg=="control"){
		slm.Main_View mv = new slm.Main_View();
		mv.setVisible(true);
		}
	}
	

	public static void main( String [] args ) {
    slmMain cl = new slmMain();
    
    cl.run("control");
}

}
