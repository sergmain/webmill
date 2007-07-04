package org.riverock.webmill.container.definition;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:13:52
 */
public interface WebXmlDefinitionProcessor {

    WebAppType process( File webXmlFile );

    WebAppType process(InputStream inputStream);

    void marshall(WebAppType webApp, OutputStream outputStream, String rootElement);
}
