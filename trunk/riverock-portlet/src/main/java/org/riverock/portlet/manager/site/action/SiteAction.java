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
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.SiteBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.portlet.tools.FacesTools;

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
    private DataProvider dataProvider = null;

    public SiteAction() {
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setSiteSessionBean( SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public String selectSite() {
        log.debug( "Select site action." );
        loadCurrentSite();

        return "site";
    }

// Add actions
    public String addSiteAction() {
        log.debug( "Add site action." );

        if (log.isDebugEnabled()) {
            log.debug("site: " + siteSessionBean.getSiteExtended());
            log.debug("    objectType: " + siteSessionBean.getObjectType());
            log.debug("    objectId: " + siteSessionBean.getId());
            if (siteSessionBean.getSiteExtended()!=null) {
                log.debug("    language: " + siteSessionBean.getSiteExtended().getSite().getDefLanguage());
                log.debug("    country: " + siteSessionBean.getSiteExtended().getSite().getDefCountry());
                log.debug("    variant: " + siteSessionBean.getSiteExtended().getSite().getDefVariant());
                log.debug("    companyId: " + siteSessionBean.getSiteExtended().getSite().getCompanyId());
            }
        }

        SiteExtended siteExtended = new SiteExtended( new SiteBean(), new ArrayList<String>(), null );
        siteSessionBean.setSiteExtended(siteExtended);

        return "site-add";
    }

    public String processAddSiteAction() {
        log.debug( "Procss add site action." );

        if (log.isDebugEnabled()) {
            log.debug("site: " + siteSessionBean.getSiteExtended());
            if (siteSessionBean.getSiteExtended()!=null) {
                log.debug("    language: " + siteSessionBean.getSiteExtended().getSite().getDefLanguage());
                log.debug("    country: " + siteSessionBean.getSiteExtended().getSite().getDefCountry());
                log.debug("    variant: " + siteSessionBean.getSiteExtended().getSite().getDefVariant());
                log.debug("    companyId: " + siteSessionBean.getSiteExtended().getSite().getCompanyId());
                log.debug("    objectType: " + siteSessionBean.getObjectType());
                log.debug("    objectId: " + siteSessionBean.getId());
            }
        }

        if( siteSessionBean.getSiteExtended()!=null ) {
            Long siteId = FacesTools.getPortalDaoProvider().getPortalSiteDao().createSiteWithVirtualHost(
                siteSessionBean.getSiteExtended().getSite(), siteSessionBean.getSiteExtended().getVirtualHosts()
            );
            siteSessionBean.setSiteExtended(null);
            siteSessionBean.setId(siteId);
            dataProvider.clearSite();
            loadCurrentSite();
        }

        return "site";
    }

    public String cancelAddSiteAction() {
        log.debug( "Cancel add site action." );

        siteSessionBean.setSiteExtended(null);
        dataProvider.clearSite();

        return "site";
    }

// Edit actions
    public String editSiteAction() {
        log.debug( "Edit site action." );

        return "site-edit";
    }

    public String processEditSiteAction() {
        log.debug( "Save changes site action." );

        if( siteSessionBean.getSiteExtended()!=null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteDao().updateSiteWithVirtualHost(
                siteSessionBean.getSiteExtended().getSite(), siteSessionBean.getSiteExtended().getVirtualHosts()
            );
            dataProvider.clearSite();
            loadCurrentSite();
        }

        return "site";
    }

    public String cancelEditSiteAction() {
        log.debug( "Cancel edit site action." );

        return "site";
    }

// Delete actions
    public String deleteSiteAction() {
        log.debug( "delete site action." );

        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );

        return "site-delete";
    }

    public String cancelDeleteSiteAction() {
        log.debug( "Cancel delete holding action." );

        return "site";
    }

    public String processDeleteSiteAction() {
        log.info( "Process delete site action." );
        if( siteSessionBean.getSiteExtended() != null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteDao().deleteSite(siteSessionBean.getSiteExtended().getSite().getSiteId());
            siteSessionBean.setSiteExtended( null );
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            dataProvider.clearSite();
        }

        return "site";
    }

    public String changeSite() {
        log.info( "Change site action." );
        return "site";
    }

// virtual host actions
    public void deleteVirtualHostAction() {
        log.debug( "Delete virtual host action." );

        String host = siteSessionBean.getCurrentVirtualHost();
        if (log.isDebugEnabled()) {
            log.debug( "delete virtual host: " + host );
        }

        if (StringUtils.isBlank(host)) {
            return;
        }

        siteSessionBean.getSiteExtended().getVirtualHosts().remove(host.toLowerCase());

    }

    public void addVirtualHostAction() {
        log.debug( "Add virtual host action." );

        String newHost = siteSessionBean.getNewVirtualHost();

        if (log.isDebugEnabled()) {
            log.debug( "New virtual host: " + newHost );
        }

        if (StringUtils.isBlank(newHost)) {
            return;
        }

        siteSessionBean.getSiteExtended().getVirtualHosts().add( newHost.toLowerCase() );
        siteSessionBean.setNewVirtualHost(null);
    }

// private methods
    private void loadCurrentSite() {
        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
    }
}
