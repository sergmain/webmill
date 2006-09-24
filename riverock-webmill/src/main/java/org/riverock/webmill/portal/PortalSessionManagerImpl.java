/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal;

import javax.portlet.ActionRequest;
import javax.portlet.PortletSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.PortalSessionManager;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthSessionImpl;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 14:31:31
 *         $Id$
 */
public class PortalSessionManagerImpl implements PortalSessionManager {
    private final static Logger log = Logger.getLogger( PortalSessionManagerImpl.class );
    private ActionRequest actionRequest = null;
    private ClassLoader classLoader = null;

    PortalSessionManagerImpl( ClassLoader classLoader, ActionRequest actionRequest ) {
        this.actionRequest = actionRequest;
        this.classLoader = classLoader;
    }

    public boolean createSession(String userLogin, String userPassword) {

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );

            if (log.isDebugEnabled()) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                log.debug("PortalSessionManagerImpl class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
            }

            PortletSession session = actionRequest.getPortletSession();
            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();

            // don't check if login or password is null.
            if ( StringUtils.isBlank(userLogin) || StringUtils.isBlank(userLogin) ) {
                return false;
            }

            if (auth_==null) {
                auth_ = new AuthSessionImpl( userLogin, userPassword );
            }

            if (log.isDebugEnabled()) {
                ClassLoader cl = auth_.getClass().getClassLoader();
                log.debug("AuthSessionImpl class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
            }

            final boolean checkAccess = auth_.checkAccess( actionRequest.getServerName() );
            if ( log.isDebugEnabled() ) {
                log.debug(
                    "checkAccess status for server name '"+actionRequest.getServerName()+"' " +
                        " is " + checkAccess + ", auth: " + auth_ );
            }
            if (checkAccess) {
                session.setAttribute( org.riverock.sso.main.Constants.AUTH_SESSION, auth_, PortletSession.APPLICATION_SCOPE);
                return true;
            }
            else {
                session.removeAttribute( org.riverock.sso.main.Constants.AUTH_SESSION, PortletSession.APPLICATION_SCOPE );
                return false;
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
