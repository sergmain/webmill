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
