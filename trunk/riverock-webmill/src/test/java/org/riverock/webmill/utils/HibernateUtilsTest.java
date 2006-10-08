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

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.MySQLDialect;

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
        cfg.setProperty("hibernate.dialect", MySQLDialect.class.getName() );
        cfg.setProperty("hibernate.connection.release_mode", "after_transaction" );
//        cfg.setProperty("hibernate.connection.release_mode", "on_close" );
        cfg.setProperty("hibernate.transaction.factory_class", org.hibernate.transaction.JDBCTransactionFactory.class.getName() );
        cfg.setProperty("hibernate.current_session_context_class", "thread" );
        cfg.setProperty("hibernate.transaction.flush_before_completion", "false" );
        cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider" );

        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver" );
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull" );
        cfg.setProperty("hibernate.show_sql", "true" );
        cfg.setProperty("hibernate.connection.username", "root" );
//        cfg.setProperty("hibernate.connection.password", "" );

        HibernateUtils.setAnnotatedClasses(cfg);

        HibernateUtils.setSessionFactory(cfg.buildSessionFactory());

    }
}
