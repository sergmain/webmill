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
package org.riverock.generic.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.riverock.cache.impl.SimpleCacheFactory;
import org.riverock.cache.impl.CacheException;
import org.riverock.generic.exception.GenericException;
import org.riverock.common.tools.MainTools;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public class CacheFactory {
    private static Logger log = Logger.getLogger( CacheFactory.class );

    protected static Integer CountClassInCache = 20;

    private SimpleCacheFactory cache = null;

    public CacheFactory(String className) {
        log.info("create constructor: "+className);
        cache = new SimpleCacheFactory(className);
    }

    public void reinit() {
        cache.reinit();
    }

    public synchronized void terminate(Long id) throws CacheException {
        cache.terminate(id);
    }

    private static synchronized Object createObject(Long id__, String className)
        throws GenericException {

        Class p[] = new Class[1];
        Object obj = null;

        try {
            Long idLong = id__;
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
            Object obj = cache.get( id__ );

            if (log.isDebugEnabled()) log.debug("Get object from cache, class "+cache.getClassName()+", result object "+obj);

            if (obj!=null)
                return obj;

            if (log.isDebugEnabled()) log.debug("Result object is null, create new object "+id__);

            Object cacheItem = createObject(id__, cache.getClassName());

            if (log.isDebugEnabled())log.debug("Result new object is "+cacheItem);

            if (cacheItem==null)
                return null;

            cache.put(id__, cacheItem );
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
                objArgs1 = null;

                if (log.isDebugEnabled())
                    log.debug("#12.12.017 ");
            }
        }
        catch (Exception e) {
            log.error("Method invocation error . ClassName: " + className + ", id: " + id + ", isFull: " + isFullReinit, e);
        }
    }
}
