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

import java.util.*;



import javax.portlet.*;



public class RenderRequestWrapper implements RenderRequest

{

    // global parameters for page



    // parameters for current portlet

    private Map parameters = null;

    private PortletSession session = null;



    public RenderRequestWrapper(){}



    public RenderRequestWrapper(

        Map parameters,

        PortletSession session

        )

    {

        this.parameters = parameters;

        this.session = session;

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

        return null;

    }



    public Principal getUserPrincipal()

    {

        return null;

    }



    public boolean isUserInRole( String role )

    {

        return false;

    }



    public Object getAttribute( String key )

    {

        return null;

    }



    public Enumeration getAttributeNames()

    {

        return null;

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



        return new Vector(parameters.keySet()).elements();

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

        throw new IllegalStateException("Not implemented");

    }



    public boolean isSecure()

    {

        return false;

    }



    public void setAttribute( String key, Object o )

    {

    }



    public void removeAttribute( String key )

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

