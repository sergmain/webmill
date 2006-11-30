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
package org.riverock.portlet.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;

/**
 * User: Admin
 * Date: Jan 12, 2003
 * Time: 8:11:48 PM
 * <p/>
 * $Id$
 */
public class LanguagePerSite implements PortletGetList {
    private static Logger log = Logger.getLogger( LanguagePerSite.class );

    public LanguagePerSite() {
    }

    private PortalDaoProvider provider;
    public void setPortalDaoProvider(PortalDaoProvider provider) {
        this.provider=provider;
    }

    public List<ClassQueryItem> getList( Long catalogLanguageId, Long idContext ) {
        if( log.isDebugEnabled() )
            log.debug( "Get list of Language. catalogLanguageId - " + catalogLanguageId);

        try {
            List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
            List<SiteLanguage> siteLanguages = provider.getPortalSiteLanguageDao().getSiteLanguageList(provider.getPortalCatalogDao().getSiteId(catalogLanguageId));
            for (SiteLanguage siteLanguage : siteLanguages) {
                String name = "" + siteLanguage.getSiteLanguageId() + ", " +
                    StringTools.getLocale( siteLanguage.getCustomLanguage() ).toString() + ", " +
                    siteLanguage.getNameCustomLanguage();

                ClassQueryItem item =
                    new ClassQueryItemImpl( siteLanguage.getSiteLanguageId(), StringTools.truncateString( name, 60 ) );

                if( item.getIndex().equals( idContext ) )
                    item.setSelected( true );

                v.add( item );

            }
            return v;
        }
        catch( Exception e ) {
            log.error( "Get list of Language. catalogLanguageId - " + catalogLanguageId, e );
            return null;
        }
    }
}
