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
 * Time: 11:55:15 AM
 *
 * $Id$
 */

package org.riverock.portlet.servlets.view.auth;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.PortletConfig;

import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.StringManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.sso.main.MainUserInfo;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;


public class BindChange extends HttpServlet
{
    private static Logger log = Logger.getLogger(BindChange.class);

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

//            CtxInstance ctxInstance =
//                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            RenderRequest renderRequest = null;
            PortletConfig portletConfig = null;

            out = response.getWriter();
            ContextNavigator.setContentType(response);

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( BindIndex.AUTH_BIND_ROLE ) )
            {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/"+CtxInstance.ctx(), "continue");
                return;
            }

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

            db_ = DatabaseAdapter.getInstance(false);

            // Todo. After implement this portlet as 'real' portlet, not servlet,
            // remove following code, and switch to 'ctxInstance.sCustom' field
            // start from
            StringManager sCustom = null;
            String nameLocaleBundle = null;
            nameLocaleBundle = "mill.locale.AUTH_USER";
            if ((nameLocaleBundle != null) && (nameLocaleBundle.trim().length() != 0))
                sCustom = StringManager.getManager(nameLocaleBundle, renderRequest.getLocale());
            // end where

            String index_page = CtxInstance.url("mill.auth.bind");

            Long id_auth_user = PortletTools.getLong(renderRequest, "id_auth_user");
            if (id_auth_user==null)
                throw new IllegalArgumentException("id_auth_user not initialized");

            AuthInfo authInfoUser = AuthInfo.getInstance(db_, id_auth_user);
            boolean isRigthRelate = false;
            MainUserInfo userInfo = null;

            if (authInfoUser != null && authInfo != null)
            {
                isRigthRelate =
                    InternalAuthProviderTools.checkRigthOnUser(db_,
                        authInfoUser.authUserID, authInfo.authUserID);
                userInfo = MainUserInfo.getInstance( authInfoUser.userLogin);
            }

            if (isRigthRelate)
            {


                out.write("<FORM ACTION=\"");
                out.write(

                    CtxInstance.url("mill.auth.commit_ch_bind")

                );
                out.write("\" METHOD=\"POST\">\r\n");
                out.write("<input type=\"submit\" value=\"");
                out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("button.change"));
                out.write("\">\r\n");
                out.write("<INPUT TYPE=\"hidden\" NAME=\"id_auth_user\" VALUE=\"");
                out.write("" + id_auth_user);
                out.write("\">\r\n");
                out.write("<TABLE  border=\"0\" width=\"100%\" class=\"l\">\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"20%\" class=\"par\">");
                out.write(sCustom.getStr("ch_bind.jsp.fio"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write(userInfo.getLastName() + "&nbsp;" + userInfo.getFirstName() + "&nbsp;" + userInfo.getMiddleName());
                out.write("\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(sCustom.getStr("ch_bind.jsp.user_login"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write("<input type=\"text\" name=\"user_login\" size=\"20\" maxlength=\"20\"  \r\nvalue=\"");
                out.write(authInfoUser.userLogin);
                out.write("\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(sCustom.getStr("ch_bind.jsp.user_password"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write("<input type=\"text\" name=\"user_password\" size=\"20\" maxlength=\"20\"\r\nvalue=\"");
                out.write(authInfoUser.userPassword);
                out.write("\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(sCustom.getStr("ch_bind.jsp.user_password_check"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" colspan=\"3\">\r\n");
                out.write("<input type=\"text\" name=\"user_password_resume\" size=\"20\" maxlength=\"20\" \r\nvalue=\"");
                out.write(authInfoUser.userPassword);
                out.write("\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" class=\"par\">");
                out.write(sCustom.getStr("ch_bind.jsp.is_use_current_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\" witdth=\"30%\">\r\n");
                out.write("<select name=\"is_use_current_firm\" size=\"1\">\r\n");
                out.write(HtmlTools.printYesNo(authInfoUser.isUseCurrentFirm, true, renderRequest.getLocale()));
                out.write("\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("<td align=\"right\" width=\"20%\" class=\"par\">");
                out.write(sCustom.getStr("ch_bind.jsp.list_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"id_firm\" size=\"1\">");

                switch(db_.getFamaly())
                {
                    case DatabaseManager.MYSQL_FAMALY:

                        String sql_ =
                            "select b.id_firm id, concat(b.id_firm, ', ', b.full_name) NAME_FIRM "+
                            "from   main_LIST_FIRM b "+
                            "where  b.ID_FIRM in ("+AuthHelper.getGrantedFirmId(db_, auth_.getUserLogin())+") and b.is_deleted=0 "+
                            "order  by b.ID_FIRM ASC ";

                        if (log.isDebugEnabled())
                            log.debug("Firm list sql:\n"+sql_);

                        // Todo fix SQL parser to support MySQL connect() funcation
                        ps = db_.getConnection().prepareStatement(sql_);
//                    ps = db_.prepareStatement(sql_);
                        break;
                    default:
                        ps = db_.prepareStatement(
                            "select b.ID_FIRM id, ''||b.id_firm||', '||B.full_name NAME_FIRM "+
                            "from   v$_read_list_firm a, main_LIST_FIRM b "+
                            "where  a.ID_FIRM = b.ID_FIRM and b.is_deleted=0 and a.user_login=? "+
                            "order  by b.ID_FIRM ASC "
                        );
                        ps.setString(1, auth_.getUserLogin());
                        break;
                }
                getOptions(ps, rs, out, authInfoUser.firmID);
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;

//                out.write(
//                    Client.make_list_prn(authInfoUser.firmID, db_, "v_list_read_firm", "id",
//                        "name_firm", " where user_login = '" + auth_.getUserLogin() + "'", null, null)
//                );
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n\r\n");


                if (authInfo.isService == 1)
                {

                    out.write("\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(sCustom.getStr("ch_bind.jsp.is_service"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"is_service\" size=\"1\">\r\n");
                    out.write(HtmlTools.printYesNo(authInfoUser.isService, true, renderRequest.getLocale()));
                    out.write("\r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(sCustom.getStr("ch_bind.jsp.id_service"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_service\" size=\"1\">");
                    switch(db_.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:

                            ps = db_.prepareStatement(
                                "select  a.id_service, full_name_service "+
                                "from    main_list_service a "+
                                "where   a.id_service in ( "+AuthHelper.getGrantedServiceId(db_, auth_.getUserLogin())+")"+
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

                    getOptions(ps, rs, out, authInfoUser.serviceID);
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

//                    out.write( Client.make_list_prn(authInfoUser.serviceID, db_, "v_auth_relate_service",
//                            "id_service", "full_name_service",
//                            " where user_login = '" + auth_.getUserLogin() + "' ", null, null));

                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");

                } // if (isService==1)
                out.write("\r\n");

                if (authInfo.isRoad == 1)
                {

                    out.write("\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(sCustom.getStr("ch_bind.jsp.is_road"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"is_road\" size=\"1\">\r\n");
                    out.write(HtmlTools.printYesNo(authInfoUser.isRoad, true, renderRequest.getLocale()));
                    out.write("\r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("<td align=\"right\" class=\"par\">");
                    out.write(sCustom.getStr("ch_bind.jsp.id_road"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_road\" size=\"1\">");

                    switch(db_.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:

                            ps = db_.prepareStatement(
                                "select  a.id_road, full_name_road "+
                                "from    main_list_road a "+
                                "where   a.id_road in ( "+AuthHelper.getGrantedRoadId(db_, auth_.getUserLogin())+")"+
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

                    getOptions(ps, rs, out, authInfoUser.roadID);
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

//                    out.write(Client.make_list_prn(authInfoUser.roadID, db_, "v_auth_relate_road",
//                            "id_road", "full_name_road",
//                            " where user_login = '" + auth_.getUserLogin() + "' ", null, null));

                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");

                } // if (isRoad==1)
                out.write("\r\n");
                out.write("</TABLE>\r\n");
                out.write("<BR>\r\n");
                out.write("<INPUT TYPE=\"submit\" VALUE=\"");
                out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("button.change"));
                out.write("\">\r\n");
                out.write("</FORM>\r\n");
                out.write("<BR>");


            }

            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("page.main.3"));
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
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;

            DatabaseAdapter.close(db_);
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

            if (v_str == null) v_str = "";

            if (v_num == value.longValue())
                v_select = " SELECTED";
            else
                v_select = "";

            out.write("<option" + v_select + " value=\"" + v_num + "\">" +
                    v_str.replace('\"', '\'') +
                    "</option>\n");

        }
    }
}
