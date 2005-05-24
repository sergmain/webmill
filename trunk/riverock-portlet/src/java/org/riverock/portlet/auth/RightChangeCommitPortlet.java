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

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:12:43 PM
 *
 * $Id$
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

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.webmill.portlet.PortletTools;

public final class RightChangeCommitPortlet implements Portlet {

    private final static Logger log = Logger.getLogger( RightChangeCommitPortlet.class );

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

            String index_page = PortletTools.url( "mill.auth.right", actionRequest, actionResponse );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            if ( authInfo.getRoot() != 1 ) {
                response.sendRedirect( index_page );
                return;
            }

            Long id_relate_right = PortletTools.getLong( actionRequest, "id_relate_right" );
            if ( id_relate_right == null )
                throw new IllegalArgumentException( "id_relate_right not initialized" );

            Integer one = new Integer( 1 );
            String right =
                ( one.equals( PortletTools.getInt( actionRequest, "rs" ) ) ?"S" :"" ) +
                ( one.equals( PortletTools.getInt( actionRequest, "ri" ) ) ?"I" :"" ) +
                ( one.equals( PortletTools.getInt( actionRequest, "rd" ) ) ?"D" :"" ) +
                ( one.equals( PortletTools.getInt( actionRequest, "ru" ) ) ?"U" :"" ) +
                ( one.equals( PortletTools.getInt( actionRequest, "ra" ) ) ?"A" :"" );

            dbDyn = DatabaseAdapter.getInstance();
            ps = dbDyn.prepareStatement( "UPDATE AUTH_RELATE_RIGHT_ARM " +
                "SET " +
                "	id_access_group = ?, " +
                "	id_object_arm = ?, " +
                "	CODE_RIGHT = ?, " +
                "	is_road = ?, " +
                "	is_service = ?, " +
                "	is_firm = ? " +
                "WHERE " +
                "	id_relate_right = ?" );

            RsetTools.setLong( ps, 1, PortletTools.getLong( actionRequest, "id_access_group" ) );
            RsetTools.setLong( ps, 2, PortletTools.getLong( actionRequest, "id_object_arm" ) );
            ps.setString( 3, right );
            ps.setInt( 4, PortletTools.getInt( actionRequest, "is_road", new Integer( 0 ) ).intValue() );
            ps.setInt( 5, PortletTools.getInt( actionRequest, "is_service", new Integer( 0 ) ).intValue() );
            ps.setInt( 6, PortletTools.getInt( actionRequest, "is_firm", new Integer( 0 ) ).intValue() );
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
