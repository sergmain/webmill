package org.riverock.webmill.container.definition.web_xml_abstract;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 19:14:31
 */
public class Servlet_2_3 implements org.riverock.webmill.container.definition.web_xml_abstract.Servlet {
    private org.riverock.webmill.container.definition.web_xml_v2_3.Servlet servlet;

    public Servlet_2_3(org.riverock.webmill.container.definition.web_xml_v2_3.Servlet servlet) {
        this.servlet = servlet;
    }

    public String getServletName() {
        return servlet.getServletName().getContent();
    }

    public Object getServlet() {
        return servlet;
    }
}