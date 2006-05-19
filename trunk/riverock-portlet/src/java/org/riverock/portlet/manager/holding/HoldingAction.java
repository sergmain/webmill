package org.riverock.portlet.manager.holding;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class HoldingAction implements Serializable {
    private final static Logger log = Logger.getLogger( HoldingAction.class );
    private static final long serialVersionUID = 2043005511L;

    private HoldingSessionBean holdingSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private HoldingDataProvider holdingDataProvider = null;

    public HoldingAction() {
    }

    // getter/setter methods
    public void setHoldingSessionBean( HoldingSessionBean holdingSessionBean) {
        this.holdingSessionBean = holdingSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public HoldingDataProvider getHoldingDataProvider() {
        return holdingDataProvider;
    }

    public void setHoldingDataProvider( HoldingDataProvider holdingDataProvider ) {
        this.holdingDataProvider = holdingDataProvider;
    }

// main select action
    public String selectHolding() {
        log.info( "Select holding action." );
        loadCurrentHolding();

        holdingSessionBean.resetStatus();
        return "holding";
    }


// Company actions
    public void deleteCompanyActionListener( ActionEvent event ) {
        log.info( "Delete company action." );

        Long companyId = holdingSessionBean.getCurrentCompanyId();
        log.info( "delete company with id: " + companyId );

        Iterator<CompanyBean> iterator = holdingSessionBean.getHoldingBean().getCompanies().iterator();
        while( iterator.hasNext() ) {
            CompanyBean company = iterator.next();

            if( company.getId().equals( companyId ) ) {
                log.info( "Company is found. set isDelete to true" );
                iterator.remove();
                break;
            }
        }
    }

    public void addCompanyAction() {
        log.info( "Add company action." );

        Long companyId = holdingSessionBean.getHoldingBean().getNewCompanyId();
        
        log.info( "New id of company: " + companyId );
        if( companyId == null ) {
            return;
        }
        CompanyBean company = new CompanyBean(
            FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany( companyId )
        );

        holdingSessionBean.getHoldingBean().getCompanies().add( company );
    }

// Add actions
    public String addHoldingAction() {
        log.info( "Add holding action." );
        HoldingBean bean = new HoldingBean();
        holdingSessionBean.setHoldingBean( bean );

        holdingSessionBean.setAdd( true );

        return "holding-add";
    }

    public String processAddHoldingAction() {
        log.info( "Procss add holding action." );
        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalDaoProvider().getPortalHoldingDao().processAddHolding( 
			holdingSessionBean.getHoldingBean() 
		);
            holdingSessionBean.setHoldingBean( null );

            holdingDataProvider.reinitHoldingBeans();
        }

	holdingSessionBean.resetStatus();
	return "holding";
    }

    public String cancelAddHoldingAction() {
        log.info( "Cancel add holding action." );
        holdingSessionBean.setHoldingBean( null );

        holdingSessionBean.resetStatus();
        return "holding";
    }

// Edit actions
    public String editHoldingAction() {
        log.info( "Edit holding action." );

        holdingSessionBean.setEdit( true );
        return "holding-edit";
    }

    public String saveHoldingAction() {
        log.info( "Save holding action." );
        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalDaoProvider().getPortalHoldingDao().processSaveHolding( 
			holdingSessionBean.getHoldingBean() 
		);
            holdingSessionBean.setHoldingBean( null );
            holdingDataProvider.reinitHoldingBeans();
        }

	holdingSessionBean.resetStatus();
	return "holding";
    }

    public String cancelEditHoldingAction() {
        log.info( "Cancel edit holding action." );

	holdingSessionBean.resetStatus();
	return "holding";
    }

// Delete actions
    public String deleteHoldingAction() {
        log.info( "delete holding action." );

	holdingSessionBean.setDelete( true );
	return "holding-delete";
    }

    public String cancelDeleteHoldingAction() {
        log.info( "Cancel delete holding action." );

	holdingSessionBean.resetStatus();
	return "holding";
    }

    public String processDeleteHoldingAction() {
        log.info( "Process delete holding action." );
        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalDaoProvider().getPortalHoldingDao().processDeleteHolding( 
			holdingSessionBean.getHoldingBean() 
		);

            holdingSessionBean.setHoldingBean( null );
            holdingDataProvider.reinitHoldingBeans();
        }

	holdingSessionBean.resetStatus();
	return "holding";
    }

    private void loadCurrentHolding() {
        holdingSessionBean.setHoldingBean( lookupHoldingBean( holdingSessionBean.getCurrentHoldingId() ) );
    }

    private HoldingBean lookupHoldingBean( Long holdingId ) {
        log.info( "start search holding bean for holdingId: " + holdingId );

        HoldingBean result = null;
        for (HoldingBean holdingBean : holdingDataProvider.getHoldingBeans()) {
            if (holdingBean.getId().equals(holdingId))
                result = holdingBean;
        }

        log.info( "end search holding");
        return result;
    }
}