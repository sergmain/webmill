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
import org.hibernate.Query;

import org.riverock.interfaces.portal.bean.VirtualHost;
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
        List<VirtualHostBean> list = session.createQuery(
            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
            "where host.siteId = :site_id")
            .setLong("site_id", siteId)
            .list();
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
            "where host.id = :id");
        query.setLong("id", virtualHost.getId());
        VirtualHostBean bean = (VirtualHostBean)query.uniqueResult();
        session.delete(bean);

        session.getTransaction().commit();
    }

    public void deleteVirtualHostForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
            "where host.siteId = :site_id");
        query.setLong("site_id", siteId);
        VirtualHostBean bean = (VirtualHostBean)query.uniqueResult();
        session.delete(bean);

        session.getTransaction().commit();
    }

}
