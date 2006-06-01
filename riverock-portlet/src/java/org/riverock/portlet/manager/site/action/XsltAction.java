package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

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
public class XsltAction implements Serializable {
    private final static Logger log = Logger.getLogger( XsltAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public XsltAction() {
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
    public String selectXslt(ActionEvent event) {
        XsltAction.log.info( "Select xslt action." );
        loadCurrentXslt();

//        siteSessionBean.resetStatus();
        return "site";
    }

    public void selectSiteAction(){
        XsltAction.log.info( "select site action." );
    }

    public void selectSiteLanguageAction(){
        XsltAction.log.info( "select site language action." );
    }

    public void selectTemplateAction(){
        XsltAction.log.info( "select template action." );
    }

// Add actions
    public String addXsltAction() {
        XsltAction.log.info( "Add xslt action." );
/*
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );
*/
        return "css-add";
    }

    public String processAddXsltAction() {
        XsltAction.log.info( "Procss add xslt action." );
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

    public String cancelAddXsltAction() {
        XsltAction.log.info( "Cancel add xslt action." );
/*
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
*/
        return "site";
    }

// Edit actions
    public String editXsltAction() {
        XsltAction.log.info( "Edit holding action." );
/*
        holdingSessionBean.setEdit( true );
*/
        return "css-edit";
    }

    public String processEditXsltAction() {
        XsltAction.log.info( "Save changes xslt action." );
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

    public String cancelEditXsltAction() {
        XsltAction.log.info( "Cancel edit xslt action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

// Delete actions
    public String deleteXsltAction() {
        XsltAction.log.info( "delete xslt action." );

//	holdingSessionBean.setDelete( true );
    return "css-delete";
    }

    public String cancelDeleteXsltAction() {
        XsltAction.log.info( "Cancel delete holding action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

    public String processDeleteXsltAction() {
        XsltAction.log.info( "Process delete xslt action." );
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

    private void loadCurrentXslt() {
    }
}
