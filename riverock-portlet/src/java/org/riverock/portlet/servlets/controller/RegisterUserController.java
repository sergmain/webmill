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
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 4:32:29 PM
 *
 * $Id$
 */

package org.riverock.portlet.servlets.controller;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.portlet.RenderRequest;
import javax.portlet.PortletConfig;

import org.apache.log4j.Logger;
import org.riverock.common.mail.MailMessage;
import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.main.Constants;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.PortalConstants;

public final class RegisterUserController extends HttpServlet {
    private final static Logger log = Logger.getLogger( RegisterUserController.class );

    public RegisterUserController()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
        throws IOException, ServletException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;
        try
        {
//            CtxInstance ctxInstance =
//                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            RenderRequest renderRequest = null;
            PortletConfig portletConfig = null;

            db_ = DatabaseAdapter.getInstance(false);
            ContextNavigator.setContentType(response);

            out = response.getWriter();

//            InitPage jspPage = new InitPage(DatabaseAdapter.getInstance(false),
//                request,
//                "mill.locale._price_list"
//            );

            String index_page = response.encodeURL(CtxInstance.ctx()) + '?';
//                ctxInstance.getAsURL();

            String redirectURL = CtxInstance.url(Constants.CTX_TYPE_REGISTER);

            PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute(PortalConstants.PORTAL_INFO_ATTRIBUTE);
            if (log.isDebugEnabled())
                log.debug("getIsRegisterAllowed " + portalInfo.getSites().getIsRegisterAllowed());

// URL used in <form>
            String url = "";
// URL used in <a href=>
            String url_redir = PortletTools.getString(renderRequest, Constants.NAME_TOURL_PARAM, index_page);

            if (log.isDebugEnabled())
                log.debug("urlRedir " + url_redir);

//            boolean registerOK = false;

            String action = PortletTools.getString(renderRequest, Constants.NAME_REGISTER_ACTION_PARAM);

            if (log.isDebugEnabled())
                log.debug(Constants.NAME_REGISTER_ACTION_PARAM + " " + action);

            if (action != null)
            {
//                url_redir = PortletTools.getString(renderRequest, Constants.NAME_TOURL_PARAM, index_page);

                url_redir = StringTools.replaceString(url_redir, "%2F", "/");
                url_redir = StringTools.replaceString(url_redir, "%3F", "?");
                url_redir = StringTools.replaceString(url_redir, "%3D", "=");
                url_redir = StringTools.replaceString(url_redir, "%26", "&");

                if (log.isDebugEnabled())
                    log.debug("#1.001 " + url_redir);

                url = StringTools.rewriteURL(url_redir);

                if (log.isDebugEnabled())
                    log.debug("#1.002 " + url);

                url_redir = response.encodeURL(url_redir);

                if (log.isDebugEnabled())
                    log.debug("#1.003 " + url_redir);


                String username = PortletTools.getString(renderRequest, "username");

                String password1 = PortletTools.getString(renderRequest, "password1");
                String password2 = PortletTools.getString(renderRequest, "password2");

                String first_name = PortletTools.getString(renderRequest, "first_name");
                String last_name = PortletTools.getString(renderRequest, "last_name");

                String email = PortletTools.getString(renderRequest, "email");
                String addr = PortletTools.getString(renderRequest, "addr");
                String phone = PortletTools.getString(renderRequest, "phone");

                HttpSession sess = request_.getSession(true);

                AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
                if ((auth_ != null) && (auth_.checkAccess( renderRequest.getServerName())))
                {
//                    String args1[] = {index_page};
//                    out.write(CtxInstance.getStr("reg.already_login", args1));
//                    args1 = null;

                    response.sendRedirect( redirectURL );
                    return;
                }

                if (action.equals("reg_exists"))
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("process action 'reg_exists'");
                        log.debug("auth - "+auth_);
                        if (auth_!=null)
                            log.debug("checkAccess "+ auth_.checkAccess( renderRequest.getServerName()) );
                    }

                    if ((auth_ == null) ||
                        (!auth_.checkAccess( renderRequest.getServerName()))
                    )

                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("username "+username);
                            log.debug("password1 "+password1);
                        }

                        if ((username != null) && (password1 != null))
                        {
                            auth_ = new AuthSession(username, password1);

                            if (log.isDebugEnabled())
                            {
                                log.debug("new auth - "+auth_);
                                if (auth_!=null)
                                    log.debug("new checkAccess "+ auth_.checkAccess( renderRequest.getServerName()) );
                            }

                            if (auth_.checkAccess( renderRequest.getServerName()))
                            {
                                sess.setAttribute(Constants.AUTH_SESSION, auth_);

                                String args5[] = {url_redir};
                                out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.reg_complete", args5, portletConfig ));

                                args5 = null;
                                return;
                            }
                        }
                    }
                }
                else if (action.equals("reg_new"))
                {

                    if ((username != null) && (password1 != null) &&
                        (password1.equals(password2)))
                    {
                        InternetAddress mailAddr = new InternetAddress(email);

                        DatabaseAdapter dbDyn = null;
                        try
                        {
                            dbDyn = DatabaseAdapter.getInstance(true);

                            if (log.isDebugEnabled())
                                log.debug("#1.0005");

                            Long id_user = InternalAuthProviderTools.addNewUser(dbDyn,
                                first_name, last_name, "", portalInfo.getSites().getIdFirm(),
                                mailAddr.toString(), addr, phone);

                            if (log.isDebugEnabled())
                                log.debug("#1.0006 " + portalInfo.getSites().getIdFirm());

                            Long id_auth_user;
                            id_auth_user = InternalAuthProviderTools.addUserAuth(dbDyn, id_user,
                                portalInfo.getSites().getIdFirm(), null, null, username, password1,
                                true, false, false);

                            if (log.isDebugEnabled())
                                log.debug("#1.0007");

                            InternalAuthProviderTools.bindUserRole(dbDyn, id_auth_user, "SHOP_BUYER");

                            if (log.isDebugEnabled())
                                log.debug("#1.0008");

                            dbDyn.commit();

                        }
                        catch (SQLException e)
                        {
                            dbDyn.rollback();

                            log.error("Error register new user", e);

                            if (dbDyn.testExceptionIndexUniqueKey(e, "USER_LOGIN_AU_UK"))
                            {
                                String args2[] = {username, response.encodeURL("register.jsp?")};
                                out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.login_exists", args2, portletConfig ));

                                args2 = null;
                                return;
                            }
                            else
                            {
                                out.write("Register new user. Unknown error: " + e.toString());
                                return;
                            }

                        }
                        catch (Exception e2)
                        {
                            log.error("Error register new user", e2);
                            out.write("General error: " + e2.toString());
                            return;
                        }

                        auth_ = new AuthSession(username, password1);

                        if (log.isDebugEnabled())
                            log.debug("Admin mail: " + portalInfo.getSites().getAdminEmail());

                        String args3[] = {username, password1, renderRequest.getServerName()};

                        MailMessage.sendMessage(
                            CtxInstance.getStr( renderRequest.getLocale(), "reg.mail_body", args3, portletConfig ) + "\n\nregister from IP ", // + ctxInstance.page.p.getRemoteAddr(),
                            email,
                            portalInfo.getSites().getAdminEmail(),
                            "Confirm registration",
                            GenericConfig.getMailSMTPHost()
                        );
                        args3 = null;
                        if (auth_.checkAccess( renderRequest.getServerName() ))
                        {
                            sess.setAttribute(Constants.AUTH_SESSION, auth_);

                            String args4[] = {url_redir};
                            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.reg_complete", args4, portletConfig ));

                            args4 = null;

                            if (log.isDebugEnabled())
                                log.debug("URL after register: " + url_redir);

                            return;
                        }
                    }

                }
                else if (action.equals("send_pass"))
                {
/*
public static void sendMessage(
String message,
String email_to,
String subj,
Locale loc)
*/
                }
            }

            if (!Boolean.TRUE.equals(portalInfo.getSites().getIsRegisterAllowed()) )
                return;

            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.you_memeber", portletConfig ));
            out.write("\n");
            out.write("<table>\n");
            out.write(
                "<form method=\"POST\" action=\""+response.encodeURL("/register.jsp")+"\">\n"
            );
//            out.write(ctxInstance.getAsForm());
            out.write( ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "reg_exists") );
            out.write("<input type=\"hidden\" name=\"url\" value=\"");
            out.write(url);
            out.write("\">\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.member_login", portletConfig ));
            out.write(":");
            out.write("</td>\r\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"username\" size=\"12\">");
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"submit\" value=\"");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.register", portletConfig ));
            out.write("\">\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.member_password", portletConfig ));
            out.write(":");
            out.write("</td>\n");
            out.write("<td colspan=\"3\">");
            out.write("<input type=\"password\" name=\"password1\" size=\"12\">");
            out.write("</td>\n");
            out.write("<td colspan=\"3\">&nbsp;");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("</form>\n");
            out.write("</table>\n");
            out.write("<br>\n\n");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.forgot_password", portletConfig ));
            out.write("\r\n");
            out.write("<table>\n");
            out.write("<form method=\"post\" action=\"");
            out.write(response.encodeURL("/register.jsp"));
            out.write("\">\n");
//            out.write(ctxInstance.getAsForm());
            out.write("\r\n");
            out.write("<input type=\"hidden\" name=\"url\" value=\"");
            out.write(url);
            out.write("\">\r\n");
            out.write( ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "send_pass") );
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.member_email", portletConfig ));
            out.write(":");
            out.write("</td>\r\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"email\" size=\"12\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write("<input type=\"submit\" value=\"");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.send_password", portletConfig ));
            out.write("\">\n");
            out.write("</tr>\n");
            out.write("</form>\n");
            out.write("</table>\n");
            out.write("<br>\n\n");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.need_register", portletConfig ));
            out.write("<table>\n");
            out.write("<form method=\"post\" action=\"");
            out.write(response.encodeURL("/register.jsp"));
            out.write("\">\n");
//            out.write(ctxInstance.getAsForm());
            out.write("\n");
            out.write( ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "reg_new") );
            out.write("<input type=\"hidden\" name=\"url\" value=\"");
            out.write(url);
            out.write("\">\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.login", portletConfig ));
            out.write(":");
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"username\" size=\"20\" maxlength=\"20\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.password", portletConfig ));
            out.write(":");
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"password\" name=\"password1\" size=\"20\" maxlength=\"20\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.password_repeat", portletConfig ));
            out.write(":");
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"password\" name=\"password2\" size=\"20\" maxlength=\"20\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.first_name", portletConfig ));
            out.write(":");
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"first_name\" size=\"50\" maxlength=\"50\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.last_name", portletConfig ));
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"test\" name=\"last_name\" size=\"50\" maxlength=\"50\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.telephone", portletConfig ));
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"test\" name=\"phone\" size=\"25\" maxlength=\"25\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.address", portletConfig ));
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"test\" name=\"addr\" size=\"50\" maxlength=\"50\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td>");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.email", portletConfig ));
            out.write("</td>\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"email\" size=\"30\" maxlength=\"30\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("<tr>\n");
            out.write("<td colspan=\"2\">");
            out.write("<input type=\"submit\" value=\"");
            out.write(CtxInstance.getStr( renderRequest.getLocale(), "reg.register", portletConfig ));
            out.write("\">");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("</form>\n");
            out.write("</table>");

        }
        catch (Exception e)
        {
            log.error("Erorr processing RegisterUser page", e);
//            out.write(ExceptionTools.getStackTrace(e, 100, "<br>"));

            throw new ServletException( e );
        }
        finally
        {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }

    }
}
