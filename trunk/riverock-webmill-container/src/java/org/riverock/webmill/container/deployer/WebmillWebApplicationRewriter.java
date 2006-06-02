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
 * orginal project: Pluto, license: Apache2
 *
 * @author <a href="mailto:sweaver@einnovation.com">Scott T. Weaver </a>
 * @author <a href="mailto:mavery@einnovation.com">Matt Avery </a>
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: WebDescriptorUtilities.java,v 1.2 2004/05/12 22:25:04 taylor
 *          Exp $
 */
public class WebmillWebApplicationRewriter {
    private static final String WEBMILL_PORTLET_REGISTER = "WebmillPortletRegister";
    public static final String WEBMILL_SERVLET_XPATH = "/web-app/servlet/servlet-name[contains(child::text(), \"" + WEBMILL_PORTLET_REGISTER + "\")]";
//    public static final String WEBMILL_SERVLET_MAPPING_XPATH = "/web-app/servlet-mapping/servlet-name[contains(child::text(), \"" + WEBMILL_PORTLET_REGISTER + "\")]";
    public static final String PORTLET_TAGLIB_COUNT_XPATH = "count(/web-app/taglib/taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")])";
    public static final String PORTLET_TAGLIB_XPATH = "/web-app/taglib/taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")]";
    public static final String PORTLET_TAGLIB_LOCATION_XPATH = "/web-app/taglib[taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")]]/taglib-location";
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
    private boolean portletTaglibAdded = false;
    private static final String LOAD_ON_STARTUP_NAME = "load-on-startup";
    private String realPortletTldFile = null;

    public WebmillWebApplicationRewriter(Document doc) {
        this.document = doc;
    }

    /**
     * <p/>
     * processWebXML
     * </p>
     * <p/>
     * Infuses this PortletApplicationWar's web.xml file with
     * <code>servlet</code> and a <code>servlet-mapping</code> element for
     * the JetspeedContainer servlet. This is only done if the descriptor does
     * not already contain these items.
     *
     * @throws Exception if there is a problem infusing
     */
    public void processWebXML() throws Exception {
        try {
            Element root = document.getRootElement();

            Object registerServlet = XPath.selectSingleNode(document, WEBMILL_SERVLET_XPATH);
            Object portletTagCount = XPath.selectSingleNode(document, PORTLET_TAGLIB_COUNT_XPATH);
            int countTaglibUril = new Double(portletTagCount.toString()).intValue();
            if (countTaglibUril>1) {
                throw new IllegalStateException("Invalid web.xml file. URI http://java.sun.com/portlet defined more than once");
            }

            if (!document.hasRootElement()) {
                root = new Element("web-app");
                document.setRootElement(root);
            }

            if (registerServlet == null) {
                Element registerServletElement = new Element("servlet");
                Element servletName = new Element("servlet-name").addContent(WEBMILL_PORTLET_REGISTER);
                Element servletDspName = new Element("display-name").addContent("Webmill portlet register");
                Element servletDesc = new Element("description").addContent("Webmill servlet for register portlet application archive");
                Element servletClass = new Element("servlet-class").addContent(PortletRegisterServlet.class.getName());

                registerServletElement.addContent(servletName);
                registerServletElement.addContent(servletDspName);
                registerServletElement.addContent(servletDesc);
                registerServletElement.addContent(servletClass);
                insertLoadOnStartup(registerServletElement);
                insertElementCorrectly(root, registerServletElement, ELEMENTS_BEFORE_SERVLET);
            } else {
                // double check for register at Init
                if (registerServlet instanceof Element) {
                    Parent registerServletElement = ((Element) registerServlet).getParent();
                    if (null == XPath.selectSingleNode(registerServletElement, LOAD_ON_STARTUP_NAME)) {
                        insertLoadOnStartup((Element) registerServletElement);
                    }
                }
            }

            Object portletTaglib = XPath.selectSingleNode(document, PORTLET_TAGLIB_XPATH);
            if (portletTaglib == null) {
                realPortletTldFile = "/WEB-INF/tld/portlet.tld";

                Element taglib = new Element("taglib");
                Element taguri = new Element("taglib-uri").addContent("http://java.sun.com/portlet");
                Element taglocation = new Element("taglib-location").addContent(realPortletTldFile);

                taglib.addContent(taguri);
                taglib.addContent(taglocation);

                insertElementCorrectly(root, taglib, ELEMENTS_BEFORE_TAGLIB_MAPPING);
                portletTaglibAdded = true;

            }
            else {
                Object portletTaglibLocation = XPath.selectSingleNode(document, PORTLET_TAGLIB_LOCATION_XPATH);
                if (portletTaglibLocation==null) {
                    throw new IllegalStateException("portlet.tld file not defined in web.xml");
                }
                realPortletTldFile = ((Element)portletTaglibLocation).getText();
            }
        }
        catch (Exception e) {
            throw new Exception("Unable to process web.xml for infusion " + e.toString(), e);
        }

    }

    private void insertLoadOnStartup(Element jetspeedServletElement) {
        Element loadOnStartup = new Element(LOAD_ON_STARTUP_NAME).addContent("1");
        jetspeedServletElement.addContent(loadOnStartup);
    }

    /**
     * <p/>
     * insertElementCorrectly
     * </p>
     *
     * @param root           JDom element representing the &lt; web-app &gt;
     * @param toInsert       JDom element to insert into the web.xml hierarchy.
     * @param elementsBefore an array of web.xml elements that should be defined before the
     *                       element we want to insert. This order should be the order
     *                       defined by the web.xml's DTD type definition.
     */
    protected void insertElementCorrectly(Element root, Element toInsert, String[] elementsBefore)
        throws Exception {
        List allChildren = root.getChildren();
        List<String> elementsBeforeList = Arrays.asList(elementsBefore);
        toInsert.detach();
        int insertAfter = 0;
        int count = 0;
        for (Object anAllChildren : allChildren) {
            Element element = (Element) anAllChildren;
            if (elementsBeforeList.contains(element.getName())) {
                // determine the Content index of the element to insert after
                insertAfter = root.indexOf(element);
            }
            count++;
        }

        insertAfter = (count == 0) ? 0 : insertAfter + 1;

        try {
            root.addContent(insertAfter, toInsert);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            root.addContent(toInsert);
        }
    }

    public String getRealPortletTldFile() {
        return realPortletTldFile;
    }

    /**
     * @return Returns the portletTaglibAdded.
     */
    public boolean isPortletTaglibAdded() {
        return portletTaglibAdded;
    }
}
