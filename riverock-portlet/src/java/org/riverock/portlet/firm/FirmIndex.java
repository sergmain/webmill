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

import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 9:09:43 AM
 *
 * $Id$
 */
public final class FirmIndex extends HttpServlet
{
    private final static Log log = LogFactory.getLog(FirmIndex.class);

    public FirmIndex()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws ServletException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null )
            {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");
                return;
            }

            db_ = DatabaseAdapter.getInstance();

            String index_page = PortletService.url("mill.firm.index", renderRequest, renderResponse );

            String v_str =
                "select ID_FIRM, full_name, short_name,\n"+
                "	    address, chief, buh, url, \n"+
                "	    short_info, is_work, is_search "+
                "from 	MAIN_LIST_FIRM "+
                "where  is_deleted=0 and ID_FIRM in ";

            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = AuthHelper.getGrantedFirmId(db_, renderRequest.getRemoteUser());

                    v_str += " ("+idList+") ";

                    break;
                default:
                    v_str +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }


            if (log.isDebugEnabled())
                log.debug(v_str);

            if ( auth_.isUserInRole("webmill.firm_select") ) {

                 out.write(
                         "<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\" width=\"770\">"+
                         "<tr>"+
                         "<td>"+
                         "<b>" + bundle.getString("index.jsp.title")+ "</b><br>"+
                         "<p><a href=\""+
                         PortletService.url("mill.firm.add_firm", renderRequest, renderResponse )+
                         "\">"+bundle.getString("button.add")+"</a></p>"+
                         "<table width=\"100%\" border=\"1\" class=\"l\">"+
                         "<tr>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.full_name")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.short_name")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.address")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.chief")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.short_info")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.is_work")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.is_search")+"</td>"+
                         "<th class=\"memberArea\" width=\"4%\">"+bundle.getString("index.jsp.action")+"</td>"+
                         "</tr>");


                    ps = db_.prepareStatement( v_str );
                    switch (db_.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:
                            break;
                        default:
                            ps.setString(1, auth_.getUserLogin());
                            break;
                    }

                    rs = ps.executeQuery();

                    while ( rs.next() )
                    {
                        out.write(

                                "<tr>"+
                                "<td class=\"memberArea\">"+ RsetTools.getString(rs, "full_name", "&nbsp;") +"</td>"+
                                "<td class=\"memberArea\">"+ RsetTools.getString(rs, "short_name", "&nbsp;") +"</td>"+
                                "<td class=\"memberArea\">"+ RsetTools.getString(rs, "address", "&nbsp;") +"</td>"+
                                "<td class=\"memberArea\">"+ RsetTools.getString(rs, "chief", "&nbsp;") +"</td>"+
                                "<td class=\"memberArea\">"+ RsetTools.getString(rs, "short_info", "&nbsp;") +"</td>"+
                                "<td class=\"memberArea\">"+ HtmlTools.printYesNo(rs, "is_work", false, bundle ) +"</td>"+
                                "<td class=\"memberArea\">"+ HtmlTools.printYesNo(rs, "is_search", false, bundle ) +"</td>"+
                                "<td class=\"memberAreaAction\">"
                        );

                        Long id_firm = RsetTools.getLong(rs, "ID_FIRM");

                        out.write(
                                "<input type=\"button\" value=\""+bundle.getString("button.change")+"\" onclick=\"location.href='"+
                                PortletService.url("mill.firm.ch_firm", renderRequest, renderResponse ) + '&'+
                                "id_firm="+id_firm +
                                "';\">"+
                                "<input type=\"button\" value=\""+bundle.getString("button.delete")+"\" onclick=\"location.href='"+
                                PortletService.url("mill.firm.del_firm", renderRequest, renderResponse ) + '&'+
                                "id_firm="+id_firm+
                                "';\">"+
                                "</td>"+
                                "</tr>"
                        );

                    }
                    out.write(
                            "</table>"+
                            "<p><a href=\""+

                            PortletService.url("mill.firm.add_firm", renderRequest, renderResponse )+

                            "\">"+bundle.getString("button.add")+"</a></p>"
                    );

                out.write(
                        "<p><a href=\""+ index_page +"\">"+bundle.getString("page.main.3")+"</a></p>"+
                        "</td>"+
                        "</tr>"+
                        "</table>"
                );

            }
        }
        catch (Exception e) {
            String es = "Error FirmIndex";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
