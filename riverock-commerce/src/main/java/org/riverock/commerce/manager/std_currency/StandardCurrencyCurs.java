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

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * @author Sergei Maslyukov
 *         Date: 30.08.2006
 *         Time: 20:02:15
 *         <p/>
 *         $Id$
 */
public class StandardCurrencyCurs implements Serializable {
    private static final long serialVersionUID = 7767005419L;

    private BigDecimal curs= null;
    private Date created = null;

    public StandardCurrencyCurs() {
    }

    public StandardCurrencyCurs(StandardCurrencyCurs bean) {
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
