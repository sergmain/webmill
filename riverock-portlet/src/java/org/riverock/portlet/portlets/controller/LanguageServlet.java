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
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.portlet.main.Constants;
import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;

/**
 * @author Serge Maslyukov
 * $Id$
 */
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

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        DatabaseAdapter db_ = null;
        try
        {
//            CtxInstance ctxInstance =
//                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            RenderRequest renderRequest = null;
            PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute(PortalConstants.PORTAL_INFO_ATTRIBUTE);

            db_ = DatabaseAdapter.getInstance(false);

//            try
//            {
//                jspPage = new InitPage(db_, request,
//                                       "mill.locale.site_hamradio"
//                );
//            }
//            catch (Exception e)
//            {
//                log.error("Error create InitPage ", e);
//                throw  new ServletException(e);
//            }

            Long id_ctx = PortletTools.getLong(renderRequest, Constants.NAME_ID_CONTEXT_PARAM);
            MenuItemInterface catItem =
                portalInfo.getMenu(
                    renderRequest.getLocale().toString()
                ).searchMenuItem( id_ctx );

            if (log.isDebugEnabled())
                log.debug("catItem is " + catItem);

            if (catItem == null){
                response.sendRedirect( response.encodeURL( CtxInstance.ctx() ) );
                return;
            }

            String s = getLanguageName(db_, catItem.getIdPortlet());

            if (log.isDebugEnabled())
                log.debug("Language name - " + s);

            String newUrl = null;
            if (s == null || s.length() == 0)
                newUrl = response.encodeURL( CtxInstance.ctx() );
            else
                newUrl = response.encodeURL( CtxInstance.ctx() + '?' +
                    Constants.NAME_LANG_PARAM + '=' + s
                );

            if (log.isDebugEnabled()){
                log.debug("List current attribute in session:");

                for (Enumeration e = renderRequest.getAttributeNames(); e.hasMoreElements();){
                    String nameParam = (String) e.nextElement();
                    log.debug("#Attribute -  " + nameParam);
                }

                log.debug("New url - " + newUrl);
            }

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