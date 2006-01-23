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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;

import org.apache.log4j.Logger;

public final class StringManager {
    private final static Logger log = Logger.getLogger( StringManager.class );

    private String storage = null;

    private String currentLocaleString = null;
    private String currentLocaleBase = null;
    private boolean isVariant = false;

    private static Map messages = new HashMap();
    private static boolean isInit = false;

    public static boolean isInit() {
        return isInit;
    }

    protected void finalize() throws Throwable {
        storage = null;
        currentLocaleString = null;
        currentLocaleBase = null;

        super.finalize();
    }

    public synchronized void reinit() {
        if ( log.isDebugEnabled() ) {
            log.debug( "Start reinit hashtable " );
        }

        clear();
    }

    public synchronized void terminate( java.lang.Long id_ ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "Start terminate message in hashtable " );
        }

        clear();
    }

    public synchronized static void clear() {
        if (log.isDebugEnabled())
            log.debug("Start clear hashtable " + messages);

        if (messages != null) {
            messages.clear();
        }

        isInit = false;

        if (log.isDebugEnabled())
            log.debug("End clear hashtable " + messages);

    }

    public StringManager(){}

    public static StringManager getInstance(DatabaseAdapter db_, Long id)
    {
        return new StringManager();
    };

    public synchronized static StringManager getManager(String packageName, Locale loc)
    {
        if (log.isDebugEnabled())
            log.debug("storage " + packageName + ", locale: " + (loc == null?"locale is null":loc.toString()));

        return new StringManager(packageName, loc);
    }

    private StringManager(String packageName, Locale loc) {
        if (loc == null) {
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

    public boolean checkKey(String key) throws GenericException {

        if (!isInit) {
            return false;
        }

        if (getString(key)==null) {
            return false;
        }

        return true;
    }

    /**
     * @param key
     * @return
     * @throws GenericException
     */
    public String getStr( String key )
        throws GenericException {
        if ( !isInit ) {
            initMessages();
        }

        String s = getString(key);
        if ( s == null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "Error get message for key '" +
                    storage + "-" +
                    currentLocaleString + "-" +
                    key + "'" );
            }
            return "store " + storage + ", key " + key;
        }

        return s;

    }

    public String getStr( String key, Object[] args ) throws GenericException {
        if (!isInit ) {
            initMessages();
        }

        String s = getString( key, args );

        if ( s == null ) {
            if ( log.isDebugEnabled() )
                log.debug( "Error get message for key '" +
                    storage + "-" +
                    currentLocaleString + "-" +
                    key + "'" );

            return "store " + storage + ", key " + key;
        }

        return s;

    }

    public String getString(String key) throws GenericException {
        if (key==null){
            String msg = "key is null";
            log.error("Invoke StringManager with null's  locale key");

            throw new NullPointerException(msg);
        }

        if ( !isInit ){
            initMessages();
        }

        String str = null;
        String errorString = "cannot find message associated with key : " + key;

        if (currentLocaleString==null) {
            log.error("Invoke StringManager without initialize locale");
            return errorString;
        }

        str = (String) messages.get(storage + "-" + currentLocaleString + "-" + key);

        if (str == null && isVariant)
            str = (String) messages.get(storage + "-" + currentLocaleBase + "-" + key);

        return str;
    }

    public Collection getKeys(){
        return messages.keySet();
    }

    public String getString(String key, Object[] args) throws GenericException {
        if (!isInit) {
            initMessages();
        }

        String iString = null;
        String value = null;
        try {
            value = getString(key);

            if (value==null) {
                throw new IllegalArgumentException("Key "+key+" not found. "+storage + "-" + currentLocaleString + "-" + key);
            }

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


    static synchronized void initMessages() throws GenericException {
        if (log.isDebugEnabled()) {
            log.debug( "#77.005.01 " + (messages != null ? "" + messages.size() : "messages is null") );
        }

        if (messages == null) {
            throw new IllegalStateException("Message map is null");
        }

        messages.clear();

        try {
            messages.putAll( prepareMessages() );
            isInit = true;
        } catch (Exception e) {
            if (messages != null) {
                messages.clear();
                messages = null;
            }
            String es = "Error init messages";
            log.error( es, e );
            throw new GenericException( es, e );
        }
    }

    public static Map getMessages()  throws GenericException  {
        if (!isInit) {
            initMessages();
        }
        return Collections.unmodifiableMap( messages );
    }

    private static Map prepareMessages() throws GenericException {
        log.debug("Start prepare message hashtable");

        Map messageMap = new HashMap(9000);

        PreparedStatement st = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();
            String sql_ =
                "select a.NAME_STORAGE, d.SHORT_NAME_LANGUAGE, b.NAME_ITEM, c.MESSAGE " +
                "from   WM_I18N_STORAGE a, WM_I18N_ITEM b, WM_I18N_MESSAGE c, WM_LIST_LANGUAGE d " +
                "where  a.ID_MAIN_I18N_STORAGE=b.ID_MAIN_I18N_STORAGE and " +
                "       b.ID_MAIN_I18N_ITEM=c.ID_MAIN_I18N_ITEM and c.ID_LANGUAGE=d.ID_LANGUAGE ";

            st = db_.prepareStatement(sql_);
            rs = st.executeQuery();

            while (rs.next())
            {
                String key = "mill.locale." +RsetTools.getString(rs, "NAME_STORAGE") + "-" +
                        RsetTools.getString(rs, "SHORT_NAME_LANGUAGE") + "-" +
                        RsetTools.getString(rs, "NAME_ITEM");

                messageMap.put(key, RsetTools.getString(rs, "MESSAGE"));
            }
        }
        catch (Exception e) {
            String es = "Error init map with messages";
            log.error( es, e );
            throw new GenericException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, st);
            db_ = null;
            rs = null;
            st = null;
        }

        if (log.isDebugEnabled()) {
            log.debug("End init message hashtable ");
        }

        return messageMap;
    }
}
