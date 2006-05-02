package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalXsltDataWithIdSiteXsltList;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.port.PortalXslt;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.schema.core.WmPortalXsltDataItemType;
import org.riverock.webmill.schema.core.WmPortalXsltDataListType;

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
                "select d.ID_SITE_XSLT, d.TEXT_COMMENT " +
                "from   WM_PORTAL_XSLT d " +
                "where  d.ID_SITE_XSLT=? and d.IS_CURRENT=1";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, xsltId);
            rset = ps.executeQuery();

            PortalXsltBean portalXsltBean = null;
            if (rset.next()) {
                portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(xsltId);
                portalXsltBean.setName( RsetTools.getString(rset, "TEXT_COMMENT") );
                portalXsltBean.setXsltData(getXsltData(xsltId).toString() );


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

    public Xslt getXslt(String xsltName) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select d.ID_SITE_XSLT, d.TEXT_COMMENT " +
                "from   WM_PORTAL_XSLT d " +
                "where  d.TEXT_COMMENT=? and d.IS_CURRENT=1";

            ps = adapter.prepareStatement(sql_);

            ps.setString(1, xsltName);
            rset = ps.executeQuery();

            PortalXsltBean portalXsltBean = null;
            if (rset.next()) {
                portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId( RsetTools.getLong(rset, "ID_SITE_XSLT"));
                portalXsltBean.setName( RsetTools.getString(rset, "TEXT_COMMENT") );
                portalXsltBean.setXsltData(getXsltData(portalXsltBean.getId()).toString() );


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

    public Map<String, Xslt> getXsltMap(Long siteId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select a.CUSTOM_LANGUAGE, d.ID_SITE_XSLT, d.TEXT_COMMENT " +
                "from   WM_PORTAL_SITE_LANGUAGE a, WM_PORTAL_XSLT d " +
                "where  a.ID_SITE=? and " +
                "       a.ID_SITE_SUPPORT_LANGUAGE=d.ID_SITE_SUPPORT_LANGUAGE and " +
                "       d.IS_CURRENT=1";

            ps = adapter.prepareStatement(sql_);

            ps.setObject(1, siteId);
            rset = ps.executeQuery();
            Map<String, Xslt> map = new HashMap<String, Xslt>();

            while (rset.next()) {
                String lang = StringTools.getLocale(RsetTools.getString(rset, "CUSTOM_LANGUAGE")).toString();
                Long id = RsetTools.getLong(rset, "ID_SITE_XSLT");

                PortalXsltBean portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(id);
                portalXsltBean.setName( RsetTools.getString(rset, "TEXT_COMMENT") );
                portalXsltBean.setXsltData(getXsltData(adapter, id).toString() );
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
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }

    }

    public Map<String, XsltTransformer> getTransformerMap(Long siteId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select a.CUSTOM_LANGUAGE, d.ID_SITE_XSLT, d.TEXT_COMMENT " +
                "from   WM_PORTAL_SITE_LANGUAGE a, WM_PORTAL_XSLT d " +
                "where  a.ID_SITE=? and " +
                "       a.ID_SITE_SUPPORT_LANGUAGE=d.ID_SITE_SUPPORT_LANGUAGE and " +
                "       d.IS_CURRENT=1";

            ps = adapter.prepareStatement(sql_);

            ps.setObject(1, siteId);
            rset = ps.executeQuery();
            Map<String, XsltTransformer> map = new HashMap<String, XsltTransformer>();

            while (rset.next()) {
                String lang = StringTools.getLocale(RsetTools.getString(rset, "CUSTOM_LANGUAGE")).toString();
                Long id = RsetTools.getLong(rset, "ID_SITE_XSLT");

                PortalXsltBean portalXsltBean = new PortalXsltBean();
                portalXsltBean.setId(id);
                portalXsltBean.setName( RsetTools.getString(rset, "TEXT_COMMENT") );
                portalXsltBean.setXsltData(getXsltData(adapter, id).toString() );
                if (log.isDebugEnabled()) {
                    log.debug("XsltList. lang - " + lang);
                    log.debug("XsltList. id - " + id);
                }

                XsltTransformer item = new PortalXslt( portalXsltBean );
                map.put(lang, item);
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
