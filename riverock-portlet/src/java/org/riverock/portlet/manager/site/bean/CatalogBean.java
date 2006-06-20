/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.manager.site.bean;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.common.TreeItem;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 17:42:16
 */
public class CatalogBean implements Serializable, CatalogItem {
    private static final long serialVersionUID = 2057005507L;

    private Long catalogId;
    private Long topCatalogId = 0L;
    private Long portletId;
    private Long contextId;
    private Boolean isUseProperties = false;
    private Long templateId;
    private Long catalogLanguageId;
    private Integer orderField;
    private String storage;
    private String keyMessage;
    private String url;
    private String title;
    private String author;
    private String keyword;
    private String metadata;
    private String portletRole;
    private List<CatalogItem> subCatalogItemList = null;

    public CatalogBean(CatalogItem catalogItem) {
        this.catalogId=catalogItem.getCatalogId();
        this.topCatalogId=catalogItem.getTopCatalogId();
        this.portletId=catalogItem.getPortletId();
        this.contextId=catalogItem.getContextId();
        this.isUseProperties=catalogItem.getUseProperties();
        this.templateId=catalogItem.getTemplateId();
        this.catalogLanguageId=catalogItem.getCatalogLanguageId();
        this.orderField=catalogItem.getOrderField();
        this.storage=catalogItem.getStorage();
        this.keyMessage=catalogItem.getKeyMessage();
        this.url=catalogItem.getUrl();
        this.title=catalogItem.getTitle();
        this.author=catalogItem.getAuthor();
        this.keyword=catalogItem.getKeyword();
        this.metadata=catalogItem.getMetadata();
        this.portletRole=catalogItem.getPortletRole();
        this.subCatalogItemList=initCatalogList(catalogItem.getSubCatalogItemList());
    }

    private List<CatalogItem> initCatalogList(List<CatalogItem> subCatalogItemList) {
        if (subCatalogItemList==null) {
            return null;
        }

        List<CatalogItem> items = new ArrayList<CatalogItem>();
        for (CatalogItem item : subCatalogItemList) {
            items.add( new CatalogBean(item) );
        }
        return items;
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
        this.storage = FacesTools.convertParameter(storage);
    }

    public String getKeyMessage() {
        return keyMessage;
    }

    public void setKeyMessage(String keyMessage) {
        this.keyMessage = FacesTools.convertParameter(keyMessage);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = FacesTools.convertParameter(url);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = FacesTools.convertParameter(title);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = FacesTools.convertParameter(author);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = FacesTools.convertParameter(keyword);
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = FacesTools.convertParameter(metadata);
    }

    public String getPortletRole() {
        return portletRole;
    }

    public void setPortletRole(String portletRole) {
        this.portletRole = FacesTools.convertParameter(portletRole);
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

