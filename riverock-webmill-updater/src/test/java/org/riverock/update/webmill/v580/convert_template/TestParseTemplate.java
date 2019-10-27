package org.riverock.update.webmill.v580.convert_template;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import org.riverock.update.webmill.v580.convert_template.schema.SiteTemplate;
import org.riverock.update.webmill.v580.convert_template.schema.Template;
import org.riverock.update.webmill.v580.convert_template.schema.Portlet;
import org.riverock.update.webmill.v580.convert_template.ConvertTemplate;
import org.riverock.dbrevision.utils.Utils;

import junit.framework.TestCase;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 15:49:20
 */
public class TestParseTemplate extends TestCase {

    public void testParseTemplate() throws Exception {
        String templateName = "/xml/template/template-dynamic-1.xml";
        Template template = parseTemplate(templateName, "dynamic-template.xml");
        assertNotNull(template);
        assertEquals(9, template.getDynamicOrPortletOrXslt().size());
    }

    public void testParseTemplateIndex_1() throws Exception {
        String templateName = "/xml/template/template-index-1.xml";
        Template template = parseTemplate(templateName, "index-template.xml");
        assertNotNull(template);
        assertEquals(9, template.getDynamicOrPortletOrXslt().size());
    }

    public void testParseTemplateWithParameter() throws Exception {
        String templateName = "/xml/template/template-with-parameter.xml";
        Template template = parseTemplate(templateName, "template-with-parameter.xml");
        assertNotNull(template);
        assertEquals(13, template.getDynamicOrPortletOrXslt().size());

        assertTrue(template.getDynamicOrPortletOrXslt().get(10) instanceof Portlet);
        assertEquals(2, ((Portlet)template.getDynamicOrPortletOrXslt().get(10)).getElementParameter().size());
/*
        <Parameter name="level" value="1"/>
        <Parameter name="type_level" value="equal"/>
*/
        assertEquals("level", ((Portlet)template.getDynamicOrPortletOrXslt().get(10)).getElementParameter().get(0).getName());
        assertEquals("1", ((Portlet)template.getDynamicOrPortletOrXslt().get(10)).getElementParameter().get(0).getValue());
        assertEquals("type_level", ((Portlet)template.getDynamicOrPortletOrXslt().get(10)).getElementParameter().get(1).getName());
        assertEquals("equal", ((Portlet)template.getDynamicOrPortletOrXslt().get(10)).getElementParameter().get(1).getValue());

        
        assertTrue(template.getDynamicOrPortletOrXslt().get(11) instanceof Portlet);
        assertEquals(2, ((Portlet)template.getDynamicOrPortletOrXslt().get(11)).getElementParameter().size());
/*
        <Parameter name="level" value="2"/>
        <Parameter name="type_level" value="great"/>
*/
        assertEquals("level", ((Portlet)template.getDynamicOrPortletOrXslt().get(11)).getElementParameter().get(0).getName());
        assertEquals("2", ((Portlet)template.getDynamicOrPortletOrXslt().get(11)).getElementParameter().get(0).getValue());
        assertEquals("type_level", ((Portlet)template.getDynamicOrPortletOrXslt().get(11)).getElementParameter().get(1).getName());
        assertEquals("great", ((Portlet)template.getDynamicOrPortletOrXslt().get(11)).getElementParameter().get(1).getValue());

    }

    private static Template parseTemplate(String templateName, String resultTemplateName) throws JAXBException, IOException {
        InputStream is = TestParseTemplate.class.getResourceAsStream(templateName);

        SiteTemplate siteTemplate = Utils.getObjectFromXml(SiteTemplate.class, is);

        Template template = ConvertTemplate.convertTemplate(siteTemplate);

        byte[] bytes = Utils.getXml(template, "Template", "utf-8", true,
            new NamespacePrefixMapper[] {ConvertTemplate.NAMESPACE_PREFIX_MAPPER}
        );
        FileOutputStream writer = new FileOutputStream(resultTemplateName);
        writer.write(bytes);
        writer.flush();
        writer.close();

        is = new ByteArrayInputStream(bytes);
        template = Utils.getObjectFromXml(Template.class, is);
        return template;
    }
}
