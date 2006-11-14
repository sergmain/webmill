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
package org.riverock.portlet.manager.users;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.User;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortalUserSessionBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

    private User portalUser = null;
    private Long currentPortalUserId = null;

    public PortalUserSessionBean() {
    }

    public User getPortalUser() {
        return portalUser;
    }

    public void setPortalUser(User portalUser) {
        this.portalUser = portalUser;
    }

    public Long getCurrentPortalUserId() {
        return currentPortalUserId;
    }

    public void setCurrentPortalUserId(Long currentPortalUserId) {
        this.currentPortalUserId = currentPortalUserId;
    }
}
