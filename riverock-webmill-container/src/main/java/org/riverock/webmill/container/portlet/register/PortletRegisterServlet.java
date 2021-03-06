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
package org.riverock.webmill.container.portlet.register;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletContainerFactory;
import org.riverock.webmill.container.tools.PortletContainerUtils;

/**
 * @author smaslyukov
 *         Date: 04.08.2005
 *         Time: 22:09:41
 *         $Id: PortletRegisterServlet.java 1309 2007-08-16 10:10:59Z serg_main $
 */
public class PortletRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 50434672384237876L;
    private ServletConfig servletConfig=null;

    private String uniqueName = null;
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig=servletConfig;
        uniqueName = "context-"+System.currentTimeMillis()+"-"+((int)(Math.random()*10000));
        String portletFileName =
            servletConfig.getServletContext().getRealPath("/") + File.separatorChar +
            "WEB-INF" + File.separatorChar +
            "portlet.xml";

        File portletFile = new File(portletFileName);

        final String s = "register portlet file: " + portletFileName+", exists: "+portletFile.exists();
        System.out.println(s);
        System.out.println( "servletContextName: " + servletConfig.getServletContext().getServletContextName() );
        System.out.println( "uniqueName: " + uniqueName );


        if (portletFile.exists()) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                System.out.println("PortletRegisterServlet classLoader\n" + classLoader + "\nhashCode: " + classLoader.hashCode() );
                String deployedPath = PortletContainerUtils.getDeployedInPath(servletConfig);
                System.out.println("deployed in path = " + deployedPath);
                PortletContainerFactory.registerPortletFile( uniqueName, portletFile, servletConfig, classLoader, deployedPath );
            }
            catch (PortletContainerException e) {
                String es = "Error register portlet";
                throw new ServletException( es, e);
            }
        }
    }

    public final void destroy() {
        System.out.println("#1. Undeploy uniqueName: " + uniqueName);
        try {
            PortletContainerFactory.destroy( uniqueName, PortletContainerUtils.getDeployedInPath(servletConfig));
        }
        catch(Throwable th) {
            th.printStackTrace( System.out );
            throw new IllegalStateException( "Eror destroyContextForName portlet" );
        }
    }
}
