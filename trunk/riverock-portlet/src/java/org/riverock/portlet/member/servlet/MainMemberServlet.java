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

 * User: Admin

 * Date: Nov 25, 2002

 * Time: 8:42:34 PM

 *

 * $Id$

 */

package org.riverock.portlet.member.servlet;



import java.io.IOException;

import java.io.PrintWriter;

import java.sql.PreparedStatement;

import java.sql.ResultSet;



import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.portlet.main.Constants;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ExceptionTools;



public class MainMemberServlet extends HttpServlet

{

    private static Logger log = Logger.getLogger("org.riverock.member.servlet.MainMemberServlet");



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug( "method is POST" );



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        PrintWriter out = null;

        try

        {

            out = response.getWriter();



            InitPage.setContentType(response, "utf-8");



/*

// если оба параметра равны нул, то это значит, что надо просто показать пустую страницу

// отформатированную с мембер шаблоном.

if (request.getParameter(Constants.MEMBER_NAME_APPL_PARAM) == null &&

request.getParameter(Constants.MEMBER_NAME_MOD_PARAM )==null)

{

if (cat.isDebugEnabled())

cat.debug("request start member page");



AuthSession authSession = AuthSession.getAuthSession(request);



if (cat.isDebugEnabled())

cat.debug("AUTH_SESSION - " + authSession);



if (authSession == null)

{

//                ServletContext context = request.getServletContext();

//            RequestDispatcher dispatcher = context.getRequestDispatcher(item.value);

RequestDispatcher dispatcher = application.getRequestDispatcher(

"/share/view/login.jsp");



if (cat.isDebugEnabled())

cat.debug("RequestDispatcher - " + dispatcher);



if (dispatcher == null)

{

if (cat.isDebugEnabled())

cat.debug("RequestDispatcher is null");



break;

}

dispatcher.include(request, response);



}

return;

}

*/



            String applicationCode = ServletUtils.getString(request, Constants.MEMBER_NAME_APPL_PARAM);

            String moduleCode = ServletUtils.getString(request, Constants.MEMBER_NAME_MOD_PARAM);



            if (log.isDebugEnabled())

            {

                log.debug("applicationCode " + applicationCode);

                log.debug("moduleCode " + moduleCode);

            }



            PreparedStatement ps = null;

            ResultSet rs = null;

            DatabaseAdapter db_ = null;

            try

            {

                db_ = DatabaseAdapter.getInstance(false);

                ps = db_.prepareStatement(

                    "select a.is_new, a.url " +

                    "from auth_object_arm a, auth_arm b " +

                    "where a.id_arm = b.id_arm and b.CODE_ARM=? and a.CODE_OBJECT_ARM=? "

                );

                ps.setString(1, applicationCode);

                ps.setString(2, moduleCode);

                rs = ps.executeQuery();

                if (rs.next())

                {



                    String url = RsetTools.getString(rs, "URL");

                    int isNew = RsetTools.getInt( rs, "IS_NEW" , new Integer(0)).intValue();

                    String fullUrl =

                        (isNew==1)?

                        "/member_view?"+url.substring( url.indexOf('?')+1):

                        url+'?';



/*

                    if (cat.isDebugEnabled() && isNew==0)

                    {

                        String nameFile = InitParam.millApplPath + fullUrl;





                        cat.debug("Full url - " + fullUrl);

                        cat.debug("millApplPath - " + InitParam.millApplPath);

                        cat.debug("nameFile - " + nameFile);



                        if (nameFile.indexOf('?') != -1)

                            nameFile = nameFile.substring(0, nameFile.indexOf('?'));



                        cat.debug("end nameFile - " + nameFile);

                    }

*/



                    RequestDispatcher dispatcher = request.getRequestDispatcher(fullUrl);

                    if (log.isDebugEnabled())

                        log.debug("RequestDispatcher - " + dispatcher);



                    if (dispatcher == null)

                    {

                        if (log.isDebugEnabled())

                            log.debug("RequestDispatcher is null");



                        out.write("Error get dispatcher for path " + fullUrl);

                    }



                    if (log.isDebugEnabled())

                        log.debug("Method is 'include'. Url - " + fullUrl);





                    dispatcher.include(request, response);

//                    }



                    return;

                }

                else

                {

                    String errorSttring = " application '" + applicationCode + "'  module '" + moduleCode + "' not found";

                    log.info(errorSttring);

                    out.println(errorSttring);

                }

            }

            finally

            {

                DatabaseManager.close(db_, rs, ps);

                rs = null;

                ps = null;

                db_ = null;

            }

        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

    }

}

