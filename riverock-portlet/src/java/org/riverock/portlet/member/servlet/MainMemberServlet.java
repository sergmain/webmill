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

import java.util.Enumeration;

import java.util.Map;

import java.util.HashMap;



import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;



public class MainMemberServlet extends HttpServlet

{

    private static Logger log = Logger.getLogger("org.riverock.portlet.member.servlet.MainMemberServlet");



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug( "method is POST" );



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request_, HttpServletResponse response)

        throws IOException, ServletException

    {

        PrintWriter out = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            out = response.getWriter();



            ContextNavigator.setContentType(response, "utf-8");



            String applicationCode = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_NAME_APPL_PARAM);

            String moduleCode = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_NAME_MOD_PARAM);



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

                    String fullUrl = null;

                    Map parameterMap = null;

                    if  (isNew==1)

                    {

                        fullUrl = "/member_view?"+

                            url.substring( url.indexOf('?')+1)+ '&'+

                            Constants.NAME_TEMPLATE_CONTEXT_PARAM+ '=' + ctxInstance.getNameTemplate();



                        parameterMap = ServletTools.getParameterMap(fullUrl);

                    }

                    else

                    {

                        fullUrl = url+'?';

                        parameterMap = new HashMap();

                    }



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



                    RequestDispatcher dispatcher = request_.getRequestDispatcher(fullUrl);



                    if (log.isDebugEnabled())

                    {

                        log.debug("RequestDispatcher - " + dispatcher);

                        log.debug("ServletRequest - " + request_);

                        log.debug("Method is 'include'. Url - " + fullUrl);

                    }



                    if (dispatcher == null)

                    {

                        log.error("RequestDispatcher is null");

                        out.write("Error get dispatcher for path " + fullUrl);

                    }

                    else

                    {

                        ctxInstance.setParameters(parameterMap, ctxInstance.getNameLocaleBundle() );



                        ctxInstance.getPortletRequest().getPortletSession().setAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION, ctxInstance );

                        if (log.isDebugEnabled())

                        {

                            log.debug("ctx session - "+ctxInstance.getPortletRequest().getPortletSession());

                            for (Enumeration e = ctxInstance.getPortletRequest().getParameterNames(); e.hasMoreElements();)

                            {

                                String s = (String) e.nextElement();

                                log.debug("Request attr - " + s + ", value - " + PortletTools.getString(ctxInstance.getPortletRequest(), s));

                            }

                        }

                        dispatcher.include(request_, response);

                    }

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

