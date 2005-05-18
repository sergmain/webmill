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

import org.apache.log4j.Logger;

import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.main.Constants;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.wrapper.StreamWrapper;

/**
 * User: Admin
 * Date: Aug 24, 2003
 * Time: 6:39:51 PM
 *
 * $Id$
 */
public final class LoginXmlPortlet implements Portlet {
    private final static Logger log = Logger.getLogger( LoginXmlPortlet.class );

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

        OutputStream outputStream = null;
        try {
            outputStream = renderResponse.getPortletOutputStream();
            StreamWrapper out = new StreamWrapper(outputStream);
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );

            if ( log.isDebugEnabled() )
                log.debug( "Process input auth data" );

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();

            if ( auth_ != null && auth_.checkAccess( renderRequest.getServerName() ) ) {
                if ( log.isDebugEnabled() )
                    log.debug( "user " + auth_.getUserLogin() + " is  valid for " + renderRequest.getServerName() + " site" );

                out.write( "User already logged in." );
                return;
            }

            out.write( "<form method=\"POST\" action=\"" + PortletTools.ctx( renderRequest ) + "\" >\n" );

            out.write( ServletTools.getHiddenItem( Constants.NAME_TYPE_CONTEXT_PARAM,
                LoginUtils.CTX_TYPE_LOGIN_PLAIN ) );

            String srcURL = null;
            if ( renderRequest.getParameter( Constants.NAME_TOURL_PARAM ) != null ) {
                srcURL = PortletTools.getString( renderRequest, Constants.NAME_TOURL_PARAM );
            } else {
                srcURL = PortletTools.url( Constants.CTX_TYPE_INDEX, renderRequest, renderResponse );
            }

            srcURL = StringTools.replaceString( srcURL, "%3D", "=" );
            srcURL = StringTools.replaceString( srcURL, "%26", "&" );

            out.write( ServletTools.getHiddenItem( Constants.NAME_TOURL_PARAM, srcURL ) );

            if ( log.isDebugEnabled() ) {
                log.debug( "reqeust parameter  mill.tourl: " + renderRequest.getParameter( Constants.NAME_TOURL_PARAM ) );
                log.debug( "toURL: " + srcURL );
                log.debug( "encoded toURL - " + srcURL );
                log.debug( "Header string - " + bundle.getString( "auth.check.header" ) );
            }

            out.write( "<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\" width=\"100%\">\n" +
                "<tr><th class=\"formworks\">" + bundle.getString( "auth.check.header" ) + "</th></tr>\n" +
                "<tr><td class=\"formworks\"><input type = \"text\" name = \"" + Constants.NAME_USERNAME_PARAM + "\">&nbsp;" + bundle.getString( "auth.check.login" ) + "&nbsp;</td></tr>\n" +
                "<tr><td class=\"formworks\"><input type = \"password\" name=\"" + Constants.NAME_PASSWORD_PARAM + "\" value = \"\" >&nbsp;" + bundle.getString( "auth.check.password" ) + "</td></tr>\n" +
                "<tr><td class=\"formworks\" align=\"center\"><input type=\"submit\" name=\"button\" value=\"" +
                bundle.getString( "auth.check.register" ) + "\"></td></tr>\n" +
                "</table>\n" +
                "</form>\n" );
        }
        catch( Throwable e ) {
            String es = "Error in getInstance(DatabaseAdapter db__)";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
        }
        outputStream.flush();
        outputStream.close();
        outputStream = null;
    }
}
