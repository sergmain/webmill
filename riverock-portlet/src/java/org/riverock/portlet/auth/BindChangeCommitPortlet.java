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
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.portlet.tools.RequestTools;




/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 11:55:59 AM
 *
 * $Id$
 */
public final class BindChangeCommitPortlet implements Portlet {
    private final static Log log = LogFactory.getLog( BindChangeCommitPortlet.class );

    public BindChangeCommitPortlet() {
    }

    protected PortletConfig portletConfig = null;
    public void init(PortletConfig portletConfig) {
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

        Long idFirm = null;
        Long idService = null;
        Long idRoad = null;

        DatabaseAdapter dbDyn = null;
        String index_page = null;

        PreparedStatement ps = null;
        try {

            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( BindIndex.AUTH_BIND_ROLE ) ) {
                throw new PortletException( "You have not access right to bind right" );
            }

                    dbDyn = DatabaseAdapter.getInstance();
                    index_page = PortletService.url("mill.auth.bind", actionRequest, actionResponse );

                    Long id_auth_user = PortletService.getLong(actionRequest, "id_auth_user");
                    if (id_auth_user==null)
                        throw new IllegalArgumentException("id_auth_user not initialized");

                    AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
                    AuthInfo authInfoUser = AuthInfo.getInstance( dbDyn, id_auth_user );

                    boolean isRigthRelate = false;

                    if (authInfoUser!=null &&  authInfo!= null)
                    {
                        isRigthRelate =
                                InternalAuthProviderTools.checkRigthOnUser(dbDyn,
                                        authInfoUser.getAuthUserID(), authInfo.getAuthUserID());
                    }

                    if (isRigthRelate)
                    {
                        idFirm = InternalAuthProviderTools.initIdFirm(
                            dbDyn,
                            PortletService.getLong(actionRequest, InternalAuthProviderTools.firmIdParam),
                            authInfo.getUserLogin());
                        idService = InternalAuthProviderTools.initIdService(
                            dbDyn,
                            PortletService.getLong(actionRequest, InternalAuthProviderTools.serviceIdParam),
                            authInfo.getUserLogin());
                        idRoad = InternalAuthProviderTools.initIdRoad(
                            dbDyn,
                            PortletService.getLong(actionRequest, InternalAuthProviderTools.roadIdParam),
                            authInfo.getUserLogin());

                        if (log.isDebugEnabled())
                        {
                            log.debug("idFirm "+idFirm);
                            log.debug("idService "+idService);
                            log.debug("idRoad " + idRoad);

                            log.debug("user_login "+ RequestTools.getString(actionRequest, "user_login"));
                            log.debug("user_password "+ RequestTools.getString(actionRequest, "user_password"));

                            log.debug("is_service "+ (authInfo.getService()==1?
                                PortletService.getInt( actionRequest, "is_service", 0 ):
                                    0
                                    )
                            );
                            log.debug("is_road "+ (authInfo.getRoad()==1?
                                PortletService.getInt( actionRequest, "is_road", 0 ):
                                    0
                                    )
                            );
                            log.debug("is_use_current_firm "+ (authInfo.getUseCurrentFirm()==1?
                                PortletService.getInt( actionRequest, "is_use_current_firm", 0 ):
                                    0
                                    )
                            );
                            log.debug("id_auth_user "+ id_auth_user);
                            log.debug("auth_.getUserLogin() "+ auth_.getUserLogin() );
                        }

                        switch (dbDyn.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                ps = dbDyn.prepareStatement(
                                    "UPDATE AUTH_USER "+
                                    "SET "+
                                    "	user_login = ?, "+
                                    "	user_password = ?, "+
                                    "	is_service = ?, "+
                                    "	is_road = ?, "+
                                    "	is_use_current_firm = ?, "+
                                    "	ID_FIRM = ?, "+
                                    "	id_service = ?, "+
                                    "	id_road = ? "+
                                    "WHERE id_auth_user=? and " +
                                    "ID_FIRM  in ("+AuthHelper.getGrantedFirmId(dbDyn, auth_.getUserLogin())+") "
                                );

                                ps.setString(1, RequestTools.getString(actionRequest, "user_login"));
                                ps.setString(2, RequestTools.getString(actionRequest, "user_password"));

                                ps.setInt( 3,
                                    authInfo.getService() == 1
                                    ?PortletService.getInt( actionRequest, "is_service", 0 )==1 ?1 :0
                                    :0

                                );
                                ps.setInt( 4,
                                    authInfo.getRoad() == 1
                                    ?PortletService.getInt( actionRequest, "is_road", 0 )==1 ?1 :0
                                    :0
                                );
                                ps.setInt( 5,
                                    authInfo.getUseCurrentFirm() == 1
                                    ?PortletService.getInt( actionRequest, "is_use_current_firm", 0 )==1?1:0
                                    :0
                                );

                                if (idFirm != null)
                                    RsetTools.setLong(ps, 6, idFirm );
                                else
                                    ps.setNull(6, Types.INTEGER);

                                if (idService != null)
                                    RsetTools.setLong(ps, 7, idService );
                                else
                                    ps.setNull(7, Types.INTEGER);

                                if (idRoad != null)
                                    RsetTools.setLong(ps, 8, idRoad );
                                else
                                    ps.setNull(8, Types.INTEGER);

                                RsetTools.setLong(ps, 9, id_auth_user);

                                break;
                            default:
                                ps = dbDyn.prepareStatement(
                                    "UPDATE AUTH_USER "+
                                    "SET "+
                                    "	user_login = ?, "+
                                    "	user_password = ?, "+
                                    "	is_service = ?, "+
                                    "	is_road = ?, "+
                                    "	is_use_current_firm = ?, "+
                                    "	ID_FIRM = ?, "+
                                    "	id_service = ?, "+
                                    "	id_road = ? "+
                                    "WHERE id_auth_user=? and ID_FIRM in "+
                                    "(select z.ID_FIRM from v$_read_list_firm z where z.user_login = ? )"
                                );

                                ps.setString(1, RequestTools.getString(actionRequest, "user_login"));
                                ps.setString(2, RequestTools.getString(actionRequest, "user_password"));

                                ps.setInt( 3,
                                    authInfo.getService() == 1
                                    ?PortletService.getInt( actionRequest, "is_service", 0 )==1 ?1 :0
                                    :0

                                );
                                ps.setInt( 4,
                                    authInfo.getRoad() == 1
                                    ?PortletService.getInt( actionRequest, "is_road", 0 )==1 ?1 :0
                                    :0
                                );
                                ps.setInt( 5,
                                    authInfo.getUseCurrentFirm() == 1
                                    ?PortletService.getInt( actionRequest, "is_use_current_firm", 0 )==1?1:0
                                    :0
                                );

                                if (idFirm != null)
                                    RsetTools.setLong(ps, 6, idFirm );
                                else
                                    ps.setNull(6, Types.INTEGER);

                                if (idService != null)
                                    RsetTools.setLong(ps, 7, idService );
                                else
                                    ps.setNull(7, Types.INTEGER);

                                if (idRoad != null)
                                    RsetTools.setLong(ps, 8, idRoad );
                                else
                                    ps.setNull(8, Types.INTEGER);

                                RsetTools.setLong(ps, 9, id_auth_user);
                                ps.setString(10, auth_.getUserLogin() );

                                break;
                        }

                        int i1 = ps.executeUpdate();

                        if (log.isDebugEnabled())
                            log.debug("Count of updated records - "+i1);

                        dbDyn.commit();
                    }

                    actionResponse.sendRedirect( index_page );
                    return;
        }
        catch (Exception e) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            } catch(Exception e01){};

            final String es = "Error commit change bind";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            ps = null;
            dbDyn = null;
        }
    }
}
