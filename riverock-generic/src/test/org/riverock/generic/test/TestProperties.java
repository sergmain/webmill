/*

 * org.riverock.generic -- Database connectivity classes

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * Lesser General Public License for more details.

 *

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



/**

 * Author: mill

 * Date: Jan 24, 2003

 * Time: 10:29:07 AM

 *

 * $Id$

 */



package org.riverock.generic.test;



import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.StringTools;

import org.apache.log4j.Logger;



import java.sql.Timestamp;

import java.util.Enumeration;

import java.util.Properties;



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

