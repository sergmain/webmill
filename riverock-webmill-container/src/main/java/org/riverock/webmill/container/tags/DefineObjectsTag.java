/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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

import org.riverock.webmill.container.ContainerConstants;

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
