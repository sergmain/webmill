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
package org.riverock.webmill.admin;

import java.io.Serializable;

import org.riverock.webmill.admin.bean.SiteExtended;
import org.riverock.webmill.admin.utils.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class SiteSessionBean implements Serializable {
    private static final long serialVersionUID = 2058005508L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SITE_TYPE = 1;

    private boolean isEdit;
    private Long id = null;
    private int objectType=0;

    private SiteExtended siteExtended = null;

    private String currentVirtualHost = null;
    private String newVirtualHost = null;
    private Long currentSiteId=null;

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public String getCurrentVirtualHost() {
        return currentVirtualHost;
    }

    public void setCurrentVirtualHost(String currentVirtualHost) {
//        this.currentVirtualHost = FacesTools.convertParameter(currentVirtualHost);
        this.currentVirtualHost = currentVirtualHost;
    }

    public String getNewVirtualHost() {
        return newVirtualHost;
    }

    public void setNewVirtualHost(String newVirtualHost) {
        this.newVirtualHost = FacesTools.convertParameter(newVirtualHost);
    }

    public SiteExtended getSiteExtended() {
        return siteExtended;
    }

    public void setSiteExtended(SiteExtended siteExtended) {
        this.siteExtended = siteExtended;
    }

    public int getSiteType() {
        return SITE_TYPE;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
