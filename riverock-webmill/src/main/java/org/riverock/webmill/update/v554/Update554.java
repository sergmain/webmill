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
package org.riverock.webmill.update.v554;

import java.util.Properties;

import org.hibernate.cfg.AnnotationConfiguration;

import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 22.11.2006
 *         Time: 17:56:29
 *         <p/>
 *         $Id$
 */
public class Update554 {
    public static void prepareSession(Properties prop) {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.setProperty("hibernate.dialect", prop.getProperty("dialect") );
        cfg.setProperty("hibernate.connection.release_mode", prop.getProperty("release_mode") );
        cfg.setProperty("hibernate.current_session_context_class", prop.getProperty("current_session_context_class") );

        cfg.setProperty("hibernate.transaction.factory_class", prop.getProperty("transaction.factory_class") );
        cfg.setProperty("hibernate.transaction.flush_before_completion", prop.getProperty("transaction.flush_before_completion") );

        cfg.setProperty("hibernate.connection.driver_class", prop.getProperty("connection.driver_class") );
        cfg.setProperty("hibernate.connection.url", prop.getProperty("connection.url") );
        cfg.setProperty("hibernate.connection.username", prop.getProperty("connection.username") );
        String password = prop.getProperty("password");
        if (password!=null && password.trim().length()>0)
            cfg.setProperty("hibernate.connection.password", password );

        cfg.setProperty("hibernate.show_sql", prop.getProperty("show_sql") );

        for (Class clazz : HibernateUtils.CLASSES) {
            cfg.addAnnotatedClass(clazz);
        }

        HibernateUtils.setSessionFactory(cfg.buildSessionFactory());

    }

    public static void main(String[] args) {

    }
}
