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

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;



public class RegisterUserController extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.servlets.controller.RegisterUserController");



    public RegisterUserController()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request_, HttpServletResponse response)

        throws IOException, ServletException

    {

        Writer out = null;

        DatabaseAdapter db_ = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            db_ = DatabaseAdapter.getInstance(false);

            ContextNavigator.setContentType(response);



            out = response.getWriter();



//            InitPage jspPage = new InitPage(DatabaseAdapter.getInstance(false),

//                request,

//                "mill.locale._price_list"

//            );



            String index_page = response.encodeURL(CtxURL.ctx()) + '?' +

                ctxInstance.page.getAsURL();



            String redirectURL = response.encodeURL(CtxURL.ctx() )+ '?' +

                ctxInstance.page.getAsURL()+

                Constants.NAME_TEMPLATE_CONTEXT_PARAM +'='+

                ctxInstance.getNameTemplate() +'&'+

                Constants.NAME_TYPE_CONTEXT_PARAM + '=' +

                Constants.CTX_TYPE_REGISTER;



            if (cat.isDebugEnabled())

                cat.debug("getIsRegisterAllowed " + ctxInstance.page.p.sites.getIsRegisterAllowed());



// URL used in <form>

            String url = "";

// URL used in <a href=>

            String url_redir = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_TOURL_PARAM, index_page);



            if (cat.isDebugEnabled())

                cat.debug("urlRedir " + url_redir);



//            boolean registerOK = false;



            String action = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_REGISTER_ACTION_PARAM);



            if (cat.isDebugEnabled())

                cat.debug(Constants.NAME_REGISTER_ACTION_PARAM + " " + action);



            if (action != null)

            {

//                url_redir = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_TOURL_PARAM, index_page);



                url_redir = StringTools.replaceString(url_redir, "%2F", "/");

                url_redir = StringTools.replaceString(url_redir, "%3F", "?");

                url_redir = StringTools.replaceString(url_redir, "%3D", "=");

                url_redir = StringTools.replaceString(url_redir, "%26", "&");



                if (cat.isDebugEnabled())

                    cat.debug("#1.001 " + url_redir);



                url = StringTools.rewriteURL(url_redir);



                if (cat.isDebugEnabled())

                    cat.debug("#1.002 " + url);



                url_redir = response.encodeURL(url_redir);



                if (cat.isDebugEnabled())

                    cat.debug("#1.003 " + url_redir);





                String username = PortletTools.getString(ctxInstance.getPortletRequest(), "username");



                String password1 = PortletTools.getString(ctxInstance.getPortletRequest(), "password1");

                String password2 = PortletTools.getString(ctxInstance.getPortletRequest(), "password2");



                String first_name = PortletTools.getString(ctxInstance.getPortletRequest(), "first_name");

                String last_name = PortletTools.getString(ctxInstance.getPortletRequest(), "last_name");



                String email = PortletTools.getString(ctxInstance.getPortletRequest(), "email");

                String addr = PortletTools.getString(ctxInstance.getPortletRequest(), "addr");

                String phone = PortletTools.getString(ctxInstance.getPortletRequest(), "phone");



                HttpSession sess = request_.getSession(true);



                AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

                if ((auth_ != null) && (auth_.checkAccess( ctxInstance.page.p.getServerName())))

                {

//                    String args1[] = {index_page};

//                    out.write(ctxInstance.sCustom.getStr("reg.already_login", args1));

//                    args1 = null;



                    response.sendRedirect( redirectURL );

                    return;

                }



                if (action.equals("reg_exists"))

                {

                    if (cat.isDebugEnabled())

                    {

                        cat.debug("process action 'reg_exists'");

                        cat.debug("auth - "+auth_);

                        if (auth_!=null)

                            cat.debug("checkAccess "+ auth_.checkAccess( ctxInstance.page.p.getServerName()) );

                    }



                    if ((auth_ == null) ||

                        (!auth_.checkAccess( ctxInstance.page.p.getServerName()))

                    )



                    {

                        if (cat.isDebugEnabled())

                        {

                            cat.debug("username "+username);

                            cat.debug("password1 "+password1);

                        }



                        if ((username != null) && (password1 != null))

                        {

                            auth_ = new AuthSession(username, password1);



                            if (cat.isDebugEnabled())

                            {

                                cat.debug("new auth - "+auth_);

                                if (auth_!=null)

                                    cat.debug("new checkAccess "+ auth_.checkAccess( ctxInstance.page.p.getServerName()) );

                            }



                            if (auth_.checkAccess( ctxInstance.page.p.getServerName()))

                            {

                                sess.setAttribute(Constants.AUTH_SESSION, auth_);



                                String args5[] = {url_redir};

                                out.write(ctxInstance.sCustom.getStr("reg.reg_complete", args5));



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



                            if (cat.isDebugEnabled())

                                cat.debug("#1.0005");



                            Long id_user = InternalAuthProviderTools.addNewUser(dbDyn,

                                first_name, last_name, "", ctxInstance.page.p.sites.getIdFirm(),

                                mailAddr.toString(), addr, phone);



                            if (cat.isDebugEnabled())

                                cat.debug("#1.0006 " + ctxInstance.page.p.sites.getIdFirm());



                            Long id_auth_user;

                            id_auth_user = InternalAuthProviderTools.addUserAuth(dbDyn, id_user,

                                ctxInstance.page.p.sites.getIdFirm(), null, null, username, password1,

                                true, false, false);



                            if (cat.isDebugEnabled())

                                cat.debug("#1.0007");



                            InternalAuthProviderTools.bindUserRole(dbDyn, id_auth_user, "SHOP_BUYER");



                            if (cat.isDebugEnabled())

                                cat.debug("#1.0008");



                            dbDyn.commit();



                        }

                        catch (SQLException e)

                        {

                            dbDyn.rollback();



                            cat.error("Error register new user", e);



                            if (dbDyn.testExceptionIndexUniqueKey(e, "USER_LOGIN_AU_UK"))

                            {

                                String args2[] = {username, response.encodeURL("register.jsp?") + ctxInstance.page.getAsURL()};

                                out.write(ctxInstance.sCustom.getStr("reg.login_exists", args2));



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

                            cat.error("Error register new user", e2);

                            out.write("General error: " + e2.toString());

                            return;

                        }



                        auth_ = new AuthSession(username, password1);



                        if (cat.isDebugEnabled())

                            cat.debug("Admin mail: " + ctxInstance.page.p.sites.getAdminEmail());



                        String args3[] = {username, password1, ctxInstance.page.p.getServerName()};



                        MailMessage.sendMessage(

                            ctxInstance.sCustom.getStr("reg.mail_body", args3) + "\n\nregister from IP ", // + ctxInstance.page.p.getRemoteAddr(),

                            email,

                            ctxInstance.page.p.sites.getAdminEmail(),

                            "Confirm registration",

                            GenericConfig.getMailSMTPHost()

                        );

                        args3 = null;

                        if (auth_.checkAccess( ctxInstance.page.p.getServerName()))

                        {

                            sess.setAttribute(Constants.AUTH_SESSION, auth_);



                            String args4[] = {url_redir};

                            out.write(ctxInstance.sCustom.getStr("reg.reg_complete", args4));



                            args4 = null;



                            if (cat.isDebugEnabled())

                                cat.debug("URL after register: " + url_redir);



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



            if (!Boolean.TRUE.equals(ctxInstance.page.p.sites.getIsRegisterAllowed()) )

                return;



            out.write(ctxInstance.sCustom.getStr("reg.you_memeber"));

            out.write("\n");

            out.write("<table>\n");

            out.write(

                "<form method=\"POST\" action=\""+response.encodeURL("/register.jsp")+"\">\n"

            );

            out.write(ctxInstance.page.getAsForm());

            out.write( ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "reg_exists") );

            out.write("<input type=\"hidden\" name=\"url\" value=\"");

            out.write(url);

            out.write("\">\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.member_login"));

            out.write(":");

            out.write("</td>\r\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"username\" size=\"12\">");

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"submit\" value=\"");

            out.write(ctxInstance.sCustom.getStr("reg.register"));

            out.write("\">\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.member_password"));

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

            out.write(ctxInstance.sCustom.getStr("reg.forgot_password"));

            out.write("\r\n");

            out.write("<table>\n");

            out.write("<form method=\"post\" action=\"");

            out.write(response.encodeURL("/register.jsp"));

            out.write("\">\n");

            out.write(ctxInstance.page.getAsForm());

            out.write("\r\n");

            out.write("<input type=\"hidden\" name=\"url\" value=\"");

            out.write(url);

            out.write("\">\r\n");

            out.write( ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "send_pass") );

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.member_email"));

            out.write(":");

            out.write("</td>\r\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"email\" size=\"12\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write("<input type=\"submit\" value=\"");

            out.write(ctxInstance.sCustom.getStr("reg.send_password"));

            out.write("\">\n");

            out.write("</tr>\n");

            out.write("</form>\n");

            out.write("</table>\n");

            out.write("<br>\n\n");

            out.write(ctxInstance.sCustom.getStr("reg.need_register"));

            out.write("<table>\n");

            out.write("<form method=\"post\" action=\"");

            out.write(response.encodeURL("/register.jsp"));

            out.write("\">\n");

            out.write(ctxInstance.page.getAsForm());

            out.write("\n");

            out.write( ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "reg_new") );

            out.write("<input type=\"hidden\" name=\"url\" value=\"");

            out.write(url);

            out.write("\">\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.login"));

            out.write(":");

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"username\" size=\"20\" maxlength=\"20\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.password"));

            out.write(":");

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"password\" name=\"password1\" size=\"20\" maxlength=\"20\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.password_repeat"));

            out.write(":");

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"password\" name=\"password2\" size=\"20\" maxlength=\"20\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.first_name"));

            out.write(":");

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"first_name\" size=\"50\" maxlength=\"50\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.last_name"));

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"test\" name=\"last_name\" size=\"50\" maxlength=\"50\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.telephone"));

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"test\" name=\"phone\" size=\"25\" maxlength=\"25\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.address"));

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"test\" name=\"addr\" size=\"50\" maxlength=\"50\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td>");

            out.write(ctxInstance.sCustom.getStr("reg.email"));

            out.write("</td>\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"email\" size=\"30\" maxlength=\"30\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("<tr>\n");

            out.write("<td colspan=\"2\">");

            out.write("<input type=\"submit\" value=\"");

            out.write(ctxInstance.sCustom.getStr("reg.register"));

            out.write("\">");

            out.write("</td>\n");

            out.write("</tr>\n");

            out.write("</form>\n");

            out.write("</table>");



        }

        catch (Exception e)

        {

            cat.error("Erorr processing RegisterUser page", e);

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

