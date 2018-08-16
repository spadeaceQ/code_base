package raspberry.pattern;

import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.process.FloatProcessor;
import ij.process.FHT;
import ij.ImageStack;
import ij.IJ;
import ij.gui.GenericDialog;

public class Fold_OTF implements PlugIn {
	
	@Override
	public void run(String what) {
	    
		// get the active image plus instance
		ImagePlus aip = ij.WindowManager.getCurrentImage();
		if (aip == null) {
			IJ.showMessage("No active image stack selected");
			return;
		}

		// test if that is o.k.
		final int imgSize = aip.getWidth();
		if ( ! powerOf2Size( aip.getWidth(), aip.getHeight() ) ) {
			IJ.showMessage("Please use an image of size 2^n x 2^n\n"+
			"e.g. 128x128, 256x256, 512x512");
			return;
		}

		// Display the parameter dialog
 		GenericDialog gd = new GenericDialog("Fold through OTF");
 		gd.addNumericField("FWHM (px)", imgSize/4,0);
 		gd.showDialog();
      		if (gd.wasCanceled()) return;
		int fhmt = (int)gd.getNextNumber();

		// fold
		FHT otf = new FHT( genGauss( imgSize,imgSize, fhmt/2.35 ) , true);
		ImageStack res = new ImageStack( imgSize, imgSize);

		for (int z=0; z<aip.getStack().getSize(); z++) {
		    FHT fhttr  = new FHT( aip.getStack().getProcessor(z+1) );
		    fhttr.transform();

		    for (int y=0;y<imgSize;y++)
		    for (int x=0;x<imgSize;x++)
			    fhttr.setf(x,y, fhttr.getf(x,y) * otf.getf(x,y) );
		    
		    fhttr.inverseTransform();
		    res.addSlice(fhttr);
		    //res.addSlice(otf);
		}

		ImagePlus displ01 = new ImagePlus( "OTF-Fold "+fhmt, res);
		displ01.show();
		
	}

	/**
	 * This generates a simple Gauss PSF / OTF for later multiplication
	 */
	FloatProcessor genGauss(int w, int h, double gauss_width) {

		float r;
		float[] p = new float[w * h];
		float min = Float.MAX_VALUE;
        	float max = Float.MIN_VALUE;
		
		for (int y = 0; y < h; y++)
        	for (int x = 0; x < w; x++) {
			// for a centered OTF
			//r = (float)Math.exp(  -(Math.pow(w/2-x,2) + Math.pow(h/2-y,2)) / (2*Math.pow(gauss_width,2)));
			// for an OTF in the edges
			r  = (float)Math.exp(  -(Math.pow(  x,2) + Math.pow(  y,2)) / (2*Math.pow(gauss_width,2)));
			r += (float)Math.exp(  -(Math.pow(w-x,2) + Math.pow(  y,2)) / (2*Math.pow(gauss_width,2)));
			r += (float)Math.exp(  -(Math.pow(  x,2) + Math.pow(w-y,2)) / (2*Math.pow(gauss_width,2)));
			r += (float)Math.exp(  -(Math.pow(w-x,2) + Math.pow(w-y,2)) / (2*Math.pow(gauss_width,2)));
			if (r<min) min =r;
			if (r>max) max =r;
			p[ x + w*y ] = r;
       		}
						
     		FloatProcessor res = new FloatProcessor(w, h, p, null); 
		res.add( -min);
		res.multiply( 1 /(max-min));

		//IJ.log( "min: "+res.getMin());
     		//IJ.log( "max: "+res.getMax());
     		return res;
	}


	/** check if w==h==2^n and */
        public boolean powerOf2Size(int w, int h) {
	    if (w<16) return false;
	    int i=2;
	    while(i<w) i *= 2;
	    return i==w && w==h;
	}

}
