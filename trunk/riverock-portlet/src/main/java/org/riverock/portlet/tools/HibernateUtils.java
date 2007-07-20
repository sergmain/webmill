/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.tools;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AnnotationConfiguration;

import org.riverock.portlet.webclip.WebclipBean;
import org.riverock.portlet.search.SearchBean;

/**
 * @author Sergei Maslyukov
 *         Date: 30.11.2006
 *         Time: 20:31:51
 *         <p/>
 *         $Id$
 */
public class HibernateUtils {
    static SessionFactory sessionFactory=null;

    public static final Class[] CLASSES = {
        SearchBean.class,
        WebclipBean.class
    };

    public static final String HIBERNATE_FAMILY = "hibernate_family";
    private synchronized static void prepareSession() {
        try {
            String hibernateFamily = null;
            Context envCtx = null;
            try {
                Context initCtx = new InitialContext();
                envCtx = (Context) initCtx.lookup("java:comp/env");
            } catch (Throwable e) {
                System.out.println("JNDI context java:comp/env not found, will try search in root context");
            }

            if (envCtx!=null) {
                try {
                    hibernateFamily=(String) envCtx.lookup(HIBERNATE_FAMILY);
                }
                catch (NamingException e) {
                    System.out.println("Error get hibernate family from java:comp/env, " + e.toString());
                }
            }

            if (hibernateFamily==null) {
                envCtx = new InitialContext();
                hibernateFamily=(String) envCtx.lookup(HIBERNATE_FAMILY);
            }

            if (hibernateFamily==null) {
                throw new IllegalArgumentException("hiberante family not defined");
            }
            System.out.println("Hibernate family: "+hibernateFamily);

            AnnotationConfiguration cfg = new AnnotationConfiguration();

            cfg.setProperty("hibernate.dialect", hibernateFamily )
                .setProperty("hibernate.connection.release_mode", "after_transaction" )
                .setProperty("hibernate.transaction.factory_class", org.hibernate.transaction.JDBCTransactionFactory.class.getName() )
                .setProperty("hibernate.current_session_context_class", "thread" )
                .setProperty("hibernate.transaction.flush_before_completion", "false" )
                .setProperty("hibernate.cache.provider_class", org.hibernate.cache.EhCacheProvider.class.getName() )
                .setProperty("net.sf.ehcache.configurationResourceName", "/ehcache.xml" )
                .setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/webmill")
            ;
            setAnnotatedClasses(cfg);
            sessionFactory = cfg.buildSessionFactory();

        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }

    static void setAnnotatedClasses(AnnotationConfiguration cfg) {
        for (Class clazz : CLASSES) {
            cfg.addAnnotatedClass(clazz);
        }
    }

    public static Session getSession() throws HibernateException {
        if (sessionFactory==null) {
            prepareSession();
        }
        return sessionFactory.openSession();
    }

    public static StatelessSession getStatelSession() throws HibernateException {
        if (sessionFactory==null) {
            prepareSession();
        }
        return sessionFactory.openStatelessSession();
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtils.sessionFactory = sessionFactory;
    }
}
