/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 18:41:20
 */
public class CatalogItemBean implements Serializable {
    private static final long serialVersionUID = 1057005506L;

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
    private List<CatalogItemBean> subCatalogItemList = null;

    public CatalogItemBean(Long portletId, Long templateId, Long catalogLanguageId, Integer orderField, String keyMessage, String url) {
        this.portletId = portletId;
        this.templateId = templateId;
        this.catalogLanguageId = catalogLanguageId;
        this.orderField = orderField;
        this.keyMessage = keyMessage;
        this.url = url;
    }

    public CatalogItemBean(CatalogItemBean catalogItemBean) {
        this.catalogId = catalogItemBean.getCatalogId();
        this.topCatalogId = catalogItemBean.getTopCatalogId();
        this.portletId = catalogItemBean.getPortletId();
        this.contextId = catalogItemBean.getContextId();
        this.templateId = catalogItemBean.getTemplateId();
        this.catalogLanguageId =catalogItemBean.getCatalogLanguageId();
        this.orderField = catalogItemBean.getOrderField();
        this.keyMessage = catalogItemBean.getKeyMessage();
        this.url = catalogItemBean.getUrl();
        this.title = catalogItemBean.getTitle();
        this.author = catalogItemBean.getAuthor();
        this.keyword = catalogItemBean.getKeyword();
        this.metadata = catalogItemBean.getMetadata();
        this.portletRole = catalogItemBean.getPortletRole();
        this.subCatalogItemList = catalogItemBean.getSubCatalogItemList();
    }

    public List<CatalogItemBean> getSubCatalogItemList() {
        return subCatalogItemList;
    }

    public void setSubCatalogItemList(List<CatalogItemBean> subCatalogItemList) {
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
}
