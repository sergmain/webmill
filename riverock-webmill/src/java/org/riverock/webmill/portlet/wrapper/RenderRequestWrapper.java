/*

 * org.riverock.webmill -- Portal framework implementation

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

 * Date: Sep 20, 2003

 * Time: 1:00:05 AM

 *

 * $Id$

 */

package org.riverock.webmill.portlet.wrapper;



import java.security.Principal;

import java.util.ArrayList;

import java.util.Enumeration;

import java.util.Locale;

import java.util.Map;



import javax.portlet.PortalContext;

import javax.portlet.PortletMode;

import javax.portlet.PortletPreferences;

import javax.portlet.PortletSession;

import javax.portlet.RenderRequest;

import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.generic.tools.StringManager;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.schema.site.TemplateItemType;

import org.riverock.webmill.schema.site.SiteTemplateParameterType;



public class RenderRequestWrapper implements RenderRequest

{

    // global parameters for page

    private HttpServletRequest request = null;

    private HttpServletResponse response = null;

    private String ctxType = null;



    // parameters for current portlet

    private StringManager sm  = null;

    private String portletCode = null;

    private ArrayList parameters = null;





    public RenderRequestWrapper(){}



    public RenderRequestWrapper(

        CtxInstance ctxInstance,

        TemplateItemType templateItem

        )

    {

        this.request = ctxInstance.request ;

        this.response = ctxInstance.response ;

        this.ctxType = ctxInstance.type;





        this.portletCode = templateItem.getCode();



        if (templateItem.getParameterAsReference()!=null) {



            ArrayList parameters_ = templateItem.getParameterAsReference();

            this.parameters = null;

            if (templateItem!=null && parameters_.size()!=0 &&

                parameters_.get(0) instanceof SiteTemplateParameterType

            )

                this.parameters = parameters_;

        }

    }





    public boolean isWindowStateAllowed( WindowState windowState )

    {

        return false;

    }



    public boolean isPortletModeAllowed( PortletMode portletMode )

    {

        return false;

    }



    public PortletMode getPortletMode()

    {

        return null;

    }



    public WindowState getWindowState()

    {

        return null;

    }



    public PortletPreferences getPreferences()

    {

        return null;

    }



    public PortletSession getPortletSession()

    {

        return null;

    }



    public PortletSession getPortletSession( boolean b )

    {

        return null;

    }



    public String getProperty( String encoding )

    {

        return null;

    }



    public Enumeration getProperties( String encoding )

    {

        return null;

    }



    public Enumeration getPropertyNames()

    {

        return null;

    }



    public PortalContext getPortalContext()

    {

        return null;

    }



    public String getAuthType()

    {

        return null;

    }



    public String getContextPath()

    {

        return null;

    }



    public String getRemoteUser()

    {

        return null;

    }



    public Principal getUserPrincipal()

    {

        return null;

    }



    public boolean isUserInRole( String encoding )

    {

        return false;

    }



    public Object getAttribute( String encoding )

    {

        return null;

    }



    public Enumeration getAttributeNames()

    {

        return null;

    }



    public String getParameter( String encoding )

    {

        return null;

    }



    public Enumeration getParameterNames()

    {

        return null;

    }



    public String[] getParameterValues( String encoding )

    {

        return new String[0];

    }



    public Map getParameterMap()

    {

        return null;

    }



    public boolean isSecure()

    {

        return false;

    }



    public void setAttribute( String encoding, Object o )

    {

    }



    public void removeAttribute( String encoding )

    {

    }



    public String getRequestedSessionId()

    {

        return null;

    }



    public boolean isRequestedSessionIdValid()

    {

        return false;

    }



    public String getResponseContentType()

    {

        return null;

    }



    public Enumeration getResponseContentTypes()

    {

        return null;

    }



    public Locale getLocale()

    {

        return null;

    }



    public Enumeration getLocales()

    {

        return null;

    }



    public String getScheme()

    {

        return null;

    }



    public String getServerName()

    {

        return null;

    }



    public int getServerPort()

    {

        return 0;

    }

}

