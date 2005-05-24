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
import org.riverock.forum.core.InsertWmForumCategoryItem;
import org.riverock.forum.core.InsertWmForumConcreteItem;
import org.riverock.forum.schema.core.WmForumCategoryItemType;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
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
public class AdminForumDAO {
    private final static Logger log = Logger.getLogger(AdminForumDAO.class);

    public void execute(ModuleUser auth_, ForumActionBean forumActionBean, UrlProvider urlProvider, Long forumId) throws ActionException {

        log.debug("in execute()");

        String subAction = forumActionBean.getRequest().getString( Constants.NAME_SUB_ACTION);

        if (subAction==null){
            log.warn("parameter "+Constants.NAME_SUB_ACTION+" is null");
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("subAction: "+subAction);
        }

        try {
            if (subAction.equals("add-new-category")) {
                addNewCategory(auth_, forumActionBean, forumId);
            }
            else if (subAction.equals("add-new-forum")) {
                addNewForumConcrete(auth_, forumActionBean, forumId);
            }
            else if (subAction.equals("delete-forum")) {
                deleteForumConcrete(auth_, forumActionBean, forumId, false);
            }
            else if (subAction.equals("permanent-delete-forum")) {
                deleteForumConcrete(auth_, forumActionBean, forumId, true);
            }
            else if (subAction.equals("restore-forum")) {
                restoreForumConcrete(auth_, forumActionBean, forumId);
            }
            forumActionBean.getAdapter().commit();
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
        log.debug("out execute()");
    }

    private void restoreForumConcrete(ModuleUser auth_, ForumActionBean forumActionBean, Long forumId) throws SQLException, DatabaseException {
        log.debug("in restoreForumConcrete()");

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
        if (!CommonUtils.checkForumConcreteId(forumActionBean.getAdapter(), forumId, forumConcreteId)) {
            log.warn("Erorr check valied of forumConcreteId");
            return;
        }

        DatabaseManager.runSQL(
            forumActionBean.getAdapter(),
            "update WM_FORUM_CONCRETE " +
            "set    IS_DELETED=0 " +
            "where  F_ID=? ",
            new Object[] {forumConcreteId },
            new int[] { Types.INTEGER }
        );
        log.debug("out restoreForumConcrete()");

    }

    private void deleteForumConcrete(ModuleUser auth_, ForumActionBean forumActionBean, Long forumId, boolean isPermanent) throws SQLException, DatabaseException {
        log.debug("in deleteForumConcrete()");

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
        if (!CommonUtils.checkForumConcreteId(forumActionBean.getAdapter(), forumId, forumConcreteId)) {
            log.warn("Erorr check valied of forumConcreteId");
            return;
        }

        if (isPermanent) {
            CommonDAO.deleteForumConcrete(forumActionBean.getAdapter(), forumConcreteId);
        }
        else {
            DatabaseManager.runSQL(
                forumActionBean.getAdapter(),
                "update WM_FORUM_CONCRETE " +
                "set    IS_DELETED=1 " +
                "where  F_ID=? ",
                new Object[] {forumConcreteId },
                new int[] { Types.INTEGER }
            );
        }
        log.debug("out deleteForumConcrete()");

    }

    private void addNewForumConcrete(ModuleUser auth_, ForumActionBean forumActionBean, Long forumId) throws SQLException, PersistenceException  {
        log.debug("in addNewForumConcrete()");

        Integer forumCategoryId = forumActionBean.getRequest().getInt( "forumCategoryId" );
        if (forumCategoryId==null) {
            return;
        }
        if (!CommonUtils.checkForumCategoryId(forumActionBean.getAdapter(), forumId, forumCategoryId)) {
            return;
        }

        WmForumConcreteItemType item = new WmForumConcreteItemType();
        item.setFName( forumActionBean.getRequest().getString( "forum-name" ) );
        item.setFInfo( forumActionBean.getRequest().getString( "forum-info" ) );
        item.setFId( new Integer(CommonDAO.getForumConcreteID( forumActionBean.getAdapter() )) );
        item.setForumCategoryId( forumCategoryId );

        // hack - set moderator and last poster to admin userID
        item.setFUId( new Integer(auth_.getId().intValue()) );
        item.setFUId2( new Integer(auth_.getId().intValue()) );

        InsertWmForumConcreteItem.process( forumActionBean.getAdapter(), item );

        log.debug("out addNewForumConcrete()");
    }

    private void addNewCategory(ModuleUser auth_, ForumActionBean forumActionBean, Long forumId) throws SQLException, PersistenceException {
        log.debug("in addNewCategory()");

        WmForumCategoryItemType item = new WmForumCategoryItemType();
        item.setForumCategoryName( forumActionBean.getRequest().getString( "forum-category-name" ));
        item.setForumId( new Integer(forumId.intValue()) );
        item.setForumCategoryId( new Integer(CommonDAO.getForumCategoryID( forumActionBean.getAdapter() )) );
        item.setIsUseLocale( Boolean.FALSE );
        InsertWmForumCategoryItem.process( forumActionBean.getAdapter(), item );

        log.debug("out addNewCategory()");
    }
}
