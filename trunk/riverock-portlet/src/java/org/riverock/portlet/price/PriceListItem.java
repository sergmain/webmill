/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 * $Id$
 */
package org.riverock.portlet.price;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.site.SiteListSite;

import org.apache.log4j.Logger;

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

    public PriceListItem(DatabaseAdapter db_, Long id_shop, String serverName, Long id_item)
            throws PriceException
    {
        if ( log.isDebugEnabled() )
            log.debug("#5.001");

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql_ =
                "select a.id_item, a.id_shop, a.id, a.id_main, a.item, a.price, " +
                "	a.currency, e.name_currency, a.quantity , e.ID_CURRENCY " +
                "from 	PRICE_LIST a, cash_currency e " +
                "where	e.ID_SITE=? and " +
                "	a.currency = e.currency and a.id_shop = ? and " +
                "	a.absolete = 0 and a.is_group=0 and a.id_item =?";

        try
        {
            Long idSite = SiteListSite.getIdSite(serverName);

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSite);
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
            isGroup = RsetTools.getInt(rs, "IS_GROUP", new Integer(0)).intValue();
            itemID = RsetTools.getLong(rs, "ID");
            id_group = RsetTools.getLong(rs, "ID_MAIN");
            nameItem = RsetTools.getString(rs, "ITEM");
            priceItem = RsetTools.getDouble(rs, "PRICE", new Double(0)).doubleValue();

            if ( log.isDebugEnabled() ) {
                log.debug("RSET priceItem "+ RsetTools.getDouble(rs, "PRICE"));
                log.debug("java priceItem "+ priceItem);
            }

            id_currency = RsetTools.getLong( rs, "ID_CURRENCY");
            codeCurrency = RsetTools.getString(rs, "CURRENCY");
            nameCurrency = RsetTools.getString(rs, "NAME_CURRENCY");
            isLoad = new Integer(1).equals(RsetTools.getInt(rs, "ABSOLETE"))? "YES" : "NO";
            isSpecial = RsetTools.getInt(rs, "IS_SPECIAL", new Integer(0)).intValue();
            isManual = RsetTools.getInt(rs, "IS_MANUAL", new Integer(0)).intValue();

            quantity = RsetTools.getInt(rs, "QUANTITY", new Integer(0)).intValue();
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
