package raspberry.net;

public class BytesString {
    /** Converts null-terminated byte array to a string */
    static String getString( byte [] in, int off, int maxLen ) {

	int pos = 0;
	for (int i=off; i<maxLen+off; i++) {
	    pos = i;
	    if (in[i] ==0) break;
	}
	
	String ret = new String( in , off, pos-off );
	return ret;

    }

}
