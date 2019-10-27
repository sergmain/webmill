/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.portlet.manager.holding;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id: HoldingAction.java 1411 2007-09-06 20:27:38Z serg_main $
 */
public class HoldingAction implements Serializable {
    private final static Logger log = Logger.getLogger( HoldingAction.class );
    private static final long serialVersionUID = 2043005511L;

    private HoldingSessionBean holdingSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private HoldingDataProvider holdingDataProvider = null;

    public static final String[] ROLES = new String[]{"webmill.portal-manager"};

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
    public void deleteCompanyAction() {
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
            FacesTools.getPortalSpiProvider().getPortalCompanyDao().getCompany( companyId )
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( holdingSessionBean.getHoldingBean() != null ) {
            FacesTools.getPortalSpiProvider().getPortalHoldingDao().processAddHolding(
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalSpiProvider().getPortalHoldingDao().processSaveHolding(
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

        loadCurrentHolding();
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( holdingSessionBean.getHoldingBean() != null ) {
		FacesTools.getPortalSpiProvider().getPortalHoldingDao().processDeleteHolding(
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