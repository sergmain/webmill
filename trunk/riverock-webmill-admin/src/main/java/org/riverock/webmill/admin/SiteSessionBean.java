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
