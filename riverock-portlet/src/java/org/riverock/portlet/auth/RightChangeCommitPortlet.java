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
package org.riverock.portlet.auth;

import java.sql.PreparedStatement;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:12:43 PM
 *
 * $Id$
 */
public final class RightChangeCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( RightChangeCommitPortlet.class );

    public RightChangeCommitPortlet() {
    }

    protected PortletConfig portletConfig = null;
    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws PortletException {
        throw new PortletException( "render() method must never invoked" );
    }

    public void processAction( ActionRequest request_, ActionResponse response )
        throws PortletException {

        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            ActionRequest actionRequest = (ActionRequest)request_;
            ActionResponse actionResponse = (ActionResponse)response;

            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( RightIndex.AUTH_RIGHT_ROLE ) ) {
                throw new PortletException( "You have not enough right" );
            }

            String index_page = PortletService.url( "mill.auth.right", actionRequest, actionResponse );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            if ( authInfo.getRoot() != 1 ) {
                response.sendRedirect( index_page );
                return;
            }

            Long id_relate_right = PortletService.getLong( actionRequest, "id_relate_right" );
            if ( id_relate_right == null )
                throw new IllegalArgumentException( "id_relate_right not initialized" );

            Integer one = 1;
            String right =
                ( one.equals( PortletService.getInt( actionRequest, "rs" ) ) ?"S" :"" ) +
                ( one.equals( PortletService.getInt( actionRequest, "ri" ) ) ?"I" :"" ) +
                ( one.equals( PortletService.getInt( actionRequest, "rd" ) ) ?"D" :"" ) +
                ( one.equals( PortletService.getInt( actionRequest, "ru" ) ) ?"U" :"" ) +
                ( one.equals( PortletService.getInt( actionRequest, "ra" ) ) ?"A" :"" );

            dbDyn = DatabaseAdapter.getInstance();
            ps = dbDyn.prepareStatement( "update WM_AUTH_RELATE_RIGHT_ARM " +
                "SET " +
                "	id_access_group = ?, " +
                "	id_object_arm = ?, " +
                "	CODE_RIGHT = ?, " +
                "	is_road = ?, " +
                "	is_service = ?, " +
                "	is_firm = ? " +
                "WHERE " +
                "	id_relate_right = ?" );

            RsetTools.setLong( ps, 1, PortletService.getLong( actionRequest, "id_access_group" ) );
            RsetTools.setLong( ps, 2, PortletService.getLong( actionRequest, "id_object_arm" ) );
            ps.setString( 3, right );
            ps.setInt( 4, PortletService.getInt( actionRequest, "is_road", 0) );
            ps.setInt( 5, PortletService.getInt( actionRequest, "is_service", 0) );
            ps.setInt( 6, PortletService.getInt( actionRequest, "is_firm", 0) );
            RsetTools.setLong( ps, 7, id_relate_right );

            int i1 = ps.executeUpdate();

            if ( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            dbDyn.commit();

            response.sendRedirect( index_page );
            return;
        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error commit change right";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            ps = null;
            dbDyn = null;
        }
    }
}
