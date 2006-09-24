/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.test;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.StringTools;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Author: mill
 * Date: Jan 24, 2003
 * Time: 10:29:07 AM
 *
 * $Id$
 */
public class TestProperties
{
    private static Logger cat = Logger.getLogger("org.riverock.test.TestProperties");

    public TestProperties()
    {
    }

    public static void main(String args[])
        throws Exception
    {

        org.riverock.generic.startup.StartupApplication.init();

        Timestamp t = DateTools.getCurrentTime();
        Properties prop = System.getProperties();

        Enumeration e = prop.keys();
        for (; e.hasMoreElements() ;) {

            String s =(String) e.nextElement();
            String s1 = (String)prop.get( s );
            System.out.println("Prop: "+s+", value - "+ s1 );
        }

        System.out.println( System.getProperty("java.io.tmpdir"));
        System.out.println( System.getProperty("file.encoding"));

        String i = "»»» “ÂÒÚ";

        System.out.println(
            StringTools.convertString(i,
                "utf-8",
                System.getProperty("file.encoding")
            )
        );

        byte b[] = i.getBytes();
        char c[] = new char[b.length];

        for (int j=0; j<b.length; j++)
            c[j] = (char)b[j];
//        i.
        System.out.println( c );

    }

}
