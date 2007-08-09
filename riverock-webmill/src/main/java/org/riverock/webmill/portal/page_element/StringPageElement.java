package org.riverock.webmill.portal.page_element;

import org.riverock.webmill.container.bean.SitePortletData;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 17:38:17
 */
public class StringPageElement implements PageElement {
    private String value;

    public StringPageElement(String value) {
        this.value=value;
    }

    public void destroy() {
        value=null;
    }

    public void processAction() {
    }

    public void render() {
    }

    public SitePortletData getData() {
        return PageElementUtils.setData(value, false, false);
    }

    public boolean isXml() {
        return false;
    }

    public boolean isAction() {
        return false;
    }

    public boolean getIsRedirected() {
        return false;
    }

    public String getRedirectUrl() {
        return null;  
    }
}
