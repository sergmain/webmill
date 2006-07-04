/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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

    @SuppressWarnings({"FinalizeDoesntCallSuperFinalize"})
    public void finalize() {
        if (hashListSite != null) hashListSite.clear();
        hashListSite = null;
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
