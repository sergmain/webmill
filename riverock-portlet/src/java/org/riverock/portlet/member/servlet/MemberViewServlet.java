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

 * Time: 8:55:01 PM

 *

 * $Id$

 */

package org.riverock.portlet.member.servlet;



import java.io.IOException;

import java.io.PrintWriter;

import java.util.Enumeration;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.member.MemberForvardException;

import org.riverock.portlet.member.MemberProcessing;

import org.riverock.portlet.member.MemberServiceClass;

import org.riverock.portlet.member.ModuleManager;

import org.riverock.portlet.schema.member.types.ContentTypeActionType;

import org.riverock.portlet.schema.member.types.ModuleTypeTypeType;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;



public class MemberViewServlet extends HttpServlet

{

    private static Logger log = Logger.getLogger("org.riverock.portlet.member.servlet.MemberViewServlet");



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug( "method is POST");



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



            AuthSession auth_ = null;

            if ((auth_ = AuthTools.check(ctxInstance.getPortletRequest(), response, "/")) == null)

                return;





            if (log.isDebugEnabled())

            {

                log.debug("Total of MemberFile  - " + ModuleManager.getCountFile());

                log.debug("Total of Module - " + ModuleManager.getCountModule());

            }



            MemberProcessing mp = new MemberProcessing(ctxInstance, response, auth_);



            if (log.isDebugEnabled())

            {

                log.debug("MemberProcessing - " + mp);

                log.debug("Request URL - " + request_.getRequestURL());

                log.debug("Request getQueryString() - " + request_.getQueryString());



                for (Enumeration e = ctxInstance.getPortletRequest().getParameterNames(); e.hasMoreElements();)

                {

                    String s = (String) e.nextElement();

                    log.debug("Request attr - " + s + ", value - " + PortletTools.getString(ctxInstance.getPortletRequest(), s));

                }

            }



///////////////



            String moduleName = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_MODULE_PARAM);

            String actionName = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_ACTION_PARAM);

            String subActionName = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_SUBACTION_PARAM, "").trim();



            if (log.isDebugEnabled())

            {

                log.debug("Point #2.1 module '" + moduleName + "'");

                log.debug("Point #2.2 action '" + actionName + "'");

                log.debug("Point #2.3 subAction '" + subActionName + "'");

            }





            if (mp.mod == null)

            {

                out.println("Point #4.2. Module '" + moduleName + "' not found");

                return;

            }





            if (log.isDebugEnabled())

                log.debug("mod.getDefaultPortletType() - " + mp.mod.getType());



            // Check was module is lookup and can not calling directly from menu.

            if (mp.mod.getType() != null &&

                mp.mod.getType().getType() == ModuleTypeTypeType.LOOKUP_TYPE &&

                (mp.fromParam == null || mp.fromParam.length() == 0)

            )

            {

                out.println("Point #4.4. Module " + moduleName + " is lookup module<br>");

                return;

            }



            int actionType = ContentTypeActionType.valueOf(actionName).getType();

            if (log.isDebugEnabled())

            {

                log.debug("action name " + actionName);

                log.debug("ContentTypeActionType " + ContentTypeActionType.valueOf(actionName).toString());

                log.debug("action type " + actionType);

            }

            mp.content = MemberServiceClass.getContent(mp.mod, actionType);

            if (mp.content == null)

            {

                if (log.isDebugEnabled())

                    log.debug("Module: '" + moduleName + "', action '" + actionName + "', not found");



                out.println("Module: '" + moduleName + "', action '" + actionName + "', not found");

                return;

            }



            mp.initRight();



            if (!mp.isMainAccess)

            {

                if (log.isDebugEnabled())

                    log.debug("Access to module '"+moduleName+"' denied");



                out.println("Access to module '"+moduleName+"' denied");

                return;

            }



            String sql_ = null;



            if (!"commit".equalsIgnoreCase(subActionName))

            {

                out.println("<h4>" + MemberServiceClass.getString(mp.content.getTitle(), mp.getCtxInstance().page.currentLocale) + "</h4>");



                try

                {

                    switch (actionType)

                    {

                        case ContentTypeActionType.INDEX_TYPE:

                            if (mp.isIndexAccess)

                            {

                                String recStr = mp.checkRecursiveCall();

                                if (recStr == null)

                                {

//                                    out.println(

//                                        MemberServiceClass.getString(mp.content.getTargetAreaName(), mp.ctxInstance.page.currentLocale)+

//                                        "<br>"

//                                    );

                                    sql_ = mp.buildSelectSQL();



                                    if (log.isDebugEnabled())

                                        log.debug("index SQL\n" + sql_);



                                    if (mp.isInsertAccess)

                                    {

                                        out.println("<p>"+mp.buildAddURL() + "</p>");

                                    }

                                    try

                                    {

                                        String s = mp.buildSelectHTMLTable(sql_);



                                        out.println( s );

                                    }

                                    catch (MemberForvardException mfe)

                                    {

                                        log.debug("#7.01.01 forvard to lookup module ");

                                        return;

                                    }

                                }

                                else

                                    out.println(recStr);

                            }

                            else

                            {

                                out.println("Index access to module '"+moduleName+"' denied");

                            }

                            break;



                        case ContentTypeActionType.INSERT_TYPE:

                            if (mp.isInsertAccess)

                            {

//                                out.println(

//                                    MemberServiceClass.getString(mp.content.getTargetAreaName(), mp.ctxInstance.page.currentLocale)+

//                                    "<br>"

//                                );

                                out.println(mp.buildAddCommitForm() + "<br>");

                                out.println(mp.buildIndexURL() + "<br><br>");

                                out.println(mp.buildAddHTMLTable());



                                out.println("<input type=\"submit\" class=\"par\" value=\"" +

                                    MemberServiceClass.getString(

                                        mp.content.getActionButtonName(),

                                        mp.getCtxInstance().page.currentLocale,

                                        "Add"

                                    ) +

                                    "\">\n" +

                                    "</form>\n");

                            }

                            else

                            {

                                out.println("Index access to module '"+moduleName+"' denied");

                            }

                            break;



                        case ContentTypeActionType.CHANGE_TYPE:

                            if (mp.isChangeAccess)

                            {

//                                out.println(

//                                    MemberServiceClass.getString(mp.content.getTargetAreaName(), mp.ctxInstance.page.currentLocale)+

//                                    "<br>"

//                                );

                                out.println(mp.buildUpdateCommitForm() + "<br>");



                                sql_ = mp.buildSelectForUpdateSQL();



                                if (log.isDebugEnabled())

                                    log.debug("SQL " + sql_);



                                if (log.isDebugEnabled())

                                    log.debug("buildIndexURL");



                                out.println(mp.buildIndexURL() + "<br><br>");



                                if (log.isDebugEnabled())

                                    log.debug("buildUpdateHTMLTable");



                                out.println(mp.buildUpdateHTMLTable(sql_));

                                out.println("<input type=\"submit\" class=\"par\" value=\"" +

                                    MemberServiceClass.getString(

                                        mp.content.getActionButtonName(),

                                        mp.getCtxInstance().page.currentLocale,

                                        "Change"

                                    ) +

                                    "\">\n" +

                                    "</form>\n");

                            }

                            else

                            {

                                out.println("Index access to module '"+moduleName+"' denied");

                            }

                            break;



                        case ContentTypeActionType.DELETE_TYPE:

                            if (mp.isDeleteAccess)

                            {

//                                out.println(

//                                    MemberServiceClass.getString(mp.content.getTargetAreaName(), mp.ctxInstance.page.currentLocale)+

//                                    "<br>"

//                                );

                                out.println(mp.buildDeleteCommitForm() + "<br>");



                                sql_ = mp.buildSelectForDeleteSQL();



                                if (log.isDebugEnabled())

                                    log.debug("SQL " + sql_);



                                out.println(mp.buildIndexURL() + "<br><br>");

                                out.println(mp.buildDeleteHTMLTable(sql_));

                                out.println("<input type=\"submit\" class=\"par\" value=\"" +

                                    MemberServiceClass.getString(

                                        mp.content.getActionButtonName(),

                                        mp.getCtxInstance().page.currentLocale,

                                        "Delete"

                                    ) +

                                    "\">\n" +

                                    "</form>\n");

                            }

                            else

                            {

                                out.println("Index access to module '"+moduleName+"' denied");

                            }

                            break;



                        default:

                            log.error("Unknonw action - '" + actionType + "'");

                    }

                }

                catch (Exception e01)

                {

                    log.error("Exception while processing this page", e01);

                    out.println("Exception while processing this page:<br>" +

                        e01.toString() + "<br>" +

                        ExceptionTools.getStackTrace(e01, mp.linesInException, "<br>")

                    );

                }

                catch (Error e01)

                {

                    log.error("Error while processing this page", e01);

                    out.println("Error while processing this page:<br>" +

                        e01.toString() + "<br>" +

                        ExceptionTools.getStackTrace(e01, mp.linesInException, "<br>")

                    );

                }

                out.println("<br><p>" + mp.buildMainIndexURL() + "</p>");

            }

        }

        catch (Exception e)

        {

            log.error("General processing error ", e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

    }

}

