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
package org.riverock.portlet.price;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.schema.price.CurrencyCurrentCursType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.portlet.schema.price.StandardCurrencyItemType;
import org.riverock.portlet.schema.price.StandardCurrencyType;
import org.riverock.portlet.schema.core.WmCashCurrencyItemType;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 9:01:05 PM
 *
 * $Id$
 */
public class CurrencyItem extends CustomCurrencyItemType {
//    private static Log cat = LogFactory.getLog("org.riverock.portlet.price.CurrencyItem");

    public void fillRealCurrencyData(StandardCurrencyType stdCurrency)
            throws Exception
    {
            if (!Boolean.TRUE.equals(this.getIsUseStandardCurrency()) &&
                this.getCurrentCurs()==null)
            {
                this.setRealCurs( 0.0 );
                this.setRealDateChange( DateTools.getCurrentTime() );
                this.setIsRealInit( Boolean.FALSE );
                return;
            }

            if (Boolean.TRUE.equals(this.getIsUseStandardCurrency()) &&
                    (stdCurrency ==null ||
                    stdCurrency.getStandardCurrencyListCount()==0))
                throw new Exception("Curs for currency "+this.getCurrencyName()+
                        " can not calculated. Curs for standard currency not entered");

            CurrencyCurrentCursType curs = null;
            if (Boolean.TRUE.equals(this.getIsUseStandardCurrency()) )
            {
                for (int k=0; k<stdCurrency.getStandardCurrencyListCount(); k++)
                {
                    StandardCurrencyItemType stdItem = stdCurrency.getStandardCurrencyList(k);
                    if (stdItem.getIdStandardCurrency().equals(this.getIdStandardCurrency()) )
                    {
                        curs = stdItem.getCurrentCurs();
                        break;
                    }
                }
                if (curs == null)
                    throw new Exception("Error get curs for standard currency. Local currency - "+this.getCurrencyName());
            }
            else
                curs = this.getCurrentCurs();

            this.setRealCurs( curs.getCurs() );
            this.setRealDateChange( curs.getDateChange() );
            this.setIsRealInit( Boolean.TRUE );
    }

    public CurrencyItem()
    {
    }

    public CurrencyItem(DatabaseAdapter db_, WmCashCurrencyItemType item)
        throws PriceException
    {
        this.setCurrencyCode(item.getCurrency());
        this.setCurrencyName(item.getNameCurrency());
        this.setIdCurrency(item.getIdCurrency());
        this.setIdSite(item.getIdSite());
        this.setIdStandardCurrency(item.getIdStandartCurs());
        this.setIsUsed(item.getIsUsed());
        this.setIsUseStandardCurrency(item.getIsUseStandart());
        this.setPercent(item.getPercentValue());
        this.setCurrentCurs(
            CurrencyService.getCurrentCurs(db_, this.getIdCurrency(), this.getIdSite())
        );
    }

    public void set(DatabaseAdapter db_, ResultSet rs)
        throws SQLException, PriceException
    {
        this.setCurrencyCode(RsetTools.getString(rs, "CURRENCY"));
        this.setCurrencyName(RsetTools.getString(rs, "NAME_CURRENCY"));
        this.setIdCurrency(RsetTools.getLong(rs, "ID_CURRENCY"));
        this.setIdSite(RsetTools.getLong(rs, "ID_SITE"));
        this.setIdStandardCurrency(RsetTools.getLong(rs, "ID_STANDART_CURS"));
        this.setIsUsed( RsetTools.getInt( rs, "IS_USED", 0 )==1 );
        this.setIsUseStandardCurrency( RsetTools.getInt( rs, "IS_USE_STANDART", 0 ) == 1 );
        this.setPercent(RsetTools.getDouble(rs, "PERCENT_VALUE"));
        this.setCurrentCurs(
            CurrencyService.getCurrentCurs(db_, this.getIdCurrency(), this.getIdSite())
        );
    }
}
