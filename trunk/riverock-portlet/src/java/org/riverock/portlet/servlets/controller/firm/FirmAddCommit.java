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

 * Time: 10:28:09 AM

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



import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class FirmAddCommit extends HttpServlet

{

    private static Logger log = Logger.getLogger(FirmAddCommit.class);



    public FirmAddCommit()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {



            InitPage.setContentType(response, "utf-8");



            out = response.getWriter();

            AuthSession auth_ = AuthTools.check(request, response, "/");

            if (auth_ == null)

                return;



            DatabaseAdapter dbDyn = null;

            String index_page = null;



            if (auth_.isUserInRole("webmill.firm_insert"))

            {

                PreparedStatement ps = null;

                try

                {

                    dbDyn = DatabaseAdapter.getInstance(true);

                    InitPage jspPage = new InitPage(dbDyn, request, response,

                            "mill.locale.MAIN_LIST_FIRM",

                            Constants.NAME_LANG_PARAM, null, null);



                    index_page = CtxURL.url(request, response, jspPage.cross, "mill.firm.index");



                    CustomSequenceType seq = new CustomSequenceType();

                    seq.setSequenceName("seq_MAIN_LIST_FIRM");

                    seq.setTableName( "MAIN_LIST_FIRM");

                    seq.setColumnName( "ID_FIRM" );

                    Long sequenceValue = new Long(dbDyn.getSequenceNextValue( seq ));





                    ps = dbDyn.prepareStatement(

                        "insert into MAIN_LIST_FIRM (" +

                        "	ID_FIRM, " +

                        "	full_name, " +

                        "	short_name, " +

                        "	address, " +

                        "	telefon_buh, " +

                        "	telefon_chief, " +

                        "	chief, " +

                        "	buh, " +

                        "	fax, " +

                        "	email, " +

                        "	icq, " +

                        "	short_client_info, " +

                        "	url, " +

                        "	short_info, " +

                        "	is_work, " +

                        "	is_need_recvizit, " +

                        "	is_need_person, " +

                        "	is_search," +

                        "is_deleted" +

                        ")" +



                        (dbDyn.getIsNeedUpdateBracket()?"(":"")+



                        " select " +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?," +

                        "	?,0 from AUTH_USER " +

                        "where USER_LOGIN=? " +

                        (dbDyn.getIsNeedUpdateBracket()?")":"")



                    );



                    int num=1;

                    RsetTools.setLong(ps, num++, sequenceValue);

                    ps.setString(num++, ServletUtils.getString(request, "full_name"));

                    ps.setString(num++, ServletUtils.getString(request, "short_name"));

                    ps.setString(num++, ServletUtils.getString(request, "address"));

                    ps.setString(num++, ServletUtils.getString(request, "telefon_buh"));

                    ps.setString(num++, ServletUtils.getString(request, "telefon_chief"));

                    ps.setString(num++, ServletUtils.getString(request, "chief"));

                    ps.setString(num++, ServletUtils.getString(request, "buh"));

                    ps.setString(num++, ServletUtils.getString(request, "fax"));

                    ps.setString(num++, ServletUtils.getString(request, "email"));

                    RsetTools.setLong(ps, num++, ServletTools.getLong(request, "icq"));

                    ps.setString(num++, ServletUtils.getString(request, "short_client_info"));

                    ps.setString(num++, ServletUtils.getString(request, "url"));

                    ps.setString(num++, ServletUtils.getString(request, "short_info"));

                    RsetTools.setLong(ps, num++, ServletTools.getLong(request, "is_work"));

                    RsetTools.setLong(ps, num++, ServletTools.getLong(request, "is_need_recvizit"));

                    RsetTools.setLong(ps, num++, ServletTools.getLong(request, "is_need_person"));

                    RsetTools.setLong(ps, num++, ServletTools.getLong(request, "is_search"));

                    ps.setString(num++, auth_.getUserLogin());



                    int i1 = ps.executeUpdate();



                    if (log.isDebugEnabled())

                        log.debug("Count of inserted records - "+i1);



//                    long servID = InternalAuthProvider.getAuthInfo( auth_ ).serviceID;

//                    AuthTools.setRelateServiceFirm(dbDyn, servID, sequenceValue);



                    dbDyn.commit();

                    response.sendRedirect(index_page);

                    return;



                }

                catch (Exception e1)

                {

                    try

                    {

                        dbDyn.rollback();

                    }

                    catch (Exception e001)

                    {

                    }



                    out.write("<html><head></head<body>" +

                            "Error while processing this page:<br>" +

                            ExceptionTools.getStackTrace(e1, 20, "<br>") + "<br>" +

                            "<p><a href=\"" + index_page + "\">continue</a></p>" +

                            "</body></html>"

                    );

                }

                finally

                {

                    DatabaseManager.close( dbDyn, ps );

                    ps = null;

                    dbDyn = null;

                }

            }

        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

    }

}

