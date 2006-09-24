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
package org.riverock.forum.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.bean.ForumTopLevelBean;
import org.riverock.forum.core.GetWmForumWithSiteIdList;
import org.riverock.forum.schema.core.WmForumItemType;
import org.riverock.forum.schema.core.WmForumListType;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 18:52:15
 *         $Id$
 */
public class TopLevelForumDAO {
    private final static Logger log = Logger.getLogger(TopLevelForumDAO.class);

    public ForumTopLevelBean execute(Long siteId) throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
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
