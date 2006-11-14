/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.webmill.admin.bean.*;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 17:26:38
 */
@SuppressWarnings({"UnusedAssignment"})
public class WebmillAdminDaoImpl implements WebmillAdminDao {
    private final static Logger log = Logger.getLogger(WebmillAdminDaoImpl.class);

    public List<CompanyBean> getCompanyList() {
        List<CompanyBean> list = new ArrayList<CompanyBean>();
        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select ID_FIRM, full_name, short_name, " +
                    "	    address, chief, buh, url, " +
                    "	    short_info, is_work, is_search, is_deleted " +
                    "from 	WM_LIST_COMPANY " +
                    "where  is_deleted=0";

            ps = db.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(loadCompanyFromResultSet(rs));
            }
            return list;
        }
        catch (Exception e) {
            if (db != null && db.testExceptionTableNotFound(e)) {
                return list;
            }
            String es = "Error load company list";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public CompanyBean getCompany(Long companyId) {
        if (companyId == null) {
            return null;
        }

        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select ID_FIRM, full_name, short_name, " +
                    "	    address, chief, buh, url,  " +
                    "	    short_info, is_work, is_search, is_deleted " +
                    "from 	WM_LIST_COMPANY " +
                    "where  is_deleted=0 and ID_FIRM=? ";

            ps = db.prepareStatement(sql);
            RsetTools.setLong(ps, 1, companyId);
            rs = ps.executeQuery();

            CompanyBean company = null;
            if (rs.next()) {
                company = loadCompanyFromResultSet(rs);
            }
            return company;
        }
        catch (Exception e) {
            String es = "Error load company for id: " + companyId;
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public Long processAddCompany(CompanyBean companyBean) {

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_COMPANY");
            seq.setTableName("WM_LIST_COMPANY");
            seq.setColumnName("ID_FIRM");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);


            ps = dbDyn.prepareStatement("insert into WM_LIST_COMPANY (" +
                "	ID_FIRM, " +
                "	full_name, " +
                "	short_name, " +
                "	address, " +
                "	chief, " +
                "	buh, " +
                "	url, " +
                "	short_info, " +
                "   is_deleted" +
                ")values " +

                (dbDyn.getIsNeedUpdateBracket() ? "(" : "") +

                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "   0 " +
                (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));

            int num = 1;
            RsetTools.setLong(ps, num++, sequenceValue);
            ps.setString(num++, companyBean.getName());
            ps.setString(num++, companyBean.getShortName());
            ps.setString(num++, companyBean.getAddress());
            ps.setString(num++, companyBean.getCeo());
            ps.setString(num++, companyBean.getCfo());
            ps.setString(num++, companyBean.getWebsite());
            ps.setString(num++, companyBean.getInfo());

            int i1 = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of inserted records - " + i1);

            dbDyn.commit();
            return sequenceValue;
        }
        catch (Exception e) {
            try {
                if (dbDyn != null)
                    dbDyn.rollback();
            }
            catch (Exception e001) {
                //catch rollback error
            }
            String es = "Error add new company";
            log.error(es, e);
            throw new IllegalStateException(es, e);

        }
        finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

    public void processSaveCompany(CompanyBean company) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void processDeleteCompany(CompanyBean company) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //// Portal user section

    public List<UserBean> getUserList() {

        List<UserBean> list = new ArrayList<UserBean>();
        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select a.ID_USER,a.ID_FIRM,a.FIRST_NAME,a.MIDDLE_NAME,a.LAST_NAME," +
                    "       a.DATE_START_WORK,a.DATE_FIRE,a.ADDRESS,a.TELEPHONE,a.EMAIL,a.IS_DELETED " +
                    "from   WM_LIST_USER a " +
                    "where  a.IS_DELETED=0";

            ps = db.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                UserBean beanPortal = loadPortalUserFromResultSet(rs);
                final CompanyBean company = getCompany(beanPortal.getCompanyId());
                if (company != null)
                    beanPortal.setCompanyName(company.getName());
                else
                    beanPortal.setCompanyName("Warning. Company not found");

                list.add(beanPortal);
            }
            return list;
        }
        catch (Exception e) {
            if (db != null && db.testExceptionTableNotFound(e)) {
                return list;
            }
            String es = "Error load list of portal users";
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public UserBean getUser(Long portalUserId) {
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
                    "       a.DATE_START_WORK,a.DATE_FIRE,a.ADDRESS,a.TELEPHONE,a.EMAIL, a.IS_DELETED " +
                    "from   WM_LIST_USER a " +
                    "where  a.ID_USER=? and a.IS_DELETED=0 ";

            ps = db.prepareStatement(sql);
            ps.setLong(1, portalUserId);
            rs = ps.executeQuery();

            UserBean beanPortal = null;
            if (rs.next()) {
                beanPortal = loadPortalUserFromResultSet(rs);
                CompanyBean company = getCompany(beanPortal.getCompanyId());
                if (company != null)
                    beanPortal.setCompanyName(company.getName());
                else
                    beanPortal.setCompanyName("Warning. Company not found");
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

    public Long addUser(UserBean portalUserBean) {
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            Long userId = addUser(dbDyn, portalUserBean);
            dbDyn.commit();
            return userId;
        }
        catch (Exception e) {
            try {
                if (dbDyn != null)
                    dbDyn.rollback();
            }
            catch (Exception e001) {
                // catch rollback exception
            }
            String es = "Error add new portal user";
            log.error(es, e);
            throw new IllegalStateException(es, e);

        }
        finally {
            DatabaseManager.close(dbDyn);
            dbDyn = null;
        }
    }

    public Long addUser(DatabaseAdapter dbDyn, UserBean portalUserBean) {
        log.debug("Start addUserInfo");

        if (portalUserBean == null) {
            throw new IllegalStateException("portalUserBean is null");
        }

        PreparedStatement ps = null;
        try {
            Long sequenceValue;
            if (portalUserBean.getUserId() == null) {
                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName("seq_WM_LIST_USER");
                seq.setTableName("WM_LIST_USER");
                seq.setColumnName("ID_USER");
                sequenceValue = dbDyn.getSequenceNextValue(seq);
                portalUserBean.setUserId(sequenceValue);
            }
            else {
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

            ps.executeUpdate();

            addAuthInfo(dbDyn, portalUserBean);
            return sequenceValue;
        }
        catch (Exception e) {
            String es = "Error add new portal user";
            log.error(es, e);
            throw new IllegalStateException(es, e);

        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public Long addAuthInfo(DatabaseAdapter db, UserBean userBean) {
        Long companyId = userBean.getCompanyId();
        Long holdingId = null;
        PreparedStatement ps = null;
        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_AUTH_USER");
            seq.setTableName("WM_AUTH_USER");
            seq.setColumnName("ID_AUTH_USER");
            Long id = db.getSequenceNextValue(seq);

            ps = db.prepareStatement(
                "insert into WM_AUTH_USER " +
                    "( ID_AUTH_USER, ID_FIRM, ID_HOLDING, " +
                    "  ID_USER, USER_LOGIN, USER_PASSWORD, " +
                    "IS_USE_CURRENT_FIRM, IS_HOLDING, IS_ROOT " +
                    ") values (" +
                    "?, " + // PK
                    "?, " + // b1.companyId, " +
                    "?, " + // b3.id_road, "+
                    "?, ?, ?, ?, " +
                    "?, 1 " +
                    ")");

            if (log.isDebugEnabled()) {
                log.debug("companyId " + companyId);
                log.debug("holdingId " + holdingId);
            }

            RsetTools.setLong(ps, 1, id);
            if (companyId != null)
                RsetTools.setLong(ps, 2, companyId);
            else
                ps.setNull(2, Types.INTEGER);

            if (holdingId != null)
                RsetTools.setLong(ps, 3, holdingId);
            else
                ps.setNull(3, Types.INTEGER);


            RsetTools.setLong(ps, 4, userBean.getUserId());
            ps.setString(5, userBean.getLogin());
            ps.setString(6, userBean.getPassword1());

            ps.setInt(7, 1); // isCompany
            ps.setInt(8, 0); // isHolding
            int i1 = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of inserted records - " + i1);

            return id;
        }
        catch (Throwable e) {
            final String es = "Error add user auth";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void updateUser(UserBean portalUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteUser(UserBean portalUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //// Site section

    public List<SiteBean> getSites() {
        List<SiteBean> list = new ArrayList<SiteBean>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            String sql =
                "select * from WM_PORTAL_LIST_SITE";

            ps = adapter.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                SiteBean siteBean = loadSiteFromResultSet(rs);
                list.add(siteBean);
            }
        }
        catch (Exception e) {
            if (adapter != null && adapter.testExceptionTableNotFound(e)) {
                return list;
            }
            String es = "Error get list of sites";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
        return list;
    }

    public SiteBean getSite(Long siteId) {
        String sql_ = "select * from WM_PORTAL_LIST_SITE where ID_SITE=?";
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(sql_);
            ps.setLong(1, siteId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return loadSiteFromResultSet(rs);
            }
            return null;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public List<VirtualHostBean> getVirtualHosts(Long siteId) {
        String sql_ =
            "select * " +
                "from  WM_PORTAL_VIRTUAL_HOST " +
                "where ID_SITE=? " +
                "order by ID_SITE_VIRTUAL_HOST ASC";

        if (siteId == null) {
            throw new IllegalStateException("getVirtualHost(), siteId is null");
        }

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            List<VirtualHostBean> virtualHosts = new ArrayList<VirtualHostBean>();

            ps = adapter.prepareStatement(sql_);
            ps.setLong(1, siteId);

            rs = ps.executeQuery();
            while (rs.next()) {
                VirtualHostBean bean = new VirtualHostBean();
                long tempLong;
                String tempString;
                tempLong = rs.getLong( "ID_SITE_VIRTUAL_HOST");
                bean.setId( tempLong );
                tempLong = rs.getLong( "ID_SITE");
                bean.setSiteId( tempLong );
                tempString = rs.getString( "NAME_VIRTUAL_HOST" );
                bean.setHost( tempString );

                virtualHosts.add(bean);
            }
            return virtualHosts;
        }
        catch (Exception e) {
            String es = "Error get list of virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public void updateSiteWithVirtualHost(SiteBean site, List<String> virtualHosts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteSite(Long siteId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteWithVirtualHost(SiteBean site, List<String> virtualHosts) {

        if (log.isDebugEnabled()) {
            log.debug("site: " + site);
            if (site != null) {
                log.debug("    language: " + site.getDefLanguage());
                log.debug("    country: " + site.getDefCountry());
                log.debug("    variant: " + site.getDefVariant());
                log.debug("    companyId: " + site.getCompanyId());
            }
        }

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_LIST_SITE");
            seq.setTableName("WM_PORTAL_LIST_SITE");
            seq.setColumnName("ID_SITE");
            Long siteId = dbDyn.getSequenceNextValue(seq);

            ps = dbDyn.prepareStatement("insert into WM_PORTAL_LIST_SITE (" +
                "ID_SITE, ID_FIRM, DEF_LANGUAGE, DEF_COUNTRY, DEF_VARIANT, " +
                "NAME_SITE, ADMIN_EMAIL, IS_CSS_DYNAMIC, CSS_FILE, " +
                "IS_REGISTER_ALLOWED, PROPERTIES " +
                ")values " +
                (dbDyn.getIsNeedUpdateBracket() ? "(" : "") +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	? " +
                (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));

            int num = 1;
            RsetTools.setLong(ps, num++, siteId);
            RsetTools.setLong(ps, num++, site.getCompanyId());
            ps.setString(num++, site.getDefLanguage());
            ps.setString(num++, site.getDefCountry());
            ps.setString(num++, site.getDefVariant());
            ps.setString(num++, site.getSiteName());
            ps.setString(num++, site.getAdminEmail());
            ps.setInt(num++, site.getCssDynamic() ? 1 : 0);
            ps.setString(num++, site.getCssFile());
            ps.setInt(num++, site.getRegisterAllowed() ? 1 : 0);
            ps.setString(num++, site.getProperties());

            int i1 = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of inserted records - " + i1);

            if (virtualHosts != null) {
                for (String s : virtualHosts) {
                    VirtualHostBean host = new VirtualHostBean(null, siteId, s);
                    createVirtualHost(dbDyn, host);
                }
            }

            dbDyn.commit();
            return siteId;
        }
        catch (Exception e) {
            try {
                if (dbDyn != null)
                    dbDyn.rollback();
            }
            catch (Exception e001) {
                //catch rollback error
            }
            String es = "Error add new site";
            log.error(es, e);
            throw new IllegalStateException(es, e);

        }
        finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

    public List<VirtualHostBean> getVirtualHostsFullList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSite(SiteBean siteBean) {
        return createSiteWithVirtualHost(siteBean, null);
    }

    public Long createXslt(XsltBean xslt) {
        String sql_ =
            "insert into WM_PORTAL_XSLT" +
                "(ID_SITE_XSLT, IS_CURRENT, TEXT_COMMENT, ID_SITE_SUPPORT_LANGUAGE)" +
                "values" +
                "( ?,  ?,  ?,  ?)";

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_XSLT");
            seq.setTableName("WM_PORTAL_XSLT");
            seq.setColumnName("ID_SITE_XSLT");
            Long id = adapter.getSequenceNextValue(seq);

            DatabaseManager.runSQL(
                adapter,
                "update WM_PORTAL_XSLT set IS_CURRENT=0 where ID_SITE_SUPPORT_LANGUAGE=?",
                new Object[]{xslt.getSiteLanguageId()},
                new int[]{Types.NUMERIC}
            );

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id);
            ps.setInt(2, xslt.isCurrent() ? 1 : 0);
            ps.setString(3, xslt.getName());
            ps.setLong(4, xslt.getSiteLanguageId());

            int countInsertRecord = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug("Count of inserted records - " + countInsertRecord);
            }

            /**
             * @param idRec - value of PK in main table
             * @param pkName - name PK in main table
             * @param pkType - type of PK in main table
             * @param nameTargetTable  - name of slave table
             * @param namePkTargetTable - name of PK in slave table
             * @param nameTargetField - name of filed with BigText data in slave table
             * @param insertString - insert string
             * @param isDelete - delete data from slave table before insert true/false
             */
            DatabaseManager.insertBigText(
                adapter,
                id,
                "ID_SITE_XSLT",
                PrimaryKeyTypeTypeType.NUMBER,
                "WM_PORTAL_XSLT_DATA",
                "ID_SITE_XSLT_DATA",
                "XSLT",
                xslt.getXsltData(),
                false
            );

            adapter.commit();
            return id;
        }
        catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            log.error("Item getIdSiteXslt(), value - " + xslt.getId());
            log.error("Item getIsCurrent(), value - " + xslt.isCurrent());
            log.error("Item getTextComment(), value - " + xslt.getName());
            log.error("Item getXslt(), value - " + xslt.getXsltData());
            log.error("Item getIdSiteSupportLanguage(), value - " + xslt.getSiteLanguageId());
            log.error("SQL " + sql_);
            log.error("Exception insert data in db", e);
            String es = "Error create site language";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public SiteLanguageBean getSiteLanguage(Long siteId, String locale) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteLanguage(SiteLanguageBean siteLanguageBean) {
        String sql_ =
            "insert into WM_PORTAL_SITE_LANGUAGE" +
                "(ID_SITE_SUPPORT_LANGUAGE, ID_SITE, CUSTOM_LANGUAGE, NAME_CUSTOM_LANGUAGE)" +
                "values" +
                "( ?,  ?,  ?,  ?)";

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_SITE_LANGUAGE");
            seq.setTableName("WM_PORTAL_SITE_LANGUAGE");
            seq.setColumnName("ID_SITE_SUPPORT_LANGUAGE");
            Long id = adapter.getSequenceNextValue(seq);

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id);
            ps.setLong(2, siteLanguageBean.getSiteId());
            ps.setString(3, siteLanguageBean.getLocale());
            ps.setString(4, siteLanguageBean.getNameCustomLanguage());

            int countInsertRecord = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of inserted records - " + countInsertRecord);

            adapter.commit();
            return id;
        }
        catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error create site language";
            log.error("Item getIdSiteSupportLanguage(), value - " + siteLanguageBean.getSiteLanguageId());
            log.error("Item getIdSite(), value - " + siteLanguageBean.getSiteId());
            log.error("Item getCustomLanguage(), value - " + siteLanguageBean.getLocale());
            log.error("Item getNameCustomLanguage(), value - " + siteLanguageBean.getNameCustomLanguage());
            log.error("SQL " + sql_);
            log.error("Exception insert data in db", e);
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public TemplateBean getTemplate(String templateName, Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_TEMPLATE where NAME_SITE_TEMPLATE=? and ID_SITE_SUPPORT_LANGUAGE=?"
            );
            ps.setString(1, templateName);
            ps.setLong(2, siteLanguageId);

            rs = ps.executeQuery();

            TemplateBean bean = null;
            if (rs.next()) {
                bean = new TemplateBean();

                fillTemplateBean(rs, bean);
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getPortletName()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public Long createTemplate(TemplateBean template) {
        String sql_ =
            "insert into WM_PORTAL_TEMPLATE" +
                "(ID_SITE_TEMPLATE, NAME_SITE_TEMPLATE, TEMPLATE_DATA, ID_SITE_SUPPORT_LANGUAGE, " +
                "IS_DEFAULT_DYNAMIC)" +
                "values" +
                "( ?,  ?,  ?,  ?,  ?)";

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            clearDefaultDynamicFlag(template, adapter);

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_TEMPLATE");
            seq.setTableName("WM_PORTAL_TEMPLATE");
            seq.setColumnName("ID_SITE_TEMPLATE");
            Long id = adapter.getSequenceNextValue(seq);

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id);
            ps.setString(2, template.getTemplateName());
            ps.setString(3, template.getTemplateData());
            ps.setLong(4, template.getSiteLanguageId());
            ps.setInt(5, template.isDefaultDynamic() ? 1 : 0);

            int countInsertRecord = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug("Count of inserted records - " + countInsertRecord);
            }

            adapter.commit();
            return id;
        }
        catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            log.error("Item getIdSiteTemplate(), value - " + template.getTemplateId());
            log.error("Item getNameSiteTemplate(), value - " + template.getTemplateName());
            log.error("Item getTemplateData(), value - " + template.getTemplateData());
            log.error("Item getIdSiteSupportLanguage(), value - " + template.getSiteLanguageId());
            log.error("Item getIsDefaultDynamic(), value - " + template.isDefaultDynamic());
            log.error("SQL " + sql_);
            log.error("Exception insert data in db", e);
            String es = "Error create template";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, ps);
            adapter = null;
            ps = null;
        }
    }

    public Long createPortletName(PortletNameBean portletNameBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletId, Long templateId) {
        if (log.isDebugEnabled()) {
            log.debug("InternalDaoCatalogImpl.getCatalogItemId()");
            log.debug("     siteLanguageId: " + siteLanguageId);
            log.debug("     portletId: " + portletId);
            log.debug("     templateId: " + templateId);
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return DatabaseManager.getLongValue(adapter,
                "select a.ID_SITE_CTX_CATALOG " +
                    "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=? and a.ID_SITE_CTX_TYPE=? and a.ID_SITE_TEMPLATE=? ",
                new Object[]{siteLanguageId, portletId, templateId},
                new int[]{Types.DECIMAL, Types.DECIMAL, Types.DECIMAL}
            );
        }
        catch (Exception e) {
            String es = "Error get getCatalogItemId()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Long createCatalogItem(CatalogBean item) {
        String sql_ =
            "insert into WM_PORTAL_CATALOG" +
                "(ID_SITE_CTX_CATALOG, ID_TOP_CTX_CATALOG, ORDER_FIELD, ID_SITE_CTX_TYPE, " +
                "STORAGE, KEY_MESSAGE, ID_CONTEXT, IS_USE_PROPERTIES, ID_SITE_TEMPLATE, " +
                "ID_SITE_CTX_LANG_CATALOG, CTX_PAGE_URL, CTX_PAGE_TITLE, CTX_PAGE_AUTHOR, " +
                "CTX_PAGE_KEYWORD, URL_RESOURCE, METADATA, PORTLET_ROLE)" +
                "values" +
                "( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_CATALOG");
            seq.setTableName("WM_PORTAL_CATALOG");
            seq.setColumnName("ID_SITE_CTX_CATALOG");
            Long id = adapter.getSequenceNextValue(seq);

            ps = adapter.prepareStatement(sql_);

            if (item.getTopCatalogId() == null) {
                item.setTopCatalogId(0L);
            }

            ps.setLong(1, id);
            ps.setLong(2, item.getTopCatalogId());
            if (item.getOrderField() != null)
                ps.setInt(3, item.getOrderField());
            else
                ps.setNull(3, Types.INTEGER);

            ps.setLong(4, item.getPortletId());
            ps.setNull(5, Types.VARCHAR);

            ps.setString(6, item.getKeyMessage());
            if (item.getContextId() != null)
                ps.setLong(7, item.getContextId());
            else
                ps.setNull(7, Types.INTEGER);

            ps.setInt(8, 0);
            if (item.getTemplateId() != null)
                ps.setLong(9, item.getTemplateId());
            else
                ps.setNull(9, Types.INTEGER);

            ps.setLong(10, item.getCatalogLanguageId());
            if (item.getUrl() != null)
                ps.setString(11, item.getUrl());
            else
                ps.setNull(11, Types.VARCHAR);

            if (item.getTitle() != null)
                ps.setString(12, item.getTitle());
            else
                ps.setNull(12, Types.VARCHAR);

            if (item.getAuthor() != null)
                ps.setString(13, item.getAuthor());
            else
                ps.setNull(13, Types.VARCHAR);

            if (item.getKeyword() != null)
                ps.setString(14, item.getKeyword());
            else
                ps.setNull(14, Types.VARCHAR);

            ps.setNull(15, Types.VARCHAR);

            if (item.getMetadata() != null)
                ps.setString(16, item.getMetadata());
            else
                ps.setNull(16, Types.VARCHAR);

            if (item.getPortletRole() != null)
                ps.setString(17, item.getPortletRole());
            else
                ps.setNull(17, Types.VARCHAR);


            int countInsertRecord = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug("Count of inserted records - " + countInsertRecord);
            }

            adapter.commit();
            return id;
        }
        catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error create site language";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public Long createVirtualHost(VirtualHostBean virtualHost) {
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            Long hostId = createVirtualHost(dbDyn, virtualHost);
            dbDyn.commit();
            return hostId;
        }
        catch (Exception e) {
            try {
                if (dbDyn != null)
                    dbDyn.rollback();
            }
            catch (Exception e001) {
                // catch rollback exception
            }
            String es = "Error add new virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);

        }
        finally {
            DatabaseManager.close(dbDyn);
            dbDyn = null;
        }
    }

    public Long createVirtualHost(DatabaseAdapter adapter, VirtualHostBean virtualHost) {

        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_VIRTUAL_HOST");
            seq.setTableName("WM_PORTAL_VIRTUAL_HOST");
            seq.setColumnName("ID_SITE_VIRTUAL_HOST");
            Long hostId = adapter.getSequenceNextValue(seq);

            String sql_ =
                "insert into WM_PORTAL_VIRTUAL_HOST" +
                    "(ID_SITE_VIRTUAL_HOST, ID_SITE, NAME_VIRTUAL_HOST)" +
                    "values" +
                    "( ?,  ?,  ?)";

            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = adapter.prepareStatement(sql_);

                ps.setLong(1, hostId);
                ps.setLong(2, virtualHost.getSiteId());
                ps.setString(3, virtualHost.getHost());

                int countInsertRecord = ps.executeUpdate();

                if (log.isDebugEnabled()) {
                    log.debug("Count of inserted records - " + countInsertRecord);
                }
            }
            finally {
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }

            return hostId;
        }
        catch (Throwable e) {
            String es = "Error create virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    public XsltBean getCurrentXslt(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogLanguageItem(CatalogLanguageBean catalogLanguageItem) {
        if (log.isDebugEnabled()) {
        }
        String sql_ =
            "insert into WM_PORTAL_CATALOG_LANGUAGE" +
                "(ID_SITE_CTX_LANG_CATALOG, CATALOG_CODE, IS_DEFAULT, ID_SITE_SUPPORT_LANGUAGE)" +
                "values" +
                "( ?,  ?,  ?,  ?)";

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_CATALOG_LANGUAGE");
            seq.setTableName("WM_PORTAL_CATALOG_LANGUAGE");
            seq.setColumnName("ID_SITE_CTX_LANG_CATALOG");
            Long id = adapter.getSequenceNextValue(seq);

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id);
            if (catalogLanguageItem.getCatalogCode() != null)
                ps.setString(2, catalogLanguageItem.getCatalogCode());
            else
                ps.setNull(2, Types.VARCHAR);

            ps.setInt(3, catalogLanguageItem.getDefault() ? 1 : 0);
            ps.setLong(4, catalogLanguageItem.getSiteLanguageId());

            int countInsertRecord = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug("Count of inserted records - " + countInsertRecord);
            }

            adapter.commit();
            return id;
        }
        catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            log.error("Item getIdSiteCtxLangCatalog(), value - " + catalogLanguageItem.getCatalogLanguageId());
            log.error("Item getCatalogCode(), value - " + catalogLanguageItem.getCatalogCode());
            log.error("Item getIsDefault(), value - " + catalogLanguageItem.getDefault());
            log.error("Item getIdSiteSupportLanguage(), value - " + catalogLanguageItem.getSiteLanguageId());
            String es = "Error create site language";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public SiteBean getSite(String siteName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public XsltBean getXslt(String name, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CatalogLanguageBean getCatalogLanguageItem(String catalogCode, Long siteLanguageId) {
        String sql_ =
            "select * from WM_PORTAL_CATALOG_LANGUAGE where CATALOG_CODE=? and id_site_support_language=?";

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement(sql_);
            RsetTools.setString(ps, 1, catalogCode);
            RsetTools.setLong(ps, 2, siteLanguageId);
            rs = ps.executeQuery();
            if (rs.next()) {
                CatalogLanguageBean bean = new CatalogLanguageBean();

                Long tempLong = rs.getLong("ID_SITE_CTX_LANG_CATALOG");
                if (!rs.wasNull())
                    bean.setCatalogLanguageId(tempLong);

                String tempString = rs.getString("CATALOG_CODE");
                if (!rs.wasNull())
                    bean.setCatalogCode(tempString);

                int tempBoolean = rs.getInt("IS_DEFAULT");
                if (!rs.wasNull())
                    bean.setDefault(tempBoolean == 1);
                else
                    bean.setDefault(false);

                tempLong = rs.getLong("ID_SITE_SUPPORT_LANGUAGE");
                if (!rs.wasNull())
                    bean.setSiteLanguageId(tempLong);

                return bean;
            }
            return null;
        }
        catch (Exception e) {
            String es = "Error get getCatalogLanguageItem()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public PortletNameBean getPortletName(Long portletId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_PORTLET_NAME where ID_SITE_CTX_TYPE=?"
            );
            ps.setLong(1, portletId);

            rs = ps.executeQuery();

            PortletNameBean bean = null;
            if (rs.next()) {
                bean = new PortletNameBean();

                long tempLong = rs.getLong("ID_SITE_CTX_TYPE");
                if (!rs.wasNull())
                    bean.setPortletId(tempLong);

                String tempString = rs.getString("TYPE");
                if (!rs.wasNull())
                    bean.setPortletName(tempString);

                bean.setActive(false);
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getPortletName()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public TemplateBean getTemplate(Long templateId, Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_TEMPLATE where ID_SITE_TEMPLATE=? and ID_SITE_SUPPORT_LANGUAGE=?"
            );
            ps.setLong(1, templateId);
            ps.setLong(2, siteLanguageId);

            rs = ps.executeQuery();

            TemplateBean bean = null;
            if (rs.next()) {
                bean = new TemplateBean();
                fillTemplateBean(rs, bean);
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getPortletName()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public PortletNameBean getPortletName(String portletName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_PORTLET_NAME where TYPE=?"
            );
            ps.setString(1, portletName);

            rs = ps.executeQuery();

            PortletNameBean bean = null;
            if (rs.next()) {
                bean = new PortletNameBean();

                long tempLong = rs.getLong("ID_SITE_CTX_TYPE");
                if (!rs.wasNull())
                    bean.setPortletId(tempLong);

                String tempString = rs.getString("TYPE");
                if (!rs.wasNull())
                    bean.setPortletName(tempString);

                bean.setActive(false);
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getPortletName()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public PortletNameBean createPortletName(String portletName) {
        String sql_ =
            "insert into WM_PORTAL_PORTLET_NAME" +
                "(ID_SITE_CTX_TYPE, TYPE)" +
                "values" +
                "( ?,  ?)";

        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_PORTLET_NAME");
            seq.setTableName("WM_PORTAL_PORTLET_NAME");
            seq.setColumnName("ID_SITE_CTX_TYPE");
            Long id = adapter.getSequenceNextValue(seq);

            PortletNameBean portletNameBean = new PortletNameBean();
            portletNameBean.setActive(true);
            portletNameBean.setPortletId(id);
            portletNameBean.setPortletName(portletName);

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, portletNameBean.getPortletId());
            ps.setString(2, portletNameBean.getPortletName());

            int countInsertRecord = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug("Count of inserted records - " + countInsertRecord);
            }

            adapter.commit();
            return portletNameBean;
        }
        catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error create portlet name";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    private CompanyBean loadCompanyFromResultSet(ResultSet rs) throws Exception {

        CompanyBean company = new CompanyBean();
        company.setId(RsetTools.getLong(rs, "ID_FIRM"));
        company.setName(RsetTools.getString(rs, "full_name"));
        company.setShortName(RsetTools.getString(rs, "short_name"));
        company.setAddress(RsetTools.getString(rs, "address"));
        company.setCeo(RsetTools.getString(rs, "chief"));
        company.setCfo(RsetTools.getString(rs, "buh"));
        company.setWebsite(RsetTools.getString(rs, "url"));
        company.setInfo(RsetTools.getString(rs, "short_info"));
        company.setDeleted(RsetTools.getInt(rs, "is_deleted", 0) == 1);

        return company;
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
        bean.setDeleted(RsetTools.getInt(rs, "IS_DELETED", 0) == 1);

        return bean;
    }

    public static SiteBean loadSiteFromResultSet(ResultSet rs)
        throws java.sql.SQLException {
        SiteBean item = new SiteBean();

        long tempLong;
        String tempString = null;
        int tempBoolean;

        tempLong = rs.getLong("ID_SITE");
        if (!rs.wasNull())
            item.setSiteId(tempLong);

        tempLong = rs.getLong("ID_FIRM");
        if (!rs.wasNull())
            item.setCompanyId(tempLong);

        tempString = rs.getString("DEF_LANGUAGE");
        if (!rs.wasNull())
            item.setDefLanguage(tempString);

        tempString = rs.getString("DEF_COUNTRY");
        if (!rs.wasNull())
            item.setDefCountry(tempString);

        tempString = rs.getString("DEF_VARIANT");
        if (!rs.wasNull())
            item.setDefVariant(tempString);

        tempString = rs.getString("NAME_SITE");
        if (!rs.wasNull())
            item.setSiteName(tempString);

        tempString = rs.getString("ADMIN_EMAIL");
        if (!rs.wasNull())
            item.setAdminEmail(tempString);

        tempBoolean = rs.getInt("IS_CSS_DYNAMIC");
        if (!rs.wasNull())
            item.setCssDynamic(tempBoolean == 1);
        else
            item.setCssDynamic(false);

        tempString = rs.getString("CSS_FILE");
        if (!rs.wasNull())
            item.setCssFile(tempString);

        tempBoolean = rs.getInt("IS_REGISTER_ALLOWED");
        if (!rs.wasNull())
            item.setRegisterAllowed(tempBoolean == 1);
        else
            item.setRegisterAllowed(false);

        tempString = rs.getString("PROPERTIES");
        if (!rs.wasNull())
            item.setProperties(tempString);
        return item;
    }

    private void clearDefaultDynamicFlag(TemplateBean template, DatabaseAdapter adapter) throws SQLException {
        if (template.isDefaultDynamic()) {
            DatabaseManager.runSQL(
                adapter,
                "update WM_PORTAL_TEMPLATE set IS_DEFAULT_DYNAMIC=0 where ID_SITE_SUPPORT_LANGUAGE=? and IS_DEFAULT_DYNAMIC!=0",
                new Object[]{template.getSiteLanguageId()},
                new int[]{Types.NUMERIC}
            );
        }
    }

    private void fillTemplateBean(ResultSet rs, TemplateBean bean) throws SQLException {
        long tempLong;
        String tempString = null;
        int tempBoolean;
        tempLong = rs.getLong("ID_SITE_TEMPLATE");
        if (!rs.wasNull())
            bean.setTemplateId(tempLong);
        tempString = rs.getString("NAME_SITE_TEMPLATE");
        if (!rs.wasNull())
            bean.setTemplateName(tempString);
        tempString = rs.getString("TEMPLATE_DATA");
        if (!rs.wasNull())
            bean.setTemplateData(tempString);
        tempLong = rs.getLong("ID_SITE_SUPPORT_LANGUAGE");
        if (!rs.wasNull())
            bean.setSiteLanguageId(tempLong);
        tempBoolean = rs.getInt("IS_DEFAULT_DYNAMIC");
        if (!rs.wasNull())
            bean.setDefaultDynamic(tempBoolean == 1);
        else
            bean.setDefaultDynamic(false);
    }

}
