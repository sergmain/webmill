/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.generic.db.factory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.structure.DbDataFieldDataType;
import org.riverock.generic.schema.db.structure.DbFieldType;
import org.riverock.generic.schema.db.structure.DbImportedPKColumnType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;

/**
 *
 *
 * $Id$
 */
public class SAPconnect extends DatabaseAdapter {

    public boolean getIsClosed()
        throws SQLException {
        if (conn == null)
            return true;
        return conn.isClosed();
    }

    public int getMaxLengthStringField() {
        return 2000;
    }

    protected DataSource createDataSource() throws SQLException {
        return null;
    }

    public String getDriverClass() {
        return "com.sap.dbtech.jdbc.DriverSapDB";
    }

    public boolean getIsBatchUpdate() {
        throw new Error("not tested");
//        return true;
    }

    public boolean getIsNeedUpdateBracket() {
        return true;
    }

    public boolean getIsByteArrayInUtf8() {
        return false;
    }

    public void createTable(DbTableType table) throws Exception {
        throw new Exception("not implemented");
    }

    public void createForeignKey(DbTableType view) throws Exception {
    }

    public void dropTable(DbTableType table) throws Exception {
    }

    public void dropTable(String nameTable) throws Exception {
    }

    public void dropSequence(String nameSequence) throws Exception {
    }

    public void dropConstraint(DbImportedPKColumnType impPk) throws Exception {
    }

    public void addColumn(DbTableType table, DbFieldType field) throws Exception {
    }

    public String getNameDateBind() {
        return "?";
    }

    public String getOnDeleteSetNull() {
        return null;
    }

    public void bindDate(PreparedStatement ps, int idx, Timestamp stamp) throws SQLException {
        ps.setTimestamp(idx, stamp);
    }

    public String getDefaultTimestampValue() {
        return "SYSDATE";
    }

    public void setDefaultValue(DbTableType originTable, DbFieldType originField) {
    }

    public List getViewList(String schemaPattern, String tablePattern) throws Exception {
        return DatabaseManager.getViewList(conn, schemaPattern, tablePattern);
    }

    public ArrayList getSequnceList(String schemaPattern) throws Exception {
        return null;
    }

    public String getViewText(DbViewType view) throws Exception {
        return null;
    }

    public void createView(DbViewType view)
        throws Exception {
        if (view == null ||
            view.getName() == null || view.getName().length() == 0 ||
            view.getText() == null || view.getText().length() == 0
        )
            return;

        String sql_ = "create VIEW " + view.getName() + " as " + view.getText();
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql_);
            ps.executeUpdate();
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void createSequence(DbSequenceType seq) throws Exception {
    }

    public void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException {
        ps.setNull(index, Types.LONGVARBINARY);
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException {
        ps.setNull(index, Types.LONGVARCHAR);
    }

    public String getClobField(ResultSet rs, String nameField)
        throws SQLException {
        return getClobField(rs, nameField, 20000);
    }

    public String getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception {
        return null;
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength)
        throws SQLException {
        return "";
/*
	CLOB clob = ((OracleResultSet)rs).getCLOB (nameField);

	if (clob==null)
		return null;

	return clob.getSubString(1, maxLength);
*/
    }

    public long getSequenceNextValue(String s)
        throws SQLException {
        long id_ = -1;

        String sql_ =
            "select " + s.trim() + ".nextval from dual";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = this.conn.prepareStatement(sql_);

            rs = ps.executeQuery();

            if (rs.next())
                id_ = rs.getLong(1);

        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        return id_;
    }

    /**
     * ���������� �������� ��������(������������������) ��� ������� ����� ������������������.
     * ��� ������ ��������� � ������ ����� ������ ����� ���� ������ �� �������.
     *
     * @param sequence - String. ��� ����������������� ��� ��������� ���������� ��������.
     * @return long - ��������� �������� ��� ����� �� ������������������
     * @throws SQLException
     */
    public long getSequenceNextValue(CustomSequenceType sequence)
        throws SQLException {
        throw new SQLException("not implemented");
//        return 0;
    }

    public boolean testExceptionTableNotFound(Exception e) {

        if ((e instanceof SQLException) &&
            (e.toString().indexOf("ORA-00942") != -1))
            return true;
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e, String index) {
        if ((e instanceof SQLException) &&
            ((e.toString().indexOf("ORA-00001") != -1) &&
            (e.toString().indexOf(index) != -1)))

            return true;

        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e) {
        return false;
    }

    public boolean testExceptionTableExists(Exception e) {
        return false;
    }

    public boolean testExceptionViewExists(Exception e) {
        return false;
    }

    public boolean testExceptionSequenceExists(Exception e) {
        return false;
    }

    public boolean testExceptionConstraintExists(Exception e) {
        return false;
    }

    public int getFamaly() {
        return DatabaseManager.SAPDB_FAMALY;
    }

    public int getVersion() {
        return 7;
    }

    public int getSubVersion() {
        return 3;
    }

    public SAPconnect() {
        super();
    }
}
