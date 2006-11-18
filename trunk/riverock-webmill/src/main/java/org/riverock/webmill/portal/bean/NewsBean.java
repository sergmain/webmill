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

import org.riverock.interfaces.portal.bean.News;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.sql.Blob;

/**
 * User: SergeMaslyukov
 * Date: 19.11.2006
 * Time: 1:54:06
 * <p/>
 * $Id$
 */
@Entity
@Table(name="wm_news_item")
@TableGenerator(
    name="TABLE_NEWS_ITEM",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_news_item",
    allocationSize = 1,
    initialValue = 1
)
public class NewsBean implements News, Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_NEWS_ITEM")
    @Column(name="ID")
    private Long newsId;

    @Column(name="ID_NEWS")
    private Long newsGroupId;

    @Column(name="EDATE")
    private Date postDate;

    @Column(name="HEADER")
    private String newsHeader;

    @Column(name="ANONS")
    private String newsAnons;

    @Column(name="NEWS_BLOB")
    private Blob newsBlob;

    @Column(name="IS_PLAIN")
    private boolean isPlain;

    @Column(name="IS_DELETED")
    private boolean isDeleted;

    @Transient
    private String newsText;

    public Blob getNewsBlob() {
        return newsBlob;
    }

    public void setNewsBlob(Blob newsBlob) {
        this.newsBlob = newsBlob;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getNewsGroupId() {
        return newsGroupId;
    }

    public void setNewsGroupId(Long newsGroupId) {
        this.newsGroupId = newsGroupId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        if (postDate==null) {
            this.postDate=null;
            return;
        }
        this.postDate = new Date(postDate.getTime());
    }

    public String getNewsHeader() {
        return newsHeader;
    }

    public void setNewsHeader(String newsHeader) {
        this.newsHeader = newsHeader;
    }

    public String getNewsAnons() {
        return newsAnons;
    }

    public void setNewsAnons(String newsAnons) {
        this.newsAnons = newsAnons;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public boolean isPlain() {
        return isPlain;
    }

    public void setPlain(boolean plain) {
        isPlain = plain;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
