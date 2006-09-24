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

import org.riverock.webmill.admin.utils.FacesTools;

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
