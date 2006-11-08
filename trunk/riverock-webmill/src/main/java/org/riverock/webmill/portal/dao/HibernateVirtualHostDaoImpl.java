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
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.webmill.portal.bean.VirtualHostBean;

/**
 * @author Sergei Maslyukov
 *         Date: 08.11.2006
 *         Time: 19:49:27
 *         <p/>
 *         $Id$
 */
public class HibernateVirtualHostDaoImpl implements InternalVirtualHostDao {
    public List<VirtualHost> getVirtualHostsFullList() {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select host from org.riverock.webmill.portal.bean.VirtualHostBean as host");
        List<VirtualHostBean> siteList = query.list();
        session.getTransaction().commit();
        return (List)siteList;
    }

    public List<VirtualHost> getVirtualHosts(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
            "where host.siteId = ?1");
        query.setLong(1, siteId);
        List<VirtualHostBean> list = query.list();
        session.getTransaction().commit();
        return (List)list;
    }

    public Long createVirtualHost(VirtualHost virtualHost) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        VirtualHostBean bean = new VirtualHostBean(virtualHost.getId(), virtualHost.getSiteId(), virtualHost.getHost() );
        session.save(bean);
        session.flush();
        session.getTransaction().commit();
        return bean.getId();
    }

    public void deleteVirtualHost(VirtualHost virtualHost) {
        if (virtualHost==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
            "where host.id = ?1");
        query.setLong(1, virtualHost.getId());
        VirtualHostBean bean = (VirtualHostBean)query.uniqueResult();
        session.delete(bean);

        session.getTransaction().commit();
    }

    public void deleteVirtualHostForSite(DatabaseAdapter adapter, Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
            "where host.siteId = ?1");
        query.setLong(1, siteId);
        VirtualHostBean bean = (VirtualHostBean)query.uniqueResult();
        session.delete(bean);

        session.getTransaction().commit();
    }

    public Long createVirtualHost(DatabaseAdapter adapter, VirtualHost host) {
        return createVirtualHost(host);
    }
}
