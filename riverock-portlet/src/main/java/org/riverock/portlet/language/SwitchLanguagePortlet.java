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

import java.util.Enumeration;

import javax.portlet.*;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.common.utils.PortletUtils;

/**
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 9:56:36 PM
 *
 * $Id: SwitchLanguagePortlet.java 1229 2007-06-28 11:25:40Z serg_main $
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

    public void processAction( final ActionRequest actionRequest, final ActionResponse actionResponse )
        throws PortletException {

        try {

            if ( log.isDebugEnabled() ) {
                for ( Enumeration e = actionRequest.getParameterNames(); e.hasMoreElements(); ) {
                    String s = (String)e.nextElement();
                    log.debug( "PortletRequest.processAction param: "+s+", value - "+PortletUtils.getString(actionRequest, s, null) );
                }
            }
            Long siteLanguageId = PortletUtils.getLong( actionRequest, NAME_ID_LANGUAGE );
            if (log.isDebugEnabled()) {
                log.debug("siteLanguageId: " + siteLanguageId);
            }

            PortalDaoProvider provider = (PortalDaoProvider)actionRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );

            SiteLanguage siteLanguage = provider.getPortalSiteLanguageDao().getSiteLanguage(siteLanguageId);
            String languageLocaleName=null;
            if (siteLanguage==null || siteLanguage.getCustomLanguage()==null) {
                log.warn("locale for siteLanguageId: " + siteLanguageId +" not found");
            }
            else {
                languageLocaleName = siteLanguage.getCustomLanguage();
            }
            if (log.isDebugEnabled()) {
                log.debug("Language name: " + languageLocaleName);
            }

            String newUrl;
            if (languageLocaleName == null || languageLocaleName.length() == 0) {
                newUrl = actionResponse.encodeURL(PortletUtils.ctx( actionRequest ));
            }
            else {
                StringBuilder b =
                    PortletUtils.ctxStringBuilder( actionRequest, null, null, StringTools.getLocale(languageLocaleName) )
                        .append( '?' )
                        .append( ContainerConstants.NAME_LANG_PARAM )
                        .append( '=' )
                        .append( languageLocaleName );
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
