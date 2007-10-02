package org.riverock.webmill.container.deployer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.riverock.webmill.container.definition.DefinitionProcessorFactory;
import org.riverock.webmill.container.definition.WebXmlDefinitionProcessor;
import org.riverock.webmill.container.definition.WebAppDefinition;
import org.riverock.webmill.container.definition.web_xml_v2_4.WebAppType;

/**
 * @author smaslyukov
 *         Date: 08.08.2005
 *         Time: 19:01:23
 *         $Id: WebXmlXPathTest.java 1111 2006-11-30 00:18:47Z serg_main $
 */
public class WebXmlXPathTest extends TestCase {

    public void testWebXml() throws Exception {

        String[] files = {
            "/xml/web-app/v2_4/web.xml"
        };

        for (String fileName : files) {
            System.out.println("fileName = " + fileName);

            InputStream inputStream = WebXmlXPathTest.class.getResourceAsStream(fileName);
            WebXmlDefinitionProcessor processor = DefinitionProcessorFactory.getWebXmlDefinitionProcessor();
            WebAppDefinition webApp = processor.process(inputStream);
            assertNotNull(webApp);
        }
    }

    public void testXPath() throws Exception {

        String[] files = {
            "/xml/web-app/v2_4/web.xml"
        };

        for (String fileName : files) {
            System.out.println("fileName = " + fileName);

            InputStream inputStream = WebXmlXPathTest.class.getResourceAsStream(fileName);

            validateWebXmlAfterPatch(inputStream);
        }
    }

    private void validateWebXmlAfterPatch(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Document document = getDocument(inputStream);

        Node registerServlet = XPathAPI.selectSingleNode(document, WebmillWebApplicationRewriter.WEBMILL_SERVLET_XPATH);
        assertNotNull(registerServlet);
        XObject portletTagCount = XPathAPI.eval(document, WebmillWebApplicationRewriter.PORTLET_TAGLIB_COUNT_XPATH);
        int i=0;
        int countTaglibUril = new Double(portletTagCount.toString()).intValue();
        assertEquals(countTaglibUril, 1);

        Node portletTaglib = XPathAPI.selectSingleNode(document, WebmillWebApplicationRewriter.PORTLET_TAGLIB_XPATH);
        Element common = document.createElement("Aaaa");
        portletTaglib.appendChild(common);

        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty("encoding", "utf-8");
        DOMSource source = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);
        transformer.transform(source, result);
        System.out.println(" = " + stringWriter.toString());
    }

    private static Document getDocument(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        return docBuilder.parse( inputStream );
    }
}
