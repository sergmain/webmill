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



import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;



import org.apache.log4j.Logger;



import org.riverock.webmill.port.InitPage;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.utils.ServletUtils;



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



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {



            ContextNavigator.setContentType(response, "utf-8");



            out = response.getWriter();

            AuthSession auth_ = AuthTools.check(request, response, "/");

            if ( auth_==null )

                return;



            DatabaseAdapter dbDyn = null;

            String index_page = null;



            if( auth_.isUserInRole("webmill.firm_update") )

            {

                PreparedStatement ps = null;

                try

                {

                    dbDyn = DatabaseAdapter.getInstance( true );

                    InitPage  jspPage =  new InitPage(dbDyn, request,

                                                      "mill.locale.MAIN_LIST_FIRM"

                    );



                    index_page = CtxURL.url( request, response, jspPage, "mill.firm.index");



                    if (ServletTools.isNotInit(request, response, "id_firm", index_page))

                        return;



                    Long id_firm = ServletTools.getLong(request, "id_firm");





                    ps = dbDyn.prepareStatement(

                            "UPDATE MAIN_LIST_FIRM "+

                            "SET "+

                            "	number_reestr = ?, "+

                            "	zip = ?, "+

                            "	okpo = ?, "+

                            "	okonh = ?, "+

                            "	full_name = ?, "+

                            "	short_name = ?, "+

                            "	address = ?, "+

                            "	telefon_buh = ?, "+

                            "	telefon_chief = ?, "+

                            "	telefon_io = ?, "+

                            "	registr_number = ?, "+

                            "	chief = ?, "+

                            "	buh = ?, "+

                            "	fax = ?, "+

                            "	email = ?, "+

                            "	icq = ?, "+

                            "	account = ?, "+

                            "	bank = ?, "+

                            "	kor_account = ?, "+

                            "	bik = ?, "+

                            "	inn_bank = ?, "+

                            "	short_client_info = ?, "+

                            "	url = ?, "+

                            "	short_info = ?, "+

                            "	is_work = ?, "+

                            "	type_client = ?, "+

                            "	is_need_recvizit = ?, "+

                            "	is_need_person = ?, "+

                            "	fio = ?, "+

                            "	is_search = ? "+

                            "WHERE ID_FIRM = ? and ID_FIRM in "+

                            "(select ID_FIRM from v$_read_list_firm where user_login = ? )"



                    );

                    RsetTools.setLong(ps, 1, ServletTools.getLong(request, "number_reestr"));

                    RsetTools.setLong(ps, 2, ServletTools.getLong(request, "zip"));

                    ps.setString(3, ServletUtils.getString(request, "okpo"));

                    ps.setString(4, ServletUtils.getString(request, "okonh"));

                    ps.setString(5, ServletUtils.getString(request, "full_name"));

                    ps.setString(6, ServletUtils.getString(request, "short_name"));

                    ps.setString(7, ServletUtils.getString(request, "address"));

                    ps.setString(8, ServletUtils.getString(request, "telefon_buh"));

                    ps.setString(9, ServletUtils.getString(request, "telefon_chief"));

                    ps.setString(10, ServletUtils.getString(request, "telefon_io"));

                    ps.setString(11, ServletUtils.getString(request, "registr_number"));

                    ps.setString(12, ServletUtils.getString(request, "chief"));

                    ps.setString(13, ServletUtils.getString(request, "buh"));

                    ps.setString(14, ServletUtils.getString(request, "fax"));

                    ps.setString(15, ServletUtils.getString(request, "email"));

                    RsetTools.setLong(ps, 16, ServletTools.getLong(request, "icq"));

                    ps.setString(17, ServletUtils.getString(request, "account"));

                    ps.setString(18, ServletUtils.getString(request, "bank"));

                    ps.setString(19, ServletUtils.getString(request, "kor_account"));

                    ps.setString(20, ServletUtils.getString(request, "bik"));

                    RsetTools.setLong(ps, 21, ServletTools.getLong(request, "inn_bank"));

                    ps.setString(22, ServletUtils.getString(request, "short_client_info"));

                    ps.setString(23, ServletUtils.getString(request, "url"));

                    ps.setString(24, ServletUtils.getString(request, "short_info"));

                    RsetTools.setLong(ps, 25, ServletTools.getLong(request, "is_work"));

                    RsetTools.setLong(ps, 26, ServletTools.getLong(request, "type_client"));

                    RsetTools.setLong(ps, 27, ServletTools.getLong(request, "is_need_recvizit"));

                    RsetTools.setLong(ps, 28, ServletTools.getLong(request, "is_need_person"));

                    ps.setString(29, ServletUtils.getString(request, "fio"));

                    RsetTools.setLong(ps, 30, ServletTools.getLong(request, "is_search"));

                    RsetTools.setLong(ps, 31, id_firm);

                    ps.setString(32, auth_.getUserLogin() );



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

