package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;

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
    public String selectSite() {
        SiteAction.log.info( "Select site action." );
        loadCurrentSite();

//        siteSessionBean.resetStatus();
        return "site";
    }

// Add actions
    public String addSiteAction() {
        SiteAction.log.info( "Add site action." );

        SiteExtended bean = new SiteExtended();
        siteSessionBean.setSiteExtended( bean );

//        holdingSessionBean.setAdd( true );

        return "site-add";
    }

    public String processAddSiteAction() {
        SiteAction.log.info( "Procss add site action." );

        if( siteSessionBean.getSiteExtended()!=null ) {
//            FacesTools.getPortalDaoProvider().getPortalSiteDao().processAddHolding(siteSessionBean.getSiteExtended());
            siteSessionBean.setSiteExtended(null);

            dataProvider.clearSite();
        }

//    holdingSessionBean.resetStatus();

        return "site";
    }

    public String cancelAddSiteAction() {
        SiteAction.log.info( "Cancel add site action." );

        siteSessionBean.setSiteExtended(null);

//        holdingSessionBean.resetStatus();

        return "site";
    }

// Edit actions
    public String editSiteAction() {
        SiteAction.log.info( "Edit site action." );

        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
//        holdingSessionBean.setEdit( true );

        return "site-edit";
    }

    public String processEditSiteAction() {
        SiteAction.log.info( "Save changes site action." );

        if( siteSessionBean.getSiteExtended()!=null ) {
//            FacesTools.getPortalDaoProvider().getPortalHoldingDao().processSaveHolding(siteSessionBean.getSiteExtended());
            siteSessionBean.setSiteExtended( null );
            dataProvider.clearSite();
        }

//        holdingSessionBean.resetStatus();

        return "site";
    }

    public String cancelEditSiteAction() {
        SiteAction.log.info( "Cancel edit site action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

// Delete actions
    public String deleteSiteAction() {
        SiteAction.log.info( "delete site action." );

//	holdingSessionBean.setDelete( true );
    return "site-delete";
    }

    public String cancelDeleteSiteAction() {
        SiteAction.log.info( "Cancel delete holding action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

    public String processDeleteSiteAction() {
        SiteAction.log.info( "Process delete site action." );
/*
        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalDaoProvider().getPortalHoldingDao().processDeleteHolding(
			holdingSessionBean.getHoldingBean()
		);

            holdingSessionBean.setHoldingBean( null );
            holdingDataProvider.reinitHoldingBeans();
        }

	holdingSessionBean.resetStatus();
*/
    return "site";
    }

    private void loadCurrentSite() {
    }
}
