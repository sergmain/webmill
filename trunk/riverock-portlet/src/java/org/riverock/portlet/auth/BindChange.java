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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.sso.main.MainUserInfo;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.tools.PortletService;

public class BindChange extends HttpServlet
{
    private static Log log = LogFactory.getLog(BindChange.class);

    public BindChange()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
        throws IOException, ServletException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ResourceBundle bundle = ResourceBundle.getBundle("org.riverock.portlet.resource.AuthUser", renderRequest.getLocale() );

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
            MainUserInfo userInfo = null;

            if (authInfoUser != null && authInfo != null)
            {
                isRigthRelate =
                    InternalAuthProviderTools.checkRigthOnUser(db_,
                        authInfoUser.getAuthUserID(), authInfo.getAuthUserID());
                userInfo = MainUserInfo.getInstance( authInfoUser.getUserLogin());
            }

            if (isRigthRelate)
            {


                out.write("<FORM ACTION=\"");
                out.write(

                    PortletService.url("mill.auth.commit_ch_bind", renderRequest, renderResponse )

                );
                out.write("\" METHOD=\"POST\">\r\n");
                out.write("<input type=\"submit\" value=\"");
                out.write(bundle.getString("button.change"));
                out.write("\">\r\n");
                out.write("<INPUT TYPE=\"hidden\" NAME=\"id_auth_user\" VALUE=\"");
                out.write("" + id_auth_user);
                out.write("\">\r\n");
                out.write("<TABLE  border=\"0\" width=\"100%\" class=\"l\">\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"20%\" class=\"par\">");
                out.write(bundle.getString("ch_bind.jsp.fio"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write(userInfo.getLastName() + "&nbsp;" + userInfo.getFirstName() + "&nbsp;" + userInfo.getMiddleName());
                out.write("\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(bundle.getString("ch_bind.jsp.user_login"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write("<input type=\"text\" name=\"user_login\" size=\"20\" maxlength=\"20\"  \r\nvalue=\"");
                out.write(authInfoUser.getUserLogin());
                out.write("\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(bundle.getString("ch_bind.jsp.user_password"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write("<input type=\"text\" name=\"user_password\" size=\"20\" maxlength=\"20\"\r\nvalue=\"");
                out.write(authInfoUser.getUserPassword());
                out.write("\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(bundle.getString("ch_bind.jsp.user_password_check"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write("<input type=\"text\" name=\"user_password_resume\" size=\"20\" maxlength=\"20\" \r\nvalue=\"");
                out.write(authInfoUser.getUserPassword());
                out.write("\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(bundle.getString("ch_bind.jsp.is_use_current_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" witdth=\"30%\">\r\n");
                out.write("<select name=\"is_use_current_firm\" size=\"1\">\r\n");
                out.write(HtmlTools.printYesNo(authInfoUser.getUseCurrentFirm(), true, bundle ));
                out.write("\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("<td align=\"right\" width=\"20%\" class=\"par\">");
                out.write(bundle.getString("ch_bind.jsp.list_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"id_firm\" size=\"1\">");

                switch(db_.getFamaly())
                {
                    case DatabaseManager.MYSQL_FAMALY:

                        String sql_ =
                            "select b.ID_FIRM id, b.FULL_NAME NAME_FIRM "+
                            "from   WM_LIST_COMPANY b "+
                            "where  b.ID_FIRM in ("+AuthHelper.getGrantedCompanyId(db_, auth_.getUserLogin())+") and b.is_deleted=0 "+
                            "order  by b.ID_FIRM ASC ";

                        if (log.isDebugEnabled())
                            log.debug("Firm list sql:\n"+sql_);

                        // Todo fix SQL parser to support MySQL concat() function
                        ps = db_.getConnection().prepareStatement(sql_);
//                    ps = db_.prepareStatement(sql_);
                        break;
                    default:
                        ps = db_.prepareStatement(
                            "select b.ID_FIRM id, b.FULL_NAME NAME_FIRM "+
                            "from   v$_read_list_firm a, WM_LIST_COMPANY b "+
                            "where  a.ID_FIRM = b.ID_FIRM and b.is_deleted=0 and a.user_login=? "+
                            "order  by b.ID_FIRM ASC "
                        );
                        ps.setString(1, auth_.getUserLogin());
                        break;
                }
                getOptions(ps, rs, out, authInfoUser.getFirmID());
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;

                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n\r\n");


                if (authInfo.getService() == 1)
                {

                    out.write("\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(bundle.getString("ch_bind.jsp.is_service"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"is_service\" size=\"1\">\r\n");
                    out.write(HtmlTools.printYesNo(authInfoUser.getService(), true, bundle ));
                    out.write("\r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(bundle.getString("ch_bind.jsp.id_service"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_service\" size=\"1\">");
                    switch(db_.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:

                            ps = db_.prepareStatement(
                                "select  a.id_service, full_name_service "+
                                "from    WM_LIST_GROUP_COMPANY a "+
                                "where   a.id_service in ( "+AuthHelper.getGrantedGroupCompanyId(db_, auth_.getUserLogin())+")"+
                                "order   by id_service ASC "
                            );
                            break;
                        default:
                            ps = db_.prepareStatement(
                                "select id_service, full_name_service "+
                                "from   V_AUTH_RELATE_SERVICE "+
                                "where  user_login=? "+
                                "order  by id_service ASC "
                            );
                            ps.setString(1, auth_.getUserLogin());
                            break;
                    }

                    getOptions(ps, rs, out, authInfoUser.getServiceID());
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");

                } // if (isService==1)
                out.write("\r\n");

                if (authInfo.getRoad() == 1)
                {

                    out.write("\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(bundle.getString("ch_bind.jsp.is_road"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"is_road\" size=\"1\">\r\n");
                    out.write(HtmlTools.printYesNo(authInfoUser.getRoad(), true, bundle ));
                    out.write("\r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(bundle.getString("ch_bind.jsp.id_road"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_road\" size=\"1\">");

                    switch(db_.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:

                            ps = db_.prepareStatement(
                                "select  a.id_road, full_name_road "+
                                "from    WM_LIST_HOLDING a "+
                                "where   a.id_road in ( "+AuthHelper.getGrantedHoldingId(db_, auth_.getUserLogin())+")"+
                                "order   by id_road ASC "
                            );
                            break;
                        default:
                            ps = db_.prepareStatement(
                                "select id_road, full_name_road "+
                                "from   V_AUTH_RELATE_road "+
                                "where  user_login=? "+
                                "order  by id_road ASC "
                            );
                            ps.setString(1, auth_.getUserLogin());
                            break;
                    }

                    getOptions(ps, rs, out, authInfoUser.getRoadID());
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");

                } // if (isRoad==1)
                out.write("\r\n");
                out.write("</TABLE>\r\n");
                out.write("<BR>\r\n");
                out.write("<INPUT TYPE=\"submit\" VALUE=\"");
                out.write(bundle.getString("button.change"));
                out.write("\">\r\n");
                out.write("</FORM>\r\n");
                out.write("<BR>");


            }

            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(bundle.getString("page.main.3"));
            out.write("</a>");
            out.write("</p>\r\n");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("</table>\r\n");

        }
        catch (Exception e)
        {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
        finally
        {
            DatabaseManager.close(db_,rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    private void getOptions(PreparedStatement ps, ResultSet rs, Writer out, Long value) throws SQLException, IOException {
        rs = ps.executeQuery();

        String v_str;
        String v_select;
        long v_num;
        while (rs.next())
        {
            v_num = rs.getLong(1);
            v_str = rs.getString(2);
            if (v_str==null)
                v_str = ""+v_num+", unknown value";

            if (value!=null && v_num == value)
                v_select = " SELECTED";
            else
                v_select = "";

            out.write("<option" + v_select + " value=\"" + v_num + "\">" +
                    v_num + ", " + v_str.replace('\"', '\'') +
                    "</option>\n");

        }
    }
}
