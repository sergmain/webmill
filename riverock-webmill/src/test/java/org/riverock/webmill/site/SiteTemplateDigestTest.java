/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.site;

import org.riverock.interfaces.portal.template.PortalTemplate;

/**
 * @author smaslyukov
 *         Date: 06.08.2005
 *         Time: 15:56:49
 *         $Id$
 */
public class SiteTemplateDigestTest {
    public static final String[] template = {
        "<?xml  version=\"1.0\"  encoding=\"UTF-8\"?>\n" +
        "<SiteTemplate role=\"test.role\">\n" +
        "\n" +
        "<SiteTemplateItem type=\"custom\"  value=\"HeaderStart\"/>\n" +
        "<SiteTemplateItem type=\"custom\"  value=\"HeaderText\"/>\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderEnd\"/>\n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\" code=\"TOP_MENU\" xmlRoot=\"TopMenu\" role=\"webmill.authentic\"/>\n" +
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
        "</SiteTemplate>",

        "<SiteTemplate> \n" +
        "\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderStart\"/> \n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderText\"/> \n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderEnd\"/> \n" +
        "\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"MainStart\"/> \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"mill.menu\"/> \n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"Separator\"/> \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetInvalidParameterTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetReaderInProcessActionTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetReaderIllegalStateAfterInputStreamTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetReaderIllegalStateWithApplFormPostDataTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetInputStreamInProcessActionTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetInputStreamIllegalStateAfterReaderTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetInputStreamIllegalStateWithApplFormPostDataTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetRemoteUserNullTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetRequestedSessionIdTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetResponseContentTypeTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetResponseContentTypesTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"CheckIsPortletModeAllowedTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"IsRequestedSessionIdValidForValidSessionTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"IsRequestedSessionIdValidForInvalidSessionTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"CheckIsWindowStateAllowedTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"RemoveAttributeTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"SetAttributeTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetParameterOnlySeenByTargetTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"GetParameterOnlySeenByTargetTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"CheckSingleParameterValuesTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"portlet\"  value=\"SetCharacterEncodingIllegalStateTestPortlet\">\n" +
        "<Parameter name=\"is-xml\" value=\"true\"/>\n" +
        "</SiteTemplateItem>  \n" +
        "\n" +
        "<SiteTemplateItem  type=\"custom\"  value=\"Footer\"/> \n" +
        "</SiteTemplate> "
    };

    public static void main(String[] args) throws Exception {

        for (String aTemplate : template) {
            PortalTemplate siteTemplate =
                PortalTemplateManagerImpl.digestSiteTemplate(aTemplate, "template", 1L);
            System.out.println("template:\n" + siteTemplate.toString());
        }
    }
}
