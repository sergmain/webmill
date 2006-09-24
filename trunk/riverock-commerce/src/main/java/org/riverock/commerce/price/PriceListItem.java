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

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * $Id$
 */
public class PriceListItem
{
    private static Logger log = Logger.getLogger( PriceListItem.class );

    public Long id_shop = null;
    public int isGroup = 0;
    public Long itemID = null;
    public Long id_group = null;
    public double priceItem = 0;
    public Long id_currency = null;
    public String nameItem = "";
    public String codeCurrency = "";
    public String nameCurrency = "";
    public String isLoad = "YES";
    public int isSpecial = 0;
    public int isManual = 0;

    public int quantity;
    public Long idPK = null;		// Primary key

    protected void finalize() throws Throwable {
        nameItem = null;
        codeCurrency = null;
        nameCurrency = null;
        isLoad = null;

        super.finalize();
    }

    public PriceListItem(){
    }

    public PriceListItem(DatabaseAdapter db_, Long id_shop, Long siteId, Long id_item)
            throws PriceException
    {
        if ( log.isDebugEnabled() )
            log.debug("#5.001");

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql_ =
                "select a.id_item, a.id_shop, a.id, a.id_main, a.item, a.price, " +
                "	a.currency, e.name_currency, a.quantity , e.ID_CURRENCY " +
                "from 	WM_PRICE_LIST a, cash_currency e " +
                "where	e.ID_SITE=? and " +
                "	a.currency = e.currency and a.id_shop = ? and " +
                "	a.absolete = 0 and a.is_group=0 and a.id_item =?";

        try
        {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, siteId);
            RsetTools.setLong(ps, 2, id_shop);
            RsetTools.setLong(ps, 3, id_item);

            rs = ps.executeQuery();

            if (rs.next()) {
                if ( log.isDebugEnabled() )
                    log.debug("#2.0001");

                set(rs);
            }
        }
        catch (Exception e) {
            final String es = "Error create object PriceListItem";
            log.error(es, e);
            throw new PriceException(es, e);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        if ( log.isDebugEnabled() )
            log.debug("#5.002");
    }

//    public PriceListItem(Long currencyID, ResultSet rs) throws PriceException {
//        set( rs );
//    }
//
    public void set(ResultSet rs) throws PriceException {
        if ( log.isDebugEnabled() )
            log.debug("#2.0002");

        try {
            id_shop = RsetTools.getLong(rs, "ID_SHOP");
            isGroup = RsetTools.getInt( rs, "IS_GROUP", 0 );
            itemID = RsetTools.getLong(rs, "ID");
            id_group = RsetTools.getLong(rs, "ID_MAIN");
            nameItem = RsetTools.getString(rs, "ITEM");
            priceItem = RsetTools.getDouble(rs, "PRICE", 0.0);

            if ( log.isDebugEnabled() ) {
                log.debug("RSET priceItem "+ RsetTools.getDouble(rs, "PRICE"));
                log.debug("java priceItem "+ priceItem);
            }

            id_currency = RsetTools.getLong( rs, "ID_CURRENCY");
            codeCurrency = RsetTools.getString(rs, "CURRENCY");
            nameCurrency = RsetTools.getString(rs, "NAME_CURRENCY");
            isLoad = RsetTools.getInt(rs, "ABSOLETE", 0)==1? "YES" : "NO";
            isSpecial = RsetTools.getInt(rs, "IS_SPECIAL", 0);
            isManual = RsetTools.getInt(rs, "IS_MANUAL", 0);
            quantity = RsetTools.getInt(rs, "QUANTITY", 0);
            idPK = RsetTools.getLong(rs, "ID_ITEM");
        }
        catch (Exception e) {
            final String es = "Error init pricelist item data";
            log.error(es, e);
            throw new PriceException(es, e);
        }
    }

    public void print(PrintStream out){
    }

    public String getPlainHTML(){
        return ("PriceItem: ")+
        (" isGroup -> " + Integer.toString(isGroup))+
        (" itemID -> " + itemID)+
        (" id_group -> " + id_group)+
        (" NameItem -> " + nameItem)+
        (" PriceItem -> " + Double.toString(priceItem))+
        (" CodeCurrency -> " + codeCurrency)+
        (" NameCurrency -> " + nameCurrency)+
        (" isLoad -> " + isLoad);
    }
}
