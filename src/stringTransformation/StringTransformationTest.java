package stringTransformation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class StringTransformationTest {
    
    /** Apply a list of transformations trList to a string x to get the 
     * resultant string.
     * @param x string to be transformed
     * @param trList list of transformations to be applied
     * @return string resulting from applying trList to x
     */
    private static String applyTransformations( String x, 
    		List<TransElement> trList ) {
        int i = 0;    // Index into string x
        StringBuffer z = new StringBuffer(); // transformed string
        for( TransElement tr : trList ) {
            i = tr.apply(x, i, z);
        }
        assert i == x.length();  // Must reach the end of x
        return z.toString();
    }
    
    private static void runTest( String x, String y ) {
        RecursiveStringTransformation recursive = 
                new RecursiveStringTransformation( x, y );

        System.out.println( "Transformation cost of '" + x + "' to '" + y + "'" );
        System.out.println( "Recursive algorithm gives " + 
                recursive.stringTransformation() );
        //System.out.println( "Number of calls " + recursive.numberOfCalls );
                
        DynamicStringTransformation dynamic = 
                new DynamicStringTransformation( x, y );
        System.out.println( "Dynamic programming algorithm gives " + 
                dynamic.stringTransformation() );
        //System.out.println( "Number of iterations of inner loop " + 
        //      dynamic.numberOfIterations );
        
        String z = applyTransformations( x, dynamic.getTransList() );
        System.out.println( "Transformed string = '" + z + "'" );
        if( !z.equals(y) ) {
            System.out.println( "Transformation does not reconstruct target string" );
        }
        System.out.println( dynamic.getTransList() );
    }
    
    private static String readString( BufferedReader input ) 
            throws IOException {
        String s = input.readLine();
        if( s == null) {
            System.out.println( " Missing input string" );
        }
        return s;
    }
    
    public static void main(String[] args) throws IOException {
        /** Buffered reader for input source file */
        InputStream input;
        if( args.length == 0 ) {
            input = System.in;
        } else {
            System.out.println( "File '" + args[0] + "'" );
            input = new FileInputStream( args[0] );
        }
        BufferedReader inputBuffer = 
                new BufferedReader( new InputStreamReader( input ) );
        String x = readString( inputBuffer );   
        String y = readString( inputBuffer );
        runTest( x, y );
    }
}
