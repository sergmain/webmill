package org.riverock.webmill.test;

import junit.framework.TestCase;

/**
 * User: SergeMaslyukov
 * Date: 19.12.2004
 * Time: 13:46:46
 * $Id$
 */
public class TestWriterWithRussian extends TestCase {

    private static byte[] bytes = new byte[]{
        (byte)0xD0, (byte)0xA2, (byte)0xD0, (byte)0xB5, 
        (byte)0xD1, (byte)0x81, (byte)0xD1, (byte)0x82,
        (byte)0x20, (byte)0xD0, (byte)0x98, (byte)0xD0,
        (byte)0x98, (byte)0xD0, (byte)0x98};

    public void testConvertSequenceWithRussianChars() throws Exception {

        System.out.println( "version: "+System.getProperty( "java.runtime.version" ) );

        String utf8 = new String( bytes, "utf-8" );

        System.out.println( "new String(bytes) = " + new String( bytes ) );
        System.out.println( "new String(bytes) = " + new String( bytes, "Cp1251" ) );
        System.out.println( "new String(bytes) = " + utf8 );

        byte[] b = utf8.getBytes("utf8");

        if (b.length!=bytes.length) {
            throw new Exception( "Size of array not equals" );
        }
        for (int i=0; i<b.length; i++) {
            if (b[i]!=bytes[i]) {
                throw new Exception( "byte at index "+i+" not equals, o: "+bytes[i]+", n: "+b[i] );
            }
        }
    }
}
