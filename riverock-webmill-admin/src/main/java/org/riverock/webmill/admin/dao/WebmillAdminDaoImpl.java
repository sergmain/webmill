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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
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

            List<CompanyBean> list = new ArrayList<CompanyBean>();
            while (rs.next()) {
                list.add(loadCompanyFromResultSet(rs));
            }
            return list;
        }
        catch (Exception e) {
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

            List<UserBean> list = new ArrayList<UserBean>();
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<VirtualHostBean> getVirtualHosts(Long siteId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
            if (site!=null) {
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
            seq.setSequenceName( "seq_WM_PORTAL_LIST_SITE" );
            seq.setTableName( "WM_PORTAL_LIST_SITE" );
            seq.setColumnName( "ID_SITE" );
            Long siteId = dbDyn.getSequenceNextValue( seq );

            ps = dbDyn.prepareStatement( "insert into WM_PORTAL_LIST_SITE (" +
                "ID_SITE, ID_FIRM, DEF_LANGUAGE, DEF_COUNTRY, DEF_VARIANT, " +
                "NAME_SITE, ADMIN_EMAIL, IS_CSS_DYNAMIC, CSS_FILE, " +
                "IS_REGISTER_ALLOWED, PROPERTIES " +
                ")values " +
                ( dbDyn.getIsNeedUpdateBracket() ? "(" : "" ) +
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
                ( dbDyn.getIsNeedUpdateBracket() ? ")" : "" ) );

            int num = 1;
            RsetTools.setLong( ps, num++, siteId );
            RsetTools.setLong( ps, num++, site.getCompanyId() );
            ps.setString( num++, site.getDefLanguage() );
            ps.setString( num++, site.getDefCountry() );
            ps.setString( num++, site.getDefVariant() );
            ps.setString( num++, site.getSiteName() );
            ps.setString( num++, site.getAdminEmail() );
            ps.setInt( num++, site.getCssDynamic()?1:0 );
            ps.setString( num++, site.getCssFile() );
            ps.setInt( num++, site.getRegisterAllowed()?1:0 );
            ps.setString( num++, site.getProperties() );

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            if (virtualHosts!=null) {
                for (String s : virtualHosts) {
                    VirtualHostBean host = new VirtualHostBean(null, siteId, s );
                    createVirtualHost(dbDyn, host);
                }
            }

            dbDyn.commit();
            return siteId;
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error add new site";
            log.error( es, e );
            throw new IllegalStateException( es, e );

        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    public List<VirtualHostBean> getVirtualHostsFullList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSite(SiteBean siteBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createXslt(XsltBean xsltBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public SiteLanguageBean getSiteLanguage(Long siteId, String locale) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSiteLanguage(SiteLanguageBean siteLanguageBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getTemplate(String templateName, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createTemplate(TemplateBean templateBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createPortletName(PortletNameBean portletNameBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletId, Long templateId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogItem(CatalogBean catalogBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createVirtualHost(VirtualHostBean virtualHost) {

        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_VIRTUAL_HOST");
            seq.setTableName("WM_PORTAL_VIRTUAL_HOST");
            seq.setColumnName("ID_SITE_VIRTUAL_HOST");
            Long siteId = adapter.getSequenceNextValue(seq);

            WmPortalVirtualHostItemType item = new WmPortalVirtualHostItemType();
            item.setIdSiteVirtualHost(siteId);
            item.setIdSite(host.getSiteId());
            item.setNameVirtualHost(host.getHost());

            InsertWmPortalVirtualHostItem.process(adapter, item);
            return siteId;
        } catch (Throwable e) {
            String es = "Error create virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    public XsltBean getCurrentXslt(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogLanguageItem(CatalogLanguageBean bean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SiteBean getSite(String siteName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public XsltBean getXslt(String name, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CatalogLanguageBean getCatalogLanguageItem(String catalogCode, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PortletNameBean getPortletName(Long portletId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getTemplate(Long templateId, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
}
