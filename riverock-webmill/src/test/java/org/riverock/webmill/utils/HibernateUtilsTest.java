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
package org.riverock.webmill.utils;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * User: SergeMaslyukov
 * Date: 08.10.2006
 * Time: 18:30:57
 * <p/>
 * $Id$
 */
public class HibernateUtilsTest {

    public static void prepareSession() {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.setProperty("hibernate.dialect", MySQL5InnoDBDialect.class.getName() );
        cfg.setProperty("hibernate.connection.release_mode", "after_transaction" );
//        cfg.setProperty("hibernate.connection.release_mode", "on_close" );
        cfg.setProperty("hibernate.transaction.factory_class", org.hibernate.transaction.JDBCTransactionFactory.class.getName() );
        cfg.setProperty("hibernate.current_session_context_class", "thread" );
        cfg.setProperty("hibernate.transaction.flush_before_completion", "false" );
//        cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider" );

        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver" );
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull" );
        cfg.setProperty("hibernate.show_sql", "true" );
        cfg.setProperty("hibernate.connection.username", "root" );
//        cfg.setProperty("hibernate.connection.password", "" );

        HibernateUtils.setAnnotatedClasses(cfg);

        HibernateUtils.setSessionFactory(cfg.buildSessionFactory());

    }
}
