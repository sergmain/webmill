/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.webmill.admin.bean.CompanyBean;
import org.riverock.webmill.admin.bean.SiteBean;
import org.riverock.webmill.admin.bean.SiteExtended;
import org.riverock.webmill.admin.bean.VirtualHostBean;
import org.riverock.webmill.admin.dao.DaoFactory;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class SiteService implements Serializable {
    private final static Logger log = Logger.getLogger(SiteService.class);
    private static final long serialVersionUID = 2058005507L;

    public SiteService() {
    }

    public List<SelectItem> getSiteList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<SiteBean> sites = DaoFactory.getWebmillAdminDao().getSites();

        for (SiteBean site : sites) {
            if (site.getSiteId() == null) {
                throw new IllegalStateException("siteIdd is null, name: " + site.getSiteName());
            }

            list.add(new SelectItem(site.getSiteId(), site.getSiteName()));
        }
        return list;
    }

    @SuppressWarnings({"RedundantStringConstructorCall"})
    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<CompanyBean> companies = DaoFactory.getWebmillAdminDao().getCompanyList();

        for (CompanyBean company : companies) {
            if (company.getId() == null) {
                throw new IllegalStateException("id is null, name: " + company.getName());
            }
            String companyName = company.getName();
            if (StringUtils.isBlank(companyName)) {
                companyName = "<empty company name>";
            }
            list.add(new SelectItem(company.getId().toString(), companyName));
        }
        return list;
    }

    public SiteBean getSite(Long siteId) {
        SiteBean site = DaoFactory.getWebmillAdminDao().getSite(siteId);
        return new SiteBean(site);
    }

    public List<SiteBean> getSites() {
        List<SiteBean> list = new ArrayList<SiteBean>();
        List<SiteBean> sites = DaoFactory.getWebmillAdminDao().getSites();
        for (SiteBean site: sites) {
            list.add( new SiteBean(site) );
        }
        return list;
    }

    public SiteExtended getSiteExtended(Long siteId) {
        SiteExtended siteExtended = new SiteExtended();
        siteExtended.setSite(DaoFactory.getWebmillAdminDao().getSite(siteId));
        List<VirtualHostBean> virtualHosts = DaoFactory.getWebmillAdminDao().getVirtualHosts(siteExtended.getSite().getSiteId());
        List<String> hosts = new ArrayList<String>();
        for (VirtualHostBean host : virtualHosts) {
            hosts.add(host.getHost().toLowerCase());
        }
        siteExtended.setVirtualHosts(hosts);
        Long companyId = siteExtended.getSite().getCompanyId();
        CompanyBean company = DaoFactory.getWebmillAdminDao().getCompany(companyId);
        if (log.isDebugEnabled()) {
            log.debug("companyId: " + companyId);
            log.debug("company: " + company);
        }
        siteExtended.setCompany(company);
        return siteExtended;
    }
}
