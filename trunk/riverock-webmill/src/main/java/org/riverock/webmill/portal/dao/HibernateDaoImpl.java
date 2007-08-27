/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.riverock.webmill.a3.bean.RequestStatisticBean;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 17:14:08
 *         <p/>
 *         $Id$
 */
public class HibernateDaoImpl implements InternalDao {

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
        ConcurrentMap<String, Long> userAgent = new ConcurrentHashMap<String, Long>();
        return userAgent;
/*
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

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
*/
    }

    public ConcurrentMap<String, Long> getUrlList() {
        ConcurrentMap<String, Long> url = new ConcurrentHashMap<String, Long>();
        return url;
/*

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
*/
    }
/*

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

*/
    public void saveRequestStatistic(ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean) {
/*
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
*/
    }
}
