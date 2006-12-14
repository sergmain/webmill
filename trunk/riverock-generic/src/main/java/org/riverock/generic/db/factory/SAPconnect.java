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
import java.sql.Blob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.DbView;
import org.riverock.generic.annotation.schema.db.DbField;
import org.riverock.generic.annotation.schema.db.DbTable;
import org.riverock.generic.annotation.schema.db.DbSequence;
import org.riverock.generic.annotation.schema.db.DbImportedPKColumn;
import org.riverock.generic.annotation.schema.db.DbDataFieldData;
import org.riverock.generic.annotation.schema.db.CustomSequence;

/**
 * $Id$
 */
public class SAPconnect extends DatabaseAdapter {

    public int getMaxLengthStringField() {
        return 2000;
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

    public void createTable(DbTable table) throws Exception {
        throw new Exception("not implemented");
    }

    public void createForeignKey(DbTable view) throws Exception {
    }

    public void dropTable(DbTable table) throws Exception {
    }

    public void dropTable(String nameTable) throws Exception {
    }

    public void dropSequence(String nameSequence) throws Exception {
    }

    public void dropConstraint(DbImportedPKColumn impPk) throws Exception {
    }

    public void addColumn(DbTable table, DbField field) throws Exception {
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

    public void setDefaultValue(DbTable originTable, DbField originField) {
    }

    public List<DbView> getViewList(String schemaPattern, String tablePattern) throws Exception {
        return DatabaseManager.getViewList(getConnection(), schemaPattern, tablePattern);
    }

    public List<DbSequence> getSequnceList(String schemaPattern) throws Exception {
        return new ArrayList<DbSequence>();
    }

    public String getViewText(DbView view) throws Exception {
        return null;
    }

    public void createView(DbView view)
        throws Exception {
        if (view == null ||
            view.getName() == null || view.getName().length() == 0 ||
            view.getText() == null || view.getText().length() == 0
            )
            return;

        String sql_ = "create VIEW " + view.getName() + " as " + view.getText();
        PreparedStatement ps = null;
        try {
            ps = this.getConnection().prepareStatement(sql_);
            ps.executeUpdate();
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void createSequence(DbSequence seq) throws Exception {
    }

    public void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldData fieldData)
        throws SQLException {
        ps.setNull(index, Types.LONGVARBINARY);
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldData fieldData)
        throws SQLException {
        ps.setNull(index, Types.LONGVARCHAR);
    }

    public String getClobField(ResultSet rs, String nameField)
        throws SQLException {
        return getClobField(rs, nameField, 20000);
    }

    public byte[] getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception {
        Blob blob = rs.getBlob(nameField);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int count;
        byte buffer[] = new byte[1024];

        InputStream inputStream = blob.getBinaryStream();
        while ((count = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, count);
            outputStream.flush();
        }
        outputStream.close();
        return outputStream.toByteArray();
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
            ps = this.getConnection().prepareStatement(sql_);

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
    public long getSequenceNextValue(CustomSequence sequence)
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

    public int getFamily() {
        return DatabaseManager.SAPDB_FAMALY;
    }

    public int getVersion() {
        return 7;
    }

    public int getSubVersion() {
        return 3;
    }

    public SAPconnect(Connection conn) {
        super(conn);
    }
}
