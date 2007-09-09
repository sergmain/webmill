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
import java.util.Observable;
import java.util.Observer;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.StatelessSession;

import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.webmill.portal.dao.HibernateUtils;
import org.riverock.webmill.portal.bean.VirtualHostBean;

/**
 * @author Sergei Maslyukov
 *         Date: 08.11.2006
 *         Time: 19:49:27
 *         <p/>
 *         $Id$
 */
public class HibernateVirtualHostDaoImpl implements InternalVirtualHostDao {

    private final ChangableObservable observable = new ChangableObservable();

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public List<VirtualHost> getVirtualHostsFullList() {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery("select host from org.riverock.webmill.portal.bean.VirtualHostBean as host");
            List<VirtualHostBean> siteList = query.list();
            return (List)siteList;
        }
        finally {
            session.close();
        }
    }

    public List<VirtualHost> getVirtualHosts(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<VirtualHostBean> list = session.createQuery(
                "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
                "where host.siteId = :site_id")
                .setLong("site_id", siteId)
                .list();
            return (List)list;
        }
        finally {
            session.close();
        }
    }

    public Long createVirtualHost(VirtualHost virtualHost) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            if (virtualHost.isDefaultHost()) {
                clearDefaultHostFlag(virtualHost.getSiteId(), session);
            }
            VirtualHostBean bean = new VirtualHostBean(virtualHost.getId(), virtualHost.getSiteId(), virtualHost.getHost(), virtualHost.isDefaultHost() );
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
            return bean.getId();
        }
        finally {
            session.close();
            observable.notifyObservers();
//            SiteList.destroy();
        }
    }

    public void deleteVirtualHost(VirtualHost virtualHost) {
        if (virtualHost==null || virtualHost.isDefaultHost()) {
            return;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.VirtualHostBean host where host.id = :id")
                .setLong("id", virtualHost.getId())
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
            observable.notifyObservers();
//            SiteList.destroy();
        }
    }

    public void deleteVirtualHostForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.VirtualHostBean host where host.siteId=:site_id")
                .setLong("site_id", siteId)
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
            observable.notifyObservers();
//            SiteList.destroy();
        }
    }

    private void clearDefaultHostFlag(Long siteId, Session session) {
        List<VirtualHostBean> hosts = session.createQuery("select host from org.riverock.webmill.portal.bean.VirtualHostBean as host where host.siteId = :siteId")
            .setLong("siteId", siteId)
            .list();
        for (VirtualHostBean host : hosts) {
            if (host.isDefaultHost()) {
                host.setDefaultHost(false);
            }
        }
    }
}
