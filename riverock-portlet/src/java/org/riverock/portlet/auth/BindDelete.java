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

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 11:55:24 AM
 *
 * $Id$
 */
public class BindDelete extends HttpServlet
{
    private static Log cat = LogFactory.getLog(BindDelete.class);

    public BindDelete()
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
        DatabaseAdapter db_ = null;
        try
        {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( BindIndex.AUTH_BIND_ROLE ) )
            {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/"+PortletService.ctx( renderRequest ), "continue");
                return;
            }

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

            db_ = DatabaseAdapter.getInstance();

            String index_page = PortletService.url("mill.auth.bind", renderRequest, renderResponse );

            Long id_auth_user = PortletService.getLong(renderRequest, "id_auth_user");
            if (id_auth_user==null)
                throw new IllegalArgumentException("id_auth_user not initialized");

            AuthInfo authInfoUser = AuthInfo.getInstance(db_, id_auth_user);
            boolean isRigthRelate = false;

            if (authInfoUser != null && authInfo != null)
            {
                isRigthRelate =
                    InternalAuthProviderTools.checkRigthOnUser(db_,
                        authInfoUser.getAuthUserID(), authInfo.getAuthUserID());
            }

            if (auth_.isUserInRole(BindIndex.AUTH_BIND_ROLE) && isRigthRelate)
            {

                out.write("\r\n");
                out.write(bundle.getString("del_bind.jsp.confirm"));
                out.write("\r\n");
                out.write("<BR>\r\n");
                out.write("<FORM ACTION=\"");
                out.write(

                    PortletService.url("mill.auth.commit_del_bind", renderRequest, renderResponse )

                );
                out.write("\" METHOD=\"POST\">\r\n");
                out.write("<INPUT TYPE=\"hidden\" NAME=\"id_auth_user\" VALUE=\"");
                out.write("" + id_auth_user);
                out.write("\">\r\n");
                out.write("<INPUT TYPE=\"submit\" VALUE=\"");
                out.write(bundle.getString("button.delete"));
                out.write("\">\n");
                out.write("\n");
                out.write("</FORM>\n");
                out.write("<TABLE  border=\"1\" width=\"100%\" class=\"l\">\n");
                out.write("<!--tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write("<%=bundle.getString(\"del_bind.jsp.name_firm\")%\\>");
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<%= RsetTools.getString(rs, \"name_firm\", \"&nbsp;\")%\\>\r\n");
                out.write("</td>\r\n");
                out.write("</tr-->\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("del_bind.jsp.user_login"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write(authInfoUser.getUserLogin());
                out.write("\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("del_bind.jsp.is_service"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write(HtmlTools.printYesNo(authInfoUser.getService(), false, bundle ));
                out.write("\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("del_bind.jsp.is_road"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write(HtmlTools.printYesNo(authInfoUser.getRoad(), false, bundle ));
                out.write("\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("del_bind.jsp.is_use_current_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write(HtmlTools.printYesNo(authInfoUser.getUseCurrentFirm(), false, bundle ));
                out.write("\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("</TABLE>\r\n");


            } // if ( getRight())

            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(bundle.getString("page.main.3"));
            out.write("</a>");
            out.write("</p>\r\n");

        }
        catch (Exception e)
        {
            cat.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
        finally
        {
            DatabaseAdapter.close(db_);
            db_ = null;
        }

    }


}
