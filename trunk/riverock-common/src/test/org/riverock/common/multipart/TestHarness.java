/*

 * org.riverock.common -- Supporting classes, interfaces, and utilities

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

 * 2003. Copyright (c) jSmithy. http:// multipart.jSmithy.com

 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/

 *

 * $Id$

 */



package org.riverock.common.multipart;



import org.apache.log4j.Category;



import java.io.File;

import java.io.FileInputStream;

import java.util.Enumeration;

import java.util.Hashtable;



public class TestHarness

{

    private static Category cat = Category.getInstance("org.riverock.multipart.TestHarness");



    private static MultipartHandler test;



    public static void main(String argv[])

    {

        Hashtable values = new Hashtable();



        try

        {

            File inData = new File("post-dump-22300.dat");

            FileInputStream in = new FileInputStream( inData );

            test = new MultipartHandler(

                values, in,

                100000,

                "multipart/form-data; boundary=---------------------------7d32f72320164",

                new File("result"),

                false,

                false,

                true

            );



//            test = new MultipartHandler(values, System.in, 1000000,

//                argv[0], new File("/tmp"));



            System.out.println("isSaveUploadedFilesToDisk - "+test.isSaveUploadedFilesToDisk());



            Enumeration e = values.keys();

            while (e.hasMoreElements())

            {

                String key = (String) e.nextElement();

                Object obj = test.getValues().get(key);

                System.out.println(":: " + key + " ::-> " +

                    obj.toString()+" ("+ obj.getClass().getName()+")");

            }



            e = test.getPartsHash().keys();

            while (e.hasMoreElements())

            {

                String key = (String) e.nextElement();

                Object obj = test.getPartsHash().get(key);

                System.out.println(":: " + key + " ::-> " +

                    obj.toString()+" ("+ obj.getClass().getName()+")");

            }

        }

        catch (Exception ioioe)

        {

            System.err.println( ioioe.toString() );

        }

    }

}

