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
package org.riverock.portlet.member.context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.member.ClassQueryItemImpl;


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

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        if( log.isDebugEnabled() )
            log.debug( "Get list of Language. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( "SELECT d.ID_SITE_SUPPORT_LANGUAGE, d.CUSTOM_LANGUAGE, d.NAME_CUSTOM_LANGUAGE " +
                "FROM   WM_PORTAL_CATALOG_LANGUAGE a, WM_PORTAL_SITE_LANGUAGE c, WM_PORTAL_SITE_LANGUAGE d " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=? and " +
                "       a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=d.ID_SITE" );
            RsetTools.setLong( ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while( rs.next() ) {
                Long id = RsetTools.getLong( rs, "ID_SITE_SUPPORT_LANGUAGE" );
                String name = "" + id + ", " +
                    StringTools.getLocale( RsetTools.getString( rs, "CUSTOM_LANGUAGE" ) ).toString() + ", " +
                    RsetTools.getString( rs, "NAME_CUSTOM_LANGUAGE" );

                ClassQueryItem item =
                    new ClassQueryItemImpl( id, StringTools.truncateString( name, 60 ) );

                if( item.getIndex().equals( idContext ) )
                    item.setSelected( true );

                v.add( item );
            }
            return v;

        }
        catch( Exception e ) {
            log.error( "Get list of Language. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e );
            return null;
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
