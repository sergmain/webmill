/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalXsltDataWithIdSiteXsltList;
import org.riverock.webmill.core.GetWmPortalXsltItem;
import org.riverock.webmill.core.GetWmPortalXsltWithIdSiteSupportLanguageList;
import org.riverock.webmill.core.InsertWmPortalXsltItem;
import org.riverock.webmill.core.UpdateWmPortalXsltItem;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.port.PortalXslt;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.schema.core.WmPortalXsltDataItemType;
import org.riverock.webmill.schema.core.WmPortalXsltDataListType;
import org.riverock.webmill.schema.core.WmPortalXsltItemType;
import org.riverock.webmill.schema.core.WmPortalXsltListType;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:46:43
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalXsltDaoImpl implements InternalXsltDao {
    private final static Logger log = Logger.getLogger(InternalXsltDaoImpl.class);


    public Xslt getXslt(Long xsltId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select * " +
                "from   WM_PORTAL_XSLT " +
                "where  ID_SITE_XSLT=?";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, xsltId);
            rset = ps.executeQuery();

            PortalXsltBean portalXsltBean = null;
            if (rset.next()) {
                WmPortalXsltItemType item = GetWmPortalXsltItem.fillBean(rset);
                portalXsltBean = initXsltBean(adapter, item);

                if (log.isDebugEnabled()) {
                    log.debug("XsltList. id - " + xsltId);
                }
            }
            return portalXsltBean;
        }
        catch (Exception e) {
            final String es = "Error get xslt ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }
    }

    private PortalXsltBean initXsltBean(DatabaseAdapter adapter, WmPortalXsltItemType item) throws PortalPersistenceException {
        PortalXsltBean portalXsltBean = new PortalXsltBean();
        portalXsltBean.setId(item.getIdSiteXslt());
        portalXsltBean.setName( item.getTextComment() );
        portalXsltBean.setXsltData( getXsltData(adapter, item.getIdSiteXslt() ).toString() );
        portalXsltBean.setSiteLanguageId( item.getIdSiteSupportLanguage() );
        portalXsltBean.setCurrent(item.getIsCurrent() != null && item.getIsCurrent() );
        return portalXsltBean;
    }

    public Xslt getXslt(String xsltName, Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select * " +
                "from   WM_PORTAL_XSLT " +
                "where  TEXT_COMMENT=? and ID_SITE_SUPPORT_LANGUAGE=?";

            ps = adapter.prepareStatement(sql_);

            ps.setString(1, xsltName);
            ps.setLong(2, siteLanguageId);
            rset = ps.executeQuery();

            PortalXsltBean portalXsltBean = null;
            if (rset.next()) {
                WmPortalXsltItemType item = GetWmPortalXsltItem.fillBean(rset);
                portalXsltBean = initXsltBean(adapter, item);

                if (log.isDebugEnabled()) {
                    log.debug("XsltList. id - " + portalXsltBean.getId());
                }
            }
            return portalXsltBean;
        }
        catch (Exception e) {
            final String es = "Error get xslt ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }
    }

    public Xslt getCurrentXslt(Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select * " +
                "from   WM_PORTAL_XSLT " +
                "where  ID_SITE_SUPPORT_LANGUAGE=? and IS_CURRENT=1";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, siteLanguageId);
            rset = ps.executeQuery();

            PortalXsltBean portalXsltBean = null;
            if (rset.next()) {
                WmPortalXsltItemType item = GetWmPortalXsltItem.fillBean(rset);
                portalXsltBean = initXsltBean(adapter, item);

                if (log.isDebugEnabled()) {
                    log.debug("XsltList. id - " + portalXsltBean.getId());
                }
            }
            return portalXsltBean;
        }
        catch (Exception e) {
            final String es = "Error get xslt ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }
    }

    public Long createXslt(Xslt xslt) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_XSLT" );
            seq.setTableName( "WM_PORTAL_XSLT" );
            seq.setColumnName( "ID_SITE_XSLT" );
            Long id = adapter.getSequenceNextValue( seq );

            DatabaseManager.runSQL(
                adapter,
                "update WM_PORTAL_XSLT set IS_CURRENT=0 where ID_SITE_SUPPORT_LANGUAGE=?",
                new Object[] {xslt.getSiteLanguageId()},
                new int[]{Types.NUMERIC}
            );

            WmPortalXsltItemType item = new WmPortalXsltItemType();
            item.setIdSiteXslt(id);
            item.setIdSiteSupportLanguage(xslt.getSiteLanguageId());
            item.setIsCurrent(xslt.isCurrent());
            item.setTextComment(xslt.getName());
            item.setXslt(null);

            InsertWmPortalXsltItem.process(adapter, item);

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
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error create site language";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public List<Xslt> getXsltList(Long siteLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            List<Xslt> list = new ArrayList<Xslt>();
            WmPortalXsltListType xsltList = GetWmPortalXsltWithIdSiteSupportLanguageList.getInstance(adapter, siteLanguageId).item;
            for (Object o : xsltList.getWmPortalXsltAsReference()) {
                PortalXsltBean xslt = initXsltBean(adapter, (WmPortalXsltItemType)o);
                list.add(xslt);
            }
            return list;
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

    public void deleteXsltForSite(DatabaseAdapter adapter, Long siteId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_XSLT_DATA " +
                    "where ID_SITE_XSLT in " +
                    "(select a.ID_SITE_XSLT from WM_PORTAL_XSLT a, WM_PORTAL_SITE_LANGUAGE b " +
                    "where b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_XSLT " +
                    "where ID_SITE_SUPPORT_LANGUAGE in " +
                    "(select a.ID_SITE_SUPPORT_LANGUAGE from WM_PORTAL_SITE_LANGUAGE a " +
                    "where   a.ID_SITE=? )",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete xslt for site";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteXsltForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_XSLT_DATA " +
                    "where ID_SITE_XSLT in " +
                    "(select a.ID_SITE_XSLT from WM_PORTAL_XSLT a " +
                    "where a.ID_SITE_SUPPORT_LANGUAGE=?)",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_XSLT where ID_SITE_SUPPORT_LANGUAGE=?",
                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete xslt for site language";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void updateXslt(Xslt xslt) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            clearCurrentFlag(xslt, adapter);

            WmPortalXsltItemType item = new WmPortalXsltItemType();
            item.setIdSiteSupportLanguage(xslt.getSiteLanguageId());
            item.setIdSiteXslt(xslt.getId());
            item.setIsCurrent(xslt.isCurrent());
            item.setTextComment(xslt.getName());
            item.setXslt(null);

            UpdateWmPortalXsltItem.process(adapter, item);
            DatabaseManager.insertBigText(
                adapter,
                xslt.getId(),
                "ID_SITE_XSLT",
                PrimaryKeyTypeTypeType.NUMBER,
                "WM_PORTAL_XSLT_DATA",
                "ID_SITE_XSLT_DATA",
                "XSLT",
                xslt.getXsltData(),
                true
            );

            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error update xslt";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }

    }

    public void deleteXslt(Long xsltId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            WmPortalXsltItemType currentXslt = GetWmPortalXsltItem.getInstance(adapter, xsltId).item;
            if (currentXslt!=null && currentXslt.getIsCurrent()) {
                // dont delete Xslt, if this Xslt is 'current'
                return;
            }

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_XSLT_DATA " +
                    "where ID_SITE_XSLT=?",

                new Object[]{xsltId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_XSLT where ID_SITE_XSLT=?",
                new Object[]{xsltId}, new int[]{Types.DECIMAL}
            );
            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error delete css";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return getCurrentXsltForSiteMap( adapter, siteId );
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

    /**
     *
     * @param adapter DatabaseAdapter
     * @param siteId  siteId
     * @return Map<String, Xslt> - String site locale, Xslt - xslt template
     */
    public Map<String, Xslt> getCurrentXsltForSiteMap(DatabaseAdapter adapter, Long siteId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            String sql_ =
                "select a.CUSTOM_LANGUAGE, d.ID_SITE_XSLT, d.TEXT_COMMENT, d.ID_SITE_SUPPORT_LANGUAGE, d.IS_CURRENT " +
                "from   WM_PORTAL_SITE_LANGUAGE a, WM_PORTAL_XSLT d " +
                "where  a.ID_SITE=? and " +
                "       a.ID_SITE_SUPPORT_LANGUAGE=d.ID_SITE_SUPPORT_LANGUAGE and d.IS_CURRENT=1";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, siteId);
            rset = ps.executeQuery();
            Map<String, Xslt> map = new HashMap<String, Xslt>();

            while (rset.next()) {
                String lang = StringTools.getLocale(RsetTools.getString(rset, "CUSTOM_LANGUAGE")).toString();
                Long id = RsetTools.getLong(rset, "ID_SITE_XSLT");

                PortalXsltBean portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(id);
                portalXsltBean.setName( RsetTools.getString(rset, "TEXT_COMMENT") );
                portalXsltBean.setXsltData(getXsltData(adapter, id).toString() );
                portalXsltBean.setSiteLanguageId( RsetTools.getLong(rset, "ID_SITE_SUPPORT_LANGUAGE") );
                portalXsltBean.setCurrent( RsetTools.getInt(rset, "IS_CURRENT", 0)==1 );

                if (log.isDebugEnabled()) {
                    log.debug("XsltList. lang - " + lang);
                    log.debug("XsltList. id - " + id);
                }

                map.put(lang, portalXsltBean);
            }

            return map;
        }
        catch (Exception e) {
            final String es = "Error get css ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }
    }

    public Map<String, XsltTransformer> getTransformerForCurrentXsltMap(Long siteId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Map<String, XsltTransformer> map = new HashMap<String, XsltTransformer>();
            for ( Map.Entry<String, Xslt> entry : getCurrentXsltForSiteMap(adapter, siteId).entrySet() ) {
                map.put(entry.getKey(), new PortalXslt( entry.getValue() ));
            }
            return map;
        }
        catch (Exception e) {
            final String es = "Error get css ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }
    }

    static {
        try {
            SqlStatement.registerRelateClass(PortalXslt.class, GetWmPortalXsltDataWithIdSiteXsltList.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in ";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public StringBuilder getXsltData(Long xsltId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return getXsltData( adapter, xsltId );
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

    private StringBuilder getXsltData(DatabaseAdapter adapter, Long xsltId) throws PortalPersistenceException {
        StringBuilder sb = new StringBuilder();
        WmPortalXsltDataListType xsltList = GetWmPortalXsltDataWithIdSiteXsltList.getInstance(adapter, xsltId).item;
        for (int i = 0; i < xsltList.getWmPortalXsltDataCount(); i++) {
            WmPortalXsltDataItemType item = xsltList.getWmPortalXsltData(i);
            sb.append( item.getXslt() );
        }

        return sb;
    }

    private void clearCurrentFlag(Xslt xslt, DatabaseAdapter adapter) throws SQLException {
        if (xslt.isCurrent()) {
            DatabaseManager.runSQL(
                adapter,
                "update WM_PORTAL_XSLT set IS_CURRENT=0 where ID_SITE_SUPPORT_LANGUAGE=?",
                new Object[] {xslt.getSiteLanguageId()},
                new int[]{Types.NUMERIC}
            );
        }
    }
}
