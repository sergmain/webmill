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

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.common.tools.RsetTools;
import org.riverock.portlet.webclip.WebclipBean;

import java.sql.*;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 17:31:51
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class WebclipDaoImpl implements WebclipDao {
    private static Logger log = Logger.getLogger( WebclipDaoImpl.class );

    public WebclipBean getWebclip(Long siteId, Long webclipId) {
        if (webclipId==null || siteId==null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select DATE_POST " +
                "from   WM_PORTLET_WEBCLIP " +
                "where  ID_SITE=? and ID_WEBCLIP=?"
            );
            RsetTools.setLong(ps, 1, siteId );
            RsetTools.setLong(ps, 2, webclipId );

            rs = ps.executeQuery();

            if (rs.next()) {
                Timestamp datePost = RsetTools.getTimestamp(rs, "DATE_POST");
                String webclipData = initWebclipData(db_, webclipId);
                return new WebclipBean(siteId, webclipId, webclipData, datePost);
            }
            return null;
        }
        catch (Throwable e) {
            final String es = "Error get webclip";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }

    public Long createWebclip(Long siteId, String webclipData) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTLET_WEBCLIP" );
            seq.setTableName( "WM_PORTLET_WEBCLIP" );
            seq.setColumnName( "ID_WEBCLIP" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_PORTLET_WEBCLIP"+
                 "(ID_WEBCLIP, ID_SITE, DATE_POST, WEBCLIP_DATA)"+
                "values"+
                "( ?,  ?,  ?,  null )";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );
            ps.setLong(2, siteId );
            ps.setTimestamp(3, new java.sql.Timestamp( System.currentTimeMillis()));
            ps.executeUpdate();

            /**
             * @param idRec - value of PK in main table
             * @param pkName - name PK in main table
             * @param pkType - type of PK in main table
             * @param nameTargetTable  - name of slave table
             * @param namePkTargetTable - name of PK in slave table
             * @param nameTargetField - name of filed with BigText data in slave table
             * @param insertString - insert string
             * @param isDelete - delete data from slave table before insert true/false
             */

            if (StringUtils.isNotBlank(webclipData)) {
                DatabaseManager.insertBigText(
                    adapter,
                    id,
                    "ID_WEBCLIP",
                    PrimaryKeyTypeTypeType.NUMBER,
                    "WM_PORTLET_WEBCLIP_DATA",
                    "ID_WEBCLIP_DATA",
                    "WEBCLIP_DATA",
                    webclipData,
                    false
                );
            }
            adapter.commit();
            return id;
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error create webclip";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
            rs=null;
            ps=null;
            adapter=null;
        }
    }

    public void updateWebclip(WebclipBean webclip) {
        String sql_ =
            "update WM_PORTLET_WEBCLIP "+
            "set    DATE_POST=? "+
            "where  ID_SITE_CTX_ARTICLE=?";

        PreparedStatement ps = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement(sql_);

            ps.setTimestamp(1, new java.sql.Timestamp( System.currentTimeMillis()));
            ps.setLong(2, webclip.getWebclipId() );
            ps.executeUpdate();

            /**
             * @param idRec - value of PK in main table
             * @param pkName - name PK in main table
             * @param pkType - type of PK in main table
             * @param nameTargetTable  - name of slave table
             * @param namePkTargetTable - name of PK in slave table
             * @param nameTargetField - name of filed with BigText data in slave table
             * @param insertString - insert string
             * @param isDelete - delete data from slave table before insert true/false
             */
            if (StringUtils.isNotBlank(webclip.getWebclipData())) {
                DatabaseManager.insertBigText(
                    adapter,
                    webclip.getWebclipId(),
                    "ID_WEBCLIP",
                    PrimaryKeyTypeTypeType.NUMBER,
                    "WM_PORTLET_WEBCLIP_DATA",
                    "ID_WEBCLIP_DATA",
                    "WEBCLIP_DATA",
                    webclip.getWebclipData(),
                    true
                );
            }
            else {
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_PORTLET_WEBCLIP_DATA where ID_WEBCLIP=?",
                    new Object[]{webclip.getWebclipId()}, new int[]{Types.DECIMAL}
                );

            }
            adapter.commit();
        }
        catch (Exception e) {
            try {
                if( adapter != null )
                    adapter.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error update webclip";
            log.error( es, e );
            throw new IllegalStateException( es, e );
       }
       finally {
            DatabaseManager.close(adapter, ps);
       }
    }

    public void deleteWebclip(Long siteId, Long weblipId) {
        if (weblipId==null || siteId==null) {
            return;
        }

        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            Long testWebclipId = DatabaseManager.getLongValue(
                dbDyn, "select ID_WEBCLIP from WM_PORTLET_WEBCLIP where ID_WEBCLIP=? and ID_SITE=?",
                new Object[]{weblipId, siteId}, new int[]{Types.DECIMAL, Types.DECIMAL}
            );
            if (testWebclipId==null) {
                return;
            }
            DatabaseManager.runSQL(
                dbDyn,
                "delete from WM_PORTLET_WEBCLIP_DATA where ID_WEBCLIP=?",
                new Object[]{weblipId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                dbDyn,
                "delete from WM_PORTLET_WEBCLIP where ID_WEBCLIP=?",
                new Object[]{weblipId}, new int[]{Types.DECIMAL}
            );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error delete webclip";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn);
        }
    }

    private String initWebclipData(DatabaseAdapter db, Long webclipId) throws SQLException {
        if (webclipId==null) {
            return "";
        }

        return DatabaseManager.getBigTextField(db, webclipId, "WEBCLIP_DATA", "WM_PORTLET_WEBCLIP_DATA", "ID_WEBCLIP", "ID_WEBCLIP");
    }
}

