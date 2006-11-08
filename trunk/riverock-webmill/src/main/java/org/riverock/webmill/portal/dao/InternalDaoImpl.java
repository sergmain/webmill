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
import java.sql.Types;
import java.util.*;
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
import org.riverock.webmill.core.*;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.menu.PortalMenu;
import org.riverock.webmill.portal.menu.PortalMenuLanguage;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.utils.SiteList;

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
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ConcurrentMap<String, Long> userAgent = new ConcurrentHashMap<String, Long>();

            ps = adapter.prepareStatement("select ID_SITE_USER_AGENT, USER_AGENT from WM_PORTAL_ACCESS_USERAGENT ");
            rs = ps.executeQuery();

            while(rs.next()){
                userAgent.put(RsetTools.getString(rs, "USER_AGENT"), RsetTools.getLong(rs, "ID_SITE_USER_AGENT") );
            }
            return userAgent;
        }
        catch (Exception e) {
            String es = "Error get getUserAgentList()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
        }
    }

    public ConcurrentMap<String, Long> getUrlList() {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ConcurrentMap<String, Long> url = new ConcurrentHashMap<String, Long>();

            ps = adapter.prepareStatement("select ID_SITE_ACCESS_URL, URL from WM_PORTAL_ACCESS_URL ");
            rs = ps.executeQuery();

            while(rs.next()){
                url.put(RsetTools.getString(rs, "URL"), RsetTools.getLong(rs, "ID_SITE_ACCESS_URL") );
            }
            return url;
        }
        catch (Exception e) {
            String es = "Error get getUrlList()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter, rs, ps);
            rs = null;
            ps = null;
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

    public void saveRequestStatistic(ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();

            Long userAgentId = userAgentList.get(bean.getUserAgent());
            if (userAgentId == null) {
                seq.setSequenceName("SEQ_WM_PORTAL_ACCESS_USERAGENT");
                seq.setTableName("WM_PORTAL_ACCESS_USERAGENT");
                seq.setColumnName("ID_SITE_USER_AGENT");
                userAgentId = adapter.getSequenceNextValue(seq);

                DatabaseManager.runSQL(
                    adapter,
                     "insert into WM_PORTAL_ACCESS_USERAGENT"+
                         "(ID_SITE_USER_AGENT, USER_AGENT)"+
                         "values"+
                         "( ?,  ?)",
                    new Object[]{userAgentId, bean.getUserAgent()},
                    new int[]{Types.DECIMAL, Types.VARCHAR}
                );
            }

            Long urlId = urlList.get( bean.getUrl() );
            if (urlId == null) {
                seq.setSequenceName("SEQ_WM_PORTAL_ACCESS_URL");
                seq.setTableName("WM_PORTAL_ACCESS_URL");
                seq.setColumnName("ID_SITE_ACCESS_URL");
                urlId = adapter.getSequenceNextValue(seq);

                DatabaseManager.runSQL(
                    adapter,
                    "insert into WM_PORTAL_ACCESS_URL"+
                        "(ID_SITE_ACCESS_URL, URL)"+
                        "values"+
                        "( ?,  ?)",
                    new Object[]{urlId, bean.getUrl()},
                    new int[]{Types.DECIMAL, Types.VARCHAR}
                );
            }

            Long idSite = SiteList.getSiteId(bean.getServerName());

            seq.setSequenceName("SEQ_WM_PORTAL_ACCESS_STAT");
            seq.setTableName("WM_PORTAL_ACCESS_STAT");
            seq.setColumnName("ID_SITE_ACCESS_STAT");
            long accessStatId = adapter.getSequenceNextValue(seq);

            DatabaseManager.runSQL(
                adapter,
                "insert into WM_PORTAL_ACCESS_STAT"+
                    "(ID_SITE_ACCESS_STAT, IP, ID_SITE, IS_REFER_TOO_BIG, ACCESS_DATE, "+
                    "REFER, ID_SITE_ACCESS_USER_AGENT, PARAMETERS, ID_SITE_ACCESS_URL, "+
                    "IS_PARAM_TOO_BIG, SERVER_NAME)"+
                    "values"+
                    "( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?)",
                new Object[]{
                    accessStatId, bean.getRemoteAddr(), idSite, bean.isReferTooBig()?1:0, bean.getAccessDate(), bean.getRefer(), userAgentId, bean.getParameters(),
                    urlId, bean.isParamTooBig()?1:0, idSite==null?bean.getServerName():null
                },
                new int[] {
                    Types.DECIMAL, Types.DECIMAL, Types.DECIMAL, Types.DECIMAL, Types.TIMESTAMP, Types.VARCHAR, Types.DECIMAL, Types.VARCHAR,
                    Types.DECIMAL, Types.DECIMAL, Types.VARCHAR
                }
            );

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

            SqlStatement.registerRelateClass( PortalTemplateManagerImpl.class, GetWmPortalTemplateItem.class );

            c = PortalMenuLanguage.class;
            SqlStatement.registerRelateClass( c, PortalMenu.class );
            SqlStatement.registerRelateClass( c, GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.class );

            SqlStatement.registerRelateClass( PortalMenu.class, GetWmPortalCatalogWithIdSiteCtxLangCatalogList.class );

            Class p = PortalInfoImpl.class;
            SqlStatement.registerRelateClass(p, GetWmPortalListSiteItem.class);
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
