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
 * $Revision$
 * $Date$
 * $RCSfile$
 *
 */
package org.riverock.portlet.member;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.webmill.container.schema.site.SiteTemplate;


import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SiteTemplateMember {
    private static Log log = LogFactory.getLog( SiteTemplateMember.class );

    private static CacheFactory cache = new CacheFactory( SiteTemplateMember.class.getName() );

    public Map memberTemplate = new HashMap();

    public void reinit()
    {
        cache.reinit();
    }

    public SiteTemplateMember()
    {
    }

    public static SiteTemplateMember getInstance(DatabaseAdapter db__, long id__) throws Exception {
        return getInstance(db__, id__ );
    }

    public static SiteTemplateMember getInstance(DatabaseAdapter db__, Long id__)
            throws Exception
    {
        return (SiteTemplateMember) cache.getInstanceNew(db__, id__);
    }

    protected void finalize() throws Throwable
    {
        if (memberTemplate != null)
        {
            memberTemplate.clear();
            memberTemplate = null;
        }

        super.finalize();
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select b.NAME_SITE_TEMPLATE, b.TEMPLATE_DATA, c.CUSTOM_LANGUAGE "+
            "from   WM_PORTAL_TEMPLATE_MEMBER a, WM_PORTAL_TEMPLATE b, WM_PORTAL_SITE_LANGUAGE c "+
            "where  a.ID_SITE_TEMPLATE=b.ID_SITE_TEMPLATE and "+
            "       a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and "+
            "       c.ID_SITE=? ";

        try
        {
            SqlStatement.registerSql( sql_, new SiteTemplateMember().getClass() );
        }
        catch(Exception e)
        {
            log.error("Exception in registerSql, sql\n"+sql_, e);
        }
        catch(Error e)
        {
            log.error("Error in registerSql, sql\n"+sql_, e);
        }
    }

    public SiteTemplateMember(DatabaseAdapter db_, Long idSite)
            throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSite );

            rs = ps.executeQuery();

            while (rs.next())
            {
                SiteTemplate st = (SiteTemplate) Unmarshaller.unmarshal(SiteTemplate.class,
                    new InputSource(
                        new StringReader( RsetTools.getString(rs, "TEMPLATE_DATA") )
                    )
                );

                st.setNameTemplate( RsetTools.getString(rs, "NAME_SITE_TEMPLATE") );

                memberTemplate.put(
                    StringTools.getLocale( RsetTools.getString(rs, "CUSTOM_LANGUAGE") ).toString(),
                    st );
            }
        }
        catch(Exception e)
        {
            log.error("Exception process SiteTemplate Member", e);
            throw e;
        }
        catch(Error e)
        {
            log.error("Error process SiteTemplate Member", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        if (log.isDebugEnabled())
        {
            Object[] obj = memberTemplate.entrySet() .toArray();

            for( final Object newVar : obj ) {
                Map.Entry entry = ( Map.Entry ) newVar;
                log.debug( "hashmap key - " + entry.getKey() + " value " + entry.getValue() );
            }
        }
    }
}