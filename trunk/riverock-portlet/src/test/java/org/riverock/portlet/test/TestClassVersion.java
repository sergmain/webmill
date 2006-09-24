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

/**
 * Author: mill
 * Date: Apr 9, 2003
 * Time: 10:05:38 AM
 *
 * $Id$
 */

package org.riverock.portlet.test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

import org.riverock.generic.main.ExtensionFileFilter;

public class TestClassVersion
{
    public TestClassVersion()
    {
    }

    private static int BUFSIZE = 8192;

    private static Hashtable hash = null;

    private static void add( int version_ )
    {
        Integer version = new Integer(version_);
        Integer ver = (Integer)hash.get( version );
        if (ver != null)
            return;

        hash.put( version,  version);
    }

    public static void main(String args[])
        throws Exception
    {
//        if (args.length==0)
//            throw new IllegalArgumentException("need parameter");

        if (args.length==0 || args[0].startsWith("-d"))
        {
            File currentDir = new File(".");
            File currentList[] = currentDir.listFiles( new ExtensionFileFilter(".jar"));

            for (int i=0; i<currentList.length; i++)
                System.out.println("Version of "+currentList[i]+" - "+
                    procesClass(currentList[i])
                );

        }
        else
            System.out.println("version of "+args[0]+" - "+procesClass( new File(args[0])) );
    }

    private static String procesClass( File jarName  ) throws IOException
    {
        hash = new Hashtable();
        JarFile jar = new JarFile(jarName);

        for (Enumeration e = jar.entries(); e.hasMoreElements() ;)
        {
            JarEntry entry = (JarEntry)e.nextElement();
            if (!entry.isDirectory())
            {
                DataInputStream file =
                    new DataInputStream(
                        new BufferedInputStream(jar.getInputStream(entry),BUFSIZE)
                    );
/*
                ClassParser parser = new ClassParser( file, entry.getName());
                try
                {
                    JavaClass javaClass = parser.parse();
                    add( javaClass.getMajor() );
                }
                catch(java.io.EOFException e1)
                {
                }
                catch(ClassFormatError e1)
                {
                }
*/
            }
        }
        String foundVersion = "";
        boolean isFirst = true;
        for (Enumeration e = hash.keys(); e.hasMoreElements() ;)
        {
            Integer ver = (Integer)e.nextElement();
            if (isFirst)
                isFirst = false;
            else
                foundVersion += ", ";

            foundVersion += ver.intValue();
        }
        return foundVersion;
    }
}

