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
public class SiteLanguageAction implements Serializable {
    private final static Logger log = Logger.getLogger( SiteLanguageAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;

    public SiteLanguageAction() {
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
    public String selectSiteLanguage() {
        SiteLanguageAction.log.info( "Select site language action." );
        loadCurrentSiteLanguage();

//        siteSessionBean.resetStatus();
        return "site";
    }

// Add actions
    public String addSiteLanguageAction() {
        SiteLanguageAction.log.info( "Add site language action." );
/*
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );
*/
        return "css-add";
    }

    public String processAddSiteLanguageAction() {
        SiteLanguageAction.log.info( "Procss add site language action." );
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

    public String cancelAddSiteLanguageAction() {
        SiteLanguageAction.log.info( "Cancel add site language action." );
/*
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
*/
        return "site";
    }

// Edit actions
    public String editSiteLanguageAction() {
        SiteLanguageAction.log.info( "Edit holding action." );
/*
        holdingSessionBean.setEdit( true );
*/
        return "css-edit";
    }

    public String processEditSiteLanguageAction() {
        SiteLanguageAction.log.info( "Save changes site language action." );
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

    public String cancelEditSiteLanguageAction() {
        SiteLanguageAction.log.info( "Cancel edit site language action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

// Delete actions
    public String deleteSiteLanguageAction() {
        SiteLanguageAction.log.info( "delete site language action." );

//	holdingSessionBean.setDelete( true );
    return "css-delete";
    }

    public String cancelDeleteSiteLanguageAction() {
        SiteLanguageAction.log.info( "Cancel delete holding action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

    public String processDeleteSiteLanguageAction() {
        SiteLanguageAction.log.info( "Process delete site language action." );
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

    private void loadCurrentSiteLanguage() {
    }
}
