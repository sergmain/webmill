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
package org.riverock.webmill.container.deployer;

import org.apache.xpath.XPathAPI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

    static final String WEB_XML_VERSION_XPATH = "/web-app/@version";
    static final String WEBMILL_SERVLET_XPATH = "/web-app/servlet/servlet-name[contains(child::text(), \"" + WEBMILL_PORTLET_REGISTER + "\")]";
    static final String PORTLET_TAGLIB_COUNT_XPATH = "count(/web-app/jsp-config/taglib/taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")])";
    static final String PORTLET_TAGLIB_XPATH = "/web-app/jsp-config/taglib/taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")]";
    static final String PORTLET_TAGLIB_LOCATION_XPATH = "/web-app/jsp-config/taglib[taglib-uri[contains(child::text(), \"http://java.sun.com/portlet\")]]/taglib-location";

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
            Element root = document.getDocumentElement();

            if (new Double(XPathAPI.eval(document, WEB_XML_VERSION_XPATH).toString()).floatValue()<2.4) {
                throw new IllegalStateException("web.xml file must be version 2.4 or higher");
            }
            
            Node registerServlet = XPathAPI.selectSingleNode(document, WEBMILL_SERVLET_XPATH);
            Node portletTagCount = XPathAPI.selectSingleNode(document, PORTLET_TAGLIB_COUNT_XPATH);
            int countTaglibUril = new Double(portletTagCount.toString()).intValue();
            if (countTaglibUril>1) {
                throw new IllegalStateException("Invalid web.xml file. URI http://java.sun.com/portlet defined more than once");
            }

            if (registerServlet == null) {
                Element registerServletElement = document.createElement("servlet");
                Element servletName = document.createElement("servlet-name");
                    servletName.setNodeValue(WEBMILL_PORTLET_REGISTER);
                Element servletDspName = document.createElement("display-name");
                    servletDspName.setNodeValue("Webmill portlet register");
                Element servletDesc = document.createElement("description");
                    servletDesc.setNodeValue("Webmill servlet for register portlet application archive");
                Element servletClass = document.createElement("servlet-class");
                    servletClass.setNodeValue(PortletRegisterServlet.class.getName());

                registerServletElement.appendChild(servletName);
                registerServletElement.appendChild(servletDspName);
                registerServletElement.appendChild(servletDesc);
                registerServletElement.appendChild(servletClass);
                insertLoadOnStartup(document, registerServletElement);
                insertElementCorrectly(root, registerServletElement, ELEMENTS_BEFORE_SERVLET);
            }
            else {
                // double check for register at Init
                if (registerServlet instanceof Element) {
                    Node registerServletElement = registerServlet.getParentNode();
                    if (null == XPathAPI.selectSingleNode(registerServletElement, LOAD_ON_STARTUP_NAME)) {
                        insertLoadOnStartup(document, (Element) registerServletElement);
                    }
                }
            }

            Object portletTaglib = XPathAPI.selectSingleNode(document, PORTLET_TAGLIB_XPATH);
            if (portletTaglib == null) {
                realPortletTldFile = "/WEB-INF/tld/portlet.tld";

                Element taglib = document.createElement("taglib");
                Element taguri = document.createElement("taglib-uri");
                    taguri.setNodeValue("http://java.sun.com/portlet");
                Element taglocation = document.createElement("taglib-location");
                    taglocation.setNodeValue(realPortletTldFile);

                taglib.appendChild(taguri);
                taglib.appendChild(taglocation);

                insertElementCorrectly(root, taglib, ELEMENTS_BEFORE_TAGLIB_MAPPING);
                portletTaglibAdded = true;

            }
            else {
                Object portletTaglibLocation = XPathAPI.selectSingleNode(document, PORTLET_TAGLIB_LOCATION_XPATH);
                if (portletTaglibLocation==null) {
                    throw new IllegalStateException("portlet.tld file not defined in web.xml");
                }
                realPortletTldFile = ((Element)portletTaglibLocation).getNodeValue();
            }
        }
        catch (Exception e) {
            throw new Exception("Unable to process web.xml for infusion " + e.toString(), e);
        }

    }

    private void insertLoadOnStartup(Document document, Element jetspeedServletElement) {
        Element loadOnStartup = document.createElement(LOAD_ON_STARTUP_NAME);
            loadOnStartup.setNodeValue("1");
        jetspeedServletElement.appendChild(loadOnStartup);
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
        if (true) throw new RuntimeException("Need rewrite");
/*

        NodeList allChildren = root.getChildNodes();
        List<String> elementsBeforeList = Arrays.asList(elementsBefore);
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
*/
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
