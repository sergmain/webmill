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
 * Time: 12:12:56 PM
 * $Id$
 */
public final class RightDeleteCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( RightDeleteCommitPortlet.class );

    public RightDeleteCommitPortlet() {
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
            if ( auth_ == null || !auth_.isUserInRole( RightIndex.AUTH_RIGHT_ROLE ) ) {
                throw new PortletException( "You have not enough right" );
            }

            dbDyn = DatabaseAdapter.getInstance();

            String index_page = PortletService.url( "mill.auth.right", actionRequest, actionResponse );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            if ( authInfo.getRoot() != 1 ) {
                actionResponse.sendRedirect( index_page );
                return;
            }

            Long id_relate_right = PortletService.getLong( actionRequest, "id_relate_right" );
            if ( id_relate_right == null )
                throw new IllegalArgumentException( "id_relate_right not initialized" );


            ps = dbDyn.prepareStatement( "delete from AUTH_RELATE_RIGHT_ARM " +
                "where id_relate_right = ? " );

            RsetTools.setLong( ps, 1, id_relate_right );

            int i1 = ps.executeUpdate();

            if ( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            dbDyn.commit();
            actionResponse.sendRedirect( index_page );

            return;
        }
        catch( final Exception e ) {
            try {
                if ( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error commit delete right";
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
