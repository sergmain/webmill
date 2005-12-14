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
package org.riverock.portlet.member;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.ModuleTypeTypeType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.common.config.PropertiesProvider;

/**
 * User: Admin
 * Date: Nov 25, 2002
 * Time: 8:55:01 PM
 *
 * $Id$
 */
public final class MemberViewServlet extends HttpServlet
{
    private final static Log log = LogFactory.getLog(MemberViewServlet.class);

    private ServletConfig servletConfig = null;
    public void init(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        if (log.isDebugEnabled())
            log.debug( "method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        if (log.isDebugEnabled()) {
            log.debug("request: " + request );
            log.debug("response: " + response );
        }
        PrintWriter out = null;
        MemberProcessing mp = null;
        try {
            RenderRequest renderRequest = (RenderRequest)request;
            RenderResponse renderResponse = (RenderResponse)response;
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            if (!renderRequest.isUserInRole("webmill.member")) {
                out.println("Access denied, you have not right to execute 'webmill.member' portlet");
                return;
            }

//            ModuleManager moduleManager = ModuleManager.getInstance( servletConfig.getServletContext().getRealPath( "/" ) );
            ModuleManager moduleManager = ModuleManager.getInstance( PropertiesProvider.getConfigPath() );
            mp = new MemberProcessing( renderRequest, renderResponse, bundle, moduleManager );

            if (log.isDebugEnabled()) {
                log.debug("MemberProcessing - " + mp);
                log.debug("Request URL - " + request.getRequestURL());
                log.debug("Request getQueryString() - " + request.getQueryString());

                for (Enumeration e = renderRequest.getParameterNames(); e.hasMoreElements();)
                {
                    String s = (String) e.nextElement();
                    log.debug("Request param: " + s + ", value: " + RequestTools.getString(renderRequest, s));
                }
                boolean isFound = false;
                for (Enumeration e = renderRequest.getAttributeNames(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();
                    log.debug("Request attr: " + key + ", value: " + renderRequest.getAttribute( key ));
                    isFound = true;
                }
                if (!isFound) {
                    log.debug("Count of attr in request is: "+isFound);  
               }
            }

///////////////

            String moduleName = RequestTools.getString(renderRequest, MemberConstants.MEMBER_MODULE_PARAM);
            String actionName = RequestTools.getString(renderRequest, MemberConstants.MEMBER_ACTION_PARAM);
            String subActionName = RequestTools.getString(renderRequest, MemberConstants.MEMBER_SUBACTION_PARAM, "").trim();

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
                (mp.getFromParam() == null || mp.getFromParam().length() == 0)
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
                    log.debug("Access to module '" + moduleName + "' denied");

                out.println("Access to module '" + moduleName + "' denied");
                return;
            }

            String sql_ = null;

            if (!"commit".equalsIgnoreCase(subActionName))
            {
                out.println("<h4>" + MemberServiceClass.getString(mp.content.getTitle(), renderRequest.getLocale()) + "</h4>");

                    switch (actionType)
                    {
                        case ContentTypeActionType.INDEX_TYPE:
                            if (mp.isIndexAccess)
                            {
                                String recStr = mp.checkRecursiveCall();
                                if (recStr == null)
                                {
                                    sql_ = mp.buildSelectSQL();

                                    if (log.isDebugEnabled())
                                        log.debug("index SQL\n" + sql_);

                                    if (mp.isInsertAccess)
                                    {
                                        out.println("<p>"+mp.buildAddURL() + "</p>");
                                    }
                                    String s = mp.buildSelectHTMLTable(sql_);
                                    out.println( s );
                                }
                                else
                                    out.println(recStr);
                            }
                            else
                            {
                                putAccessDenied( out, moduleName );
                            }
                            break;

                        case ContentTypeActionType.INSERT_TYPE:
                            if (mp.isInsertAccess) {
                                out.println(mp.buildAddCommitForm() + "<br>");
                                out.println(mp.buildIndexURL() + "<br><br>");
                                putInsertButton( out, mp, renderRequest );
                                out.println(mp.buildAddHTMLTable());
                                putInsertButton( out, mp, renderRequest );
                                putEndForm( out );
                            }
                            else {
                                putAccessDenied( out, moduleName );
                            }
                            break;

                        case ContentTypeActionType.CHANGE_TYPE:
                            if (mp.isChangeAccess) {
                                out.println(mp.buildUpdateCommitForm() + "<br>");

                                sql_ = mp.buildSelectForUpdateSQL();

                                if (log.isDebugEnabled())
                                    log.debug("SQL " + sql_);

                                if (log.isDebugEnabled())
                                    log.debug("buildIndexURL");

                                out.println(mp.buildIndexURL() + "<br><br>");

                                putChangeButton( out, mp, renderRequest );

                                if (log.isDebugEnabled())
                                    log.debug("buildUpdateHTMLTable");

                                out.println(mp.buildUpdateHTMLTable(sql_));
                                putChangeButton( out, mp, renderRequest );
                                putEndForm( out );
                            }
                            else {
                                putAccessDenied( out, moduleName );
                            }
                            break;

                        case ContentTypeActionType.DELETE_TYPE:
                            if (mp.isDeleteAccess) {
                                out.println(mp.buildDeleteCommitForm() + "<br>");

                                sql_ = mp.buildSelectForDeleteSQL();

                                if (log.isDebugEnabled())
                                    log.debug("SQL " + sql_);

                                out.println(mp.buildIndexURL() + "<br><br>");
                                putDeleteButton( out, mp, renderRequest );
                                out.println(mp.buildDeleteHTMLTable(sql_));
                                putDeleteButton( out, mp, renderRequest );
                                putEndForm( out );
                            }
                            else {
                                putAccessDenied( out, moduleName );
                            }
                            break;

                        default:
                            log.error("Unknonw action - '" + actionType + "'");
                    }
                out.println("<br><p>" + mp.buildMainIndexURL() + "</p>");
            }
        }
        catch (Exception e) {
            final String es = "General processing error ";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally{
            if (mp!=null) {
                mp.destroy();
                mp = null;
            }
        }
    }

    private static void putAccessDenied( PrintWriter out, String moduleName ) {
        out.println("Access to module '"+moduleName+"' is denied");
    }

    private static void putInsertButton( PrintWriter out, MemberProcessing mp, RenderRequest renderRequest ) {
        putButton( out, mp, renderRequest, "Add" );
    }

    private static void putChangeButton( PrintWriter out, MemberProcessing mp, RenderRequest renderRequest ) {
        putButton( out, mp, renderRequest, "Change" );
    }

    private static void putDeleteButton( PrintWriter out, MemberProcessing mp, RenderRequest renderRequest ) {
        putButton( out, mp, renderRequest, "Delete" );
    }

    private static void putButton( PrintWriter out, MemberProcessing mp, RenderRequest renderRequest, String name ) {
        out.println("<input type=\"submit\" class=\"par\" value=\"" +
            MemberServiceClass.getString(
                mp.content.getActionButtonName(),
                renderRequest.getLocale(),
                name
            ) +
            "\">\n"
        );
    }

    private static void putEndForm( PrintWriter out ) {
        out.println( "</form>" );
    }
}
