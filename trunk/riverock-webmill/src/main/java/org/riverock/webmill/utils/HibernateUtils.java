/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.utils;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;

/**
 * This is 1st version of Hibernate utils class. initializer is very simple
 *
 * User: SergeMaslyukov
 * Date: 08.10.2006
 * Time: 17:49:58
 * <p/>
 * $Id$
 */
public class HibernateUtils {
    static SessionFactory sessionFactory=null;

    public static final Class[] CLASSES = {
        CssBean.class,
        PortalXsltBean.class,
        SiteLanguageBean.class,
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


    static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtils.sessionFactory = sessionFactory;
    }
}
