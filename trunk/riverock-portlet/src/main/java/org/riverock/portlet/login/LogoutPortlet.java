/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.login;

import java.util.Enumeration;
import java.io.Writer;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 9:50:27 PM
 *
 * $Id$
 */
public final class LogoutPortlet implements Portlet {
    private final static Logger log = Logger.getLogger( LogoutPortlet.class );

    public LogoutPortlet() {
    }

    public void init( final PortletConfig portletConfig ) {
    }

    public void render( RenderRequest renderRequest, RenderResponse renderResponse ) throws PortletException, IOException {
        ContentTypeTools.setContentType( renderResponse, ContentTypeTools.CONTENT_TYPE_UTF8 );
        Writer out = renderResponse.getWriter();
        out.write("You are logged out");
    }

    public void destroy() {
    }

    public void processAction( final ActionRequest actionRequest, final ActionResponse actionResponse )
       throws PortletException {

        processLogout(actionRequest);
    }

    private void processLogout(PortletRequest actionRequest) throws PortletException {
        try
        {
            PortletSession session = actionRequest.getPortletSession();

            if (log.isDebugEnabled()) {
                for (Enumeration e = actionRequest.getParameterNames(); e.hasMoreElements(); ) {

                    String param = (String)e.nextElement();
                    log.debug("parameter "+ param+" value "+ PortletService.getString(actionRequest, param, null) );
                }
            }
            session.invalidate();
        }
         catch (Exception e) {
            String es = "Error process logout";
            log.error( es, e );
            throw new PortletException( es, e );
         }
    }

}
