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



import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.riverock.sso.main.Constants;



import org.apache.log4j.Logger;



public class AuthTools

{

    private static Logger log = Logger.getLogger("org.riverock.sso.a3.AuthTools");



/*

    public static AuthSession check(HttpServletRequest request, HttpServletResponse response)

        throws Exception

    {



        DatabaseAdapter db_ = null;

        try

        {



            if (log.isDebugEnabled() && request!=null)

                log.debug("check rights. request method " + request.getMethod());



            CrossPageParam cross = null;

            db_ = DatabaseAdapter.getInstance(false);

            PortalInfo p = PortalInfo.getInstance(db_, request.getServerName());

            cross = new CrossPageParam(

                request, Constants.NAME_LANG_PARAM,p.defaultLocale, null, null

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

                    log.debug("check rights.  redir: " + redir);



            }

            return check(request, response, redir);

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

*/



    public static AuthSession check(HttpServletRequest request, HttpServletResponse response, String defURL)

        throws AuthException

    {

        AuthSession authSession = getAuthSession(request);



        if (log.isDebugEnabled())

            log.debug("AUTH_SESSION - " + authSession);



        if (authSession == null)

        {



            // work arounf java.lang.IllegalStateException

            // XXX это исключение возникает из-за того что делается response.sendRedirect()

            // не в оригиральном объекте, а в клоне.

            //отследить какие объекты генерируют это исключение

            long currentTimeMills = System.currentTimeMillis();

            for (int i = 0; i < 5; i++)

            {

                try

                {

                    response.sendRedirect(defURL);

                    break;

                }

                catch (IOException e)

                {

                    log.error("IOException auth check. Send redirect failed ", e);

                    throw new AuthException( e.toString() );

                }

                catch (IllegalStateException e)

                {

                    if (i == 4) // last loop

                    {

                        log.error("IllegalStateException auth check. Send redirect failed ", e);

                        throw new AuthException(e.toString());

                    }

                    else

                    {

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



        try

        {

            if (!authSession.checkAccess( request.getServerName()))

            {

                if (log.isDebugEnabled())

                    log.debug("redirect to - " + defURL);



                response.sendRedirect(defURL);

                return null;

            }

        }

        catch (Exception e)

        {

            log.error("Exception auth check. Access denied.", e);

            throw new AuthException(e.toString());

        }

        return authSession;

    }



    public static AuthInfo getAuthInfo(HttpServletRequest request)

        throws AuthException

    {

        AuthSession authSession = getAuthSession(request);

        if (authSession == null)

            return null;



        return InternalAuthProvider.getAuthInfo( authSession );

    }



    public static AuthSession getAuthSession(HttpSession session)

    {

        return (AuthSession) session.getAttribute(Constants.AUTH_SESSION);

    }



    public static AuthSession getAuthSession(HttpServletRequest request)

    {

        return (AuthSession) request.getSession(true).getAttribute(Constants.AUTH_SESSION);

    }

}

