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



import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;



public class AuthCheckSession

{

    private static Logger log = Logger.getLogger( "org.riverock.webmill.portal.AuthCheckSession.MainUserInfo" );



    public static AuthSession check(PortletRequest request, HttpServletResponse response)

        throws Exception

    {



        DatabaseAdapter db_ = null;

        try

        {



//            if (log.isDebugEnabled() && request!=null)

//                log.debug("check rights. request method " + request.getMethod());



            db_ = DatabaseAdapter.getInstance(false);

/*

            InitPage page = new InitPage(db_, request,"mill.locale.site_hamradio");



            String redir = CtxURL.url(request, response, page, Constants.CTX_TYPE_LOGIN_CHECK);



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

                    log.debug("check rights.  redir: " + redir);



            }

*/

            return AuthTools.check(request, response, "/");

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

