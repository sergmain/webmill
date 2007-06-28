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

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.VariableInfo;

import org.riverock.interfaces.ContainerConstants;

/**
 * Supporting class for the <CODE>defineObjects</CODE> tag.
 * Creates the following variables to be used in the JSP:
 * <UL>
 * <LI><CODE>renderRequest</CODE>
 * <LI><CODE>renderResponse</CODE>
 * <LI><CODE>portletConfig</CODE>
 * </UL>
 *
 * @see javax.portlet.PortletRequest
 * @see javax.portlet.RenderResponse
 * @see javax.portlet.PortletConfig
 */
public class DefineObjectsTag extends TagSupport {

    /**
     * Processes the <CODE>defineObjects</CODE> tag.
     *
     * @return <CODE>SKIP_BODY</CODE>
     */
    public int doStartTag() throws JspException {
        PortletRequest renderRequest = (PortletRequest) pageContext.getRequest();
        RenderResponse renderResponse = (RenderResponse) pageContext.getResponse();
        PortletConfig portletConfig = (PortletConfig) pageContext.getRequest().getAttribute(ContainerConstants.PORTAL_PORTLET_CONFIG_ATTRIBUTE);

        if (pageContext.getAttribute("renderRequest") == null) {
            pageContext.setAttribute("renderRequest", renderRequest, PageContext.PAGE_SCOPE);
        }

        if (pageContext.getAttribute("renderResponse") == null) {
            pageContext.setAttribute("renderResponse", renderResponse, PageContext.PAGE_SCOPE);
        }

        if (pageContext.getAttribute("portletConfig") == null) {
            pageContext.setAttribute("portletConfig", portletConfig, PageContext.PAGE_SCOPE);
        }

        return SKIP_BODY;
    }

    public static class TEI extends TagExtraInfo {

        public VariableInfo [] getVariableInfo(TagData tagData) {
            return new VariableInfo []{
                new VariableInfo("renderRequest", "javax.portlet.RenderRequest", true, VariableInfo.AT_BEGIN),
                new VariableInfo("renderResponse", "javax.portlet.RenderResponse", true, VariableInfo.AT_BEGIN),
                new VariableInfo("portletConfig", "javax.portlet.PortletConfig", true, VariableInfo.AT_BEGIN)
            };
        }
    }
}
