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
package org.riverock.webmill.update.v554;

import java.util.List;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Blob;
import java.sql.ResultSet;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.Css;

/**
 * @author Sergei Maslyukov
 *         Date: 07.11.2006
 *         Time: 14:47:06
 *         <p/>
 *         $Id$
 */
public class ConvertCssBigTableToBlob {
    public static void main(String[] args) throws SQLException, IOException {
        StartupApplication.init();

        List<Site> sites = InternalDaoFactory.getInternalSiteDao().getSites();
        for (Site site : sites) {
            System.out.println("site = " + site);
            List<Css> cssList = InternalDaoFactory.getInternalCssDao().getCssList(site.getSiteId());

            for (Css css : cssList) {

                DatabaseAdapter db = DatabaseAdapter.getInstance();
                switch(db.getFamily()){
                    case DatabaseManager.ORACLE_FAMALY:
                        updateBlobForOracle(db, css);
                        break;
                    case DatabaseManager.MYSQL_FAMALY:
                        updateBlobForMySql(db, css);
                        break;
                    default:
                        throw new RuntimeException("Not implemented");
                }
                db.commit();
            }
        }
    }

    private static void updateBlobForOracle(DatabaseAdapter db, Css css) throws SQLException, IOException {
        // получаем поток на клоб
        PreparedStatement ps = db.prepareStatement("select css_blob from wm_portal_css where ID_SITE_CONTENT_CSS=?");
        ps.setLong(1, css.getCssId());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            byte[] fileBytes = new byte[]{};
            if (css.getCss()!= null) {
                fileBytes = css.getCss().getBytes();
            }

            Blob mapBlob = rs.getBlob("css_blob");
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

    private static void updateBlobForMySql(DatabaseAdapter db, Css css) throws SQLException {
        String sql_ =
            "update wm_portal_css " +
                "set css_blob=? " +
                "where ID_SITE_CONTENT_CSS=?";

        PreparedStatement ps = db.prepareStatement(sql_);

        byte[] fileBytes = new byte[]{};
        if (css.getCss()!= null) {
            fileBytes = css.getCss().getBytes();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        ps.setBinaryStream(1, byteArrayInputStream, fileBytes.length);
        ps.setLong(2, css.getCssId());
        ps.executeUpdate();
    }
}
