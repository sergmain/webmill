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

 * Time: 10:37:10 AM

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



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.sso.a3.AuthSession;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;



public class FirmDeleteCommit extends HttpServlet

{

    private static Logger log = Logger.getLogger(FirmDeleteCommit.class);



    public FirmDeleteCommit()

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

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );





            ContextNavigator.setContentType(response, "utf-8");



            out = response.getWriter();



            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

            if ( auth_==null || !auth_.isUserInRole( "webmill.firm_delete" ) )

            {

                WebmillErrorPage.process(out, null, "You have not enough right", "/", "continue");

                return;

            }



            DatabaseAdapter dbDyn = null;

            String index_page = null;



            if( auth_.isUserInRole("webmill.firm_delete") )

            {

                PreparedStatement ps = null;

                try{

                    dbDyn = DatabaseAdapter.getInstance( true );

//                    InitPage  jspPage =  new InitPage(dbDyn, request,

//                                                      "mill.locale.MAIN_LIST_FIRM"

//                    );



                    index_page = CtxURL.url( ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.firm.index");



//                    if (ServletTools.isNotInit(request, response, "id_firm", index_page))

//                        return;

                    Long id_firm = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_firm");

                    if (id_firm==null)

                        throw new IllegalArgumentException("id_firm not initialized");





                    ps = dbDyn.prepareStatement(

                        "update MAIN_LIST_FIRM set is_deleted = 1 "+

                        "where ID_FIRM = ? and ID_FIRM in "+

                        "( select z1.ID_FIRM from v$_read_list_firm z1 "+

                        " where z1.user_login = ? ) "

                    );



                    RsetTools.setLong(ps, 1, id_firm );

                    ps.setString(2, auth_.getUserLogin() );



                    int i1 = ps.executeUpdate();



                    if (log.isDebugEnabled())

                        log.debug("Count of deleted records - "+i1);



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

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

    }

}

