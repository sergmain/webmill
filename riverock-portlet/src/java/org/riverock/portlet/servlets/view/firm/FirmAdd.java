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

 * Time: 9:32:08 AM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.firm;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;



import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.common.tools.ExceptionTools;



public class FirmAdd extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.servlets.view.firm.FirmAdd");



    public FirmAdd()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {



            InitPage.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if (auth_ == null)

                return;



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);



            InitPage jspPage = new InitPage(db_, request, response,

                "mill.firm.index",

                Constants.NAME_LANG_PARAM, null, null);



//            PortalInfo p = PortalInfo.getInstance(db_, request.getServerName());

//            CrossPageParam cross = new CrossPageParam(request, Constants.NAME_LANG_PARAM, p.defaultLocale, null, null);

//            Locale loc = cross.getLocale();

//            StringManager sm = StringManager.getManager("mill.locale.MAIN_LIST_FIRM", loc);

//            StringManager sm1 = StringManager.getManager("mill.locale.main", loc);



            String index_page = CtxURL.url(request, response, jspPage.cross, "mill.firm.index");



            if (auth_.isUserInRole("webmill.firm_insert"))

            {



                out.write("\r\n");

                out.write("<form method=\"POST\" action=\"");

                out.write(



                        CtxURL.url(request, response, jspPage.cross, "mill.firm.commit_add_firm")



                );

                out.write("\">\r\n");

                out.write("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\" bordercolor=\"#000000\">\r\n");

                out.write("<tr>\r\n");

                out.write("<td class=\"head\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.new_rec"));

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("</table>\r\n");

                out.write("<table width=\"100%\" border=\"1\" cellspacing=\"0\" bordercolor=\"#000000\" cellpadding=\"3\" class=\"l\">\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.number_reestr"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"number_reestr\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.zip"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"zip\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.okpo"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"okpo\" size=\"18\" maxlength=\"18\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.okonh"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"okonh\" size=\"50\" maxlength=\"140\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.full_name"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"full_name\" size=\"50\" maxlength=\"800\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.short_name"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"short_name\" size=\"50\" maxlength=\"250\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.address"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"address\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.telefon_buh"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"telefon_buh\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.telefon_chief"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"telefon_chief\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.telefon_io"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"telefon_io\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.registr_number"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"registr_number\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.chief"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"chief\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.buh"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"buh\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.fax"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"fax\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.email"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"email\" size=\"50\" maxlength=\"80\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.icq"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"icq\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.account"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"account\" size=\"22\" maxlength=\"22\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.bank"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"bank\" size=\"50\" maxlength=\"160\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.kor_account"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"kor_account\" size=\"22\" maxlength=\"22\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.bik"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"bik\" size=\"22\" maxlength=\"22\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.inn_bank"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"inn_bank\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.short_client_info"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"short_client_info\" size=\"50\" maxlength=\"1500\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.url"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"url\" size=\"50\" maxlength=\"160\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.short_info"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"short_info\" size=\"50\" maxlength=\"2000\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write("<span style=\"color:red\">*");

                out.write("</span>");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.is_work"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<select name=\"is_work\">\r\n");

                out.write("<option value=\"0\">");

                out.write(jspPage.sMain.getStr("yesno.no"));

                out.write("</option>\r\n");

                out.write("<option value=\"1\" selected>");

                out.write(jspPage.sMain.getStr("yesno.yes"));

                out.write("</option>\r\n");

                out.write("</select>\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.type_client"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"type_client\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.is_need_recvizit"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<select name=\"is_need_recvizit\">\r\n");

                out.write("<option value=\"0\">");

                out.write(jspPage.sMain.getStr("yesno.no"));

                out.write("</option>\r\n");

                out.write("<option value=\"1\">");

                out.write(jspPage.sMain.getStr("yesno.yes"));

                out.write("</option>\r\n");

                out.write("</select>\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.is_need_person"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<select name=\"is_need_person\">\r\n");

                out.write("<option value=\"0\">");

                out.write(jspPage.sMain.getStr("yesno.no"));

                out.write("</option>\r\n");

                out.write("<option value=\"1\">");

                out.write(jspPage.sMain.getStr("yesno.yes"));

                out.write("</option>\r\n");

                out.write("</select>\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.fio"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<input type=\"text\" name=\"fio\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("add_firm.jsp.is_search"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<select name=\"is_search\">\r\n");

                out.write("<option value=\"0\">");

                out.write(jspPage.sMain.getStr("yesno.no"));

                out.write("</option>\r\n");

                out.write("<option value=\"1\">");

                out.write(jspPage.sMain.getStr("yesno.yes"));

                out.write("</option>\r\n");

                out.write("</select>\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("</table>\r\n");

                out.write("<br>\r\n");

                out.write("<input type=\"submit\" class=\"par\" value=\"");

                out.write(jspPage.sMain.getStr("button.add"));

                out.write("\">\r\n");

                out.write("</form>\r\n");



            }

            out.write("\r\n");

            out.write("<br>\r\n");

            out.write("<p>");

            out.write("<a href=\"");

            out.write(index_page);

            out.write("\">");

            out.write(jspPage.sMain.getStr("page.main.3"));

            out.write("</a>");

            out.write("</p>\r\n");



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

