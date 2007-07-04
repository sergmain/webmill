package org.riverock.webmill.container.definition;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.namespace.QName;

import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;
import org.riverock.webmill.container.definition.web_xml_v2_4.ObjectFactory;
import org.riverock.webmill.container.portlet.PortletContainerException;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:14:43
 */
public class WebXmlDefinitionProcessorImpl implements WebXmlDefinitionProcessor {
    private static final String PACKAGE_NAME = ObjectFactory.class.getPackage().getName();

    public WebAppType process(File webXmlFile) {
        System.out.println("Start unmarshal portlet file: " + webXmlFile);
        try {
            return process(new FileInputStream(webXmlFile));
        }
        catch (FileNotFoundException e) {
            throw new PortletContainerException("Error process web.xml file");
        }
    }

    public WebAppType process(InputStream inputStream) {
        System.out.println("Start unmarshal portlet file from input stream.");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE_NAME);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Source source =  new StreamSource(inputStream);
            JAXBElement<WebAppType> jaxbElement = unmarshaller.unmarshal( source, WebAppType.class );

            return jaxbElement.getValue();
        }
        catch (JAXBException e) {
            throw new PortletContainerException("Error process web.xml file");
        }
    }

    public void marshall(WebAppType webApp, OutputStream outputStream, String rootElement) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance ( PACKAGE_NAME );
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");

            if (rootElement != null && rootElement.trim().length() > 0) {
                // http://weblogs.java.net/blog/kohsuke/archive/2005/10/101_ways_to_mar.html
                marshaller.marshal( new JAXBElement(new QName("", rootElement), WebAppType.class, webApp ), outputStream);
            }
            else {
                marshaller.marshal(webApp, outputStream);
            }
        }
        catch (JAXBException e) {
            throw new PortletContainerException("Error marshal web.xml file");
        }
        
    }
}
