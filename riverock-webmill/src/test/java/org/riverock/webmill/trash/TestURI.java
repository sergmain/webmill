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

/**
 * User: Admin
 * Date: Sep 14, 2003
 * Time: 10:00:05 PM
 *
 * $Id: TestURI.java 1243 2007-07-12 16:58:42Z serg_main $
 */
package org.riverock.webmill.trash;

import java.net.URL;

public class TestURI
{
    private static String addr = "file:/d:\\";

    public static void main(String args[])
        throws Exception
    {
        URL url = new URL(addr);
        System.out.println( url.toString());

        Object obj = url.getContent();
//        url.

        System.out.println( obj.toString() );
//        sun.net.www.content.text.PlainTextInputStream a;
    }

}
