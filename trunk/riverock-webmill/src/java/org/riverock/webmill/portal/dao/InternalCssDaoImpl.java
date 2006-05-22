package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalCssDataWithIdSiteContentCssList;
import org.riverock.webmill.core.GetWmPortalCssWithIdSiteList;
import org.riverock.webmill.core.InsertWmPortalCssItem;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.main.ContentCSS;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.schema.core.WmPortalCssDataItemType;
import org.riverock.webmill.schema.core.WmPortalCssDataListType;
import org.riverock.webmill.schema.core.WmPortalCssItemType;
import org.riverock.webmill.schema.core.WmPortalCssListType;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:34:41
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalCssDaoImpl implements InternalCssDao {
    private final static Logger log = Logger.getLogger( InternalCssDaoImpl.class );

    static String cssCurrentSql_ =
        "select a.date_post, b.css_data " +
        "from   WM_PORTAL_CSS a, WM_PORTAL_CSS_DATA b " +
        "where  a.ID_SITE=? and a.is_current=1 and " +
        "       a.id_site_content_css=b.id_site_content_css " +
        "order by ID_SITE_CONTENT_CSS_DATA asc";

    static {
        try {
            SqlStatement.registerSql(cssCurrentSql_, ContentCSS.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public CssBean getCssCurrent(Long siteId) {
        if (siteId == null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(cssCurrentSql_);

            RsetTools.setLong(ps, 1, siteId);
            rset = ps.executeQuery();
            CssBean cssBean = new CssBean();
            StringBuilder sb = new StringBuilder();
            while (rset.next()) {
                if (isFirstRecord) {
                    cssBean.setDate( RsetTools.getTimestamp(rset, "DATE_POST") );
                    isFirstRecord = false;
                }
                sb.append( RsetTools.getString(rset, "CSS_DATA", "") );

            }
            cssBean.setCss(sb.toString() );
            return cssBean;
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


    public Long createCss(Css css) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_CSS" );
            seq.setTableName( "WM_PORTAL_CSS" );
            seq.setColumnName( "ID_SITE_CONTENT_CSS" );
            Long id = adapter.getSequenceNextValue( seq );

            DatabaseManager.runSQL(
                adapter,
                "update WM_PORTAL_CSS set IS_CURRENT=0 where ID_SITE=?",
                new Object[] {css.getSiteId()},
                new int[]{Types.NUMERIC}
            );

            WmPortalCssItemType item = new WmPortalCssItemType();
            item.setIdSiteContentCss(id);
            item.setIdSite(css.getSiteId());
            item.setIsCurrent(css.isCurrent());
            item.setTextComment(css.getCssComment());
            item.setDatePost( new Date(System.currentTimeMillis()));

            InsertWmPortalCssItem.process(adapter, item);

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
                "ID_SITE_CONTENT_CSS",
                PrimaryKeyTypeTypeType.NUMBER,
                "WM_PORTAL_CSS_DATA",
                "ID_SITE_CONTENT_CSS_DATA",
                "CSS_DATA",
                css.getCss(),
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
            String es = "Error create css";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public List<Css> getCssList(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            List<Css> list = new ArrayList<Css>();
            WmPortalCssListType cssList = GetWmPortalCssWithIdSiteList.getInstance(adapter, siteId).item;
            for ( Object o : cssList.getWmPortalCssAsReference()) {
                WmPortalCssItemType css = (WmPortalCssItemType)o;
                CssBean cssBean = new CssBean();
                cssBean.setCss( getCssData(adapter, css.getIdSiteContentCss() ).toString() );
                cssBean.setCssComment(css.getTextComment());
                cssBean.setCssId(css.getIdSiteContentCss());
                cssBean.setCurrent(css.getIsCurrent());
                cssBean.setDate(css.getDatePost());
                cssBean.setSiteId(css.getIdSite());
                list.add(cssBean);
            }
            return list;
        } catch (Throwable e) {
            String es = "Error get css";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    static String cssSql_ =
        "select a.ID_SITE_CONTENT_CSS, a.IS_CURRENT, a.DATE_POST, a.TEXT_COMMENT, a.ID_SITE, b.css_data " +
        "from   WM_PORTAL_CSS a, WM_PORTAL_CSS_DATA b " +
        "where  a.ID_SITE_CONTENT_CSS=? and a.ID_SITE_CONTENT_CSS=b.ID_SITE_CONTENT_CSS " +
        "order by ID_SITE_CONTENT_CSS_DATA asc";

    static {
        try {
            SqlStatement.registerSql(cssCurrentSql_, ContentCSS.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public Css getCss(Long cssId) {
        if (cssId == null)
            return new CssBean();

        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(cssSql_);

            RsetTools.setLong(ps, 1, cssId);
            rset = ps.executeQuery();
            CssBean cssBean = new CssBean();
            StringBuilder sb = new StringBuilder();
            while (rset.next()) {
                if (isFirstRecord) {
                    cssBean.setCssId( RsetTools.getLong(rset, "ID_SITE_CONTENT_CSS") );
                    cssBean.setCurrent( RsetTools.getInt(rset, "IS_CURRENT", 0)==1 );
                    cssBean.setDate( RsetTools.getTimestamp(rset, "DATE_POST") );
                    cssBean.setCssComment( RsetTools.getString(rset, "TEXT_COMMENT") );
                    cssBean.setSiteId( RsetTools.getLong(rset, "ID_SITE") );
                    isFirstRecord = false;
                }
                sb.append( RsetTools.getString(rset, "CSS_DATA", "") );

            }
            cssBean.setCss(sb.toString() );
            return cssBean;
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

    private StringBuilder getCssData(DatabaseAdapter adapter, Long cssId) throws PortalPersistenceException {
        StringBuilder sb = new StringBuilder();
        WmPortalCssDataListType cssList = GetWmPortalCssDataWithIdSiteContentCssList.getInstance(adapter, cssId).item;
        for (Object o : cssList.getWmPortalCssDataAsReference()) {
            WmPortalCssDataItemType item = (WmPortalCssDataItemType)o;
            sb.append( item.getCssData() );
        }

        return sb;
    }

}
