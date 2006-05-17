package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
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
import org.riverock.webmill.core.InsertWmPortalXsltItem;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.port.PortalXslt;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.schema.core.WmPortalXsltDataItemType;
import org.riverock.webmill.schema.core.WmPortalXsltDataListType;
import org.riverock.webmill.schema.core.WmPortalXsltItemType;

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
                portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(xsltId);
                portalXsltBean.setName( item.getTextComment() );
                portalXsltBean.setXsltData( getXsltData(adapter, xsltId).toString() );
                portalXsltBean.setSiteLanguageId( item.getIdSiteSupportLanguage() );
                portalXsltBean.setCurrent(item.getIsCurrent() != null && item.getIsCurrent() );

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
                portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(item.getIdSiteXslt());
                portalXsltBean.setName( item.getTextComment() );
                portalXsltBean.setXsltData( getXsltData(adapter, item.getIdSiteXslt()).toString() );
                portalXsltBean.setSiteLanguageId( item.getIdSiteSupportLanguage() );
                portalXsltBean.setCurrent(item.getIsCurrent() != null && item.getIsCurrent() );

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
                portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(item.getIdSiteXslt());
                portalXsltBean.setName( item.getTextComment() );
                portalXsltBean.setXsltData( getXsltData(adapter, item.getIdSiteXslt()).toString() );
                portalXsltBean.setSiteLanguageId( item.getIdSiteSupportLanguage() );
                portalXsltBean.setCurrent(item.getIsCurrent() != null && item.getIsCurrent() );

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


}
