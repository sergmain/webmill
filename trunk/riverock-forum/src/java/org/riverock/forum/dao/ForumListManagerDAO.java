package org.riverock.forum.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.exception.PersistenceException;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.core.InsertWmForumCategoryItem;
import org.riverock.forum.core.InsertWmForumConcreteItem;
import org.riverock.forum.schema.core.WmForumCategoryItemType;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.forum.schema.core.WmForumItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.tools.RsetTools;

/**
 * @author SMaslyukov
 *         Date: 21.04.2005
 *         Time: 11:25:39
 *         $Id$
 */
public class ForumListManagerDAO {
    private final static Logger log = Logger.getLogger(ForumListManagerDAO.class);

    public void execute(ModuleUser auth_, ModuleActionRequest forumActionBean, UrlProvider urlProvider) throws ActionException {

        log.debug("in execute()");

        String subAction = forumActionBean.getRequest().getString( Constants.NAME_SUB_ACTION);

        if (subAction==null){
            log.warn("parameter "+Constants.NAME_SUB_ACTION+" is null");
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("subAction: "+subAction);
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (subAction.equals("update-forum")) {
                updateForum(auth_, forumActionBean, adapter );
            }
            else if (subAction.equals("add-forum")) {
                addNewForum(auth_, forumActionBean, adapter);
            }
            else if (subAction.equals("delete-forum")) {
                deleteForum(auth_, forumActionBean, false, adapter);
            }
            else if (subAction.equals("permanent-delete-forum")) {
                deleteForum(auth_, forumActionBean, true, adapter);
            }
            else if (subAction.equals("restore-forum")) {
                restoreForum(auth_, forumActionBean, adapter);
            }
            adapter.commit();
        }
        catch (SQLException e) {
            String es = "Error process admin action, sub action: "+subAction;
            log.error(es, e);
            throw new ActionException(es, e);
        }
        catch (PersistenceException e) {
            String es = "Error process admin action, sub action: "+subAction;
            log.error(es, e);
            throw new ActionException(es, e);
        }
        catch (DatabaseException e) {
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

    private void restoreForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter) throws SQLException, DatabaseException {
        log.debug("in restoreForum()");

//        int checkBox = PortletTools.getInt(forumActionBean.getRequest(), "confirm-delete", new Integer(0) ).intValue();
        Integer forumConcreteId = forumActionBean.getRequest().getInt( "forumConcreteId" );
        if (log.isDebugEnabled()) {
//            log.debug("checkBox: "+checkBox);
            log.debug("forumConcreteId: "+forumConcreteId);
        }
//        if (checkBox!=1) {
//            return;
//        }
        if (forumConcreteId==null) {
            return;
        }

        DatabaseManager.runSQL(
            adapter,
            "update WM_FORUM_CONCRETE " +
            "set    IS_DELETED=0 " +
            "where  F_ID=? ",
            new Object[] {forumConcreteId },
            new int[] { Types.INTEGER }
        );
        log.debug("out restoreForum()");

    }

    private void deleteForum(ModuleUser auth_, ModuleActionRequest forumActionBean, boolean isPermanent, DatabaseAdapter adapter) throws SQLException, DatabaseException {
        log.debug("in deleteForum()");

        int checkBox = forumActionBean.getRequest().getInt( "confirm-delete", new Integer(0) ).intValue();
        Integer forumConcreteId = forumActionBean.getRequest().getInt( "forumConcreteId" );
        if (log.isDebugEnabled()) {
            log.debug("checkBox: "+checkBox);
            log.debug("forumConcreteId: "+forumConcreteId);
        }
        if (checkBox!=1) {
            return;
        }
        if (forumConcreteId==null) {
            return;
        }

        if (isPermanent) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = adapter.prepareStatement(
                    "select a.T_ID from WM_FORUM_TOPIC a where T_F_ID=?"
                );
                ps.setInt(1, forumConcreteId.intValue());
                rs = ps.executeQuery();

                while (rs.next()) {
                    DatabaseManager.runSQL(
                        adapter,
                        "delete from WM_FORUM_MESSAGE where M_T_ID=? ",
                        new Object[] { RsetTools.getLong(rs, "T_ID") },
                        new int[] { Types.INTEGER }
                    );
                }
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM_TOPIC where T_F_ID=? ",
                    new Object[] { forumConcreteId },
                    new int[] { Types.INTEGER }
                );
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM_CONCRETE where F_ID=? ",
                    new Object[] { forumConcreteId },
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
                "update WM_FORUM_CONCRETE " +
                "set    IS_DELETED=1 " +
                "where  F_ID=? ",
                new Object[] {forumConcreteId },
                new int[] { Types.INTEGER }
            );
        }
        log.debug("out deleteForum()");

    }

    private void addNewForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter) throws SQLException, PersistenceException  {
        log.debug("in addNewForum()");

        WmForumItemType item = new WmForumItemType();
        item.setForumName( forumActionBean.getRequest().getString( "forum-name" ) );
        item.setForumId( new Integer(CommonDAO.getForumConcreteID( adapter )) );
/*
        item.setForumCategoryId( forumCategoryId );

        // hack - set moderator and last poster to admin userID
        item.setFUId( new Integer(auth_.getId().intValue()) );
        item.setFUId2( new Integer(auth_.getId().intValue()) );

        InsertWmForumConcreteItem.process( adapter, item );
*/
        log.debug("out addNewForum()");
    }

    private void updateForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter) throws SQLException, PersistenceException {
        log.debug("in updateForum()");

        WmForumCategoryItemType item = new WmForumCategoryItemType();
        item.setForumCategoryName( forumActionBean.getRequest().getString( "forum-category-name" ));
//        item.setForumId( new Integer(forumId.intValue()) );
        item.setForumCategoryId( new Integer(CommonDAO.getForumConcreteID( adapter )) );
        item.setIsUseLocale( Boolean.FALSE );
        InsertWmForumCategoryItem.process( adapter, item );

        log.debug("out updateForum()");
    }
}
