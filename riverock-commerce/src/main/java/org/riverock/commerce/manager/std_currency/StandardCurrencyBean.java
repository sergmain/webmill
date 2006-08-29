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
package org.riverock.commerce.manager.std_currency;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencyBean {
    private Long standardCurrencyId=null;
    private String standardCurrencyName = null;
    private String standardCurrencyCode = null;
    private boolean isDeleted=false;

    public StandardCurrencyBean() {
    }

    public StandardCurrencyBean(StandardCurrencyBean standardCurrencyBean) {
        this.standardCurrencyId = standardCurrencyBean.getStandardCurrencyId();
        this.standardCurrencyName = standardCurrencyBean.getStandardCurrencyName();
        this.standardCurrencyCode = standardCurrencyBean.getStandardCurrencyCode();
        this.isDeleted = standardCurrencyBean.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getStandardCurrencyId() {
        return standardCurrencyId;
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        this.standardCurrencyId = standardCurrencyId;
    }

    public String getStandardCurrencyName() {
        return standardCurrencyName;
    }

    public void setStandardCurrencyName(String standardCurrencyName) {
        this.standardCurrencyName = standardCurrencyName;
    }

    public String getStandardCurrencyCode() {
        return standardCurrencyCode;
    }

    public void setStandardCurrencyCode(String standardCurrencyCode) {
        this.standardCurrencyCode = standardCurrencyCode;
    }
}
