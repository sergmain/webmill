package org.riverock.webmill.portal.dao;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;

import org.xml.sax.SAXException;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.CatalogBean;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 11:12:05
 */
public class OfflineInternalCatalogDao implements InternalCatalogDao {
    public static CatalogLanguageBean catalogLanguageBean1 = new CatalogLanguageBean(124L, "mill-right1-en", false, 13L);
    public static CatalogLanguageBean catalogLanguageBean2 = new CatalogLanguageBean(125L, "mill-right2-en", false, 13L);
    public static CatalogLanguageBean catalogLanguageBean3 = new CatalogLanguageBean(126L, "mill-right3-en", false, 13L);
    public static CatalogLanguageBean catalogLanguageBean4 = new CatalogLanguageBean(264L, "action-menu", false, 13L);
    public static CatalogLanguageBean catalogLanguageBean5 = new CatalogLanguageBean(130L, "mill-topmenu-en", true, 13L);
    /*
    124, mill-right1-en, 0, 13
    125, mill-right2-en, 0, 13
    126, mill-right3-en, 0, 13
    264, action-menu, 0, 13
    130, mill-topmenu-en, 1, 13
    */
    public static List<CatalogLanguageBean> catalogLanguageBeans = Arrays.asList(
        catalogLanguageBean1, catalogLanguageBean2, catalogLanguageBean3, catalogLanguageBean4, catalogLanguageBean5
    );

    public List<CatalogItem> catalogItems = new ArrayList<CatalogItem>();

    public static class CatalogItems {
        public List<CatalogItem> items = new ArrayList<CatalogItem>();

        public void addItem(CatalogItem item) {
            items.add(item);
        }
        
        public List<CatalogItem> getItems() {
            return items;
        }

        public void setItems(List<CatalogItem> items) {
            this.items = items;
        }
    }


    public OfflineInternalCatalogDao() throws IOException, SAXException {
        loadMenus();
        int i=0;
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteLanguageId, String pageUrl) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName, Long catalogId) {
        if (portletName==null || siteId==null || locale==null || templateName==null || catalogId==null) {
            return null;
        }

        String resultPortletName = portletName;
        if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
            resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
        }

        if (siteId==16L && locale.toString().equals("en") && resultPortletName.equals("mill.news_block") 
            && templateName.equals("templ-mill-dyn1-en") && catalogId==228L) {
            return 228L;
        }
        return null;
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String pageUrl) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId) {
        List<CatalogItem> result =new ArrayList<CatalogItem>();
        for (CatalogItem catalogItem : catalogItems) {
            if (catalogItem.getCatalogLanguageId().equals(catalogLanguageId)) {
                result.add(catalogItem);
            }
        }
        return result;
    }

    public CatalogItem getCatalogItem(Long catalogId) {
        for (CatalogItem catalogItem : catalogItems) {
            if (catalogItem.getCatalogId().equals(catalogId)) {
                return catalogItem;
            }
        }
        return null;  
    }

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId) {
        for (CatalogLanguageBean catalogLanguageBean : catalogLanguageBeans) {
            if (catalogLanguageBean.getCatalogLanguageId().equals(catalogLanguageId)) {
                return catalogLanguageBean;
            }
        }
        return null;  
    }

    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return null;
        }
        if (siteLanguageId==13L) {
            return (List)catalogLanguageBeans;
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogItem(CatalogItem catalogItem) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateCatalogItem(CatalogItem catalogItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteCatalogItem(Long catalogId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteCatalogLanguageItem(Long catalogLanguageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteCatalogLanguageForSiteLanguage(Long siteLanguageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getSiteId(Long catalogLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isUrlExist(String url, Long siteLanguageId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void changeTemplateForCatalogLanguage(Long catalogLanguageId, Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private void loadMenus() throws IOException, SAXException {
/*
<table>
	<row>
		<ID_SITE_CTX_CATALOG>19</ID_SITE_CTX_CATALOG>
		<ID_TOP_CTX_CATALOG>0</ID_TOP_CTX_CATALOG>
		<ORDER_FIELD>20</ORDER_FIELD>
		<ID_SITE_CTX_TYPE>12</ID_SITE_CTX_TYPE>
		<STORAGE></STORAGE>
		<KEY_MESSAGE>&#1053;&#1086;&#1074;&#1086;&#1089;&#1090;&#1080;</KEY_MESSAGE>
		<ID_CONTEXT></ID_CONTEXT>
		<IS_USE_PROPERTIES>0</IS_USE_PROPERTIES>
		<ID_SITE_TEMPLATE>4</ID_SITE_TEMPLATE>
		<ID_SITE_CTX_LANG_CATALOG>106</ID_SITE_CTX_LANG_CATALOG>
		<CTX_PAGE_URL>news</CTX_PAGE_URL>
		<CTX_PAGE_TITLE></CTX_PAGE_TITLE>
		<CTX_PAGE_AUTHOR></CTX_PAGE_AUTHOR>
		<CTX_PAGE_KEYWORD></CTX_PAGE_KEYWORD>
		<URL_RESOURCE></URL_RESOURCE>
		<METADATA></METADATA>
		<PORTLET_ROLE></PORTLET_ROLE>
	</row>
*/
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("table", CatalogItems.class);
        digester.addObjectCreate("table/row", CatalogBean.class);
        digester.addSetNext("table/row", "addItem");
        digester.addBeanPropertySetter("table/row/ID_SITE_CTX_CATALOG", "catalogId");
        digester.addBeanPropertySetter("table/row/ID_TOP_CTX_CATALOG", "topCatalogId");
        digester.addBeanPropertySetter("table/row/ORDER_FIELD", "orderField");
        digester.addBeanPropertySetter("table/row/ID_SITE_CTX_TYPE", "portletId");
//        digester.addBeanPropertySetter("table/row/STORAGE", "system");
        digester.addBeanPropertySetter("table/row/KEY_MESSAGE", "keyMessage");
        digester.addBeanPropertySetter("table/row/ID_CONTEXT", "contextId");
//        digester.addBeanPropertySetter("table/row/IS_USE_PROPERTIES", "system");
        digester.addBeanPropertySetter("table/row/ID_SITE_TEMPLATE", "templateId");
        digester.addBeanPropertySetter("table/row/ID_SITE_CTX_LANG_CATALOG", "catalogLanguageId");
        digester.addBeanPropertySetter("table/row/CTX_PAGE_URL", "url");
        digester.addBeanPropertySetter("table/row/CTX_PAGE_TITLE", "title");
        digester.addBeanPropertySetter("table/row/CTX_PAGE_AUTHOR", "author");
        digester.addBeanPropertySetter("table/row/CTX_PAGE_KEYWORD", "keyword");
//        digester.addBeanPropertySetter("table/row/URL_RESOURCE", "system");
        digester.addBeanPropertySetter("table/row/METADATA", "metadata");
        digester.addBeanPropertySetter("table/row/PORTLET_ROLE", "portletRole");

        InputStream inputStream = OfflineInternalCatalogDao.class.getResourceAsStream("/xml/site/menus.xml");

        CatalogItems items = (CatalogItems)digester.parse(inputStream);

        this.catalogItems = items.getItems();
    }

}
