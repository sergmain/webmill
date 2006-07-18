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
 *         Time: 17:26:33
 */
public interface WebmillInitDao {
    List<CompanyBean> getCompanyList();

    List<SiteBean> getSites();

    SiteBean getSite(Long siteId);

    List<VirtualHostBean> getVirtualHosts(Long siteId);

    CompanyBean getCompany(Long companyId);

    List<UserBean> getUserList();

    Long processAddCompany(CompanyBean company);

    void processSaveCompany(CompanyBean company);

    void processDeleteCompany(CompanyBean company);

    Long addUser(UserBean portalUser);

    UserBean getUser(Long currentPortalUserId);

    void updateUser(UserBean portalUser);

    void deleteUser(UserBean portalUser);

    void updateSiteWithVirtualHost(SiteBean site, List<String> virtualHosts);

    void deleteSite(Long siteId);

    Long createSiteWithVirtualHost(SiteBean site, List<String> virtualHosts);

    List<VirtualHostBean> getVirtualHostsFullList();

    Long createSite(SiteBean siteBean);

    void createXslt(XsltBean xsltBean);

    SiteLanguageBean getSiteLanguage(Long siteId, String locale);

    Long createSiteLanguage(SiteLanguageBean siteLanguageBean);

    TemplateBean getTemplate(String templateName, Long siteLanguageId);

    void createTemplate(TemplateBean templateBean);

    Long createPortletName(PortletNameBean portletNameBean);

    Long getCatalogItemId(Long siteLanguageId, Long portletId, Long templateId);

    Long createCatalogItem(CatalogBean catalogBean);

    void createVirtualHost(VirtualHostBean virtualHost);

    XsltBean getCurrentXslt(Long siteLanguageId);

    Long createCatalogLanguageItem(CatalogLanguageBean bean);

    SiteBean getSite(String siteName);

    XsltBean getXslt(String name, Long siteLanguageId);

    CatalogLanguageBean getCatalogLanguageItem(String catalogCode, Long siteLanguageId);

    PortletNameBean getPortletName(Long portletId);

    TemplateBean getTemplate(Long templateId, Long siteLanguageId);
}
