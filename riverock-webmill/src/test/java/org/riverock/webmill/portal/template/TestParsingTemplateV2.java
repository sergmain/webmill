package org.riverock.webmill.portal.template;

import java.io.InputStream;
import java.lang.Object;
import java.util.List;

import junit.framework.TestCase;

import org.riverock.common.tools.XmlTools;
import org.riverock.webmill.template.schema.*;
import org.riverock.webmill.portal.template.parser.TemplateParserUtils;
import org.riverock.webmill.utils.SingletonFactory;
import org.riverock.webmill.portal.template.TemplateUtils;

/**
 * User: SMaslyukov
 * Date: 24.07.2007
 * Time: 19:38:35
 */
public class TestParsingTemplateV2 extends TestCase {

    public void testNullOperation() {
        List<Object> elements = TemplateParserUtils.getElements(null);
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    public void testConvertNullToTemplate() throws Exception {
        Template t = TemplateUtils.convertTemplate(null);
        assertNotNull(t);
    }

    public void testConvertTemplate() throws Exception {

        InputStream is;
        SiteTemplate siteTemplate;

        is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_1.xml");
        siteTemplate = XmlTools.getObjectFromXml(SiteTemplate.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(siteTemplate);

        Template t = TemplateUtils.convertTemplate(siteTemplate);
        assertNotNull(t);

        assertEquals(siteTemplate.getSiteTemplateItem().size(), t.getPortletOrDynamicOrXslt().size());

/*
        <SiteTemplateItem type="custom" value="HeaderStart"/>
        <SiteTemplateItem type="custom" value="HeaderText"/>
        <SiteTemplateItem type="custom" value="HeaderEnd"/>
        <SiteTemplateItem type="portlet" value="mill.menu" code="TOP_MENU" xmlRoot="TopMenu" role="webmill.authentic"/>
        <SiteTemplateItem type="portlet" value="mill.login_xml" xmlRoot="LoginOnIndexPage"/>
        <SiteTemplateItem type="custom" value="MainStart"/>
        <SiteTemplateItem type="portlet" value="mill.menu"/>
        <SiteTemplateItem type="portlet" value="mill.menu_member"/>
        <SiteTemplateItem type="custom" value="Separator"/>
        <SiteTemplateItem type="portlet" value="mill.news_block"/>     <-- 9
        <SiteTemplateItem type="portlet" value="mill.menu" code="DEFAULT_ru_RU">
            <Parameter name="level" value="1"/>
            <Parameter name="type_level" value="equal"/>
        </SiteTemplateItem>
        <SiteTemplateItem type="portlet" value="mill.menu" code="DEFAULT_ru_RU">
            <Parameter name="level" value="1"/>
            <Parameter name="type_level" value="great"/>
        </SiteTemplateItem>
        <SiteTemplateItem type="custom" value="Footer"/>
        <SiteTemplateItem type="dynamic"/>
*/
        assertEquals("custom", siteTemplate.getSiteTemplateItem().get(0).getType());
        assertEquals("portlet", siteTemplate.getSiteTemplateItem().get(3).getType());
        assertEquals("portlet", siteTemplate.getSiteTemplateItem().get(9).getType());
        assertEquals("dynamic", siteTemplate.getSiteTemplateItem().get(13).getType());
        SiteTemplateItem item = siteTemplate.getSiteTemplateItem().get(11);
        assertEquals("portlet", item.getType());
        assertEquals("mill.menu", item.getValue());
        assertEquals("DEFAULT_ru_RU", item.getCode());
        assertEquals(2, item.getParameter().size());
        Parameter p;
        p = item.getParameter().get(0);
        assertEquals("level", p.getName());
        assertEquals("1", p.getValue());

        p = item.getParameter().get(1);
        assertEquals("type_level", p.getName());
        assertEquals("great", p.getValue());


        assertEquals(Xslt.class, t.getPortletOrDynamicOrXslt().get(0).getClass());
        assertEquals(Portlet.class, t.getPortletOrDynamicOrXslt().get(3).getClass());
        assertEquals(Portlet.class, t.getPortletOrDynamicOrXslt().get(9).getClass());
        assertEquals(Dynamic.class, t.getPortletOrDynamicOrXslt().get(13).getClass());
        Object o = t.getPortletOrDynamicOrXslt().get(11);
        assertEquals(Portlet.class, o.getClass());
        Portlet portlet = (Portlet)o;

        assertEquals("mill.menu", portlet.getName());
        assertEquals("DEFAULT_ru_RU", portlet.getCode());
        assertEquals(2, portlet.getElementParameter().size());

        ElementParameter ep;
        ep = portlet.getElementParameter().get(0);
        assertEquals("level", ep.getName());
        assertEquals("1", ep.getValue());

        ep = portlet.getElementParameter().get(1);
        assertEquals("type_level", ep.getName());
        assertEquals("great", ep.getValue());
    }


    public void testUnmarshalTemplate_v2_1() throws Exception {
        InputStream is;
        SiteTemplate siteTemplate;

        is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_1.xml");
        siteTemplate = XmlTools.getObjectFromXml(SiteTemplate.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(siteTemplate);
    }

    public void testUnmarshalTemplate_v2_2() throws Exception {
        InputStream is;

        is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_2.xml");
        Template siteTemplate = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(siteTemplate);
    }

    public void testUnmarshalTemplate_v2_2_2() throws Exception {

        InputStream is;
        Template template;
        is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_2.xml");
        template = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(template);
    }

    public void testUnmarshalTemplate_v2_3() throws Exception {
        InputStream is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_3.xml");
        Template template = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(template);
    }

    public void testUnmarshalTemplate_v2_4() throws Exception {

        InputStream is;
        is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_4.xml");
        Template template = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(template);

        Html html = template.getHtml();
        assertNotNull(html);

/*
        <?xml version="1.0" encoding="UTF-8"?>
        <Template xmlns:element="http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="http://webmill.riverock.org/xsd/riverock-template.xsd">
        <html>
            <head>
                <title>aaaa</title>
            </head>
            <body>
                <element:custom value="Aaa"/>
                <element:dynamic/>
                <element:portlet value="webmill:search" xmlRoot="SearchXml" code="SEARCH_CODE"/>
            </body>
        </html>
        </Template>
*/

        assertNotNull(html.getHead());
        assertNotNull(html.getHead().getContent());
        assertEquals(1, html.getHead().getContent().size());
        assertTrue(html.getHead().getContent().get(0) instanceof Title);

        assertEquals("aaaa", ((Title)html.getHead().getContent().get(0)).getContent());

        assertNotNull(html.getBody());
        assertNotNull(html.getBody().getPOrH1OrH2());
        assertEquals(3, html.getBody().getPOrH1OrH2().size());

        assertTrue(html.getBody().getPOrH1OrH2().get(0) instanceof Xslt);
        assertEquals("Aaa", ((Xslt)html.getBody().getPOrH1OrH2().get(0)).getName());

        assertTrue(html.getBody().getPOrH1OrH2().get(1) instanceof Dynamic);

        assertTrue(html.getBody().getPOrH1OrH2().get(2) instanceof Portlet);

        Portlet portlet = (Portlet)html.getBody().getPOrH1OrH2().get(2);
        assertEquals("webmill:search", portlet.getName());
        assertEquals("SearchXml", portlet.getXmlRoot());
        assertEquals("SEARCH_CODE", portlet.getCode());

        List<Object> elements = TemplateParserUtils.getElements(template);
        assertEquals(3, elements.size());

        assertTrue(elements.get(0) instanceof Xslt);
        assertTrue(elements.get(1) instanceof Dynamic);
        assertTrue(elements.get(2) instanceof Portlet);
    }

    public void testUnmarshalTemplate_v2_5() throws Exception {
        InputStream is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_5.xml");
        Template template = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(template);
        assertEquals(9, template.getPortletOrDynamicOrXslt().size());
    }

    public void testUnmarshalTemplate_v2_6() throws Exception {
        InputStream is = TestParsingTemplateV2.class.getResourceAsStream("/xml/resources/template/template_v2_6.xml");
        Template template = XmlTools.getObjectFromXml(Template.class, is, SingletonFactory.getValidationEventHandler());
        assertNotNull(template);
//        assertEquals(9, template.getPortletOrDynamicOrXslt().size());
    }


}
