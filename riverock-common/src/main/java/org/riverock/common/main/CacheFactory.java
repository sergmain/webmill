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
package org.riverock.common.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

import org.riverock.common.exception.GenericException;
import org.riverock.common.tools.MainTools;

/**
 * $Id$
 */
public class CacheFactory {
    private static Logger log = Logger.getLogger( CacheFactory.class );

    private Cache cache = null;
    private Class clazz=null;

    public CacheFactory(Class clazz) {
        if (log.isInfoEnabled()) {
            log.info("create constructor with class: " + clazz.getName());
        }
        this.clazz=clazz;
        CacheManager singletonManager = CacheManager.create();
        singletonManager.addCache(clazz.getName());
        cache = singletonManager.getCache(clazz.getName());
    }

    public synchronized static void shutdown() {
        CacheManager manager=null;
        try {
            manager = CacheManager.getInstance();
        }
        catch (Throwable e) {
            System.err.println("General error of releasing cache");
            e.printStackTrace(System.err);
        }
        if (manager!=null) {
            String[] cacheNames = null;
            try {
                cacheNames = manager.getCacheNames();
            }
            catch (Throwable e) {
                System.err.println("Error get name of caches");
                e.printStackTrace(System.err);
            }
            if (cacheNames!=null) {
                for (String cacheName : cacheNames) {
                    try {
                        manager.removeCache(cacheName);
                    }
                    catch (Throwable e) {
                        System.err.println("Error remove cache with name: " + cacheName);
                        e.printStackTrace(System.err);
                    }
                }
            }
            try {
                manager.shutdown();
            }
            catch (Exception e) {
                System.err.println("Error shutdown cache");
                e.printStackTrace(System.err);
            }
        }
    }

    public void reinit() {
        cache.removeAll();
    }

    public void terminate(Long id) {
        cache.remove(id);
    }

    private static Object createObject(Long idLong, String className)
        throws GenericException {

        Class p[] = new Class[1];
        Object obj;
        try {
            p[0] = Long.class;

            if (log.isDebugEnabled()) {
                log.debug("Class Long: " + p[0]);
                log.debug("Name class " + className );
            }

            Class cl = Class.forName( className );

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

            Object o[] = new Object[1];
            o[0] = idLong;

            obj = constr.newInstance(o);

            return obj;
        }
        catch(Throwable e)
        {
            log.error("Error create object", e);
            throw new GenericException(e.toString(), e);
        }
    }

    public synchronized Object getInstanceNew(Long id__) throws GenericException {
        try {
            Element obj = cache.get( id__ );

            if (log.isDebugEnabled()) log.debug("Get object from cache, class "+clazz.getName()+", result object "+obj);

            if (obj!=null)
                return obj.getObjectValue();

            if (log.isDebugEnabled()) log.debug("Result object is null, create new object "+id__);

            Object cacheItem = createObject(id__, clazz.getName());

            if (log.isDebugEnabled())log.debug("Result new object is "+cacheItem);

            if (cacheItem==null)
                return null;

            cache.put(new Element(id__, cacheItem));
            return cacheItem;
        }
        catch (Throwable e) {
            log.error("Error in CacheFactory.getInstanceNew(DatabaseAdapter db__, Long id__)", e);
            throw new GenericException(e.getMessage(), e);
        }
    }

    public synchronized static void terminate(String className, Long id, boolean isFullReinit) {
        if (className == null){
            if (log.isInfoEnabled())
                log.info("#12.12.000 param className is null");

            return;
        }

        try{
            if (log.isDebugEnabled())
                log.debug("#12.12.001 ClassName: " + className + ", id: " + id + ", isFull: " + isFullReinit);

            Object obj = MainTools.createCustomObject( className );

            if (isFullReinit) {
                Method method1 = obj.getClass().getMethod("reinit", (Class[])null);

                if (log.isDebugEnabled())
                    log.debug("#12.12.009  method1 is " + method1);

                if (method1 != null) {
                    method1.invoke(obj, (Object[])null);

                    if (log.isDebugEnabled())
                        log.debug("#12.12.010 ");
                }
            }
            else {
                Class[] cl1 = {Long.class};
                Method method1 = obj.getClass().getMethod("terminate", cl1);

                if (method1 == null) {
                    log.error("#12.12.020 error create method terminate{} for class  " + className);
                    return;
                }

                if (log.isDebugEnabled())
                    log.debug("#12.12.013  method1 is " + method1);

                Object objArgs1[] = { id };
                method1.invoke(obj, objArgs1);

                if (log.isDebugEnabled())
                    log.debug("#12.12.017 ");
            }
        }
        catch (Exception e) {
            log.error("Method invocation error . ClassName: " + className + ", id: " + id + ", isFull: " + isFullReinit, e);
        }
    }
}
