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
