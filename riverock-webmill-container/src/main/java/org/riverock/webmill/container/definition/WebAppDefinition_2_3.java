package org.riverock.webmill.container.definition;

import java.util.ArrayList;
import java.util.List;

import org.riverock.webmill.container.definition.web_xml_abstract.Servlet;
import org.riverock.webmill.container.definition.web_xml_abstract.Servlet_2_3;
import org.riverock.webmill.container.definition.web_xml_v2_3.*;
import org.riverock.webmill.container.deployer.WebmillWebApplicationRewriter;
import org.riverock.webmill.container.portlet.register.PortletRegisterServlet;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 19:01:03
 */
public class WebAppDefinition_2_3 implements WebAppDefinition {
    private WebApp webApp;

    public WebAppDefinition_2_3(WebApp webXml) {
        this.webApp = webXml;
    }

    public Class getWebAppClass() {
        return WebApp.class;
    }

    public String getWebAppPackage() {
        return ObjectFactory.class.getPackage().getName();
    }

    public Object getWebAppDefinition() {
        return webApp;
    }

    public List<Servlet> getServlets() {
        List<Servlet> servlets = new ArrayList<Servlet>();
        for (org.riverock.webmill.container.definition.web_xml_v2_3.Servlet o : webApp.getServlet()) {
            servlets.add( new Servlet_2_3(o) );
        }
        return servlets;
    }

    public int getCountTaglib(String taglibUri) {
        int count=0;
        for (Taglib taglib : webApp.getTaglib()) {
            if (taglib.getTaglibUri().getContent().equals(taglibUri)) {
                count++;
            }
        }
        return count;
    }

    public void initWebmillRegisterServlet(Servlet abstractServlet) {
        org.riverock.webmill.container.definition.web_xml_v2_3.Servlet servlet = (org.riverock.webmill.container.definition.web_xml_v2_3.Servlet)abstractServlet.getServlet() ;
        ServletName servletName = new ServletName();
        servletName.setContent(WebmillWebApplicationRewriter.WEBMILL_PORTLET_REGISTER);
        servlet.setServletName(servletName);
        Description description = new Description();
        description.setContent("Webmill servlet for register portlet application archive");
        servlet.setDescription(description);

        DisplayName displayName = new DisplayName();
        displayName.setContent("Webmill portlet register");
        servlet.setDisplayName(displayName);
        ServletClass clazzName = new ServletClass();
        clazzName.setContent(PortletRegisterServlet.class.getName());
        servlet.setServletClass(clazzName);


        LoadOnStartup loadOnStartup = new LoadOnStartup();
        loadOnStartup.setContent("1");
        servlet.setLoadOnStartup(loadOnStartup);

        webApp.getServlet().add( servlet );
    }

    public void initWebmillRegisterServlet() {
        initWebmillRegisterServlet( new Servlet_2_3(new org.riverock.webmill.container.definition.web_xml_v2_3.Servlet()) );
    }

    public String prepareTaglib() {
        Taglib portletTaglib = getTaglib(webApp, WebmillWebApplicationRewriter.PORTLET_URI);

        if (portletTaglib == null) {

            Taglib taglib = new Taglib();
            TaglibLocation path= new TaglibLocation();
            path.setContent(WebmillWebApplicationRewriter.WEB_INF_TLD_PORTLET_TLD);
            taglib.setTaglibLocation(path);
            TaglibUri uri = new TaglibUri();
            uri.setContent(WebmillWebApplicationRewriter.PORTLET_URI);
            taglib.setTaglibUri(uri);
            webApp.getTaglib().add(taglib);
            return WebmillWebApplicationRewriter.WEB_INF_TLD_PORTLET_TLD;
        }
        else {
            TaglibLocation path = new TaglibLocation();
            path.setContent(WebmillWebApplicationRewriter.WEB_INF_TLD_PORTLET_TLD);
            portletTaglib.setTaglibLocation(path);
            
            return portletTaglib.getTaglibLocation().getContent();
        }
        
    }

    private static Taglib getTaglib(WebApp webApp, String taglibUri) {
        for (Taglib taglib : webApp.getTaglib()) {
            if (taglib.getTaglibUri().getContent().equals(taglibUri)) {
                return taglib;
            }
        }
        return null;
    }

}
