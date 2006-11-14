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
package org.riverock.webmill.admin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class SiteExtended implements Serializable {
    private static final long serialVersionUID = 2058005301L;

    private SiteBean site = null;
    private List<String> virtualHosts = null;
    private CompanyBean company = null;
    private List<SiteLanguageBean> siteLanguage = null;
    private String locales = null;

    public SiteExtended(){
    }

    public SiteExtended(SiteBean siteBean, List<String> virtualHosts, CompanyBean company){
        this.site=siteBean;
        this.virtualHosts=virtualHosts;
        this.company=company;
    }

    public String getLocales() {
        return locales;
    }

    public void setLocales(String locales) {
        this.locales = locales;
    }

    public List<String> getLocaleList() {
        List<String> localeList = new ArrayList<String>();
        for (StringTokenizer stringTokenizer = new StringTokenizer(locales, ", "); stringTokenizer.hasMoreTokens();) {
            String localeName = stringTokenizer.nextToken().trim();
            localeList.add(localeName);
        }
        return localeList;
    }

    public List<SiteLanguageBean> getSiteLanguage() {
        return siteLanguage;
    }

    public void setSiteLanguage(List<SiteLanguageBean> siteLanguage) {
        this.siteLanguage = siteLanguage;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = new CompanyBean(company);
    }

    public SiteBean getSite() {
        return site;
    }

    public void setSite(SiteBean site) {
        this.site = new SiteBean(site);
    }

    public List<String> getVirtualHosts() {
        return virtualHosts;
    }

    public void setVirtualHosts(List<String> virtualHosts) {
        this.virtualHosts = virtualHosts;
    }
}
