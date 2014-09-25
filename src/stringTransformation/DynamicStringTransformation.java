package stringTransformation;

import java.util.ArrayList;
import java.util.List;

import stringTransformation.TransElement.TransCode;

public class DynamicStringTransformation { 

    private String x; // Source string
    private String y; // Target string
    
    public DynamicStringTransformation( String x, String y ) {
        super();
        this.x = x;
        this.y = y;
    }
       
    /** Dynamic programming implementation of string transformation function
     * @return the minimal cost over all transformations that transform
     *   string x to string y 
     */
    int[][] table;
    TransElement[][] trans;
    public int numberOfIterations;
    private int iKill;
    
    public int stringTransformation() {
        numberOfIterations = transformCost();
        return numberOfIterations;
    }
    
    /**
     * Calculate the Minimum Transformation Cost using a bottom-up
     *  dynamic approach. Runs in O(M * N) time.
     * @return minimum cost of string transformation.
     */
    private int transformCost() {
        table = new int[x.length()+1][y.length()+1];
        trans = new TransElement[x.length()+1][y.length()+1];
        
        // Initialise Tables
        for(int i = 1; i <= x.length(); i++) {
            table[i][0] = i * TransCode.Delete.cost;
            trans[i][0] = new TransElement.DeleteElement();
        }
        for(int j = 1; j <= y.length(); j++) {
            table[0][j] = j * TransCode.Insert.cost;
            trans[0][j] = new TransElement.InsertElement(y.charAt(j - 1));
        }
        
        
        // Fill in tables: by row, left to right and top to bottom.
        for(int i = 1; i <= x.length(); i++) {
            for(int j = 1; j <= y.length(); j++) {
                
                int copy = Integer.MAX_VALUE; 
                int replace = Integer.MAX_VALUE;
                int swap = Integer.MAX_VALUE;

                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    // Copy Case
                    copy = table[i - 1][j - 1] + TransCode.Copy.cost;
                } else {
                    // Replace Case
                    replace = table[i - 1][j - 1] + TransCode.Replace.cost;
                }
                
                if (i > 1 && j > 1 
                        && x.charAt(i - 1) == y.charAt(j - 2)
                        && x.charAt(i - 2) == y.charAt(j - 1)) {
                    // Swap Case
                    swap = table[i - 2][j - 2] + TransCode.Swap.cost;
                }
                
                // Always Consider Delete, and Insert Cases
                int delete = table[i - 1][j] + TransCode.Delete.cost;
                int insert = table[i][j - 1] + TransCode.Insert.cost;
                

                table[i][j] = min(copy, replace, swap, delete, insert);
                if (table[i][j] == copy) {
                    trans[i][j] = new TransElement.CopyElement();
                } else if (table[i][j] == replace) {
                    trans[i][j] = new TransElement.ReplaceElement(y.charAt(j - 1));
                } else if (table[i][j] == swap) {
                    trans[i][j] = new TransElement.SwapElement();
                } else if (table[i][j] == delete) {
                    trans[i][j] = new TransElement.DeleteElement();
                } else {
                    trans[i][j] = new TransElement.InsertElement(y.charAt(j - 1));
                }
            }
        }
        
        // Find location of any potential Kill Elements.
        // Must be the last operation.
        for (int i = 0; i < x.length() - 2; i++) {
            if (table[i][y.length()] + TransCode.Kill.cost 
                    < table[x.length()][y.length()]) {
                table[x.length()][y.length()] = table[i][y.length()] 
                        + TransCode.Kill.cost;
                
                trans[x.length()][y.length()] = new TransElement.KillElement();
                iKill = i;
            }
        }
        
        // Total cost is the last element in the 2D array 
        return table[x.length()][y.length()]; 
    }
    
    
    /**
     * @requires that the stringTransformation function has been called to 
     *  calculate the minimum cost and set up the matrices from which the 
     *  list of transformations is extracted
     * @return list of transformations to convert x to y
     */
    public List<TransElement> getTransList() {
        List<TransElement> transList = new ArrayList<TransElement>();
        
        // No need to repeatedly build the transformation list, it won't change
        if (transList.isEmpty()) {
            getSequence(transList, x.length(), y.length());
        }
        
        return transList;
    }
    
    /**
     * Recursively build the transformation list to solve the 
     *  transformation with the minimal cost.
     * @param transList List of Transformation to be built
     * @param i array index
     * @param j array index
     */
    private void getSequence(List<TransElement> transList, int i, int j) {
        int iNext = i;  // Next value of i in recursive call
        int jNext = j;  // Next value of j in recursive call
        
        // Sequence is complete when i and j both equal 0
        if (i == 0 && j == 0) return;
        
        // Check the type of TransElement at the location given by (i, j)
        // Reduce iNext and jNext respectively 
        if (trans[i][j] instanceof TransElement.KillElement) {
            iNext = iKill;
        } else if (trans[i][j] instanceof TransElement.DeleteElement) {
            iNext = i - 1;
        } else if(trans[i][j] instanceof TransElement.SwapElement) {
            iNext = i - 2;
            jNext = j - 2;
        } else if (trans[i][j] instanceof TransElement.InsertElement) {
            jNext = j - 1;
        } else {
            // Must be either Copy or Replace - both have same effect on i, j
            iNext = i - 1;
            jNext = j - 1;
        }
        
        getSequence(transList, iNext, jNext);
        
        // Add current Element to Transformation List
        transList.add(trans[i][j]);
    }
    
    /**
     * Calculate the minimum value of a list of integers 
     * @param numbers integers
     * @return minimum of numbers
     */
    private static int min(int ... numbers) {
        int min = Integer.MAX_VALUE;
        for (int number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }
}
