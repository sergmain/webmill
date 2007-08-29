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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AnnotationConfiguration;

import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.bean.*;

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
    private final static Logger log = Logger.getLogger( HibernateUtils.class );
    static SessionFactory sessionFactory=null;

    public static final Class[] CLASSES = {
        CssBean.class,
        PortalXsltBean.class,
        SiteLanguageBean.class,
        SiteBean.class,
        VirtualHostBean.class,
        PortletNameBean.class,
        AuthInfoImpl.class,
        UserBean.class,
        CompanyBean.class,
        HoldingBean.class,
        HoldingCompanyRelationBean.class,
        RoleBeanImpl.class,
        AuthRelateRole.class,
        CatalogBean.class,
        CatalogLanguageBean.class,
        TemplateBean.class,
        ArticleBean.class,
        NewsBean.class,
        NewsGroupBean.class,
        UserMetadataItemBean.class
    };

    public static synchronized void destroy() {
        if (sessionFactory!=null) {
            sessionFactory.close();
            sessionFactory=null;
        }
    }

    public static final String HIBERNATE_FAMILY = "hibernate_family";
    private synchronized static void prepareSession() {
        if (sessionFactory!=null) {
            return;
        }
        
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
                .setProperty("hibernate.cache.provider_class", net.sf.ehcache.hibernate.EhCacheProvider.class.getName() )
                .setProperty("net.sf.ehcache.configurationResourceName", "/ehcache.xml" )
                .setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/webmill")

//                .setProperty("hibernate.validator.autoregister_listeners", "false")
                .setProperty("hibernate.search.autoregister_listeners", "false")
            ;

            setAnnotatedClasses(cfg);
            
            //noinspection UnnecessaryLocalVariable
            SessionFactory sessionFactoryReference = cfg.buildSessionFactory();
            sessionFactory = sessionFactoryReference;

        }
        catch (Throwable ex) {
            String es = "Error prepare hibernate session";
            log.error(es);
            throw new PortalException(es, ex);
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

    public static StatelessSession getStatelessSession() throws HibernateException {
        if (sessionFactory==null) {
            prepareSession();
        }
        return sessionFactory.openStatelessSession();
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtils.sessionFactory = sessionFactory;
    }
}
