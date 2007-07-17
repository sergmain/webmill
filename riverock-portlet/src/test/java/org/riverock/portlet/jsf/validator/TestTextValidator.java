package org.riverock.portlet.jsf.validator;

import junit.framework.TestCase;

/**
 * User: SMaslyukov
 * Date: 17.07.2007
 * Time: 17:34:27
 */
public class TestTextValidator extends TestCase {

    public static final String XML_1 =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Aaaa>\n" +
            "\t\t<bbb ccc=\"ccc\"&ffff</bbb>\n" +
            "</Aaaa>";
    public static final String XML_2 =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?\n" +
            "<Aaaa>\n" +
            "\t\t<bbb ccc=\"ccc\">ffff</bbb>\n" +
            "</Aaaa>";

    public static final String XML_3 =
        "<?xml  version=\"1.0\"  encoding=\"UTF-8\" ?>\n" +
            "<!--  edited  with  XMLSPY  v5  U  (http://www.xmlspy.com)  by  Serg  (Millennium)  --> \n" +
            "<SiteTemplate  name=\"index\"> \n" +
            "\n" +
            "<SiteTemplateItem  type=\"custom\"  value=\"HeaderStart\"/> \n" +
            "<SiteTemplateItem  type=\"custom\"  value=\"HeaderTextIndex\"/> \n" +
            "<!--\n" +
            "<SiteTemplateItem  type=\"file\"  value=\"/t/_test.html\"/> \n" +
            "-->\n" +
            "<SiteTemplateItem  type=\"custom\"  value=\"HeaderEnd\"/> \n" +
            "\n" +
            "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\"/> \n" +
            "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu_member\"/> \n" +
            "<SiteTemplateItem  type=\"custom\"  value=\"Separator\"/> \n" +
            "<SiteTemplateItem  type=\"dynamic\"/> \n" +
            "<SiteTemplateItem  type=\"custom\"  value=\"Separator1\"/> \n" +
            "<SiteTemplateItem  type=\"custom\"  value=\"Footer\"/> \n" +
            "\n" +
            "</SiteTemplate> ";

    public void testXmlValidator() throws Exception {
        String s = TextValidator.validateAsXml(XML_1);
        assertNotNull(s);

        s = TextValidator.validateAsXml(XML_2);
        assertNotNull(s);

        s = TextValidator.validateAsXml(XML_3);
        assertNull(s);

        TextValidator v = new TextValidator("xml");
        TextValidator v1 = new TextValidator("xml");

        assertEquals(v, v);
        assertEquals(v1, v);
        assertEquals(v, v1);
        assertFalse( v.equals(null));
    }
}
