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



package org.riverock.webmill.portlet.impl;



import java.net.MalformedURLException;



import javax.portlet.PortletContext;



import org.apache.pluto.core.impl.PortletRequestDispatcherImpl;



public class PortletContextImpl implements PortletContext

//    , InternalPortletContext

{

//    private PortletApplicationDefinition portletApplicationDefinition;

    private javax.servlet.ServletContext servletContext;



    public PortletContextImpl(javax.servlet.ServletContext servletContext)

//        ,PortletApplicationDefinition portletApplicationDefinition)

    {

        this.servletContext = servletContext;

//        this.portletApplicationDefinition = portletApplicationDefinition;

    }



    // javax.portlet.PortletContext implementation ------------------------------------------------

    public String getServerInfo()

    {

        return "Pluto/1.0";

    }



    public javax.portlet.PortletRequestDispatcher getRequestDispatcher(String path)

    {

		try {

	        javax.servlet.RequestDispatcher rd = servletContext.getRequestDispatcher(path);

    	    return new PortletRequestDispatcherImpl(rd);

        } catch (Exception e) {

    		// need to catch exception because of tomcat 4.x bug

    		// tomcat throws an exception instead of return null

    		// if the path was not found

    		return null;

    	}



    }



    public javax.portlet.PortletRequestDispatcher getNamedDispatcher(String name)

    {

       	javax.servlet.RequestDispatcher rd = servletContext.getNamedDispatcher(name);

       	return rd != null ? new PortletRequestDispatcherImpl(rd)

           	              : null;

    }



    public java.io.InputStream getResourceAsStream(String path)

    {

        return servletContext.getResourceAsStream(path);

    }

    

    public int getMajorVersion()

    {

        return 1;

    }



    public int getMinorVersion()

    {

        return 0;

    }



    public String getMimeType(String file)

    {

        return servletContext.getMimeType(file);

    }



    public String getRealPath(String path)

    {

        return servletContext.getRealPath(path);

    }



    public java.util.Set getResourcePaths(String path)

    {

        return servletContext.getResourcePaths(path);

    }



    public java.net.URL getResource(String path) throws MalformedURLException

    {

        if (path == null || !path.startsWith("/"))

        {

            throw new MalformedURLException("path must start with a '/'");

        }

        return servletContext.getResource(path);

    }



    public Object getAttribute(String name)

    {

        if (name == null)

        {

            throw new IllegalArgumentException("Attribute name == null");

        }



        return servletContext.getAttribute(name);

    }



    public java.util.Enumeration getAttributeNames()

    {

        return servletContext.getAttributeNames();

    }



    public String getInitParameter(String name)

    {

        if (name == null)

        {

            throw new IllegalArgumentException("Parameter name == null");

        }



        return servletContext.getInitParameter(name);

    }



    public java.util.Enumeration getInitParameterNames()

    {

        return servletContext.getInitParameterNames();

    }



    public void log(String msg)

    {

        servletContext.log(msg);

    }



    public void log(String message, Throwable throwable)

    {

        servletContext.log(message, throwable);

    }



    public void removeAttribute(String name)

    {

        if (name == null)

        {

            throw new IllegalArgumentException("Attribute name == null");

        }



        servletContext.removeAttribute(name);

    }



    public void setAttribute(String name, Object object)

    {

        if (name == null)

        {

            throw new IllegalArgumentException("Attribute name == null");

        }



        servletContext.setAttribute(name, object);

    }



    public String getPortletContextName()

    {

        return servletContext.getServletContextName();

    }

    // --------------------------------------------------------------------------------------------



    // org.apache.pluto.core.InternalPortletContext implementation --------------------------------

    public javax.servlet.ServletContext getServletContext()

    {

        return servletContext;

    }



//    public PortletApplicationDefinition getInternalPortletApplicationDefinition()

//    {

//        return portletApplicationDefinition;

//    }

    // --------------------------------------------------------------------------------------------

}



