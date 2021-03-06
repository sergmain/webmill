/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.module.web.user;

import java.security.Principal;

import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 16:56:56
 *         $Id: WebmillModuleUserImpl.java 1056 2006-11-15 19:02:57Z serg_main $
 */
public class WebmillModuleUserImpl implements ModuleUser {

    private AuthSession authSession = null;
    public WebmillModuleUserImpl(Principal userPrincipal) {
        this.authSession = (AuthSession) userPrincipal;
    }

    public String getName() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getName();
        }
    }

    public String getAddress() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUser().getAddress();
        }
    }

    public Long getId() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUser().getUserId();
        }
    }

    public String getUserLogin() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserLogin();
        }
    }

    public boolean isCompany() {
        return authSession.getAuthInfo().isCompany();
    }

    public boolean isHolding() {
        return authSession.getAuthInfo().isHolding();
    }
}
