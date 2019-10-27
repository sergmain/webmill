/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * @author Sergei Maslyukov
 *         Date: 30.08.2006
 *         Time: 20:02:15
 *         <p/>
 *         $Id$
 */
@Entity
@Table(name = "wm_cash_curs_std")
@TableGenerator(
    name = "TABLE_CASH_CURS_STD",
    table = "wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_cash_curs_std",
    allocationSize = 1,
    initialValue = 1
)
public class StandardCurrencyCurs implements Serializable {
    private static final long serialVersionUID = 7767005419L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_CASH_CURS_STD")
    @Column(name = "ID_STD_CURS")
    private Long standardCurrencyCursId;

    @Column(name = "ID_STD_CURR")
    private Long standardCurrencyId;

    @Column(name = "VALUE_CURS")
    private BigDecimal curs;

    @Column(name = "DATE_CHANGE")
    private Date created;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    public Long getStandardCurrencyId() {
        return standardCurrencyId;
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        this.standardCurrencyId = standardCurrencyId;
    }

    public Long getStandardCurrencyCursId() {
        return standardCurrencyCursId;
    }

    public void setStandardCurrencyCursId(Long standardCurrencyCursId) {
        this.standardCurrencyCursId = standardCurrencyCursId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public BigDecimal getCurs() {
        return curs;
    }

    public void setCurs(BigDecimal curs) {
        this.curs = curs;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
