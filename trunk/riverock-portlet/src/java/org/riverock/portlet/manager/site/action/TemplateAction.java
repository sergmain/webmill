package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class TemplateAction implements Serializable {
    private final static Logger log = Logger.getLogger( TemplateAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public TemplateAction() {
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
    public String selectTemplate() {
        TemplateAction.log.info( "Select template action." );
        loadCurrentTemplate();

//        siteSessionBean.resetStatus();
        return "site";
    }

// Add actions
    public String addTemplateAction() {
        TemplateAction.log.info( "Add template action." );
/*
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );
*/
        return "css-add";
    }

    public String processAddTemplateAction() {
        TemplateAction.log.info( "Procss add template action." );
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

    public String cancelAddTemplateAction() {
        TemplateAction.log.info( "Cancel add template action." );
/*
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
*/
        return "site";
    }

// Edit actions
    public String editTemplateAction() {
        TemplateAction.log.info( "Edit holding action." );
/*
        holdingSessionBean.setEdit( true );
*/
        return "css-edit";
    }

    public String processEditTemplateAction() {
        TemplateAction.log.info( "Save changes template action." );
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

    public String cancelEditTemplateAction() {
        TemplateAction.log.info( "Cancel edit template action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

// Delete actions
    public String deleteTemplateAction() {
        TemplateAction.log.info( "delete template action." );

//	holdingSessionBean.setDelete( true );
    return "css-delete";
    }

    public String cancelDeleteTemplateAction() {
        TemplateAction.log.info( "Cancel delete holding action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

    public String processDeleteTemplateAction() {
        TemplateAction.log.info( "Process delete template action." );
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

    private void loadCurrentTemplate() {
    }
}
