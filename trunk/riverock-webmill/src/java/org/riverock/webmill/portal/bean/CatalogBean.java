package org.riverock.webmill.portal.bean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 15:52:55
 *         $Id$
 */
public class CatalogBean {
   private Long catalogId; // idSiteCtxCatalog
   private Long topCatalogId = 0l; //idTopCtxCatalog
   private Long portletId; //idSiteCtxType
   private Long contextId;
   private Boolean isUseProperties = false;
   private Long templateId; //idSiteTemplate
   private Long catalogLanguageId; // idSiteCtxLangCatalog;
   private Integer orderField;
   private String storage;
   private String keyMessage;
   private String url; //ctxPageUrl;
   private String title; //ctxPageTitle;
   private String author; //ctxPageAuthor;
   private String keyword; //ctxPageKeyword;
   private String metadata;
   private String portletRole;

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

    public Boolean getUseProperties() {
        return isUseProperties;
    }

    public void setUseProperties(Boolean useProperties) {
        isUseProperties = useProperties;
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

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
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
}
