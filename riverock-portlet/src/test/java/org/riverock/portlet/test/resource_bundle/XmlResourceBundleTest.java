/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
