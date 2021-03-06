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
package org.riverock.portlet.main;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.FacesTools;

public class AuthSessionBean implements Serializable {
    private static final long serialVersionUID = 2055005503L;

    private AuthSession authSession = null;

    public AuthSessionBean() {
        this.authSession = ( AuthSession ) FacesTools.getUserPrincipal();
    }

    public AuthSession getAuthSession() {
        return authSession;
    }

    public String getName() {
        return authSession!=null?authSession.getName():null;
    }

    public String getAddress() {
        return authSession!=null?authSession.getUser().getAddress():null;
    }

    public Long getId() {
        return authSession!=null?authSession.getUser().getUserId():null;
    }

    public String getUserLogin() {
        return authSession!=null?authSession.getUserLogin():null;
    }

    public boolean isCompany() {
        return authSession != null && authSession.getAuthInfo().isCompany();
    }

    public boolean isHolding() {
        return authSession != null && authSession.getAuthInfo().isHolding();
    }

    public Long getHoldingId() {
        return authSession!=null?authSession.getAuthInfo().getHoldingId():null;
    }

}
