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
 */
package org.riverock.generic.test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import org.riverock.generic.main.ExtensionFileFilter;

/**
 * Author: mill
 * Date: Apr 9, 2003
 * Time: 10:05:38 AM
 *
 * $Id$
 */
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

