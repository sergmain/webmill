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
package org.riverock.webmill.portal.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.interfaces.generic.InternalRequest;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:00:05 AM
 *
 * $Id$
 */
public final class RenderRequestImpl extends WebmillPortletRequest implements RenderRequest, InternalRequest {
    private final static Logger log = Logger.getLogger( RenderRequestImpl.class );

    public RenderRequestImpl(
        final Map<String, List<String>> parameters,
        final PortalRequestInstance portalRequestInstance,
        final Map<String, List<String>> renderParameters,
        final ServletContext servletContext,
        final String contextPath,
        final PortletPreferences portletPreferences,
        final Map<String, List<String>> portletProperties,
        final PortalContext portalContext,
        final PortletContext portletContext,
        final PortletDefinition portletDefinition,
        final Namespace namespace
    ) {

        super(
            servletContext, portalRequestInstance.getHttpRequest(), portletPreferences,
            portletProperties, renderParameters, portletContext,
            portletDefinition, namespace
        );

        prepareRequest( parameters, portalRequestInstance, contextPath, portalContext);
    }

    public void setIncluded(boolean included) {
        super.included=included;
    }

    public boolean isIncluded() {
        return super.included;
    }

    public void setIncludedQueryString(String queryString) {
        if (!included) {
            throw new IllegalStateException("Parameters cannot be appended to render request which is not included in a dispatch.");
        }
        if (StringUtils.isNotBlank(queryString)) {
            includedParameters = parseQueryString(queryString);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("No query string appended to the included request.");
            }
        }
    }

    /**
     * author: Apache groupl, project: Pluto, license: Apache2
     *
     * Parses the appended query string and merges the appended parameters to
     * the original parameters. Query parameters are name-value pairs separated
     * by the '<code>&amp;</code>' character.
     * @param queryString  the appended query string.
     */
    private Map<String, List<String>> parseQueryString(String queryString) {

        // Create the appended parameters map:
        //   key is the parameter name as a string,
        //   value is a List of parameter values (List of String).
        Map<String, List<String>> appendedParameters = new HashMap<String, List<String>>();

        // Parse the appended query string.
        if (log.isDebugEnabled()) {
            log.debug("Parsing appended query string: " + queryString);
        }
        try {
            StringTokenizer st = new StringTokenizer(queryString, "&", false);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int equalIndex = token.indexOf("=");
                if (equalIndex > 0) {
                    String value;
                    String key = URLDecoder.decode( token.substring(0, equalIndex), "utf-8" );
                    if (equalIndex < token.length() - 1) {
                        value = URLDecoder.decode( token.substring(equalIndex + 1), "utf-8" );
                    }
                    else {
                        value = "";
                    }
                    List<String> values = appendedParameters.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                    }
                    values.add(value);
                    appendedParameters.put(key, values);
                }
            }
        } catch (UnsupportedEncodingException e) {
            String es = "Error decode parameter string";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        if (log.isDebugEnabled()) {
            log.debug(appendedParameters.size() + " parameters appended.");
        }
        return appendedParameters;
    }
}
