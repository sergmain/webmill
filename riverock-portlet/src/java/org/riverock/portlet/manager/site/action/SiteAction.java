package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.main.AuthSessionBean;

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

    public SiteAction() {
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
/*
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );
*/
        return "site-add";
    }

    public String processAddSiteAction() {
        SiteAction.log.info( "Procss add site action." );
/*
        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalDaoProvider().getPortalHoldingDao().processAddHolding(
			holdingSessionBean.getHoldingBean()
		);
            holdingSessionBean.setHoldingBean( null );

            holdingDataProvider.reinitHoldingBeans();
        }

	holdingSessionBean.resetStatus();
*/
    return "site";
    }

    public String cancelAddSiteAction() {
        SiteAction.log.info( "Cancel add site action." );
/*
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
*/
        return "site";
    }

// Edit actions
    public String editSiteAction() {
        SiteAction.log.info( "Edit site action." );
/*
        holdingSessionBean.setEdit( true );
*/
        return "css-edit";
    }

    public String processEditSiteAction() {
        SiteAction.log.info( "Save changes site action." );
/*
        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalDaoProvider().getPortalHoldingDao().processSaveHolding(
			holdingSessionBean.getHoldingBean()
		);
            holdingSessionBean.setHoldingBean( null );
            holdingDataProvider.reinitHoldingBeans();
        }

	holdingSessionBean.resetStatus();
*/
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
    return "css-delete";
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
