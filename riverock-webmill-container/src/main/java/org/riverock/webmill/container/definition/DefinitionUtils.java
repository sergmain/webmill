package org.riverock.webmill.container.definition;

import java.util.List;
import java.util.ArrayList;

import org.riverock.webmill.container.definition.web_xml_v2_4.JspConfigType;
import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;
import org.riverock.webmill.container.definition.web_xml_v2_4.TaglibType;
import org.riverock.webmill.container.definition.web_xml_v2_4.ServletType;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:29:28
 */
public class DefinitionUtils {

    public static JspConfigType getJspConfig(WebAppType webApp) {
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

    public static List<ServletType> getServlets(WebAppType webApp) {
        if (webApp==null) {
            return null;
        }
        List<ServletType> servlets = new ArrayList<ServletType>();
        for (Object o : webApp.getDescriptionAndDisplayNameAndIcon()) {
            if (o instanceof ServletType) {
                servlets.add((ServletType)o);
            }
        }
        return servlets;
    }

    public static TaglibType getTaglib(WebAppType webApp, String taglibUri) {
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

    public static int getCountTaglib(WebAppType webApp, String taglibUri) {
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

    public static ServletType getServlet(WebAppType webApp, String servletName) {
        List<ServletType> servlets = getServlets(webApp);
        for (ServletType servlet : servlets) {
            if (servlet.getServletName().equals(servletName)) {
                return servlet;
            }
        }
        return null;
    }


}
