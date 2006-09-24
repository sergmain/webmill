/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.bean;

import java.io.Serializable;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:07:34
 * <p/>
 * $Id$
 */
public class CurrencyPrecisionBean implements Serializable {
    private static final long serialVersionUID = 2625005325L;
    
    private Long currencyPrecisionId;
    private Long currencyId;
    private Long shopId;
    private Integer precision;

    public CurrencyPrecisionBean() {
    }

    public CurrencyPrecisionBean(CurrencyPrecisionBean bean) {
        this.currencyPrecisionId = bean.getCurrencyPrecisionId();
        this.currencyId = bean.getCurrencyId();
        this.shopId = bean.getShopId();
        this.precision = bean.getPrecision();
    }

    public Long getCurrencyPrecisionId() {
        return currencyPrecisionId;
    }

    public void setCurrencyPrecisionId(Long currencyPrecisionId) {
        this.currencyPrecisionId = currencyPrecisionId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }
}
