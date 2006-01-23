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

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;

/**
 * 
 * $Revision$
 * $Date$
 * $RCSfile$
 *
 */
public class StringLocaleManager
{

    /**
     * The ResourceBundle for this StringManager.
     */

    private ResourceBundle bundle;
    private Locale currentLocale;

    protected void finalize() throws Throwable
    {
        bundle = null;
        currentLocale = null;

        super.finalize();
    }

    /**
     * Creates a new StringManager for a given package. This is a
     * private method and all access to it is arbitrated by the
     * static getManager method call so that only one StringManager
     * per package will be created.
     *
     * @param packageName Name of package to create StringManager for.
     */

    private StringLocaleManager(String packageName, Locale loc)
    {
        currentLocale = loc;
        String bundleName = packageName + ".LocalStrings";
        bundle = ResourceBundle.getBundle(bundleName, loc);
    }

    public String getStr(String key)
            throws UnsupportedEncodingException
    {
        String s = getString(key);
        String charset = LocaleCharset.getCharset(currentLocale);

        return new String(s.getBytes("8859_1"), charset);
    }

    public String getStr(String key, Object[] args)
            throws UnsupportedEncodingException
    {
        String s = getString(key, args);
        String charset = LocaleCharset.getCharset(currentLocale);

        return new String(s.getBytes("8859_1"), charset);
    }


    /**
     * Get a string from the underlying resource bundle.
     *
     * @param key
     */
    public String getString(String key)
    {
        if (key == null)
        {
            String msg = "key is null";

            throw new NullPointerException(msg);
        }

        String str = null;

        try
        {
            str = bundle.getString(key);
        }
        catch (MissingResourceException mre)
        {
            str = "cannot find message associated with key : " + key;
            mre.printStackTrace();
        }

        return str;
    }

    /**
     * Get a string from the underlying resource bundle and format
     * it with the given set of arguments.
     *
     * @param key
     * @param args
     */

    public String getString(String key, Object[] args)
    {
        String iString = null;
        String value = getString(key);

        // this check for the runtime exception is some pre 1.1.6
        // VM's don't do an automatic toString() on the passed in
        // objects and barf out

        try
        {
            // ensure the arguments are not null so pre 1.2 VM's don't barf
            Object nonNullArgs[] = args;
            for (int i = 0; i < args.length; i++)
            {
                if (args[i] == null)
                {
                    if (nonNullArgs == args) nonNullArgs = (Object[]) args.clone();
                    nonNullArgs[i] = "null";
                }
            }

            iString = MessageFormat.format(value, nonNullArgs);
        }
        catch (IllegalArgumentException iae)
        {
            StringBuilder buf = new StringBuilder();
            buf.append(value);
            for (int i = 0; i < args.length; i++)
            {
                buf.append(" arg[" + i + "]=" + args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object argument. This argument can of course be
     * a String object.
     *
     * @param key
     * @param arg
     */

    public String getString(String key, Object arg)
    {
        Object[] args = new Object[]{arg};
        return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. These arguments can of course
     * be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     */

    public String getString(String key, Object arg1, Object arg2)
    {
        Object[] args = new Object[]{arg1, arg2};
        return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. These arguments can of course
     * be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     * @param arg3
     */

    public String getString(String key, Object arg1, Object arg2,
                            Object arg3)
    {
        Object[] args = new Object[]{arg1, arg2, arg3};
        return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. These arguments can of course
     * be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */

    public String getString(String key, Object arg1, Object arg2,
                            Object arg3, Object arg4)
    {
        Object[] args = new Object[]{arg1, arg2, arg3, arg4};
        return getString(key, args);
    }
    // --------------------------------------------------------------
    // STATIC SUPPORT METHODS
    // --------------------------------------------------------------


    private static Map<String, StringLocaleManager> managers = new HashMap<String, StringLocaleManager>();


    public synchronized static void clear()
    {
        managers.clear();
    }

    public synchronized static StringLocaleManager getManager(String packageName, Locale loc)
    {
        StringLocaleManager mgr = (StringLocaleManager) managers.get(packageName + "_" + loc.toString());

        if (mgr == null)
        {
            mgr = new StringLocaleManager(packageName, loc);
            managers.put(packageName + "_" + loc.toString(), mgr);
        }

        return mgr;
    }
}
