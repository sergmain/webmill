package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.Comparator;

import junit.framework.TestCase;
import junit.framework.Assert;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.common.TreeItem;

/**
 * User: SergeMaslyukov
 * Date: 29.04.2007
 * Time: 23:45:57
 */
public class TestCatalogComparator extends TestCase {
    private static class TempCatalogItem implements CatalogItem {

        private Long catalogId;
        private Long topCatalogId;
        private Integer orderField;
        private String keyMessage;

        public TempCatalogItem(Long catalogId, Long topCatalogId, Integer orderField, String keyMessage) {
            this.catalogId = catalogId;
            this.topCatalogId = topCatalogId;
            this.orderField = orderField;
            this.keyMessage = keyMessage;
        }

        public void setCatalogId(Long catalogId) {
            this.catalogId = catalogId;
        }

        public void setTopCatalogId(Long topCatalogId) {
            this.topCatalogId = topCatalogId;
        }

        public void setOrderField(Integer orderField) {
            this.orderField = orderField;
        }

        public void setKeyMessage(String keyMessage) {
            this.keyMessage = keyMessage;
        }

        public Long getCatalogId() {
            return catalogId;
        }

        public Long getTopCatalogId() {
            return topCatalogId;
        }

        public Long getPortletId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Long getContextId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Long getTemplateId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Long getCatalogLanguageId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Integer getOrderField() {
            return orderField;
        }

        public String getKeyMessage() {
            return keyMessage;
        }

        public String getUrl() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getTitle() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getAuthor() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getKeyword() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getMetadata() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getPortletRole() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public List<CatalogItem> getSubCatalogItemList() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean isIncludeInSitemap() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Long getTopId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Long getId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public List<TreeItem> getSubTree() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setSubTree(List<TreeItem> list) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public void testComparator() {
        CatalogItem item1 = new TempCatalogItem(0L, 0L, null, "Wwww");
        CatalogItem item2 = new TempCatalogItem(1L, 0L, null, "Aaaa");

        Comparator<CatalogItem> comparator = new HibernateCatalogDaoImpl.MenuItemComparator();

        Assert.assertEquals(1, comparator.compare(item1, item2));
        Assert.assertEquals(-1, comparator.compare(item2, item1));
        Assert.assertEquals(0, comparator.compare(item1, item1));
        Assert.assertEquals(0, comparator.compare(item2, item2));
    }

}
