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
 * Time: 10:36:55 AM
 *
 * $Id$
 */

package org.riverock.portlet.servlets.controller.firm;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;

public class FirmChangeCommit extends HttpServlet
{
    private static Logger cat = Logger.getLogger(FirmChangeCommit.class);

    public FirmChangeCommit()
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
        Writer out = null;
        try
        {

            CtxInstance ctxInstance =
                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );

            ContextNavigator.setContentType(response, "utf-8");

            out = response.getWriter();
            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( "webmill.firm_update" ) )
            {
                WebmillErrorPage.process(out, null, "You have not enough right", "/", "continue");
                return;
            }

            DatabaseAdapter dbDyn = null;
            String index_page = null;

            {
                PreparedStatement ps = null;
                try
                {
                    dbDyn = DatabaseAdapter.getInstance( true );

                    index_page = ctxInstance.url("mill.firm.index");

                    Long id_firm = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_firm");
                    if (id_firm==null)
                        throw new IllegalArgumentException("id_firm not initialized");

                    String sql =
                        "UPDATE MAIN_LIST_FIRM "+
                        "SET "+
                        "	full_name = ?, "+
                        "	short_name = ?, "+
                        "	address = ?, "+
                        "	telefon_buh = ?, "+
                        "	telefon_chief = ?, "+
                        "	chief = ?, "+
                        "	buh = ?, "+
                        "	fax = ?, "+
                        "	email = ?, "+
                        "	icq = ?, "+
                        "	short_client_info = ?, "+
                        "	url = ?, "+
                        "	short_info = ?, "+
                        "	is_work = ?, "+
                        "	is_need_recvizit = ?, "+
                        "	is_need_person = ?, "+
                        "	is_search = ? "+
                        "WHERE ID_FIRM = ? and ID_FIRM in ";


                    switch (dbDyn.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:
                            String idList = AuthHelper.getGrantedFirmId(dbDyn, ctxInstance.getPortletRequest().getRemoteUser());

                            sql += " ("+idList+") ";

                            break;
                        default:
                            sql +=
                                "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                            break;
                    }

                    ps = dbDyn.prepareStatement(sql);
                   int num=1;
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "full_name"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "short_name"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "address"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "telefon_buh"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "telefon_chief"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "chief"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "buh"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "fax"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "email"));
                    RsetTools.setLong(ps, num++, PortletTools.getLong(ctxInstance.getPortletRequest(), "icq"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "short_client_info"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "url"));
                    ps.setString(num++, PortletTools.getString(ctxInstance.getPortletRequest(), "short_info"));
                    RsetTools.setLong(ps, num++, PortletTools.getLong(ctxInstance.getPortletRequest(), "is_work"));
                    RsetTools.setLong(ps, num++, PortletTools.getLong(ctxInstance.getPortletRequest(), "is_need_recvizit"));
                    RsetTools.setLong(ps, num++, PortletTools.getLong(ctxInstance.getPortletRequest(), "is_need_person"));
                    RsetTools.setLong(ps, num++, PortletTools.getLong(ctxInstance.getPortletRequest(), "is_search"));
                    RsetTools.setLong(ps, num++, id_firm);
                    switch (dbDyn.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:
                            break;
                        default:
                            ps.setString(32, auth_.getUserLogin() );
                            break;
                    }

                    int i1 = ps.executeUpdate();

                    if(cat.isDebugEnabled())
                        cat.debug("Count of updated record - "+i1);

                    dbDyn.commit();
                    response.sendRedirect( index_page );
                    return;

                }
                catch(Exception e1)
                {
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
        }
        catch (Exception e)
        {
            cat.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
    }
}
