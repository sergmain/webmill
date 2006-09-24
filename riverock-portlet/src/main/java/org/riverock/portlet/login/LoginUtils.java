/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.login;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.PortalSessionManager;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 2, 2002
 * Time: 4:42:13 PM
 * 
 * $Id$
 */
public final class LoginUtils {
    private final static Logger log = Logger.getLogger( LoginUtils.class );

    static final String CTX_TYPE_LOGIN_PLAIN = "mill.login_plain";
    static final String CTX_TYPE_LOGIN_XML = "mill.login_xml";
    public static final String NAME_USERNAME_PARAM = "mill.username";
    public static final String NAME_PASSWORD_PARAM = "mill.password";
    public static final String NAME_TOURL_PARAM = "mill.tourl";
    public static final String LOGIN_CHECK_PORTLET = "mill.login_check";

    public static void check( final ActionRequest actionRequest, final ActionResponse actionResponse )
        throws IOException {

        String login = actionRequest.getParameter( NAME_USERNAME_PARAM );
        String password = actionRequest.getParameter( NAME_PASSWORD_PARAM );
        String url = PortletService.getString(actionRequest, NAME_TOURL_PARAM, null);
        if( url != null ) {
            url = actionResponse.encodeURL( url );
        }
        if( log.isDebugEnabled() ) {
            log.debug( "userLogin: " + ( login != null ? (login + ", class: " + login.getClass().getName() ) : "null" ) );
            log.debug( "userPassword: " + ( password != null ? (password + ", class: " + password.getClass().getName() ) : "null" ) );
            log.debug( "URL #3: " + url );
        }

        if ( StringUtils.isBlank( login ) || StringUtils.isBlank( password )) {
            return;
        }

        PortalSessionManager sessionManager = (PortalSessionManager)actionRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_SESSION_MANAGER );

        if (log.isDebugEnabled()) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            log.debug("LoginUtils classLoader: "+cl+"\nhash: "+cl.hashCode() );
            ClassLoader ccl = sessionManager.getClass().getClassLoader();
            log.debug("PortalSessionManager impl classLoader: "+ccl+"\nhash: "+ccl.hashCode() );
        }

        boolean checkAccess = sessionManager.createSession( login, password );
        if (log.isDebugEnabled()) {
            log.debug("checkAccess: " + checkAccess);
        }

        if( checkAccess ) {
            if( url != null ) {
                actionResponse.sendRedirect( url );
            }
            return;
        }

        long currentTimeMills = System.currentTimeMillis();

        if( log.isDebugEnabled() )
            log.debug( "mills " + currentTimeMills );

        try {
            Thread.sleep( 3000 );
        }
        catch( InterruptedException e ) {
            // catch InterruptedException
        }

        if( log.isDebugEnabled() )
            log.debug( "mills " + System.currentTimeMillis() );
    }
}
