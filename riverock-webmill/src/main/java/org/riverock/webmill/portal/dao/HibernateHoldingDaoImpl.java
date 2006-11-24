/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.hibernate.Session;

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.HoldingBean;
import org.riverock.webmill.portal.bean.HoldingCompanyRelationBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 10.11.2006
 *         Time: 21:52:09
 *         <p/>
 *         $Id$
 */
public class HibernateHoldingDaoImpl implements InternalHoldingDao {

    public Holding loadHolding( Long holdingId, AuthSession authSession ) {
        if( holdingId == null || authSession==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        HoldingBean bean = (HoldingBean)session.createQuery(
            "select holding from org.riverock.webmill.portal.bean.HoldingBean as holding " +
            "where  holding.id=:holdingId and holding.id in (:holdingIds)")
            .setParameterList("holdingIds", authSession.getGrantedHoldingIdList())
            .setLong("holdingId", holdingId)
            .uniqueResult();

        if (bean!=null) {
            bean.setCompanyIdList(
                session.createQuery(
                    "select relate.companyId " +
                        "from  org.riverock.webmill.portal.bean.HoldingCompanyRelationBean as relate " +
                        "where relate.holdingId=:holdingId")
                    .setLong("holdingId", holdingId)
                    .list()
            );
        }
        session.getTransaction().commit();
        return bean;
    }

    public List<Holding> getHoldingList( AuthSession authSession ) {
        if (authSession==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<HoldingBean> bean = session.createQuery(
            "select holding from org.riverock.webmill.portal.bean.HoldingBean as holding " +
            "where  holding.id in (:holdingIds)")
            .setParameterList("holdingIds", authSession.getGrantedHoldingIdList())
            .list();
        for (HoldingBean holdingBean : bean) {
            holdingBean.setCompanyIdList(
                session.createQuery(
                    "select relate.companyId " +
                        "from  org.riverock.webmill.portal.bean.HoldingCompanyRelationBean as relate " +
                        "where relate.holdingId=:holdingId")
                    .setLong("holdingId", holdingBean.getId())
                    .list()
            );
        }
        session.getTransaction().commit();
        return (List)bean;
    }

    public Long processAddHolding( Holding holdingBean, AuthSession authSession ) {
        if (authSession==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        HoldingBean bean = new HoldingBean(holdingBean);
        session.save(bean);

        if (authSession!=null && holdingBean.getCompanyIdList()!=null) {
            for (Long companyId : holdingBean.getCompanyIdList()) {
                HoldingCompanyRelationBean relate = new HoldingCompanyRelationBean();
                relate.setCompanyId(companyId);
                relate.setHoldingId(bean.getId());
                session.save(relate);
            }
        }
        session.flush();
        session.getTransaction().commit();
        return bean.getId();
    }

    public void processSaveHolding( Holding holdingBean, AuthSession authSession ) {
        if (authSession==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        HoldingBean bean = (HoldingBean)session.createQuery(
            "select holding from org.riverock.webmill.portal.bean.HoldingBean as holding " +
            "where  holding.id=:holdingId and holding.id in (:holdingIds)")
            .setParameterList("holdingIds", authSession.getGrantedHoldingIdList())
            .setLong("holdingId", holdingBean.getId())
            .uniqueResult();

        if (bean==null) {
            session.getTransaction().commit();
            return;
        }

        bean.setName(holdingBean.getName());
        bean.setShortName(holdingBean.getShortName());

        List<HoldingCompanyRelationBean> relate = session.createQuery(
            "select relate " +
                "from  org.riverock.webmill.portal.bean.HoldingCompanyRelationBean as relate " +
                "where relate.holdingId=:holdingId")
            .setLong("holdingId", holdingBean.getId())
            .list();

        for (HoldingCompanyRelationBean holdingCompanyRelationBean : relate) {
            session.delete(holdingCompanyRelationBean);
        }

        if (holdingBean.getCompanyIdList()!=null) {
            for (Long companyId : holdingBean.getCompanyIdList()) {
                HoldingCompanyRelationBean relateBean = new HoldingCompanyRelationBean();
                relateBean.setCompanyId(companyId);
                relateBean.setHoldingId(bean.getId());
                session.save(relateBean);
            }
        }
        session.getTransaction().commit();
    }

    public void processDeleteHolding( Holding holdingBean, AuthSession authSession ) {
        if (authSession==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        HoldingBean bean = (HoldingBean)session.createQuery(
            "select holding from org.riverock.webmill.portal.bean.HoldingBean as holding " +
            "where  holding.id=:holdingId and holding.id in (:holdingIds)")
            .setParameterList("holdingIds", authSession.getGrantedHoldingIdList())
            .setLong("holdingId", holdingBean.getId())
            .uniqueResult();

        if (bean==null) {
            session.getTransaction().commit();
            return;
        }
        session.createQuery(
            "delete org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate " +
                "where relate.holdingId=:holdingId")
            .setLong("holdingId", holdingBean.getId())
            .executeUpdate();
        session.delete(bean);

        session.getTransaction().commit();
    }

    public void setRelateHoldingCompany(Long holdingId, Long companyId ) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        HoldingCompanyRelationBean relateBean = new HoldingCompanyRelationBean();
        relateBean.setCompanyId(companyId);
        relateBean.setHoldingId(holdingId);
        session.save(relateBean);
        session.getTransaction().commit();
    }
}
