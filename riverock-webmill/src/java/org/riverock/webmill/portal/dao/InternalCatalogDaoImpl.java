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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.core.GetWmPortalCatalogItem;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageItem;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList;
import org.riverock.webmill.core.GetWmPortalCatalogWithIdSiteCtxLangCatalogList;
import org.riverock.webmill.core.InsertWmPortalCatalogItem;
import org.riverock.webmill.core.InsertWmPortalCatalogLanguageItem;
import org.riverock.webmill.core.UpdateWmPortalCatalogItem;
import org.riverock.webmill.core.UpdateWmPortalCatalogLanguageItem;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.schema.core.WmPortalCatalogItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageListType;
import org.riverock.webmill.schema.core.WmPortalCatalogListType;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 15:47:34
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalCatalogDaoImpl implements InternalCatalogDao {
    private final static Logger log = Logger.getLogger(InternalCatalogDaoImpl.class);

    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogItemId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            return DatabaseManager.getLongValue(
                adapter,
                "select a.ID_SITE_CTX_CATALOG " +
                "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and a.ID_SITE_CTX_CATALOG=?",
                    new Object[]{ siteId, locale.toString().toLowerCase(), catalogItemId }
            );
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

    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId) {
        if (log.isDebugEnabled()) {
            log.debug("InternalDaoCatalogImpl.getCatalogItemId()");
            log.debug("     siteLanguageId: " + siteLanguageId);
            log.debug("     portletNameId: " + portletNameId);
            log.debug("     templateId: " + templateId);
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return DatabaseManager.getLongValue( adapter,
                "select a.ID_SITE_CTX_CATALOG " +
                "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=? and a.ID_SITE_CTX_TYPE=? and a.ID_SITE_TEMPLATE=? ",
                new Object[]{siteLanguageId, portletNameId, templateId}
            );
        }
        catch (Exception e) {
            String es = "Error get getCatalogItemId()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        if (portletName==null) {
            return null;
        }

        String resultPortletName = portletName;
        if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
            resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
        }

        if (log.isDebugEnabled()) {
            log.debug("InternalDaoImpl.getCatalogItemId()");
            log.debug("     siteId: " + siteId);
            log.debug("     locale: " + locale.toString().toLowerCase() );
            log.debug("     portletName: " + portletName);
            log.debug("     resultPortletName: " + resultPortletName);
            log.debug("     templateName: " + templateName);
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return DatabaseManager.getLongValue( adapter,
                "select a.ID_SITE_CTX_CATALOG " +
                "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c, " +
                "       WM_PORTAL_PORTLET_NAME d, WM_PORTAL_TEMPLATE e " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and " +
                "       a.ID_SITE_CTX_TYPE=d.ID_SITE_CTX_TYPE and " +
                "       a.ID_SITE_TEMPLATE=e.ID_SITE_TEMPLATE and " +
                "       d.TYPE=? and e.NAME_SITE_TEMPLATE=? ",
                new Object[]{siteId, locale.toString().toLowerCase(), resultPortletName, templateName}
            );
        }
        catch (Exception e) {
            String es = "Error get getCatalogItemId()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String pageName) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            return DatabaseManager.getLongValue(
                    adapter,
                    "select a.ID_SITE_CTX_CATALOG " +
                    "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                    "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and " +
                    "       a.CTX_PAGE_URL=?",
                    new Object[]{ siteId, locale.toString().toLowerCase(), pageName }
            );
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

    public CatalogItem getCatalogItem(Long catalogId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCatalogItem(), catalogId: "+catalogId);
        }
        if (catalogId==null) {
            return null;
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogItemType catalogItem =
                    GetWmPortalCatalogItem.getInstance(adapter, catalogId).item;

            if (catalogItem==null) {
                return null;
            }

            return initCatalogItem(catalogItem);
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

    private static CatalogItem initCatalogItem(WmPortalCatalogItemType item) {
        CatalogBean bean = null;
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
        bean.setTemplateId( item.getIdSiteTemplate() );
        bean.setTitle( item.getCtxPageTitle() );
        bean.setTopCatalogId( item.getIdTopCtxCatalog() );
        bean.setUrl( item.getCtxPageUrl() );

        return bean;
    }

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogListType catalogList =
                    GetWmPortalCatalogWithIdSiteCtxLangCatalogList.getInstance(adapter, catalogLanguageId).item;

            List<WmPortalCatalogItemType> list = catalogList.getWmPortalCatalogAsReference();

            // remove call of sorting, when will be implemented sort in SQL query
            Collections.sort(list, new MenuItemComparator());

            List<CatalogItem> beans = new ArrayList<CatalogItem>();
            for (WmPortalCatalogItemType item : list) {
                beans.add(initCatalogItem(item));
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

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId ) {
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

    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogLanguageListType list =
                GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.
                getInstance(adapter, siteLanguageId )
                .item;

            List<CatalogLanguageItem> beans = new ArrayList<CatalogLanguageItem>();
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

    public Long createCatalogItem(CatalogItem catalogItem) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_CATALOG" );
            seq.setTableName( "WM_PORTAL_CATALOG" );
            seq.setColumnName( "ID_SITE_CTX_CATALOG" );
            Long id = adapter.getSequenceNextValue( seq );

            WmPortalCatalogItemType item = new WmPortalCatalogItemType();
            item.setIdSiteCtxCatalog(id);
            item.setCtxPageAuthor( catalogItem.getAuthor());
            item.setCtxPageKeyword( catalogItem.getKeyword());
            item.setCtxPageTitle( catalogItem.getTitle());
            item.setCtxPageUrl( catalogItem.getUrl());
            item.setIdContext( catalogItem.getContextId());
            item.setIdSiteCtxLangCatalog( catalogItem.getCatalogLanguageId());
            item.setIdSiteCtxType( catalogItem.getPortletId());
            item.setIdSiteTemplate( catalogItem.getTemplateId());
            item.setIdTopCtxCatalog( catalogItem.getTopCatalogId());
            if (catalogItem.getTopCatalogId()==null)
                item.setIdTopCtxCatalog(0L);

            item.setKeyMessage( catalogItem.getKeyMessage());
            item.setMetadata( catalogItem.getMetadata());
            item.setOrderField( catalogItem.getOrderField());
            item.setPortletRole( catalogItem.getPortletRole());

            InsertWmPortalCatalogItem.process(adapter, item);

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

    public void updateCatalogItem(CatalogItem catalogItem) {
        if (catalogItem==null) {
            return;
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogItemType item = new WmPortalCatalogItemType();
            item.setCtxPageAuthor(catalogItem.getAuthor());
            item.setCtxPageKeyword(catalogItem.getKeyword());
            item.setCtxPageTitle(catalogItem.getTitle());
            item.setCtxPageUrl(catalogItem.getUrl());
            item.setIdContext(catalogItem.getContextId());
            item.setIdSiteCtxCatalog(catalogItem.getCatalogId());
            item.setIdSiteCtxLangCatalog(catalogItem.getCatalogLanguageId());
            item.setIdSiteCtxType(catalogItem.getPortletId());
            item.setIdSiteTemplate(catalogItem.getTemplateId());
            item.setIdTopCtxCatalog(catalogItem.getTopCatalogId());
            item.setKeyMessage(catalogItem.getKeyMessage());
            item.setMetadata(catalogItem.getMetadata());
            item.setOrderField(catalogItem.getOrderField());
            item.setPortletRole(catalogItem.getPortletRole());

            UpdateWmPortalCatalogItem.process(adapter, item);
            
            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public void deleteCatalogItem(Long catalogId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_CATALOG where ID_SITE_CTX_CATALOG=?",
                new Object[]{catalogId}, new int[]{Types.DECIMAL}
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
            String es = "Error delete catalog item";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        if (log.isDebugEnabled()) {
            log.debug("Item getIdSiteCtxLangCatalog(), value - "+catalogLanguageItem.getCatalogLanguageId());
            log.debug("Item getCatalogCode(), value - "+catalogLanguageItem.getCatalogCode());
            log.debug("Item getIsDefault(), value - "+catalogLanguageItem.getDefault());
            log.debug("Item getIdSiteSupportLanguage(), value - "+catalogLanguageItem.getSiteLanguageId());
        }
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_CATALOG_LANGUAGE" );
            seq.setTableName( "WM_PORTAL_CATALOG_LANGUAGE" );
            seq.setColumnName( "ID_SITE_CTX_LANG_CATALOG" );
            Long id = adapter.getSequenceNextValue( seq );

            WmPortalCatalogLanguageItemType item = new WmPortalCatalogLanguageItemType();
            item.setIdSiteCtxLangCatalog(id);
            item.setIdSiteSupportLanguage(catalogLanguageItem.getSiteLanguageId());
            item.setCatalogCode(catalogLanguageItem.getCatalogCode());
            item.setIsDefault(catalogLanguageItem.getDefault());

            InsertWmPortalCatalogLanguageItem.process(adapter, item);

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

    public void updateCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        if (log.isDebugEnabled()) {
            log.debug("catalogLanguageItem: " + catalogLanguageItem);
        }
        if (catalogLanguageItem==null) {
            return;
        }

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCatalogLanguageItemType item = new WmPortalCatalogLanguageItemType();
            item.setCatalogCode(catalogLanguageItem.getCatalogCode());
            item.setIdSiteCtxLangCatalog(catalogLanguageItem.getCatalogLanguageId());
            item.setIdSiteSupportLanguage(catalogLanguageItem.getSiteLanguageId());
            item.setIsDefault(catalogLanguageItem.getDefault());

            UpdateWmPortalCatalogLanguageItem.process(adapter, item);

            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public void deleteCatalogLanguageItem(Long catalogLanguageId) {
        if (log.isDebugEnabled()) {
           log.debug("catalogLanguageId: " +catalogLanguageId);
        }
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_CATALOG_LANGUAGE where ID_SITE_CTX_LANG_CATALOG=?",
                new Object[]{catalogLanguageId}, new int[]{Types.DECIMAL}
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
            String es = "Error delete catalog language";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_CATALOG_LANGUAGE where CATALOG_CODE=? and id_site_support_language=?"
            );

            RsetTools.setString(ps, 1, catalogLanguageCode);
            RsetTools.setLong(ps, 2, siteLanguageId);
            rs = ps.executeQuery();
            if (rs.next()) {
                WmPortalCatalogLanguageItemType item = GetWmPortalCatalogLanguageItem.fillBean(rs);
                CatalogLanguageBean bean = new CatalogLanguageBean();
                bean.setCatalogCode( item.getCatalogCode() );
                bean.setCatalogLanguageId( item.getIdSiteCtxLangCatalog() );
                bean.setDefault( item.getIsDefault() );
                bean.setSiteLanguageId( item.getIdSiteSupportLanguage() );

                return bean;
            }
            return null;
        }
        catch (Exception e) {
            final String es = "Error get catalogLanguageItem bean for name: " + catalogLanguageCode;
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
