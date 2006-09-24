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
package org.riverock.commerce.manager.std_currency;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencySessionBean implements Serializable {
    private static final long serialVersionUID = 7767005504L;

    private StandardCurrencyBean standardCurrencyBean = null;
    private Long currentStandardCurrencyId = null;
    private BigDecimal currentCurs =null;

    public StandardCurrencySessionBean() {
    }

    public BigDecimal getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(BigDecimal currentCurs) {
        this.currentCurs = currentCurs;
    }

    public StandardCurrencyBean getStandardCurrencyBean() {
        return standardCurrencyBean;
    }

    public void setStandardCurrencyBean(StandardCurrencyBean standardCurrencyBean) {
        this.standardCurrencyBean = standardCurrencyBean;
    }

    public Long getCurrentStandardCurrencyId() {
        return currentStandardCurrencyId;
    }

    public void setCurrentStandardCurrencyId(Long currentStandardCurrencyId) {
        this.currentStandardCurrencyId = currentStandardCurrencyId;
    }
}

