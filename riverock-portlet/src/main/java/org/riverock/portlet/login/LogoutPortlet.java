/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
