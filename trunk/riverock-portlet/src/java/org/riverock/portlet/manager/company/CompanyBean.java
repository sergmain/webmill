/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.portlet.manager.company;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyBean implements Serializable, Company {
    private static final long serialVersionUID = 2055005502L;

    private String name = null;
    private Long id = null;
    private String shortName = null;
    private String address = null;
    private String ceo = null;
    private String cfo = null;
    private String website = null;
    private String info = null;
    private boolean isDeleted = false;

    public CompanyBean() {
    }

    public CompanyBean(Company company) {
        this.name = company.getName();
        this.id = company.getId();
        this.shortName = company.getShortName();
        this.address = company.getAddress();
        this.ceo = company.getCeo();
        this.cfo = company.getCfo();
        this.website = company.getWebsite();
        this.info = company.getInfo();
        this.isDeleted = company.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FacesTools.convertParameter(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = FacesTools.convertParameter(shortName);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = FacesTools.convertParameter(address);
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = FacesTools.convertParameter(ceo);
    }

    public String getCfo() {
        return cfo;
    }

    public void setCfo(String cfo) {
        this.cfo = FacesTools.convertParameter(cfo);
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = FacesTools.convertParameter(website);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = FacesTools.convertParameter(info);
    }
}
