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

/**
 * User: Admin
 * Date: Aug 24, 2003
 * Time: 6:39:51 PM
 *
 * $Id$
 */
package org.riverock.portlet.portlets;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.main.Constants;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.portlet.PortletGetList;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.PortletResultContent;

import org.apache.log4j.Logger;

public final class LoginPlain implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( LoginPlain.class );

    public String out = "";

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private ResourceBundle bundle = null;
    private PortletConfig portletConfig = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.portletConfig = portletConfig;
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public PortletResultContent getInstance(DatabaseAdapter db__) throws PortletException {
        try {
            if (log.isDebugEnabled())
                log.debug("Process input auth data");

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();

            if (auth_ == null) {
                auth_ = new AuthSession(
                    renderRequest.getParameter(Constants.NAME_USERNAME_PARAM),
                    renderRequest.getParameter(Constants.NAME_PASSWORD_PARAM)
                );
            }

//            CtxInstance ctxInstance = (CtxInstance)portletRequest.getPortletSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            if ( auth_.checkAccess( renderRequest.getServerName()) ) {
                if (log.isDebugEnabled())
                    log.debug("user " + auth_.getUserLogin() + "is  valid for " + renderRequest.getServerName() + " site");

                return this;
            }

            out += "<form method=\"POST\" action=\"" + CtxInstance.ctx() + "\" >\n";

//            out += ctxInstance.getAsForm();
//            out += ServletTools.getHiddenItem(
//                    Constants.NAME_TEMPLATE_CONTEXT_PARAM,
//                    param.getNameTemplate()
//            );
            out += ServletTools.getHiddenItem(
                    Constants.NAME_TYPE_CONTEXT_PARAM,
                    Constants.CTX_TYPE_LOGIN_CHECK
            );

            String srcURL = null;
            if (renderRequest.getParameter(Constants.NAME_TOURL_PARAM) != null) {
                srcURL = PortletTools.getString(renderRequest, Constants.NAME_TOURL_PARAM);
            }
            else {
                srcURL = CtxInstance.url( Constants.CTX_TYPE_LOGIN );
            }

            if (log.isDebugEnabled())
                log.debug("toUTL - " + srcURL);

            srcURL = StringTools.replaceString(srcURL, "%3D", "=");
            srcURL = StringTools.replaceString(srcURL, "%26", "&");

            if (log.isDebugEnabled())
                log.debug("encoded toUTL - " + srcURL);

            out += ServletTools.getHiddenItem(Constants.NAME_TOURL_PARAM, srcURL);

            if (log.isDebugEnabled())
                log.debug("Header string - " + CtxInstance.getStr( renderRequest.getLocale(), "auth.check.header", portletConfig ));

            out += ("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\" width=\"100%\">\n"+
                    "<tr><th class=\"formworks\">"+ CtxInstance.getStr( renderRequest.getLocale(), "auth.check.header", portletConfig ) + "</th></tr>\n" +
                    "<tr><td class=\"formworks\"><input type = \"text\" name = \""+ Constants.NAME_USERNAME_PARAM + "\">&nbsp;"+ CtxInstance.getStr( renderRequest.getLocale(), "auth.check.login", portletConfig ) + "&nbsp;</td></tr>\n"+
                    "<tr><td class=\"formworks\"><input type = \"password\" name=\""+ Constants.NAME_PASSWORD_PARAM +"\" value = \"\" >&nbsp;"+ CtxInstance.getStr( renderRequest.getLocale(), "auth.check.password", portletConfig ) +"</td></tr>\n"+
                    "<tr><td class=\"formworks\" align=\"center\"><input type=\"submit\" name=\"button\" value=\""+
                    CtxInstance.getStr( renderRequest.getLocale(), "auth.check.register", portletConfig ) +"\"></td></tr>\n"+
            "</table>\n"+
            "</form>\n");
        }
        catch (Throwable e){
            String es = "Error in getInstance(DatabaseAdapter db__)";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        return this;
    }

    public byte[] getXml(String name)
    {
        if(log.isDebugEnabled())
            log.debug("LoginPlain. method is 'Xml'");

        return getXml();
    }

    public byte[] getXml()
    {
        log.warn("Call LoginPlain.getXml()");
        return null;
    }

    public byte[] getPlainHTML()
        throws Exception
    {
        return out.getBytes( WebmillConfig.getHtmlCharset() );
    }

    public boolean isXml(){ return false; }
    public boolean isHtml(){ return true; }

    public LoginPlain(){}

    public PortletResultContent getInstance(DatabaseAdapter db__, Long id__)
            throws PortletException
    {
        return getInstance(db__);
    }

    public PortletResultContent getInstanceByCode( DatabaseAdapter db__, String portletCode_ ) throws PortletException
    {
        return null;
    }

    public List getList( Long idSiteCtxLangCatalog, Long idContext)
    {
        return null;
    }
}
