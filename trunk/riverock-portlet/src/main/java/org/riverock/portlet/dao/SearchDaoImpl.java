/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.dao;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import org.riverock.portlet.search.stub.SearchBean;
import org.riverock.portlet.tools.HibernateUtils;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 23:43:01
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class SearchDaoImpl implements SearchDao {
    /**
     * Store search request in DB
     *
     * @param siteId Long
     * @param searchString String
     */
    public void storeRequest(Long siteId, String searchString) {
        if (StringUtils.isBlank(searchString)) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            SearchBean bean = new SearchBean();
            bean.setSiteId(siteId);
            bean.setSearchDate(new Date());
            bean.setWord(searchString);

            session.save(bean);
            session.flush();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
