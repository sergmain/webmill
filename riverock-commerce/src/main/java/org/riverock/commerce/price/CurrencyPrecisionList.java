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

import org.apache.log4j.Logger;
import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.bean.price.CurrencyPrecisionListType;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.bean.price.CurrencyPrecisionType;

import java.io.Serializable;
import java.util.List;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 5:27:47 PM
 *
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class CurrencyPrecisionList extends CurrencyPrecisionListType implements Serializable {
    private static Logger log = Logger.getLogger( CurrencyPrecisionList.class );

    public void initCurrencyPrecision(Long idShop) {
        List<CurrencyPrecisionBean> list = CommerceDaoFactory.getCurrencyPrecisionDao().getCurrencyPrecisionList(idShop);
        for (CurrencyPrecisionBean bean : list) {
            CurrencyPrecisionType prec = new CurrencyPrecisionType();

            prec.setShopPrecisionId( bean.getCurrencyPrecisionId() );
            prec.setCurrencyId( bean.getCurrencyId());
            prec.setShopId( bean.getShopId() );
            prec.setPrecision( bean.getPrecision() );

            this.getPrecisionsList().add( prec );
        }
    }

    public CurrencyPrecisionType getCurrencyPrecision( Long idCurrency ) {
        if (log.isDebugEnabled()) {
            log.debug("count defined precision - "+this.getPrecisionsList().size());
            log.debug("id_currency - " + idCurrency);
        }

        for (CurrencyPrecisionType prec : this.getPrecisionsList()) {
            if (prec.getCurrencyId().equals(idCurrency))
                return prec;
        }
        return null;
    }
}
