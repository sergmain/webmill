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

 *

 * $Author$

 *

 * $Id$

 *

 */

package org.riverock.portlet.portlets.controller;



import java.io.IOException;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Enumeration;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.exception.DatabaseException;

import org.riverock.portlet.main.Constants;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.config.ConfigException;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portal.menu.MenuItemInterface;

import org.riverock.webmill.portlet.CtxURL;



public class LanguageServlet extends HttpServlet

{

    private static Logger log = Logger.getLogger( LanguageServlet.class );



    public LanguageServlet()

    {

    }



    private String getLanguageName(DatabaseAdapter db_, Long idSiteLanguage)

        throws SQLException

    {

        final String sql_ =

            "select CUSTOM_LANGUAGE " +

            "from SITE_SUPPORT_LANGUAGE a " +

            "where a.ID_SITE_SUPPORT_LANGUAGE=? ";



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSiteLanguage);



            rs = ps.executeQuery();



            if (rs.next())

                return RsetTools.getString(rs, "CUSTOM_LANGUAGE");

        }

        catch (SQLException e01)

        {

            log.error(e01);

            throw e01;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

        return null;

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        HttpSession session = request.getSession();

        InitPage jspPage = null;



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            try

            {

                jspPage = new InitPage(db_, request, response,

                    "mill.locale.site_hamradio",

                    Constants.NAME_LANG_PARAM, null, null);

            }

            catch (Exception e)

            {

                log.error("Error create InitPage ", e);

                throw  new ServletException(e);

            }



            Long id_ctx = ServletTools.getLong(request, Constants.NAME_ID_CONTEXT_PARAM);



            MenuItemInterface catItem = jspPage.menuLanguage.searchMenuItem( id_ctx );



            if (log.isDebugEnabled())

                log.debug("catItem is " + catItem);



            if (catItem == null)

            {

                response.sendRedirect( response.encodeURL( CtxURL.ctx() ) );

                return;

            }



            String s = getLanguageName(db_, catItem.getIdPortlet());



            if (log.isDebugEnabled())

                log.debug("Language name - " + s);



            String newUrl = null;

            if (s == null || s.length() == 0)

                newUrl = response.encodeURL( CtxURL.ctx() );

            else

                newUrl = response.encodeURL(CtxURL.ctx() + '?' +

                    Constants.NAME_LANG_PARAM + '=' + s

                );



            if (log.isDebugEnabled())

                log.debug("List current attribute in session:");



            for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();)

            {

                String nameParam = (String) e.nextElement();

                if (log.isDebugEnabled())

                    log.debug("#Attribute -  " + nameParam);

            }



//        session.removeAttribute(Constants.CURRENT_CATALOG_ITEM_SESSION);

            if (log.isDebugEnabled())

                log.debug("New url - " + newUrl);



            response.sendRedirect(newUrl );

            return;

        }

        catch (SQLException e)

        {

            log.error("SQLException", e);

            throw new ServletException(e.getMessage());

        }

        catch (DatabaseException e)

        {

            log.error("Error create DatabaseAdapter", e);

            throw new ServletException(e.toString());

        }

        catch (ConfigException e)

        {

            log.error("Error parse config file", e);

            throw new ServletException(e.getMessage());

        }

        finally

        {

            DatabaseAdapter.close( db_ );

            db_ = null;

        }



    }

}