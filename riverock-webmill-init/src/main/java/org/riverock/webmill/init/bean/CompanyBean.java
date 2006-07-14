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
package org.riverock.webmill.init.bean;

import java.io.Serializable;

import org.riverock.webmill.init.utils.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class CompanyBean implements Serializable {
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

    public CompanyBean(CompanyBean company) {
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
