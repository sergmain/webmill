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
import java.util.Map;
import java.io.StringWriter;

import org.hibernate.Session;
import org.apache.log4j.Logger;

import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.webmill.portal.bean.CatalogBean;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 19:21:19
 *         <p/>
 *         $Id$
 */
public class HibernatePreferencesDaoImpl implements InternalPreferencesDao {
    private final static Logger log = Logger.getLogger(HibernatePreferencesDaoImpl.class);

    private ClassLoader classLoader=null;

    public HibernatePreferencesDaoImpl() {
        classLoader=Thread.currentThread().getContextClassLoader();
    }

    public void store(Map<String, List<String>> preferences, Long catalogId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            StringWriter out = new StringWriter();

            for (Map.Entry<String, List<String>> entry : preferences.entrySet()) {
                for (String s : entry.getValue()) {
                    out.write(entry.getKey()+'='+s+'\n');
                }
            }
            String s = out.toString();

            if (log.isDebugEnabled()) {
                log.debug("    result metadata: " + s);
                log.debug("    catalogId: " + catalogId);
            }

            if (catalogId==null) {
                return;
            }

            Session session = HibernateUtils.getSession();
            session.beginTransaction();

            CatalogBean bean = (CatalogBean)session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                    "where catalog.catalogId=:catalogId")
                .setLong("catalogId", catalogId)
                .uniqueResult();

            if (bean==null) {
                session.getTransaction().commit();
                return;
            }
            bean.setMetadata(s);

            session.getTransaction().commit();
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
