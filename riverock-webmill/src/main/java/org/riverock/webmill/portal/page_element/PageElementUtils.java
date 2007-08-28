package org.riverock.webmill.portal.page_element;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalPageController;
import org.riverock.webmill.portal.preference.PreferenceFactory;
import org.riverock.webmill.portal.url.interpreter.PortletParameters;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.RequestState;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.template.parser.ParsedTemplateElement;
import org.riverock.webmill.template.schema.Portlet;
import org.riverock.webmill.template.TemplateUtils;
import org.riverock.webmill.exception.PortalException;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 13:13:29
 */
public class PageElementUtils {

    static SitePortletData setData(String data, boolean isError, boolean isXml) {
        return setData(data.getBytes(), isError, isXml);
    }

    static SitePortletData setData(byte[] bytes, boolean isError, boolean isXml) {
        SitePortletData data = new SitePortletData();

        data.setData( bytes );
        data.setIsError( isError );
        data.setIsXml( isXml );

        return data;
    }

}
