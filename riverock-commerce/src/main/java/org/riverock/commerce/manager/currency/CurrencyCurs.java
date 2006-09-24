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
package org.riverock.commerce.manager.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:41:42
 *         <p/>
 *         $Id$
 */
public class CurrencyCurs implements Serializable {
    private static final long serialVersionUID = 55957005419L;

    private BigDecimal curs= null;
    private Date created = null;

    public CurrencyCurs() {
    }

    public CurrencyCurs(CurrencyCurs bean) {
        this.curs = bean.getCurs();
        this.created = bean.getCreated();
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
