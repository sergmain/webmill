/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.tags;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * * Supporting class for the <CODE>renderURL</CODE> tag.
 * * Creates a url that points to the current Portlet and triggers an render request
 * * with the supplied parameters.
 * *
 */
public class RenderURLTag extends BasicURLTag {


    /* (non-Javadoc)
         * @see javax.servlet.jsp.tagext.Tag#doStartTag()
         */
    public int doStartTag() throws JspException {
        if (var != null) {
            pageContext.removeAttribute(var, PageContext.PAGE_SCOPE);
        }
        RenderResponse renderResponse = (RenderResponse) pageContext.getRequest().getAttribute("javax.portlet.response");

        if (renderResponse != null) {
            setUrl(renderResponse.createRenderURL());
            if (portletMode != null) {
                try {
                    PortletMode mode = new PortletMode(portletMode);
                    url.setPortletMode(mode);
                }
                catch (PortletModeException e) {
                    throw new JspException(e);
                }
            }
            if (windowState != null) {
                try {
                    WindowState state = new WindowState(windowState);
                    url.setWindowState(state);
                }
                catch (WindowStateException e) {
                    throw new JspException(e);
                }
            }
            if (secure != null) {
                try {
                    url.setSecure(getSecureBoolean());
                }
                catch (PortletSecurityException e) {
                    throw new JspException(e);
                }
            }
        }
        return EVAL_PAGE;
    }
}

