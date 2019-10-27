package org.riverock.webmill.container.definition;

import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.lang.String;

import org.riverock.webmill.container.definition.web_xml_abstract.Servlet;
import org.riverock.webmill.container.definition.web_xml_abstract.Servlet_2_4;
import org.riverock.webmill.container.definition.web_xml_v2_4.*;
import org.riverock.webmill.container.portlet.register.PortletRegisterServlet;
import org.riverock.webmill.container.deployer.WebmillWebApplicationRewriter;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 19:01:50
 */
public class WebAppDefinition_2_4 implements WebAppDefinition {
    private WebAppType webApp;

    public WebAppDefinition_2_4(WebAppType webXml) {
        this.webApp = webXml;
    }

    public Class getWebAppClass() {
        return WebAppType.class;
    }

    public String getWebAppPackage() {
        return ObjectFactory.class.getPackage().getName();
    }

    public Object getWebAppDefinition() {
        return webApp;
    }

    public List<Servlet> getServlets() {
        List<Servlet> servlets = new ArrayList<Servlet>();
        for (Object o : webApp.getDescriptionAndDisplayNameAndIcon()) {
            if (o instanceof ServletType) {
                servlets.add( new Servlet_2_4((ServletType)o) );
            }
        }
        return servlets;
    }

    public int getCountTaglib(String taglibUri) {
        JspConfigType jspConfig = getJspConfig(webApp);
        if (jspConfig==null) {
            return 0;
        }
        int count=0;
        for (TaglibType taglib : jspConfig.getTaglib()) {
            if (taglib.getTaglibUri().equals(taglibUri)) {
                count++;
            }
        }
        return count;
    }

    private static JspConfigType getJspConfig(WebAppType webApp) {
        if (webApp==null) {
            return null;
        }
        for (Object o : webApp.getDescriptionAndDisplayNameAndIcon()) {
            if (o instanceof JspConfigType) {
                return (JspConfigType)o;
            }
        }
        return null;
    }

    public void initWebmillRegisterServlet() {
        initWebmillRegisterServlet( new Servlet_2_4(new ServletType()) );
    }

    public void initWebmillRegisterServlet(Servlet abstractServlet) {
        ServletType servlet = (ServletType)abstractServlet.getServlet();
        ServletNameType servletName = new ServletNameType();
        servletName.setValue(WebmillWebApplicationRewriter.WEBMILL_PORTLET_REGISTER);
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

        webApp.getDescriptionAndDisplayNameAndIcon().add(servlet);
    }

    public String prepareTaglib() {
        TaglibType portletTaglib = getTaglib(webApp, WebmillWebApplicationRewriter.PORTLET_URI);
        JspConfigType jspConfig = DefinitionUtils.getJspConfig(webApp);
        if (jspConfig==null) {
            jspConfig = new JspConfigType();
            webApp.getDescriptionAndDisplayNameAndIcon().add(jspConfig);
        }

        if (portletTaglib == null) {
            TaglibType taglib = new TaglibType();
            PathType path= new PathType();
            path.setValue(WebmillWebApplicationRewriter.WEB_INF_TLD_PORTLET_TLD);
            taglib.setTaglibLocation(path);
            org.riverock.webmill.container.definition.web_xml_v2_4.String uri = new org.riverock.webmill.container.definition.web_xml_v2_4.String();
            uri.setValue(WebmillWebApplicationRewriter.PORTLET_URI);
            taglib.setTaglibUri(uri);
            jspConfig.getTaglib().add(taglib);
            return WebmillWebApplicationRewriter.WEB_INF_TLD_PORTLET_TLD;
        }
        else {
            if (portletTaglib.getTaglibLocation()==null || portletTaglib.getTaglibLocation().getValue()==null) {
                PathType path= new PathType();
                path.setValue(WebmillWebApplicationRewriter.WEB_INF_TLD_PORTLET_TLD);
                portletTaglib.setTaglibLocation(path);
            }
            return portletTaglib.getTaglibLocation().getValue();
        }
    }

    private static TaglibType getTaglib(WebAppType webApp, String taglibUri) {
        JspConfigType jspConfig = getJspConfig(webApp);
        if (jspConfig==null) {
            return null;
        }
        for (TaglibType taglib : jspConfig.getTaglib()) {
            if (taglib.getTaglibUri().equals(taglibUri)) {
                return taglib;
            }
        }
        return null;
    }

}
