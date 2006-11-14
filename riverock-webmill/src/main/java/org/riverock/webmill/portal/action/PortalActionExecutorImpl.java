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
package org.riverock.webmill.portal.action;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.action.PortalActionExecutor;
import org.riverock.webmill.google.sitemap.GoogleSitemapService;

/**
 * @author Sergei Maslyukov
 *         Date: 22.09.2006
 *         Time: 17:48:42
 *         <p/>
 *         $Id$
 */
public class PortalActionExecutorImpl implements PortalActionExecutor {
    private final static Logger log = Logger.getLogger( PortalActionExecutorImpl.class );

    private static Map<String, Integer> action = new HashMap<String, Integer>();

    private static final int CREATE_GOOGLE_SITEMAP_TYPE = 1;
    private static final String CREATE_GOOGLE_SITEMAP_ACTION = "create-google-sitemap";

    static {
        action.put(CREATE_GOOGLE_SITEMAP_ACTION, CREATE_GOOGLE_SITEMAP_TYPE);
    }

    private ClassLoader portalClassLoader;
    private Long siteId;
    private String virtualHostUrl;
    private String applicationPath;
    private String portalContext;

    public PortalActionExecutorImpl(ClassLoader portalClassLoader, Long siteId, String applicationPath, String virtualHostUrl, String portalContext) {
        this.portalClassLoader=portalClassLoader;
        this.siteId=siteId;
        this.virtualHostUrl=virtualHostUrl;
        this.applicationPath=applicationPath;
        this.portalContext=portalContext;
    }
    
    public Map<String, Object> execute(String command, Map<String, Object> parameters) {
        log.debug("Start PortalActionExecutorImpl.execute()");
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            Map<String, Object> result = new HashMap<String, Object>();

            Integer idx = action.get(command);
            if (idx==null) {
                log.warn("PortalAction '"+command+"' not registered");
                return result;
            }
            switch (idx) {
                case CREATE_GOOGLE_SITEMAP_TYPE:
                    GoogleSitemapService.createSitemap(siteId, virtualHostUrl, portalContext, applicationPath);
                    return result;
                default:
                    log.warn("Unknown value of actionIndex: " + idx);
                    return result;

            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }

    }
}
