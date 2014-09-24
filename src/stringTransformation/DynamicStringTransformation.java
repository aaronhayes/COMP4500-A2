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
     * 
     * @return
     */
    private int transformCost() {
        table = new int[x.length()+1][y.length()+1];
        trans = new TransElement[x.length()+1][y.length()+1];
        
        for(int i = 0; i <= x.length(); i++) {
            table[i][0] = i * TransCode.Delete.cost;
            trans[i][0] = new TransElement.DeleteElement();
        }
        
        for(int j = 1 ; j <= y.length(); j++) {
            table[0][j] = j * TransCode.Insert.cost;
            if (j < y.length())
            trans[0][j] = new TransElement.InsertElement(y.charAt(j));
        }
        
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
        
        
        for (int i = 0; i < x.length() - 2; i++) {
            if (table[i][y.length()] + TransCode.Kill.cost < table[x.length()][y.length()]) {
                table[x.length()][y.length()] = table[i][y.length()] + TransCode.Kill.cost;
                trans[x.length()][y.length()] = new TransElement.KillElement();
                iKill = i;
            }
        }
        
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
        getSequence(transList, x.length(), y.length());
        return transList;
    }
    
    /**
     * 
     * @param transList
     * @param i
     * @param j
     */
    private void getSequence(List<TransElement> transList, int i, int j) {
        int iNext, jNext;
        if (i <= 0 && j <= 0) return;
        if (i < 0 || j < 0) return;
        
        //System.out.println(i + "," + j + ":" + trans[i][j]);
        if (trans[i][j] instanceof TransElement.CopyElement || 
                trans[i][j] instanceof TransElement.ReplaceElement) {
            iNext = i - 1;
            jNext = j - 1;
        } else if (trans[i][j] instanceof TransElement.SwapElement) {
            iNext = i - 2;
            jNext = j - 2;
        } else if (trans[i][j] instanceof TransElement.DeleteElement) {
            iNext = i - 1;
            jNext = j;
        } else if (trans[i][j] instanceof TransElement.InsertElement) {
            iNext = i;
            jNext = j - 1;
        } else {
            // KILL Element
            iNext = iKill;
            jNext = j;
        }
        
        getSequence(transList, iNext, jNext);
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
