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

 * Time: 9:09:43 AM

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

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.portlet.tools.HtmlTools;

import org.riverock.sso.a3.AuthSession;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;



public class FirmIndex extends HttpServlet

{

    private static Logger log = Logger.getLogger(FirmIndex.class);



    public FirmIndex()

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

        try

        {



            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

            if ( auth_==null )

            {

                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");

                return;

            }



            db_ = DatabaseAdapter.getInstance( false );



            String index_page = ctxInstance.url("mill.firm.index");



            String v_str =

                    "select "+

                    "	ID_FIRM\n"+

                    "	,number_reestr\n"+

                    "	,zip\n"+

                    "	,okpo\n"+

                    "	,okonh\n"+

                    "	,full_name\n"+

                    "	,short_name\n"+

                    "	,address\n"+

                    "	,chief\n"+

                    "	,buh\n"+

                    "	,account\n"+

                    "	,bank\n"+

                    "	,kor_account\n"+

                    "	,bik\n"+

                    "	,url\n"+

                    "	,short_info\n"+

                    "	,is_work\n"+

                    "	,is_search\n"+



                    "from 	MAIN_LIST_FIRM a ";

            v_str += "where \n";

            v_str += "	is_deleted=0 ";

            v_str += " and ";

            v_str += (

                    " a.ID_FIRM in "+

                    "        (select z1.ID_FIRM from v$_read_list_firm z1 "+

                    "        where z1.user_login = ? "+

                    "        )");

//"order by ID_FIRM asc ";





            if (log.isDebugEnabled())

                log.debug(v_str);



            if( auth_.isUserInRole("webmill.firm_select") )

            {

                 out.write(

                         "<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\" width=\"770\">"+

                         "<tr>"+

                         "<td>"+

                         "<b>" + ctxInstance.sCustom.getStr("index.jsp.title")+ "</b><br>"+

                         "<p><a href=\""+

                         ctxInstance.url("mill.firm.add_firm")+

                         "\">"+ctxInstance.getStringManager().getStr("button.add")+"</a></p>"+

                         "<table width=\"100%\" border=\"1\" class=\"l\">"+

                         "<tr>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.full_name")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.short_name")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.address")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.chief")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.short_info")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.is_work")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.is_search")+"</td>"+

                         "<th class=\"memberArea\" width=\"4%\">"+ctxInstance.sCustom.getStr("index.jsp.action")+"</td>"+

                         "</tr>");





                PreparedStatement ps = null;

                ResultSet rs = null;

                try

                {

                    ps = db_.prepareStatement( v_str );

                    ps.setString(1, auth_.getUserLogin());



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

                                "<td class=\"memberArea\">"+ HtmlTools.printYesNo(rs, "is_work", false, ctxInstance.getPortletRequest().getLocale() ) +"</td>"+

                                "<td class=\"memberArea\">"+ HtmlTools.printYesNo(rs, "is_search", false, ctxInstance.getPortletRequest().getLocale() ) +"</td>"+

                                "<td class=\"memberAreaAction\">"

                        );



                        Long id_firm = RsetTools.getLong(rs, "ID_FIRM");



                        out.write(

                                "<input type=\"button\" value=\""+ctxInstance.getStringManager().getStr("button.change")+"\" onclick=\"location.href='"+

                                ctxInstance.url("mill.firm.ch_firm") + '&'+

                                "id_firm="+id_firm +

                                "';\">"+

                                "<input type=\"button\" value=\""+ctxInstance.getStringManager().getStr("button.delete")+"\" onclick=\"location.href='"+

                                ctxInstance.url("mill.firm.del_firm") + '&'+

                                "id_firm="+id_firm+

                                "';\">"+

                                "</td>"+

                                "</tr>"

                        );



                    }

                    out.write(

                            "</table>"+

                            "<p><a href=\""+



                            ctxInstance.url("mill.firm.add_firm")+



                            "\">"+ctxInstance.getStringManager().getStr("button.add")+"</a></p>"

                    );



                }

                finally

                {

                    DatabaseManager.close( rs, ps );

                    rs = null;

                    ps = null;

                }



                out.write(

                        "<p><a href=\""+ index_page +"\">"+ctxInstance.getStringManager().getStr("page.main.3")+"</a></p>"+

                        "</td>"+

                        "</tr>"+

                        "</table>"

                );



            }



            out = response.getWriter();

        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

        finally

        {

            DatabaseAdapter.close(db_);

            db_ = null;

        }



    }

}

