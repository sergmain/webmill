package org.riverock.webmill.container.definition;

import java.util.List;

import org.riverock.webmill.container.definition.web_xml_abstract.Servlet;
import org.riverock.webmill.container.definition.web_xml_v2_4.JspConfigType;
import org.riverock.webmill.container.definition.web_xml_v2_4.TaglibType;
import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;

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

    public static List<Servlet> getServlets(WebAppDefinition webApp) {
        if (webApp==null) {
            return null;
        }
        return webApp.getServlets();
    }

    public static Servlet getServlet(WebAppDefinition webApp, String servletName) {
        List<Servlet> servlets = getServlets(webApp);
        for (Servlet servlet : servlets) {
            if (servlet.getServletName().equals(servletName)) {
                return servlet;
            }
        }
        return null;
    }
}
