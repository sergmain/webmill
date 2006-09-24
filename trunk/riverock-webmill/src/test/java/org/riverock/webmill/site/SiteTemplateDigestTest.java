/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
