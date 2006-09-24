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

import org.apache.log4j.Logger;
import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.schema.price.CurrencyPrecisionListType;
import org.riverock.portlet.schema.price.CurrencyPrecisionType;

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

    public void initCurrencyPrecision(DatabaseAdapter db_, Long idShop) {
        List<CurrencyPrecisionBean> list = CommerceDaoFactory.getCurrencyPrecisionDao().getCurrencyPrecisionList(idShop);
        for (CurrencyPrecisionBean bean : list) {
            CurrencyPrecisionType prec = new CurrencyPrecisionType();

            prec.setIdShopPrecision( bean.getCurrencyPrecisionId() );
            prec.setIdCurrency( bean.getCurrencyId());
            prec.setIdShop( bean.getShopId() );
            prec.setPrecision( bean.getPrecision() );

            this.addPrecisions( prec );
        }
/*
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select ID_PRICE_SHOP_PRECISION,ID_CURRENCY,ID_SHOP, PRECISION_SHOP " +
                "from   WM_PRICE_SHOP_PRECISION " +
                "where  ID_SHOP=?";

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idShop);
            rs = ps.executeQuery();

            while (rs.next()) {
                CurrencyPrecisionType prec = new CurrencyPrecisionType();

                prec.setIdShopPrecision( RsetTools.getLong(rs, "ID_PRICE_SHOP_PRECISION"));
                prec.setIdCurrency(RsetTools.getLong(rs, "ID_CURRENCY"));
                prec.setIdShop(RsetTools.getLong(rs, "ID_SHOP"));
                prec.setPrecision(RsetTools.getInt(rs, "PRECISION_SHOP"));

                this.addPrecisions( prec );
            }
        }
        catch (Exception e) {
            String es = "Error get precision currency list";
            log.error(es, e);
            throw new IllegalStateException(e);
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
*/
    }

    public CurrencyPrecisionType getCurrencyPrecision( Long idCurrency ) {
        if (log.isDebugEnabled()) {
            log.debug("count defined precision - "+this.getPrecisionsCount());
            log.debug("id_currency - " + idCurrency);
        }

        for(int i=0; i<this.getPrecisionsCount(); i++) {
            CurrencyPrecisionType prec = this.getPrecisions(i);
            if (prec.getIdCurrency().equals(idCurrency))
                return prec;
        }
        return null;
    }
}
