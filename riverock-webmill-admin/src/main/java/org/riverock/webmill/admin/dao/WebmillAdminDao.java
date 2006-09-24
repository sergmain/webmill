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
package org.riverock.webmill.admin.dao;

import java.util.List;

import org.riverock.webmill.admin.bean.*;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 17:26:33
 */
public interface WebmillAdminDao {
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

    Long createXslt(XsltBean xsltBean);

    SiteLanguageBean getSiteLanguage(Long siteId, String locale);

    Long createSiteLanguage(SiteLanguageBean siteLanguageBean);

    TemplateBean getTemplate(String templateName, Long siteLanguageId);

    Long createTemplate(TemplateBean templateBean);

    Long createPortletName(PortletNameBean portletNameBean);

    Long getCatalogItemId(Long siteLanguageId, Long portletId, Long templateId);

    Long createCatalogItem(CatalogBean catalogBean);

    Long createVirtualHost(VirtualHostBean virtualHost);

    Long createVirtualHost(DatabaseAdapter adapter, VirtualHostBean virtualHost);

    XsltBean getCurrentXslt(Long siteLanguageId);

    Long createCatalogLanguageItem(CatalogLanguageBean bean);

    SiteBean getSite(String siteName);

    XsltBean getXslt(String name, Long siteLanguageId);

    CatalogLanguageBean getCatalogLanguageItem(String catalogCode, Long siteLanguageId);

    PortletNameBean getPortletName(Long portletId);

    TemplateBean getTemplate(Long templateId, Long siteLanguageId);

    PortletNameBean getPortletName(String portletName);

    PortletNameBean createPortletName(String portletNameBean);
}
