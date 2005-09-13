package org.riverock.module.web.request;

import org.riverock.webmill.container.tools.PortletService;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.web.user.WebmillModuleUserImpl;
import org.riverock.module.exception.ModuleException;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.exception.GenericException;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:44:52
 *         $Id$
 */
public class WebmillPortletModuleRequestImpl extends PortletModuleRequestImpl {
    static private final Log log = LogFactory.getLog(WebmillPortletModuleRequestImpl.class);

    public WebmillPortletModuleRequestImpl(PortletRequest portletRequest) {
        this.portletRequest = portletRequest;
    }

    public String getRemoteAddr() {
        return PortletService.getRemoteAddr( portletRequest );
    }

    public String getUserAgent() {
        return PortletService.getUserAgent( portletRequest );
    }

    public Long getServerNameId() throws ModuleException{
        String serverName = portletRequest.getServerName();
        try {
            return SiteListSite.getIdSite(serverName);
        }
        catch (GenericException e) {
            String es = "Error get siteId for serverName '"+serverName+"'";
            log.error(es, e);
            throw new ModuleException(es, e);
        }
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
