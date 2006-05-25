package org.riverock.portlet.manager.site.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.MissingResourceException;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.SiteBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class SiteAction implements Serializable {
    private final static Logger log = Logger.getLogger( SiteAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public SiteAction() {
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // getter/setter methods
    public void setSiteSessionBean( SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectSite(ActionEvent event) {
        SiteAction.log.info( "Select site action." );
        loadCurrentSite();

//        siteSessionBean.resetStatus();
        return "site";
    }

// Add actions
    public String addSiteAction() {
        log.info( "Add site action." );

        SiteExtended siteExtended = new SiteExtended( new SiteBean(), new ArrayList<VirtualHost>(), null );
        siteSessionBean.setSiteExtended(siteExtended);

//        holdingSessionBean.setAdd( true );

        return "site-add";
    }

    public String processAddSiteAction() {
        log.info( "Procss add site action." );

        if (log.isDebugEnabled()) {
            log.debug("site: " + siteSessionBean.getSiteExtended());
            if (siteSessionBean.getSiteExtended()!=null) {
                log.debug("    language: " + siteSessionBean.getSiteExtended().getSite().getDefLanguage());
                log.debug("    country: " + siteSessionBean.getSiteExtended().getSite().getDefCountry());
                log.debug("    variant: " + siteSessionBean.getSiteExtended().getSite().getDefVariant());
                log.debug("    companyId: " + siteSessionBean.getSiteExtended().getSite().getCompanyId());
            }
        }

        if( siteSessionBean.getSiteExtended()!=null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteDao().createSite(siteSessionBean.getSiteExtended().getSite());
            siteSessionBean.setSiteExtended(null);
            dataProvider.clearSite();
        }

        return "site";
    }

    public String cancelAddSiteAction() {
        log.info( "Cancel add site action." );

        siteSessionBean.setSiteExtended(null);
        dataProvider.clearSite();

//        holdingSessionBean.resetStatus();

        return "site";
    }

// Edit actions
    public String editSiteAction() {
        log.info( "Edit site action." );

//        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
//        holdingSessionBean.setEdit( true );
         MissingResourceException
        return "site-edit";
    }

    public String processEditSiteAction() {
        log.info( "Save changes site action." );

        if( siteSessionBean.getSiteExtended()!=null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteDao().updateSite(siteSessionBean.getSiteExtended().getSite());
            siteSessionBean.setSiteExtended( null );
            dataProvider.clearSite();
        }

//        holdingSessionBean.resetStatus();

        return "site";
    }

    public String cancelEditSiteAction() {
        log.info( "Cancel edit site action." );

//	holdingSessionBean.resetStatus();
        return "site";
    }

// Delete actions
    public String deleteSiteAction() {
        log.info( "delete site action." );

        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );

//	holdingSessionBean.setDelete( true );
        return "site-delete";
    }

    public String cancelDeleteSiteAction() {
        log.info( "Cancel delete holding action." );

//	holdingSessionBean.resetStatus();
        return "site";
    }

    public String processDeleteSiteAction() {
        log.info( "Process delete site action." );
        if( siteSessionBean.getSiteExtended() != null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteDao().deleteSite(siteSessionBean.getSiteExtended().getSite().getSiteId());
            siteSessionBean.setSiteExtended( null );
            dataProvider.clearSite();
//            holdingDataProvider.reinitHoldingBeans();
        }

//	holdingSessionBean.resetStatus();

        return "site";
    }

    private void loadCurrentSite() {
        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
    }
}
