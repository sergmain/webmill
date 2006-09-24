/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
