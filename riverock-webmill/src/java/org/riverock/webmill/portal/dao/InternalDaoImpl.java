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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.TemplateBean;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.a3.audit.RequestStatisticBean;
import org.riverock.webmill.core.*;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.main.ContentCSS;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.port.PortalXslt;
import org.riverock.webmill.port.PortalXsltList;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.PortletNameBean;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.bean.TemplateBeanImpl;
import org.riverock.webmill.portal.menu.PortalMenu;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.menu.PortalMenuLanguage;
import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.schema.core.*;
import org.riverock.webmill.site.PortalTemplateManagerImpl;

/**
 * @author SergeMaslyukov
 *         Date: 05.12.2005
 *         Time: 20:23:06
 *         $Id$
 */
public class InternalDaoImpl implements InternalDao {
    private final static Logger log = Logger.getLogger(InternalDaoImpl.class);

    public InternalDaoImpl() {
    }

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
                map.put(RsetTools.getString(rs, "NAME_VIRTUAL_HOST").toLowerCase(),
                    RsetTools.getLong(rs, "ID_SITE"));
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

    public CssBean getCssBean(Long siteId) {
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

    public SiteBean getSiteBean(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalListSiteItemType site = GetWmPortalListSiteItem.getInstance(adapter, siteId).item;

            SiteBean bean = new SiteBean();
            bean.setActivateEmailOrder( site.getIsActivateEmailOrder() );
            bean.setAdminEmail( site.getAdminEmail() );
            bean.setCompanyId( site.getIdFirm() );
            bean.setCssDynamic( site.getIsCssDynamic() );
            bean.setCssFile( site.getCssFile() );
            bean.setDefCountry( site.getDefCountry() );
            bean.setDefLanguage( site.getDefLanguage() );
            bean.setDefVariant( site.getDefVariant() );
            bean.setOrderEmail( site.getOrderEmail() );
            bean.setRegisterAllowed( site.getIsRegisterAllowed() );
            bean.setSiteId( site.getIdSite() );
            bean.setSiteName( site.getNameSite() );

            if (bean.getDefLanguage() == null)
                bean.setDefLanguage("");

            return bean;
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

    public SiteLanguageBean getSiteLanguageBean(Long siteLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalSiteLanguageItemType lang = GetWmPortalSiteLanguageItem.getInstance(adapter, siteLanguageId).item;

            SiteLanguageBean bean = new SiteLanguageBean();
            bean.setCustomLanguage( StringTools.getLocale( lang.getCustomLanguage()).toString() );
//            bean.setLanguageId( lang.getIdLanguage() );
            bean.setNameCustomLanguage( lang.getNameCustomLanguage() );
            bean.setSiteId( lang.getIdSite() );
            bean.setSiteLanguageId( lang.getIdSiteSupportLanguage() );
            return bean;
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

    static {
        try {
            Class c = SiteMenu.class;
            SqlStatement.registerRelateClass( c, PortalMenuLanguage.class );
            SqlStatement.registerRelateClass( c, GetWmPortalSiteLanguageWithIdSiteList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in ";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public List<SiteLanguageBean> getSiteLanguageList(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            List<SiteLanguageBean> list = new ArrayList<SiteLanguageBean>();
            WmPortalSiteLanguageListType langs = GetWmPortalSiteLanguageWithIdSiteList.getInstance(adapter, siteId).item;

            for (int i = 0; i < langs.getWmPortalSiteLanguageCount(); i++) {
                WmPortalSiteLanguageItemType lang = langs.getWmPortalSiteLanguage(i);
                SiteLanguageBean bean = new SiteLanguageBean();
                bean.setCustomLanguage( StringTools.getLocale(lang.getCustomLanguage()).toString() );
//                bean.setLanguageId( lang.getIdLanguage() );
                bean.setNameCustomLanguage( lang.getNameCustomLanguage() );
                bean.setSiteId( lang.getIdSite() );
                bean.setSiteLanguageId( lang.getIdSiteSupportLanguage() );
                list.add( bean );
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

    public CatalogLanguageBean getCatalogLanguageBean(Long catalogLanguageId ) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogLanguageItemType ic =
                GetWmPortalCatalogLanguageItem.getInstance(adapter, catalogLanguageId ).item;

            CatalogLanguageBean bean = new CatalogLanguageBean();
            bean.setCatalogCode( ic.getCatalogCode() );
            bean.setCatalogLanguageId( ic.getIdSiteCtxLangCatalog() );
            bean.setDefault( ic.getIsDefault() );
            bean.setSiteLanguageId( ic.getIdSiteSupportLanguage() );

            return bean;
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

    static{
        Class c = PortalMenuLanguage.class;
        try{
            SqlStatement.registerRelateClass( c, PortalMenu.class );
            SqlStatement.registerRelateClass( c, GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public List<CatalogLanguageBean> getCatalogLanguageList(Long siteLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogLanguageListType list =
                GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.
                getInstance(adapter, siteLanguageId )
                .item;

            List<CatalogLanguageBean> beans = new ArrayList<CatalogLanguageBean>();
            for (Object o : list.getWmPortalCatalogLanguageAsReference()) {
                WmPortalCatalogLanguageItemType ic = (WmPortalCatalogLanguageItemType) o;
                CatalogLanguageBean bean = new CatalogLanguageBean();
                bean.setCatalogCode(ic.getCatalogCode());
                bean.setCatalogLanguageId(ic.getIdSiteCtxLangCatalog());
                bean.setDefault(ic.getIsDefault());
                bean.setSiteLanguageId(siteLanguageId);
                beans.add(bean);
            }
            return beans;
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

    static{
        try{
            SqlStatement.registerRelateClass( PortalMenu.class, GetWmPortalCatalogWithIdSiteCtxLangCatalogList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public CatalogBean getCatalogBean(Long catalogId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogItemType catalogItem =
                    GetWmPortalCatalogItem.getInstance(adapter, catalogId).item;

            if (catalogItem==null) {
                return null;
            }

            return initCatalogBean(catalogItem);
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

    private static CatalogBean initCatalogBean(WmPortalCatalogItemType item) {
        CatalogBean bean = null;
        // Dont include menuitem with id_template==null to menu
        if (item.getIdSiteTemplate()!=null) {
            bean = new CatalogBean();
            bean.setAuthor( item.getCtxPageAuthor() );
            bean.setCatalogId( item.getIdSiteCtxCatalog() );
            bean.setCatalogLanguageId( item.getIdSiteCtxLangCatalog() );
            bean.setContextId( item.getIdContext() );
            bean.setPortletId( item.getIdSiteCtxType() );
            bean.setKeyMessage( item.getKeyMessage() );
            bean.setKeyword( item.getCtxPageKeyword() );
            bean.setMetadata( item.getMetadata() );
            bean.setOrderField( item.getOrderField() );
            bean.setPortletRole( item.getPortletRole() );
            bean.setStorage( item.getStorage() );
            bean.setTemplateId( item.getIdSiteTemplate() );
            bean.setTitle( item.getCtxPageTitle() );
            bean.setTopCatalogId( item.getIdTopCtxCatalog() );
            bean.setUrl( item.getCtxPageUrl() );
            bean.setUseProperties( item.getIsUseProperties() );
        }
        return bean;
    }

    public List<CatalogBean> getCatalogList(Long catalogLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogListType catalogList =
                    GetWmPortalCatalogWithIdSiteCtxLangCatalogList.getInstance(adapter, catalogLanguageId).item;

            List<WmPortalCatalogItemType> list = catalogList.getWmPortalCatalogAsReference();

            // remove call of sorting, when will be implemented sort in SQL query
            Collections.sort(list, new MenuItemComparator());

            List<CatalogBean> beans = new ArrayList<CatalogBean>();
            for (WmPortalCatalogItemType item : list) {
                // Dont include menuitem with id_template==null to menu
                if (item.getIdSiteTemplate() != null) {
                    beans.add(initCatalogBean(item));
                }
            }
            return beans;
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

    static {
        try {
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

    public TemplateBean getTemplateBean(Long templateId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalTemplateItemType template = GetWmPortalTemplateItem.getInstance(adapter, templateId).item;
            TemplateBeanImpl bean = null;
            if (template!=null) {
                bean = new TemplateBeanImpl();
                bean.setSiteLanguageId( template.getIdSiteSupportLanguage() );
                bean.setTemplateData( template.getTemplateData() );
                bean.setTemplateId( template.getIdSiteTemplate() );
                bean.setTemplateName( template.getNameSiteTemplate() );
            }
            return bean;
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

    public PortletNameBean getPortletNameBean(Long portletId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalPortletNameItemType ctxType = GetWmPortalPortletNameItem.getInstance(adapter, portletId ).item;
            PortletNameBean bean = new PortletNameBean();
            if (ctxType!=null) {
                bean.setName( ctxType.getType() );
                bean.setPortletId( portletId );
            }
            return bean;
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

    public Map<String, XsltTransformer> getTransformerMap(Long siteId) {
        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            String sql_ =
                "select a.CUSTOM_LANGUAGE, d.ID_SITE_XSLT " +
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

                if (log.isDebugEnabled()) {
                    log.debug("XsltList. lang - " + lang);
                    log.debug("XsltList. id - " + id);
                }

                XsltTransformer item = new PortalXslt( getXslt(adapter, id).toString() );
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

    public StringBuilder getXslt(Long xsltId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return getXslt( adapter, xsltId );
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

    private StringBuilder getXslt(DatabaseAdapter adapter, Long xsltId) throws PortalPersistenceException {
        StringBuilder sb = new StringBuilder();
        WmPortalXsltDataListType xsltList = GetWmPortalXsltDataWithIdSiteXsltList.getInstance(adapter, xsltId).item;
        for (int i = 0; i < xsltList.getWmPortalXsltDataCount(); i++) {
            WmPortalXsltDataItemType item = xsltList.getWmPortalXsltData(i);
            sb.append( item.getXslt() );
        }

        return sb;
    }


    static String templateSql =
        "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +
        "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE " +
        "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_SITE_LANGUAGE b " +
        "where  b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";
    static {
        try {
            SqlStatement.registerSql( sql_, PortalTemplateManagerImpl.class );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public List<TemplateBean> getTemplateList( Long siteId ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(templateSql);
            ps.setLong(1, siteId);

            rs = ps.executeQuery();

            List<TemplateBean> beans =  new ArrayList<TemplateBean>();
            while (rs.next()) {

                TemplateBeanImpl bean = new TemplateBeanImpl();
                bean.setSiteLanguageId( RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE") );
                bean.setTemplateData( RsetTools.getString(rs, "TEMPLATE_DATA") );
                bean.setTemplateId( RsetTools.getLong(rs, "ID_SITE_TEMPLATE") );
                bean.setTemplateName( RsetTools.getString(rs, "NAME_SITE_TEMPLATE") );
                bean.setTemplateLanguage( RsetTools.getString(rs, "CUSTOM_LANGUAGE") );

                beans.add( bean );
            }
            return beans;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    static String templateLanguageSql =
        "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +
        "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE " +
        "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_SITE_LANGUAGE b " +
        "where  b.ID_SITE_SUPPORT_LANGUAGE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";
    static {
        try {
            SqlStatement.registerSql( sql_, PortalTemplateManagerImpl.class );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public List<TemplateBean> getTemplateLanguageList( Long siteLanguageId ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(templateLanguageSql);
            ps.setLong(1, siteLanguageId);

            rs = ps.executeQuery();

            List<TemplateBean> beans =  new ArrayList<TemplateBean>();
            while (rs.next()) {

                TemplateBeanImpl bean = new TemplateBeanImpl();
                bean.setSiteLanguageId( siteLanguageId );
                bean.setTemplateData( RsetTools.getString(rs, "TEMPLATE_DATA") );
                bean.setTemplateId( RsetTools.getLong(rs, "ID_SITE_TEMPLATE") );
                bean.setTemplateName( RsetTools.getString(rs, "NAME_SITE_TEMPLATE") );
                bean.setTemplateLanguage( RsetTools.getString(rs, "CUSTOM_LANGUAGE") );

                beans.add( bean );
            }
            return beans;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public Long getCatalogId(Long siteId, Locale locale, String portletName, String templateName) {
        if (log.isDebugEnabled()) {
            log.debug("InternalDaoImpl.getCatalogId()");
            log.debug("     siteId: " + siteId);
            log.debug("     locale: " + locale.toString().toLowerCase() );
            log.debug("     portletName: " + portletName);
            log.debug("     templateName: " + templateName);
        }
        /*
select a.ID_SITE_CTX_CATALOG
from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c,
       	WM_PORTAL_PORTLET_NAME d, WM_PORTAL_TEMPLATE e
where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and
       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and
       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and
       a.ID_SITE_CTX_TYPE=d.ID_SITE_CTX_TYPE and
       a.ID_SITE_TEMPLATE=e.ID_SITE_TEMPLATE and
       d.TYPE=? and e.NAME_SITE_TEMPLATE=?
               */

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Long ctxId = DatabaseManager.getLongValue( adapter,
                "select a.ID_SITE_CTX_CATALOG " +
                "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c, " +
                "       WM_PORTAL_PORTLET_NAME d, WM_PORTAL_TEMPLATE e " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and " +
                "       a.ID_SITE_CTX_TYPE=d.ID_SITE_CTX_TYPE and " +
                "       a.ID_SITE_TEMPLATE=e.ID_SITE_TEMPLATE and " +
                "       d.TYPE=? and e.NAME_SITE_TEMPLATE=? ",
                new Object[]{siteId, locale.toString().toLowerCase(), portletName, templateName}
            );
            return ctxId;
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

    public Long getCatalogId(Long siteId, Locale locale, String pageName) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            Long ctxId = DatabaseManager.getLongValue(
                    adapter,
                    "select a.ID_SITE_CTX_CATALOG " +
                    "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                    "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and " +
                    "       a.CTX_PAGE_URL=?",
                    new Object[]{ siteId, locale.toString().toLowerCase(), pageName }
            );

            return ctxId;
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

    public Long getCatalogId(Long siteId, Locale locale, Long catalogId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            Long ctxId = DatabaseManager.getLongValue(
                adapter,
                "select a.ID_SITE_CTX_CATALOG " +
                "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and a.ID_SITE_CTX_CATALOG=?",
                    new Object[]{ siteId, locale.toString().toLowerCase(), catalogId }
            );

            return ctxId;
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

    private class MenuItemComparator implements Comparator<WmPortalCatalogItemType> {
        public int compare(WmPortalCatalogItemType o1, WmPortalCatalogItemType o2) {

            if (o1==null && o2==null)
                return 0;
            if (o1==null)
                return 1;
            if (o2==null)
                return -1;

            // "order by a.ID_TOP_CTX_CATALOG ASC, a.ORDER_FIELD ASC ";
            if ( o1.getIdTopCtxCatalog().equals( o2 .getIdTopCtxCatalog()))
            {
                if ( o1.getOrderField()==null && o2.getOrderField()==null)
                    return 0;

                if ( o1.getOrderField()!=null && o2.getOrderField()==null )
                    return -1;

                if ( o1.getOrderField()==null && o2.getOrderField()!=null)
                    return 1;

                return o1.getOrderField().compareTo( o2.getOrderField() );
            }
            else
                return o1.getIdTopCtxCatalog().compareTo( o2.getIdTopCtxCatalog() );
        }
    }
}
