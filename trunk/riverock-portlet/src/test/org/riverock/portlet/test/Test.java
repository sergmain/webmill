/*

 * org.riverock.portlet -- Portlet Library

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

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

 *

 */



package org.riverock.portlet.test;



import org.riverock.common.tools.MainTools;

import org.riverock.webmill.config.WebmillConfig;





import java.io.OutputStreamWriter;

import java.io.FileOutputStream;

import java.lang.reflect.Method;

import java.util.TreeSet;

import java.util.Iterator;

import java.util.Hashtable;

import java.util.Enumeration;



public class Test

{

    private static Test test = null;



    public static void main(String argsValue[])

        throws Exception

    {

        TreeSet set = new TreeSet();



        set.add(new Long(10));

        set.add(new Long(1));

        set.add(new Long(15));

        set.add(new Long(20));

        set.add(new Long(15));



        Iterator it = set.iterator();

        while (it.hasNext())

        {

            System.out.println("long = "+it.next());

        }





        Hashtable hash = new Hashtable();



        hash.put(new Long(10), new Long(10));

        hash.put(new Long(1), new Long(10));

        hash.put(new Long(15), new Long(10));

        hash.put(new Long(20), new Long(10));

        hash.put(new Long(15), new Long(10));



        System.out.println();

        for (Enumeration e = hash.keys() ; e.hasMoreElements() ;) {

           System.out.println("Long = " + e.nextElement());

        }





        String className = "org.riverock.portlet.test.TestOption";

        Object obj = MainTools.createCustomObject( className );



        TestOption to = (TestOption)obj;

        to.setType("qqq");



        Method m[] = obj.getClass().getMethods();

        for (int i=0; i<m.length; i++)

        {

            System.out.println("Method - "+m[i].toString());

        }



        String setter = "setType";

        String type = "java.lang.String" ;

        Class[] cl = { Class.forName( type ) };

        Method method = obj.getClass().getMethod( setter, cl );



        if (method == null)

        {

            System.out.println( "Error create method '"+setter+"()' for class  " + className );

            throw new Exception("Error create method '"+setter+"()' for class  " + className );

        }



        Object[] objArgs = null;



        String stringValue = "test value";

        Object[] args = { stringValue };

        objArgs = args;

        obj = method.invoke(obj, args);





        String test = to.type;

    }



    public static Test getInstance()

        throws Exception

    {

        if (test == null)

            test = new Test();



        return test;

    }



    private Test()

        throws Exception

    {

        String file_name = WebmillConfig.getWebmillDebugDir()+"Test.constructor.log";



        OutputStreamWriter prn_ = new OutputStreamWriter(

            new FileOutputStream(file_name, true)

            , "Cp1251");



        String ss = "Instance is: " + this + "\n";

        prn_.write(ss, 0, ss.length());

        prn_.flush();

        prn_.close();

        prn_ = null;



    }





    protected void finalize() throws java.lang.Throwable

    {

        String file_name = WebmillConfig.getWebmillDebugDir()+"Test.finalize.log";



        OutputStreamWriter prn_ = new OutputStreamWriter(

            new FileOutputStream(file_name, true)

            , "Cp1251");



        String ss = "Instance is: " + this + "\n";

        prn_.write(ss, 0, ss.length());

        prn_.flush();

        prn_.close();

        prn_ = null;



        super.finalize();

    }



}

