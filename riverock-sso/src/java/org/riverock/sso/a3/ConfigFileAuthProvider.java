/*
 * org.riverock.sso -- Single Sign On implementation
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
package org.riverock.sso.a3;

import java.io.Serializable;

import org.riverock.sso.schema.config.AuthProviderParametersListType;

/**
 * User: Admin
 * Date: Sep 9, 2003
 * Time: 2:55:25 PM
 *
 * $Id$
 */
public final class ConfigFileAuthProvider implements AuthProviderInterface, Serializable {
    private static final long serialVersionUID = 20434672384237105L;

    public boolean isUserInRole(final AuthSession authSession, String role) {
        return false;
    }

    public boolean checkAccess(final AuthSession authSession, final String serverName) {
        return false;
    }

    public void setParameters(final AuthProviderParametersListType params) throws Exception {
    }

    public void initUserInfo(final AuthSession authSession) {
    }
}
