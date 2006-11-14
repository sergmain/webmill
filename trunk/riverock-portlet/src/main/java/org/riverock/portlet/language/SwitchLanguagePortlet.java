/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 9:56:36 PM
 *
 * $Id$
 */
public final class SwitchLanguagePortlet implements Portlet {
    private final static Logger log = Logger.getLogger( SwitchLanguagePortlet.class );
    public final static String NAME_ID_LANGUAGE   = "mill.id_language";

    public SwitchLanguagePortlet() {
    }

    protected PortletConfig portletConfig = null;
    public void init( final PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public void render( final RenderRequest renderRequest, final RenderResponse renderResponse )
        throws PortletException {
        throw new PortletException( "render() method must never invoked" );
    }

    private String getLanguageName( final Long idSiteLanguage ) throws Exception {
        final String sql_ =
            "select CUSTOM_LANGUAGE from WM_PORTAL_SITE_LANGUAGE " +
            "where ID_SITE_SUPPORT_LANGUAGE=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();
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
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
        return null;
    }

    public void processAction( final ActionRequest actionRequest, final ActionResponse actionResponse )
        throws PortletException {

        try {

            if ( log.isDebugEnabled() ) {
                for ( Enumeration e = actionRequest.getParameterNames(); e.hasMoreElements(); ) {
                    String s = (String)e.nextElement();
                    log.debug( "PortletRequest attr - "+s+", value - "+PortletService.getString(actionRequest, s, null) );
                }
            }
            Long id_lang = PortletService.getLong( actionRequest, NAME_ID_LANGUAGE );
            if (log.isDebugEnabled()) {
                log.debug("id_lang: " + id_lang);
            }

            String s = getLanguageName( id_lang );

            if (log.isDebugEnabled())
                log.debug("Language name: " + s);

            String newUrl;
            if (s == null || s.length() == 0) {
                newUrl = actionResponse.encodeURL(PortletService.ctx( actionRequest ));
            }
            else {
                StringBuilder b = null;
                b = PortletService.ctxStringBuilder( actionRequest, null, null, StringTools.getLocale(s) );
                b.append( '?' ).append( ContainerConstants.NAME_LANG_PARAM ).append( '=' ).append( s );
                newUrl = b.toString();
            }

            if (log.isDebugEnabled()) {
                log.debug("List current attribute in session:");
                log.debug("New url - " + newUrl);

                PortletSession session = actionRequest.getPortletSession();
                for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
                    String nameParam = (String) e.nextElement();
                    log.debug("#Attribute -  " + nameParam);
                }
            }

            actionResponse.sendRedirect(newUrl);
        }
        catch (Exception e) {
            String es = "Error switch language";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }
}
