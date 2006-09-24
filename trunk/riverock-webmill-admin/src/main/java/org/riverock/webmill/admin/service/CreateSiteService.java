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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.StringTools;
import org.riverock.webmill.admin.bean.*;
import org.riverock.webmill.admin.dao.DaoFactory;
import org.riverock.webmill.admin.dao.WebmillAdminDao;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class CreateSiteService {
    private final static Logger log = Logger.getLogger(CreateSiteService.class);

    public static final String SITE_CONFIG_DIR = "/WEB-INF/webmill/admin";
    private static final int MAX_BINARY_FILE_SIZE = 0x4000;

    public static Long createSite(SiteExtended siteExtended) {

        try {

            WebmillAdminDao dao = DaoFactory.getWebmillAdminDao();

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

            //Create new site
            SiteBean siteBean = new SiteBean();
            siteBean.setSiteName(siteExtended.getSite().getSiteName());
            siteBean.setCssDynamic(false);
            siteBean.setCssFile("/default.css");
            // default locale allways 'en'
            Locale locale = Locale.ENGLISH;
            siteBean.setDefCountry(locale.getCountry());
            siteBean.setDefLanguage(locale.getLanguage());
            siteBean.setDefVariant(locale.getVariant());
            siteBean.setCompanyId(siteExtended.getSite().getCompanyId());
            Long siteId = dao.createSite(siteBean);

            if (log.isDebugEnabled()) {
                log.debug("    siteId: " + siteId);
            }
            List<VirtualHostBean> hosts = dao.getVirtualHosts(siteId);
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
                    virtualHost.setSiteId(siteId);
                    dao.createVirtualHost(virtualHost);
                }
            }

            boolean isEnglishLocale=false;
            for (String localeNameTemp : siteExtended.getLocaleList()) {
                Locale localeTemp = StringTools.getLocale(localeNameTemp);
                String localeName = localeTemp.toString();
                if (localeName.equalsIgnoreCase(Locale.ENGLISH.toString())) {
                    isEnglishLocale=true;
                }
                SiteLanguageBean siteLanguage = new SiteLanguageBean(localeName, localeName);
                createSiteLanguage(dao, siteId, siteLanguage);
            }
            if (!isEnglishLocale) {
                SiteLanguageBean siteLanguage = new SiteLanguageBean(Locale.ENGLISH.toString(), Locale.ENGLISH.toString());
                createSiteLanguage(dao, siteId, siteLanguage);
            }

            return siteId;
        }
        catch (IOException e) {
            String es = "Error create new site";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    private static void createSiteLanguage(WebmillAdminDao dao, Long siteId, SiteLanguageBean siteLanguageConfig) throws IOException {
        SiteLanguageBean siteLanguage = dao.getSiteLanguage(siteId, siteLanguageConfig.getLocale());
        if (siteLanguage == null) {
            SiteLanguageBean siteLanguageBean = new SiteLanguageBean();
            siteLanguageBean.setSiteId(siteId);
            siteLanguageBean.setLocale(siteLanguageConfig.getLocale());
            siteLanguageBean.setNameCustomLanguage(siteLanguageConfig.getLocale());
            siteLanguageBean.setSiteLanguageId( dao.createSiteLanguage(siteLanguageBean) );
            siteLanguage = siteLanguageBean;
        }

        List<TemplateBean> templates = new ArrayList<TemplateBean>();
        siteLanguage.setTemplates(templates);
        String indexTemplateFile =
            PropertiesProvider.getApplicationPath()+
                SITE_CONFIG_DIR+ File.separatorChar+
                "index-template.xml";
        String indexTemplate = readBinaryFile(indexTemplateFile);
        TemplateBean indexTemplateBean = new TemplateBean(siteLanguage.getSiteLanguageId(), "index", indexTemplate, false);
        templates.add( indexTemplateBean);

        String dynamicTemplateFile =
            PropertiesProvider.getApplicationPath()+
                SITE_CONFIG_DIR+File.separatorChar+
                "dynamic-template.xml";
        String dynamicTemplate = readBinaryFile(dynamicTemplateFile);
        TemplateBean dynamicTemplateBean = new TemplateBean(siteLanguage.getSiteLanguageId(), "dynamic", dynamicTemplate, true);
        templates.add( dynamicTemplateBean);

        for (TemplateBean templateItem : templates) {
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
                templateItem.setTemplateId(dao.createTemplate(templateItem));
            }
        }
        String xsltFile =
            PropertiesProvider.getApplicationPath()+
                SITE_CONFIG_DIR+File.separatorChar+
                "default.xslt";
        String xsltContent = readBinaryFile(xsltFile);

        XsltBean xslt = new XsltBean();
        xslt.setSiteLanguageId(siteLanguage.getSiteLanguageId());
        xslt.setCurrent(true);
        xslt.setName("Default XSLT. "+new Date());
        xslt.setXsltData(xsltContent);
        DaoFactory.getWebmillAdminDao().createXslt(xslt);

        String catalogCode = "MAIN";
        CatalogLanguageBean catalogLanguageItem = dao.getCatalogLanguageItem(catalogCode, siteLanguage.getSiteLanguageId());
        if (catalogLanguageItem == null) {
            catalogLanguageItem = new CatalogLanguageBean();
            catalogLanguageItem.setDefault(true);
            catalogLanguageItem.setCatalogCode(catalogCode);
            catalogLanguageItem.setSiteLanguageId(siteLanguage.getSiteLanguageId());
            catalogLanguageItem.setCatalogLanguageId( dao.createCatalogLanguageItem(catalogLanguageItem) );
        }

        Map<String, Long> portlets = new HashMap<String, Long>();
        String millIndexPortlet = "mill.index";
        String webmillPortalManagerPortlet = "webmill.portal-manager";
        String millLoginPortlet = "mill.login";
        String millLogoutPortlet = "mill.logout";

        initPortletName(dao, millIndexPortlet, portlets);
        initPortletName(dao, webmillPortalManagerPortlet, portlets);
        initPortletName(dao, millLoginPortlet, portlets);
        initPortletName(dao, millLogoutPortlet, portlets);

        List<CatalogItemBean> menuList = new ArrayList<CatalogItemBean>();
        menuList.add(
            new CatalogItemBean(
                portlets.get(millIndexPortlet), indexTemplateBean.getTemplateId(), catalogLanguageItem.getCatalogLanguageId(), 10, "Homepage", "homepage"
            )
        );
        menuList.add(
            new CatalogItemBean(
                portlets.get(webmillPortalManagerPortlet), dynamicTemplateBean.getTemplateId(), catalogLanguageItem.getCatalogLanguageId(), 20, "Webmill manager", "webmill-manager"
            )
        );
        menuList.add(
            new CatalogItemBean(
                portlets.get(millLoginPortlet), dynamicTemplateBean.getTemplateId(), catalogLanguageItem.getCatalogLanguageId(), 30, "Login", "login"
            )
        );
        menuList.add(
            new CatalogItemBean(
                portlets.get(millLogoutPortlet), dynamicTemplateBean.getTemplateId(), catalogLanguageItem.getCatalogLanguageId(), 40, "Logout", "logout"
            )
        );
        processMenu(siteLanguage, catalogLanguageItem, menuList, 0L);
    }

    private static void initPortletName(WebmillAdminDao dao, String millIndexPortlet, Map<String, Long> portlets) {
        PortletNameBean portletNameBean;
        portletNameBean = dao.getPortletName(millIndexPortlet);
        if (portletNameBean==null) {
            portletNameBean = dao.createPortletName(millIndexPortlet);
        }
        portlets.put( millIndexPortlet,  portletNameBean.getPortletId());
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
            PortletNameBean portletName = DaoFactory.getWebmillAdminDao().getPortletName(menuItem.getPortletId());
            if (portletName==null) {
                throw new IllegalStateException("Portlet name not found for ID "+menuItem.getPortletId());
            }

            TemplateBean template = DaoFactory.getWebmillAdminDao().getTemplate(menuItem.getTemplateId(), siteLanguage.getSiteLanguageId());
            if (template==null) {
                throw new IllegalStateException("Template not found for ID "+menuItem.getTemplateId());
            }

            Long catalogItemId = DaoFactory.getWebmillAdminDao().getCatalogItemId(
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
                catalogBean.setOrderField(menuItem.getOrderField());

                catalogItemId = DaoFactory.getWebmillAdminDao().createCatalogItem(catalogBean);
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
