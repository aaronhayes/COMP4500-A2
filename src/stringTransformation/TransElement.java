package stringTransformation;

public abstract class TransElement {
    
    /** Codes for each of the individual transformations.
     * Each as an associated cost.
     * The costs may be varied for testing purposes.
     */
    public enum TransCode {
        Delete( 2 ), 
        Replace( 3 ), 
        Copy( 1 ), 
        Insert( 2 ), 
        Swap( 2 ), 
        Kill( 5 );
        
        int cost;
        
        private TransCode( int cost ) {
            this.cost = cost;
        }
        public int getCost() {
            return cost;
        }
    }
    
    public TransElement() {
        super();
    }
    
    /** Return the corresponding element of TransCode */
    public abstract TransCode getCode();
    
    /** Apply a single element of a transformation to the input string x
     * to (possibly) extend the transformed string z 
     * @param x input string being transformed
     * @param i number of characters in the input string already consumed
     * @param z transformed string
     * @return number of characters in the input string consumed after
     * application of this transformation
     */
    public abstract int apply( String x, int i, StringBuffer z );
    
    /** Each type of transformation has a corresponding subclass of TransElement
     * representing the transformation.
     * Each supplies an implementation of the apply method (see above)
     * specific to the particular transformation.
     */
    public static class DeleteElement extends TransElement {
        
        public DeleteElement() {
            super();
        }
        @Override
        public TransCode getCode() {
            return TransCode.Delete;
        }
        /** String z is unchanged
         * @requires current position i in the source string is before its end
         * @return current position plus 1 to delete the current character 
         *         in source
         */
        @Override
        public int apply( String x, int i, StringBuffer z ) {
            assert i < x.length();
            return i+1;
        }
        @Override
        public String toString() {
            return "Delete";
        }
    }

    public static class ReplaceElement extends TransElement {
        public char ch; // replacement character
        
        public ReplaceElement( char ch ) {
            super();
            this.ch = ch;
        }
        @Override
        public TransCode getCode() {
            return TransCode.Replace;
        }
        /** Replacement character ch is appended to target string
         * @requires current position i in source string is before its end
         * @return current position plus 1 to delete the replaced character
         */
        @Override
        public int apply( String x, int i, StringBuffer z ) {
            assert i < x.length();
            z.append(ch);
            return i + 1;
        }
        @Override
        public String toString() {
            return "Replace(" + ch + ")";
        }
    }
    public static class CopyElement extends TransElement {
         
        public CopyElement() {
            super();
        }
        @Override
        public TransCode getCode() {
            return TransCode.Copy;
        }
        /** The current character in the source is appended to the target
         * @requires current position i in the source string is before its end
         * @return current position plus 1 to consume the current character
         */
        @Override
        public int apply( String x, int i, StringBuffer z ) {
            assert i < x.length();
            z.append(x.charAt(i));
            return i + 1;
        }
        @Override
        public String toString() {
            return "Copy";
        }
    }
    public static class InsertElement extends TransElement {
        public char ch; // character to be inserted
        
        public InsertElement( char ch ) {
            super();
            this.ch = ch;
        }
        @Override
        public TransCode getCode() {
            return TransCode.Insert;
        }
        /** Append the inserted character to the end of target 
         * @requires i is a valid position in source - could be at the end
         * @return current position unchanged as insert does not consume 
         *         any characters
         */
        @Override
        public int apply( String x, int i, StringBuffer z ) {
            assert i <= x.length();
            z.append(ch);
            return i;
        }
        @Override
        public String toString() {
            return "Insert(" + ch + ")";
        }
    }
    public static class SwapElement extends TransElement {
         
        public SwapElement() {
            super();
        }
        @Override
        public TransCode getCode() {
            return TransCode.Swap;
        }
        /** Append characters i+1 and i of source to target in that order
         * @requires that there are at least two characters left in source
         * @return current position plus 2 as two characters are consumed
         */
        @Override
        public int apply( String x, int i, StringBuffer z ) {
            assert i+1 < x.length();
            z.append(x.charAt(i+1));
            z.append(x.charAt(i));
            return i + 2;
        }
        @Override
        public String toString() {
            return "Swap";
        }
    }
    public static class KillElement extends TransElement {
         
        public KillElement() {
            super();
        }
        @Override
        public TransCode getCode() {
            return TransCode.Kill;
        }
        /** Consume all remaining characters of source
         * @requires current position is before the end of source
         * @return length of source because all its characters are consumed
         */
        @Override
        public int apply( String x, int i, StringBuffer z ) {
            assert i < x.length();
            return x.length();
        }
        @Override
        public String toString() {
            return "Kill";
        }
    }
}
