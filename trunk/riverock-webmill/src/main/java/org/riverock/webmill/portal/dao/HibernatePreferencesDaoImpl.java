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
import java.util.HashMap;
import java.io.*;

import org.hibernate.StatelessSession;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.webmill.portal.dao.HibernateUtils;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.common.collections.MapWithParameters;

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

    public Map<String, List<String>> load(Long catalogId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );

            if (catalogId==null) {
                return new HashMap<String, List<String>>();
            }

            StatelessSession session = HibernateUtils.getStatelessSession();
            try {
                CatalogBean bean = (CatalogBean)session.createQuery(
                    "select catalog " +
                        "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                        "where catalog.catalogId=:catalogId")
                    .setLong("catalogId", catalogId)
                    .uniqueResult();

                if (bean==null) {
                    return new HashMap<String, List<String>>();
                }

                return initMetadata(bean.getMetadata());
            }
            finally {
                session.close();
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
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

            StatelessSession session = HibernateUtils.getStatelessSession();
            try {
                CatalogBean bean = (CatalogBean)session.createQuery(
                    "select catalog " +
                        "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                        "where catalog.catalogId=:catalogId")
                    .setLong("catalogId", catalogId)
                    .uniqueResult();

                if (bean!=null) {
                    bean.setMetadata(s);
                }
            }
            finally {
                session.close();
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Map<String, List<String>> initMetadata( String metadata ) {
        if (log.isDebugEnabled()) {
            log.debug("metadata: " + metadata);
        }
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        if (metadata==null) {
            return map;
        }

        BufferedReader reader = new BufferedReader( new InputStreamReader( new ByteArrayInputStream(metadata.getBytes())) );
        try {
            String s;
            while ((s=reader.readLine())!=null) {
                if (log.isDebugEnabled()) {
                    log.debug("Line int metadata: " + s);
                }
                int idx = s.indexOf('=');
                if (idx==-1) {
                    continue;
                }
                String key = s.substring(0, idx).trim();
                String value = s.substring(idx+1).trim();
                if (log.isDebugEnabled()) {
                    log.debug("    key: " + key +", " +value);
                }

                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                    MapWithParameters.putInStringList(map, key, value);
                }
            }
            return map;
        }
        catch( IOException e ) {
            String es = "Error load properties";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }
}
