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
package org.riverock.generic.main;

import java.lang.reflect.Constructor;

import org.riverock.cache.impl.CacheArray;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.exception.GenericException;

import org.apache.log4j.Logger;

/**
 * @deprecated Not supported. Use CacheFactory
 */
public abstract class CacheItemV2 extends CacheArray
{
    private static Logger log = Logger.getLogger( CacheItemV2.class );

    private synchronized CacheItemV2 createObject(DatabaseAdapter db_, Long id__)
        throws GenericException
    {

        Class p[] = new Class[2];
        Object obj = null;

        try {
            Long idLong = id__;
            p[0] = org.riverock.generic.db.DatabaseAdapter.class;
            p[1] = idLong.getClass();

            if (log.isDebugEnabled())
            {
                log.debug("Class DB: " + p[0]);
                log.debug("Class Long: " + p[1]);
            }

            if (log.isDebugEnabled())
                log.debug("Name class " + this.getClass().getName());

            Class cl = Class.forName(this.getClass().getName());

            if (log.isDebugEnabled())
                log.debug("Class instance: " + cl);

            Constructor cn[] = cl.getConstructors();

            if (log.isDebugEnabled())
            {
                log.debug("Constructor count " + cn.length);

                for (int i = 0; i < cn.length; i++)
                    log.debug("constructor #" + i + " " + cn[i]);

                log.debug("Get constructor ");
            }

            Constructor constr = cl.getConstructor(p);

            if (log.isDebugEnabled())
                log.debug("Constructor " + constr);

            Object o[] = new Object[2];
            o[0] = db_;
            o[1] = idLong;

            obj = constr.newInstance(o);

            return (CacheItemV2) obj;
        }
        catch(Exception e)
        {
            log.error("Exception create CacheItemV2 object", e);
            throw new GenericException(e.toString());
        }
        catch(Error err)
        {
            log.error("Error create CacheItemV2 object", err);
            throw err;
        }
    }

    public synchronized CacheItemV2 getInstanceNew(DatabaseAdapter db__, long id__)
            throws GenericException
    {
        return getInstanceNew(db__, new Long(id__));
    }

    public synchronized CacheItemV2 getInstanceNew(DatabaseAdapter db__, Long id__)
        throws GenericException
    {
        try
        {
            Object obj = get( id__ );

            if (log.isDebugEnabled())
                log.debug("Get object from cache, class "+this.getClass().getName()+", result object "+obj);

            if (obj!=null)
                return (CacheItemV2)obj;

            if (log.isDebugEnabled())
                log.debug("Result object is null, create new object "+id__);

            Object cacheItem = createObject(db__, id__);

            if (log.isDebugEnabled())
                log.debug("Result new object is "+cacheItem);

            if (cacheItem==null)
                return null;

            put(id__, cacheItem );
            return (CacheItemV2)cacheItem;
        }
        catch (Exception e)
        {
            log.error("Exception in CacheItemV2.getInstanceNew(DatabaseAdapter db__, long id__)", e);
            throw new GenericException(e.getMessage());
        }
        catch (Error err)
        {
            log.error("Error in CacheItemV2.getInstanceNew(DatabaseAdapter db__, long id__)", err);
            throw err;
        }
    }
}
