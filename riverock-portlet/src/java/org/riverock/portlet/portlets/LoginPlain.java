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



import javax.servlet.http.HttpSession;



import org.riverock.sso.a3.AuthSession;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.portlet.main.Constants;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;



import org.apache.log4j.Logger;



public class LoginPlain implements Portlet, PortletResultObject, PortletGetList

{

    private static Logger log = Logger.getLogger(LoginPlain.class);



    public String out = "";

    public PortletParameter param = null;



    protected void finalize() throws Throwable

    {

        param = null;



        super.finalize();

    }



    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    public PortletResultObject getInstance(DatabaseAdapter db__) throws Exception

    {

        try

        {



            if (log.isDebugEnabled())

                log.debug("Process input auth data");



//            StringManager authLocalize = StringManager.getManager("mill.locale.auth", param.webPage.currentLocale);



            HttpSession session = param.getRequest().getSession();

            AuthSession auth_ = (AuthSession) session.getAttribute(Constants.AUTH_SESSION);



            if (auth_ == null)

            {

                auth_ = new AuthSession(

                        param.getRequest().getParameter(Constants.NAME_USERNAME_PARAM),

                        param.getRequest().getParameter(Constants.NAME_PASSWORD_PARAM)

                );

            }



            if (auth_.checkAccess( param.getRequest().getServerName()))

            {

                if (log.isDebugEnabled())

                    log.debug("user " + auth_.getUserLogin() + "is  valid for " + param.getRequest().getServerName() + " site");



                return this;

            }



            out += "<form method=\"POST\" action=\"" + CtxURL.ctx() + "\" >\n";



            out += param.getJspPage().cross.getAsForm();

            out += ServletTools.getHiddenItem(

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM,

                    ServletUtils.getString(param.getRequest(), Constants.NAME_TEMPLATE_CONTEXT_PARAM)

            );

            out += ServletTools.getHiddenItem(

                    Constants.NAME_TYPE_CONTEXT_PARAM,

                    Constants.CTX_TYPE_LOGIN_CHECK

            );



            String srcURL = null;

            if (param.getRequest().getParameter(Constants.NAME_TOURL_PARAM) != null)

                srcURL = ServletUtils.getString(param.getRequest(), Constants.NAME_TOURL_PARAM);

            else

            {

                srcURL = CtxURL.url(param.getRequest(), param.getResponse(), param.getJspPage().cross, Constants.CTX_TYPE_LOGIN  );

            }



            if (log.isDebugEnabled())

                log.debug("toUTL - " + srcURL);



            srcURL = StringTools.replaceString(srcURL, "%3D", "=");

            srcURL = StringTools.replaceString(srcURL, "%26", "&");



            if (log.isDebugEnabled())

                log.debug("encoded toUTL - " + srcURL);



            out += ServletTools.getHiddenItem(Constants.NAME_TOURL_PARAM, srcURL);



            if (log.isDebugEnabled())

                log.debug("Header string - " + param.getSm().getStr("auth.check.header"));





            out += ("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\" width=\"100%\">\n"+

                    "<tr><th class=\"formworks\">"+ param.getSm().getStr("auth.check.header") + "</th></tr>\n" +

                    "<tr><td class=\"formworks\"><input type = \"text\" name = \""+ Constants.NAME_USERNAME_PARAM + "\">&nbsp;"+ param.getSm().getStr("auth.check.login") + "&nbsp;</td></tr>\n"+

                    "<tr><td class=\"formworks\"><input type = \"password\" name=\""+ Constants.NAME_PASSWORD_PARAM +"\" value = \"\" >&nbsp;"+ param.getSm().getStr("auth.check.password") +"</td></tr>\n"+

                    "<tr><td class=\"formworks\" align=\"center\"><input type=\"submit\" name=\"button\" value=\""+

                    param.getSm().getStr("auth.check.register") +"\"></td></tr>\n"+

            "</table>\n"+

            "</form>\n");

        }

        catch (Exception e)

        {

            log.error(e);

            throw e;

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



    public PortletResultObject getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        return getInstance(db__);

    }



    public PortletResultObject getInstanceByCode( DatabaseAdapter db__, String portletCode_ ) throws Exception

    {

        return null;

    }



    public List getList( Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

}

