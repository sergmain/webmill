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
 * Date: Dec 2, 2002
 * Time: 9:50:27 PM
 *
 * $Id$
 */
package org.riverock.portlet.servlets.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.portlet.main.Constants;

public final class Logout extends HttpServlet
{
    private final static Logger log = Logger.getLogger( Logout.class );

    public Logout()
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
//            CtxInstance ctxInstance =
//                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );

            RenderRequest renderRequest = null;
            RenderResponse renderResponse = null;
            ContextNavigator.setContentType(response, "utf-8");

            PortletSession session = renderRequest.getPortletSession();

            if (log.isDebugEnabled())
            {
                for (Enumeration e = renderRequest.getParameterNames();
                     e.hasMoreElements(); )
                {

                    String param = (String)e.nextElement();
                    log.debug("parameter "+ param+" value "+PortletTools.getString(renderRequest, param) );
                }

            }

            int countLoop = 3;

            for (int i=0; i<countLoop; i++)
            {
                try
                {
                    for (Enumeration e = session.getAttributeNames();
                         e.hasMoreElements();
                         e = session.getAttributeNames()
                            )
                    {
                        String name = (java.lang.String) e.nextElement() ;

                        if (log.isDebugEnabled())
                            log.debug("Attribute: "+name);

                        PortletTools.immediateRemoveAttribute( session, name );
                    }
                }
                catch( java.util.ConcurrentModificationException e)
                {
                    if (i==countLoop-1)
                        throw e;
                }
            }

            session.invalidate();

            if (log.isDebugEnabled())
                log.debug("url to redir:  "+response.encodeURL( CtxInstance.ctx() ) );

            response.sendRedirect( CtxInstance.url(Constants.CTX_TYPE_INDEX, null, renderResponse ) );
            return;

        }
         catch (Exception e)
         {
             log.error(e);
             out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
         }

     }

}
