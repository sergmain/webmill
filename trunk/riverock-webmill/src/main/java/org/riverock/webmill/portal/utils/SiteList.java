/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.portal.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:01:59
 *         $Id$
 */
public class SiteList {
    private final static Logger log = Logger.getLogger(SiteList.class);

    private Map<String, Long> hashListSite = new HashMap<String, Long>();
    
    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static SiteList backupObject = null;

    private final static Object syncObject = new Object();

    public static Long getSiteId(String serverName) {
        return SiteList.getInstance().searchSiteId(serverName);
    }

    public SiteList() {
    }

    public void reinit() {
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_) {
        lastReadData = 0;
    }

    protected void finalize() throws Throwable {
        if (hashListSite != null) {
            hashListSite.clear();
            hashListSite = null;
        }
        super.finalize();
    }

    public static SiteList getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("#15.01.01 lastReadData: " + lastReadData + ", current " + System.currentTimeMillis());
            log.debug("#15.01.02 LENGTH_TIME_PERIOD " + LENGTH_TIME_PERIOD + ", status " +
                (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || (backupObject == null)));
        }

        if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
            || (backupObject == null)) {
            synchronized (syncObject) {
                if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                    || (backupObject == null)) {
                    if (log.isDebugEnabled()) {
                        log.debug("#15.01.03 reinit cached value ");
                        log.debug("#15.01.04 old value " + backupObject);
                    }

                    backupObject = null;

                    SiteList site = new SiteList();
                    site.hashListSite = InternalDaoFactory.getInternalDao().getSiteIdMap();

                    backupObject = site;

                    if (log.isDebugEnabled()) log.debug("#15.01.05 new value " + backupObject);
                }
                else if (log.isDebugEnabled()) log.debug("Get from cache");

                if (log.isDebugEnabled()) log.debug("#15.01.09 ret value " + backupObject);

                lastReadData = System.currentTimeMillis();
            }
        }
        return backupObject;
    }

    private Long searchSiteId(final String serverName) {
        if (serverName == null) return null;
        Long siteId = hashListSite.get(serverName.toLowerCase());
        if (siteId == null && log.isInfoEnabled()) {
            log.info("site with serverName '" + serverName + "' not found");
            log.info("Dump map with current serverNames");
            for (String s : hashListSite.keySet()) {
                log.info("Value in map: " + s + ", value: " + hashListSite.get(s));
            }
        }
        return siteId;
    }

}
