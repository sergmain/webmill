/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.portal.dao.PortalUserDao;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.UserBean;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:19:26
 */
@SuppressWarnings({"UnusedAssignment"})
public class PortalUserDaoImpl implements PortalUserDao {
    private final static Logger log = Logger.getLogger(PortalUserDaoImpl.class);

    private AuthSession authSession = null;

    PortalUserDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public User getUser(Long portalUserId) {
        if (portalUserId == null) {
            return null;
        }

        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select a.ID_USER,a.ID_FIRM,a.FIRST_NAME,a.MIDDLE_NAME,a.LAST_NAME, " +
                    "       a.DATE_START_WORK,a.DATE_FIRE,a.ADDRESS,a.TELEPHONE,a.EMAIL " +
                    "from   WM_LIST_USER a " +
                    "where  a.ID_USER=? and a.IS_DELETED=0 and a.ID_FIRM in ";

            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = db.prepareStatement(sql);
            int num = 1;
            ps.setLong(num++, portalUserId);
            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }

            rs = ps.executeQuery();

            UserBean beanPortal = null;
            if (rs.next()) {
                beanPortal = loadPortalUserFromResultSet(rs);
                final Company company = InternalDaoFactory.getInternalCompanyDao().getCompany(beanPortal.getCompanyId(), authSession);
                if (company != null)
                    beanPortal.setCompanyName(company.getName());
                else
                    beanPortal.setCompanyName("Warning. Ccompany not found");
            }
            return beanPortal;
        }
        catch (Exception e) {
            String es = "Error load portal user for id: " + portalUserId;
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<User> getUserList() {

        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select a.ID_USER,a.ID_FIRM,a.FIRST_NAME,a.MIDDLE_NAME,a.LAST_NAME,a.DATE_START_WORK,a.DATE_FIRE,a.ADDRESS,a.TELEPHONE,a.EMAIL " +
                    "from   WM_LIST_USER a " +
                    "where  a.IS_DELETED=0  and a.ID_FIRM in ";

            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = db.prepareStatement(sql);

            int num = 1;
            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }
            rs = ps.executeQuery();

            List<User> list = new ArrayList<User>();
            while (rs.next()) {
                UserBean beanPortal = loadPortalUserFromResultSet(rs);
                final Company company = InternalDaoFactory.getInternalCompanyDao().getCompany(beanPortal.getCompanyId(), authSession);
                if (company != null)
                    beanPortal.setCompanyName(company.getName());
                else
                    beanPortal.setCompanyName("Warning. Ccompany not found");

                list.add(beanPortal);
            }
            return list;
        }
        catch (Exception e) {
            String es = "Error load list of portlet names";
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public Long addUser(User portalUserBean) {
        log.debug("Start addUser");
        
        if (portalUserBean == null) {
            throw new IllegalStateException("portalUserBean is null");
        }

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            Long sequenceValue;
            if (portalUserBean.getUserId() == null) {
                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName("seq_WM_PORTAL_PORTLET_NAME");
                seq.setTableName("WM_PORTAL_PORTLET_NAME");
                seq.setColumnName("ID_SITE_CTX_TYPE");
                sequenceValue = dbDyn.getSequenceNextValue(seq);
            } else {
                sequenceValue = portalUserBean.getUserId();
            }

            String sql =
                "insert into WM_LIST_USER " +
                    "(ID_USER,ID_FIRM,FIRST_NAME,MIDDLE_NAME,LAST_NAME,DATE_START_WORK, " +
                    "ADDRESS,TELEPHONE,EMAIL) " +
                    "values " +
                    (dbDyn.getIsNeedUpdateBracket() ? "(" : "") +
                    "?,?,?,?,?,?,?,?,? " +
                    (dbDyn.getIsNeedUpdateBracket() ? ")" : "");

            ps = dbDyn.prepareStatement(sql);
            int num = 1;
            ps.setLong(num++, sequenceValue);
            ps.setLong(num++, portalUserBean.getCompanyId());
            ps.setString(num++, portalUserBean.getFirstName());
            ps.setString(num++, portalUserBean.getMiddleName());
            ps.setString(num++, portalUserBean.getLastName());
            RsetTools.setTimestamp(ps, num++, new Timestamp(System.currentTimeMillis()));
            ps.setString(num++, portalUserBean.getAddress());
            ps.setString(num++, portalUserBean.getPhone());
            ps.setString(num++, portalUserBean.getEmail());
            switch (dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
//                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }

            ps.executeUpdate();

            dbDyn.commit();
            return sequenceValue;
        }
        catch (Exception e) {
            try {
                if (dbDyn != null)
                    dbDyn.rollback();
            }
            catch (Exception e001) {
                // catch rollback exception
            }
            String es = "Error add new portlet name ";
            log.error(es, e);
            throw new IllegalStateException(es, e);

        }
        finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

    public void updateUser(User portalUserBean) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            String sql =
                "update WM_LIST_USER " +
                    "set    FIRST_NAME=?,MIDDLE_NAME=?,LAST_NAME=?, " +
                    "       ADDRESS=?,TELEPHONE=?,EMAIL=? " +
                    "where  ID_USER=? and is_deleted=0 and  ID_FIRM in ";

            switch (dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement(sql);
            int num = 1;
            ps.setString(num++, portalUserBean.getFirstName());
            ps.setString(num++, portalUserBean.getMiddleName());
            ps.setString(num++, portalUserBean.getLastName());
            ps.setString(num++, portalUserBean.getAddress());
            ps.setString(num++, portalUserBean.getPhone());
            ps.setString(num++, portalUserBean.getEmail());
            ps.setLong(num++, portalUserBean.getUserId());
            switch (dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }

            int i1 = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of updated record - " + i1);

            dbDyn.commit();
        }
        catch (Exception e) {
            try {
                if (dbDyn != null) {
                    dbDyn.rollback();
                }
            }
            catch (Exception e001) {
                // catch rollback exception
            }

            String es = "Error save portlet name";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

    public void deleteUser(User portalUserBean) {

        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            if (portalUserBean.getUserId() == null)
                throw new IllegalArgumentException("id of portal user is null");

            String sql =
                "update WM_LIST_USER " +
                    "set    is_deleted=1 " +
                    "where  ID_USER=? and is_deleted = 0 and ID_FIRM in ";

            switch (dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement(sql);
            int num = 1;
            ps.setLong(num++, portalUserBean.getUserId());
            switch (dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }

            int i1 = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of deleted records - " + i1);

            dbDyn.commit();
        }
        catch (Exception e) {
            try {
                if (dbDyn != null) {
                    dbDyn.rollback();
                }
            }
            catch (Exception e001) {
                //catch rollback exception
            }

            String es = "Error delete portlet name";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

    private UserBean loadPortalUserFromResultSet(ResultSet rs) throws Exception {

        UserBean bean = new UserBean();
        bean.setUserId(RsetTools.getLong(rs, "ID_USER"));
        bean.setCompanyId(RsetTools.getLong(rs, "ID_FIRM"));
        bean.setFirstName(RsetTools.getString(rs, "FIRST_NAME"));
        bean.setMiddleName(RsetTools.getString(rs, "MIDDLE_NAME"));
        bean.setLastName(RsetTools.getString(rs, "LAST_NAME"));
        bean.setCreatedDate(RsetTools.getTimestamp(rs, "DATE_START_WORK"));
        bean.setDeletedDate(RsetTools.getTimestamp(rs, "DATE_FIRE"));
        bean.setAddress(RsetTools.getString(rs, "ADDRESS"));
        bean.setPhone(RsetTools.getString(rs, "TELEPHONE"));
        bean.setEmail(RsetTools.getString(rs, "EMAIL"));

        return bean;
    }
}
