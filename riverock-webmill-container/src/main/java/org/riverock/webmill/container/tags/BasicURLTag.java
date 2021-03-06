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

import java.io.IOException;

import javax.portlet.PortletURL;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Supporting class for the <CODE>actionURL</CODE> and <CODE>renderURL</CODE> tag.
 * Creates a url that points to the current Portlet and triggers an action request
 * with the supplied parameters.
 *
 */
public abstract class BasicURLTag extends TagSupport
{

    public static class TEI extends TagExtraInfo
    {
        public VariableInfo[] getVariableInfo(TagData tagData)
        {
            VariableInfo vi[] = null;
            String var = tagData.getAttributeString("var");
            if (var != null)
            {
                vi = new VariableInfo[1];
                vi[0] = new VariableInfo(var, "java.lang.String", true, VariableInfo.AT_END);
            }
            return vi;
        }
    }

    protected String portletMode;
    protected String secure;
    protected Boolean secureBoolean;
    protected String windowState;
    protected PortletURL url;
    protected String var;

    /**
     * Processes the <CODE>actionURL</CODE> or <CODE>renderURL</CODE> tag.
     * @return int
     */
    public abstract int doStartTag() throws JspException;

    /**
     *
     * @return int
     */
    public int doEndTag() throws JspException {
        if (var == null)
        {
            try
            {
                JspWriter writer = pageContext.getOut();
                writer.print(url); 
                writer.flush();
            }
            catch (IOException ioe)
            {
                throw new JspException("actionURL/renderURL Tag Exception: cannot write to the output writer.");
            }
        } else {
                pageContext.setAttribute (var, url.toString(), PageContext.PAGE_SCOPE);
        }
        return EVAL_PAGE;
    }

    /**
     * Returns the portletMode.
     * @return String
     */
    public String getPortletMode()
    {
        return portletMode;
    }

    /**
     * @return secure as String
     */
    public String getSecure()
    {
        return secure;
    }

    /**
     * @return secure as Boolean
     */
    public boolean getSecureBoolean()
    {
        return this.secureBoolean;
    }

    /**
     * Returns the windowState.
     * @return String
     */
    public String getWindowState()
    {
        return windowState;
    }

    /**
     * @return PortletURL
     */
    public PortletURL getUrl()
    {
        return url;
    }

    /**
     * Returns the var.
     * @return String
     */
    public String getVar()
    {
        return var;
    }

    /**
     * Sets the portletMode.
     * @param portletMode The portletMode to set
     */
    public void setPortletMode(String portletMode)
    {
        this.portletMode = portletMode;
    }

    /**
     * Sets secure to boolean value of the string
     * @param secure
     */
    public void setSecure(String secure)
    {
        this.secure = secure;
        this.secureBoolean = new Boolean(secure);
    }

    /**
     * Sets the windowState.
     * @param windowState The windowState to set
     */
    public void setWindowState(String windowState)
    {
        this.windowState = windowState;
    }

    /**
     * Sets the url.
     * @param url The url to set
     */
    public void setUrl(PortletURL url)
    {
        this.url = url;
    }

    /**
     * Sets the var.
     * @param var The var to set
     */
    public void setVar(String var)
    {
        this.var = var;
    }
}
