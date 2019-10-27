package org.riverock.portlet.featured_article;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * User: SergeMaslyukov
 * Date: 14.10.2007
 * Time: 14:45:10
 * $Id$
 */
public class HibernateUtils {
    static SessionFactory sessionFactory=null;

    public static final Class[] CLASSES = {
        FeaturedArticle.class
    };

    public static final String HIBERNATE_FAMILY = "hibernate_family";

    public static synchronized void destroy() {
        if (sessionFactory!=null) {
            sessionFactory.close();
            sessionFactory=null;
        }
    }

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

//                .setProperty("hibernate.validator.autoregister_listeners", "false")
//                .setProperty("hibernate.search.autoregister_listeners", "false")
            ;
            setAnnotatedClasses(cfg);

            //noinspection UnnecessaryLocalVariable
            SessionFactory sessionFactoryReference = cfg.buildSessionFactory();
            sessionFactory = sessionFactoryReference;

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
