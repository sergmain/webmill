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
 * Time: 11:55:47 AM
 *
 * $Id$
 */

package org.riverock.portlet.servlets.controller.auth;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.portlet.portlets.WebmillErrorPage;


public class BindAddCommit extends HttpServlet
{
    private static Logger cat = Logger.getLogger(BindAddCommit.class);

    public BindAddCommit()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (cat.isDebugEnabled())
            cat.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        Long idFirm = null;
        Long idService = null;
        Long idRoad = null;

        Writer out = null;
        try
        {

//            CtxInstance ctxInstance =
//                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            RenderRequest renderRequest = null;

            ContextNavigator.setContentType(response, "utf-8");

            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( "webmill.auth_bind" ) )
            {
                WebmillErrorPage.process(out, null, "You have not right to bind right", "/"+CtxInstance.ctx(), "continue");
                return;
            }

            DatabaseAdapter dbDyn = null;
            String index_page = null;

                PreparedStatement ps = null;
                try{
                    dbDyn = DatabaseAdapter.getInstance( true );
                    index_page = CtxInstance.url("mill.auth.bind");

                    Long id_user = PortletTools.getLong(renderRequest, "id_user");
                    if (id_user==null)
                        throw new IllegalArgumentException("id_user not initialized");

                    CustomSequenceType seq = new CustomSequenceType();
                    seq.setSequenceName("seq_AUTH_USER");
                    seq.setTableName( "AUTH_USER");
                    seq.setColumnName( "ID_AUTH_USER" );
                    Long id = new Long(dbDyn.getSequenceNextValue( seq ));

                    ps = dbDyn.prepareStatement(
                        "insert into AUTH_USER "+
                        "( ID_AUTH_USER, ID_FIRM, ID_SERVICE, ID_ROAD, "+
                        "  ID_USER, USER_LOGIN, USER_PASSWORD, " +
                        "IS_USE_CURRENT_FIRM, IS_SERVICE, IS_ROAD "+
                        ") values ("+
                        "?, " + // PK
                        "?, " + // b1.idFirm, " +
                        "?, " + // b2.id_service, " +
                        "?, " + //b3.id_road, "+
                        "?, ?, ?, ?, ?, ? " +
                        ")"
/*
                        "  from "+
                        "    (select USER_LOGIN from AUTH_USER where USER_LOGIN=? ) a, "+
                        "    (select ID_FIRM, USER_LOGIN from v$_read_list_firm where USER_LOGIN=? and ID_FIRM=?) b1, "+
                        "    (select id_service, USER_LOGIN from v$_read_list_service where USER_LOGIN = ? and id_service=?) b2, "+
                        "    (select id_road, USER_LOGIN from v$_read_list_road where USER_LOGIN = ? and id_road=?) b3 "+
                        " where  a.USER_LOGIN = b1.USER_LOGIN(+) and "+
                        "    a.USER_LOGIN = b2.USER_LOGIN(+) and "+
                        "    a.USER_LOGIN = b3.USER_LOGIN(+) "
*/
                    );

                    AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

                    idFirm = InternalAuthProviderTools.initIdFirm(
                        dbDyn,
                        PortletTools.getLong(renderRequest, InternalAuthProviderTools.firmIdParam),
                        authInfo.userLogin);
                    idService = InternalAuthProviderTools.initIdService(
                        dbDyn,
                        PortletTools.getLong(renderRequest, InternalAuthProviderTools.serviceIdParam),
                        authInfo.userLogin);
                    idRoad = InternalAuthProviderTools.initIdRoad(
                        dbDyn,
                        PortletTools.getLong(renderRequest, InternalAuthProviderTools.roadIdParam),
                        authInfo.userLogin);

                    if (cat.isDebugEnabled())
                    {
                        cat.debug("idFirm "+idFirm);
                        cat.debug("idService "+idService);
                        cat.debug("idRoad " + idRoad);
                    }

                    RsetTools.setLong(ps, 1, id );
                    if (idFirm != null)
                        RsetTools.setLong(ps, 2, idFirm );
                    else
                        ps.setNull(2, Types.INTEGER);

                    if (idService != null)
                        RsetTools.setLong(ps, 3, idService );
                    else
                        ps.setNull(3, Types.INTEGER);

                    if (idRoad != null)
                        RsetTools.setLong(ps, 4, idRoad );
                    else
                        ps.setNull(4, Types.INTEGER);


                    RsetTools.setLong(ps, 5, id_user );
                    ps.setString(6, PortletTools.getString(renderRequest, "user_login"));
                    ps.setString(7, PortletTools.getString(renderRequest, "user_password"));

                    RsetTools.setInt(ps, 8, (authInfo.isUseCurrentFirm==1?
                            PortletTools.getInt(renderRequest, "is_use_current_firm"):
                            null
                            )
                    );
                    RsetTools.setInt(ps, 9, (authInfo.isService==1?
                            PortletTools.getInt(renderRequest, "is_service"):
                            null
                            )
                    );
                    RsetTools.setInt(ps, 10, (authInfo.isRoad==1?
                            PortletTools.getInt(renderRequest, "is_road"):
                            null
                            )
                    );
                    int i1 = ps.executeUpdate();

                    if (cat.isDebugEnabled())
                        cat.debug("Count of inserted records - "+i1);

                    dbDyn.commit();

                    response.sendRedirect( index_page );
                    return;
                }
                catch(Exception e1)
                {
                    cat.error("Error commit add bind", e1);
                    try {
                        dbDyn.rollback();
                    }catch(Exception e001){}
                    out.write("<html><head></head<body>" +
                            "Error while processing this page:<br>"+
                            ExceptionTools.getStackTrace(e1, 20, "<br>")+"<br>" +
                            "<p><a href=\"" + index_page+ "\">continue</a></p>" +
                            "</body></html>"
                    );
                }
                finally
                {
                    if (ps != null)
                    {
                        try {
                            ps.close();
                            ps = null;
                        } catch(Exception e) {}
                    }
                    if (dbDyn !=null)
                    {
                        DatabaseAdapter.close( dbDyn );
                        dbDyn = null;
                    }
                }
        }
        catch (Exception e)
        {
            cat.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
    }
}
