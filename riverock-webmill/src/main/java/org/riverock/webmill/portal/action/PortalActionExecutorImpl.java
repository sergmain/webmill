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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.action.PortalActionExecutor;
import org.riverock.webmill.portal.action.google.sitemap.GoogleSitemapService;
import org.riverock.webmill.portal.action.menu.MenuItemsProvider;
import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalRequest;

/**
 * @author Sergei Maslyukov
 *         Date: 22.09.2006
 *         Time: 17:48:42
 *         <p/>
 *         $Id$
 */
public class PortalActionExecutorImpl implements PortalActionExecutor {
    private final static Logger log = Logger.getLogger( PortalActionExecutorImpl.class );

    private static enum PortalAction {CREATE_GOOGLE_SITEMAP_TYPE, GET_MENU_ITEMS}

    private static Map<String, PortalAction> actions = new HashMap<String, PortalAction>();

    private static final String CREATE_GOOGLE_SITEMAP_ACTION = "create-google-sitemap";
    private static final String GET_MENU_ITEMS = "get-menu-items";

    static {
        actions.put(CREATE_GOOGLE_SITEMAP_ACTION, PortalAction.CREATE_GOOGLE_SITEMAP_TYPE);
        actions.put(GET_MENU_ITEMS, PortalAction.GET_MENU_ITEMS);
    }

    private Long siteId;
    private String virtualHostUrl;
    private String applicationPath;
    private String portalContext;
    private PortalInstance portalInstance;
    private PortalRequest portalRequest;

    public PortalActionExecutorImpl(PortalInstance portalInstance, Long siteId, String applicationPath, String virtualHostUrl, String portalContext, PortalRequest portalRequest) {
        this.siteId=siteId;
        this.virtualHostUrl=virtualHostUrl;
        this.applicationPath=applicationPath;
        this.portalContext=portalContext;
        this.portalRequest = portalRequest;
        this.portalInstance = portalInstance;
    }
    
    public Map<String, Object> execute(String command, Map<String, Object> parameters) {
        log.debug("Start PortalActionExecutorImpl.execute()");
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalInstance.getPortalClassLoader() );

            Map<String, Object> result = new HashMap<String, Object>();

            PortalAction action = actions.get(command);
            if (action==null) {
                log.warn("PortalAction '"+command+"' not registered");
                return result;
            }
            switch (action) {
                case CREATE_GOOGLE_SITEMAP_TYPE:
                    GoogleSitemapService.createSitemap(siteId, virtualHostUrl, portalContext, applicationPath);
                    return result;
                case GET_MENU_ITEMS:
                    result.put("result", MenuItemsProvider.getMenuItems(portalInstance, portalRequest, siteId, parameters));
                    return result;
                default:
                    log.warn("Unknown value of actionIndex: " + action);
                    return result;
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
