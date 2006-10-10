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

import java.sql.SQLException;
import java.sql.Blob;
import java.util.List;

import org.hibernate.Session;

import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.webmill.main.CssAnnotated;
import org.riverock.webmill.utils.HibernateUtilsTest;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 05.10.2006
 *         Time: 20:09:19
 *         <p/>
 *         $Id$
 */
public class CssAnnotationTest {
    public static void main(String[] args) throws DatabaseException, SQLException {

        StartupApplication.init();
        HibernateUtilsTest.prepareSession();

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        long mills = System.currentTimeMillis();

        List result;
//        for (int i=0; i<10000; i++) {
            result = session.createQuery("select css from org.riverock.webmill.main.CssAnnotated as css").list();
//        }

        System.out.println("Time: " +(System.currentTimeMillis()-mills) +" mills.");
        System.out.println("result: " + result);

        CssAnnotated cssAnnotated = (CssAnnotated)result.get(0);
        Blob blob = cssAnnotated.getCssblob();
        long length=blob.length();
        byte[] bytes = blob.getBytes(1, (int)length);
        System.out.println("bytes = " + new String(bytes));

        blob.setBytes(1, "09877654321".getBytes());
        cssAnnotated.setComment("new comment");

        Long count =  (Long)session.createQuery("select count(*) from org.riverock.webmill.main.CssAnnotated").uniqueResult();
        System.out.println("count = " + count);

        session.flush();
        session.getTransaction().commit();

    }
}
