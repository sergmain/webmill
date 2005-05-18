/*
 * org.riverock.webmill -- Portal framework implementation
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

package org.riverock.webmill.portlet.context;

import org.apache.log4j.Logger;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portlet.ContextFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;
import java.util.Map;

/**
 * $Id$
 */
public final class UrlContextFactory extends ContextFactory {
    private final static Logger log = Logger.getLogger(UrlContextFactory.class);

    private UrlContextFactory(DatabaseAdapter adapter, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        super(adapter, request, portalInfo);
    }

    public static ContextFactory getInstance(DatabaseAdapter db, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        UrlContextFactory factory = new UrlContextFactory(db, request, portalInfo);
        if (factory.getDefaultCtx()!=null)
            return factory;
        else
            return null;
    }

    public static final String URL_PAGE = "/page-";
    protected Long initPortalParameters(DatabaseAdapter db, HttpServletRequest request) throws PortalException {

        log.debug("Start process as page, format request: /<CONTEXT>/url/<LOCALE>,<PORTLET_TYPE>,<TEMPLATE_NAME>/<PARAMETER_OF_OTHER_PORTLET>/page-/<ID_CONTEXT><URL_TO_RESOURCE>");
        // format request: /<CONTEXT>/url/<LOCALE>,<TEMPLATE_NAME>/<PARAMETER_OF_OTHER_PORTLET>/page-/<ID_CONTEXT><URL_TO_PAGE>
        // URL_TO_PAGE: [/CONTEXT_NAME]/page-url (page-url is html, jsp or other page)


//        try
        {
            String path = request.getPathInfo();
            if (path==null || path.equals("/")) {
                return null;
            }

            if (log.isDebugEnabled()) log.debug("path: "+path);

            int idxSlash = path.indexOf('/', 1);
            if (log.isDebugEnabled()) log.debug("idxSlash: "+idxSlash);
            if (idxSlash==-1)
                return null;

            String localeFromUrl = null;
            localeFromUrl = path.substring(1, idxSlash);
            StringTokenizer st = new StringTokenizer(localeFromUrl, ",", false);
            if (log.isDebugEnabled()) log.debug("st.countTokens(): "+st.countTokens());

            if ( st.countTokens()!=4 )
                return null;

            realLocale = StringTools.getLocale( st.nextToken() );
            String ctxType = st.nextToken();
            String ctxTemplate = st.nextToken();
            Long ctxId = new Long( st.nextToken() );

            Long id = null;

            setPortletInfo( ctxTemplate );

            if ( log.isDebugEnabled() ) {
                log.debug( "ctxTemplate: " + ctxTemplate );
                log.debug( "ctxType: " + ctxType );
                log.debug( "locale: " + realLocale );
                log.debug( "portletId: " + id );
                log.debug( "part: " + path.substring( idxSlash ) );
            }

            idxSlash = path.indexOf( URL_PAGE, idxSlash );
            if (log.isDebugEnabled()) log.debug("idx of '" + URL_PAGE + "': " + idxSlash);
            if (idxSlash==-1)
                return null;

            urlResource = path.substring( idxSlash + URL_PAGE.length() );

            if (log.isDebugEnabled()) log.debug("urlResource: " + urlResource);
            return ctxId;
        }
    }

    protected void prepareParameters( HttpServletRequest httpRequest, Map httpRequestParameter )  throws PortalException {
        dynamicParameter = httpRequestParameter;
    }
}
