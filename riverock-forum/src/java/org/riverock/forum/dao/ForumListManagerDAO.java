package org.riverock.forum.dao;

import org.apache.log4j.Logger;
import org.riverock.common.tools.RsetTools;
import org.riverock.forum.core.InsertWmForumItem;
import org.riverock.forum.core.UpdateWmForumItem;
import org.riverock.forum.exception.PersistenceException;
import org.riverock.forum.schema.core.WmForumItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.exception.ModuleException;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.user.ModuleUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author SMaslyukov
 *         Date: 21.04.2005
 *         Time: 11:25:39
 *         $Id$
 */
public class ForumListManagerDAO {
    private final static Logger log = Logger.getLogger(ForumListManagerDAO.class);

    public void execute(ModuleUser auth_, ModuleActionRequest moduleActionRequest, UrlProvider urlProvider) throws ActionException {

        log.debug("in execute()");

        String subAction = moduleActionRequest.getRequest().getString( Constants.NAME_SUB_ACTION);

        if (log.isDebugEnabled()) {
            log.debug("subAction: "+subAction);
        }

        if (subAction==null){
            return;
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            WmForumItemType forum = getForum(adapter, moduleActionRequest);

            if (subAction.equals("update-forum")) {
                updateForum(auth_, moduleActionRequest, adapter, forum );
            }
            else if (subAction.equals("add-forum")) {
                addNewForum(auth_, moduleActionRequest, adapter);
            }
            else if (subAction.equals("delete-forum")) {
                deleteForum(auth_, moduleActionRequest, false, adapter, forum);
            }
            else if (subAction.equals("permanent-delete-forum")) {
                deleteForum(auth_, moduleActionRequest, true, adapter, forum);
            }
            else if (subAction.equals("restore-forum")) {
                restoreForum(auth_, moduleActionRequest, adapter, forum);
            }
            adapter.commit();
        }
        catch (Exception e) {
            String es = "Error process admin action, sub action: "+subAction;
            log.error(es, e);
            throw new ActionException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
        log.debug("out execute()");
    }

    private static WmForumItemType getForum(DatabaseAdapter adapter, ModuleActionRequest moduleActionRequest) {
        Long forumId = moduleActionRequest.getRequest().getLong("forumId");
        if (forumId==null) {
            return null;
        }
        return CommonUtils.checkForumId( adapter, forumId, moduleActionRequest.getRequest().getServerName() );
    }

    private void restoreForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter, WmForumItemType forum) throws SQLException, DatabaseException {
        log.debug("in restoreForum()");

        if (forum==null) {
            return;
        }

        DatabaseManager.runSQL(
            adapter,
            "update WM_FORUM " +
            "set    IS_DELETED=0 " +
            "where  FORUM_ID=? ",
            new Object[] {forum.getForumId() },
            new int[] { Types.INTEGER }
        );
        log.debug("out restoreForum()");

    }

    private void deleteForum(ModuleUser auth_, ModuleActionRequest moduleActionRequest, boolean isPermanent, DatabaseAdapter adapter, WmForumItemType forum) throws SQLException, DatabaseException {
        log.debug("in deleteForum()");

        if (forum==null) {
            return;
        }

        int checkBox = moduleActionRequest.getRequest().getInt( "confirm-delete", new Integer(0) ).intValue();
        if (log.isDebugEnabled()) {
            log.debug("checkBox: "+checkBox);
        }
        if (checkBox!=1) {
            return;
        }

        if (isPermanent) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = adapter.prepareStatement(
                    "select a.F_ID " +
                    "from   WM_FORUM_CONCRETE a , WM_FORUM_CATEGORY b " +
                    "where  a.FORUM_CATEGORY_ID=b.FORUM_CATEGORY_ID and b.FORUM_ID=?"
                );
                ps.setInt(1, forum.getForumId().intValue());
                rs = ps.executeQuery();

                while (rs.next()) {
                    CommonDAO.deleteForumConcrete(adapter, RsetTools.getInt(rs, "F_ID"));
                }
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM_CATEGORY where FORUM_ID=? ",
                    new Object[] { forum.getForumId() },
                    new int[] { Types.INTEGER }
                );
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM where FORUM_ID=? ",
                    new Object[] { forum.getForumId() },
                    new int[] { Types.INTEGER }
                );
            }
            finally {
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }
        }
        else {
            DatabaseManager.runSQL(
                adapter,
                "update WM_FORUM " +
                "set    IS_DELETED=1 " +
                "where  FORUM_ID=? ",
                new Object[] { forum.getForumId() },
                new int[] { Types.INTEGER }
            );
        }
        log.debug("out deleteForum()");

    }

    private void addNewForum(ModuleUser auth_, ModuleActionRequest moduleActionRequest, DatabaseAdapter adapter) throws SQLException, PersistenceException, ModuleException  {
        log.debug("in addNewForum()");

        WmForumItemType forum = new WmForumItemType();
        forum.setForumName( moduleActionRequest.getRequest().getString( "forum-name" ) );
        forum.setForumId( new Integer(CommonDAO.getForumID( adapter )) );
        forum.setSiteId( new Integer(moduleActionRequest.getRequest().getServerNameId().intValue()) );
        forum.setIsDeleted( Boolean.FALSE );
        forum.setIsUseLocale( Boolean.FALSE );

        InsertWmForumItem.process( adapter, forum );

        log.debug("out addNewForum()");
    }

    private void updateForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter, WmForumItemType forum) throws SQLException, PersistenceException {
        log.debug("in updateForum()");

        if (forum==null) {
            return;
        }

        forum.setForumName( forumActionBean.getRequest().getString( "forum-name" ));
        forum.setIsUseLocale( Boolean.FALSE );
        UpdateWmForumItem.process( adapter, forum );

        log.debug("out updateForum()");
    }
}
