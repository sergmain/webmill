/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.action;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.webmill.admin.SiteDataProvider;
import org.riverock.webmill.admin.SiteSessionBean;
import org.riverock.webmill.admin.bean.SiteBean;
import org.riverock.webmill.admin.bean.SiteExtended;
import org.riverock.webmill.admin.dao.DaoFactory;
import org.riverock.webmill.admin.service.CreateSiteService;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class SiteAction implements Serializable {
    private final static Logger log = Logger.getLogger(SiteAction.class);
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private SiteDataProvider siteDataProvider = null;

    public SiteAction() {
    }

    public void setSiteDataProvider(SiteDataProvider dataProvider) {
        this.siteDataProvider = dataProvider;
    }

    public void setSiteSessionBean(SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public String selectSite(ActionEvent event) {
        log.debug("Select site action.");
        loadCurrentSite();

        return "site";
    }

// Add actions

    public String addSiteAction() {
        log.debug("Add site action.");

        if (log.isDebugEnabled()) {
            log.debug("site: " + siteSessionBean.getSiteExtended());
            log.debug("    objectType: " + siteSessionBean.getObjectType());
            log.debug("    objectId: " + siteSessionBean.getId());
            if (siteSessionBean.getSiteExtended() != null) {
                log.debug("    language: " + siteSessionBean.getSiteExtended().getSite().getDefLanguage());
                log.debug("    country: " + siteSessionBean.getSiteExtended().getSite().getDefCountry());
                log.debug("    variant: " + siteSessionBean.getSiteExtended().getSite().getDefVariant());
                log.debug("    companyId: " + siteSessionBean.getSiteExtended().getSite().getCompanyId());
            }
        }

        SiteExtended siteExtended = new SiteExtended(new SiteBean(), new ArrayList<String>(), null);
        siteSessionBean.setSiteExtended(siteExtended);

        return "site-add";
    }

    public String processAddSiteAction() {
        log.debug("Procss add site action.");

        SiteExtended siteExtended = siteSessionBean.getSiteExtended();
        if (log.isDebugEnabled()) {
            log.debug("site: " + siteExtended);
            if (siteExtended != null) {
                log.debug("    language: " + siteExtended.getSite().getDefLanguage());
                log.debug("    country: " + siteExtended.getSite().getDefCountry());
                log.debug("    variant: " + siteExtended.getSite().getDefVariant());
                log.debug("    companyId: " + siteExtended.getSite().getCompanyId());
                log.debug("    objectType: " + siteSessionBean.getObjectType());
                log.debug("    objectId: " + siteSessionBean.getId());
            }
        }

        if (siteExtended != null) {

            Long siteId = CreateSiteService.createSite(siteExtended);

            siteSessionBean.setSiteExtended(null);
            siteSessionBean.setId(siteId);
            siteDataProvider.clearSite();
            loadCurrentSite();
        }

        return "site";
    }

    public String cancelAddSiteAction() {
        log.debug("Cancel add site action.");

        siteSessionBean.setSiteExtended(null);
        siteDataProvider.clearSite();

        return "site";
    }

// Edit actions

    public String editSiteAction() {
        log.debug("Edit site action.");

        return "site-edit";
    }

    public String processEditSiteAction() {
        log.debug("Save changes site action.");

        if (siteSessionBean.getSiteExtended() != null) {
            DaoFactory.getWebmillAdminDao().updateSiteWithVirtualHost(
                siteSessionBean.getSiteExtended().getSite(), siteSessionBean.getSiteExtended().getVirtualHosts()
            );
            siteDataProvider.clearSite();
            loadCurrentSite();
        }

        return "site";
    }

    public String cancelEditSiteAction() {
        log.debug("Cancel edit site action.");

        return "site";
    }

// Delete actions

    public String deleteSiteAction() {
        log.debug("delete site action.");

        siteSessionBean.setSiteExtended(siteDataProvider.getSiteExtended());

        return "site-delete";
    }

    public String cancelDeleteSiteAction() {
        log.debug("Cancel delete holding action.");

        return "site";
    }

    public String processDeleteSiteAction() {
        log.info("Process delete site action.");
        if (siteSessionBean.getSiteExtended() != null) {
            DaoFactory.getWebmillAdminDao().deleteSite(siteSessionBean.getSiteExtended().getSite().getSiteId());
            siteSessionBean.setSiteExtended(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            siteDataProvider.clearSite();
        }

        return "site";
    }

    public String changeSite() {
        log.info("Change site action.");
        return "site";
    }

// virtual host actions

    public void deleteVirtualHostActionListener(ActionEvent event) {
        log.debug("Delete virtual host action.");

        String host = siteSessionBean.getCurrentVirtualHost();
        if (log.isDebugEnabled()) {
            log.debug("delete virtual host: " + host);
        }

        if (StringUtils.isBlank(host)) {
            return;
        }

        siteSessionBean.getSiteExtended().getVirtualHosts().remove(host.toLowerCase());

    }

    public void addVirtualHostAction() {
        log.debug("Add virtual host action.");

        String newHost = siteSessionBean.getNewVirtualHost();

        if (log.isDebugEnabled()) {
            log.debug("New virtual host: " + newHost);
        }

        if (StringUtils.isBlank(newHost)) {
            return;
        }

        siteSessionBean.getSiteExtended().getVirtualHosts().add(newHost.toLowerCase());
        siteSessionBean.setNewVirtualHost(null);
    }

// private methods

    private void loadCurrentSite() {
        siteSessionBean.setSiteExtended(siteDataProvider.getSiteExtended());
    }
}
