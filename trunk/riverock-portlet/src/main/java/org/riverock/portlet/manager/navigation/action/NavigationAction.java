package org.riverock.portlet.manager.navigation.action;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.navigation.NavigationDataProvider;
import org.riverock.portlet.manager.navigation.NavigationSessionBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 22:19:15
 * $Id$
 */
public class NavigationAction {
    private final static Logger log = Logger.getLogger(NavigationAction.class);

    private NavigationSessionBean navigationSessionBean;

    private AuthSessionBean authSessionBean;

    private NavigationDataProvider navigationDataProvider;
    private static final String NAV = "nav";

    public NavigationSessionBean getNavigationSessionBean() {
        return navigationSessionBean;
    }

    public void setNavigationSessionBean(NavigationSessionBean navigationSessionBean) {
        this.navigationSessionBean = navigationSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public NavigationDataProvider getNavigationDataProvider() {
        return navigationDataProvider;
    }

    public void setNavigationDataProvider(NavigationDataProvider navigationDataProvider) {
        this.navigationDataProvider = navigationDataProvider;
    }

    public String changeSite() {
        log.info( "Change site action." );

        return NAV;
    }

    public String applyTemplateChanges() {
        log.info( "Apply template changes." );

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();

        portalSpiProvider.getPortalTemplateDao().setDefaultDynamic(navigationSessionBean.getDynamicTemplateId());
        portalSpiProvider.getPortalTemplateDao().setMaximizedTemplate(navigationSessionBean.getMaximazedTemplateId());
        portalSpiProvider.getPortalTemplateDao().setPopupTemplate(navigationSessionBean.getPopupTemplateId());


        return NAV;
    }
}
