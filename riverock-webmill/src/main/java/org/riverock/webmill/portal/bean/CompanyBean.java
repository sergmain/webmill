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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.bean.Company;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:16:22
 *         $Id$
 */
@Entity
@Table(name="WM_LIST_COMPANY")
//@Table(name="wm_list_company")
@TableGenerator(
    name="TABLE_LIST_COMPANY",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_list_company",
    allocationSize = 1,
    initialValue = 1
)
public class CompanyBean implements Serializable, Company {
    private static final long serialVersionUID = 1057005508L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_LIST_COMPANY")
    @Column(name="ID_FIRM")
    private Long id = null;

    @Column(name="FULL_NAME")
	private String name = null;

    @Column(name="SHORT_NAME")
	private String shortName = null;

    @Column(name="ADDRESS")
	private String address = null;

    @Column(name="CHIEF")
	private String ceo = null;

    @Column(name="BUH")
	private String cfo = null;

    @Column(name="URL")
	private String website = null;

    @Column(name="SHORT_INFO")
	private String info = null;

    @Column(name="IS_DELETED")
    private boolean isDeleted = false;

    public CompanyBean() {
    }

    public CompanyBean(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.shortName = company.getShortName();
        this.address = company.getAddress();
        this.ceo = company.getCeo();
        this.cfo = company.getCfo();
        this.website = company.getWebsite();
        this.info = company.getInfo();
        isDeleted = company.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getCfo() {
		return cfo;
	}

	public void setCfo(String cfo) {
		this.cfo = cfo;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
