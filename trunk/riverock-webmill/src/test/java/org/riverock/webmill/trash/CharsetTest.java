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
package org.riverock.webmill.trash;

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
