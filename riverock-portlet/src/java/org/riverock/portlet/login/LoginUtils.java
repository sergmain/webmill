/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.login;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.portlet.main.Constants;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.portlet.PortletTools;

/**
 * Author: mill
 * Date: Dec 2, 2002
 * Time: 4:42:13 PM
 *
 * $Id$
 */
public final class LoginUtils {
    private final static Logger log = Logger.getLogger( LoginUtils.class );

    static final String CTX_TYPE_LOGIN_PLAIN  = "mill.login_plain";
    static final String CTX_TYPE_LOGIN_XML  = "mill.login_xml";

    public static void check(final ActionRequest actionRequest, final ActionResponse actionResponse)
        throws PortletException {

        try {
            PortletSession session = actionRequest.getPortletSession();
            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();

            String l_ = actionRequest.getParameter( Constants.NAME_USERNAME_PARAM );
            String p_ = actionRequest.getParameter( Constants.NAME_PASSWORD_PARAM );
            if (log.isDebugEnabled()) {
                log.debug("userLogin: " + (l_!=null? (l_.toString()+", class: "+l_.getClass().getName()) :"null") );
                log.debug("userPassword: " + (p_!=null? (p_.toString()+", class: "+p_.getClass().getName()) :"null") );
            }

            // don't check if login or password is null. 
            if ( StringTools.isEmpty(l_) || StringTools.isEmpty(p_) ) {
                return;
            }

            if (auth_==null) {
                auth_ = new AuthSession( l_, p_ );
            }

            final boolean checkAccess = auth_.checkAccess( actionRequest.getServerName() );
            if ( log.isDebugEnabled() ) {
                log.debug(
                    "checkAccess status for server name '"+actionRequest.getServerName()+"' " +
                    " is " + checkAccess + ", auth: " + auth_ );
            }
            if (checkAccess) {
                session.setAttribute( Constants.AUTH_SESSION, auth_);

                String url = PortletTools.getString( actionRequest, Constants.NAME_TOURL_PARAM );
                if(log.isDebugEnabled()) {
                    log.debug("URL #2: "+url);
                }

                if ( url!=null ) {
                    url = actionResponse.encodeURL( url );

                    if (log.isDebugEnabled()) {
                        log.debug("URL #3: "+url);
                    }

                    actionResponse.sendRedirect( url );
                }
                return;
            }

            // User not correctly input authorization data
            // remove all objects from session
            PortletTools.cleanSession( session );

            long currentTimeMills = System.currentTimeMillis();

            if(log.isDebugEnabled())
                log.debug("mills "+currentTimeMills);
/*
            while ( (System.currentTimeMillis() - currentTimeMills)<3000)
                for (int i=0; i<50; i++);

*/

try {
  Thread.sleep(3000);
}
catch(InterruptedException e){}

            if(log.isDebugEnabled())
                log.debug("mills "+System.currentTimeMillis());

            session.invalidate();
            return;

        }
        catch (Exception e) {
            final String es = "LoginUtils exception: ";
            log.error( es, e);
            throw new PortletException( es, e);
        }
    }
}
