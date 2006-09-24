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
package org.riverock.commerce.price;

import java.util.Map;
import java.util.HashMap;

import org.riverock.portlet.schema.price.CurrencyPrecisionType;

/**
 *
 * $Author$
 *
 * $Id$
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

    public CurrencyPrecisionType precision = null;

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
        precision = null;

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