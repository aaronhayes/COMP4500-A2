package stringTransformation;

import stringTransformation.TransElement.TransCode;

public class RecursiveStringTransformation { 

    private String x; // Source string
    private String y; // Target string
    
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
        numberOfCalls = 0;
        //TODO
    }
    
}
