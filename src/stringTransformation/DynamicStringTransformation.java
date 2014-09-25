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
    private int iKill;  // Location of Kill Transformation
    
    public int stringTransformation() {
        numberOfIterations = transformCost();
        return numberOfIterations;
    }
    
    /**
     * Calculate the Minimum Transformation Cost using a bottom-up
     *  dynamic approach. Runs in O(M * N) time.
     *  Use i, j as index variables to both strings x, y respectively. 
     *   i.e. table[i][j] is the cost of an operation to transform X.charAt(i)
     *   into Y.charAt(j). The transformation type of that cost is trans[i][j].
     *   This is used consistently throughout this file.
     *       
     * @return minimum cost of string transformation.
     */
    private int transformCost() {
        initialiseArrays();
        
        // Fill in tables: by row, left to right and top to bottom.
        // Note that i = j = 1 initially: index of chars must be -1 from i,j.
        for(int i = 1; i <= x.length(); i++) {
            for(int j = 1; j <= y.length(); j++) {
                
                TransElement tran;
                int minCost;
                
                // Consider either Copy or Replace Transformations
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    // Copy Case
                    minCost = table[i - 1][j - 1] + TransCode.Copy.cost;
                    tran = new TransElement.CopyElement();
                } else {
                    // Replace Case
                    minCost = table[i - 1][j - 1] + TransCode.Replace.cost;
                    tran = new TransElement.ReplaceElement(y.charAt(j - 1));
                }
                
                // Must consider Swap Transformation
                if (i > 1 && j > 1 
                        && x.charAt(i - 1) == y.charAt(j - 2)
                        && x.charAt(i - 2) == y.charAt(j - 1)
                        && (table[i - 2][j - 2] + TransCode.Swap.cost 
                                < minCost)) {
                    // Swap Case
                    minCost = table[i - 2][j - 2] + TransCode.Swap.cost;
                    tran = new TransElement.SwapElement();
                }
                
                // Always Consider Delete, and Insert Cases
                if (table[i - 1][j] + TransCode.Delete.cost < minCost) {
                    // Delete Case
                    minCost = table[i - 1][j] + TransCode.Delete.cost;
                    tran = new TransElement.DeleteElement();
                }
                if (table[i][j - 1] + TransCode.Insert.cost < minCost) {
                    // Insert Case
                    minCost = table[i][j - 1] + TransCode.Insert.cost;
                    tran = new TransElement.InsertElement(y.charAt(j - 1));
                }

                // Set current element in Arrays to correct values
                table[i][j] = minCost;
                trans[i][j] = tran;
            }
        }
        
        findKillTransformation();
        
        // Total cost is the last element in the 2D array 
        return table[x.length()][y.length()]; 
    }
    
    /**
     * Initialise the two arrays (table and trans).
     * @ensure table/trans are initialised with the proper size
     *      and contain costs/transformations for deleting/inserting chars in
     *      strings x and y respectively. 
     */
    private void initialiseArrays() {
        // Create arrays with correct size
        table = new int[x.length() + 1][y.length() + 1];
        trans = new TransElement[x.length() + 1][y.length() + 1];
        
        // Add initial values into arrays
        // That is delete and insert each char of respective strings
        for(int i = 1; i <= x.length(); i++) {
            // Add cost/transformation to delete each char in the x string
            table[i][0] = i * TransCode.Delete.cost;
            trans[i][0] = new TransElement.DeleteElement();
        }
        
        for(int j = 1; j <= y.length(); j++) {
            // Add cost/transformation to insert each char in the y string
            table[0][j] = j * TransCode.Insert.cost;
            trans[0][j] = new TransElement.InsertElement(y.charAt(j - 1));
        }
    }
    
    /**
     * Find the location of a potential kill element.
     *  It must be the last operation, and after the y string has been
     *      completely traversed. Hence j = y.length().
     *  @ensure iKill to be the i location of the kill element if it exists.
     */
    private void findKillTransformation() {
        for (int i = 0; i < x.length() - 2; i++) {
            if (table[i][y.length()] + TransCode.Kill.cost 
                    < table[x.length()][y.length()]) {
                
                table[x.length()][y.length()] = table[i][y.length()] 
                        + TransCode.Kill.cost;
                trans[x.length()][y.length()] = new TransElement.KillElement();
                iKill = i;
            }
        }
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
}
