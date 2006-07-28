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

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.structure.DbDataFieldDataType;
import org.riverock.generic.schema.db.structure.DbFieldType;
import org.riverock.generic.schema.db.structure.DbImportedPKColumnType;
import org.riverock.generic.schema.db.structure.DbPrimaryKeyColumnType;
import org.riverock.generic.schema.db.structure.DbPrimaryKeyType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;

/**
 * IBM DB2 connection
 * $Author$
 *
 * $Id$
 *
 */
public class IBMDB2connect extends DatabaseAdapter {
    private static Logger log = Logger.getLogger(IBMDB2connect.class);

    public int getFamaly() {
        return DatabaseManager.DB2_FAMALY;
    }

    public int getVersion() {
        return 8;
    }

    public int getSubVersion() {
        return 0;
    }

    public IBMDB2connect() {
        super();
    }

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
        return "com.ibm.db2.jcc.DB2Driver";
    }

    public boolean getIsBatchUpdate() {
        return true;
    }

    public boolean getIsNeedUpdateBracket() {
        return false;
    }

    public boolean getIsByteArrayInUtf8() {
        return false;
    }

    public String getClobField(ResultSet rs, String nameField)
        throws SQLException {
        return getClobField(rs, nameField, 20000);
    }

    public String getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception {
        return null;
    }

    public void createTable(DbTableType table) throws Exception {
        if (table == null || table.getFieldsCount() == 0)
            return;

        String sql = "create table \"" + table.getName() + "\" " +
            "(";

        boolean isFirst = true;

        for (int i = 0; i < table.getFieldsCount(); i++) {
            DbFieldType field = table.getFields(i);
            if (!isFirst)
                sql += ",";
            else
                isFirst = !isFirst;

            sql += " \"" + field.getName() + "\"";
            switch (field.getJavaType().intValue()) {

                case Types.NUMERIC:
                case Types.DECIMAL:
//                    if (true) throw new Exception("check count digit after comma");
                    sql += " DECIMAL(" + (field.getSize().intValue() > 31 ? 31 : field.getSize().intValue()) + ',' + field.getDecimalDigit() + ")";
                    break;

                case Types.INTEGER:
                    sql += " INTEGER";
                    break;

                case Types.DOUBLE:
                    sql += " DOUBLE";
                    break;

                case Types.CHAR:
                    sql += " VARCHAR(1)";
                    break;

                case Types.VARCHAR:
                    sql += " VARCHAR(" + field.getSize() + ")";
                    break;

                case Types.TIMESTAMP:
                case Types.DATE:
                    sql += " TIMESTAMP";
                    break;

                case Types.LONGVARCHAR:
                    // Oracle 'long' fields type
                    sql += " VARCHAR(10)";
                    break;

                case Types.LONGVARBINARY:
                    // Oracle 'long raw' fields type
                    sql += " LONGVARBINARY";
                    break;

                default:
                    field.setJavaStringType("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
                    System.out.println("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
            }

            if (field.getDefaultValue() != null) {
                String val = field.getDefaultValue().trim();

//                if (!val.equalsIgnoreCase("null"))
//                    val = "'"+val+"'";
                if (DatabaseManager.checkDefaultTimestamp(val))
                    val = "CURRENT TIMESTAMP";

                sql += (" DEFAULT " + val);
            }

            if (field.getNullable().intValue() == DatabaseMetaData.columnNoNulls) {
                sql += " NOT NULL ";
            }
        }

        if (table.getPrimaryKey() != null && table.getPrimaryKey().getColumnsCount() != 0) {
            DbPrimaryKeyType pk = table.getPrimaryKey();

            String namePk = pk.getColumns(0).getPkName();
//            if (namePk.length()>16)
//            {
//                System.out.println( "Name of PK is too long - "+namePk );
//                namePk = StringTools.truncateString(namePk, 15)+"_PK";
//                System.out.println( "New name of PK is too long - "+namePk );
//            }

            sql += ", CONSTRAINT " + namePk + " PRIMARY KEY ( ";

            int seq = Integer.MIN_VALUE;
            isFirst = true;
            for (int i = 0; i < pk.getColumnsCount(); i++) {
                DbPrimaryKeyColumnType column = null;
                int seqTemp = Integer.MAX_VALUE;
                for (int k = 0; k < pk.getColumnsCount(); k++) {
                    DbPrimaryKeyColumnType columnTemp = pk.getColumns(k);
                    if (seq < columnTemp.getKeySeq().intValue() && columnTemp.getKeySeq().intValue() < seqTemp) {
                        seqTemp = columnTemp.getKeySeq().intValue();
                        column = columnTemp;
                    }
                }
                seq = column.getKeySeq().intValue();

                if (!isFirst)
                    sql += ",";
                else
                    isFirst = !isFirst;

                sql += column.getColumnName();
            }
            sql += " )";
        }

        sql += " )";

        System.out.println(sql);

        Statement ps = null;
        try {
            ps = this.conn.createStatement();
            ps.executeUpdate(sql);
            this.conn.commit();
        }
        catch (SQLException e) {
            log.error("SqlCode " + e.getErrorCode());
            log.error("state " + e.getSQLState());
            log.error("message " + e.getMessage());
            log.error("sql " + sql);
            log.error("string " + e.toString());
            throw e;
        }
        catch (Exception e) {
            log.error("Exception in dropTable()", e);
            throw e;
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }

    }

    public void createForeignKey(DbTableType view) throws Exception {
    }

    public void dropTable(DbTableType table) throws Exception {
        if (table == null)
            return;

        dropTable(table.getName());
    }

    public void dropTable(String nameTable) throws Exception {
        if (nameTable == null || nameTable.trim().length() == 0)
            return;

        String sql = "drop table " + nameTable;

//        System.out.println( sql );

        Statement ps = null;
        try {
            ps = this.conn.createStatement();
            ps.executeUpdate(sql);
        }
        catch (SQLException e) {
            log.error("code " + e.getErrorCode());
            log.error("state " + e.getSQLState());
            log.error("message " + e.getMessage());
            log.error("string " + e.toString());
            throw e;
        }
        catch (Exception e) {
            log.error("Exception in dropTable()", e);
            throw e;
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void dropSequence(String nameSequence) throws Exception {
    }

    public void dropConstraint(DbImportedPKColumnType impPk) throws Exception {
        if (impPk == null)
            return;

        String sql = "ALTER TABLE " + impPk.getPkTableName() + " DROP CONSTRAINT " + impPk.getPkName();

//        System.out.println( sql );

        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            log.error("code " + e.getErrorCode());
            log.error("state " + e.getSQLState());
            log.error("message " + e.getMessage());
            log.error("string " + e.toString());
            throw e;
        }
        catch (Exception e) {
            log.error("Exception in dropTable()", e);
            throw e;
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
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
        return "CURRENT TIMESTAMP";
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

        String sql_ =
            "CREATE VIEW " + view.getName() +
            " AS " +
            StringTools.replaceStringArray(view.getText(),
                new String[][]{{"||", "+"}, {"\n", " "}}).trim();

        Statement ps = null;
        try {
            ps = this.conn.createStatement();
            ps.execute(sql_);
//            ps.execute();
        }
        catch (SQLException e) {
            String errorString = "Error create view. Error code " + e.getErrorCode() + "\n" + sql_;
            log.error(errorString, e);
            System.out.println(errorString);
            throw e;
        }
        catch (Exception e) {
            log.error("Exception in dropTable()", e);
            throw e;
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
        ps.setNull(index, Types.VARCHAR);
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException {
        ps.setString(index, "");
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength)
        throws SQLException {
        return null;
    }
/*
            CLOB clob = ((OracleResultSet) rs).getCLOB(nameField);

            if (clob == null)
                return null;

            return clob.getSubString(1, maxLength);
        }
*/

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
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select max(" + sequence.getColumnName() + ") max_id from " + sequence.getTableName());

            rs = ps.executeQuery();

            if (rs.next())
                return rs.getLong(1) + 1;
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        return 1;
    }

    public boolean testExceptionTableNotFound(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -204)
                return true;
        }
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e, String index) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -(org.hsqldb.Trace.VIOLATION_OF_UNIQUE_INDEX))
                return true;
        }
/*
        if ((e instanceof SQLException) &&
                ((e.toString().indexOf("ORA-00001") != -1) &&
                (e.toString().indexOf(index) != -1)))

            return true;
*/
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e) {
        return false;
    }

    public boolean testExceptionTableExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -601)
                return true;
        }
        return false;
    }

    public boolean testExceptionViewExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -601)
                return true;
        }
        return false;
    }

    public boolean testExceptionSequenceExists(Exception e) {
        return false;
    }

    public boolean testExceptionConstraintExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -(org.hsqldb.Trace.CONSTRAINT_ALREADY_EXISTS))
                return true;
        }
        return false;
    }

    public void nop() {
        int i = 0;
    }
}
