package org.riverock.webmill.portal.page_element;

import org.riverock.webmill.container.bean.SitePortletData;

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
