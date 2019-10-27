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

import org.riverock.interfaces.portal.bean.NewsGroup;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: SergeMaslyukov
 * Date: 19.11.2006
 * Time: 2:32:37
 * <p/>
 * $Id$
 */
@javax.persistence.Entity
@Table(name="WM_NEWS_LIST")
@TableGenerator(
    name="TABLE_NEWS_LIST",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_news_list",
    allocationSize = 1,
    initialValue = 1
)
public class NewsGroupBean implements NewsGroup, Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_NEWS_LIST")
    @Column(name="ID_NEWS")
    private Long newsGroupId;

    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
    private Long siteLanguageId;

    @Column(name="NAME_NEWS")
    private String newsGroupName;

    @Column(name="CODE_NEWS_GROUP")
    private String newsGroupCode;

    @Column(name="COUNT_NEWS")
    private int countNewsPerGroup;

    @Column(name="ORDER_FIELD")
    private Integer orderField;

    @Column(name="IS_DELETED")
    private boolean isDeleted;

    public NewsGroupBean() {
    }

    public NewsGroupBean(NewsGroup newsGroup) {
        this.newsGroupId = newsGroup.getNewsGroupId();
        this.siteLanguageId = newsGroup.getSiteLanguageId();
        this.newsGroupName = newsGroup.getNewsGroupName();
        this.newsGroupCode = newsGroup.getNewsGroupCode();
        this.countNewsPerGroup = newsGroup.getCountNewsPerGroup();
        this.orderField = newsGroup.getOrderField();
        isDeleted = newsGroup.isDeleted();
    }

    public Long getNewsGroupId() {
        return newsGroupId;
    }

    public void setNewsGroupId(Long newsGroupId) {
        this.newsGroupId = newsGroupId;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public String getNewsGroupName() {
        return newsGroupName;
    }

    public void setNewsGroupName(String newsGroupName) {
        this.newsGroupName = newsGroupName;
    }

    public String getNewsGroupCode() {
        return newsGroupCode;
    }

    public void setNewsGroupCode(String newsGroupCode) {
        this.newsGroupCode = newsGroupCode;
    }

    public int getCountNewsPerGroup() {
        return countNewsPerGroup;
    }

    public void setCountNewsPerGroup(int countNewsPerGroup) {
        this.countNewsPerGroup = countNewsPerGroup;
    }

    public Integer getOrderField() {
        return orderField;
    }

    public void setOrderField(Integer orderField) {
        this.orderField = orderField;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
