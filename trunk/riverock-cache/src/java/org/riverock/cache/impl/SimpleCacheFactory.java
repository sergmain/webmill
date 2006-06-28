/*
 * org.riverock.cache -- Generic cache implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.cache.impl;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import org.riverock.cache.config.CacheConfig;
import org.riverock.cache.schema.config.CacheClassItemType;
import org.riverock.common.tools.MainTools;

/**
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class SimpleCacheFactory implements Cache {
    private final static Logger log = Logger.getLogger( SimpleCacheFactory.class );

    private String cacheClass = null;
    private int maxCount = 0;
    private long[] accessTime = null;
    private long[] accessCount = null;
    private long[] indexValue = null;
    private Object[] cache = null;

    private CacheClassItemType classDefinition = null;

    public SimpleCacheFactory(String className)
    {
        cacheClass = className;
    }

    private int maxCountItems()
    {
        initClassDefinition();
        return classDefinition.getMaxObjectCount();
    }

    private void initClassDefinition()
    {
        if (classDefinition==null)
            classDefinition = CacheConfig.getClassDefinition( cacheClass );

        if (classDefinition==null)
        {
            classDefinition = new CacheClassItemType();
            if (log.isInfoEnabled())
            {
                log.info("Cache for class "+ cacheClass+" not defined. Use default:");
                log.info("    initObjectCount: "+classDefinition.getInitObjectCount());
                log.info("    maxObjectCount: "+classDefinition.getMaxObjectCount());
                log.info("    ttl: "+classDefinition.getTtl());
                log.info("    cache control: "+classDefinition.getCacheControlType().toString());
            }
        }
    }

    private long maxTimePeriod()
    {
        initClassDefinition();
        return classDefinition.getTtl();
    }

    public String getClassName()
    {
        return cacheClass;
    }

    /**
     * Get an item from the cache
     * @param key
     * @return the cached object or <tt>null</tt>
     * @throws CacheException
     */
    public synchronized Object get(Object key)
    throws CacheException
    {
        if (key==null)
        {
            log.warn( "Key is null" );
            return null;
        }
        if (! (key instanceof Long))
        {
            String es = "SimpleCache support only key with Long type. Class is "+key.getClass().getName();
            log.error(es);
            throw new CacheException( es );
        }

        long id__ = ((Long) key);
        try
        {
            int idx = getIndexOfItem(id__);

            if (log.isDebugEnabled())
                log.debug("idx " + idx + " id: " + id__);

            boolean isReplace = false;
            if (idx == -1)
            {
                isReplace = true;
                idx = getNewIndex();
            }

            long currentTime = System.currentTimeMillis();
            if (((currentTime - accessTime[idx]) > maxTimePeriod())
                || (cache[idx] == null) || isReplace)
            {
                cache[idx] = null;
                accessTime[idx] = currentTime;
                accessCount[idx] = 0;
                indexValue[idx] = id__;
                return null;
            }

            if (log.isDebugEnabled())
            {
                log.debug("accessTime[idx] " + accessTime[idx]);
                log.debug("cache " + cache);
                log.debug("cache.length " + cache.length);
                log.debug("cache[idx] " + cache[idx]);
            }

            accessTime[idx] = System.currentTimeMillis();
            accessCount[idx]++;
            return cache[idx];
        }
        catch (java.lang.ArrayStoreException e)
        {
            log.error("Exception in get(Object key)", e);
            throw new CacheException(e);
        }
        catch (Error err)
        {
            log.error("Error in in get(Object key)", err);
            throw new CacheException(err.toString());
        }
    }


    /**
     * Add an item to the cache
     * @param key
     * @param value
     * @throws CacheException
     */
    public void put(Object key, Object value) throws CacheException {
        if (! (key instanceof Long))
        {
            String es = "SimpleCache support only key with Long type";
            log.error(es);
            throw new CacheException( es );
        }

        long id__ = ((Long) key);
        try
        {
            int idx = getIndexOfItem( id__);

            if (log.isDebugEnabled())
                log.debug("idx " + idx + " id: " + id__);

            boolean isReplace = false;
            if (idx == -1)
            {
                isReplace = true;
                idx = getNewIndex();
            }

            if (log.isDebugEnabled())
            {
                log.debug("idx " + idx);
                log.debug("accessTime[idx] " + accessTime[idx]);
                log.debug("cache " + cache);
                log.debug("cache.length " + cache.length);
                log.debug("cache[idx] " + cache[idx]);
            }

            long currentTime = System.currentTimeMillis();

            if (((currentTime - accessTime[idx]) > maxTimePeriod())
                || (cache[idx] == null) || isReplace)
            {
                if (log.isDebugEnabled())
                    log.debug("Place object, id: " + id__+", idx "+idx);

                cache[idx] = null;

                cache[idx] = value;
                accessTime[idx] = currentTime;
                accessCount[idx] = 0;
                indexValue[idx] = id__;
            }

            accessTime[idx] = currentTime;
            accessCount[idx]++;

            if (log.isDebugEnabled())
                log.debug("idx: " + idx + ", obj: " + cache[idx]);
        }
        catch (Exception e)
        {
            log.error("Exception in put(Object key, Object value)", e);
            throw new CacheException(e.toString());
        }
        catch (Error err)
        {
            log.error("Error in put(Object key, Object value)", err);
            throw new CacheException(err.toString());
        }
    }


    /**
     * Remove an item from the cache
     */
    public void remove(Object key) throws CacheException
    {
        if (! (key instanceof Long))
        {
            String es = "SimpleCache support only key with Long type";
            log.error(es);
            throw new CacheException( es );
        }

        try
        {
            terminate((Long)key);
        }
        catch(Exception exc)
        {
            log.error("Exception in remove(Object key", exc);
            throw new CacheException( exc.toString() );
        }
    }

    /**
     * Clear the cache
     */
    public void clear()
    {
        reinit();
    }

    /**
     * Clean up
     */
    public void destroy()
    {
        destroyCache();
    }

    public long nextTimestamp() {
        return Timestamper.next();
    }

    public void lock(Object key) {
        //local cache, so we use synchronization
    }

    public void unlock(Object key) {
        //local cache, so we use synchronization
    }

    public int getTimeout() {
        return Timestamper.ONE_MS * 60000; //ie. 60 seconds
    }

    public synchronized static void terminate(String className, long id, boolean isFullReinit)
    {
        if (className == null)
        {
            if (log.isInfoEnabled())
                log.info("#12.12.000 param className is null");

            return;
        }
        try
        {
            if (log.isDebugEnabled())
                log.debug("#12.12.001 ClassName: " + className + ", id: " + id + ", isFull: " + isFullReinit);

            Object obj = MainTools.createCustomObject( className );

            if (isFullReinit)
            {
                Method method1 = obj.getClass().getMethod("reinit", (Class[])null);

                if (log.isDebugEnabled())
                    log.debug("#12.12.009  method1 is " + method1);

                if (method1 != null)
                {
                    method1.invoke(obj, (Object[])null);

                    if (log.isDebugEnabled())
                        log.debug("#12.12.010 ");
                }
            }
            else
            {
                Class[] cl1 = {Class.forName("java.lang.Long")};
                Method method1 = obj.getClass().getMethod("terminate", cl1);

                if (method1 == null)
                {
                    log.error("#12.12.020 error create method terminate{} for class  " + className);
                    return;
                }

                if (log.isDebugEnabled())
                    log.debug("#12.12.013  method1 is " + method1);

                Object objArgs1[] = {id};
                method1.invoke(obj, objArgs1);
                objArgs1 = null;

                if (log.isDebugEnabled())
                    log.debug("#12.12.017 ");
            }
        }
        catch (Exception e)
        {
            log.error("Method invocation error . ClassName: " + className + ", id: " + id + ", isFull: " + isFullReinit, e);
        }
    }

    public synchronized void reinit()
    {
        if (log.isDebugEnabled())
            log.debug("full reinit cache for class "+cacheClass);

        initValueArray();
    }

    public synchronized void terminate(Long id) {
        if (log.isDebugEnabled())
            log.debug("#12.17.001  id " + id);

        if (id != null) {
            int itemIdx = getIndexOfItem(id);
            if (itemIdx==-1)
                return;

            cache[itemIdx] = null;
        }
    }

    private synchronized void destroyCache() {
        accessTime = null;
        accessCount = null;
        indexValue = null;
        cache = null;
    }


    private synchronized void initArray() {
        if ((accessTime == null) || (cache == null) || (indexValue == null) || (accessCount == null)){
            destroyCache();
        }
        initValueArray();
    }

    private synchronized void initValueArray()
    {
        int countItem = maxCountItems();

        if (log.isDebugEnabled())
            log.debug("#12.05.02 countItem " + countItem );

        accessTime = null;
        accessCount = null;
        indexValue = null;
        cache = null;

        accessTime = new long[countItem];
        accessCount = new long[countItem];
        indexValue = new long[countItem];
        cache = new Object[countItem];
        maxCount = countItem;
    }

    protected synchronized void checkFullInitArray()
    {
        checkWithInitArray();
        checkWithInitValueArray();
    }

    protected synchronized boolean checkWithInitArray()
    {
        if (log.isDebugEnabled())
        {
            log.debug("status of checkWithInitArray "+
                ((accessTime == null) ||
                (cache == null) ||
                (indexValue == null) ||
                (accessCount == null) ||
                (maxCount == 0)
                )
            );
            log.debug("accessTime "+accessTime);
            log.debug("cache "+cache);
            log.debug("indexValue "+indexValue);
            log.debug("accessCount "+accessCount);
            log.debug("maxCount "+maxCount);
        }
        if ((accessTime == null) ||
            (cache == null) ||
            (indexValue == null) ||
            (accessCount == null) ||
            (maxCount == 0)
        )
        {
            if (log.isDebugEnabled())
                log.debug("checkWithInitArray(), array is empty. call initArray()");

            initArray();
            return true;
        }
        return false;
    }

    protected synchronized boolean checkWithInitValueArray()
    {

        if ((accessTime == null) ||
            (cache == null) ||
            (indexValue == null) ||
            (accessCount == null)
            || maxCount==0
        )
        {
            if (log.isDebugEnabled())
                log.debug("checkWithInitValueArray(int idx), array is empty. call initValueArray()");

            initValueArray();
            return true;
        }
        return false;
    }

    protected synchronized int getIndexOfItem( long id__ )
    {
        if (checkWithInitArray())
            return 0;

        if (checkWithInitValueArray())
            return 0;

        if (log.isDebugEnabled())
        {
            log.debug("Max element 2 " + maxCountItems());
            log.debug("Array " + indexValue.length);
        }

        for (int i = 0; i < maxCountItems(); i++)
        {
            if (indexValue[i] == id__)
                return i;
        }
        return -1;
    }

    protected synchronized int getNewIndex() {
        int idx = -1;

        long countItem = maxCountItems();

        long minCount = Long.MAX_VALUE;
        long currTime = System.currentTimeMillis();
        boolean isExpired = false;
        long periodExpired = maxTimePeriod();
        int expiredId = 0;
        long tmp;

        for (int j = 0; j < countItem; j++) {
            if (cache[j] == null) {
                return j;
            }

            tmp = currTime - accessTime[j];
            if (tmp > periodExpired) {
                periodExpired = tmp;
                expiredId = j;
                isExpired = true;
            }

            if (accessCount[j] < minCount) {
                minCount = accessCount[j];
                idx = j;
            }
        }
        if (isExpired) {
            return expiredId;
        }

        if (log.isDebugEnabled()) {
            log.debug("Not expired");
        }

        long minTime = Long.MAX_VALUE;
        for (int i = 0; i < countItem; i++) {
            if ((accessCount[i] == minCount) && (accessTime[i] < minTime)) {
                minTime = accessTime[i];
                idx = i;
            }
        }

        return idx;

    }
}
