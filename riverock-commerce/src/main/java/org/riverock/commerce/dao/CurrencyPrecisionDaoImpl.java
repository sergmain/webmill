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
package org.riverock.commerce.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.commerce.bean.CurrencyPrecision;
import org.riverock.commerce.tools.HibernateUtils;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:14:40
 * <p/>
 * $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class CurrencyPrecisionDaoImpl implements CurrencyPrecisionDao {
    private static Logger log = Logger.getLogger( CurrencyPrecisionDaoImpl.class );

    public CurrencyPrecision getCurrencyPrecision(Long currencyPrecisionId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CurrencyPrecision item = (CurrencyPrecision)session.createQuery(
            "select currencyPrec from org.riverock.commerce.bean.CurrencyPrecision currencyPrec " +
                "where currencyPrec.currencyPrecisionId=:currencyPrecisionId")
            .setLong("currencyPrecisionId", currencyPrecisionId)
            .uniqueResult();
        session.getTransaction().commit();
        return item;
    }

    public List<CurrencyPrecision> getCurrencyPrecisionList(Long shopId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List item = session.createQuery(
            "select currencyPrec from org.riverock.commerce.bean.CurrencyPrecision currencyPrec " +
                "where currencyPrec.shopId=:shopId")
            .setLong("shopId", shopId)
            .list();
        session.getTransaction().commit();
        return item;
    }

    public void updateCurrencyPrecision(Long currencyPrecisionId, Integer currencyPrecision) {
        if (currencyPrecisionId==null || currencyPrecision==null) {
            log.info("currencyPrecisionId: "+currencyPrecisionId+", currencyPrecision: " + currencyPrecision);
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CurrencyPrecision item = (CurrencyPrecision)session.createQuery(
            "select currencyPrec from org.riverock.commerce.bean.CurrencyPrecision currencyPrec " +
                "where currencyPrec.currencyPrecisionId=:currencyPrecisionId")
            .setLong("currencyPrecisionId", currencyPrecisionId)
            .uniqueResult();

        if (item!=null) {
            item.setPrecision(currencyPrecision);
            session.update(item);
        }
        session.getTransaction().commit();
    }
}
