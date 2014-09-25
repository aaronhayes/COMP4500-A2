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
        numberOfCalls = transformCost(x, y);
        return numberOfCalls;
    }
    
    /**
     * Calculate the cost to transform one string into another.
     *  Solve by recursively summing the cost of transforming substrings.
     *  
     * @param stringX String 1
     * @param stringY String 2
     * @return cost of string transformation
     */
    private int transformCost(String stringX, String stringY) {
        
        if (stringX.isEmpty()) return stringY.length() * TransCode.Insert.cost;
        if (stringY.isEmpty()) return min(TransCode.Kill.cost, 
                x.length() * TransCode.Delete.cost);
        
        
        int copy = Integer.MAX_VALUE; 
        int replace = Integer.MAX_VALUE;
        int swap = Integer.MAX_VALUE;
        
        if (stringX.charAt(0) == stringY.charAt(0)) {
            // Copy Case
            copy = transformCost(stringX.substring(1), stringY.substring(1))
                    + TransCode.Copy.cost;
        } else {
            // Replace Case
            replace = transformCost(stringX.substring(1), stringY.substring(1))
                    + TransCode.Replace.cost;
        }
        
        if (stringX.length() > 1 && stringY.length() > 1 
                && stringX.charAt(0) == stringY.charAt(1)
                && stringX.charAt(1) == stringY.charAt(0)) {
            // Swap Case
            swap = transformCost(stringX.substring(2), stringY.substring(2)) 
                    + TransCode.Swap.cost;
        }
        
        // Always Consider Delete, and Insert Cases
        int delete = transformCost(stringX.substring(1), stringY) 
                + TransCode.Delete.cost;
        int insert = transformCost(stringX, stringY.substring(1)) 
                + TransCode.Insert.cost;
        
        return min(copy, replace, delete, swap, insert);
    }
    
    
    /**
     * Calculate the minimum value within a set of integers 
     * @param numbers set of integers to be compared
     * @return minimum of numbers or 0 if numbers.length == 0
     */
    private static int min(int ... numbers) {
        if (numbers.length == 0) return 0;
        
        int min = Integer.MAX_VALUE;
        for (int number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }
}
