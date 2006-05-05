package org.riverock.webmill.portal.dao;

import java.util.Locale;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.schema.core.WmPortalCatalogItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogListType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageListType;
import org.riverock.webmill.core.GetWmPortalCatalogItem;
import org.riverock.webmill.core.GetWmPortalCatalogWithIdSiteCtxLangCatalogList;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageItem;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList;
import org.riverock.interfaces.portal.bean.CatalogItem;

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

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        if (log.isDebugEnabled()) {
            log.debug("InternalDaoImpl.getCatalogItemId()");
            log.debug("     siteId: " + siteId);
            log.debug("     locale: " + locale.toString().toLowerCase() );
            log.debug("     portletName: " + portletName);
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
                new Object[]{siteId, locale.toString().toLowerCase(), portletName, templateName}
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
                // Dont include menuitem with id_template==null to menu
                if (item.getIdSiteTemplate() != null) {
                    beans.add(initCatalogItem(item));
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
