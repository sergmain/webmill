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

 * Time: 12:12:28 PM

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

import org.riverock.generic.schema.db.CustomSequenceType;



public class RightAddCommit extends HttpServlet

{

    private static Logger cat = Logger.getLogger(RightAddCommit.class);



    public RightAddCommit()

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



            InitPage.setContentType(response, "utf-8");



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if ( auth_==null )

                return;



            DatabaseAdapter dbDyn = null;





            if( auth_.isUserInRole("webmill.auth_right") )

            {

                PreparedStatement ps = null;

                String rightUrl = null;



                try{

                    dbDyn = DatabaseAdapter.getInstance( true );

                    InitPage jspPage =  new InitPage(dbDyn, request, response,

                            "mill.locale.AUTH_RELATE_RIGHT_ARM",

                            Constants.NAME_LANG_PARAM, null, null);



                    rightUrl = CtxURL.url( request, response, jspPage.cross, "mill.auth.right");



                    AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

                    if (authInfo.isRoot!=1)

                    {

                        response.sendRedirect( rightUrl);

                        return;

                    }



                    CustomSequenceType seq = new CustomSequenceType();

                    seq.setSequenceName("seq_AUTH_RELATE_RIGHT_ARM");

                    seq.setTableName( "AUTH_RELATE_RIGHT_ARM");

                    seq.setColumnName( "ID_RELATE_RIGHT" );

                    Long id = new Long(dbDyn.getSequenceNextValue( seq ) );



                    ps = dbDyn.prepareStatement(

                            "insert into AUTH_RELATE_RIGHT_ARM ("+

                            "	ID_RELATE_RIGHT, "+

                            "	id_access_group, "+

                            "	id_object_arm, "+

                            "	code_right, "+

                            "	is_road, "+

                            "	is_service, "+

                            "	is_firm)"+

                            "values "+

                            "(?, ?, ?, ?, ?, ?, ? )"

//                            "from auth_user b where b.user_login=?"

                    );



                    String right =

                            (Boolean.TRUE.equals(ServletTools.getInt(request, "rs"))?"S":"")+

                            (Boolean.TRUE.equals(ServletTools.getInt(request, "ri"))?"I":"")+

                            (Boolean.TRUE.equals(ServletTools.getInt(request, "rd"))?"D":"")+

                            (Boolean.TRUE.equals(ServletTools.getInt(request, "ru"))?"U":"")+

                            (Boolean.TRUE.equals(ServletTools.getInt(request, "ra"))?"A":"");





                    RsetTools.setLong(ps, 1, id);

                    RsetTools.setLong(ps, 2, ServletTools.getLong(request, "id_access_group"));

                    RsetTools.setLong(ps, 3, ServletTools.getLong(request, "id_object_arm"));



                    ps.setString(4, right);



                    RsetTools.setLong(ps, 5, ServletTools.getLong(request, "is_road"));

                    RsetTools.setLong(ps, 6, ServletTools.getLong(request, "is_service"));

                    RsetTools.setLong(ps, 7, ServletTools.getLong(request, "is_firm"));



//                    ps.setString(8, auth_.getUserLogin());



                    int i1 = ps.executeUpdate();

                    if (cat.isDebugEnabled())

                        cat.debug("Count of inserted records - "+i1);



                    dbDyn.commit();

                    response.sendRedirect( rightUrl);

                    return;

                }

                catch(Exception e1)

                {

                    cat.error("Error commit add right", e1);

                    try {

                        dbDyn.rollback();

                    }catch(Exception e001){}



                    out.write("<html><head></head<body>" +

                            "Error while processing this page:<br>"+

                            ExceptionTools.getStackTrace(e1, 20, "<br>")+"<br>" +

                            "<p><a href=\"" + rightUrl+ "\">continue</a></p>" +

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

