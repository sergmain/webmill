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
