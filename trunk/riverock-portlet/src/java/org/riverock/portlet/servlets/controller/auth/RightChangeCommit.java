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

 * Time: 12:12:43 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.controller.auth;



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

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;



import org.riverock.webmill.portlet.PortletTools;



public class RightChangeCommit extends HttpServlet

{

    private static Logger cat = Logger.getLogger(RightChangeCommit.class);



    public RightChangeCommit()

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

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

            if ( auth_==null || !auth_.isUserInRole( "webmill.auth_bind" ) )

            {

                WebmillErrorPage.process(out, null, "You have not enough right", "/", "continue");

                return;

            }



            DatabaseAdapter dbDyn = null;

            String index_page = null;



            {

                PreparedStatement ps = null;

                try{

                    dbDyn = DatabaseAdapter.getInstance( true );

//                    InitPage jspPage =  new InitPage(dbDyn, request,

//                                                     "mill.locale.AUTH_RELATE_RIGHT_ARM"

//                    );



                    index_page = ctxInstance.url("mill.auth.right");



                    AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

                    if (authInfo.isRoot!=1)

                    {

                        response.sendRedirect( index_page );

                        return;

                    }



//                    if (ServletTools.isNotInit(request, response, "id_relate_right", index_page ))

//                        return;

                    Long id_relate_right = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_relate_right");

                    if (id_relate_right==null)

                    throw new IllegalArgumentException("id_relate_right not initialized");



                    Integer one = new Integer(1);

                    String right =

                            (one.equals(PortletTools.getInt(ctxInstance.getPortletRequest(), "rs"))?"S":"")+

                            (one.equals(PortletTools.getInt(ctxInstance.getPortletRequest(), "ri"))?"I":"")+

                            (one.equals(PortletTools.getInt(ctxInstance.getPortletRequest(), "rd"))?"D":"")+

                            (one.equals(PortletTools.getInt(ctxInstance.getPortletRequest(), "ru"))?"U":"")+

                            (one.equals(PortletTools.getInt(ctxInstance.getPortletRequest(), "ra"))?"A":"");



                    ps = dbDyn.prepareStatement(

                            "UPDATE AUTH_RELATE_RIGHT_ARM "+

                            "SET "+

                            "	id_access_group = ?, "+

                            "	id_object_arm = ?, "+

                            "	CODE_RIGHT = ?, "+

                            "	is_road = ?, "+

                            "	is_service = ?, "+

                            "	is_firm = ? "+

                            "WHERE "+

                            "	id_relate_right = ?"

                    );



                    RsetTools.setLong(ps, 1, PortletTools.getLong(ctxInstance.getPortletRequest(), "id_access_group"));

                    RsetTools.setLong(ps, 2, PortletTools.getLong(ctxInstance.getPortletRequest(), "id_object_arm"));

                    ps.setString(3, right);

                    ps.setInt(4, PortletTools.getInt(ctxInstance.getPortletRequest(), "is_road", new Integer(9)).intValue() );

                    ps.setInt(5, PortletTools.getInt(ctxInstance.getPortletRequest(), "is_service", new Integer(9)).intValue() );

                    ps.setInt(6, PortletTools.getInt(ctxInstance.getPortletRequest(), "is_firm", new Integer(9)).intValue() );

                    RsetTools.setLong(ps, 7, id_relate_right);



                    int i1 = ps.executeUpdate();



                    if (cat.isDebugEnabled())

                        cat.debug("Count of inserted records - "+i1);



                    dbDyn.commit();



                    response.sendRedirect( index_page );

                    return;

                }

                catch(Exception e1)

                {

                    cat.error("Error commit change right", e1);

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

