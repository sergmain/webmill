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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.webmill.admin.bean.*;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 17:26:38
 */
public class HibernatedWebmillAdminDaoImpl implements WebmillAdminDao {
    private final static Logger log = Logger.getLogger(HibernatedWebmillAdminDaoImpl.class);

    public List<CompanyBean> getCompanyList() {
        List<CompanyBean> list = new ArrayList<CompanyBean>();
        List<Company> companies = InternalDaoFactory.getInternalCompanyDao().getCompanyList_notRestricted();
        for (Company company : companies) {
            list.add( new CompanyBean(company) );
        }
        return list;
    }

    public CompanyBean getCompany(Long companyId) {
        Company company = InternalDaoFactory.getInternalCompanyDao().getCompany_notRestricted(companyId);
        if (company==null)
            return null;
        return new CompanyBean(company);
    }

    public Long processAddCompany(CompanyBean companyBean) {
        return InternalDaoFactory.getInternalCompanyDao().processAddCompany(companyBean, null);
    }

    public void processSaveCompany(CompanyBean company) {
        InternalDaoFactory.getInternalCompanyDao().processSaveCompany_notRestricted(company);
    }

    public void processDeleteCompany(CompanyBean company) {
        InternalDaoFactory.getInternalCompanyDao().processDeleteCompany_notRestricted(company);
    }

    //// Portal user section

    public List<UserBean> getUserList() {
        List<UserBean> list = new ArrayList<UserBean>();
        List<User> users = InternalDaoFactory.getInternalUserDao().getUserList_notRestricted();
        for (User user : users) {
            list.add( new UserBean(user) );
        }
        return list;
    }

    public UserBean getUser(Long userId) {
        User user = InternalDaoFactory.getInternalUserDao().getUser_notRestricted(userId);
        if (user==null)
            return null;
        return new UserBean(user);
    }

    public Long addUser(final UserBean userBean) {
        final Long userId = InternalDaoFactory.getInternalUserDao().addUser(userBean);
        AuthInfo authInfo = new AuthInfo() {
            public Long getAuthUserId() {
                return null;
            }

            public Long getUserId() {
                return userId;
            }

            public Long getCompanyId() {
                return userBean.getCompanyId();
            }

            public Long getHoldingId() {
                return null;
            }

            public String getUserLogin() {
                return userBean.getLogin();
            }

            public String getUserPassword() {
                return userBean.getPassword1();
            }

            public boolean isCompany() {
                return true;
            }

            public boolean isHolding() {
                return false;
            }

            public boolean isRoot() {
                return true;
            }
        };
        InternalDaoFactory.getInternalAuthDao().addUserInfo( authInfo, new ArrayList<RoleEditableBean>(), userBean.getCompanyId(), null);
        return userId;
    }

    public void updateUser(UserBean portalUser) {
        InternalDaoFactory.getInternalUserDao().updateUser_notRestricted(portalUser);
    }

    public void deleteUser(UserBean portalUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //// Site section

    public List<SiteBean> getSites() {
        List<SiteBean> list = new ArrayList<SiteBean>();
        List<Site> sites = InternalDaoFactory.getInternalSiteDao().getSites();
        for (Site site : sites) {
            list.add( new SiteBean(site) );
        }
        return list;
    }

    public SiteBean getSite(Long siteId) {
        Site site = InternalDaoFactory.getInternalSiteDao().getSite(siteId);
        if (site==null)
            return null;
        return new SiteBean(site);
    }

    public List<VirtualHostBean> getVirtualHosts(Long siteId) {
        List<VirtualHostBean> list = new ArrayList<VirtualHostBean>();
        List<VirtualHost> hosts = InternalDaoFactory.getInternalVirtualHostDao().getVirtualHosts(siteId);
        for (VirtualHost host : hosts) {
            list.add( new VirtualHostBean(host) );
        }
        return list;
    }

    public void updateSiteWithVirtualHost(SiteBean site, List<VirtualHost> virtualHosts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteSite(Long siteId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteWithVirtualHost(SiteBean site, List<VirtualHost> virtualHosts) {
        return InternalDaoFactory.getInternalSiteDao().createSite(site, virtualHosts);
    }

    public List<VirtualHostBean> getVirtualHostsFullList() {
        List<VirtualHostBean> list = new ArrayList<VirtualHostBean>();
        List<VirtualHost> hosts = InternalDaoFactory.getInternalVirtualHostDao().getVirtualHostsFullList();
        for (VirtualHost host : hosts) {
            list.add( new VirtualHostBean(host) );
        }
        return list;
    }

    public Long createSite(SiteBean siteBean) {
        return createSiteWithVirtualHost(siteBean, null);
    }

    public Long createXslt(XsltBean xslt) {
        return InternalDaoFactory.getInternalXsltDao().createXslt(xslt);
    }

    public SiteLanguageBean getSiteLanguage(Long siteId, String locale) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteLanguage(SiteLanguageBean siteLanguageBean) {
        return InternalDaoFactory.getInternalSiteLanguageDao().createSiteLanguage(siteLanguageBean);
    }

    public TemplateBean getTemplate(String templateName, Long siteLanguageId) {
        Template template = InternalDaoFactory.getInternalTemplateDao().getTemplate(templateName, siteLanguageId);
        if (template==null)
            return null;
        return new TemplateBean(template);
    }

    public Long createTemplate(TemplateBean template) {
        org.riverock.webmill.portal.bean.TemplateBean bean = new org.riverock.webmill.portal.bean.TemplateBean(template);
        return InternalDaoFactory.getInternalTemplateDao().createTemplate(bean);
    }

    public Long createPortletName(PortletNameBean portletNameBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletId, Long templateId) {

        if (log.isDebugEnabled()) {
            log.debug("InternalDaoCatalogImpl.getCatalogItemId()");
            log.debug("     siteLanguageId: " + siteLanguageId);
            log.debug("     portletId: " + portletId);
            log.debug("     templateId: " + templateId);
        }
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteLanguageId, portletId, templateId);
    }

    public Long createCatalogItem(CatalogBean item) {
        return InternalDaoFactory.getInternalCatalogDao().createCatalogItem(item);
    }

    public Long createVirtualHost(VirtualHostBean virtualHost) {
        return InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(virtualHost);
    }

    public XsltBean getCurrentXslt(Long siteLanguageId) {
        Xslt currentXslt = InternalDaoFactory.getInternalXsltDao().getCurrentXslt(siteLanguageId);
        if (currentXslt==null)
            return null;
        return new XsltBean(currentXslt);
    }

    public Long createCatalogLanguageItem(CatalogLanguageBean catalogLanguageItem) {
        return InternalDaoFactory.getInternalCatalogDao().createCatalogLanguageItem(catalogLanguageItem);
    }

    public SiteBean getSite(String siteName) {
        Site site = InternalDaoFactory.getInternalSiteDao().getSite(siteName);
        if (site==null)
            return null;
        return new SiteBean(site);
    }

    public XsltBean getXslt(String name, Long siteLanguageId) {
        Xslt xslt = InternalDaoFactory.getInternalXsltDao().getXslt(name, siteLanguageId);
        if (xslt==null)
            return null;
        return new XsltBean(xslt);
    }

    public CatalogLanguageBean getCatalogLanguageItem(String catalogCode, Long siteLanguageId) {
        CatalogLanguageItem catalogLanguageItem = InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItem(catalogCode, siteLanguageId);
        if (catalogLanguageItem==null)
            return null;
        return new CatalogLanguageBean(catalogLanguageItem);
    }

    public PortletNameBean getPortletName(Long portletId) {
        PortletName portletName = InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletId);
        if (portletName==null)
            return null;
        return new PortletNameBean(portletName);
    }

    public TemplateBean getTemplate(Long templateId, Long siteLanguageId) {
        Template template = InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
        if (template==null)
            return null;
        return new TemplateBean(template);
    }

    public PortletNameBean getPortletName(String portletName) {
        PortletName bean = InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletName);
        if (bean==null)
            return null;
        return new PortletNameBean(bean);
    }

    public PortletNameBean createPortletName(String portletName) {
        PortletNameBean bean = new PortletNameBean();
        bean.setPortletName(portletName);
        Long portletId = InternalDaoFactory.getInternalPortletNameDao().createPortletName(bean);
        return new PortletNameBean( InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletId) );
    }
}
