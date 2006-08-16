/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.dao;

import java.sql.Types;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * @author Sergei Maslyukov
 *         Date: 24.05.2006
 *         Time: 18:31:00
 */
public class InternalCmsDaoImpl implements InternalCmsDao {
    private final static Logger log = Logger.getLogger(InternalCmsDaoImpl.class);

    public void deleteArticleForSite(DatabaseAdapter adapter, Long siteId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE_DATA " +
                    "where ID_SITE_CTX_ARTICLE in " +
                    "(select a.ID_SITE_CTX_ARTICLE from WM_PORTLET_ARTICLE a, WM_PORTAL_SITE_LANGUAGE b " +
                    " where b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE " +
                    "where ID_SITE_SUPPORT_LANGUAGE in " +
                    "(select ID_SITE_SUPPORT_LANGUAGE from WM_PORTAL_SITE_LANGUAGE where ID_SITE=?)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete articles";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteArticleForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE_DATA " +
                    "where ID_SITE_CTX_ARTICLE in " +
                    "(select a.ID_SITE_CTX_ARTICLE from WM_PORTLET_ARTICLE a " +
                    " where a.ID_SITE_SUPPORT_LANGUAGE=?)",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE " +
                    "where ID_SITE_SUPPORT_LANGUAGE=? ",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete articles";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteNewsForSite(DatabaseAdapter adapter, Long siteId) {

        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM_TEXT " +
                    "where ID in " +
                    "(select a.ID from WM_NEWS_ITEM a, WM_NEWS_LIST b, WM_PORTAL_SITE_LANGUAGE c " +
                    "where c.ID_SITE=? and b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and" +
                    "      b.ID_NEWS=a.ID_NEWS )",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM " +
                    "where ID_NEWS in " +
                    "(select b.ID_NEWS from WM_NEWS_LIST b, WM_PORTAL_SITE_LANGUAGE c " +
                    " where c.ID_SITE=? and b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE ) ",
                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_LIST " +
                    "where ID_SITE_SUPPORT_LANGUAGE in " +
                    "(select ID_SITE_SUPPORT_LANGUAGE from WM_PORTAL_SITE_LANGUAGE where ID_SITE=?)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete news";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteNewsForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {

        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM_TEXT " +
                    "where ID in " +
                    "(select a.ID from WM_NEWS_ITEM a, WM_NEWS_LIST b " +
                    " where b.ID_SITE_SUPPORT_LANGUAGE=? and b.ID_NEWS=a.ID_NEWS )",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM " +
                    "where ID_NEWS in " +
                    "(select b.ID_NEWS from WM_NEWS_LIST b where b.ID_SITE_SUPPORT_LANGUAGE=? ) ",
                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_LIST " +
                    "where ID_SITE_SUPPORT_LANGUAGE=? ",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete news";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }
}