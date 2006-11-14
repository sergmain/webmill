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

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.a3.bean.AuthInfoImpl;
import org.riverock.webmill.portal.bean.CompanyBean;
import org.riverock.webmill.portal.bean.HoldingCompanyRelationBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 10.11.2006
 *         Time: 21:15:38
 *         <p/>
 *         $Id$
 */
public class HibernateCompanyDaoImpl implements InternalCompanyDao {

    public Company getCompany( String companyName ) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CompanyBean bean = (CompanyBean)session.createQuery(
            "select company from org.riverock.webmill.portal.bean.CompanyBean as company " +
            "where  company.name=:companyName ")
            .setString("companyName", companyName)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public Company getCompany( Long companyId, AuthSession authSession ) {
        if( companyId == null || authSession==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CompanyBean bean = (CompanyBean)session.createQuery(
            "select company from org.riverock.webmill.portal.bean.CompanyBean as company " +
            "where  company.isDeleted=false and company.id=:companyid and company.id in (:companyIds)")
            .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
            .setLong("companyid", companyId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public List<Company> getCompanyList( AuthSession authSession ) {
        if (authSession==null) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List bean = session.createQuery(
            "select company from org.riverock.webmill.portal.bean.CompanyBean as company " +
            "where  company.isDeleted=false and company.id in (:companyIds)")
            .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
            .list();
        session.getTransaction().commit();
        return bean;
    }

    public Long processAddCompany( Company companyBean, Long holdingId ) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CompanyBean bean = new CompanyBean(companyBean);
        session.save(bean);

        if( holdingId != null ) {
            HoldingCompanyRelationBean relate = new HoldingCompanyRelationBean();
            relate.setCompanyId(bean.getId());
            relate.setHoldingId(holdingId);
            session.save(relate);
        }

        session.flush();
        session.getTransaction().commit();
        return bean.getId();
    }

    public Long processAddCompany( Company companyBean, String userLogin, Long holdingId, AuthSession authSession ) {
        if (authSession==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        AuthInfoImpl user = (AuthInfoImpl)session.createQuery(
            "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl as auth " +
            "where  auth.userLogin=:userLogin ")
            .setString("userLogin", userLogin)
            .uniqueResult();

        if (user==null) {
            session.getTransaction().commit();
            return null;
        }

        CompanyBean bean = new CompanyBean(companyBean);
        session.save(bean);

        if( holdingId != null ) {
            HoldingCompanyRelationBean relate = new HoldingCompanyRelationBean();
            relate.setCompanyId(bean.getId());
            relate.setHoldingId(holdingId);
            session.save(relate);
        }

        session.flush();
        session.getTransaction().commit();
        return bean.getId();
    }

    public void processSaveCompany( Company companyBean, AuthSession authSession ) {
        if (authSession==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        CompanyBean company = (CompanyBean)session.createQuery(
            "select company from org.riverock.webmill.portal.bean.CompanyBean as company " +
            "where  company.is=:companyId and companyId in ( :companyIds)")
            .setParameterList("companyIds", authSession.getGrantedCompanyIdList() )
            .setLong("companyId", companyBean.getId())
            .uniqueResult();

        if (company==null) {
            session.getTransaction().commit();
            return;
        }
        company.setName(companyBean.getName());
        company.setShortName(companyBean.getShortName());
        company.setAddress(companyBean.getAddress());
        company.setCeo(companyBean.getCeo());
        company.setCfo(companyBean.getCfo());
        company.setWebsite(companyBean.getWebsite());
        company.setInfo(companyBean.getInfo());
        company.setDeleted(companyBean.isDeleted());

        session.getTransaction().commit();
    }


    public void processDeleteCompany( Company companyBean, AuthSession authSession ) {
        if (authSession==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        CompanyBean company = (CompanyBean)session.createQuery(
            "select company from org.riverock.webmill.portal.bean.CompanyBean as company " +
            "where  company.is=:companyId and companyId in ( :companyIds)")
            .setParameterList("companyIds", authSession.getGrantedCompanyIdList() )
            .setLong("companyId", companyBean.getId())
            .uniqueResult();

        if (company==null) {
            session.getTransaction().commit();
            return;
        }
        session.delete(company);

        session.getTransaction().commit();
    }
}
