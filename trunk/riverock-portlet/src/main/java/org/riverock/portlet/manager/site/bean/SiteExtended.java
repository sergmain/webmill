/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Site;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 19:33:55
 */
public class SiteExtended implements Serializable {
    private static final long serialVersionUID = 2058005301L;

    private SiteBean site = null;
    private List<VirtualHostBean> virtualHosts = null;
    private CompanyBean company = null;

    public SiteExtended(){
    }

    public SiteExtended(SiteBean siteBean, List<VirtualHostBean> virtualHosts, CompanyBean company){
        this.site=siteBean;
        this.virtualHosts=virtualHosts;
        this.company=company;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = new CompanyBean(company);
    }

    public SiteBean getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = new SiteBean(site);
    }

    public List<VirtualHostBean> getVirtualHosts() {
        return virtualHosts;
    }

    public void setVirtualHosts(List<VirtualHostBean> virtualHosts) {
        this.virtualHosts = virtualHosts;
    }

    public boolean isDefaultHostCheckedNotOnce() {
        if (virtualHosts==null || virtualHosts.isEmpty()) {
            return false;
        }
        int count=0;
        for (VirtualHostBean virtualHost : virtualHosts) {
            if (virtualHost.isDefaultHost()) {
                count++;
            }
        }
        return count!=1;
    }
}
