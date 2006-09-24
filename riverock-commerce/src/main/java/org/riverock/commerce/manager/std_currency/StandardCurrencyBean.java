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
import java.util.List;
import java.util.ArrayList;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencyBean implements Serializable {

    private static final long serialVersionUID = 7767005501L;

    private Long standardCurrencyId=null;
    private String standardCurrencyName = null;
    private String standardCurrencyCode = null;
    private boolean isDeleted=false;
    private List<StandardCurrencyCurs> curses = new ArrayList<StandardCurrencyCurs>();

    public StandardCurrencyBean() {
    }

    public StandardCurrencyBean(StandardCurrencyBean standardCurrencyBean) {
        this.standardCurrencyId = standardCurrencyBean.getStandardCurrencyId();
        this.standardCurrencyName = standardCurrencyBean.getStandardCurrencyName();
        this.standardCurrencyCode = standardCurrencyBean.getStandardCurrencyCode();
        this.isDeleted = standardCurrencyBean.isDeleted();
        this.curses = standardCurrencyBean.getCurses();
    }

    public List<StandardCurrencyCurs> getCurses() {
        return curses;
    }

    public void setCurses(List<StandardCurrencyCurs> curses) {
        this.curses = curses;
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
