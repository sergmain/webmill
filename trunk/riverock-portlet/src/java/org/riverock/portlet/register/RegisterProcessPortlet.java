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
package org.riverock.portlet.register;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portlet.PortletTools;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 4:32:29 PM
 * <p/>
 * $Id$
 */
public final class RegisterProcessPortlet implements Portlet {

    private final static Logger log = Logger.getLogger(RegisterProcessPortlet.class);

    public RegisterProcessPortlet() {
    }

    private PortletConfig portletConfig = null;

    public void init(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
        throws IOException, PortletException {

        Writer out = renderResponse.getWriter();
        String indexPage = PortletTools.url(Constants.CTX_TYPE_INDEX, renderRequest, renderResponse, "");
        try {

            if (renderRequest.getParameter(RegisterPortlet.ERROR_TEXT) != null) {
                WebmillErrorPage.processPortletError(out, null,
                    (String) renderRequest.getParameter(RegisterPortlet.ERROR_TEXT),
                    (String) renderRequest.getParameter(RegisterPortlet.ERROR_URL),
                    (String) renderRequest.getParameter(RegisterPortlet.ERROR_URL_NAME));

                printMessageToIndexPage(out, indexPage);

                return;
            }
/*
        if ( renderRequest.getAttribute( ERROR_TEXT )!=null ) {
            WebmillErrorPage.processPortletError(out, null,
                (String)renderRequest.getAttribute( ERROR_TEXT ),
                (String)renderRequest.getAttribute( ERROR_URL ),
                (String)renderRequest.getAttribute( ERROR_URL_NAME ));

           printMessageToIndexPage( out, indexPage );
 
           return;
        } 
*/
            String action = null;
            try {
                action = PortletTools.getString(renderRequest, RegisterPortlet.NAME_REGISTER_ACTION_PARAM);
            } catch (ConfigException e) {
                String es = "Error get parameter";
                log.error(es, e);
                throw new PortletException(es, e);
            }

            if (action != null) {
                if (action.equals("send_pass")) {
                    printSuccessSendPassword(out);
                } else if (action.equals("reg_new")) {
                    printSuccessRegistration(out);
                }
                printMessageToIndexPage(out, indexPage);
            }
        } finally {
            out.flush();
            out.close();
        }
    }

    private void printSuccessSendPassword(Writer out) throws IOException {
        out.write("<p class=\"register-text\">Password successfully sended to you.</p>");
    }

    private void printSuccessRegistration(Writer out) throws IOException {
        out.write("<p class=\"register-text\">Registration was successfull. Please reply to letter, which was sended to you, for confir registration.</p>");
    }

    private void printMessageToIndexPage(Writer out, String indexPage) throws IOException {
        out.write("<p class=\"register-continue\">You can continue work at <a href=\"");
        out.write(indexPage);
        out.write("\">homepage</a></p>");
    }


    public void processAction(final ActionRequest actionRequest, final ActionResponse actionResponse) {

        if (log.isDebugEnabled()) {
            log.debug("start process action");
        }

        StringBuffer out = new StringBuffer();
        DatabaseAdapter db_ = null;
        String registerUrl = PortletTools.url(RegisterPortlet.REGISTER_PORTLET, actionRequest, actionResponse);
        try {
//            PortletSession session = actionRequest.getPortletSession();
            AuthSession auth_ = (AuthSession) actionRequest.getUserPrincipal();


            ResourceBundle bundle = portletConfig.getResourceBundle(actionRequest.getLocale());

            db_ = DatabaseAdapter.getInstance();

            String indexPage = PortletTools.url(Constants.CTX_TYPE_INDEX, actionRequest, actionResponse, "" );

            PortalInfo portalInfo = (PortalInfo) actionRequest.getAttribute(PortalConstants.PORTAL_INFO_ATTRIBUTE);

            if (log.isDebugEnabled()) {
                log.debug("getIsRegisterAllowed " + portalInfo.getSites().getIsRegisterAllowed());
            }

            String url = "";
            String url_redir = PortletTools.getString(actionRequest, Constants.NAME_TOURL_PARAM, indexPage);

            if (log.isDebugEnabled()) {
                log.debug("urlRedir " + url_redir);
            }

            String action = PortletTools.getString(actionRequest, RegisterPortlet.NAME_REGISTER_ACTION_PARAM);

            if (log.isDebugEnabled()) {
                log.debug(RegisterPortlet.NAME_REGISTER_ACTION_PARAM + " " + action);
            }

            if (action != null) {
//                url_redir = PortletTools.getString(actonRequest, Constants.NAME_TOURL_PARAM, index_page);

                url_redir = StringTools.replaceString(url_redir, "%2F", "/");
                url_redir = StringTools.replaceString(url_redir, "%3F", "?");
                url_redir = StringTools.replaceString(url_redir, "%3D", "=");
                url_redir = StringTools.replaceString(url_redir, "%26", "&");

                if (log.isDebugEnabled())
                    log.debug("#1.001 url_redir: " + url_redir);

                url = StringTools.rewriteURL(url_redir);

                if (log.isDebugEnabled())
                    log.debug("#1.002 url: " + url);

                url_redir = actionResponse.encodeURL(url_redir);

                if (log.isDebugEnabled())
                    log.debug("#1.003 url_redir: " + url_redir);


                String username = PortletTools.getString(actionRequest, RegisterPortlet.USERNAME_PARAM);

                String password1 = PortletTools.getString(actionRequest, RegisterPortlet.PASSWORD1_PARAM);
                String password2 = PortletTools.getString(actionRequest, RegisterPortlet.PASSWORD2_PARAM);

                String firstName = PortletTools.getString(actionRequest, RegisterPortlet.FIRST_NAME_PARAM);
                String lastName = PortletTools.getString(actionRequest, RegisterPortlet.LAST_NAME_PARAM);
                String middleName = PortletTools.getString(actionRequest, RegisterPortlet.MIDDLE_NAME_PARAM);

                String email = PortletTools.getString(actionRequest, RegisterPortlet.EMAIL_PARAM);
//                String address = PortletTools.getString(actionRequest, RegisterPortlet.ADDRESS_PARAM);
//                String phone = PortletTools.getString(actionRequest, RegisterPortlet.PHONE_PARAM);

                if (action.equals("reg_new")) {

                    if (auth_ != null) {
                        WebmillErrorPage.setErrorInfo(actionResponse,
                            "You already logged in",
                            RegisterPortlet.ERROR_TEXT,
                            null,
                            "Continue",
                            RegisterPortlet.ERROR_URL_NAME,
                            indexPage,
                            RegisterPortlet.ERROR_URL);
                        return;
                    }

                    if (!Boolean.TRUE.equals(portalInfo.getSites().getIsRegisterAllowed())) {
                        if (log.isDebugEnabled()) {
                            log.debug("Registration on this site not allowed");
                        }
                        return;
                    }

                    String role = PortletTools.getMetadata(actionRequest, RegisterPortlet.DEFAULT_ROLE_METADATA);
                    RegisterProcessor processor = new RegisterProcessor(username, password1, password2,
                        email,
                        firstName,
                        lastName,
                        middleName,
                        role, actionRequest.getServerName(),
                        bundle, portalInfo);

                    switch (processor.registerStatus) {
                        case RegisterProcessor.ROLE_IS_NULL_STATUS:
                            throw new PortletException("Can not add new user because default role not specified in metadata");
                        case RegisterProcessor.USERNAME_ALREADY_EXISTS_STATUS:
                            String args2[] = {username, registerUrl};
                            out.append(
                                PortletTools.getString(bundle, "reg.login_exists", args2)
                            );
//                            out.append(PortletTools.getStr(actionRequest.getLocale(), "reg.login_exists", args2, portletConfig));
                            args2 = null;
                            break;
                    }
                    processor.destroy();
                    processor = null;

                } else if (action.equals("send_pass")) {
                    if (auth_ == null) {
                        WebmillErrorPage.setErrorInfo(actionResponse,
                            "You must log in, before of sending of password",
                            RegisterPortlet.ERROR_TEXT,
                            null,
                            "Continue",
                            RegisterPortlet.ERROR_URL_NAME,
                            indexPage,
                            RegisterPortlet.ERROR_URL);
                        return;
                    }

                    RegisterProcessor.sendPassword( auth_, portalInfo.getSites().getAdminEmail(), bundle);
                }
            }

        } catch (Throwable e) {
            WebmillErrorPage.setErrorInfo(actionResponse,
                "Error execute register portlet",
                RegisterPortlet.ERROR_TEXT,
                e,
                "Continue",
                RegisterPortlet.ERROR_URL_NAME,
                registerUrl,
                RegisterPortlet.ERROR_URL);

            final String es = "Error execute register portlet";
            log.error(es, e);
        } finally {
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }
}
