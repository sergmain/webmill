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

package org.riverock.webmill.main;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;

import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.utils.ServletUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.tools.StringTools;

/**
 * Author: mill
 * Date: Nov 22, 2002
 * Time: 3:13:52 PM
 *
 * $Id$
 */
public class ServletCSS extends HttpServlet
{
    private static Logger log = Logger.getLogger("org.riverock.webmill.main.ServletCSS");

    public ServletCSS()
    {
    }

    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response_)
            throws IOException, ServletException
    {
        DatabaseAdapter db_ = null;
        try
        {
            Writer out = response_.getWriter();

            db_ = DatabaseAdapter.getInstance(false);
            PortalInfo p = PortalInfo.getInstance(db_, request_.getServerName());

            if (log.isDebugEnabled())
                log.debug("Dynamic status " + p.sites.getIsCssDynamic());

            if (Boolean.TRUE.equals(p.sites.getIsCssDynamic()) ) {
                if (log.isDebugEnabled()) log.debug("ID_SITE " + p.sites.getIdSite());
                if (log.isDebugEnabled()) log.debug("p.getDefaultLocale().toString() " + p.getDefaultLocale().toString());
                if (log.isDebugEnabled()) log.debug("request_.getParameter " + request_.getParameter("mill.lang"));

                Locale locale = StringTools.getLocale(ServletUtils.getString(request_, "mill.lang", p.getDefaultLocale().toString() ));
                if (log.isDebugEnabled()) log.debug("locale: '" + locale.toString()+"'");

                Long siteSupportLanguageId = p.getIdSupportLanguage(locale);
                if (log.isDebugEnabled()) log.debug("siteSupportLanguageId: " + siteSupportLanguageId);

                ContentCSS css = ContentCSS.getInstance(db_, siteSupportLanguageId);
                if (css == null || css.css.length() == 0)
                {
                    out.write("<style type=\"text/css\"><!-- -->");
                    return;
                }
                out.write(css.css);
                return;
            }

            String cssFile = (p.sites.getCssFile() != null ? p.sites.getCssFile() : "//front_styles.css");

            if (log.isDebugEnabled())
                log.debug("forvard to static CSS: " + cssFile);

            RequestDispatcher dispatcher = request_.getRequestDispatcher(cssFile);
            if (log.isDebugEnabled())
                log.debug("RequestDispatcher - " + dispatcher);

            if (dispatcher == null)
            {
                if (log.isDebugEnabled())
                    log.debug("RequestDispatcher is null");

                out.write("Error get dispatcher for path " + cssFile);
            }
            dispatcher.forward(request_, response_);
            return;
        }
        catch (Exception e)
        {
            log.error("Error processing ServletCSS", e);
            throw new ServletException(e);
        }
    }
}
