package org.riverock.portlet.webclip.manager.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.common.TreeItem;

/**
 * User: SMaslyukov
 * Date: 18.05.2007
 * Time: 18:53:23
 */
public class MenuItem implements Serializable, CatalogItem {

    private Long catalogId;
    private Long topCatalogId = 0L;
    private Long portletId;
    private Long contextId;
    private Long templateId;
    private Long catalogLanguageId;
    private Integer orderField;
    private String keyMessage;
    private String url;
    private String title;
    private String author;
    private String keyword;
    private String metadata;
    private String portletRole;
    private List<CatalogItem> subCatalogItemList = null;
    private boolean isIncludeInSitemap;

    public MenuItem(){}

    public MenuItem(CatalogItem item) {
        this.catalogId=item.getCatalogId();
        this.topCatalogId=item.getTopCatalogId();
        this.portletId=item.getPortletId();
        this.contextId=item.getContextId();
        this.templateId=item.getTemplateId();
        this.catalogLanguageId=item.getCatalogLanguageId();
        this.orderField=item.getOrderField();
        this.keyMessage=item.getKeyMessage();
        this.url=item.getUrl();
        this.title=item.getTitle();
        this.author=item.getAuthor();
        this.keyword=item.getKeyword();
        this.metadata=item.getMetadata();
        this.portletRole=item.getPortletRole();
        this.isIncludeInSitemap=item.isIncludeInSitemap();
        if (item.getSubCatalogItemList()!=null) {
            this.subCatalogItemList = new ArrayList<CatalogItem>();
            for (CatalogItem catalogItem : item.getSubCatalogItemList()) {
                this.subCatalogItemList.add(new MenuItem(catalogItem));
            }
        }
    }

    public boolean isIncludeInSitemap() {
        return isIncludeInSitemap;
    }

    public void setIncludeInSitemap(boolean includeInSitemap) {
        isIncludeInSitemap = includeInSitemap;
    }

    public List<CatalogItem> getSubCatalogItemList() {
        return subCatalogItemList;
    }

    public void setSubCatalogItemList(List<CatalogItem> subCatalogItemList) {
        this.subCatalogItemList = subCatalogItemList;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public Long getTopCatalogId() {
        return topCatalogId;
    }

    public void setTopCatalogId(Long topCatalogId) {
        this.topCatalogId = topCatalogId;
    }

    public Long getPortletId() {
        return portletId;
    }

    public void setPortletId(Long portletId) {
        this.portletId = portletId;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getCatalogLanguageId() {
        return catalogLanguageId;
    }

    public void setCatalogLanguageId(Long catalogLanguageId) {
        this.catalogLanguageId = catalogLanguageId;
    }

    public Integer getOrderField() {
        return orderField;
    }

    public void setOrderField(Integer orderField) {
        this.orderField = orderField;
    }

    public String getKeyMessage() {
        return keyMessage;
    }

    public void setKeyMessage(String keyMessage) {
        this.keyMessage = keyMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getPortletRole() {
        return portletRole;
    }

    public void setPortletRole(String portletRole) {
        this.portletRole = portletRole;
    }

    public Long getTopId() {
        return this.topCatalogId;
    }

    public Long getId() {
        return this.catalogId;
    }

    public List<TreeItem> getSubTree() {
        return (List)this.subCatalogItemList;
    }

    public void setSubTree(List<TreeItem> list) {
        this.subCatalogItemList=(List)list;
    }
}
