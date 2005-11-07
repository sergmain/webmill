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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.tools.XmlTools;

import org.riverock.portlet.schema.portlet.login.LoginType;
import org.riverock.portlet.schema.portlet.login.types.LoginTypeIsLoggedType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: Admin
 * Date: Aug 24, 2003
 * Time: 6:39:51 PM
 *
 * $Id$
 */
public final class LoginXmlPortlet implements Portlet {
    private final static Log log = LogFactory.getLog( LoginXmlPortlet.class );

    public LoginXmlPortlet() {
    }

    private PortletConfig portletConfig = null;

    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
    }

    public void processAction( final ActionRequest actionRequest, final ActionResponse actionResponse ) throws PortletException {
        LoginUtils.check(actionRequest, actionResponse);
    }

    public void render( final RenderRequest renderRequest, final RenderResponse renderResponse ) throws PortletException, IOException {

        OutputStream out = null;
        try {
            out = renderResponse.getPortletOutputStream();
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );

            if ( log.isDebugEnabled() ) {
                log.debug( "Process input auth data" );
            }

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();

            LoginType login = new LoginType();
            login.setPortletName( LoginUtils.CTX_TYPE_LOGIN_XML );
            if ( auth_ != null && auth_.checkAccess( renderRequest.getServerName() ) ) {
                if ( log.isDebugEnabled() )
                    log.debug( "user " + auth_.getUserLogin() + " is  valid for " + renderRequest.getServerName() + " site" );

                login.setIsLogged( LoginTypeIsLoggedType.VALUE_1 );
                login.setInviteMessage( "User already logged in." );
                login.setUserName( auth_.getName() );
            }
            else {

                String srcURL = null;
                if ( renderRequest.getParameter( LoginUtils.NAME_TOURL_PARAM ) != null ) {
                    srcURL = RequestTools.getString( renderRequest, LoginUtils.NAME_TOURL_PARAM );
                }
                else {
                    srcURL = PortletService.url( ContainerConstants.CTX_TYPE_INDEX, renderRequest, renderResponse );
                }

                srcURL = StringTools.replaceString( srcURL, "%3D", "=" );
                srcURL = StringTools.replaceString( srcURL, "%26", "&" );

                if ( log.isDebugEnabled() ) {
                    log.debug( "reqeust parameter  mill.tourl: " + renderRequest.getParameter( LoginUtils.NAME_TOURL_PARAM ) );
                    log.debug( "toURL: " + srcURL );
                    log.debug( "encoded toURL - " + srcURL );
                    log.debug( "Header string - " + bundle.getString( "auth.check.header" ) );
                }

                login.setActionUrl( PortletService.ctx( renderRequest ) );
                login.setToUrl( srcURL );
                login.setInviteMessage( bundle.getString( "auth.check.header" ) );
                login.setLoginMessage( bundle.getString( "auth.check.login" ) );
                login.setPasswordMessage( bundle.getString( "auth.check.password" ) );
                login.setButtonMessage( bundle.getString( "auth.check.register" ) );
                login.setIsLogged( LoginTypeIsLoggedType.VALUE_0 );
            }

            String xmlRoot = (String)renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE );
            if (StringTools.isEmpty( xmlRoot ) ) {
                xmlRoot = "LoginXml";
            }

            byte[] bytes = XmlTools.getXml( login, xmlRoot, null, "utf-8");
            out.write( bytes );
        }
        catch( Throwable e ) {
            String es = "Error in render()";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            if (out!=null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
}
