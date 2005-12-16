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
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.webmill.container.tools.PortletService;




/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:12:28 PM
 *
 * $Id$
 */
public final class RightAddCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( RightAddCommitPortlet.class );

    public RightAddCommitPortlet() {
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
            ActionRequest renderRequest = (ActionRequest)request_;
            ActionResponse renderResponse = (ActionResponse)response;

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( RightIndex.AUTH_RIGHT_ROLE ) ) {
                throw new PortletException( "You have not enough right to set right" );
            }

            dbDyn = DatabaseAdapter.getInstance();

            String rightUrl = PortletService.url( "mill.auth.right", renderRequest, renderResponse );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            if ( authInfo.getRoot() != 1 ) {
                response.sendRedirect( rightUrl );
                return;
            }

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_AUTH_RELATE_RIGHT_ARM" );
            seq.setTableName( "WM_AUTH_RELATE_RIGHT_ARM" );
            seq.setColumnName( "ID_RELATE_RIGHT" );
            Long id = dbDyn.getSequenceNextValue( seq );

            ps = dbDyn.prepareStatement( "insert into WM_AUTH_RELATE_RIGHT_ARM (" +
                "	ID_RELATE_RIGHT, " +
                "	id_access_group, " +
                "	id_object_arm, " +
                "	code_right, " +
                "	is_road, " +
                "	is_service, " +
                "	is_firm)" +
                "values " +
                "(?, ?, ?, ?, ?, ?, ? )"
            );

            String right =
                ( Boolean.TRUE.equals( PortletService.getInt( renderRequest, "rs" ) ) ?"S" :"" ) +
                ( Boolean.TRUE.equals( PortletService.getInt( renderRequest, "ri" ) ) ?"I" :"" ) +
                ( Boolean.TRUE.equals( PortletService.getInt( renderRequest, "rd" ) ) ?"D" :"" ) +
                ( Boolean.TRUE.equals( PortletService.getInt( renderRequest, "ru" ) ) ?"U" :"" ) +
                ( Boolean.TRUE.equals( PortletService.getInt( renderRequest, "ra" ) ) ?"A" :"" );


            RsetTools.setLong( ps, 1, id );
            RsetTools.setLong( ps, 2, PortletService.getLong( renderRequest, "id_access_group" ) );
            RsetTools.setLong( ps, 3, PortletService.getLong( renderRequest, "id_object_arm" ) );

            ps.setString( 4, right );

            RsetTools.setLong( ps, 5, PortletService.getLong( renderRequest, "is_road" ) );
            RsetTools.setLong( ps, 6, PortletService.getLong( renderRequest, "is_service" ) );
            RsetTools.setLong( ps, 7, PortletService.getLong( renderRequest, "is_firm" ) );

            int i1 = ps.executeUpdate();
            if ( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            dbDyn.commit();
            response.sendRedirect( rightUrl );
            return;
        }
        catch( Exception e ) {
            try {
                if ( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error commit add right";
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
