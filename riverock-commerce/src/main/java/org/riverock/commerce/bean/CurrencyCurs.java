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
import javax.validation.constraints.NotNull;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 19:12:14
 *         <p/>
 *         $Id$
 */
@Entity
@Table(name="wm_cash_curr_value")
@TableGenerator(
    name="TABLE_CASH_CURR_VALUE",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_cash_curr_value",
    allocationSize = 1,
    initialValue = 1
)
public class CurrencyCurs implements Serializable {
    private static final long serialVersionUID = 2625005437L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_CASH_CURR_VALUE")
    @Column(name="ID_CURVAL")
    private Long currencyCursId;

    @Column(name="ID_CURRENCY")
    private Long currencyId;
          
    @Column(name="DATE_CHANGE")
    private Date date;

    @Column(name="CURS")
    @NotNull
    private BigDecimal curs;

    public Long getCurrencyCursId() {
        return currencyCursId;
    }

    public void setCurrencyCursId(Long currencyCursId) {
        this.currencyCursId = currencyCursId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getCurs() {
        return curs;
    }

    public void setCurs(BigDecimal curs) {
        this.curs = curs;
    }
}
