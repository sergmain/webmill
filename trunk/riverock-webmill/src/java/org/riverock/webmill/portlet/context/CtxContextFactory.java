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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.SimpleStringTokenizer;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.FileManagerException;
import org.riverock.interfaces.portlet.menu.MenuLanguageInterface;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portlet.ContextFactory;
import org.riverock.webmill.portlet.PortletManager;
import org.riverock.webmill.portlet.PortletTools;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class CtxContextFactory extends ContextFactory {
    private final static Logger log = Logger.getLogger(CtxContextFactory.class);

    private CtxContextFactory(DatabaseAdapter adapter, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        super(adapter, request, portalInfo);
    }

    public static CtxContextFactory getInstance(DatabaseAdapter adapter, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        CtxContextFactory factory = new CtxContextFactory(adapter, request, portalInfo);

        if (factory.getDefaultPortletType()==null || factory.getNameTemplate()==null){
            // If not found context, processing as 'index_page'
            MenuLanguageInterface menu = portalInfo.getMenu(factory.realLocale.toString());
            if (menu==null){
                log.error( "menu is null, locale: "+factory.realLocale.toString() );
            }
            else
                factory.setPortletInfo( menu.getIndexMenuItem() );
        }
        return factory;
    }

    protected Long getCtxId(DatabaseAdapter db, HttpServletRequest request) throws PortalPersistenceException {

        String ctxType = request.getParameter( Constants.NAME_TYPE_CONTEXT_PARAM );

        String ctxTemplate =
            request.getParameter( Constants.NAME_TEMPLATE_CONTEXT_PARAM );

        PortletType portlet = null;
        try {
            portlet = PortletManager.getPortletDescription( ctxType );
        } catch (FileManagerException e) {
            log.error("Error getPortletDescription for type "+ctxType);
        }
        if (portlet!=null){
            namePortletId = PortletTools.getStringParam(portlet, PortletTools.name_portlet_id);
        }

        Long id = null;
        if (namePortletId!=null)
            id = ServletTools.getLong(request, namePortletId);

        setPortletInfo(id, ctxType, ctxTemplate);

        if ( log.isDebugEnabled() ){
            log.debug( "ctxTemplate: "+ctxTemplate );
            log.debug( "ctxType: "+ctxType );
            log.debug( "namePortletId: "+namePortletId );
            log.debug( "portletId: "+id );
        }


        Long ctxId = null;
        try {
            ctxId = DatabaseManager.getLongValue(
                db,
                "select a.ID_SITE_CTX_CATALOG " +
                "from   SITE_CTX_CATALOG a, SITE_CTX_LANG_CATALOG b, SITE_SUPPORT_LANGUAGE c, " +
                "       SITE_CTX_TYPE d, SITE_TEMPLATE e " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=? and c.CUSTOM_LANGUAGE=? and " +
                "       a.ID_SITE_CTX_TYPE=d.ID_SITE_CTX_TYPE and " +
                "       a.ID_SITE_TEMPLATE=e.ID_SITE_TEMPLATE and " +
                "       d.TYPE=? and e.NAME_SITE_TEMPLATE=? ",
                new Object[]{portalInfo.getSiteId(), realLocale.toString(), ctxType, ctxTemplate}
            );
        } catch (SQLException e) {
            log.error("Error get contextId from DB", e);
        }
        return ctxId;
    }

    protected void prepareParameters( HttpServletRequest httpRequest )  throws PortalException {
        try {
            if ( log.isDebugEnabled() ) {
                log.debug( "Start get parameters from http request, query: "+httpRequest.getQueryString() );
            }
            dynamicParameter = PortletTools.getParameters( httpRequest );

            if ( log.isDebugEnabled() ) {
                log.debug( "dynamicParameter: "+dynamicParameter );
                if ( dynamicParameter!=null ) {
                    Iterator it = dynamicParameter.keySet().iterator();
                    while (it.hasNext()) {
                        String key = (String)it.next();
                        Object obj = dynamicParameter.get( key );
                        if (obj instanceof List) {
                            log.debug( "Parameter, key: "+key );
                            Iterator ii = ((List)obj).iterator();
                            while (ii.hasNext()) {
                                log.debug( "               value: "+ii.next() );
                            }
                        }
                        else {
                            log.debug( "Parameter, key: "+key+", value: "+obj.toString() );
                        }
                    }
                }
            }
        }
        catch( Exception e ) {
            String es = "Error get parameters from http request";
            log.error( es, e );
            throw new PortalException( es, e );
        }

        String s = httpRequest.getServletPath();
        int idx = s.lastIndexOf( '/' );
        if (idx==-1)
            return;

        s = s.substring( 0, idx );

        String namespace = null;
        StringTokenizer st = new StringTokenizer( s, "/", false );

        while ( st.hasMoreTokens() ) {
            String element = st.nextToken();

            SimpleStringTokenizer p = new SimpleStringTokenizer( element, new String[]{",,"} );
            // get portlet namespace
            if ( p.hasMoreTokens() )
                namespace = p.nextToken();

            Map param = new HashMap();
            while ( p.hasMoreTokens() ) {
                String temp = p.nextToken();

                idx = temp.indexOf( ',' );
                if (idx==-1)
                    continue;

                String key = temp.substring( 0, idx );
                String value = temp.substring( idx+1 );

                MapWithParameters.put( param, key, value );
            }
            PortletParameters pp = new PortletParameters( namespace, param );
            portletsParameter.add( pp );
        }
    }
}
