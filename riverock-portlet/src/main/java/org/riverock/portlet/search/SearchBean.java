/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.search;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

/**
 * @author Sergei Maslyukov
 *         Date: 30.11.2006
 *         Time: 21:05:08
 *         <p/>
 *         $Id$
 */
@Entity
@Table(name="WM_PORTLET_SEARCH")
@TableGenerator(
    name="TABLE_PORTLET_SEARCH",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portlet_webclip",
    allocationSize = 1,
    initialValue = 1
)
public class SearchBean implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTLET_SEARCH")
    @Column(name="ID_SEARCH")
    private Long searchId;

    @Column(name="ID_SITE")
    private Long siteId;

    @Column(name="SEARCH_DATE")
    private Date searchDate;

    @Column(name="WORD", length=50)
    private String word;

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
