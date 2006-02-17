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
package org.riverock.portlet.member;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.container.portal.bean.PortalTemplateImpl;

/**
 * $Revision$
 * $Date$
 * $RCSfile$
 */
public class SiteTemplateMember {
    private static Log log = LogFactory.getLog( SiteTemplateMember.class );

    private static CacheFactory cache = new CacheFactory( SiteTemplateMember.class.getName() );

    public Map<String, PortalTemplate> memberTemplate = new HashMap<String, PortalTemplate>();

    public void reinit() {
        cache.reinit();
    }

    public SiteTemplateMember() {
    }

    public static SiteTemplateMember getInstance( Long id__ ) throws Exception {
        return ( SiteTemplateMember ) cache.getInstanceNew( id__ );
    }

    protected void finalize() throws Throwable {
        if( memberTemplate != null ) {
            memberTemplate.clear();
            memberTemplate = null;
        }

        super.finalize();
    }

    static String sql_ = null;

    static {
        sql_ =
            "select b.NAME_SITE_TEMPLATE, b.TEMPLATE_DATA, c.CUSTOM_LANGUAGE " +
            "from   WM_PORTAL_TEMPLATE_MEMBER a, WM_PORTAL_TEMPLATE b, WM_PORTAL_SITE_LANGUAGE c " +
            "where  a.ID_SITE_TEMPLATE=b.ID_SITE_TEMPLATE and " +
            "       a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
            "       c.ID_SITE=? ";

        try {
            SqlStatement.registerSql( sql_, SiteTemplateMember.class );
        }
        catch( Throwable e ) {
            final String es = "Error in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public SiteTemplateMember( Long idSite )
        throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idSite );

            rs = ps.executeQuery();

            while( rs.next() ) {
                PortalTemplateImpl st = ( PortalTemplateImpl ) Unmarshaller.unmarshal( PortalTemplateImpl.class,
                    new InputSource( new StringReader( RsetTools.getString( rs, "TEMPLATE_DATA" ) ) ) );

                st.setTemplateName( RsetTools.getString( rs, "NAME_SITE_TEMPLATE" ) );

                memberTemplate.put( StringTools.getLocale( RsetTools.getString( rs, "CUSTOM_LANGUAGE" ) ).toString(),
                    st );
            }
        }
        catch( Exception e ) {
            log.error( "Exception process SiteTemplate Member", e );
            throw e;
        }
        catch( Error e ) {
            log.error( "Error process SiteTemplate Member", e );
            throw e;
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }

        if( log.isDebugEnabled() ) {
            Object[] obj = memberTemplate.entrySet().toArray();

            for( final Object newVar : obj ) {
                Map.Entry entry = ( Map.Entry ) newVar;
                log.debug( "hashmap key - " + entry.getKey() + " value " + entry.getValue() );
            }
        }
    }
}