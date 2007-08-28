package org.riverock.webmill.template.parser;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import junit.framework.TestCase;

import org.riverock.webmill.template.schema.Template;
import org.riverock.webmill.utils.SingletonFactory;
import org.riverock.common.tools.XmlTools;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 20:52:14
 */
public class TestTemplateParser extends TestCase {

    public void testTemplateParsing_2_thread() throws Exception {
        
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/resources/template/template_v2_2.xml");
        ParsedTemplateElement[] template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(5, template.length);
        assertTrue(template[1].isXslt());
        assertTrue(template[3].isDynamic());
    }

    public void testTemplateParsing_2() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_2.xml");
        ParsedTemplateElement[] template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(5, template.length);
        assertTrue(template[1].isXslt());
        assertTrue(template[3].isDynamic());
    }

    public void testTemplateParsing_3() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_3.xml");
        ParsedTemplateElement[] template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(3, template.length);
        assertTrue(template[1].isXslt());
    }

    public void testTemplateParsing_4() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_4.xml");
        ParsedTemplateElement[] template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(7, template.length);
        assertTrue(template[1].isXslt());
        assertTrue(template[3].isDynamic());
        assertTrue(template[5].isPortlet());
    }

    public void testTemplateParsing_5() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_5.xml");
        ParsedTemplateElement[] template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(19, template.length);
        assertTrue(template[1].isXslt());
    }

    public void testTemplateCountElements_7() throws Exception {
        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_7.xml");
        Template t = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(t);
        List<Object> list = TemplateParserUtils.getElements(t);
        assertNotNull(list);
        assertEquals(12, list.size());
    }

    public void testTemplateParsing_8() throws Exception {

        InputStream is = TestTemplateParser.class.getResourceAsStream("/xml/resources/template/template_v2_8.xml");
        ParsedTemplateElement[] template = TemplateParserFactory.getTemplateParser().parse(is);
        assertNotNull(template);
        assertEquals(11, template.length);
/*
        <element:xslt name="HeaderEnd"/>
        <element:portlet name="mill.menu" code="MAIN_MENU">
            <element:parameter value="0" name="level"/>
            <element:parameter value="great" name="type_level"/>
        </element:portlet>
        <element:xslt name="Separator"/>
        <element:portlet xmlRoot="TopMenu" name="mill.menu" code="MAIN_MENU">
            <element:parameter value="0" name="level"/>
            <element:parameter value="equal" name="type_level"/>
        </element:portlet>
        <element:xslt name="SeparatorDynStart"/>
*/

        assertTrue(template[0].isString());
        assertTrue(StringUtils.isBlank(template[0].getString()));
        assertTrue(template[1].isXslt());
        assertTrue(StringUtils.isBlank(template[2].getString()));
        assertTrue(template[3].isPortlet());
        assertTrue(StringUtils.isBlank(template[4].getString()));
        assertTrue(template[5].isXslt());
        assertTrue(StringUtils.isBlank(template[6].getString()));
        assertTrue(template[7].isPortlet());
        assertTrue(StringUtils.isBlank(template[8].getString()));
        assertTrue(template[9].isXslt());
        assertTrue(StringUtils.isBlank(template[10].getString()));
        assertNotNull(template[3].getPortlet().getElementParameter());
        assertEquals(2, template[3].getPortlet().getElementParameter().size());
    }
}
