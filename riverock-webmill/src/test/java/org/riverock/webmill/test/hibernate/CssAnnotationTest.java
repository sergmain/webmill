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
import java.util.Date;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.AnnotationConfiguration;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.webmill.main.CssAnnotated;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.utils.HibernateUtilsTest;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.webmill.portal.dao.InternalCssDao;
import org.riverock.webmill.portal.dao.HibernateCssDaoImpl;
import org.riverock.interfaces.portal.bean.Css;

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
        InternalCssDao cssDao = new HibernateCssDaoImpl();
        List<Css> result=null;
        long mills = System.currentTimeMillis();
        result = cssDao.getCssList(16L);
        System.out.println("Time: " +(System.currentTimeMillis()-mills) +" mills.");
        System.out.println("result: " + result);

        Css css = cssDao.getCssCurrent(16L);

        System.out.println("css: " + css);

        CssBean bean = new CssBean();
//        bean.setCss("aaaa");
        bean.setCssComment("this is comment");
        bean.setCurrent(false);
        bean.setDate( new Date() );
        bean.setSiteId( 16L );

        Long cssId = cssDao.createCss(bean);
        System.out.println("cssId = " + cssId);

/*

//        Session session = HibernateUtils.getSession();
        List result=null;
        session.beginTransaction();

//        for (int i=0; i<10000; i++) {
            result = session.createQuery("select css from org.riverock.webmill.main.CssAnnotated as css").list();
//        }

        System.out.println("Time: " +(System.currentTimeMillis()-mills) +" mills.");
        System.out.println("result: " + result);

        Css cssAnnotated = result.get(0);
        Blob blob = cssAnnotated.getCss();
        long length=blob.length();
        byte[] bytes = blob.getBytes(1, (int)length);
        System.out.println("bytes = " + new String(bytes));

        blob.setBytes(1, "09877654321".getBytes());
        cssAnnotated.setCssComment("new comment");

        Long count =  (Long)session.createQuery("select count(*) from org.riverock.webmill.main.CssAnnotated").uniqueResult();
        System.out.println("count = " + count);

        session.flush();
        session.getTransaction().commit();
*/
    }
}
