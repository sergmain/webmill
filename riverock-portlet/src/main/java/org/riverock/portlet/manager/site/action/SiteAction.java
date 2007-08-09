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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.SiteBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.VirtualHostBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

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

    public static final String[] ROLES = new String[]{"webmill.authentic"};

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

        SiteExtended siteExtended = new SiteExtended( new SiteBean(), new ArrayList<VirtualHostBean>(), null );
        siteSessionBean.setSiteExtended(siteExtended);

        return "site-add";
    }

    public String processAddSiteAction() {
        log.debug( "Procss add site action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

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
                siteSessionBean.getSiteExtended().getSite(), (List)siteSessionBean.getSiteExtended().getVirtualHosts()
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( siteSessionBean.getSiteExtended()!=null ) {
            if (log.isDebugEnabled()) {
                log.debug("virtual hosts: " +siteSessionBean.getSiteExtended().getVirtualHosts());
            }
            FacesTools.getPortalDaoProvider().getPortalSiteDao().updateSiteWithVirtualHost(
                siteSessionBean.getSiteExtended().getSite(), (List)siteSessionBean.getSiteExtended().getVirtualHosts()
            );
            dataProvider.clearSite();
            loadCurrentSite();
        }

        return "site";
    }

    public String cancelEditSiteAction() {
        log.debug( "Cancel edit site action." );
        loadCurrentSite();
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( siteSessionBean.getSiteExtended() != null ) {
            FacesTools.getPortalDaoProvider().getPortalSiteDao().deleteSite(siteSessionBean.getSiteExtended().getSite().getSiteId());
            siteSessionBean.setCurrentSiteId(null);
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

        Iterator<VirtualHostBean> iterator = siteSessionBean.getSiteExtended().getVirtualHosts().iterator();
        while (iterator.hasNext()) {
            VirtualHostBean virtualHostBean = iterator.next();
            if (virtualHostBean.getHost().equalsIgnoreCase(host)) {
                iterator.remove();
            }
        }
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

        siteSessionBean.getSiteExtended().getVirtualHosts().add(
            new VirtualHostBean(
                newHost.toLowerCase(), null,
                siteSessionBean.getSiteExtended().getVirtualHosts().isEmpty(), null
            )
        );
        siteSessionBean.setNewVirtualHost(null);
    }

// private methods
    private void loadCurrentSite() {
        siteSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
    }
}
