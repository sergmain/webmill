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

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Blob;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.util.List;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.portal.dao.*;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Article;

/**
 * @author Sergei Maslyukov
 *         Date: 07.11.2006
 *         Time: 14:47:06
 *         <p/>
 *         $Id$
 */
public class ConvertArticleBigTableToBlob {
    public static void main(String[] args) throws SQLException, IOException {
        StartupApplication.init();

        InternalSiteLanguageDao internalSiteLanguageDao = new InternalSiteLanguageDaoImpl();
        InternalCmsDao internalXsltDao = new InternalCmsDaoImpl();
        InternalSiteDao internalSiteDao = new InternalSiteDaoImpl();

        List<Site> sites = internalSiteDao.getSites();
        for (Site site : sites) {
            System.out.println("site = " + site);
            List<SiteLanguage> sitesLanguages = internalSiteLanguageDao.getSiteLanguageList(site.getSiteId());
            for (SiteLanguage sitesLanguage : sitesLanguages) {
                List<Article> articles = internalXsltDao.getArticleList(sitesLanguage.getSiteLanguageId(), true);

                for (Article article : articles) {

                    DatabaseAdapter db = DatabaseAdapter.getInstance();
                    switch(db.getFamily()){
                        case DatabaseManager.ORACLE_FAMALY:
                            updateBlobForOracle(db, article);
                            break;
                        case DatabaseManager.MYSQL_FAMALY:
                            updateBlobForMySql(db, article);
                            break;
                        default:
                            throw new RuntimeException("Not implemented");
                    }
                    db.commit();
                }

                articles = internalXsltDao.getArticleList(sitesLanguage.getSiteLanguageId(), false);

                for (Article article : articles) {

                    DatabaseAdapter db = DatabaseAdapter.getInstance();
                    switch(db.getFamily()){
                        case DatabaseManager.ORACLE_FAMALY:
                            updateBlobForOracle(db, article);
                            break;
                        case DatabaseManager.MYSQL_FAMALY:
                            updateBlobForMySql(db, article);
                            break;
                        default:
                            throw new RuntimeException("Not implemented");
                    }
                    db.commit();
                }
            }
        }
    }

    private static void updateBlobForOracle(DatabaseAdapter db, Article article) throws SQLException, IOException {
        // �������� ����� �� ����
        PreparedStatement ps = db.getConnection().prepareStatement(
            "select article_blob from wm_portlet_article where ID_SITE_CTX_ARTICLE=? for update ");

        ps.setLong(1, article.getArticleId());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            byte[] fileBytes = new byte[]{};
            if (article.getArticleData()!= null) {
                fileBytes = article.getArticleData().getBytes();
            }

            Blob mapBlob = rs.getBlob(1);
            OutputStream blobOutputStream = mapBlob.setBinaryStream(0L);
            blobOutputStream.write(fileBytes);
            blobOutputStream.flush();
            blobOutputStream.close();
        }
        rs.close();
        rs=null;
        ps.close();
        ps=null;
    }

    private static void updateBlobForMySql(DatabaseAdapter db, Article article) throws SQLException {
        String sql_ =
            "update wm_portlet_article " +
                "set article_blob=? " +
                "where ID_SITE_CTX_ARTICLE=?";

        PreparedStatement ps = db.prepareStatement(sql_);

        byte[] fileBytes = new byte[]{};
        if (article.getArticleData()!= null) {
            fileBytes = article.getArticleData().getBytes();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        ps.setBinaryStream(1, byteArrayInputStream, fileBytes.length);
        ps.setLong(2, article.getArticleId());
        ps.executeUpdate();
    }
}
