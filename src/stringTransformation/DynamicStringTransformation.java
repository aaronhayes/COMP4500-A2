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
    
    public int stringTransformation() {
        numberOfIterations = 0;
        
        initiliseTables();
        for (int i = 0; i < x.length(); i++) {
            for (int j = 0; j < y.length(); j++) {
                System.out.print(trans[i][j] + ", ");
            }
            System.out.println();
        }
        
        for (int i = 1; i < x.length(); i++) {
            for (int j = 1; j < y.length(); j++) {
                
                table[i][j] = Integer.MAX_VALUE;
                
                System.out.println(i + ", " +j);
                System.out.println(x.length() + ", " + y.length());
                
                if (x.charAt(i) == y.charAt(j)) {
                    table[i][j] = table[i - 1][j - 1] + TransCode.Copy.cost;
                    trans[i][j] = new TransElement.CopyElement();
                }
                
                if (x.charAt(i) != y.charAt(j) && table[i - 1][j - 1] + TransCode.Replace.cost < table[i][j]) {
                    table[i][j] = table[i - 1][j - 1] + TransCode.Replace.cost;
                    trans[i][j] = new TransElement.ReplaceElement(y.charAt(j));
                }
                
                if (i >= 1 && j >= 1 
                        && x.charAt(i) == y.charAt(j - 1) 
                        && x.charAt(i - 1) == y.charAt(j) 
                        && table[i - 1][j - 1] + TransCode.Swap.cost < table[i][j]) {
                    table[i][j] = table[i - 1][j - 1] + TransCode.Swap.cost;
                    trans[i][j] = new TransElement.SwapElement();
                }
                
                if (table[i - 1][j] + TransCode.Delete.cost < table[i][j]) {
                    table[i][j] = table[i - 1][j] + TransCode.Delete.cost;
                    trans[i][j] = new TransElement.DeleteElement();
                }
                
                if (table[i][j - 1] + TransCode.Insert.cost < table[i][j]) {
                    table[i][j] = table[i][j - 1] + TransCode.Insert.cost;
                    trans[i][j] = new TransElement.InsertElement(y.charAt(j));
                }     
                
            }
        }
        
        
        for (int i = 0; i < x.length() - 2; i++) {
            if (table[i][y.length() - 1] + TransCode.Kill.cost < table[x.length() - 1][y.length() - 1]) {
                table[x.length() - 1][y.length() - 1] = table[i][y.length() - 1] + TransCode.Kill.cost;
                trans[x.length() - 1][y.length() - 1] = new TransElement.KillElement();
            }
        }
        
        for (int i = 0; i < x.length(); i++) {
            for (int j = 0; j < y.length(); j++) {
                System.out.print(trans[i][j] + ", ");
            }
            System.out.println();
        }
        
        return numberOfIterations;
    }
    
    /**
     * 
     */
    private void initiliseTables() {
        
        table = new int[x.length()][y.length()];
        trans = new TransElement[x.length()][y.length()];
                                
        
        for (int i = 0; i < x.length(); i++) {
            table[i][0] = i * TransCode.Delete.cost;
            trans[i][0] = new TransElement.DeleteElement();
        }
        
        for (int j = 0; j < y.length(); j++) {
            table[0][j] = j * TransCode.Insert.cost;
            trans[0][j] = new TransElement.InsertElement(y.charAt(j));
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
        getSequence(transList, x.length() - 1, y.length() - 1);
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
        if (i == 0 && j == 0) return;
        
        if (i < 0 || j < 0) return;
        
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
            iNext = x.length() - 2;
            jNext = j;
        }
        
        getSequence(transList, iNext, jNext);
        transList.add(trans[i][j]);
    }
}
