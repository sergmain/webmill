package org.riverock.webmill.portal.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.dao.PortalDaoFactory;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:01:59
 *         $Id$
 */
public class SiteList {
    private final static Logger log = Logger.getLogger(SiteList.class);

    private Map<String, Long> hashListSite = new HashMap<String, Long>();

    public Long getLongIdSite(final String serverName) {
        if (serverName == null) return null;
        Long siteId = hashListSite.get(serverName.toLowerCase());
        if (siteId == null) {
            log.warn("site with serverName '" + serverName + "' not found");
            log.warn("Dump map with current serverNames");
            for (String s : hashListSite.keySet()) {
                log.warn("Value in map: " + s + ", value: " + hashListSite.get(s));
            }
        }
        return siteId;
    }

    public static Long getIdSite(String serverName) {
        return SiteList.getInstance().getLongIdSite(serverName);
    }

    public SiteList() {
    }

    public void reinit() {
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_) {
        lastReadData = 0;
    }

    public void finalize() {
        if (hashListSite != null) hashListSite.clear();
        hashListSite = null;
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static SiteList backupObject = null;
    private static Object syncObject = new Object();

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
                    site.hashListSite = PortalDaoFactory.getPortalDao().getSiteIdMap();

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

}
