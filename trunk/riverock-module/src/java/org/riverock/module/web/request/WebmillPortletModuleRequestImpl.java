package org.riverock.module.web.request;

import javax.portlet.PortletRequest;

import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.web.user.WebmillModuleUserImpl;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:44:52
 *         $Id$
 */
public class WebmillPortletModuleRequestImpl extends PortletModuleRequestImpl {

    public WebmillPortletModuleRequestImpl(PortletRequest portletRequest) {
        this.portletRequest = portletRequest;
    }

    public String getRemoteAddr() {
        return PortletService.getRemoteAddr( portletRequest );
    }

    public String getUserAgent() {
        return PortletService.getUserAgent( portletRequest );
    }

    public Long getSiteId() {
        return (Long)portletRequest.getAttribute( ContainerConstants.PORTAL_PROP_SITE_ID );
    }

    public Object getAttribute(String key) {
        return portletRequest.getAttribute( key );
    }

    public ModuleUser getUser() {
        if (portletRequest.getUserPrincipal()!=null) {
            return new WebmillModuleUserImpl( portletRequest.getUserPrincipal() );
        }
        else {
            return null;
        }
    }
}
