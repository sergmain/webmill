package org.riverock.webmill.portal.page_element;

import org.riverock.webmill.container.bean.SitePortletData;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 13:11:28
 */
public class XsltPageElement implements PageElement {
    private String name;

    public XsltPageElement(String name) {
        this.name = name;
    }

    public void destroy() {
        name=null;
    }

    public void processAction() {
    }

    public void render() {
    }

    public SitePortletData getData() {
        return PageElementUtils.setData(
            new StringBuilder("<").append( name ).append( "/>" ).toString().getBytes(),
            false, true
        );
    }

    public boolean isXml() {
        return true;
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
