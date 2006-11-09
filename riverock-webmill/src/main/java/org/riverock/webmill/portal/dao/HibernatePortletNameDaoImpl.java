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
    public PortletName getPortletName(Long portletId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        PortletNameBean bean = (PortletNameBean)session.createQuery(
            "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
                "where portlet.portletId = :portletId")
            .setLong("portletId", portletId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public PortletName getPortletName(String portletName) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        PortletNameBean bean = (PortletNameBean)session.createQuery(
            "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
            "where portlet.portletName = :portletName")
            .setString("portletName", portletName)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public Long createPortletName(PortletName portletName) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        PortletNameBean bean = new PortletNameBean(portletName);
        session.save(bean);
        session.flush();
        session.getTransaction().commit();
        return bean.getPortletId();
    }

    public void updatePortletName(PortletName portletNameBean) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        PortletNameBean bean = (PortletNameBean) session.createQuery(
            "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
                "where portlet.portletId = :portletId")
            .setLong("portletId", portletNameBean.getPortletId())
            .uniqueResult();
        if (bean!=null) {
            bean.setPortletName(portletNameBean.getPortletName());
        }
        session.getTransaction().commit();
    }

    public void deletePortletName(PortletName portletNameBean) {
        if (portletNameBean==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        PortletNameBean bean = (PortletNameBean) session.createQuery(
            "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet " +
                "where portlet.portletId = :portletId")
            .setLong("portletId", portletNameBean.getPortletId())
            .uniqueResult();
        
        if (bean!=null) {
            session.delete(bean);
        }

        session.getTransaction().commit();
    }

    public List<PortletName> getPortletNameList() {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<PortletNameBean> list = session.createQuery(
            "select portlet from org.riverock.webmill.portal.bean.PortletNameBean as portlet")
            .list();
        session.getTransaction().commit();
        return (List)list;
    }

    public void registerPortletName(String portletName) {
        if (portletName==null) {
            return;
        }
        String resultPortletName = portletName;
        if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
            resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
        }
        PortletNameBean bean = new PortletNameBean();
        bean.setPortletName(resultPortletName);
        createPortletName(bean);
    }
}
