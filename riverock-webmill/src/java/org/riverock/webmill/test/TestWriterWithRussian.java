package org.riverock.webmill.test;

/**
 * User: SergeMaslyukov
 * Date: 19.12.2004
 * Time: 13:46:46
 * $Id$
 */
public class TestWriterWithRussian {

//6D 65 3E D0 A2 D0 B5 D1 ≥ 81 D1 82 20 D0 98 D0 98   me>–¢–µ—Å—Ç –?–?
//D0 98 3C 2F 4D 6F 64 75 ≥ 6C 65 4E 61 6D 65 3E 3C   –?</ModuleName><

    private static byte[] bytes = new byte[]{
        (byte)0xD0, (byte)0xA2, (byte)0xD0, (byte)0xB5, 
        (byte)0xD1, (byte)0x81, (byte)0xD1, (byte)0x82,
        (byte)0x20, (byte)0xD0, (byte)0x98, (byte)0xD0,
        (byte)0x98, (byte)0xD0, (byte)0x98};

    public static void main(String args[]) throws Exception {

//        Properties properties = System.getProperties();
//
//        for (Enumeration e = properties.propertyNames(); e.hasMoreElements() ;) {
//            String key = (String)e.nextElement();
//            System.out.println(key+":  "+properties.get( key ));
//        }

        System.out.println( "version: "+System.getProperty( "java.runtime.version" ) );

        String utf8 = new String( bytes, "utf-8" );

        System.out.println( "new String(bytes) = " + new String( bytes ) );
        System.out.println( "new String(bytes) = " + new String( bytes, "Cp1251" ) );
        System.out.println( "new String(bytes) = " + utf8 );

        byte[] b = utf8.getBytes("utf8");

        if (b.length!=bytes.length) {
            System.out.println( "Size of array not equals" );
            return;
        }
        for (int i=0; i<b.length; i++) {
            if (b[i]!=bytes[i]) {
                System.out.println( "byte at index "+i+" not equals, o: "+bytes[i]+", n: "+b[i] );
                return;
            }
        }
    }
}
