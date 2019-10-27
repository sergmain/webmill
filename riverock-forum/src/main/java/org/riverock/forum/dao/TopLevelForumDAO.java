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
package org.riverock.forum.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.bean.ForumTopLevelBean;
import org.riverock.forum.dao.core.GetWmForumWithSiteIdList;
import org.riverock.forum.dao.bean.WmForumItemType;
import org.riverock.forum.dao.bean.WmForumListType;
import org.riverock.generic.db.Database;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 18:52:15
 *         $Id: TopLevelForumDAO.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public class TopLevelForumDAO {
    private final static Logger log = Logger.getLogger(TopLevelForumDAO.class);

    public ForumTopLevelBean execute(Long siteId) throws ActionException {
        Database adapter = null;
        try {
            adapter = Database.getInstance();
            WmForumListType forumsCore = new GetWmForumWithSiteIdList(adapter, siteId).item;
            ForumTopLevelBean forumTopLevelBean = new ForumTopLevelBean();
            List<ForumBean> forums = new ArrayList<ForumBean>();
            forumTopLevelBean.setForums( forums );
            for (int i=0; i<forumsCore.getWmForumCount(); i++) {
                WmForumItemType item = forumsCore.getWmForum(i);

                ForumBean forum = new ForumBean();
                forum.setForumId( item.getForumId().longValue() );
                forum.setForumName( item.getForumName() );
                forum.setDeleted( item.getIsDeleted() );

                forums.add(forum);
            }

            return forumTopLevelBean;
        }
        catch (Exception e) {
            String es = "Error execute TopLevelForumDAO";
            log.error(es, e);
            throw new ActionException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }
}
