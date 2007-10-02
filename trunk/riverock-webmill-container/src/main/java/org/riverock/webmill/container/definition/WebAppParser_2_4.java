package org.riverock.webmill.container.definition;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.riverock.webmill.container.definition.web_xml_v2_3.ObjectFactory;
import org.riverock.webmill.container.definition.web_xml_v2_3.WebApp;
import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 18:51:03
 */
public class WebAppParser_2_4 {

    public static WebAppDefinition parser(byte[] bytes) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.riverock.webmill.container.definition.web_xml_v2_4.ObjectFactory.class.getPackage().getName());
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Source source =  new StreamSource(new ByteArrayInputStream(bytes));
        JAXBElement<WebAppType> jaxbElement = unmarshaller.unmarshal( source, WebAppType.class );

        return new WebAppDefinition_2_4(jaxbElement.getValue());
    }
}