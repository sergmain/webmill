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
import java.sql.Types;

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
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.portlet.tools.RequestTools;




/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 11:55:47 AM
 *
 * $Id$
 */
public final class BindAddCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( BindAddCommitPortlet.class );

    public BindAddCommitPortlet() {
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

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse )
        throws PortletException {

        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {

            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( BindIndex.AUTH_BIND_ROLE ) ) {
                throw new PortletException( "You have not right to bind right" );
            }

            dbDyn = DatabaseAdapter.getInstance();
            String index_page = null;
            index_page = PortletService.url( "mill.auth.bind", actionRequest, actionResponse );

            Long id_user = PortletService.getLong( actionRequest, "id_user" );
            if ( id_user == null )
                throw new IllegalArgumentException( "id_user not initialized" );

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_AUTH_USER" );
            seq.setTableName( "WM_AUTH_USER" );
            seq.setColumnName( "ID_AUTH_USER" );
            Long id = dbDyn.getSequenceNextValue( seq );

            ps = dbDyn.prepareStatement( "insert into WM_AUTH_USER " +
                "( ID_AUTH_USER, ID_FIRM, ID_SERVICE, ID_ROAD, " +
                "  ID_USER, USER_LOGIN, USER_PASSWORD, " +
                "IS_USE_CURRENT_FIRM, IS_SERVICE, IS_ROAD " +
                ") values (" +
                "?, " + // PK
                "?, " + // b1.idFirm, " +
                "?, " + // b2.id_service, " +
                "?, " + //b3.id_road, "+
                "?, ?, ?, ?, ?, ? " +
                ")"
            );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

            Long idFirm = null;
            Long idService = null;
            Long idRoad = null;

            idFirm = InternalAuthProviderTools.initIdFirm( dbDyn,
                PortletService.getLong( actionRequest, InternalAuthProviderTools.firmIdParam ),
                authInfo.getUserLogin() );
            idService = InternalAuthProviderTools.initIdService( dbDyn,
                PortletService.getLong( actionRequest, InternalAuthProviderTools.serviceIdParam ),
                authInfo.getUserLogin() );
            idRoad = InternalAuthProviderTools.initIdRoad( dbDyn,
                PortletService.getLong( actionRequest, InternalAuthProviderTools.roadIdParam ),
                authInfo.getUserLogin() );

            if ( log.isDebugEnabled() ) {
                log.debug( "idFirm " + idFirm );
                log.debug( "idService " + idService );
                log.debug( "idRoad " + idRoad );
            }

            RsetTools.setLong( ps, 1, id );
            if ( idFirm != null )
                RsetTools.setLong( ps, 2, idFirm );
            else
                ps.setNull( 2, Types.INTEGER );

            if ( idService != null )
                RsetTools.setLong( ps, 3, idService );
            else
                ps.setNull( 3, Types.INTEGER );

            if ( idRoad != null )
                RsetTools.setLong( ps, 4, idRoad );
            else
                ps.setNull( 4, Types.INTEGER );


            RsetTools.setLong( ps, 5, id_user );
            ps.setString( 6, RequestTools.getString( actionRequest, "user_login" ) );
            ps.setString( 7, RequestTools.getString( actionRequest, "user_password" ) );

            ps.setInt( 8,
                authInfo.getUseCurrentFirm() == 1
                ?PortletService.getInt( actionRequest, "is_use_current_firm", 0 )==1?1:0
                :0
            );
            ps.setInt( 9,
                authInfo.getService() == 1
                ?PortletService.getInt( actionRequest, "is_service", 0 )==1 ?1 :0
                :0

            );
            ps.setInt( 10,
                authInfo.getRoad() == 1
                ?PortletService.getInt( actionRequest, "is_road", 0 )==1 ?1 :0
                :0
            );
            int i1 = ps.executeUpdate();

            if ( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            dbDyn.commit();

            actionResponse.sendRedirect( index_page );
            return;
        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error commit add bind";
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
