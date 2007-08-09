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
package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

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
    private DataProvider dataProvider = null;

    public static final String[] ROLES = new String[]{"webmill.authentic"};

    public SiteLanguageAction() {
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
    public String selectSiteLanguage() {
        log.debug( "Select site language action." );
        loadCurrentObject();

        return "site";
    }

// Add actions
    public String addSiteLanguageAction() {
        log.debug( "Add site language action." );

        SiteLanguageBean siteLanguageBean = new SiteLanguageBean();
        siteLanguageBean.setSiteId(siteSessionBean.getId());
        setSessionObject(siteLanguageBean);

        return "site-language-add";
    }

    public String processAddSiteLanguageAction() {
        log.debug( "Procss add site language action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            Long siteLanguageId = FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().createSiteLanguage(
                getSessionObject()
            );
            setSessionObject(null);
            siteSessionBean.setId(siteLanguageId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelAddSiteLanguageAction() {
        log.debug( "Cancel add site language action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editSiteLanguageAction() {
        log.debug( "Edit site language action." );

        return "site-language-edit";
    }

    public String processEditSiteLanguageAction() {
        log.debug( "Save changes site language action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().updateSiteLanguage(
                getSessionObject()
            );
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditSiteLanguageAction() {
        log.debug( "Cancel edit site language action." );

        loadCurrentObject();
        return "site";
    }

// Delete actions
    public String deleteSiteLanguageAction() {
        log.debug( "delete site language action." );

        setSessionObject( new SiteLanguageBean(dataProvider.getSiteLanguage()) );

        return "site-language-delete";
    }

    public String cancelDeleteSiteLanguageAction() {
        log.debug( "Cancel delete holding action." );

        return "site";
    }

    public String processDeleteSiteLanguageAction() {
        log.debug( "Process delete site language action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() != null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().deleteSiteLanguage(getSessionObject().getSiteLanguageId());
            setSessionObject(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "site";
    }

    private void setSessionObject(SiteLanguageBean bean) {
        siteSessionBean.setSiteLanguage( bean );
    }

    private void loadCurrentObject() {
        siteSessionBean.setSiteLanguage( new SiteLanguageBean(dataProvider.getSiteLanguage()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearSiteLanguage();
    }

    private SiteLanguageBean getSessionObject() {
        return siteSessionBean.getSiteLanguage();
    }
}
