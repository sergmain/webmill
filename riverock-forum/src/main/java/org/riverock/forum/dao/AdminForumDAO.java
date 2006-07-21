/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
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
 *
 */
package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.core.GetWmForumCategoryItem;
import org.riverock.forum.core.GetWmForumConcreteItem;
import org.riverock.forum.core.InsertWmForumCategoryItem;
import org.riverock.forum.core.InsertWmForumConcreteItem;
import org.riverock.forum.core.UpdateWmForumCategoryItem;
import org.riverock.forum.core.UpdateWmForumConcreteItem;
import org.riverock.forum.exception.PersistenceException;
import org.riverock.forum.schema.core.WmForumCategoryItemType;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.user.ModuleUser;

/**
 * @author SMaslyukov
 *         Date: 21.04.2005
 *         Time: 11:25:39
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
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
                addNewCategory(forumActionBean, forumId);
            }
            else if (subAction.equals("add-new-forum")) {
                addNewForumConcrete(auth_, forumActionBean, forumId);
            }
            else if (subAction.equals("delete-forum")) {
                deleteForumConcrete(forumActionBean, forumId, false);
            }
            else if (subAction.equals("permanent-delete-forum")) {
                deleteForumConcrete(forumActionBean, forumId, true);
            }
            else if (subAction.equals("restore-forum")) {
                restoreForumConcrete(forumActionBean, forumId);
            }
            else if (subAction.equals("update-forum-category")) {
                updateForumCategory(forumActionBean, forumId );
            }
            else if (subAction.equals("delete-forum-category")) {
                deleteForumCategory(forumActionBean, false, forumId);
            }
            else if (subAction.equals("permanent-delete-forum-category")) {
                deleteForumCategory(forumActionBean, true, forumId);
            }
            else if (subAction.equals("restore-forum-category")) {
                restoreForumCategory(forumActionBean, forumId);
            }
            else if (subAction.equals("update-forum-concrete")) {
                updateForumConcrete(forumActionBean, forumId );
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
        log.debug("out execute()");
    }

    private void restoreForumConcrete(ForumActionBean forumActionBean, Long forumId) throws SQLException {
        log.debug("in restoreForumConcrete()");

        Long forumConcreteId = forumActionBean.getRequest().getLong( "forumConcreteId" );
        if (log.isDebugEnabled()) {
            log.debug("forumConcreteId: "+forumConcreteId);
        }

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

    private void deleteForumConcrete(ForumActionBean forumActionBean, Long forumId, boolean isPermanent) throws SQLException {
        log.debug("in deleteForumConcrete()");

        int checkBox = forumActionBean.getRequest().getInt("confirm-delete", 0);
        Long forumConcreteId = forumActionBean.getRequest().getLong( "forumConcreteId" );
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

        Long forumCategoryId = forumActionBean.getRequest().getLong( "forumCategoryId" );
        if (forumCategoryId==null) {
            return;
        }
        if (!CommonUtils.checkForumCategoryId(forumActionBean.getAdapter(), forumId, forumCategoryId)) {
            return;
        }

        WmForumConcreteItemType item = new WmForumConcreteItemType();
        item.setFName( forumActionBean.getRequest().getString( "forum-name" ) );
        item.setFInfo( forumActionBean.getRequest().getString( "forum-info" ) );
        item.setFId(CommonDAO.getForumConcreteID(forumActionBean.getAdapter()) );
        item.setForumCategoryId( forumCategoryId );

        // hack - set moderator and last poster to admin userID
        item.setFUId(auth_.getId() );
        item.setFUId2(auth_.getId() );

        InsertWmForumConcreteItem.process( forumActionBean.getAdapter(), item );

        log.debug("out addNewForumConcrete()");
    }

    private void addNewCategory(ForumActionBean forumActionBean, Long forumId) throws SQLException, PersistenceException {
        log.debug("in addNewCategory()");

        WmForumCategoryItemType item = new WmForumCategoryItemType();
        item.setForumCategoryName( forumActionBean.getRequest().getString( "forum-category-name" ));
        item.setForumId(forumId );
        item.setForumCategoryId(CommonDAO.getForumCategoryID(forumActionBean.getAdapter()) );
        item.setIsUseLocale( Boolean.FALSE );
        InsertWmForumCategoryItem.process( forumActionBean.getAdapter(), item );

        log.debug("out addNewCategory()");
    }

    ///////////

    private void restoreForumCategory(ForumActionBean forumActionBean, Long forumId) throws SQLException {
        log.debug("in restoreForumCategory()");

        Long forumCategoryId = forumActionBean.getRequest().getLong( "forumCategoryId" );
        if (!CommonUtils.checkForumCategoryId(forumActionBean.getAdapter(), forumId, forumCategoryId)){
            return;
        }

        DatabaseManager.runSQL(
            forumActionBean.getAdapter(),
            "update WM_FORUM_CATEGORY " +
            "set    IS_DELETED=0 " +
            "where  FORUM_CATEGORY_ID=? ",
            new Object[] { forumCategoryId },
            new int[] { Types.INTEGER }
        );
        log.debug("out restoreForumCategory()");

    }

    private void deleteForumCategory(ForumActionBean forumActionBean, boolean isPermanent, Long forumId) throws SQLException {
        log.debug("in deleteForumCategory()");

        Long forumCategoryId = forumActionBean.getRequest().getLong( "forumCategoryId" );
        if (!CommonUtils.checkForumCategoryId(forumActionBean.getAdapter(), forumId, forumCategoryId)){
            return;
        }

        int checkBox = forumActionBean.getRequest().getInt("confirm-delete", 0);
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
                ps = forumActionBean.getAdapter().prepareStatement(
                    "select a.F_ID " +
                    "from   WM_FORUM_CONCRETE a " +
                    "where  a.FORUM_CATEGORY_ID=?"
                );
                ps.setLong(1, forumCategoryId);
                rs = ps.executeQuery();

                while (rs.next()) {
                    CommonDAO.deleteForumConcrete(forumActionBean.getAdapter(), RsetTools.getLong(rs, "F_ID"));
                }
                DatabaseManager.runSQL(
                    forumActionBean.getAdapter(),
                    "delete from WM_FORUM_CATEGORY where FORUM_CATEGORY_ID=? ",
                    new Object[] { forumCategoryId },
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
                forumActionBean.getAdapter(),
                "update WM_FORUM_CATEGORY " +
                "set    IS_DELETED=1 " +
                "where  FORUM_CATEGORY_ID=? ",
                new Object[] { forumCategoryId },
                new int[] { Types.INTEGER }
            );
        }
        log.debug("out deleteForumCategory()");

    }

    private void updateForumCategory(ForumActionBean forumActionBean, Long forumId) throws PersistenceException {
        log.debug("in updateForumCategory()");

        Long forumCategoryId = forumActionBean.getRequest().getLong( "forumCategoryId" );
        if (!CommonUtils.checkForumCategoryId(forumActionBean.getAdapter(), forumId, forumCategoryId)){
            return;
        }

        WmForumCategoryItemType forumCategory = GetWmForumCategoryItem.getInstance(forumActionBean.getAdapter(), (long)forumCategoryId.intValue() ).item;
        forumCategory.setForumCategoryName( forumActionBean.getRequest().getString( "forum-category-name" ));
        UpdateWmForumCategoryItem.process( forumActionBean.getAdapter(), forumCategory );

        log.debug("out updateForumCategory()");
    }

    private void updateForumConcrete(ForumActionBean forumActionBean, Long forumId) throws PersistenceException {
        log.debug("in updateForumConcrete()");

        Long forumConcreteId = forumActionBean.getRequest().getLong( "forumConcreteId" );
        if (!CommonUtils.checkForumConcreteId(forumActionBean.getAdapter(), forumId, forumConcreteId)){
            return;
        }
        WmForumConcreteItemType forumConcrete = GetWmForumConcreteItem.getInstance( forumActionBean.getAdapter(), (long)forumConcreteId.intValue()).item;
        forumConcrete.setFName( forumActionBean.getRequest().getString( "forum-name" ));
        forumConcrete.setFInfo( forumActionBean.getRequest().getString( "forum-desc" ));
        UpdateWmForumConcreteItem.process( forumActionBean.getAdapter(), forumConcrete );

        log.debug("out updateForumConcrete()");
    }


}
