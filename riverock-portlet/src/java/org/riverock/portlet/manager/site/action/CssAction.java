/**
 * License
 * 
 */
package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.DataProvider;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class CssAction implements Serializable {
    private final static Logger log = Logger.getLogger( CssAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public CssAction() {
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
    public String selectCss() {
        log.info( "Select CSS action." );
        loadCurrentCss();

//        siteSessionBean.resetStatus();
        return "site";
    }

// Add actions
    public String addCssAction() {
        log.info( "Add CSS action." );
/*        
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );
*/
        return "css-add";
    }

    public String processAddCssAction() {
        log.info( "Procss add CSS action." );
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

    public String cancelAddCssAction() {
        log.info( "Cancel add CSS action." );
/*        
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
*/
        return "site";
    }

// Edit actions
    public String editCssAction() {
        log.info( "Edit CSS action." );
/*
        holdingSessionBean.setEdit( true );
*/
        return "css-edit";
    }

    public String processEditCssAction() {
        log.info( "Save changes CSS action." );
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

    public String cancelEditCssAction() {
        log.info( "Cancel edit CSS action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

// Delete actions
    public String deleteCssAction() {
        log.info( "delete CSS action." );

//	holdingSessionBean.setDelete( true );
    return "css-delete";
    }

    public String cancelDeleteCssAction() {
        log.info( "Cancel delete CSS action." );

//	holdingSessionBean.resetStatus();
    return "site";
    }

    public String processDeleteCssAction() {
        log.info( "Process delete CSS action." );
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

    private void loadCurrentCss() {
    }
}
