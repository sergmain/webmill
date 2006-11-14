/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.test.resource_bundle;

import java.util.Properties;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * @author SergeMaslyukov
 *         Date: 01.12.2005
 *         Time: 16:09:56
 *         $Id$
 */
public class XmlResourceBundleTest {

    public static void main( String[] args ) throws Exception {
        Properties properties = new Properties( );

        properties.setProperty( "aaa", "»»»" );
        properties.setProperty( "aaa1", "»»»" );
        properties.setProperty( "aaa2", "»»»" );
        properties.setProperty( "aaa3", "»»»" );

        OutputStream stream = new FileOutputStream( "test.xml" );
        properties.storeToXML( stream, "test xml resource" );
        properties = null;
        stream.close();
    }
}
