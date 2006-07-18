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
package org.riverock.webmill.admin.dao;

import java.util.List;

import org.riverock.webmill.admin.bean.*;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 17:26:38
 */
public class WebmillInitDaoImpl implements WebmillInitDao {
    public List<CompanyBean> getCompanyList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<SiteBean> getSites() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SiteBean getSite(Long siteId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<VirtualHostBean> getVirtualHosts(Long siteId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CompanyBean getCompany(Long companyId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UserBean> getUserList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long processAddCompany(CompanyBean company) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void processSaveCompany(CompanyBean company) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void processDeleteCompany(CompanyBean company) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long addUser(UserBean portalUser) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserBean getUser(Long currentPortalUserId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateUser(UserBean portalUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteUser(UserBean portalUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateSiteWithVirtualHost(SiteBean site, List<String> virtualHosts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteSite(Long siteId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteWithVirtualHost(SiteBean site, List<String> virtualHosts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<VirtualHostBean> getVirtualHostsFullList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSite(SiteBean siteBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createXslt(XsltBean xsltBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public SiteLanguageBean getSiteLanguage(Long siteId, String locale) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteLanguage(SiteLanguageBean siteLanguageBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getTemplate(String templateName, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createTemplate(TemplateBean templateBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createPortletName(PortletNameBean portletNameBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletId, Long templateId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogItem(CatalogBean catalogBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createVirtualHost(VirtualHostBean virtualHost) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public XsltBean getCurrentXslt(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogLanguageItem(CatalogLanguageBean bean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SiteBean getSite(String siteName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public XsltBean getXslt(String name, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CatalogLanguageBean getCatalogLanguageItem(String catalogCode, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PortletNameBean getPortletName(Long portletId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getTemplate(Long templateId, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
