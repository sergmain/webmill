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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.bean.CurrencyCurs;
import org.riverock.commerce.tools.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 21:53:14
 *         <p/>
 *         $Id$
 */
public class CurrencyDaoImpl implements CurrencyDao {

    /**
     * list of curs for currency not initialized
     *
     * @return List<Currency>
     */
    public List<Currency> getCurrencyList(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<Currency> list = session.createQuery(
            "select currency from org.riverock.commerce.bean.Currency currency " +
                "where currency.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();
        session.getTransaction().commit();
        return list;
    }

    public Long createCurrency(Currency currency) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.save(currency);
        session.getTransaction().commit();
        return currency.getCurrencyId();
    }

    public void updateCurrency(Currency currency) {
        if (currency ==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.update(currency);
        session.getTransaction().commit();
    }

    public void deleteCurrency(Long currencyId) {
        if (currencyId==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.createQuery
            ("delete org.riverock.commerce.bean.CurrencyCurs curs where curs.currencyId=:currencyId ")
            .setLong("currencyId", currencyId)
            .executeUpdate();

        session.createQuery
            ("delete org.riverock.commerce.bean.Currency currency " +
                "where currency.currencyId=:currencyId ")
            .setLong("currencyId", currencyId)
            .executeUpdate();

        session.getTransaction().commit();
    }

    public Currency getCurrency(Long currencyId) {
        if (currencyId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Currency bean = (Currency)session.createQuery(
            "select currency from org.riverock.commerce.bean.Currency currency " +
                "where currency.currencyId=:currencyId ")
            .setLong("currencyId", currencyId)
            .uniqueResult();

        session.getTransaction().commit();
        return bean;
    }

    public Long addCurrencyCurs(Long currencyId, BigDecimal curs) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CurrencyCurs bean = new CurrencyCurs();
        bean.setCurrencyId(currencyId);
        bean.setCurs(curs);
        bean.setDate(new Date());
        session.save(bean);
        session.getTransaction().commit();
        return bean.getCurrencyCursId();
    }

    public List<CurrencyCurs> getCurrencyCurses(Long currencyId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<CurrencyCurs> list = session.createQuery(
            "select currenyCurs from org.riverock.commerce.bean.CurrencyCurs currenyCurs " +
                "where currenyCurs.currencyId=:currencyId " +
                "order by currenyCurs.date desc")
            .setLong("currencyId", currencyId)
            .list();
        session.getTransaction().commit();
        return list;
    }
}
