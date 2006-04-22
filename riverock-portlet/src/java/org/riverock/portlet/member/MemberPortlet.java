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

import java.io.IOException;
import java.io.Writer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;

/**
 * User: Admin
 * Date: Nov 25, 2002
 * Time: 8:42:34 PM
 *
 * $Id$
 */
public final class MemberPortlet implements Portlet {

    private PortletConfig portletConfig = null;
    public MemberPortlet() {
    }

    public void init(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {
        MemberPortletActionMethod.processAction( actionRequest, actionResponse );
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException {
        if ( renderRequest.getParameter( MemberConstants.ERROR_TEXT )!=null ) {
            Writer out = renderResponse.getWriter();
            WebmillErrorPage.processPortletError(out, null,
                renderRequest.getParameter( MemberConstants.ERROR_TEXT ),
                renderRequest.getParameter( renderResponse.createRenderURL().toString() ),
                renderRequest.getParameter( MemberConstants.ERROR_URL_NAME ));

            out.flush();
            out.close();
            return;
        }
        MemberPortletRenderMethod.render( renderRequest, renderResponse );
    }
}
