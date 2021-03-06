/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.tools;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import org.hibernate.cfg.Configuration;
import org.riverock.commerce.bean.*;

/**
 * @author Sergei Maslyukov
 *         Date: 11.12.2006
 *         Time: 20:22:05
 *         <p/>
 *         $Id$
 */
public class HibernateUtils {
    static SessionFactory sessionFactory=null;

    public static final Class[] CLASSES = {
        Currency.class,
        CurrencyCurs.class,
        CurrencyPrecision.class,
        Shop.class,
        ShopItem.class,
        UserOrder.class,
        ImportShopItem.class,
        StandardCurrency.class,
        StandardCurrencyCurs.class
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

            Configuration cfg = new Configuration();

            cfg.setProperty("hibernate.dialect", hibernateFamily )
                .setProperty("hibernate.connection.release_mode", "after_transaction" )
                .setProperty("hibernate.transaction.factory_class", "jdbc" )
                .setProperty("hibernate.current_session_context_class", "thread" )
                .setProperty("hibernate.transaction.flush_before_completion", "false" )
//                .setProperty("hibernate.cache.provider_class", org.hibernate.cache.EhCacheProvider.class.getName() )
                .setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/webmill")
            ;
            setAnnotatedClasses(cfg);
            sessionFactory = cfg.buildSessionFactory();

        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }

    static void setAnnotatedClasses(Configuration cfg) {
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


    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtils.sessionFactory = sessionFactory;
    }
}
