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

 * $Id$

 */

package org.riverock.generic.tools;



import java.io.UnsupportedEncodingException;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.text.MessageFormat;

import java.util.Hashtable;

import java.util.Locale;



import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;



import org.apache.log4j.Logger;



public class StringManager

{

    private static Logger log = Logger.getLogger("org.riverock.tools.StringManager" );



    private String storage = null;



    private String currentLocaleString = null;

    private String currentLocaleBase = null;

    private boolean isVariant = false;



    private static Hashtable messages = null;





    public static boolean isInit()

    {

        return messages != null;

    }



    protected void finalize() throws Throwable

    {

        storage = null;

        currentLocaleString = null;

        currentLocaleBase = null;



        super.finalize();

    }



    public synchronized void reinit()

    {

        if (log.isDebugEnabled())

            log.debug("Start reinit hashtable ");



        clear();

    }



    public synchronized void terminate(java.lang.Long id_)

    {

        if (log.isDebugEnabled())

            log.debug("Start terminate message in hashtable ");



        clear();

    }





    public synchronized static void clear()

    {

        if (log.isDebugEnabled())

            log.debug("Start clear hashtable " + messages);



        if (messages != null)

            messages.clear();



        if (log.isDebugEnabled())

            log.debug("End clear hashtable " + messages);



        messages = null;

    }



    public StringManager(){}



    public static StringManager getInstance(DatabaseAdapter db_, Long id)

    {

        return new StringManager();

    };





    private StringManager(String packageName, Locale loc)

    {

        if (loc == null)

        {

            log.error("Error init StringManager - locale is null");

            currentLocaleString = null;

            currentLocaleBase = null;

            isVariant = false;

            return;

        }



        currentLocaleString = loc.toString();



        String variant = loc.getVariant();



        if (log.isDebugEnabled())

            log.debug("Locale variant - " + variant);



        if (variant == null || variant.length() == 0)

            isVariant = false;

        else

            isVariant = true;



        if (isVariant)

            currentLocaleBase = new Locale(loc.getLanguage(), loc.getCountry()).toString();

        else

            currentLocaleBase = currentLocaleString;





        storage = packageName;

    }



    public boolean checkKey(String key)

    {

        if (messages==null)

            return false;



        if (getString(key)==null)

            return false;



        return true;

    }



    public String getStr(String key)

            throws UnsupportedEncodingException

    {

        if (messages == null)

        {

            initMessages();

        }



        String s = getString(key);

        if (s == null)

        {

            if (log.isDebugEnabled())

                log.debug("Error get message for key '" +

                        storage + "-" +

                        currentLocaleString + "-" +

                        key + "'");



            return "store " + storage + ", key " + key;

        }



        return s;



    }



    public String getStr(String key, Object[] args)

            throws UnsupportedEncodingException

    {

        if (messages == null)

        {

            initMessages();

        }



        String s = getString(key, args);



        if (s == null)

        {

            if (log.isDebugEnabled())

                log.debug("Error get message for key '" +

                        storage + "-" +

                        currentLocaleString + "-" +

                        key + "'");



            return "store " + storage + ", key " + key;

        }



        return s;



    }





    public String getString(String key)

    {

        if (messages == null)

        {

            initMessages();

        }



        if (key == null)

        {

            String msg = "key is null";

            log.error("Invoke StringManager with null's  locale key");



            throw new NullPointerException(msg);

        }



        String str = null;

        String errorString = "cannot find message associated with key : " + key;



        if (currentLocaleString == null)

        {

            log.error("Invoke StringManager without initialize locale");

            return errorString;

        }



        str = (String) messages.get(storage + "-" + currentLocaleString + "-" + key);



        if (str == null && isVariant)

            str = (String) messages.get(storage + "-" + currentLocaleBase + "-" + key);



/*

        try

        {

            str = (String) messages.get(storage + "-" + currentLocale.toString() + "-" + key);

//	    str = bundle.getString(key);

        }

        catch (MissingResourceException mre)

        {

            str = errorString;

            mre.printStackTrace();

        }

*/



        return str;

    }





    public String getString(String key, Object[] args)

    {

        if (messages == null)

        {

            initMessages();

        }



        String iString = null;

        String value = null;

        try

        {

            value = getString(key);



            if (value==null)

                throw new IllegalArgumentException("Key "+key+" not found. "+storage + "-" + currentLocaleString + "-" + key);



        // this check for the runtime exception is some pre 1.1.6

        // VM's don't do an automatic toString() on the passed in

        // objects and barf out



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

            log.error("Error call getString(String key, Object[] args)", iae);



            StringBuffer buf = new StringBuffer();

            buf.append(value);

            for (int i = 0; i < args.length; i++)

            {

                buf.append(" arg[" + i + "]=" + args[i]);

            }

            iString = buf.toString();

        }

        return iString;

    }





    synchronized void initMessages()

    {

        if (log.isDebugEnabled())

        {

            log.debug("Start init message hashtable " + messages);

            log.debug("storage " + storage + ", locale " + currentLocaleString);

        }



        if (messages != null)

            return;



        if (log.isDebugEnabled())

            log.debug("#77.005.01 " + (messages != null?"" + messages.size():"messages is null"));



        messages = new Hashtable(9000);



        if (log.isDebugEnabled())

            log.debug("#77.005.02 " + (messages != null?"" + messages.size():"messages is null"));



        PreparedStatement st = null;

        ResultSet rs = null;

//	FileOutputStream os = null;

        int i = 0;

        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            String sql_ =

                    "select a.NAME_STORAGE, d.SHORT_NAME_LANGUAGE, b.NAME_ITEM, c.MESSAGE " +

                    "from main_i18n_storage a, main_i18n_item b, main_i18n_message c, main_language d " +

                    "where a.ID_MAIN_I18N_STORAGE=b.ID_MAIN_I18N_STORAGE and " +

                    "b.ID_MAIN_I18N_ITEM=c.ID_MAIN_I18N_ITEM and c.ID_LANGUAGE=d.ID_LANGUAGE ";



            st = db_.prepareStatement(sql_);

            rs = st.executeQuery();



            while (rs.next())

            {

                // for compatible with previos version of StringManager,

                // add 'mill.locale.'

                String key = "mill.locale." + RsetTools.getString(rs, "NAME_STORAGE") + "-" +

                        RsetTools.getString(rs, "SHORT_NAME_LANGUAGE") + "-" +

                        RsetTools.getString(rs, "NAME_ITEM");



//		os.write( (key+"\t="+RsetTools.getString(rs,"MESSAGE")+"\n").getBytes() );



                if (messages == null)

                {

                    if (log.isDebugEnabled())

                    {

                        log.debug("#77.005.03 " + (messages != null?"" + messages.size():"messages is null"));

                        log.debug("#77.005.04 " + key + " " + i);

                    }

                }

                messages.put(key, RsetTools.getString(rs, "MESSAGE"));

                i++;

            }

        }

        catch (Exception e)

        {

            log.error(ExceptionTools.getStackTrace(e, 10));

            if (messages != null)

            {

                messages.clear();

                messages = null;

            }

        }

        finally

        {

            org.riverock.generic.db.DatabaseManager.close(db_, rs, st);

            db_ = null;

            rs = null;

            st = null;

        }



        if (log.isDebugEnabled())

            log.debug("End init message hashtable ");

    }





    public synchronized static StringManager getManager(String packageName, Locale loc)

    {

        if (log.isDebugEnabled())

            log.debug("storage " + packageName + ", locale: " + (loc == null?"locale is null":loc.toString()));



        return new StringManager(packageName, loc);

    }

}

