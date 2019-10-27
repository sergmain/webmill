package org.riverock.webmill.container.definition;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.xpath.XPathExpressionException;

import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:13:52
 */
public interface WebXmlDefinitionProcessor {

    WebAppDefinition process( File webXmlFile );

    WebAppDefinition process(InputStream inputStream);

    void marshall(WebAppDefinition webApp, OutputStream outputStream, String rootElement);

    WebAppVersion getWebAppVersion(byte[] bytes) throws Exception;
}
