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
 * Time: 11:54:15 AM
 *
 * $Id$
 */

package org.riverock.portlet.servlets.view.auth;

import org.apache.log4j.Logger;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.StringManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.PortletConfig;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BindAdd extends HttpServlet
{
    private static Logger log = Logger.getLogger(BindAdd.class);

    public BindAdd()
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

            ContextNavigator.setContentType(response, "utf-8");

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( BindIndex.AUTH_BIND_ROLE ) )
            {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/"+CtxInstance.ctx(), "continue");
                return;
            }

            // Todo. After implement this portlet as 'real' portlet, not servlet,
            // remove following code, and switch to 'ctxInstance.sCustom' field
            // start from
            StringManager sCustom = null;
            String nameLocaleBundle = null;
            nameLocaleBundle = "mill.locale.AUTH_USER";
            if ((nameLocaleBundle != null) && (nameLocaleBundle.trim().length() != 0))
                sCustom = StringManager.getManager(nameLocaleBundle, renderRequest.getLocale());
            // end

            AuthInfo authInfoUser = InternalAuthProvider.getAuthInfo( auth_ );

            db_ = DatabaseAdapter.getInstance(false);
            String index_page = CtxInstance.url("mill.auth.bind");

                out.write("<form method=\"POST\" action=\""+CtxInstance.url("mill.auth.commit_add_bind")+"\">\r\n");
                out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n");
                out.write("<tr>\r\n");
                out.write("<th colspan=\"2\">");
                out.write(sCustom.getStr("add_bind.jsp.new_rec"));
                out.write("</th>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(sCustom.getStr("add_bind.jsp.fio"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"id_user\">\r\n");

                    switch(db_.getFamaly())
                    {
                        case DatabaseManager.MYSQL_FAMALY:
                            String sql_ =
                                "select a.ID_USER, a.LAST_NAME, a.FIRST_NAME, a.MIDDLE_NAME " +
                                "from MAIN_USER_INFO a " +
                                "where a.ID_FIRM in ("+AuthHelper.getGrantedFirmId(db_, auth_.getUserLogin())+") and " +
                                "a.IS_DELETED=0 " +
                                // Todo uncomment when will be fixed problem with default value of timestamp
                                // Todo mysql create timestamp field with presetted value
                                // Todo read 1.8.6.2 Constraint NOT NULL and DEFAULT Values 'mysql manual'
//                                "and ((a.DATE_FIRE>"+db_.getNameDateBind()+") or (a.DATE_FIRE is null)) " +
                                "order by a.LAST_NAME ASC, a.FIRST_NAME ASC, a.MIDDLE_NAME ASC ";

                            ps = db_.prepareStatement( sql_ );
                            log.debug("User list SQL:\n"+sql_);
//                            db_.bindDate(ps, 1, DateTools.getCurrentTime());

                            break;
                        default:
                            ps = db_.prepareStatement(
                                "select a.ID_USER, a.LAST_NAME, a.FIRST_NAME, a.MIDDLE_NAME " +
                                "from MAIN_USER_INFO a, V$_READ_LIST_FIRM b " +
                                "where a.ID_FIRM=b.ID_FIRM and b.USER_LOGIN=? and " +
                                "IS_DELETED=0 and ((a.DATE_FIRE>"+db_.getNameDateBind()+") or (DATE_FIRE is null)) " +
                                "order by LAST_NAME ASC, FIRST_NAME ASC, MIDDLE_NAME ASC "
                            );
                            ps.setString(1, auth_.getUserLogin());
                            db_.bindDate(ps, 2, DateTools.getCurrentTime());
                            break;
                    }

                    rs = ps.executeQuery();

                    while (rs.next())
                    {
                        Long v_num = RsetTools.getLong(rs, "ID_USER");
                        String v_str = "";
                        v_str += (RsetTools.getString(rs, "LAST_NAME") + ' ');
                        v_str += (RsetTools.getString(rs, "FIRST_NAME") + ' ') ;
                        v_str += RsetTools.getString(rs, "MIDDLE_NAME") ;

                        out.write(
                            "<option value=\"" + v_num + "\">" +
                            v_str.replace('\"', '\'') +
                            "</option>\n"
                        );

                    }
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

/*
                out.write(

                    Client.make_list_prn(null, null, db_, "v_select_user_fio a ", "id_user", "fio ",
                        " where a.ID_FIRM in (  select ID_FIRM from v$_read_list_firm b " +
                    "where user_login = '" + auth_.getUserLogin() + "' ) and " +
                    "is_deleted=0 and ((a.date_fire > sysdate) or (date_fire is null))",
                        " fio asc ")

                );
*/
                out.write("</select>\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(sCustom.getStr("add_bind.jsp.user_login"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"user_login\" size=\"20\" maxlength=\"20\" value=\"\" >\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(sCustom.getStr("add_bind.jsp.password"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"password\" name=\"user_password\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr> \r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(sCustom.getStr("add_bind.jsp.password_check"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\"> \r\n");
                out.write("<input type=\"text\" name=\"user_password_resume\" size=\"20\" maxlength=\"20\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(sCustom.getStr("add_bind.jsp.is_use_current_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"is_use_current_firm\">\r\n");
                out.write("<option value=\"0\">");
                out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("yesno.no"));
                out.write("</option>\r\n");
                out.write("<option value=\"1\">");
                out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("yesno.yes"));
                out.write("</option>\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\">");
                out.write(sCustom.getStr("add_bind.jsp.list_firm"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\"> \r\n");
                out.write("<select name=\"id_firm\" size=\"1\">\r\n");

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

            getOptions(ps, rs, out);
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;

//                out.write( Client.make_list_prn(null, null, db_, "v_list_read_firm", "id",
//                        "name_firm", " where user_login = '" + auth_.getUserLogin() + "'") );

                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>");

                if (authInfoUser.isService == 1)
                {
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(sCustom.getStr("add_bind.jsp.is_service"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"is_service\">\r\n");
                    out.write("<option value=\"0\">");
                    out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("yesno.no"));
                    out.write("</option>\r\n");
                    out.write("<option value=\"1\">");
                    out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("yesno.yes"));
                    out.write("</option>\r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(sCustom.getStr("add_bind.jsp.id_service"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_service\">\r\n");
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

                    getOptions(ps, rs, out);
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

//                    out.write(
//                        Client.make_list_prn(null, null, db_, "V_AUTH_RELATE_SERVICE",
//                            "id_service", "full_name_service",
//                            " where user_login = '" + auth_.getUserLogin() + "'")
//                    );
                    out.write(" \r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>");


                }
                if (authInfoUser.isRoad == 1)
                {


                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(sCustom.getStr("add_bind.jsp.is_road"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"is_road\">\r\n");
                    out.write("<option value=\"0\">");
                    out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("yesno.no"));
                    out.write("</option>\r\n");
                    out.write("<option value=\"1\">");
                    out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("yesno.yes"));
                    out.write("</option>\r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(sCustom.getStr("add_bind.jsp.id_road"));
                    out.write("</td>\r\n");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_road\">\r\n");

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

                    getOptions(ps, rs, out);
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;

//                    out.write(
//                        Client.make_list_prn(null, null, db_, "V_AUTH_RELATE_road",
//                            "id_road", "full_name_road",
//                            " where user_login = '" + auth_.getUserLogin() + "'")
//                    );


                    out.write(" \r\n");
                    out.write("</select>\r\n");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");
                }
                out.write("\r\n");
                out.write("</table>\r\n");
                out.write("<br>\r\n");
                out.write("<input type=\"submit\" class=\"par\" value=\"");
                out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("button.add"));
                out.write("\">\r\n");
                out.write("</form>");


            out.write("<br>\r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(CtxInstance.getStringManager( renderRequest.getLocale() ).getStr("page.main.3"));
            out.write("</a>");
            out.write("</p>\r\n\r\n");

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

    private void getOptions(PreparedStatement ps, ResultSet rs, Writer out) throws SQLException, IOException {
        rs = ps.executeQuery();

        String v_val;
        String v_str;
        while (rs.next())
        {

            v_val = rs.getString(1);
            v_str = rs.getString(2);
            if (v_str==null)
                v_str = ""+v_val+", unknown value";

            out.write( "<option value=\"" +
                v_val + "\">" + v_str.replace('\"', '\'') +
                "</option>\n");

        }
    }
}
