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
package org.riverock.commerce.price;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * $Author: serg_main $
 *
 * $Id: ShopPageParam.java 1137 2006-12-12 14:50:55Z serg_main $
 *
 */
public class ShopPageParam {
    public Long id_shop = null;
    public Long id_group = null;
    private String serverName = null;
    public Long idSite = null;
    public Long id_currency;
    public Map<String, String> currencyURL = new HashMap<String, String>();
    public boolean  isProcessInvoice =  false;

    public String sortBy = null;
    public int sortDirect = 0;

    protected void finalize() throws Throwable {
        id_shop = null;
        id_group = null;
        serverName = null;
        if (currencyURL!=null){
            currencyURL.clear();
        }
        currencyURL = null;

        super.finalize();
    }

    public ShopPageParam() {
    }

    public void setServerName( String serverName, Long siteId ) {
        this.serverName = serverName;
        this.idSite = siteId;
    }

    public String getServerName() {
        return serverName;
    }
}