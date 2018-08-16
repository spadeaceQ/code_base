package gui;


import ij.*;
import ij.process.*;
import ij.gui.*;

import java.awt.*;

import ij.plugin.*;
import ij.plugin.frame.*;

	public class CS_Plugin implements PlugIn {


		public static void main( String [] args ) {
			CS_Plugin cl = new CS_Plugin();
		    cl.run("control");
		}
		
		public void run(String arg) {
			
			if (arg.equals("control"))
			{	
				try {
					//Control_Software_main window = new Control_Software_main();
					CS_allinone window = new CS_allinone();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					//e.printStackTrace();
					showDialog();
				}
			}
			else showDialog();
		}

		
		
		public void showDialog() {
			IJ.showMessage("Some exception occured!!!");
	    }
	}


