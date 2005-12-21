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
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.tools.PortletService;




/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 11:56:12 AM
 *
 * $Id$
 */
public final class BindDeleteCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( BindDeleteCommitPortlet.class );

    public BindDeleteCommitPortlet() {
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

            String index_page = PortletService.url( "mill.auth.bind", actionRequest, actionResponse );

            Long id_auth_user = PortletService.getLong( actionRequest, "id_auth_user" );
            if ( id_auth_user == null )
                throw new PortletException( "id_auth_user not initialized" );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            AuthInfo authInfoUser = AuthInfo.getInstance( dbDyn, id_auth_user );

            boolean isRigthRelate = false;

            if ( authInfoUser != null && authInfo != null ) {
                isRigthRelate =
                    InternalAuthProviderTools.checkRigthOnUser( dbDyn,
                        authInfoUser.getAuthUserID(), authInfo.getAuthUserID() );
            }

            if ( isRigthRelate ) {

                switch( dbDyn.getFamaly() ) {
                    case DatabaseManager.MYSQL_FAMALY:
                        ps = dbDyn.prepareStatement( "delete from WM_AUTH_USER where id_auth_user=? " +
                            "and ID_FIRM in " +
                            "(" + AuthHelper.getGrantedCompanyId( dbDyn, auth_.getUserLogin() ) + ")" );

                        RsetTools.setLong( ps, 1, id_auth_user );
                        break;
                    default:
                        ps = dbDyn.prepareStatement( "delete from WM_AUTH_USER where id_auth_user=? " +
                            "and ID_FIRM in " +
                            "(select z.ID_FIRM from v$_read_list_firm z where z.user_login = ? )" );

                        RsetTools.setLong( ps, 1, id_auth_user );
                        ps.setString( 2, auth_.getUserLogin() );

                        break;
                }

                int i1 = ps.executeUpdate();

                if ( log.isDebugEnabled() )
                    log.debug( "Count of inserted records - " + i1 );

                dbDyn.commit();
            }
            actionResponse.sendRedirect( index_page );

        }
        catch( Exception e ) {
            try {
                if ( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error commit delete bind";
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
