package org.riverock.webmill.portal.page_element;

import org.riverock.webmill.container.bean.SitePortletData;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 11:36:58
 */
public interface PageElement {
    void destroy();

    void processAction();

    void render();

    SitePortletData getData();

    boolean isXml();

    boolean isAction();

    boolean getIsRedirected();

    String getRedirectUrl();
}
