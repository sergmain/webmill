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

/**
 * $Id$
 */
public class UrlContextFactory extends ContextFactory {
    private static Logger log = Logger.getLogger(UrlContextFactory.class);

    private UrlContextFactory(DatabaseAdapter adapter, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        super(adapter, request, portalInfo);
    }

    public static UrlContextFactory getInstance(DatabaseAdapter db, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        UrlContextFactory factory = new UrlContextFactory(db, request, portalInfo);

        return factory;
    }

    protected Long getCtxId(DatabaseAdapter db, HttpServletRequest request) throws PortalPersistenceException {

        log.debug("Start process as page, format request: /<CONTEXT>/url/<LOCALE>,<PORTLET_TYPE>,<TEMPLATE_NAME>/<PARAMETER_OF_OTHER_PORTLET>/page/<URL_TO_PAGE>");
        // format request: /<CONTEXT>/url/<LOCALE>,<TEMPLATE_NAME>/<PARAMETER_OF_OTHER_PORTLET>/page/<URL_TO_PAGE>
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
            if (st.countTokens()!=3)
                return null;

            realLocale = StringTools.getLocale( st.nextToken() );
            String ctxType = st.nextToken();
            String ctxTemplate = st.nextToken();

            Long id = null;
//            if (namePortletId!=null)
//                id = ServletTools.getLong(request, namePortletId);

            setPortletInfo(id, ctxType, ctxTemplate);

            if ( log.isDebugEnabled() ){
                log.debug( "ctxTemplate: "+ctxTemplate );
                log.debug( "ctxType: "+ctxType );
                log.debug( "locale: "+realLocale );
                log.debug( "namePortletId: "+namePortletId );
                log.debug( "portletId: "+id );
            }
/*
            Long ctxId = null;
            ctxId = DatabaseManager.getLongValue(
                    db,
                    "select a.ID_SITE_CTX_CATALOG " +
                    "from   SITE_CTX_CATALOG a, SITE_CTX_LANG_CATALOG b, SITE_SUPPORT_LANGUAGE c " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                    "       c.ID_SITE=? and c.CUSTOM_LANGUAGE=? and " +
                    "       a.CTX_PAGE_URL=?",
                    new Object[]{portalInfo.getSiteId(), localeFromUrl, pageName}
            );
            return ctxId;
*/
            return null;
        }
/*
        catch (SQLException e) {
            String es = "Error get ID of context from DB";
            log.error(es, e);
            throw new PortalPersistenceException(es, e);
        }
*/
    }

    protected void prepareParameters( HttpServletRequest httpRequest )  throws PortalException {
        //To change body of created methods use File | Settings | File Templates.
    }
}
