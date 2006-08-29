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

import java.io.Serializable;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencySessionBean  implements Serializable {
    private static final long serialVersionUID = 7767005504L;

    private StandardCurrencyBean standardCurrencyBean = null;
    private Long currentstandardCurrencyId = null;

    public StandardCurrencySessionBean() {
    }

    public StandardCurrencyBean getStandardCurrencyBean() {
        return standardCurrencyBean;
    }

    public void setStandardCurrencyBean(StandardCurrencyBean standardCurrencyBean) {
        this.standardCurrencyBean = standardCurrencyBean;
    }

    public Long getCurrentstandardCurrencyId() {
        return currentstandardCurrencyId;
    }

    public void setCurrentstandardCurrencyId(Long currentstandardCurrencyId) {
        this.currentstandardCurrencyId = currentstandardCurrencyId;
    }
}

