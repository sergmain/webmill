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

import org.apache.log4j.Logger;

import org.riverock.webmill.admin.bean.SiteExtended;
import org.riverock.webmill.admin.service.SiteService;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class SiteDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(SiteDataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private SiteService siteService=null;
    private SiteSessionBean siteSessionBean = null;

    private SiteExtended siteExtended = null;

    public SiteDataProvider() {
    }

    public SiteSessionBean getSiteSessionBean() {
        return siteSessionBean;
    }

    public void setSiteSessionBean(SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public SiteExtended getSiteExtended() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + siteSessionBean.getObjectType());
        }
        Long siteId = siteSessionBean.getId();
        if (siteExtended==null) {
            siteExtended = siteService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended = siteService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }
}
