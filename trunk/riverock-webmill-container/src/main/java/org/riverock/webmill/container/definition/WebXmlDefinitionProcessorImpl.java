package org.riverock.webmill.container.definition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xpath.XPathAPI;

import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.riverock.webmill.container.portlet.PortletContainerException;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:14:43
 */
public class WebXmlDefinitionProcessorImpl implements WebXmlDefinitionProcessor {
    private static final String SUN_MICROSYSTEMS_INC_DTD_WEB_APPLICATION_2_3_EN = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";

    public WebAppDefinition process(File webXmlFile) {
        System.out.println("Start unmarshal portlet file: " + webXmlFile);
        try {
            return process(new FileInputStream(webXmlFile));
        }
        catch (FileNotFoundException e) {
            throw new PortletContainerException("Error process web.xml file");
        }
    }

    public WebAppDefinition process(InputStream inputStream) {
        System.out.println("Start unmarshal portlet file from input stream.");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStreamEx();
            int count;
            byte[] bytes = new byte[0x200];
            while((count=inputStream.read(bytes))!=-1) {
                outputStream.write(bytes, 0, count);
            }
            bytes = outputStream.toByteArray();

            WebAppVersion webAppVersion = getWebAppVersion(bytes);
            switch (webAppVersion) {
                case WEB_APP_2_3:
                    return parseAsWebApp_2_3(bytes);
                case WEB_APP_2_4:
                    return parseAsWebApp_2_4(bytes);
                default:
                    throw new PortletContainerException("Not supported version of web.xml");
            }
        }
        catch (Exception e) {
            throw new PortletContainerException("Error process web.xml file", e);
        }
    }

    public WebAppVersion getWebAppVersion(byte[] bytes) throws Exception {

        // check for v2.3
        {
            String xml = new String(bytes);
            if (xml.indexOf(SUN_MICROSYSTEMS_INC_DTD_WEB_APPLICATION_2_3_EN)!=-1) {
                return WebAppVersion.WEB_APP_2_3;
            }
        }

        // check for v2.3
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document document = docBuilder.parse( new ByteArrayInputStream(bytes) );

            String xpath = "/web-app/@version";
            Node node = XPathAPI.selectSingleNode( document, xpath );

            if (node!=null && node.getNodeValue().equals("2.4")) {
                return WebAppVersion.WEB_APP_2_4;
            }
        }

        return WebAppVersion.UNKNOWN;
    }

    WebAppDefinition parseAsWebApp_2_3(byte[]  bytes) throws JAXBException {
        return WebAppParser_2_3.parser(bytes);
    }

    WebAppDefinition parseAsWebApp_2_4(byte[]  bytes) throws JAXBException {
        return WebAppParser_2_4.parser(bytes);
    }

    public void marshall(WebAppDefinition webApp, OutputStream outputStream, String rootElement) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance ( webApp.getWebAppPackage() );
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (rootElement != null && rootElement.trim().length() > 0) {
                // http://weblogs.java.net/blog/kohsuke/archive/2005/10/101_ways_to_mar.html
                marshaller.marshal( new JAXBElement(new QName("", rootElement), webApp.getWebAppClass(), webApp.getWebAppDefinition() ), outputStream);
            }
            else {
                marshaller.marshal(webApp.getWebAppDefinition(), outputStream);
            }
        }
        catch (JAXBException e) {
            throw new PortletContainerException("Error marshal web.xml file", e);
        }
        
    }
}
