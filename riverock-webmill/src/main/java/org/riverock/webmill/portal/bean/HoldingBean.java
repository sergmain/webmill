/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.webmill.portal.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:16:22
 *         $Id$
 */
@Entity
@Table(name="wm_list_holding")
@TableGenerator(
    name="TABLE_LIST_HOLDING",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_list_holding",
    allocationSize = 1,
    initialValue = 1
)
public class HoldingBean implements Serializable, Holding {
    private static final long serialVersionUID = 4055005512L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_LIST_HOLDING")
    @Column(name="ID_HOLDING")
    private Long id = null;

    @Column(name="FULL_NAME_HOLDING")
	private String name = null;

    @Column(name="NAME_HOLDING")
	private String shortName = null;

    @Transient
    private List<Long> companyIdList = null;

    public HoldingBean() {
    }

    public HoldingBean(Holding holding) {
        this.id = holding.getId();
        this.name = holding.getName();
        this.shortName = holding.getShortName();
        this.companyIdList = holding.getCompanyIdList();        
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

	public List<Long> getCompanyIdList() {
		return companyIdList;
	}

	public void setCompanyIdList(List<Long> companyIdList) {
		this.companyIdList = companyIdList;
	}
}
