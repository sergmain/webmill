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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.a3.audit.RequestStatisticBean;
import org.riverock.webmill.core.GetWmPortalAccessUrlFullList;
import org.riverock.webmill.core.GetWmPortalAccessUseragentFullList;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList;
import org.riverock.webmill.core.GetWmPortalCatalogWithIdSiteCtxLangCatalogList;
import org.riverock.webmill.core.GetWmPortalListSiteItem;
import org.riverock.webmill.core.GetWmPortalSiteLanguageWithIdSiteList;
import org.riverock.webmill.core.InsertWmPortalAccessStatItem;
import org.riverock.webmill.core.InsertWmPortalAccessUrlItem;
import org.riverock.webmill.core.InsertWmPortalAccessUseragentItem;
import org.riverock.webmill.main.ContentCSS;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.port.PortalXsltList;
import org.riverock.webmill.portal.menu.PortalMenu;
import org.riverock.webmill.portal.menu.PortalMenuLanguage;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.schema.core.WmPortalAccessStatItemType;
import org.riverock.webmill.schema.core.WmPortalAccessUrlItemType;
import org.riverock.webmill.schema.core.WmPortalAccessUrlListType;
import org.riverock.webmill.schema.core.WmPortalAccessUseragentItemType;
import org.riverock.webmill.schema.core.WmPortalAccessUseragentListType;
import org.riverock.webmill.site.PortalTemplateManagerImpl;

/**
 * @author SergeMaslyukov
 *         Date: 05.12.2005
 *         Time: 20:23:06
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalDaoImpl implements InternalDao {
    private final static Logger log = Logger.getLogger(InternalDaoImpl.class);

    public Collection<String> getSupportedLocales() {
        // Todo. return 'real' values
        Set<String> list = new HashSet<String>();
        list.add( "ru" );
        list.add( "en" );
        list.add( "ja" );
        return list;
/*
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmListLanguageListType languages = GetWmListLanguageFullList.getInstance( adapter, 1 ).item;
            for (int i=0; i<languages.getWmListLanguageCount(); i++){
                WmListLanguageItemType item = languages.getWmListLanguage( i );
                list.add( item.getShortNameLanguage() );
            }
            return list;
        }
        catch (Exception e) {
            String es = "Error get getSupportedLocales()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
*/
    }

    public ConcurrentMap<String, Long> getUserAgentList() {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalAccessUseragentListType userAgentList =
                GetWmPortalAccessUseragentFullList.getInstance(adapter, 0).item;

            ConcurrentMap<String, Long> userAgent = new ConcurrentHashMap<String, Long>(userAgentList.getWmPortalAccessUseragentCount() + 10);

            for (int i = 0; i < userAgentList.getWmPortalAccessUseragentCount(); i++) {
                WmPortalAccessUseragentItemType userAgentItem = userAgentList.getWmPortalAccessUseragent(i);
                userAgent.put( userAgentItem.getUserAgent(), userAgentItem.getIdSiteUserAgent() );
            }
            return userAgent;
        }
        catch (Exception e) {
            String es = "Error get getSupportedLocales()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public ConcurrentMap<String, Long> getUrlList() {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalAccessUrlListType urlList =
                GetWmPortalAccessUrlFullList.getInstance(adapter, 0).item;
            ConcurrentMap<String, Long> url = new ConcurrentHashMap<String, Long>(urlList.getWmPortalAccessUrlCount() + 10);
            for (int i = 0; i < urlList.getWmPortalAccessUrlCount(); i++) {
                WmPortalAccessUrlItemType urlItem = urlList.getWmPortalAccessUrl(i);
                url.put(urlItem.getUrl(), urlItem.getIdSiteAccessUrl());
            }
            return url;
        }
        catch (Exception e) {
            String es = "Error get getUrlList()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    static String sql_ = null;
    static {
        sql_ =
            "select a.ID_SITE, a.NAME_VIRTUAL_HOST from WM_PORTAL_VIRTUAL_HOST a";

        try {
            SqlStatement.registerSql( sql_, SiteList.class );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public Map<String, Long> getSiteIdMap() {

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(sql_);

            rs = ps.executeQuery();
            Map<String, Long> map = new HashMap<String, Long>();
            while (rs.next()) {
                map.put(RsetTools.getString(rs, "NAME_VIRTUAL_HOST").toLowerCase(), RsetTools.getLong(rs, "ID_SITE"));
            }
            return map;
        }
        catch(Exception e){
            final String es = "Error get list of virtual host ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    static String cssSql_ =
        "select a.date_post, b.css_data " +
        "from   WM_PORTAL_CSS a, WM_PORTAL_CSS_DATA b " +
        "where  a.ID_SITE=? and a.is_current=1 and " +
        "       a.id_site_content_css=b.id_site_content_css " +
        "order by ID_SITE_CONTENT_CSS_DATA asc";

    static {
        try {
            SqlStatement.registerSql(cssSql_, ContentCSS.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public CssBean getCss(Long siteId) {
        if (siteId == null)
            return new CssBean();

        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(cssSql_);

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

    public void saveRequestStatistic(ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            WmPortalAccessStatItemType stat = new WmPortalAccessStatItemType();

            Long userAgentId = userAgentList.get(bean.getUserAgent());
            if (userAgentId == null) {
                seq.setSequenceName("SEQ_WM_PORTAL_ACCESS_USERAGENT");
                seq.setTableName("WM_PORTAL_ACCESS_USERAGENT");
                seq.setColumnName("ID_SITE_USER_AGENT");
                userAgentId = adapter.getSequenceNextValue(seq);

                WmPortalAccessUseragentItemType item = new WmPortalAccessUseragentItemType();
                item.setIdSiteUserAgent(userAgentId);
                item.setUserAgent(bean.getUserAgent());
                InsertWmPortalAccessUseragentItem.process(adapter, item);
            }

            Long urlId = urlList.get( bean.getUrl() );
            if (urlId == null) {
                seq.setSequenceName("SEQ_WM_PORTAL_ACCESS_URL");
                seq.setTableName("WM_PORTAL_ACCESS_URL");
                seq.setColumnName("ID_SITE_ACCESS_URL");
                urlId = adapter.getSequenceNextValue(seq);
                WmPortalAccessUrlItemType item = new WmPortalAccessUrlItemType();
                item.setIdSiteAccessUrl(urlId);
                item.setUrl(bean.getUrl());
                InsertWmPortalAccessUrlItem.processData(adapter, item);
            }

            stat.setIdSiteAccessUserAgent(userAgentId);
            stat.setIdSiteAccessUrl(urlId);
            stat.setIsReferTooBig( bean.isReferTooBig() );
            stat.setRefer( bean.getRefer() );
            stat.setAccessDate( bean.getAccessDate() );
            Long idSite = SiteList.getIdSite(bean.getServerName());

            if (idSite == null) {
                stat.setServerName( bean.getServerName() );
            }

            stat.setIdSite(idSite);

            seq.setSequenceName("SEQ_WM_PORTAL_ACCESS_STAT");
            seq.setTableName("WM_PORTAL_ACCESS_STAT");
            seq.setColumnName("ID_SITE_ACCESS_STAT");
            stat.setIdSiteAccessStat(adapter.getSequenceNextValue(seq));

            stat.setIp(bean.getRemoteAddr());
            stat.setIsParamTooBig( bean.isParamTooBig() );
            stat.setIsReferTooBig( bean.isReferTooBig() );
            stat.setParameters( bean.getParameters() );
            stat.setRefer( bean.getRefer() );

            InsertWmPortalAccessStatItem.processData(adapter, stat);

            userAgentList.putIfAbsent(bean.getUserAgent(), userAgentId);
            urlList.putIfAbsent(bean.getUrl(), urlId);
            adapter.commit();
        }
        catch (Exception e) {
            String es = "Error saveRequestStatistic()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }

    }


    static {
        try {
            Class c = SiteMenu.class;
            SqlStatement.registerRelateClass( c, PortalMenuLanguage.class );
            SqlStatement.registerRelateClass( c, GetWmPortalSiteLanguageWithIdSiteList.class );

            c = PortalMenuLanguage.class;
            SqlStatement.registerRelateClass( c, PortalMenu.class );
            SqlStatement.registerRelateClass( c, GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.class );

            SqlStatement.registerRelateClass( PortalMenu.class, GetWmPortalCatalogWithIdSiteCtxLangCatalogList.class );

            Class p = PortalInfoImpl.class;
            SqlStatement.registerRelateClass(p, GetWmPortalListSiteItem.class);
            SqlStatement.registerRelateClass(p, PortalXsltList.class);
            SqlStatement.registerRelateClass(p, PortalTemplateManagerImpl.class);
            SqlStatement.registerRelateClass(p, GetWmPortalSiteLanguageWithIdSiteList.class);
            SqlStatement.registerRelateClass(p, SiteMenu.class);
        }
        catch (Exception exception) {
            final String es = "Exception in ";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

}
