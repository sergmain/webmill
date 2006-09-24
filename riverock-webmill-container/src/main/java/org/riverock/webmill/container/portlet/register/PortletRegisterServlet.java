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
package org.riverock.webmill.container.portlet.register;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerException;

/**
 * @author smaslyukov
 *         Date: 04.08.2005
 *         Time: 22:09:41
 *         $Id$
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
                String portalPath = new File(servletConfig.getServletContext().getRealPath("/")).getParent();
                PortletContainer.registerPortletFile( uniqueName, portletFile, servletConfig, classLoader, portalPath );
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
            PortletContainer.destroy( uniqueName, new File(servletConfig.getServletContext().getRealPath("/")).getParent() );
        }
        catch(Throwable th) {
            th.printStackTrace( System.out );
            throw new IllegalStateException( "Eror destroy portlet" );
        }
    }
}
