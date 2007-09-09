package org.riverock.portlet.manager.navigation.action;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.navigation.NavigationConstants;
import org.riverock.portlet.manager.navigation.NavigationDataProvider;
import org.riverock.portlet.manager.navigation.NavigationSessionBean;
import org.riverock.portlet.manager.navigation.bean.PortletAliasBean;
import org.riverock.portlet.manager.navigation.bean.UrlAliasBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 22:19:15
 * $Id$
 */
public class NavigationAction implements Serializable {
    private final static Logger log = Logger.getLogger(NavigationAction.class);

    private NavigationSessionBean navigationSessionBean;

    private AuthSessionBean authSessionBean;

    private NavigationDataProvider navigationDataProvider;

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
        log.info( "Start changeSite()" );

        navigationSessionBean.setCurrentPortletAliasId(null);
        navigationSessionBean.setCurrentSiteLanguageId(null);
        navigationSessionBean.setCurrentUrlAliasId(null);
        navigationSessionBean.setDynamicTemplateId(null);
        navigationSessionBean.setMaximazedTemplateId(null);
        navigationSessionBean.setPortletAlias(null);
        navigationSessionBean.setUrlAlias(null);
        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        
        return NavigationConstants.NAV;
    }

    // Template section
    
    public String applyTemplateChanges() {
        log.info( "Start applyTemplateChanges()" );

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();

        portalSpiProvider.getPortalTemplateDao().setDefaultDynamic(navigationSessionBean.getDynamicTemplateId());
        portalSpiProvider.getPortalTemplateDao().setMaximizedTemplate(navigationSessionBean.getMaximazedTemplateId());
        portalSpiProvider.getPortalTemplateDao().setPopupTemplate(navigationSessionBean.getPopupTemplateId());


        return NavigationConstants.NAV;
    }


    // Portlet alias section

    public String toViewPortletAlias() {
        log.info( "Start toViewPortletAlias()" );
        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String loadPortletAlias() {
        log.info( "Start loadPortletAlias()" );

        loadPortetAliasObject();

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String addPortletAlias() {
        log.info( "Start addPortletAliasAction()" );

        PortletAliasBean portletAlias = new PortletAliasBean();
        portletAlias.setSiteId(navigationSessionBean.getCurrentSiteId());

        navigationSessionBean.setPortletAlias(portletAlias);
        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.ADD);
        return NavigationConstants.NAV;
    }

    public String cancelAddPortletAlias() {
        log.info( "Start addPortletAliasAction()" );
        
        navigationSessionBean.setCurrentPortletAliasId(null);
        navigationSessionBean.setPortletAlias(null);
        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String processAddPortletAlias() {
        log.info( "Start addPortletAliasAction()" );

        PortletAliasBean portletAlias = navigationSessionBean.getPortletAlias();
        if (StringUtils.isBlank(portletAlias.getShortUrl())) {
            return NavigationConstants.NAV;
        }

        prepareShortUrl(portletAlias);

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        Long id = portalSpiProvider.getPortalAliasSpi().createPortletAlias(portletAlias);
        navigationSessionBean.setCurrentPortletAliasId(id);
        loadPortetAliasObject();

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    private static void prepareShortUrl(PortletAliasBean portletAlias) {
        portletAlias.setShortUrl( prepareUrl(portletAlias.getShortUrl()) );
    }

    private static String prepareUrl(String url) {
        String s = url.trim();
        if (!s.startsWith("/")) {
            return "/"+s;
        }
        return s;
    }

    public String editPortletAlias() {
        log.info( "Start editPortletAlias()" );

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.EDIT);
        return NavigationConstants.NAV;
    }

    public String cancelEditPortletAlias() {
        log.info( "Start addPortletAliasAction()" );

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String processEditPortletAlias() {
        log.info( "Start processEditPortletAlias()" );

        PortletAliasBean portletAlias = navigationSessionBean.getPortletAlias();
        if (StringUtils.isBlank(portletAlias.getShortUrl())) {
            return NavigationConstants.NAV;
        }

        prepareShortUrl(portletAlias);

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        portalSpiProvider.getPortalAliasSpi().updatePortletAlias(portletAlias);

        loadPortetAliasObject();

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String deletePortletAlias() {
        log.info( "Start deletePortletAlias()" );

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.DELETE);
        return NavigationConstants.NAV;
    }

    public String cancelDeletePortletAlias() {
        log.info( "Start addPortletAliasAction()" );

        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String processDeletePortletAlias() {
        log.info( "Start processDeletePortletAlias()" );

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        portalSpiProvider.getPortalAliasSpi().deletePortletAlias(navigationSessionBean.getPortletAlias());

        navigationSessionBean.setCurrentPortletAliasId(null);
        navigationSessionBean.setPortletAlias(null);
        navigationSessionBean.setPortletAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    private void loadPortetAliasObject() {
        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        PortletAlias alias = portalSpiProvider.getPortalAliasSpi().getPortletAlias(navigationSessionBean.getCurrentPortletAliasId());
        PortletName portletName = portalSpiProvider.getPortalPortletNameDao().getPortletName(alias.getPortletNameId());
        Template template = portalSpiProvider.getPortalTemplateDao().getTemplate(alias.getTemplateId());

        navigationSessionBean.setPortletAlias(new PortletAliasBean(alias, portletName.getPortletName(), template.getTemplateName()) );
    }

    // URL alias section

    public String toViewUrlAlias() {
        log.info( "Start toViewUrlAlias()" );
        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String loadUrlAlias() {
        log.info( "Start loadUrlAlias()" );

        loadUrlAliasObject();

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String addUrlAlias() {
        log.info( "Start addUrlAliasAction()" );

        UrlAliasBean urlAlias = new UrlAliasBean();
        urlAlias.setSiteId(navigationSessionBean.getCurrentSiteId());

        navigationSessionBean.setUrlAlias(urlAlias);
        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.ADD);
        return NavigationConstants.NAV;
    }

    public String cancelAddUrlAlias() {
        log.info( "Start addUrlAliasAction()" );

        navigationSessionBean.setCurrentUrlAliasId(null);
        navigationSessionBean.setUrlAlias(null);
        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String processAddUrlAlias() {
        log.info( "Start addUrlAliasAction()" );

        UrlAliasBean urlAlias = navigationSessionBean.getUrlAlias();
        if (StringUtils.isBlank(urlAlias.getUrl())) {
            return NavigationConstants.NAV;
        }

        if (StringUtils.isBlank(urlAlias.getAlias())) {
            return NavigationConstants.NAV;
        }

        urlAlias.setUrl(prepareUrl(urlAlias.getUrl()));
        urlAlias.setAlias(prepareUrl(urlAlias.getAlias()));

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        Long id = portalSpiProvider.getPortalAliasSpi().createUrlAlias(urlAlias);
        navigationSessionBean.setCurrentUrlAliasId(id);
        loadUrlAliasObject();

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String editUrlAlias() {
        log.info( "Start editUrlAlias()" );

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.EDIT);
        return NavigationConstants.NAV;
    }

    public String cancelEditUrlAlias() {
        log.info( "Start addUrlAliasAction()" );

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String processEditUrlAlias() {
        log.info( "Start processEditUrlAlias()" );

        UrlAlias urlAlias = navigationSessionBean.getUrlAlias();
        if (StringUtils.isBlank(urlAlias.getUrl())) {
            return NavigationConstants.NAV;
        }
        if (StringUtils.isBlank(urlAlias.getAlias())) {
            return NavigationConstants.NAV;
        }
        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        portalSpiProvider.getPortalAliasSpi().updateUrlAlias(urlAlias);

        loadUrlAliasObject();

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String deleteUrlAlias() {
        log.info( "Start deleteUrlAlias()" );

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.DELETE);
        return NavigationConstants.NAV;
    }

    public String cancelDeleteUrlAlias() {
        log.info( "Start addUrlAliasAction()" );

        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    public String processDeleteUrlAlias() {
        log.info( "Start processDeleteUrlAlias()" );

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        portalSpiProvider.getPortalAliasSpi().deleteUrlAlias(navigationSessionBean.getUrlAlias());

        navigationSessionBean.setCurrentUrlAliasId(null);
        navigationSessionBean.setUrlAlias(null);
        navigationSessionBean.setUrlAliasState(NavigationConstants.NavigationState.VIEW);
        return NavigationConstants.NAV;
    }

    private void loadUrlAliasObject() {
        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        UrlAlias alias = portalSpiProvider.getPortalAliasSpi().getUrlAlias(navigationSessionBean.getCurrentUrlAliasId());
        navigationSessionBean.setUrlAlias(new UrlAliasBean(alias) );
    }


}
