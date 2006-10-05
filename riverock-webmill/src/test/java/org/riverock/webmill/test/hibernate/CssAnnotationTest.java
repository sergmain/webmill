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
package org.riverock.webmill.test.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.AnnotationConfiguration;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.webmill.main.CssAnnotated;

/**
 * @author Sergei Maslyukov
 *         Date: 05.10.2006
 *         Time: 20:09:19
 *         <p/>
 *         $Id$
 */
public class CssAnnotationTest {
    public static void main(String[] args) throws DatabaseException {

//        StartupApplication.init();
        Configuration cfg = new AnnotationConfiguration().addAnnotatedClass(CssAnnotated.class);
        cfg.setProperty("hibernate.dialect", MySQLDialect.class.getName() );
        cfg.setProperty("hibernate.connection.release_mode", "after_transaction" );
//        cfg.setProperty("hibernate.connection.release_mode", "on_close" );
        cfg.setProperty("hibernate.transaction.factory_class", org.hibernate.transaction.JDBCTransactionFactory.class.getName() );
        cfg.setProperty("hibernate.current_session_context_class", "thread" );
        cfg.setProperty("hibernate.transaction.flush_before_completion", "false" );

        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver" );
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull" );
        cfg.setProperty("hibernate.connection.username", "root" );
//        cfg.setProperty("hibernate.connection.password", "" );

        SessionFactory sessions = cfg.buildSessionFactory();

//        java.sql.Connection conn = DatabaseAdapter.getInstance().getConnection();
//        Session session = sessions.openSession(conn);
        Session session = sessions.openSession();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

         session.beginTransaction();

        List result = session.createQuery("select css from org.riverock.webmill.main.CssAnnotated as css").list();
        System.out.println("result: " + result);

        Long count =  (Long)session.createQuery("select count(*) from org.riverock.webmill.main.CssAnnotated").uniqueResult();
        System.out.println("count = " + count);

        session.getTransaction().commit();

    }
}
