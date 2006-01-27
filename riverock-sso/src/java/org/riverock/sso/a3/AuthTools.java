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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.portlet.PortletRequest;

import org.riverock.sso.main.Constants;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthInfo;

/**
 * $Id$
 */
public final class AuthTools {

    public static AuthInfo getAuthInfo(PortletRequest request) {

        AuthSession authSession = getAuthSession(request);
        if (authSession == null)
            return null;

        return authSession.getAuthInfo();
    }

    public static AuthSession getAuthSession(HttpSession session){
        return (AuthSession) session.getAttribute(Constants.AUTH_SESSION);
    }

    public static AuthSession getAuthSession(PortletRequest request){
        return (AuthSession) request.getPortletSession(true).getAttribute(Constants.AUTH_SESSION);
    }

    public static AuthSession getAuthSession(HttpServletRequest request){
        return (AuthSession) request.getSession(true).getAttribute(Constants.AUTH_SESSION);
    }
}
