package raspberry.pattern;

import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;

import ij.ImageStack;
import ij.process.StackProcessor;

import ij.IJ;
import ij.Prefs;

import ij.gui.GenericDialog;

import java.text.DecimalFormat;

// Small plugin to send images over the network
public class Generate_Pattern implements PlugIn {



	
	@Override
	public void run(String arg) {

		// Display the parameter dialog
 		GenericDialog gd = new GenericDialog("Transfer to Raspberry");
 		gd.addNumericField("px/on", 3,0);
 		gd.addNumericField("px/total (mult. of 3)", 9,0);
 		gd.addCheckbox("Invert?",false);
 		gd.addNumericField("width",  1920,0);
 		gd.addNumericField("height", 1200,0);
	
		String []  form = { "Stripes" , "Chessboard" };
		gd.addChoice( "Form" , form , "Stripes" );
		
		gd.showDialog();
      		if (gd.wasCanceled()) return;

		int pxl = (int)gd.getNextNumber();
		int len = (int)gd.getNextNumber();
		boolean invert = gd.getNextBoolean();

		final int width = (int)gd.getNextNumber();
		final int height = (int)gd.getNextNumber();

		final int whichPattern = gd.getNextChoiceIndex();


		// generate the stack
		int phaseLen = len/3;

		

		ImageStack is = new ImageStack(width,height);

		if (whichPattern == 0 ) {
		    if (len%3!=0) {
			    IJ.showMessage("Please use a multiple of 3 for raspberry.pattern length");
			    return;
		    }
		    for (int dir = 0; dir<4; dir++ )
		    for (int pha=0; pha<3; pha++) {
			    IJ.showProgress(dir*4+pha,12);
			    is.addSlice( genLinePattern( 
				pxl, len, pha*phaseLen, 
				invert , dir, width, height)); 
		    }			
		}
			
		if (whichPattern == 1 )
		    for (int yO=0; yO<len/2; yO+=len/4)
		    for (int xO=0; xO<len  ; xO+=len/4)
			    is.addSlice( genChessboardPattern( 
				len, xO,yO, 
				invert , width, height)); 


		/* 
		// The old code
		int [] angles = { 0, 90, 45, 135 };
		int i=0; double toRad = 2 * Math.PI/360;

		if ( len > 3)	
		    for (double ang : angles )
		    for (int pha=0; pha<3; pha++) {
			IJ.showProgress(i++,12);
			is.addSlice( genPattern( ang*toRad, pxl, len, pha*phaseLen, invert , width, height) );
		} else {
		    // use a different algorith do generate the finest raspberry.pattern
		    double [] ang1 = {0,90};
		    for (double ang : ang1 )
		    for (int pha=0; pha<3; pha++) {
			IJ.showProgress(i++,12);
			is.addSlice( genPattern( ang*toRad, pxl, len, pha*phaseLen, invert , width, height) );
		    }

		    int [] dirs = {-1,1};
		    for (int dir : dirs )
		    for (int pha=0; pha<3; pha++) {
			IJ.showProgress(i++,12);
			is.addSlice( genFinest( pxl, len, dir , pha, invert , width, height ) );
		    }

		}
		*/

		


		ImagePlus displ01 = new ImagePlus("PatternStack", is);	
		displ01.show();	

	}


	/** Generate a line raspberry.pattern with abritraty angle. */
	static ImageProcessor genGenericLinePattern(
		double angle , int px, int len , 
		int phase, boolean invert , int w, int h) {

	    ImageProcessor ip  = new ByteProcessor(w,h);

	    for (int y=0;y<ip.getHeight();y++)
	    for (int x=0;x<ip.getWidth();x++) {
		  // took this from Marcels code :)
		  double projection =  x*Math.cos(angle)+y*Math.sin(angle) +(1920*1200*2);
		  //double length     = ( (projection+phase) *2*Math.PI)/(double)px;
		  //double factor     = ( Math.signum(Math.sin(length) )+1)*124;
		  int factor;
		  if (invert)
		  	factor = (((int)projection+phase)%len<px)?(0):(255) ;
		  else
		  	factor = (((int)projection+phase)%len<px)?(255):(0) ;
		  
		  ip.setf( x, y, (float)factor);
		}

	    return ip;

	}


	/** Generate a line raspberry.pattern in 0,90,45,135 deg. */
	static ImageProcessor genChessboardPattern(
		int len, int xO, int yO, 
		boolean invert, int w, int h) {

	    ImageProcessor ip  = new ByteProcessor(w,h);

		
	    for (int y=0;y<ip.getHeight();y++) 
	    for (int x=0;x<ip.getWidth();x++) {
		boolean onX = ((x+xO)%(len)<(len/2));
		boolean onY = ((y+yO)%(len)<(len/2));
    
		boolean on = onX ^ onY ;

		

		float px = ( on ^ invert )?(255):(0);
		ip.setf( x, y, px);
		}
		

	    return ip;

	}

	/** Generate a line raspberry.pattern in 0,90,45,135 deg. */
	static ImageProcessor genLinePattern(
		int pxOn, int len, int phase, 
		boolean invert, int dir, 
		int w, int h) {

	    ImageProcessor ip  = new ByteProcessor(w,h);

		
	    for (int y=0;y<ip.getHeight();y++) 
	    for (int x=0;x<ip.getWidth();x++) {
		boolean on = false;
		
		switch (dir) {
		    case 0:
		    on = (((y+phase)%len)<pxOn);
		    break;

		    case 1:
		    on = (((x+phase)%len)<pxOn);
		    break;

		    case 2:
		    on = (((x+y+phase*3/2)%(len*3/2))<(pxOn*3/2));
		    break;

		    case 3:
		    on = (((x-y+h+phase*3/2)%(len*3/2))<(pxOn*3/2));
		    break;

		}
		float px = ( on ^ invert )?(255):(0);
		ip.setf( x, y, px);
		}
		

	    return ip;

	}




}
