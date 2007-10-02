package org.riverock.webmill.container.definition.web_xml_abstract;

import org.riverock.webmill.container.definition.web_xml_v2_4.ServletType;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 19:14:31
 */
public class Servlet_2_4 implements Servlet {
    private ServletType servlet;

    public Servlet_2_4(ServletType servlet) {
        this.servlet = servlet;
    }

    public String getServletName() {
        return servlet.getServletName().getValue();
    }

    public Object getServlet() {
        return servlet;
    }
}
