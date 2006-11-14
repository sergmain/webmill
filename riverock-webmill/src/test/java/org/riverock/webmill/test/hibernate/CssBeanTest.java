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
package org.riverock.webmill.test.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.portal.dao.HibernateCssDaoImpl;
import org.riverock.webmill.portal.dao.InternalCssDao;
import org.riverock.webmill.utils.HibernateUtilsTest;

/**
 * @author Sergei Maslyukov
 *         Date: 05.10.2006
 *         Time: 20:09:19
 *         <p/>
 *         $Id$
 */
public class CssBeanTest {
    public static void main(String[] args) throws DatabaseException, SQLException {

        StartupApplication.init();

        HibernateUtilsTest.prepareSession();
        InternalCssDao cssDao = new HibernateCssDaoImpl();
        long mills = System.currentTimeMillis();
        List<Css> result=cssDao.getCssList(16L);
        System.out.println("Time: " +(System.currentTimeMillis()-mills) +" mills.");
        System.out.println("result: " + result);

        Css css = cssDao.getCssCurrent(16L);

        System.out.println("css: " + css);

        CssBean bean = new CssBean();
        bean.setCss("aaaa");
        bean.setCssComment("this is comment");
        bean.setCurrent(false);
        bean.setDate( new Date() );
        bean.setSiteId( 16L );

        Long cssId = cssDao.createCss(bean);
        System.out.println("cssId = " + cssId);
    }
}
