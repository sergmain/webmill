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

 * Date: 29.01.2004

 * Time: 19:01:50

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portlet.impl;



import java.io.File;

import java.io.FileInputStream;

import java.util.Map;

import java.util.List;

import java.util.ArrayList;



import javax.portlet.PortletConfig;

import javax.portlet.PortletContext;

import javax.servlet.ServletConfig;

import javax.servlet.ServletContext;



import org.riverock.interfaces.schema.javax.portlet.PortletType;



import org.apache.pluto.om.common.ObjectID;

import org.apache.pluto.om.portlet.PortletApplicationDefinition;

import org.apache.pluto.om.portlet.PortletDefinitionList;

import org.apache.pluto.om.servlet.WebApplicationDefinition;

import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;





public class PortletApplicationDefinitionImp implements PortletApplicationDefinition

{

    private static String fileSeparator = System.getProperty("file.separator");



    private PortletContext portletContext = null;

    private PortletConfig portletConfig = null;

    private ServletConfig servletConfig = null;



    private ServletContext servletContext = null;

    // Base Dir where all web modules are located

    private String baseWMDir = null;



    private PortletApplicationDefinition application = null;



    private void load() throws Exception

    {

        File f = new File(baseWMDir);

        String[] entries = f.list();

        for (int i=0; i<entries.length; i++)

        {

            File entry = new File(baseWMDir+entries[i]);

            if (entry.isDirectory())

            {

                load(baseWMDir, entries[i]);

            }

        }

    }



    private void load(String baseDir, String webModule) throws Exception

    {

    }



    private String contextPath = null;

    private WebApplicationDefinition webApplication = null;



    public void bbb(ServletConfig config)

        throws Exception

    {

        servletContext = config.getServletContext();



        PortletType portletDefinition = new PortletType();

        portletContext = new PortletContextImpl( config.getServletContext() );

        portletConfig = new PortletConfigImpl(config, portletContext, portletDefinition);



    }



    public void aa(ServletConfig config)

        throws Exception

    {

        servletContext = config.getServletContext();



        {

            baseWMDir = servletContext.getRealPath("");

            baseWMDir = baseWMDir.substring(0, baseWMDir.lastIndexOf(fileSeparator))+fileSeparator;

        }



        List structure = new ArrayList();

//        structure.add("/" + webModule);

        structure.add("/mill" );

        structure.add(null);

        structure.add(null);



        String contextRoot = (String)structure.get(0);

        WebApplicationDefinition webApplication = (WebApplicationDefinition)structure.get(1);

        Map servletMap = (Map)structure.get(2);



        setContextRoot(contextRoot);



        PortletType portletDefinition = new PortletType();

        portletContext = new PortletContextImpl( config.getServletContext() );

        portletConfig = new PortletConfigImpl(config, portletContext, portletDefinition);



    }



    protected void setPortletApplicationDefinition(PortletApplicationDefinition application)

    {

        this.application = application;

    }



    public void setContextRoot(String contextPath)

    {

        this.contextPath = contextPath;

    }



    protected void setWebApplicationDefinition(WebApplicationDefinition webApplication)

    {

        this.webApplication = webApplication;

    }



    public ObjectID getId()

    {

        return null;

    }



    public String getVersion()

    {

        return null;

    }



    public PortletDefinitionList getPortletDefinitionList()

    {

        return null;

    }



    public WebApplicationDefinition getWebApplicationDefinition()

    {

        return null;

    }



    public PortletApplicationDefinition getPortletApplicationDefinition()

    {

        return application;

    }





}

