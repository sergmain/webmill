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

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServlet;



import org.apache.log4j.Logger;



import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.RsetTools;



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



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            InitPage.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if ( auth_==null )

                return;



            DatabaseAdapter dbDyn = null;

            String index_page = null;



            if( auth_.isUserInRole("webmill.auth_right") )

            {

                PreparedStatement ps = null;

                try{

                    dbDyn = DatabaseAdapter.getInstance( true );

                    InitPage jspPage =  new InitPage(dbDyn, request, response,

                            "mill.locale.AUTH_RELATE_RIGHT_ARM",

                            Constants.NAME_LANG_PARAM, null, null);



                    index_page = CtxURL.url( request, response, jspPage.cross, "mill.auth.right");



                    AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

                    if (authInfo.isRoot!=1)

                    {

                        response.sendRedirect( index_page );

                        return;

                    }



                    if (ServletTools.isNotInit(request, response, "id_relate_right", index_page ))

                        return;

                    Long id_relate_right = ServletTools.getLong(request, "id_relate_right");



                    Integer one = new Integer(1);

                    String right =

                            (one.equals(ServletTools.getInt(request, "rs"))?"S":"")+

                            (one.equals(ServletTools.getInt(request, "ri"))?"I":"")+

                            (one.equals(ServletTools.getInt(request, "rd"))?"D":"")+

                            (one.equals(ServletTools.getInt(request, "ru"))?"U":"")+

                            (one.equals(ServletTools.getInt(request, "ra"))?"A":"");



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



                    RsetTools.setLong(ps, 1, ServletTools.getLong(request, "id_access_group"));

                    RsetTools.setLong(ps, 2, ServletTools.getLong(request, "id_object_arm"));

                    ps.setString(3, right);

                    ps.setInt(4, ServletTools.getInt(request, "is_road", new Integer(9)).intValue() );

                    ps.setInt(5, ServletTools.getInt(request, "is_service", new Integer(9)).intValue() );

                    ps.setInt(6, ServletTools.getInt(request, "is_firm", new Integer(9)).intValue() );

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

