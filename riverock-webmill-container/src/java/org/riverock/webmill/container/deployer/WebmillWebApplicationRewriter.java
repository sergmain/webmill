/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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

/*
 * Copyright 2000-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.riverock.webmill.container.deployer;

import java.util.Arrays;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;
import org.jdom.xpath.XPath;

import org.riverock.webmill.container.portlet.register.PortletRegisterServlet;

/**
 * Utilities for manipulating the web.xml deployment descriptor
 * 
 * @author <a href="mailto:sweaver@einnovation.com">Scott T. Weaver </a>
 * @author <a href="mailto:mavery@einnovation.com">Matt Avery </a>
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: WebDescriptorUtilities.java,v 1.2 2004/05/12 22:25:04 taylor
 *                Exp $
 */
public class WebmillWebApplicationRewriter
{
    private static final String WEBMILL_PORTLET_REGISTER = "WebmillPortletRegister";
    public static final String WEBMILL_SERVLET_XPATH = "/web-app/servlet/servlet-name[contains(child::text(), \""+WEBMILL_PORTLET_REGISTER+"\")]";
    public static final String WEBMILL_SERVLET_MAPPING_XPATH = "/web-app/servlet-mapping/servlet-name[contains(child::text(), \""+WEBMILL_PORTLET_REGISTER+"\")]";
    public static final String PORTLET_TAGLIB_XPATH = "/web-app/taglib/taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")]";
    protected static final String WEB_XML_PATH = "WEB-INF/web.xml";

    protected static final String[] ELEMENTS_BEFORE_SERVLET = new String[]{"icon", "display-name", "description",
            "distributable", "context-param", "filter", "filter-mapping", "listener", "servlet"};
    protected static final String[] ELEMENTS_BEFORE_SERVLET_MAPPING = new String[]{"icon", "display-name",
            "description", "distributable", "context-param", "filter", "filter-mapping", "listener", "servlet",
            "servlet-mapping"};
    
    protected static final String[] ELEMENTS_BEFORE_TAGLIB_MAPPING = new String[]{"icon", "display-name",
            "description", "distributable", "context-param", "filter", "filter-mapping", "listener", "servlet",
            "servlet-mapping", "session-config", "mime-mapping", "welcome-file-list", "error-page", "taglib"};
      
    private Document document;
    private String portletApplication;
    private boolean changed = false;
    private boolean portletTaglibAdded = false;
    private static final String LOAD_ON_STARTUP_NAME = "load-on-startup";

    public WebmillWebApplicationRewriter(Document doc, String portletApplication)
    {
            this.document = doc;
            this.portletApplication = portletApplication;
    }

    public WebmillWebApplicationRewriter(Document doc)
    {
            this.document = doc;
    }
    
    /**
     * 
     * <p>
     * processWebXML
     * </p>
     * 
     * Infuses this PortletApplicationWar's web.xml file with
     * <code>servlet</code> and a <code>servlet-mapping</code> element for
     * the JetspeedContainer servlet. This is only done if the descriptor does
     * not already contain these items.
     * 
     * @throws Exception
     *             if there is a problem infusing
     */
    public void processWebXML()
    throws Exception
    {
        try
        {
            Element root = document.getRootElement();
        
            Object jetspeedServlet = XPath.selectSingleNode(document, WEBMILL_SERVLET_XPATH);
            Object jetspeedServletMapping = XPath.selectSingleNode(document, WEBMILL_SERVLET_MAPPING_XPATH);
            Object portletTaglib = XPath.selectSingleNode(document, PORTLET_TAGLIB_XPATH);
            
            if (!document.hasRootElement())
            {
                root = new Element("web-app");
                document.setRootElement(root);
            }
        
            if (jetspeedServlet == null)
            {
                Element jetspeedServletElement = new Element("servlet");
                Element servletName = (Element) new Element("servlet-name").addContent(WEBMILL_PORTLET_REGISTER);
                Element servletDspName = (Element) new Element("display-name").addContent("Webmill portlet register");
                Element servletDesc = (Element) new Element("description")
                        .addContent("Webmill servlet for register portlet application archive");
                Element servletClass = (Element) new Element("servlet-class")
                        .addContent( PortletRegisterServlet.class.getName() );
                jetspeedServletElement.addContent(servletName);
                jetspeedServletElement.addContent(servletDspName);
                jetspeedServletElement.addContent(servletDesc);
                jetspeedServletElement.addContent(servletClass);
                insertContextNameParam(jetspeedServletElement);
                insertLoadOnStartup(jetspeedServletElement);
                insertElementCorrectly(root, jetspeedServletElement, ELEMENTS_BEFORE_SERVLET);
                changed = true;
            }
            else
            {
                // double check for register at Init
                if (jetspeedServlet instanceof Element)
                {
                    Parent jetspeedServletElement =((Element)jetspeedServlet).getParent();
                    if (null == XPath.selectSingleNode(jetspeedServletElement, "init-param/param-name[contains(child::text(), \"contextName\")]"))
                    {
                      insertContextNameParam((Element)jetspeedServletElement);
                    }
                    if (null == XPath.selectSingleNode(jetspeedServletElement, LOAD_ON_STARTUP_NAME))
                    {
                        insertLoadOnStartup((Element) jetspeedServletElement);
                    }
                }
            }
    
            if (jetspeedServletMapping == null)
            {
    
                Element jetspeedServletMappingElement = new Element("servlet-mapping");
    
                Element servletMapName = (Element) new Element("servlet-name").addContent(WEBMILL_PORTLET_REGISTER);
                Element servletUrlPattern = (Element) new Element("url-pattern").addContent("/container/*");
    
                jetspeedServletMappingElement.addContent(servletMapName);
                jetspeedServletMappingElement.addContent(servletUrlPattern);
    
                insertElementCorrectly(root, jetspeedServletMappingElement, ELEMENTS_BEFORE_SERVLET_MAPPING);
                changed = true;
            }
            
            if(portletTaglib == null)
            {
                Element taglib = new Element ("taglib");
                Element taguri = (Element) new Element("taglib-uri").addContent("http://java.sun.com/portlet");
                Element taglocation = (Element) new Element("taglib-location").addContent("/WEB-INF/tld/portlet.tld");
                
                taglib.addContent(taguri);
                taglib.addContent(taglocation);
                
                insertElementCorrectly(root, taglib, ELEMENTS_BEFORE_TAGLIB_MAPPING);
                changed = true;
                portletTaglibAdded = true;
            }
        }
        catch (Exception e)
        {
            throw new Exception("Unable to process web.xml for infusion " + e.toString(), e);
        }
    
    }
    
    private void insertContextNameParam(Element jetspeedServletElement)
    {
      Element param2Name = (Element) new Element("param-name").addContent("contextName");
        Element param2Value = (Element) new Element("param-value").addContent(portletApplication); 
        Element init2Param = new Element("init-param");
        init2Param.addContent(param2Name);
        init2Param.addContent(param2Value);
        jetspeedServletElement.addContent(init2Param);                    
        
    }
    
    private void insertLoadOnStartup(Element jetspeedServletElement)
    {
        Element loadOnStartup = (Element) new Element(LOAD_ON_STARTUP_NAME).addContent("1");
        jetspeedServletElement.addContent(loadOnStartup);        
    }
    
    public boolean isChanged()
    {
        return changed;
    }
    
    /**
     * 
     * <p>
     * insertElementCorrectly
     * </p>
     * 
     * @param root
     *            JDom element representing the &lt; web-app &gt;
     * @param toInsert
     *            JDom element to insert into the web.xml hierarchy.
     * @param elementsBefore
     *            an array of web.xml elements that should be defined before the
     *            element we want to insert. This order should be the order
     *            defined by the web.xml's DTD type definition.
     */
    protected void insertElementCorrectly( Element root, Element toInsert, String[] elementsBefore )
    throws Exception
    {
        List allChildren = root.getChildren();
        List elementsBeforeList = Arrays.asList(elementsBefore);
        toInsert.detach();
        int insertAfter = 0;
        int count = 0;
        for (int i = 0; i < allChildren.size(); i++)
        {
            Element element = (Element) allChildren.get(i);
            if (elementsBeforeList.contains(element.getName()))
            {
                // determine the Content index of the element to insert after
                insertAfter = root.indexOf(element);
            }
            count++;
        }
    
        insertAfter = (count == 0) ? 0 : insertAfter + 1;
        
        try
        {
            root.addContent(insertAfter, toInsert);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            root.addContent(toInsert);
        }
    }
    
    
    /**
     * @return Returns the portletTaglibAdded.
     */
    public boolean isPortletTaglibAdded()
    {
        return portletTaglibAdded;
    }
}