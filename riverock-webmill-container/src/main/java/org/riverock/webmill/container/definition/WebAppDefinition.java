package org.riverock.webmill.container.definition;

import java.util.List;

import org.riverock.webmill.container.definition.web_xml_abstract.Servlet;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 19:01:32
 */
public interface WebAppDefinition {
    Class getWebAppClass();

    String getWebAppPackage();

    List<Servlet> getServlets();

    int getCountTaglib(String taglibUri);

    void initWebmillRegisterServlet();

    void initWebmillRegisterServlet(Servlet abstractServlet);

    String prepareTaglib();

    Object getWebAppDefinition();
}
