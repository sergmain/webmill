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

 * Date: Dec 2, 2002

 * Time: 9:56:36 PM

 *

 * $Id$

 */

package org.riverock.portlet.servlets.controller;



import org.apache.log4j.Logger;



import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Enumeration;



import org.riverock.webmill.port.InitPage;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.portlet.main.Constants;



public class SwitchLanguage extends HttpServlet

{

    private static Logger log = Logger.getLogger(SwitchLanguage.class);



    public SwitchLanguage()

    {

    }



    private String getLanguageName( Long idSiteLanguage )

        throws Exception

    {

        final String sql_ =

            "select CUSTOM_LANGUAGE from SITE_SUPPORT_LANGUAGE " +

            "where ID_SITE_SUPPORT_LANGUAGE=? ";



        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSiteLanguage);



            rs = ps.executeQuery();



            if (rs.next())

                return RsetTools.getString(rs, "CUSTOM_LANGUAGE");

        }

        catch (Exception e01)

        {

            log.error("Error get code of language", e01);

            throw e01;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;



            DatabaseAdapter.close( db_ );

            db_ = null;

        }

        return null;

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            InitPage.setContentType(response);



            HttpSession session = request.getSession();



/*

            DatabaseAdapter db_ = null;

            try

            {

                db_ = DatabaseAdapter.getInstance(false);

            }

            catch (Exception e)

            {

                cat.error("Error create DatabaseAdapter", e);

                throw  new ServletException(e);

            }



            try

            {

                jspPage = new InitPage(db_, request, response,

                        CtxURL.ctx(), "mill.locale.site_hamradio",

                        Constants.NAME_LANG_PARAM, null, null);

            }

            catch (Exception e)

            {

                cat.error("Error create InitPage ", e);

                throw  new ServletException(e);

            }

*/

            Long id_lang = ServletTools.getLong(request, Constants.NAME_ID_LANGUAGE);



            String s = getLanguageName( id_lang );



            if (log.isDebugEnabled())

                log.debug("Language name - " + s);



            String newUrl = null;

            if (s == null || s.length() == 0)

                newUrl = response.encodeURL(CtxURL.ctx());

            else

                newUrl = response.encodeURL(CtxURL.ctx() + '?' +

                    Constants.NAME_LANG_PARAM + "=" + s

                );



            if (log.isDebugEnabled())

                log.debug("List current attribute in session:");



            for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();)

            {

                String nameParam = (String) e.nextElement();

                if (log.isDebugEnabled())

                    log.debug("#Attribute -  " + nameParam);

            }



//    session.removeAttribute(Constants.CURRENT_CATALOG_ITEM_SESSION);

            if (log.isDebugEnabled())

                log.debug("New url - " + newUrl);



            response.sendRedirect(newUrl);

            return;





        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }



}

