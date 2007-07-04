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

import java.lang.String;
import java.math.BigInteger;

import org.riverock.webmill.container.definition.DefinitionUtils;
import org.riverock.webmill.container.definition.web_xml_v2_4.*;
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
    private static final String PORTLET_URI = "http://java.sun.com/portlet";

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

    private WebAppType webXml;
    private String realPortletTldFile = null;
    private static final String WEB_INF_TLD_PORTLET_TLD = "/WEB-INF/tld/portlet.tld";

    public WebmillWebApplicationRewriter(WebAppType webXml) {
        if (webXml==null) {
            throw new DeployException("webApp object is null");
        }
        this.webXml = webXml;
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
     */
    public void processWebXML() {
        try {

            if (new Double(webXml.getVersion()).floatValue()<2.4) {
                throw new IllegalStateException("web.xml file must be version 2.4 or higher");
            }
            
            ServletType registerServlet = DefinitionUtils.getServlet(webXml, WEBMILL_PORTLET_REGISTER);
            if (DefinitionUtils.getCountTaglib(webXml, PORTLET_URI)>1) {
                throw new IllegalStateException("Invalid web.xml file. URI http://java.sun.com/portlet defined more than once");
            }

            // process servlet for registering webmill portlet
            if (registerServlet == null) {
                ServletType servlet = new ServletType();
                initWebmillRegisterServlet(servlet);
                webXml.getDescriptionAndDisplayNameAndIcon().add(servlet);
            }
            else {
                initWebmillRegisterServlet(registerServlet);
            }

            TaglibType portletTaglib = DefinitionUtils.getTaglib(webXml, PORTLET_URI);
            JspConfigType jspConfig = DefinitionUtils.getJspConfig(webXml);
            if (jspConfig==null) {
                jspConfig = new JspConfigType();
                webXml.getDescriptionAndDisplayNameAndIcon().add(jspConfig);
            }

            if (portletTaglib == null) {
                realPortletTldFile = WEB_INF_TLD_PORTLET_TLD;

                TaglibType taglib = new TaglibType();
                PathType path= new PathType();
                path.setValue(realPortletTldFile);
                taglib.setTaglibLocation(path);
                org.riverock.webmill.container.definition.web_xml_v2_4.String uri = new org.riverock.webmill.container.definition.web_xml_v2_4.String();
                uri.setValue(PORTLET_URI);
                taglib.setTaglibUri(uri);
                jspConfig.getTaglib().add(taglib);
            }
            else {
                if (portletTaglib.getTaglibLocation()==null || portletTaglib.getTaglibLocation().getValue()==null) {
                    PathType path= new PathType();
                    path.setValue(WEB_INF_TLD_PORTLET_TLD);
                    portletTaglib.setTaglibLocation(path);
                }
                realPortletTldFile = portletTaglib.getTaglibLocation().getValue();
            }
        }
        catch (Exception e) {
            throw new DeployException("Unable to process web.xml for infusion " + e.toString(), e);
        }
    }

    private void initWebmillRegisterServlet(ServletType servlet) {
        ServletNameType servletName = new ServletNameType();
        servletName.setValue(WEBMILL_PORTLET_REGISTER);
        servlet.setServletName(servletName);
        DescriptionType description = new DescriptionType();
        description.setValue("Webmill servlet for register portlet application archive");
        servlet.getDescription().add(description);

        DisplayNameType displayName = new DisplayNameType();
        displayName.setValue("Webmill portlet register");
        servlet.getDisplayName().add(displayName);
        FullyQualifiedClassType clazzName = new FullyQualifiedClassType();
        clazzName.setValue(PortletRegisterServlet.class.getName());
        servlet.setServletClass(clazzName);


        XsdIntegerType xsdInteger = new XsdIntegerType();
        xsdInteger.setValue(new BigInteger("1"));
        servlet.setLoadOnStartup(xsdInteger);
    }

    public String getRealPortletTldFile() {
        return realPortletTldFile;
    }
}
