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

 * Time: 9:52:29 AM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.firm;



import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;

import java.sql.ResultSet;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.tools.HtmlTools;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;



public class FirmChange extends HttpServlet

{

    private static Logger cat = Logger.getLogger(FirmChange.class);



    public FirmChange()

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

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = AuthTools.check(ctxInstance.getPortletRequest(), response, "/");

            if ( auth_==null )

                return;



            db_ = DatabaseAdapter.getInstance( false );



            String index_page = CtxURL.url( ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.firm.index");



            Long id_firm = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_firm");

            if (id_firm==null)

                throw new IllegalArgumentException("id_firm not initialized");



            if( auth_.isUserInRole("webmill.firm_update") )

            {



                String v_str =

                        "select a.* from MAIN_LIST_FIRM a, v$_read_list_firm b "+

                        "where  a.ID_FIRM = ? and a.ID_FIRM=b.ID_FIRM and b.user_login = ? ";



                PreparedStatement ps = null;

                ResultSet rs = null;



                try

                {



                ps = db_.prepareStatement( v_str );

                RsetTools.setLong(ps, 1, id_firm);

                ps.setString(2, auth_.getUserLogin() );



                rs = ps.executeQuery();



                if ( rs.next() )

                {



              out.write("\r\n");

              out.write("<FORM ACTION=\"");

              out.write(



                        CtxURL.url( ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.firm.commit_ch_firm")



        );

              out.write("\" METHOD=\"POST\">\r\n");

              out.write("<input type=\"submit\" value=\"");

              out.write(ctxInstance.page.sMain.getStr("button.change"));

              out.write("\">\r\n");

              out.write("<INPUT TYPE=\"hidden\" NAME=\"id_firm\" VALUE=\"");

              out.write(""+ id_firm );

              out.write("\">\r\n");

              out.write("<TABLE  border=\"0\" width=\"100%\" class=\"l\">\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.number_reestr"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"number_reestr\" size=\"20\" maxlength=\"20\"  value=\"");

              out.write(""+  RsetTools.getLong(rs, "number_reestr"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.zip"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"zip\" size=\"20\" maxlength=\"20\"  value=\"");

              out.write( "" + RsetTools.getFloat(rs, "zip"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.okpo"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"okpo\" size=\"18\" maxlength=\"18\"  value=\"");

              out.write( RsetTools.getString(rs, "okpo"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.okonh"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"okonh\" size=\"50\" maxlength=\"140\"  value=\"");

              out.write( RsetTools.getString(rs, "okonh"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.full_name"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"full_name\" size=\"50\" maxlength=\"800\"  value=\"");

              out.write( RsetTools.getString(rs, "full_name"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.short_name"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"short_name\" size=\"50\" maxlength=\"250\"  value=\"");

              out.write( RsetTools.getString(rs, "short_name"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.address"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"address\" size=\"50\" maxlength=\"200\"  value=\"");

              out.write( RsetTools.getString(rs, "address"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.telefon_buh"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"telefon_buh\" size=\"30\" maxlength=\"30\"  value=\"");

              out.write( ""+ RsetTools.getString(rs, "telefon_buh"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.telefon_chief"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"telefon_chief\" size=\"30\" maxlength=\"30\"  value=\"");

              out.write( RsetTools.getString(rs, "telefon_chief"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.telefon_io"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"telefon_io\" size=\"30\" maxlength=\"30\"  value=\"");

              out.write( RsetTools.getString(rs, "telefon_io"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.registr_number"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"registr_number\" size=\"30\" maxlength=\"30\"  value=\"");

              out.write( RsetTools.getString(rs, "registr_number"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.chief"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"chief\" size=\"50\" maxlength=\"200\"  value=\"");

              out.write( RsetTools.getString(rs, "chief"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.buh"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"buh\" size=\"50\" maxlength=\"200\"  value=\"");

              out.write( RsetTools.getString(rs, "buh"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.fax"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"fax\" size=\"30\" maxlength=\"30\"  value=\"");

              out.write( RsetTools.getString(rs, "fax"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.email"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"email\" size=\"50\" maxlength=\"80\"  value=\"");

              out.write( RsetTools.getString(rs, "email"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.icq"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"icq\" size=\"20\" maxlength=\"20\"  value=\"");

              out.write( "" + RsetTools.getLong(rs, "icq"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.account"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"account\" size=\"22\" maxlength=\"22\"  value=\"");

              out.write( RsetTools.getString(rs, "account"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.bank"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"bank\" size=\"50\" maxlength=\"160\"  value=\"");

              out.write( RsetTools.getString(rs, "bank"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.kor_account"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"kor_account\" size=\"22\" maxlength=\"22\"  value=\"");

              out.write( RsetTools.getString(rs, "kor_account"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.bik"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"bik\" size=\"22\" maxlength=\"22\"  value=\"");

              out.write( RsetTools.getString(rs, "bik"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.inn_bank"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"inn_bank\" size=\"20\" maxlength=\"20\"  value=\"");

              out.write( "" + RsetTools.getLong(rs, "inn_bank"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.short_client_info"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"short_client_info\" size=\"50\" maxlength=\"1500\"  value=\"");

              out.write( RsetTools.getString(rs, "short_client_info"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.url"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"url\" size=\"50\" maxlength=\"160\"  value=\"");

              out.write( RsetTools.getString(rs, "url"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.short_info"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"short_info\" size=\"50\" maxlength=\"2000\"  value=\"");

              out.write( RsetTools.getString(rs, "short_info"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write("<span style=\"color:red\">*");

              out.write("</span>");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.is_work"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<select name=\"is_work\" size=\"1\">\r\n\t\t");

              out.write(HtmlTools.printYesNo(rs, "is_work", true, ctxInstance.page.currentLocale)

        );

              out.write("\r\n");

              out.write("</select>\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.type_client"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"type_client\" size=\"20\" maxlength=\"20\"  value=\"");

              out.write(""+ RsetTools.getLong(rs, "type_client"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.is_need_recvizit"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<select name=\"is_need_recvizit\" size=\"1\">\r\n\t\t");

              out.write(HtmlTools.printYesNo(rs, "is_need_recvizit", true, ctxInstance.page.currentLocale));

              out.write("\r\n");

              out.write("</select>\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.is_need_person"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<select name=\"is_need_person\" size=\"1\">\r\n\t\t");

              out.write(HtmlTools.printYesNo(rs, "is_need_person", true, ctxInstance.page.currentLocale));

              out.write("\r\n");

              out.write("</select>\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.fio"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<input type=\"text\" name=\"fio\" size=\"50\" maxlength=\"200\"  value=\"");

              out.write( RsetTools.getString(rs, "fio"));

              out.write("\">\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("<tr>\r\n");

              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

              out.write(ctxInstance.sCustom.getStr("ch_firm.jsp.is_search"));

              out.write("</td>\r\n");

              out.write("<td align=\"left\">\r\n");

              out.write("<select name=\"is_search\" size=\"1\">\r\n\t\t");

              out.write(HtmlTools.printYesNo(rs, "is_search", true, ctxInstance.page.currentLocale));

              out.write("\r\n");

              out.write("</select>\r\n");

              out.write("</td>\r\n");

              out.write("</tr>\r\n");

              out.write("</TABLE>\r\n");

              out.write("<BR>\r\n");

              out.write("<INPUT TYPE=\"submit\" VALUE=\"");

              out.write(ctxInstance.page.sMain.getStr("button.change"));

              out.write("\">\r\n");

              out.write("</FORM>\r\n");

              out.write("<BR>\r\n");



                }

        else

        {

        out.write("Record not found. ID_FIRM - "+id_firm+", user "+auth_.getUserLogin());

        }



                }

                finally

                {

                    DatabaseManager.close( rs, ps );

                    rs = null;

                    ps = null;

                }

            }

            else

            {

               out.write("Access denied");

            }

              out.write("\r\n");

              out.write("<p>");

              out.write("<a href=\"");

              out.write( index_page );

              out.write("\">");

              out.write(ctxInstance.page.sMain.getStr("page.main.3"));

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

            DatabaseManager.close( db_ );

            db_ = null;

        }



    }

}

