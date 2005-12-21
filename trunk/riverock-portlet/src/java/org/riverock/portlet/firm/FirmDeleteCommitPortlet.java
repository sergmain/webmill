/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.firm;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 10:37:10 AM
 *
 * $Id$
 */
public final class FirmDeleteCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( FirmDeleteCommitPortlet.class );

    public FirmDeleteCommitPortlet() {
    }

    protected PortletConfig portletConfig = null;

    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String ERROR_URL = "ERROR_URL";

    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws IOException {

        Writer out = renderResponse.getWriter();
        WebmillErrorPage.processPortletError(out, null,
            (String)renderRequest.getAttribute( ERROR_TEXT ),
            PortletService.url(ContainerConstants.CTX_TYPE_INDEX, renderRequest, renderResponse ),
            (String)renderRequest.getAttribute( ERROR_URL ));

        out.flush();
        out.close();
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse ) {

        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( "webmill.firm_delete" ) ) {
                throw new PortletSecurityException( "You have not enough right" );
            }

            dbDyn = DatabaseAdapter.getInstance();

            String index_page = PortletService.url( "mill.firm.index", actionRequest, actionResponse );

            Long id_firm = PortletService.getLong( actionRequest, "id_firm" );
            if ( id_firm == null )
                throw new IllegalArgumentException( "id_firm not initialized" );

            String sql =
                "update WM_LIST_COMPANY set is_deleted = 1 " +
                "where  ID_FIRM = ? and ID_FIRM in ";

            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = AuthHelper.getGrantedCompanyId( dbDyn, actionRequest.getRemoteUser() );

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement( sql );

            RsetTools.setLong( ps, 1, id_firm );
            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( 2, auth_.getUserLogin() );
                    break;
            }

            int i1 = ps.executeUpdate();

            if ( log.isDebugEnabled() )
                log.debug( "Count of deleted records - " + i1 );

            dbDyn.commit();
            actionResponse.sendRedirect( index_page );
            return;

        }
        catch( Exception e ) {
            try {
                dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            actionResponse.setRenderParameter(
                ERROR_TEXT,
                "Error delete info about firm " + WebmillErrorPage.getErrorMessage(e)
            );
            actionResponse.setRenderParameter( ERROR_URL, "continue" );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            ps = null;
            dbDyn = null;
        }
    }
}
