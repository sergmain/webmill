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
package org.riverock.webmill.portal.bean;

import java.util.List;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.common.TreeItem;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 15:52:55
 *         $Id: CatalogBean.java 1337 2007-08-24 16:00:37Z serg_main $
 */
@Entity
@Table(name="WM_PORTAL_CATALOG")
@TableGenerator(
    name="TABLE_PORTAL_CATALOG",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_catalog",
    allocationSize = 1,
    initialValue = 1
)
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class CatalogBean implements Serializable, CatalogItem {
    private static final long serialVersionUID = 1057005506L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTAL_CATALOG")
    @Column(name="ID_SITE_CTX_CATALOG")
    private Long catalogId;

    @Column(name="ID_TOP_CTX_CATALOG")
    private Long topCatalogId = 0L;

    @Column(name="ID_SITE_CTX_TYPE")
    private Long portletId;

    @Column(name="ID_CONTEXT")
    private Long contextId;

    @Column(name="ID_SITE_TEMPLATE")
    private Long templateId;

    @Column(name="ID_SITE_CTX_LANG_CATALOG")
    private Long catalogLanguageId;

    @Column(name="ORDER_FIELD")
    private Integer orderField;

    @Column(name="KEY_MESSAGE")
    private String keyMessage;

    @Column(name="CTX_PAGE_URL")
    private String url;

    @Column(name="CTX_PAGE_TITLE")
    private String title;

    @Column(name="CTX_PAGE_AUTHOR")
    private String author;

    @Column(name="CTX_PAGE_KEYWORD")
    private String keyword;

    @Column(name="METADATA")
    private String metadata;

    @Column(name="PORTLET_ROLE")
    private String portletRole;

    @Column(name="IS_INCLUDE_IN_SITEMAP")
    private boolean isIncludeInSitemap;

    @Transient
    private List<CatalogItem> subCatalogItemList = null;

    public CatalogBean() {
    }

    public CatalogBean(CatalogItem catalogItem) {
        this.catalogId = catalogItem.getCatalogId();
        this.topCatalogId = catalogItem.getTopCatalogId();
        this.portletId = catalogItem.getPortletId();
        this.contextId = catalogItem.getContextId();
        this.templateId = catalogItem.getTemplateId();
        this.catalogLanguageId = catalogItem.getCatalogLanguageId();
        this.orderField = catalogItem.getOrderField();
        this.keyMessage = catalogItem.getKeyMessage();
        this.url = catalogItem.getUrl();
        this.title = catalogItem.getTitle();
        this.author = catalogItem.getAuthor();
        this.keyword = catalogItem.getKeyword();
        this.metadata = catalogItem.getMetadata();
        this.portletRole = catalogItem.getPortletRole();
        this.subCatalogItemList = catalogItem.getSubCatalogItemList();
        this.isIncludeInSitemap = catalogItem.isIncludeInSitemap();
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
        return this.getTopCatalogId();
    }

    public Long getId() {
        return this.getCatalogId();
    }

    public List<TreeItem> getSubTree() {
        return (List)this.getSubCatalogItemList();
    }

    public void setSubTree(List<TreeItem> list) {
        this.setSubCatalogItemList((List)list);
    }
}
