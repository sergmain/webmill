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
import org.riverock.commerce.bean.CurrencyPrecision;
import org.riverock.commerce.dao.CommerceDaoFactory;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 5:27:47 PM
 *
 * $Id: CurrencyPrecisionList.java 1137 2006-12-12 14:50:55Z serg_main $
 */
@SuppressWarnings({"UnusedAssignment"})
public class CurrencyPrecisionList implements Serializable {
    private static Logger log = Logger.getLogger( CurrencyPrecisionList.class );

    private List<CurrencyPrecision> precisionsList;

    public CurrencyPrecisionList(Long shopId) {
        this.precisionsList = CommerceDaoFactory.getCurrencyPrecisionDao().getCurrencyPrecisionList(shopId);
    }

    public List<CurrencyPrecision> getPrecisionsList() {
        if (precisionsList==null) {
            precisionsList = new ArrayList<CurrencyPrecision>();
        }
        return precisionsList;
    }

    public CurrencyPrecision getCurrencyPrecision( Long idCurrency ) {
        if (log.isDebugEnabled()) {
            log.debug("count defined precision - "+this.getPrecisionsList().size());
            log.debug("id_currency - " + idCurrency);
        }

        for (CurrencyPrecision prec : this.getPrecisionsList()) {
            if (prec.getCurrencyId().equals(idCurrency))
                return prec;
        }
        return null;
    }
}
