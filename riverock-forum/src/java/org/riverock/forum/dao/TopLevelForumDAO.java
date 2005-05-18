package org.riverock.forum.dao;

import java.util.LinkedList;
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
import org.riverock.module.web.request.ModuleRequest;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 18:52:15
 *         $Id$
 */
public class TopLevelForumDAO {
    private final static Logger log = Logger.getLogger(TopLevelForumDAO.class);

    public ForumTopLevelBean execute(ModuleRequest moduleRequest) throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmForumListType forumsCore = new GetWmForumWithSiteIdList(adapter, moduleRequest.getServerNameId()).item;
            ForumTopLevelBean forumTopLevelBean = new ForumTopLevelBean();
            List forums = new LinkedList();
            forumTopLevelBean.setForums( forums );
            for (int i=0; i<forumsCore.getWmForumCount(); i++) {
                WmForumItemType item = forumsCore.getWmForum(i);

                ForumBean forum = new ForumBean();
                forum.setForumId( new Long(item.getForumId().intValue()) );
                forum.setForumName( item.getForumName() );
                forum.setDeleted( item.getIsDeleted().booleanValue() );

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
