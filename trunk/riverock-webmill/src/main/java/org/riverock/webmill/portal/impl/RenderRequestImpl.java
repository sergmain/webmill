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
package org.riverock.webmill.portal.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.generic.InternalRequest;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.namespace.Namespace;

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
        final PortalRequest portalRequest,
        final Map<String, List<String>> renderParameters,
        final ServletContext servletContext,
        final String contextPath,
        final PortletPreferences portletPreferences,
        final Map<String, List<String>> portletProperties,
        final PortletContext portletContext,
        final PortletDefinition portletDefinition,
        final Namespace namespace,
        final PortalInfo portalInfo
    ) {

        super(
            servletContext, portalRequest.getHttpRequest(), portletPreferences,
            portletProperties, renderParameters, portletContext,
            portletDefinition, namespace
        );

        prepareRequest( parameters, portalRequest, contextPath, portalInfo );
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
