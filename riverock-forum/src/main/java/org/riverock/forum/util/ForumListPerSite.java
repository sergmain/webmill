/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.forum.util;

import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
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
 *         $Id$
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
        DatabaseAdapter db_ = null;

        Long siteId = provider.getPortalCatalogDao().getSiteId(idSiteCtxLangCatalog);
        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        try {
            db_ = DatabaseAdapter.getInstance();
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