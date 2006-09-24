/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.sso.a3;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.log4j.Logger;

import org.riverock.sso.main.Constants;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthInfo;

/**
 * $Id$
 */
public final class AuthTools {
    private final static Logger log = Logger.getLogger( AuthTools.class );

    public static AuthInfo getAuthInfo(PortletRequest request) {

        AuthSession authSession = getAuthSession(request);
        if (authSession == null)
            return null;

        return authSession.getAuthInfo();
    }

    public static AuthSession getAuthSession(HttpSession session){
        if (session instanceof PortletSession) {
            return (AuthSession) ((PortletSession)session).getAttribute(Constants.AUTH_SESSION, PortletSession.APPLICATION_SCOPE);
        }
        else {
            log.debug("Search auth session object in HttpSession");
            return (AuthSession) session.getAttribute(Constants.AUTH_SESSION);
        }
    }

    public static AuthSession getAuthSession(PortletRequest request){
        return (AuthSession) request.getPortletSession(true).getAttribute(Constants.AUTH_SESSION, PortletSession.APPLICATION_SCOPE);
    }

    public static AuthSession getAuthSession(HttpServletRequest request){
        return getAuthSession(request.getSession(true));
    }
}
