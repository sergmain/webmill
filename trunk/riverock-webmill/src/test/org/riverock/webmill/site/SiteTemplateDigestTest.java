package org.riverock.webmill.site;

import org.riverock.webmill.container.schema.site.SiteTemplate;

/**
 * @author smaslyukov
 *         Date: 06.08.2005
 *         Time: 15:56:49
 *         $Id$
 */
public class SiteTemplateDigestTest {
    public static final String template =
        "<?xml  version=\"1.0\"  encoding=\"UTF-8\"?>\n" +
        "<SiteTemplate role=\"test.role\">\n" +
        "\n" +
        "<SiteTemplateItem type=\"custom\"  value=\"HeaderStart\"/>\n" +
        "<SiteTemplateItem type=\"custom\"  value=\"HeaderText\"/>\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderEnd\"/>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\" code=\"TOP_MENU\" xmlRoot=\"TopMenu\"/>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.login_xml\" xmlRoot=\"LoginOnIndexPage\"/>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"MainStart\"/>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\"/>\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu_member\"/>\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"Separator\"/>\n" +
        "<SiteTemplateItem    type=\"portlet\"    value=\"mill.news_block\"/>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\" code=\"DEFAULT_ru_RU\">\n" +
        "<Parameter name=\"level\" value=\"1\"/>\n" +
        "<Parameter name=\"type_level\" value=\"equal\"/>\n" +
        "</SiteTemplateItem>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\" code=\"DEFAULT_ru_RU\">\n" +
        "<Parameter name=\"level\" value=\"1\"/>\n" +
        "<Parameter name=\"type_level\" value=\"great\"/>\n" +
        "</SiteTemplateItem>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"Footer\"/>\n" +
        "</SiteTemplate>";

    public static void main(String[] args) throws Exception {
        SiteTemplate siteTemplate = SiteTemplateList.digestSiteTemplate( template );

        System.out.println("template:\n" + siteTemplate.toString() );
    }


}
