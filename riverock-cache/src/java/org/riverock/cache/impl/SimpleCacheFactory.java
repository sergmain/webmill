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



/**

 * $Id$

 */

package org.riverock.cache.impl;



import org.riverock.common.tools.MainTools;

import org.riverock.cache.schema.config.CacheClassItemType;

import org.riverock.cache.config.CacheConfig;



import org.apache.log4j.Logger;



import java.lang.reflect.Method;



public class SimpleCacheFactory implements Cache

{

    private static Logger log = Logger.getLogger( SimpleCacheFactory.class );



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

        return classDefinition.getMaxObjectCount().intValue();

    }



//    private int initCountItems()

//    {

//        initClassDefinition();

//        return classDefinition.getInitObjectCount().intValue();

//    }



    private void initClassDefinition()

    {

        if (classDefinition==null)

            classDefinition = CacheConfig.getClassDefinition( cacheClass );



        if (classDefinition==null)

        {

            classDefinition = new CacheClassItemType();

            log.warn("Cache for class "+ cacheClass+" not defined. Use default");

            if (log.isInfoEnabled())

            {

                log.info("initObjectCount - "+classDefinition.getInitObjectCount());

                log.info("maxObjectCount - "+classDefinition.getMaxObjectCount());

                log.info("ttl - "+classDefinition.getTtl());

                log.info("cache control - "+classDefinition.getCacheControlType().toString());

            }

        }

    }



    private long maxTimePeriod()

    {

        initClassDefinition();

        return classDefinition.getTtl().longValue();

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



        long id__ = ((Long)key).longValue();

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

                cache[idx] = null;;

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

    public void put(Object key, Object value)

        throws CacheException

    {

        if (! (key instanceof Long))

        {

            String es = "SimpleCache support only key with Long type";

            log.error(es);

            throw new CacheException( es );

        }



        long id__ = ((Long)key).longValue();

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



                cache[idx] = value;;

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

    public void clear() throws CacheException

    {

        reinit();

    }



    /**

     * Clean up

     */

    public void destroy() throws CacheException

    {

        destroyCache();

    }



    public long nextTimestamp() {

        return Timestamper.next();

    }



    public void lock(Object key) throws CacheException {

        //local cache, so we use synchronization

    }



    public void unlock(Object key) throws CacheException {

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

                Method method1 = obj.getClass().getMethod("reinit", null);



                if (log.isDebugEnabled())

                    log.debug("#12.12.009  method1 is " + method1);



                if (method1 != null)

                {

                    method1.invoke(obj, null);



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



                Object objArgs1[] = {new Long(id)};

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



    public synchronized void terminate(Long id)

        throws CacheException

    {

        if (log.isDebugEnabled())

            log.debug("#12.17.001  id " + id);



        if (id != null)

            terminate(id.longValue());

    }



    public synchronized void terminate(long id)

        throws CacheException

    {

//        int cacheIdx = getIndex();



        int itemIdx = getIndexOfItem(id);

        if (itemIdx==-1)

            return;



//	accessTime[cacheIdx][itemIdx] = null;

//	accessCount[cacheIdx][itemIdx] = null;

//	indexValue[cacheIdx][itemIdx] = null;

        cache[itemIdx] = null;

    }



/*

    public synchronized int getIndex()

    {

        checkWithInitArray();



//            if(log.isInfoEnabled())

//            {

//                log.info("Start looking for cache index for class "+getNameClass());

//                log.info("CountClassInCache - "+CountClassInCache.intValue()+

//                    ", cacheClass length - "+cacheClass.length

//                );

//            }



        while (true)

        {

            for (int i = 0; i < CountClassInCache.intValue(); i++)

            {

                if (cacheClass[i] == null)

                {

                    cacheClass[i] = getNameClass();

                    return i;

                }



                if (cacheClass[i].equals(getNameClass()))

                    return i;



            }



            int oldValue = CountClassInCache.intValue();

            CountClassInCache =

                new Integer(

                    (int)(CountClassInCache.intValue() * ((CACHE_INCREASE_PERCENT+100)/100) )

                );



            if (log.isDebugEnabled())

                log.debug("new value of CountClassInCache can't equals to oldValue, di increment");



            if (oldValue==CountClassInCache.intValue())

                CountClassInCache = new Integer(oldValue+1);



            log.warn("Cache array is full");

            log.warn("Old value of CountClassInCache "+oldValue+", new "+CountClassInCache.intValue());



            long mills = System.currentTimeMillis();

            if(log.isInfoEnabled())

                log.info("Start applay new size to cache array");



            if (log.isDebugEnabled())

            {

                log.debug("maxCount.length - "+maxCount.length);

                log.debug("CountClassInCache.intValue() - "+CountClassInCache.intValue());

            }

            {

                int maxCountTemp[] = new int[ CountClassInCache.intValue() ];

                for (int i=0; i<maxCount.length; i++)

                    maxCountTemp[i] = maxCount[i];

                maxCount = maxCountTemp;

            }



            {

                String cacheClassTemp[] = new String[CountClassInCache.intValue()];

                for (int i=0; i<cacheClass.length; i++)

                    cacheClassTemp[i] = cacheClass[i];

                cacheClass = cacheClassTemp;

            }



            {

                long accessTimeTemp[][] = new long[CountClassInCache.intValue()][];

                for (int i=0; i<accessTime.length; i++)

                {

                    accessTimeTemp[i] = new long[maxCount[i]];

                    if (log.isDebugEnabled())

                    {

                        log.debug("accessTimeTemp[i] "+accessTimeTemp[i].length);

                        log.debug("maxCount[i] "+maxCount[i]);

                    }

                    if (accessTime[i]!=null)

                    {

//                        for (int j=0; j<accessTime[i].length; j++)

                        for (int j=0; j<maxCount[i]; j++)

                            accessTimeTemp[i][j] = accessTime[i][j];

                    }

//                    if (accessTime[i]==null)

//                    {

//                        accessTimeTemp[i] = new long[maxCount[i]];

//                    }

//                    else

//                    {

//                        accessTimeTemp[i] = new long[accessTime[i].length];

//                        for (int j=0; j<accessTime[i].length; j++)

//                            accessTimeTemp[i][j] = accessTime[i][j];

//                    }

                }

                accessTime = accessTimeTemp;

            }



            {

                long accessCountTemp[][] = new long[CountClassInCache.intValue()][];

                for (int i=0; i<accessCount.length; i++)

                {

                    accessCountTemp[i] = new long[ maxCount[i] ];

                    if (accessCount[i]!=null)

                    {

//                        for (int j=0; j<accessCount[i].length; j++)

                        for (int j=0; j<maxCount[i]; j++)

                            accessCountTemp[i][j] = accessCount[i][j];

                    }

//                    if (accessCount[i]==null)

//                    {

//                        accessCountTemp[i] = new long[ maxCount[i] ];

//                    }

//                    else

//                    {

//                        accessCountTemp[i] = new long[accessCount[i].length];

//                        for (int j=0; j<accessCount[i].length; j++)

//                            accessCountTemp[i][j] = accessCount[i][j];

//                    }

                }

                accessCount = accessCountTemp;

            }



            {

                long indexValueTemp[][] = new long[CountClassInCache.intValue()][];

                for (int i=0; i<indexValue.length; i++)

                {

                    indexValueTemp[i] = new long[ maxCount[i] ];

                    if (indexValue[i]!=null)

                    {

//                        for (int j=0; j<indexValue[i].length; j++)

                        for (int j=0; j<maxCount[i]; j++)

                            indexValueTemp[i][j] = indexValue[i][j];

                    }

//                    if (indexValue[i]==null)

//                    {

//                        indexValueTemp[i] = new long[ maxCount[i] ];

//                    }

//                    else

//                    {

//                        indexValueTemp[i] = new long[indexValue[i].length];

//                        for (int j=0; j<indexValue[i].length; j++)

//                            indexValueTemp[i][j] = indexValue[i][j];

//                    }

                }

                indexValue = indexValueTemp;

            }



            {

                Object cacheTemp[][] = new SimpleCache[CountClassInCache.intValue()][];

                for (int i=0; i<cache.length; i++)

                {

                    cacheTemp[i] = new SimpleCache[ maxCount[i] ];

                    if (cache[i]!=null)

                    {

//                        for (int j=0; j<cache[i].length; j++)

                        for (int j=0; j<maxCount[i]; j++)

                            cacheTemp[i][j] = cache[i][j];

                    }

//                    if (cache[i]==null)

//                    {

//                        cacheTemp[i] = new SimpleCache[ maxCount[i] ];

//                    }

//                    else

//                    {

//                        cacheTemp[i] = new SimpleCache[cache[i].length];

//                        for (int j=0; j<cache[i].length; j++)

//                            cacheTemp[i][j] = cache[i][j];

//                    }

                }

                cache = cacheTemp;

            }

            if(log.isInfoEnabled())

                log.info("Done applay new size to cache array for "+(System.currentTimeMillis()-mills)+" milliseconds");



        }

    }

*/



//    public synchronized static void reinitFullCache()

//    {

//        destroyCache();

//    }

//

    private synchronized void destroyCache()

    {

        accessTime = null;

        accessCount = null;

        indexValue = null;

        cache = null;

    }





    private synchronized void initArray()

    {

        if ((accessTime == null) || (cache == null) || (indexValue == null) ||

            (accessCount == null)

        )

        {

            destroyCache();

        }

        initValueArray();

    }



    private synchronized void initValueArray()

    {

        int countItem = maxCountItems();

//        int cacheIndex = getIndex();

        //int cacheIndex = getCacheIndex();



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

//        cacheClass = getNameClass();

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



    protected synchronized int getNewIndex()

    {

        int idx = -1;



        long countItem = maxCountItems();



        long minCount = Long.MAX_VALUE;

        long currTime = System.currentTimeMillis();

        boolean isExpired = false;

        long periodExpired = maxTimePeriod();

        int expiredId = 0;

        long tmp;



        for (int j = 0; j < countItem; j++)

        {

            if (cache[j] == null)

                return j;



            tmp = currTime - accessTime[j];

            if (tmp > periodExpired)

            {

                periodExpired = tmp;

                expiredId = j;

                isExpired = true;

            }



            if (accessCount[j] < minCount)

            {

                minCount = accessCount[j];

                idx = j;

            }

        }

        if (isExpired)

            return expiredId;



        if (log.isDebugEnabled())

            log.debug("Not expired");



        long minTime = Long.MAX_VALUE;

        for (int i = 0; i < countItem; i++)

        {

            if ((accessCount[i] == minCount) && (accessTime[i] < minTime))

            {

                minTime = accessTime[i];

                idx = i;

            }

        }



        return idx;



    }



//    public static Integer getCountClassInCache()

//    {

//        return CountClassInCache;

//    }



}

