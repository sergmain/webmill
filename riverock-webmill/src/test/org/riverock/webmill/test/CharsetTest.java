package org.riverock.webmill.test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * User: SergeMaslyukov
 * Date: 31.12.2004
 * Time: 13:01:44
 * $Id$
 */
public class CharsetTest {

    public static void main( String args[] ) {

        test( "utf8" );

        test( "utf-8" );
        test( "windows-1251" );
        test( "Cp1251" );
//        test( "Ms1251" );

    }

    private static void test( String s ) {
        Charset charset = null;
        charset = Charset.forName( s );
        System.out.print( "charset = " + charset + " " );
        System.out.print( "charset.displayName() = " + charset.displayName() + " " );
        System.out.println( "charset.name() = " + charset.name() );
        CharsetDecoder decoder = charset.newDecoder();
    }
}
