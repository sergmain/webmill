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

 * User: serg_main

 * Date: 20.05.2004

 * Time: 21:14:18

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portal.impl;



import java.util.HashMap;

import java.util.Iterator;

import java.util.Map;



import javax.portlet.*;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.webmill.portlet.CtxInstance;



public class PortletURLImpl implements PortletURL

{

    protected PortletMode mode = null;



    protected HashMap parameters = new HashMap();



//    protected PortletWindow portletWindow;



    private boolean secure;

    private HttpServletRequest servletRequest = null;

    private HttpServletResponse servletResponse = null;

    private WindowState state = null;

    private CtxInstance ctxInstance = null;





    public PortletURLImpl(

        CtxInstance ctxInstance,

        javax.servlet.http.HttpServletRequest servletRequest,

        javax.servlet.http.HttpServletResponse servletResponse)

    {

        this.servletRequest = servletRequest;

        this.servletResponse = servletResponse;

        this.ctxInstance = ctxInstance;

        secure = servletRequest.isSecure();

    }



    // javax.portlet.PortletURL -------------------------------------------------------------------

    public void setWindowState(WindowState windowState) throws WindowStateException

    {

//        PortalContext portalContext = .getPortalContext();

//        Enumeration supportedWindowStates = portalContext.getSupportedWindowStates();

//        if (windowState != null)

//        {

//            while (supportedWindowStates.hasMoreElements())

//            {

//                WindowState supportedWindowState = (WindowState) supportedWindowStates.nextElement();

//                if (windowState.equals(supportedWindowState))

//                {

//                    state = windowState;

//                    return;

//                }

//            }

//        }

        throw new WindowStateException("unsupported Window State used: " + windowState, windowState);

    }



    public void setPortletMode(PortletMode portletMode) throws PortletModeException

    {

        if (isPortletModeSupported(portletMode))

        {

            mode = portletMode;

            return;

        }

        throw new PortletModeException("unsupported Portlet Mode used: " + portletMode, portletMode);

    }



    public void setParameter(String name, String value)

    {

        if (name == null || value == null)

        {

            throw new IllegalArgumentException("name and value must not be null");

        }



        parameters.put(name, value);

    }



    public void setParameter(String name, String[] values)

    {

        if (name == null || values == null || values.length == 0)

        {

            throw new IllegalArgumentException("name and values must not be null or values be an empty array");

        }



        parameters.put(name, values);

    }



    /* (non-Javadoc)

     * @see javax.portlet.PortletURL#setParameters(Map)

     */

    public void setParameters(Map parameters)

    {

        if (parameters == null)

        {

            throw new IllegalArgumentException("Parameters must not be null.");

        }

        for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();)

        {

            Map.Entry entry = (Map.Entry) iter.next();

            if (!(entry.getKey() instanceof String))

            {

                throw new IllegalArgumentException("Key must not be null and of type java.lang.String.");

            }

            if (!(entry.getValue() instanceof String[]))

            {

                throw new IllegalArgumentException("Value must not be null and of type java.lang.String[].");

            }

        }



        this.parameters = new HashMap(parameters);

    }



    public void setSecure(boolean secure) throws PortletSecurityException

    {

        // This implementation does assume not having a supporting security environment installed!

        if (secure == true)

        {

            throw new PortletSecurityException(

                "The current implementation does assume not having a supporting security environment installed!"

            );

        }



        this.secure = secure;

    }



    public String toString()

    {

        StringBuffer url = new StringBuffer(200);



//        if (parameters != null)

//        {

//            Iterator names = parameters.keySet().iterator();

//            while (names.hasNext())

//            {

//                String name = (String) names.next();

//                Object value = parameters.get(name);

//                String[] values = value instanceof String ? new String[]{(String) value} : (String[]) value;

//                if (action)

//                {

//                    controlURL.setRequestParam(name, values);

//                }

//                else

//                {

//                    controlURL.setRenderParam(portletWindow, name, values);

//                }

//            }

//        }



        return url.toString();

    }

    // --------------------------------------------------------------------------------------------



    // internal methods ---------------------------------------------------------------------------

    private boolean isPortletModeSupported(PortletMode requestedPortletMode)

    {

//        PortletDefinition portletDefinition = referencedPortletWindow.getPortletEntity().getPortletDefinition();

//        ContentTypeSet contentTypes = portletDefinition.getContentTypeSet();

//        ContentType contentType = contentTypes.get("text/html");

//        Iterator portletModes = contentType.getPortletModes();

//        if (requestedPortletMode != null)

//        {

//            while (portletModes.hasNext())

//            {

//                PortletMode supportedPortletMode = (PortletMode) portletModes.next();

//                if (requestedPortletMode.equals(supportedPortletMode))

//                {

//                    return true;

//                }

//            }

//        }

        return false;

    }

    // --------------------------------------------------------------------------------------------



    // additional methods -------------------------------------------------------------------------

    public String getParameter(String name)

    {

        return (String) parameters.get(name);

    }



    public String[] getParameters(String name)

    {

        return (String[]) parameters.get(name);

    }



    public PortletMode getPortletMode()

    {

        return mode;

    }



    public WindowState getWindowState()

    {

        return state;

    }

    // --------------------------------------------------------------------------------------------





    private boolean action;



    public void setAction()

    {

        action = true;

    }



    public void setSecure()

    {

        secure = true;

    }



}

