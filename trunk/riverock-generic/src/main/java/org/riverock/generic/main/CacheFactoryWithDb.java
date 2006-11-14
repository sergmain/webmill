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
package org.riverock.generic.main;

import java.lang.reflect.Constructor;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.exception.GenericException;

/**
 * $Id$
 */
public class CacheFactoryWithDb {
    private static Logger log = Logger.getLogger(CacheFactoryWithDb.class);

    private Cache cache = null;
    private Class clazz=null;

    public CacheFactoryWithDb(Class clazz) {
        if (log.isInfoEnabled()) {
            log.info("create constructor with class: " + clazz.getName());
        }
        this.clazz=clazz;
        CacheManager singletonManager = CacheManager.create();
        singletonManager.addCache(clazz.getName());
        cache = singletonManager.getCache(clazz.getName());
    }

    public void reinit() {
        cache.removeAll();
    }

    public void terminate(Long id) {
        cache.remove(id);
    }

    public synchronized Object getInstanceNew(DatabaseAdapter db__, Long id__) throws GenericException {
        try {
            Element obj = cache.get(id__);

            if (log.isDebugEnabled()) log.debug("Get object from cache, class " + clazz.getName() + ", result object " + obj);

            if (obj != null)
                return obj.getObjectValue();

            if (log.isDebugEnabled()) log.debug("Result object is null, create new object " + id__);

            Object cacheItem = createObject(db__, id__, clazz.getName());

            if (log.isDebugEnabled()) log.debug("Result new object is " + cacheItem);

            if (cacheItem == null)
                return null;

            cache.put(new Element(id__, cacheItem));
            return cacheItem;
        }
        catch (Throwable e) {
            log.error("Error in CacheFactory.getInstanceNew(DatabaseAdapter db__, Long id__)", e);
            throw new GenericException(e.getMessage(), e);
        }
    }

    private static Object createObject(DatabaseAdapter db_, Long idLong, String className)
        throws GenericException {

        Class p[] = new Class[2];
        Object obj;
        try {
            p[0] = DatabaseAdapter.class;
            p[1] = Long.class;

            if (log.isDebugEnabled()) {
                log.debug("Class DB: " + p[0]);
                log.debug("Class Long: " + p[1]);
            }

            if (log.isDebugEnabled())
                log.debug("Name class " + className);

            Class cl = Class.forName(className);

            if (log.isDebugEnabled())
                log.debug("Class instance: " + cl);

            Constructor cn[] = cl.getConstructors();

            if (log.isDebugEnabled()) {
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

            return obj;
        }
        catch (Throwable e) {
            log.error("Error create object", e);
            throw new GenericException(e.toString(), e);
        }
    }
}
