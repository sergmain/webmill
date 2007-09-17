/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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

    public PortalSessionManagerImpl( ClassLoader classLoader, ActionRequest actionRequest ) {
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

            // don't check if login or password is null.
            if ( StringUtils.isBlank(userLogin) || StringUtils.isBlank(userLogin) ) {
                return false;
            }

            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();

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
                PortletSession session = actionRequest.getPortletSession(true);
                session.setAttribute( org.riverock.sso.main.Constants.AUTH_SESSION, auth_, PortletSession.APPLICATION_SCOPE);
                return true;
            }
            else {
                PortletSession session = actionRequest.getPortletSession(false);
                if (session!=null) {
                    session.removeAttribute( org.riverock.sso.main.Constants.AUTH_SESSION, PortletSession.APPLICATION_SCOPE );
                }
                return false;
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
