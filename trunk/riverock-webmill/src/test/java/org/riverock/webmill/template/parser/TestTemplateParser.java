package org.riverock.webmill.template.parser;

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 20:52:14
 */
public class TestTemplateParser extends TestCase {

    public void testTemplateParsing_2() throws Exception {
        
        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_2.xml");
        ParsedTemplate template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(5, template.getElements().length);
        assertTrue(template.getElements()[1].isXslt());
        assertTrue(template.getElements()[3].isDynamic());
    }

    public void testTemplateParsing_3() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_3.xml");
        ParsedTemplate template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(3, template.getElements().length);
        assertTrue(template.getElements()[1].isXslt());
    }

    public void testTemplateParsing_4() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_4.xml");
        ParsedTemplate template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(7, template.getElements().length);
        assertTrue(template.getElements()[1].isXslt());
        assertTrue(template.getElements()[3].isDynamic());
        assertTrue(template.getElements()[5].isPortlet());
    }

    public void testTemplateParsing_5() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_5.xml");
        ParsedTemplate template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(3, template.getElements().length);
        assertTrue(template.getElements()[1].isPortlet());
    }
}
