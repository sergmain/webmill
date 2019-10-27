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

import org.riverock.commerce.bean.StandardCurrency;
import org.riverock.commerce.bean.StandardCurrencyCurs;
import org.riverock.commerce.tools.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:45:52
 */
public class StandardCurrencyDaoImpl implements StandardCurrencyDao {

    /**
     * list of curs for currency not initialized
     *
     * @return List<StandardCurrency>
     */
    public List<StandardCurrency> getStandardCurrencyList() {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<StandardCurrency> list = session.createQuery(
            "select stdCurr from org.riverock.commerce.bean.StandardCurrency stdCurr ")
            .list();
        session.getTransaction().commit();
        return list;
    }

    public Long createStandardCurrency(StandardCurrency standardCurrency) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.save(standardCurrency);
        session.getTransaction().commit();
        return standardCurrency.getStandardCurrencyId();
    }

    public void updateStandardCurrency(StandardCurrency standardCurrency) {
        if (standardCurrency==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.update(standardCurrency);
        session.getTransaction().commit();
    }

    public void deleteStandardCurrency(Long standardCurrencyId) {
        if (standardCurrencyId==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.createQuery
            ("delete org.riverock.commerce.bean.StandardCurrencyCurs stdCurs where stdCurs.standardCurrencyId=:standardCurrencyId ")
            .setLong("standardCurrencyId", standardCurrencyId)
            .executeUpdate();

        session.createQuery
            ("delete org.riverock.commerce.bean.StandardCurrencyCurs stdCurrency " +
                "where stdCurrency.standardCurrencyId=:standardCurrencyId ")
            .setLong("standardCurrencyId", standardCurrencyId)
            .executeUpdate();

        session.getTransaction().commit();
    }

    public StandardCurrency getStandardCurrency(Long standardCurrencyId) {
        if (standardCurrencyId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        StandardCurrency bean = (StandardCurrency)session.createQuery(
            "select stdCurr from org.riverock.commerce.bean.StandardCurrency stdCurr " +
                "where stdCurr.standardCurrencyId=:standardCurrencyId ")
            .setLong("standardCurrencyId", standardCurrencyId)
            .uniqueResult();

        session.getTransaction().commit();
        return bean;
    }
    
    public void addStandardCurrencyCurs(Long standardCurrencyId, BigDecimal curs) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        StandardCurrencyCurs bean = new StandardCurrencyCurs();
        bean.setCreated( new Date() );
        bean.setCurs(curs);
        bean.setDeleted(false);
        bean.setStandardCurrencyId(standardCurrencyId);
        session.save(bean);
        session.getTransaction().commit();
    }

    public List<StandardCurrencyCurs> getStandardCurrencyCurses(Long standardCurrencyId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<StandardCurrencyCurs> list = session.createQuery(
            "select stdCurrCurs from org.riverock.commerce.bean.StandardCurrencyCurs stdCurrCurs " +
                "where stdCurrCurs.standardCurrencyId=:standardCurrencyId " +
                "order by stdCurrCurs.created desc")
            .setLong("standardCurrencyId", standardCurrencyId)
            .list();
        session.getTransaction().commit();
        return list;
    }
}
