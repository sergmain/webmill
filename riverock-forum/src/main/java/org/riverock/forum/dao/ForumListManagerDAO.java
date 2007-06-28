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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.forum.bean.ForumIntegrityBean;
import org.riverock.forum.bean.PortalUserBean;
import org.riverock.forum.dao.core.InsertWmForumItem;
import org.riverock.forum.dao.core.UpdateWmForumItem;
import org.riverock.forum.exception.PersistenceException;
import org.riverock.forum.dao.bean.WmForumItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.exception.ModuleException;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.interfaces.ContainerConstants;

/**
 * @author SMaslyukov
 *         Date: 21.04.2005
 *         Time: 11:25:39
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class ForumListManagerDAO {
    private final static Logger log = Logger.getLogger(ForumListManagerDAO.class);

    public void execute(ModuleUser auth_, ModuleActionRequest moduleActionRequest, UrlProvider urlProvider) throws ActionException {

        log.debug("in execute()");

        String subAction = moduleActionRequest.getRequest().getString(Constants.NAME_SUB_ACTION);

        if (log.isDebugEnabled()) {
            log.debug("subAction: " + subAction);
        }

        if (subAction == null) {
            return;
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            WmForumItemType forum = getForum(adapter, moduleActionRequest);

            if (subAction.equals("update-forum")) {
                updateForum(auth_, moduleActionRequest, adapter, forum);
            } else if (subAction.equals("add-forum")) {
                addNewForum(auth_, moduleActionRequest, adapter);
            } else if (subAction.equals("delete-forum")) {
                deleteForum(auth_, moduleActionRequest, false, adapter, forum);
            } else if (subAction.equals("permanent-delete-forum")) {
                deleteForum(auth_, moduleActionRequest, true, adapter, forum);
            } else if (subAction.equals("restore-forum")) {
                restoreForum(auth_, moduleActionRequest, adapter, forum);
            } else if (subAction.equals("create-lost-user")) {
                createLostUser(moduleActionRequest);
            }
            adapter.commit();
        }
        catch (Exception e) {
            String es = "Error process admin action, sub action: " + subAction;
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
        if (forumId == null) {
            return null;
        }
        return CommonUtils.checkForumId(adapter, forumId, moduleActionRequest.getRequest().getServerName());
    }

    private void restoreForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter, WmForumItemType forum) throws SQLException {
        log.debug("in restoreForum()");

        if (forum == null) {
            return;
        }

        DatabaseManager.runSQL(
            adapter,
            "update WM_FORUM " +
                "set    IS_DELETED=0 " +
                "where  FORUM_ID=? ",
            new Object[]{forum.getForumId()},
            new int[]{Types.INTEGER}
        );
        log.debug("out restoreForum()");

    }

    private void createLostUser(ModuleActionRequest forumActionBean) {
        log.debug("in createLostUser()");

        try {
            Long siteId;
            siteId = forumActionBean.getRequest().getSiteId();
            log.debug("   siteId: "+siteId);

            PortalDaoProvider portalDaoProvider = (PortalDaoProvider) forumActionBean.getRequest().getAttribute(ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER);
            ForumIntegrityBean bean = DAOFactory.getDAOFactory().getForumIntegrityDao().getIntegrityStatus(siteId);
            log.debug("   bean: "+bean);
            if (bean!=null){
                log.debug("   bean.userId list: "+bean.getLostUserId());
                if (bean.getLostUserId()!=null) {
                    log.debug("   lost userId: " +StringTools.getIdByString( bean.getLostUserId(), "null" ));
                }
            }

            if (bean!=null) {
                PortalInfo p = (PortalInfo) forumActionBean.getRequest().getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
                for (Long userId : bean.getLostUserId()) {
                    log.debug("Add lost user, userId; " + userId);
                    PortalUserBean userBean = new PortalUserBean();
                    userBean.setFirstName("Lost");
                    userBean.setLastName("User");
                    userBean.setUserId(userId);
                    userBean.setCompanyId(p.getCompanyId());
                    userBean.setCreatedDate(new Date(System.currentTimeMillis()));
                    portalDaoProvider.getPortalUserDao().addUser(userBean);
                }
            }
        } catch (ModuleException e) {
            String es = "Error create lostUser";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        log.debug("out createLostUser()");
    }

    private void deleteForum(ModuleUser auth_, ModuleActionRequest moduleActionRequest, boolean isPermanent, DatabaseAdapter adapter, WmForumItemType forum) throws SQLException {
        log.debug("in deleteForum()");

        if (forum == null) {
            return;
        }

        int checkBox = moduleActionRequest.getRequest().getInt("confirm-delete", 0);
        if (log.isDebugEnabled()) {
            log.debug("checkBox: " + checkBox);
        }
        if (checkBox != 1) {
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
                ps.setLong(1, forum.getForumId());
                rs = ps.executeQuery();

                while (rs.next()) {
                    CommonDAO.deleteForumConcrete(adapter, RsetTools.getLong(rs, "F_ID"));
                }
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM_CATEGORY where FORUM_ID=? ",
                    new Object[]{forum.getForumId()},
                    new int[]{Types.INTEGER}
                );
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM where FORUM_ID=? ",
                    new Object[]{forum.getForumId()},
                    new int[]{Types.INTEGER}
                );
            }
            finally {
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }
        } else {
            DatabaseManager.runSQL(
                adapter,
                "update WM_FORUM " +
                    "set    IS_DELETED=1 " +
                    "where  FORUM_ID=? ",
                new Object[]{forum.getForumId()},
                new int[]{Types.INTEGER}
            );
        }
        log.debug("out deleteForum()");

    }

    private void addNewForum(ModuleUser auth_, ModuleActionRequest moduleActionRequest, DatabaseAdapter adapter) throws SQLException, PersistenceException, ModuleException {
        log.debug("in addNewForum()");

        WmForumItemType forum = new WmForumItemType();
        forum.setForumName(moduleActionRequest.getRequest().getString("forum-name"));
        forum.setForumId(CommonDAO.getForumID(adapter));
        forum.setSiteId(moduleActionRequest.getRequest().getSiteId());
        forum.setIsDeleted(false);
        forum.setIsUseLocale(false);

        InsertWmForumItem.process(adapter, forum);

        log.debug("out addNewForum()");
    }

    private void updateForum(ModuleUser auth_, ModuleActionRequest forumActionBean, DatabaseAdapter adapter, WmForumItemType forum) throws PersistenceException {
        log.debug("in updateForum()");

        if (forum == null) {
            return;
        }

        forum.setForumName(forumActionBean.getRequest().getString("forum-name"));
        forum.setIsUseLocale(Boolean.FALSE);
        UpdateWmForumItem.process(adapter, forum);

        log.debug("out updateForum()");
    }
}
