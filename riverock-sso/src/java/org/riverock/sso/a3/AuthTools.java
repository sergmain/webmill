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

/**
 * $Id$
 */
package org.riverock.sso.a3;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.portlet.PortletRequest;

import org.riverock.sso.main.Constants;

import org.apache.log4j.Logger;

public class AuthTools
{
    private static Logger log = Logger.getLogger(AuthTools.class);

    public static AuthSession check(PortletRequest request, HttpServletResponse response, String defURL)
        throws AuthException{
        return check(getAuthSession(request), response, defURL, request.getServerName());
    }

    public static AuthSession check(HttpServletRequest request, HttpServletResponse response, String defURL)
        throws AuthException{
        return check(getAuthSession(request), response, defURL, request.getServerName());
    }

    public static AuthSession check(AuthSession authSession, HttpServletResponse response, String defURL, String serverName)
        throws AuthException{
        if (log.isDebugEnabled())
            log.debug("AUTH_SESSION - " + authSession);

        if (authSession == null){

            // work arounf java.lang.IllegalStateException
            // XXX это исключение возникает из-за того что делается response.sendRedirect()
            // не в оригиральном объекте, а в клоне.
            // отследить какие объекты генерируют это исключение
            long currentTimeMills = System.currentTimeMillis();
            for (int i = 0; i < 5; i++){
                try{
                    response.sendRedirect(defURL);
                    break;
                }
                catch (IOException e){
                    log.error("IOException auth check. Send redirect failed ", e);
                    throw new AuthException( e.toString() );
                }
                catch (IllegalStateException e){
                    if (i == 4) { // last loop
                        log.error("IllegalStateException auth check. Send redirect failed ", e);
                        throw new AuthException(e.toString());
                    }
                    else{
                        if (log.isInfoEnabled())
                            log.info("Exception  IllegalStateException processed. Loop - " + (i + 1));

                        // wait for 3 second
                        while ((System.currentTimeMillis() - currentTimeMills) < 3000)
                            for (int j = 0; j < 50; j++) ;
                    }
                }
            }

            return null;
        }

        try{
            if (!authSession.checkAccess( serverName )){
                if (log.isDebugEnabled())
                    log.debug("redirect to - " + defURL);

                response.sendRedirect(defURL);
                return null;
            }
        }
        catch (Exception e){
            log.error("Exception auth check. Access denied.", e);
            throw new AuthException(e.toString());
        }
        return authSession;
    }

    public static AuthInfo getAuthInfo(PortletRequest request)
        throws AuthException {

        AuthSession authSession = getAuthSession(request);
        if (authSession == null)
            return null;

        return InternalAuthProvider.getAuthInfo( authSession );
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
