/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:01:59
 *         $Id$
 */
public class SiteList {
    private final static Logger log = Logger.getLogger(SiteList.class);

    private Map<String, VirtualHost> hashListSite = new HashMap<String, VirtualHost>();
    private Map<Long, VirtualHost> mapDefaultHost = new HashMap<Long, VirtualHost>();

    private static SiteList siteList;

    public static Long getSiteId(String serverName) {
        VirtualHost virtualHost = getSiteList().searchHost(serverName);

        return virtualHost!=null?virtualHost.getSiteId():null;
    }

    public static VirtualHost getVirtualHost(String serverName) {
        return getSiteList().searchHost(serverName);
    }

    public static VirtualHost getDefaultVirtualHost(Long siteId) {
        return getSiteList().searchDefaultVirtualHost(siteId);
    }

    public static void destroy() {
        synchronized(SiteList.class) {
            if (siteList==null) {
                return;
            }
            if (siteList.hashListSite!=null) {
                siteList.hashListSite.clear();
            }
            if (siteList.mapDefaultHost!=null) {
                siteList.mapDefaultHost.clear();
            }
        }
    }

    private static SiteList getSiteList() {
        if (siteList==null) {
            synchronized(SiteList.class) {
                if (siteList!=null) {
                    return siteList;
                }
                SiteList instance = getInstance();
                siteList = instance;
            }
        }
        return siteList;
    }

    private static SiteList getInstance() {
        return new SiteList();
    }

    private SiteList() {
        List<VirtualHost> hosts = InternalDaoFactory.getInternalVirtualHostDao().getVirtualHostsFullList();
        for (VirtualHost host : hosts) {
            hashListSite.put(host.getHost().toLowerCase(), host);
            if (host.isDefaultHost()) {
                mapDefaultHost.put(host.getSiteId(), host);
            }
        }
    }

    private VirtualHost searchHost(final String serverName) {
        if (serverName == null) {
            return null;
        }
        VirtualHost host = hashListSite.get(serverName.toLowerCase());
        if (host == null && log.isInfoEnabled()) {
            log.info("site with serverName '" + serverName + "' not found");
            log.info("Dump map with current serverNames");
            for (String s : hashListSite.keySet()) {
                log.info("Value in map: " + s + ", value: " + hashListSite.get(s));
            }
        }
        return host;
    }

    private VirtualHost searchDefaultVirtualHost(Long siteId) {
        if (siteId == null) {
            return null;
        }
        VirtualHost host = mapDefaultHost.get(siteId);
        if (host == null && log.isInfoEnabled()) {
            log.info("Default virtual host for siteId '" + siteId + "' not found");
            log.info("Dump map with current serverNames");
            for (Long id : mapDefaultHost.keySet()) {
                log.info("Value in map: " + id + ", value: " + mapDefaultHost.get(id));
            }
        }
        return host;
    }

}
