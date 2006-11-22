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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.startup.StartupApplication;

/**
 * @author Sergei Maslyukov
 *         Date: 07.11.2006
 *         Time: 14:47:06
 *         <p/>
 *         $Id$
 */
public class ConvertTemplateBigTableToBlob {
    public static void main(String[] args) throws SQLException, IOException {
        StartupApplication.init();

        DatabaseAdapter db = DatabaseAdapter.getInstance();
        Connection conn = db.getConnection();

        PreparedStatement ps = conn.prepareStatement(
            "select ID_SITE_TEMPLATE, TEMPLATE_DATA from WM_PORTAL_TEMPLATE"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Long templateId = RsetTools.getLong(rs, "ID_SITE_TEMPLATE");
            String templateData = RsetTools.getString(rs, "TEMPLATE_DATA", "");
            System.out.println("templateId = " + templateId+", template size: " + templateData.length());
            switch(db.getFamily()){
                case DatabaseManager.ORACLE_FAMALY:
                    updateBlobForOracle(conn, templateId, templateData);
                    break;
                case DatabaseManager.MYSQL_FAMALY:
                    updateBlobForMySql(conn, templateId, templateData);
                    break;
                default:
                    throw new RuntimeException("Not implemented");
            }
        }
        rs.close();
        ps.close();
        conn.commit();
        conn.close();
    }

    private static void updateBlobForOracle(Connection conn, Long templateId, String templateData) throws SQLException, IOException {
        
        PreparedStatement ps = conn.prepareStatement(
            "select template_blob from wm_portal_template where id_site_template=? for update ");

        ps.setLong(1, templateId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            byte[] fileBytes = new byte[]{};
            if (templateData!= null) {
                fileBytes = templateData.getBytes();
            }

            Blob mapBlob = rs.getBlob("template_blob");
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

    private static void updateBlobForMySql(Connection conn, Long templateId, String templateData) throws SQLException {
        String sql_ =
            "update wm_portal_template " +
                "set template_blob=? " +
                "where id_site_template=?";

        PreparedStatement ps = conn.prepareStatement(sql_);

        byte[] fileBytes = new byte[]{};
        if (templateData!= null) {
            fileBytes = templateData.getBytes();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        ps.setBinaryStream(1, byteArrayInputStream, fileBytes.length);
        ps.setLong(2, templateId);
        ps.executeUpdate();
    }
}
