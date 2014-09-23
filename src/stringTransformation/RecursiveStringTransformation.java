package stringTransformation;

import stringTransformation.TransElement.TransCode;

public class RecursiveStringTransformation { 

    private String x; // Source string
    private String y; // Target string
    private StringBuffer z; // String Buffer
    
    public RecursiveStringTransformation( String x, String y ) {
        super();
        this.x = x;
        this.y = y;
    }
    
    
    public int numberOfCalls;
    
    /** Recursive implementation of string transformation function
     * @return the minimal cost over all transformations that transform
     *   string x to string y 
     */
    public int stringTransformation() {
        numberOfCalls = lev(x, y);
        return numberOfCalls;
    }
    
    
    private int lev(String a, String b) {
        if (a.length() == 0) return b.length();
        if (b.length() == 0) return a.length();
        
        return Math.min(
                lev(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1)) + 
                    (a.substring(0, a.length() -1).equals(b.substring(0, b.length() - 1)) ? 1 : 0), 
                Math.min(lev(a.substring(0, a.length() - 1), b) + 1, 
                lev(a, b.substring(0, b.length() - 1)) + 1));
    }
}
