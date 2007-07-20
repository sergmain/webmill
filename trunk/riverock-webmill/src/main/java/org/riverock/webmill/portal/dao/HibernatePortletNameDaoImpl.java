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
import org.hibernate.StatelessSession;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.bean.PortletNameBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 09.11.2006
 *         Time: 20:06:46
 *         <p/>
 *         $Id$
 */
public class HibernatePortletNameDaoImpl implements InternalPortletNameDao {
    private static final String PORTLET_QUERY_STRING = "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
    "where portlet.portletName = :portletName";

    public PortletName getPortletName(Long portletId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            PortletNameBean bean = (PortletNameBean)session.createQuery(
                "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
                    "where portlet.portletId = :portletId")
                .setLong("portletId", portletId)
                .uniqueResult();
            return bean;
        }
        finally {
            session.close();
        }
    }

    public PortletName getPortletName(String portletName) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            PortletNameBean bean = getPortletNameInternal(session, portletName);
            return bean;
        }
        finally {
            session.close();
        }
    }

    private PortletNameBean getPortletNameInternal(StatelessSession session, String portletName) {
        List<PortletNameBean> beans = session.createQuery(
            PORTLET_QUERY_STRING)
            .setString("portletName", portletName)
            .list();
        PortletNameBean bean=null;
        if (beans.size()>0) {
            bean = beans.get(0);
        }
        return bean;
    }

    private PortletNameBean getPortletNameInternal(Session session, String portletName) {
        List<PortletNameBean> beans = session.createQuery(
            PORTLET_QUERY_STRING)
            .setString("portletName", portletName)
            .list();
        PortletNameBean bean=null;
        if (beans.size()>0) {
            bean = beans.get(0);
        }
        return bean;
    }

    public Long createPortletName(PortletName portletName) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            if (getPortletNameInternal(session, portletName.getPortletName())==null) {
                PortletNameBean bean = new PortletNameBean(portletName);
                session.save(bean);
                session.flush();
                session.clear();
                session.getTransaction().commit();
                return bean.getPortletId();
            }
            return null;
        }
        finally {
            session.close();
        }
    }

    public void updatePortletName(PortletName portletNameBean) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            PortletNameBean bean = (PortletNameBean) session.createQuery(
                "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
                    "where portlet.portletId = :portletId")
                .setLong("portletId", portletNameBean.getPortletId())
                .uniqueResult();
            if (bean!=null) {
                bean.setPortletName(portletNameBean.getPortletName());
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deletePortletName(PortletName portletNameBean) {
        if (portletNameBean==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
                    "where portlet.portletId = :portletId")
                .setLong("portletId", portletNameBean.getPortletId())
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public List<PortletName> getPortletNameList() {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<PortletNameBean> list = session.createQuery(
                "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet")
                .list();
            return (List)list;
        }
        finally {
            session.close();
        }
    }

    public void registerPortletName(String portletName) {
        if (portletName==null) {
            return;
        }
        String resultPortletName = portletName;
        if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
            resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
        }
        if (getPortletName(resultPortletName)!=null) {
            return;
        }
        PortletNameBean bean = new PortletNameBean();
        bean.setPortletName(resultPortletName);
        createPortletName(bean);
    }
}
