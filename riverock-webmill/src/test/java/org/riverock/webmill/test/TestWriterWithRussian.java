/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.test;

import junit.framework.TestCase;

/**
 * User: SergeMaslyukov
 * Date: 19.12.2004
 * Time: 13:46:46
 * $Id: TestWriterWithRussian.java 1416 2007-09-08 18:12:27Z serg_main $
 */
public class TestWriterWithRussian extends TestCase {

    private static byte[] bytes = new byte[]{
        (byte)0xD0, (byte)0xA2, (byte)0xD0, (byte)0xB5, 
        (byte)0xD1, (byte)0x81, (byte)0xD1, (byte)0x82,
        (byte)0x20, (byte)0xD0, (byte)0x98, (byte)0xD0,
        (byte)0x98, (byte)0xD0, (byte)0x98};

    public void testConvertSequenceWithRussianChars() throws Exception {

//        System.out.println( "version: "+System.getProperty( "java.runtime.version" ) );

        String utf8 = new String( bytes, "utf-8" );

/*
        System.out.println( "new String(bytes) = " + new String( bytes ) );
        System.out.println( "new String(bytes) = " + new String( bytes, "Cp1251" ) );
        System.out.println( "new String(bytes) = " + utf8 );
*/

        byte[] b = utf8.getBytes("utf8");

        assertEquals("Size of array not equals", b.length, bytes.length);

        for (int i=0; i<b.length; i++) {
            assertEquals(
                "byte at index "+i+" not equals, o: "+bytes[i]+", n: "+b[i],
                b[i], bytes[i]
            );
        }
    }
}
