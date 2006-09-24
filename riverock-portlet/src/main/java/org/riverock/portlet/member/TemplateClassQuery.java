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
package org.riverock.portlet.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * User: Serge Maslyukov
 * Date: Nov 24, 2002
 * Time: 3:58:34 PM
 * <p/>
 * $Id$
 */
public class TemplateClassQuery extends BaseClassQuery {
    private static Logger log = Logger.getLogger( TemplateClassQuery.class );

    private Long idSiteCtxLangCatalog = null;

    private Long idSiteTemplate = null;

    public void setIdSiteCtxLangCatalog( Long param ) {
        idSiteCtxLangCatalog = param;

        if( log.isDebugEnabled() )
            log.debug( "idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );
    }

    public void setIdSiteTemplate( Long param ) {
        idSiteTemplate = param;

        if( log.isDebugEnabled() )
            log.debug( "idSiteTemplate - " + idSiteTemplate );
    }

    /**
     * Return current value for output on web page
     *
     * @return String
     */
    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( "select NAME_SITE_TEMPLATE from WM_PORTAL_TEMPLATE where ID_SITE_TEMPLATE = ?" );

            RsetTools.setLong( ps, 1, idSiteTemplate );

            rs = ps.executeQuery();
            if( rs.next() )
                return RsetTools.getString( rs, "NAME_SITE_TEMPLATE" );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }

        return "";
    }

    /**
     * Return list of possible values for create 'select' html-element
     *
     * @return List of org.riverock.member.ClassQueryItem
     */
    public List<ClassQueryItem> getSelectList( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( "select a.ID_SITE_TEMPLATE, a.NAME_SITE_TEMPLATE " +
                "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_CATALOG_LANGUAGE b " +
                "where  a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and " +
                "       b.ID_SITE_CTX_LANG_CATALOG = ?" );

            RsetTools.setLong( ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while( rs.next() ) {
                ClassQueryItem item = new ClassQueryItemImpl( RsetTools.getLong( rs, "ID_SITE_TEMPLATE" ),
                    RsetTools.getString( rs, "NAME_SITE_TEMPLATE" ) );

                if( item.getIndex().equals( idSiteTemplate ) )
                    item.setSelected( true );

                v.add( item );
            }
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }

        return v;
    }

    MemberQueryParameter param = null;

    public void setQueryParameter( MemberQueryParameter parameter ) throws Exception {
        this.param = parameter;
    }
}
