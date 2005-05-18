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
package org.riverock.common.tools;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

/**
 * $Id$
 */
public final class MainTools
{
    private final static Logger log = Logger.getLogger( MainTools.class );

    public static void putKey( final Map map, final Object key, final Object value )
    {
        Object obj = map.get( key );
        if (obj==null)
        {
            map.put( key, value );
            return;
        }

        if (obj instanceof List)
        {
            if (value instanceof List)
                ((List)obj).addAll((List)value);
            else
                ((List)obj).add( value );
        }
        else
        {
            List v = new LinkedList();
            v.add(obj);

            if (value instanceof List)
                v.addAll( (List)value );
            else
                v.add( value );

            map.remove( key );
            map.put( key, v );
        }
    }

    public static boolean compare( final byte[] array1, final byte[] array2 )
    {
        if (array1==null && array2==null)
            return true;

        if (array1==null && array2!=null)
            return false;

        if (array1!=null && array2==null)
            return false;

        if (array1.length!=array2.length)
        {
            System.out.println("diff length, array1 "+array1.length+", array2 "+array2.length);
            return false;
        }

        for (int i=0; i<array1.length; i++)
        {
            if (array1[i]!=array2[i])
            {
                System.out.println("diff byte at "+i);
                return false;
            }
        }
        return true;
    }

    public static int indexOf( final byte bytes[], final byte searchByte )
    {
        if (bytes==null || bytes.length==0)
            return -1;

        for (int i=0; i<bytes.length; i++)
        {
            if (bytes[i]==searchByte)
                return i;
        }
        return -1;
    }

    public static byte[] getBytes( final byte bytes[], final int offset )
    {
        if (bytes == null || bytes.length<offset)
            return null;

        byte b[] = new byte[bytes.length - offset];
        for (int i=0; i<b.length; i++)
            b[i] = bytes[offset+i];

        return b;
    }

    public static String writeToFile( final String full_file_name, final byte bytes[] )
            throws Exception
    {

        File file_ = new File(full_file_name);
        file_.delete();
        file_ = null;

        FileOutputStream out = new FileOutputStream(full_file_name, true);

        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();

        out = null;

        return full_file_name + " создан успешно";
    }

    public static boolean deleteFile( final String fileName )
    {
        return deleteFile(new File(fileName));
    }

    public static boolean deleteFile( final File file_ )
    {
        if (file_.exists())
        {
            return file_.delete();
        }

        return true;
    }

    public static Object createCustomObject( final String s )
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Object obj = null;
        try
        {
            if (log.isDebugEnabled())
                log.debug("Starting create class object for name '" + s + "'");

            if (s == null)
                return null;

            Class className = null;

            if (log.isDebugEnabled())
                log.debug("Create class for name '" + s + "'");

            className = Class.forName(s);

            if (log.isDebugEnabled())
                log.debug("Class for name '" + s + "' is " + className);

            if (className == null)
                throw new ClassNotFoundException("Error create class for name " + s);

            if (log.isDebugEnabled())
                log.debug("Create object for name '" + s + "'");

            obj = className.newInstance();

            if (log.isDebugEnabled())
                log.debug("Object for name '" + s + "' is " + obj);
        }
        catch(ClassNotFoundException e)
        {
            log.error("Error create reflection object for class name '"+s+"'", e);
            throw e;
        }
        catch(InstantiationException e)
        {
            log.error("Error create reflection object for class name '"+s+"'", e);
            throw e;
        }
        catch(IllegalAccessException e)
        {
            log.error("Error create reflection object for class name '"+s+"'", e);
            throw e;
        }
        return obj;
    }

    public static Locale RUlocale()
    {
        return new Locale("ru", "RU");
    }

    /**
     * @deprecated use StringTools.getLocale()
     */
    public static Locale getLocale( final String locale ) {
        return StringTools.getLocale( locale );
    }

    /**
     * Wrapper for Runtime.getRuntime().maxMemory()
     * in JDK 1.3 this method not implemented
     * @return
     */
    public static Long getMaxMemory()
    {
        Long maxMemory = null;
        try
        {
            Method method1 = Runtime.getRuntime().getClass().getMethod("maxMemory", null);
            if (method1 != null)
            {
                Object obj = method1.invoke(Runtime.getRuntime(), null);
                if (log.isDebugEnabled())
                    log.debug("return value - "+obj);
                maxMemory = (Long)obj;
            }
        }
        catch(Exception exc)
        {
            log.warn("Exception in getMaxMemory()", exc);
        }
        return maxMemory;
    }
}
