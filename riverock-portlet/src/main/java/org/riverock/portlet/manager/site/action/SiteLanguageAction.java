/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;
import org.riverock.portlet.tools.FacesTools;

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
