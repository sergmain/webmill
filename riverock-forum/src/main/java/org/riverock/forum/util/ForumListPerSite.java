/*
 * org.riverock.forum - Forum portlet
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
package org.riverock.forum.util;

import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.generic.db.Database;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;

/**
 * @author SMaslyukov
 *         Date: 16.04.2005
 *         Time: 17:47:12
 *         $Id: ForumListPerSite.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public class ForumListPerSite implements PortletGetList {
    private static Logger log = Logger.getLogger( ForumListPerSite.class );

    public ForumListPerSite(){}

    PortalDaoProvider provider=null;
    public void setPortalDaoProvider(PortalDaoProvider provider) {
        this.provider=provider;
    }

    public List<ClassQueryItem> getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        if (log.isDebugEnabled()) {
            log.debug("Get list of ForumInstance. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog+", idContext: "+idContext);
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Database db_ = null;

        Long siteId = provider.getPortalCatalogDao().getSiteId(idSiteCtxLangCatalog);
        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        try {
            db_ = Database.getInstance();
            ps = db_.prepareStatement(
                    "select b.FORUM_ID, b.FORUM_NAME, b.SITE_ID "+
                    "from   WM_FORUM b "+
                    "where  b.SITE_ID=? "
            );

            RsetTools.setLong(ps, 1, siteId );

            rs = ps.executeQuery();
            while (rs.next())
            {
                Long id = RsetTools.getLong(rs, "FORUM_ID");
                String name = "" + id + ", " + RsetTools.getString(rs, "FORUM_NAME");

                ClassQueryItem item =
                        new ClassQueryItemImpl(id, StringTools.truncateString(name, 60) );

                if (item.getIndex().equals(idContext))
                    item.setSelected( true );

                v.add( item );
            }
            return v;

        }
        catch(Exception e)
        {
            log.error("Get list of ForumInstance. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);
            return null;
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}