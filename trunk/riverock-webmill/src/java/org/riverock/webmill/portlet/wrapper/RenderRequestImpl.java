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

package org.riverock.webmill.portal.impl;



import java.security.Principal;

import java.util.*;



import javax.portlet.*;

import javax.servlet.http.HttpServletRequest;



import org.apache.log4j.Logger;

import org.riverock.sso.a3.AuthException;

import org.riverock.sso.a3.AuthSession;



public class RenderRequestImpl implements RenderRequest

{

    private static Logger log = Logger.getLogger(RenderRequestImpl.class);

    // global parameters for page



    private HttpServletRequest request = null;

    // parameters for current portlet

    private Map parameters = null;

    private PortletSession session = null;

    private AuthSession auth = null;



    // Locale of this request

    private Locale locale = null;



    private Locale preferredLocale[] = null;



    public RenderRequestImpl(){}



    public RenderRequestImpl(

        Map parameters,

        HttpServletRequest request,

        AuthSession auth,

        Locale locale,

        Locale preferredLocale[]

        )

    {

        this.parameters = parameters;

        this.session = new PortletSessionImpl(request.getSession(true));

        this.request = request;

        this.auth = auth;

        this.locale = locale;

        this.preferredLocale = preferredLocale;

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

        return session;

    }



    public PortletSession getPortletSession( boolean b )

    {

        return session;

    }



    public String getProperty( String key )

    {

        return null;

    }



    public Enumeration getProperties( String key )

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

        if (auth==null)

            return null;



        return auth.getUserLogin();

    }



    public Principal getUserPrincipal()

    {

        if (auth==null)

            return null;



        return auth;

    }



    public boolean isUserInRole( String role )

    {

        if (request.getServerName()==null || auth==null)

            return false;



        try

        {

            boolean status = auth.checkAccess( request.getServerName() );

            if (!status)

                return false;



            return auth.isUserInRole(role);

        }

        catch (AuthException authException)

        {

            log.error("Exception in isUserInRole(), role - "+role, authException);

        }

        return false;

    }



    public Object getAttribute( String key )

    {

        return request.getAttribute(key);

    }



    public Enumeration getAttributeNames()

    {

        return request.getAttributeNames();

    }



    public String getParameter( String key )

    {

        if (parameters==null)

            return null;



        Object obj = parameters.get(key);

        if (obj==null)

            return null;



        if (obj instanceof List)

            return ((List)obj).get(0).toString();



        return obj.toString();

    }



    public Enumeration getParameterNames()

    {

        if (parameters==null)

            return new Hashtable().elements();



        return Collections.enumeration(parameters.keySet());

    }



    public String[] getParameterValues( String key )

    {

        if (parameters==null)

            return null;



        Object obj = parameters.get(key);

        if (obj==null)

            return null;



        if (obj instanceof List)

        {

            int size = ((List)obj).size();

            String values[] = new String[size];



            for (int i=0; i<size; i++)

                values[i] = ((List)obj).get(i).toString();



            return values;

        }

        else

            return new String[]{ obj.toString() };



    }



    public Map getParameterMap()

    {

        Map map = new HashMap();

        map.putAll(parameters);

        return map;

    }



    public boolean isSecure()

    {

        return request.isSecure();

    }



    public void setAttribute( String key, Object obj )

    {

        request.setAttribute(key, obj);

    }



    public void removeAttribute( String key )

    {

        request.removeAttribute(key);

    }



    public String getRequestedSessionId()

    {

        return request.getRequestedSessionId();

    }



    public boolean isRequestedSessionIdValid()

    {

        return request.isRequestedSessionIdValid();

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

        return locale;

    }



    public Enumeration getLocales()

    {

        return Collections.enumeration( Arrays.asList(preferredLocale) );

    }



    public String getScheme()

    {

        return request.getScheme();

    }



    public String getServerName()

    {

        return request.getServerName();

    }



    public int getServerPort()

    {

        return request.getServerPort();

    }

}

