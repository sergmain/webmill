/*
 * org.riverock.webmill.init - Webmill portal initializer web application
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
package org.riverock.webmill.init.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.webmill.init.bean.*;
import org.riverock.webmill.init.dao.DaoFactory;
import org.riverock.webmill.init.dao.WebmillInitDao;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class CreateSiteService {
    private final static Logger log = Logger.getLogger(CreateSiteService.class);

    public static final String SITE_CONFIG_FILE = "/WEB-INF/webmill/static/site-config.xml";
    private static final int MAX_BINARY_FILE_SIZE = 0x4000;

    private static class VirtualHostSiteExist {
        private VirtualHostSiteExist(boolean isValid) {
            this.isValid = isValid;
        }

        private VirtualHostSiteExist(boolean isValid, Long siteId) {
            this.isValid = isValid;
            this.siteId = siteId;
        }

        private boolean isValid;
        private Long siteId;
    }

    public static void createSite(SiteExtended siteExtended) {

        WebmillInitDao dao = DaoFactory.getWebmillInitDao();

        List<VirtualHostBean> hostFullList = dao.getVirtualHostsFullList();
        if (log.isDebugEnabled()) {
            log.debug("process site: " + siteExtended.getSite().getSiteName());
            if (hostFullList != null && !hostFullList.isEmpty()) {
                for (VirtualHostBean virtualHost : hostFullList) {
                    log.debug("   virtual host: " + virtualHost.getHost() + ", siteId: " + virtualHost.getSiteId());
                }
            } else {
                log.debug("   no virtual host for this site");
            }
        }

        VirtualHostSiteExist b = checkVirtualHost(hostFullList, siteExtended.getVirtualHosts());
        if (!b.isValid) {
            log.error("Error in list of virtual hosts, name site config: " + siteExtended.getSite().getSiteName());
        }

        if (b.siteId == null) {
            // create new company
            CompanyBean company = dao.getCompany(siteExtended.getCompany().getId());
            if (company == null) {
                CompanyBean companyBean = createCompanyBean(siteExtended.getCompany());
                companyBean.setId(dao.processAddCompany(companyBean));
                company = companyBean;
            }
            SiteBean site = dao.getSite(siteExtended.getSite().getSiteName());
            if (site == null) {
                //Create new site
                SiteBean siteBean = new SiteBean();
                siteBean.setSiteName(siteExtended.getSite().getSiteName());
                siteBean.setCssDynamic(false);
                siteBean.setCssFile(siteExtended.getSite().getCssFile());
                Locale locale = StringTools.getLocale(siteExtended.getSite().getSiteDefaultLocale());
                siteBean.setDefCountry(locale.getCountry());
                siteBean.setDefLanguage(locale.getLanguage());
                siteBean.setDefVariant(locale.getVariant());
                siteBean.setCompanyId(company.getId());
                b.siteId = dao.createSite(siteBean);
            } else {
                b.siteId = site.getSiteId();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("    siteId: " + b.siteId);
        }
        List<VirtualHostBean> hosts = dao.getVirtualHosts(b.siteId);
        for (String hostName : siteExtended.getVirtualHosts()) {
            boolean isNotExists = true;
            for (VirtualHostBean host : hosts) {
                if (hostName.equalsIgnoreCase(host.getHost())) {
                    isNotExists = false;
                    break;
                }
            }
            if (isNotExists) {
                VirtualHostBean virtualHost = new VirtualHostBean();
                virtualHost.setHost(hostName.toLowerCase());
                virtualHost.setSiteId(b.siteId);
                dao.createVirtualHost(virtualHost);
            }
        }

        for (SiteLanguageBean siteLanguageConfig : siteExtended.getSiteLanguage()) {
            SiteLanguageBean siteLanguage = dao.getSiteLanguage(b.siteId, siteLanguageConfig.getLocale());
            if (siteLanguage == null) {
                SiteLanguageBean siteLanguageBean = new SiteLanguageBean();
                siteLanguageBean.setSiteId(b.siteId);
                siteLanguageBean.setLocale(siteLanguageConfig.getLocale());
                siteLanguageBean.setNameCustomLanguage(siteLanguageConfig.getLocale());
                siteLanguageBean.setSiteLanguageId( dao.createSiteLanguage(siteLanguageBean) );
                siteLanguage = siteLanguageBean;
            }
            for (TemplateBean templateItem : siteLanguageConfig.getTemplates()) {
                TemplateBean template = dao.getTemplate(templateItem.getTemplateName(), siteLanguage.getSiteLanguageId());
                if (template == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("template");
                        log.debug("    name: " + templateItem.getTemplateName());
                        log.debug("    id: " + templateItem.getTemplateId());
                        log.debug("    lang: " + templateItem.getTemplateLanguage());
                        log.debug("    siteLangId: " + templateItem.getSiteLanguageId());
                        log.debug("    isDefaultDynamic: " + templateItem.isDefaultDynamic());
                    }
                    dao.createTemplate(templateItem);
                }
            }
            XsltBean xslt = siteLanguageConfig.getXslt();
            xslt.setCurrent(true);
            DaoFactory.getWebmillInitDao().createXslt(xslt);

            for (CatalogLanguageBean catalogLanguage : siteLanguageConfig.getCatalogLanguages()) {
                CatalogLanguageBean catalogLanguageItem =
                    dao.getCatalogLanguageItem(catalogLanguage.getCatalogCode(), siteLanguage.getSiteLanguageId());

                if (catalogLanguageItem == null) {
                    catalogLanguage.setSiteLanguageId(siteLanguage.getSiteLanguageId());
                    catalogLanguage.setCatalogLanguageId( dao.createCatalogLanguageItem(catalogLanguage) );
                }

                processMenu(siteLanguage, catalogLanguage, catalogLanguage.getCatalogItems(), 0L);
            }
        }
    }

    private static VirtualHostSiteExist checkVirtualHost(List<VirtualHostBean> hostFullList, List hosts) {
        if (hosts == null || hosts.isEmpty()) {
            return new VirtualHostSiteExist(true);
        }

        Long siteId = null;
        for (Object objHost : hosts) {
            String hostName = (String) objHost;
            for (VirtualHostBean host : hostFullList) {
                if (hostName.equalsIgnoreCase(host.getHost())) {
                    if (siteId == null) {
                        siteId = host.getSiteId();
                    }
                    if (!siteId.equals(host.getSiteId())) {
                        return new VirtualHostSiteExist(false);
                    }
                    break;
                }
            }
        }
        return new VirtualHostSiteExist(true, siteId);
    }

    private static void processMenu(SiteLanguageBean siteLanguage, CatalogLanguageBean catalogLanguageItem, List<CatalogItemBean> menuList, Long topCatalogItemId) {
        if (menuList == null) {
            return;
        }

        int orderFiled = 1;
        for (CatalogItemBean menuItem : menuList) {
            if (log.isDebugEnabled()) {
                log.debug("Process menu item");
                log.debug("    portletName: " + menuItem.getPortletId());
                log.debug("    templateName: " + menuItem.getTemplateId());
                log.debug("    menuName: " + menuItem.getKeyMessage());
                log.debug("    url: " + menuItem.getUrl());
            }
            PortletNameBean portletName = DaoFactory.getWebmillInitDao().getPortletName(menuItem.getPortletId());
            if (portletName==null) {
                throw new IllegalStateException("Portlet name not found for ID "+menuItem.getPortletId());
            }

            TemplateBean template = DaoFactory.getWebmillInitDao().getTemplate(menuItem.getTemplateId(), siteLanguage.getSiteLanguageId());
            if (template==null) {
                throw new IllegalStateException("Template not found for ID "+menuItem.getTemplateId());
            }

            Long catalogItemId = DaoFactory.getWebmillInitDao().getCatalogItemId(
                siteLanguage.getSiteLanguageId(), portletName.getPortletId(), template.getTemplateId()
            );

            if (log.isDebugEnabled()) {
                log.debug("catalogItemId: " + catalogItemId);
                log.debug("catalogLanguageItem.getCatalogLanguageId(): " + catalogLanguageItem.getCatalogLanguageId());
                log.debug("portletName.getPortletId(): " + portletName.getPortletId());
                log.debug("template.getTemplateId(): " + template.getTemplateId());
            }

            if (catalogItemId == null) {
                CatalogBean catalogBean = new CatalogBean();
                catalogBean.setCatalogLanguageId(catalogLanguageItem.getCatalogLanguageId());
                catalogBean.setPortletId(portletName.getPortletId());
                catalogBean.setTopCatalogId(topCatalogItemId);
                catalogBean.setTemplateId(template.getTemplateId());
                catalogBean.setKeyMessage(menuItem.getKeyMessage());
                catalogBean.setUrl(menuItem.getUrl());
                catalogBean.setOrderField(orderFiled++);

                catalogItemId = DaoFactory.getWebmillInitDao().createCatalogItem(catalogBean);
            }

            processMenu(siteLanguage, catalogLanguageItem, menuItem.getSubCatalogItemList(), catalogItemId);
        }
    }

    private static String readBinaryFile(String binaryFileName) throws IOException {
        File templateFile = new File(binaryFileName);
        InputStream inputStream = new FileInputStream(templateFile);
        long size = templateFile.length();
        if (size > MAX_BINARY_FILE_SIZE) {
            throw new IllegalStateException("Template file too big. Max size of template - " + MAX_BINARY_FILE_SIZE);
        }
        byte[] bytes = new byte[(int) size];
        inputStream.read(bytes, 0, (int) size);

        return new String(bytes);
    }

    private static CompanyBean createCompanyBean(CompanyBean companyType) {
        CompanyBean company = new CompanyBean();
        company.setAddress(companyType.getAddress());
        company.setCeo(companyType.getCeo());
        company.setCfo(companyType.getCfo());
        company.setDeleted(false);
        company.setInfo(companyType.getInfo());
        company.setName(companyType.getName());
        company.setShortName(companyType.getShortName());
        company.setWebsite(companyType.getWebsite());
        return company;
    }
}
