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

package org.riverock.portlet.test;

import org.riverock.common.tools.MainTools;
import org.riverock.portlet.tools.SiteUtils;


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
        String file_name = SiteUtils.getTempDir()+"Test.constructor.log";

        OutputStreamWriter prn_ = new OutputStreamWriter(
            new FileOutputStream(file_name, true)
            , "Cp1251");

        String ss = "Instance is: " + this + "\n";
        prn_.write(ss, 0, ss.length());
        prn_.flush();
        prn_.close();
        prn_ = null;

    }
}
