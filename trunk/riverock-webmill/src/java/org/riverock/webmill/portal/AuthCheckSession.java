/*

 * org.riverock.webmill -- Portal framework implementation

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

 * User: serg_main

 * Date: 23.11.2003

 * Time: 21:04:29

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portal;



import java.util.Enumeration;



import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.common.tools.StringTools;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.port.PortalInfo;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.tools.CrossPageParam;

import org.riverock.webmill.tools.CrossPageParamInterface;



import org.apache.log4j.Logger;



public class AuthCheckSession

{

    private static Logger log = Logger.getLogger( "org.riverock.webmill.portal.AuthCheckSession.MainUserInfo" );



    public static AuthSession check(HttpServletRequest request, HttpServletResponse response)

        throws Exception

    {



        DatabaseAdapter db_ = null;

        try

        {



            if (log.isDebugEnabled() && request!=null)

            {

                log.debug("check rights. request method " + request.getMethod());



//                HttpSession session = request.getSession();

//                log.debug("type ctx 1 - " + session.getAttribute(Constants.TYPE_CTX_SESSION));

            }



            CrossPageParamInterface cross = null;

            db_ = DatabaseAdapter.getInstance(false);

            PortalInfo p = PortalInfo.getInstance(db_, request.getServerName());

            cross = new CrossPageParam(

                request, Constants.NAME_LANG_PARAM, p.defaultLocale, null, null

            );



            String redir = CtxURL.url(request, response, cross, Constants.CTX_TYPE_LOGIN_CHECK);



            if (request.getMethod().toUpperCase().equals("GET"))

            {

                String param = "";

                Enumeration e = request.getParameterNames();

                boolean isFirst = true;

                String addStr = "";

                for (; e.hasMoreElements();)

                {

                    String n = (String) e.nextElement();

                    String[] s = request.getParameterValues(n);

                    for (int i = 0; i < s.length; i++)

                    {

                        if (!isFirst)

                            addStr = "&";

                        else

                            isFirst = false;



                        if (log.isDebugEnabled())

                            log.debug("check rights. param: " + addStr + n + " value: " + s[i]);



                        param += StringTools.rewriteURL(addStr + n + "=" + s[i]);

                    }

                }



                redir +=

                    Constants.NAME_TOURL_PARAM + '=' +

                    StringTools.rewriteURL(request.getRequestURI() + '?' + param);



                if (log.isDebugEnabled())

                {

                    log.debug("check rights.  redir: " + redir);

//                    HttpSession session = request.getSession();

//                    log.debug("type ctx 2 - " + session.getAttribute(Constants.TYPE_CTX_SESSION));

                }



            }

            return AuthTools.check(request, response, redir);

        }

        catch (Exception e01)

        {

            log.error("check rights. Error  get  cross information", e01);

            throw e01;

        }

        finally

        {

            org.riverock.generic.db.DatabaseManager.close( db_ );

            db_ = null;

        }

    }





}

