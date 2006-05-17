/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

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
    public String selectHolding() {
        log.info( "Select holding action." );
        loadCurrentHolding();

//        siteSessionBean.resetStatus();
        return "holding";
    }


// Company actions
    public void deleteCompanyActionListener( ActionEvent event ) {
        log.info( "Delete company action." );
/*
        Long companyId = siteSessionBean.getCurrentCompanyId();
        log.info( "delete company with id: " + companyId );

        Iterator<CompanyBean> iterator = null;
        iterator = siteSessionBean.getHoldingBean().getCompanies().iterator();
        while( iterator.hasNext() ) {
            CompanyBean company = iterator.next();

            if( company.getId().equals( companyId ) ) {
                log.info( "Company is found. set isDelete to true" );
                iterator.remove();
                break;
            }
        }
*/        
    }

    public void selectSiteAction(){
        log.info( "select site action." );
    }

    public void selectSiteLanguageAction(){
        log.info( "select site language action." );
    }

    public void selectTemplateAction(){
        log.info( "select template action." );
    }

    public void addCompanyAction() {
        log.info( "Add company action." );
/*
        Long companyId = siteSessionBean.getHoldingBean().getNewCompanyId();
        
        log.info( "New id of company: " + companyId );
        if( companyId == null ) {
            return;
        }
        CompanyBean company; 
        company= new CompanyBean(
            FacesTools.getPortalDaoProvider().getPortalCompanyDao().loadCompany( companyId )
        );

        holdingSessionBean.getHoldingBean().getCompanies().add( company );
*/        
    }

// Add actions
    public String addHoldingAction() {
        log.info( "Add holding action." );
/*        
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );
*/
        return "holding-add";
    }

    public String processAddHoldingAction() {
        log.info( "Procss add holding action." );
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
	return "holding";
    }

    public String cancelAddHoldingAction() {
        log.info( "Cancel add holding action." );
/*        
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
*/        
        return "holding";
    }

// Edit actions
    public String editHoldingAction() {
        log.info( "Edit holding action." );
/*
        holdingSessionBean.setEdit( true );
*/        
        return "holding-edit";
    }

    public String saveHoldingAction() {
        log.info( "Save holding action." );
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
	return "holding";
    }

    public String cancelEditHoldingAction() {
        log.info( "Cancel edit holding action." );

//	holdingSessionBean.resetStatus();
	return "holding";
    }

// Delete actions
    public String deleteHoldingAction() {
        log.info( "delete holding action." );

//	holdingSessionBean.setDelete( true );
	return "holding-delete";
    }

    public String cancelDeleteHoldingAction() {
        log.info( "Cancel delete holding action." );

//	holdingSessionBean.resetStatus();
	return "holding";
    }

    public String processDeleteHoldingAction() {
        log.info( "Process delete holding action." );
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
	return "holding";
    }

    private void loadCurrentHolding() {
    }
}
