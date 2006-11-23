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
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.DbViewType;
import org.riverock.generic.annotation.schema.db.DbTableType;
import org.riverock.generic.annotation.schema.db.DbFieldType;
import org.riverock.generic.annotation.schema.db.CustomSequenceType;
import org.riverock.generic.annotation.schema.db.DbPrimaryKeyType;
import org.riverock.generic.annotation.schema.db.DbPrimaryKeyColumnType;
import org.riverock.generic.annotation.schema.db.DbImportedPKColumnType;
import org.riverock.generic.annotation.schema.db.DbSequenceType;
import org.riverock.generic.annotation.schema.db.DbDataFieldDataType;


/**
 * MySQL database connect
 * $Author$
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class MYSQLconnect extends DatabaseAdapter {
    private final static Logger log = Logger.getLogger(MYSQLconnect.class);

    public int getFamily() {
        return DatabaseManager.MYSQL_FAMALY;
    }

    public int getVersion() {
        return 4;
    }

    public int getSubVersion() {
        return 0;
    }

    public MYSQLconnect() {
        super();
    }

//    public boolean isNeedUpdateBracket = false;

    public boolean getIsClosed() throws SQLException {
        return conn == null || conn.isClosed();
    }

    public int getMaxLengthStringField() {
        return 1000000;
    }

    protected DataSource createDataSource() {
        return null;
    }

    public String getDriverClass() {
        return "com.mysql.jdbc.Driver";
    }

    public boolean getIsBatchUpdate() {
        return false;
    }

    public boolean getIsNeedUpdateBracket() {
        return true;
    }

    public boolean getIsByteArrayInUtf8() {
        return false;
    }

    public String getClobField(ResultSet rs, String nameField) {
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

    public void createTable(DbTableType table) throws Exception {
        if (table == null || table.getFields().size() == 0)
            return;

        String sql = "create table " + table.getName() + "\n" +
            "(";

        boolean isFirst = true;

        for (DbFieldType field : table.getFields()) {
            if (!isFirst)
                sql += ",";
            else
                isFirst = !isFirst;

            sql += "\n" + field.getName() + "";
            int fieldType = field.getJavaType();
            switch (fieldType) {

                case Types.NUMERIC:
                case Types.DECIMAL:
                    if (field.getDecimalDigit() == null || field.getDecimalDigit() == 0)
                        sql += " DECIMAL";
                    else
                        sql += " DECIMAL(" + field.getSize() + "," + field.getDecimalDigit() + ")";
                    break;

                case Types.INTEGER:
                    sql += " INTEGER";
                    break;

                case Types.DOUBLE:
                    sql += " DOUBLE";
                    if (field.getDecimalDigit() == null || field.getDecimalDigit() == 0)
                        sql += " DOUBLE";
                    else
                        sql += " DOUBLE(" + field.getSize() + "," + field.getDecimalDigit() + ")";
                    break;

                case Types.CHAR:
                    sql += " VARCHAR(1)";
                    break;

                case Types.VARCHAR:
                    if (field.getSize() < 256)
                        sql += " VARCHAR(" + field.getSize() + ")";
                    else
                        sql += " TEXT";
                    break;

                case Types.TIMESTAMP:
                case Types.DATE:
                    sql += " TIMESTAMP";
                    break;

                case Types.LONGVARCHAR:
                    sql += " text ";
                    break;

                case Types.BLOB:
                    sql += " LONGBLOB";
                    break;

                case Types.OTHER:
                    sql += " LONGTEXT";
                    break;

                // clob not supported by mysql
                case Types.CLOB:
                default:
                    field.setJavaStringType("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
                    System.out.println("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
            }

            if (field.getDefaultValue() != null) {
                String val = field.getDefaultValue().trim();

                if (StringUtils.isNotBlank(val)) {
                    switch (fieldType) {
                        case Types.CHAR:
                        case Types.VARCHAR:
                            val = "'" + val + "'";
                            break;
                        case Types.TIMESTAMP:
                        case Types.DATE:
                            if (DatabaseManager.checkDefaultTimestamp(val))
                                val = "CURRENT_TIMESTAMP";

                            break;
                        default:
                    }
                    sql += (" DEFAULT " + val);
                }
            }

            if (field.getNullable() == DatabaseMetaData.columnNoNulls) {
                sql += " NOT NULL ";
            }
        }
        if (table.getPrimaryKey() != null && table.getPrimaryKey().getColumns().size() != 0) {
            DbPrimaryKeyType pk = table.getPrimaryKey();

//            String namePk = pk.getColumns(0).getPkName();

            // in MySQL all primary keys named as 'PRIMARY'
            sql += ",\n PRIMARY KEY (\n";

            int seq = Integer.MIN_VALUE;
            isFirst = true;
            for (int i = 0; i < pk.getColumnsCount(); i++) {
                DbPrimaryKeyColumnType column = null;
                int seqTemp = Integer.MAX_VALUE;
                for (int k = 0; k < pk.getColumnsCount(); k++) {
                    DbPrimaryKeyColumnType columnTemp = pk.getColumns(k);
                    if (seq < columnTemp.getKeySeq() && columnTemp.getKeySeq() < seqTemp) {
                        seqTemp = columnTemp.getKeySeq();
                        column = columnTemp;
                    }
                }
                seq = column.getKeySeq();

                if (!isFirst)
                    sql += ",";
                else
                    isFirst = !isFirst;

                sql += column.getColumnName();
            }
            sql += "\n)";
        }
        sql += "\n)";

        System.out.println(sql);

        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch (SQLException e) {
//            System.out.println( "code "+e.getErrorCode() );
//            System.out.println( "state "+e.getSQLState() );
//            System.out.println( "message "+e.getMessage() );
//            System.out.println( "string "+e.toString() );
            throw e;
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }

    }

    public void dropTable(DbTableType table) throws Exception {
        dropTable(table.getName());
    }

    public void dropTable(String nameTable) throws Exception {
        if (nameTable == null)
            return;

        String sql = "drop table " + nameTable;

//        System.out.println( sql );

        Statement ps = null;
        try {
//            stmt = conn.createStatement();
//            stmt.executeUpdate("DROP TABLE statement_test");

            ps = this.conn.createStatement();
            ps.executeUpdate(sql);

//            ps = this.conn.prepareStatement(sql);
//            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("code " + e.getErrorCode());
            System.out.println("state " + e.getSQLState());
            System.out.println("message " + e.getMessage());
            System.out.println("string " + e.toString());
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
//            System.out.println( "code "+e.getErrorCode() );
//            System.out.println( "state "+e.getSQLState() );
//            System.out.println( "message "+e.getMessage() );
//            System.out.println( "string "+e.toString() );
            throw e;
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void addColumn(DbTableType table, DbFieldType field) throws Exception {
        String sql = "alter table " + table.getName() + " add " + field.getName() + " ";

        int fieldType = field.getJavaType();
        switch (fieldType) {

            case Types.NUMERIC:
            case Types.DECIMAL:
                sql += " DECIMAL";
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
                if (field.getSize() < 256)
                    sql += " VARCHAR(" + field.getSize() + ")";
                else
                    sql += " TEXT";
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
                sql += " LONGVARBINARY";
                break;

            case Types.BLOB:
                sql += " LONGBLOB";
            
            default:
                field.setJavaStringType("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
                System.out.println("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
        }

        if (field.getDefaultValue() != null) {
            String val = field.getDefaultValue().trim();

            if (StringUtils.isNotBlank(val)) {
                switch (fieldType) {
                    case Types.CHAR:
                    case Types.VARCHAR:
                        val = "'" + val + "'";
                        break;
                    case Types.TIMESTAMP:
                    case Types.DATE:
                        if (DatabaseManager.checkDefaultTimestamp(val))
                            val = "'CURRENT_TIMESTAMP'";

                        break;
                    default:
                }
                sql += (" DEFAULT " + val);
            }
        }

        if (field.getNullable() == DatabaseMetaData.columnNoNulls) {
            sql += " NOT NULL ";
        }

        if (log.isDebugEnabled())
            log.debug("MySql addColumn sql - \n" + sql);

        Statement ps = null;
        try {
            ps = this.conn.createStatement();
            ps.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public String getNameDateBind() {
        return "?";
    }

    public String getOnDeleteSetNull() {
        return "";
    }

    /**
     * bind Timestamp value
     *
     * @param ps
     * @param stamp @see java.sql.Timestamp
     * @throws SQLException
     */
    public void bindDate(PreparedStatement ps, int idx, Timestamp stamp) throws SQLException {
        ps.setTimestamp(idx, stamp);
    }

    public String getDefaultTimestampValue() {
        return "'CURRENT_TIMESTAMP'";
    }

    public void setDefaultValue(DbTableType originTable, DbFieldType originField) {
    }

    public List<DbViewType> getViewList(String schemaPattern, String tablePattern) throws Exception {
        // version 3.x and 4.0 of MySQL not support view
        return new ArrayList<DbViewType>(0);
    }

    public ArrayList getSequnceList(String schemaPattern) throws Exception {
        return null;
    }

    public String getViewText(DbViewType view) throws Exception {
        return null;
    }

    public void createView(DbViewType view) throws Exception {
        // current version of MySql not supported view
/*
        if (view == null ||
            view.getName() == null || view.getName().length() == 0 ||
            view.getText() == null || view.getText().length() == 0
        )
            return;

        String sql_ =
            "CREATE VIEW " + view.getName() +
            " AS " + StringUtils.replace(view.getText(), "||", "+");

        Statement ps = null;
        try {
            ps = this.conn.createStatement();
            ps.execute(sql_);
        }
        catch (SQLException e) {
            String errorString = "Error create view. Error code " + e.getErrorCode() + "\n" + sql_;
            log.error(errorString, e);
            System.out.println(errorString);
            throw e;
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
*/
    }

    public void createSequence(DbSequenceType seq) throws Exception {
    }

    public void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException {
        ps.setNull(index, Types.VARCHAR);
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException {
        ps.setString(index, fieldData.getStringData());
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength) {
        return null;
    }
/*
            CLOB clob = ((OracleResultSet) rs).getCLOB(nameField);

            if (clob == null)
                return null;

            return clob.getSubString(1, maxLength);
        }
*/

    /**
     * Возвращает значение сиквенса(последовательности) для данного имени последовательности.
     * Для разных коннектов к разным базам данных может быть решена по разному.
     *
     * @param sequence - String. Имя последовательноти для получения следующего значения.
     * @return long - следующее значение для ключа из последовательности
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
            SQLException exception = (SQLException) e;
            log.error("Error code: " + exception.getErrorCode());
            log.error("getSQLState : " + exception.getSQLState());
            if (exception.getErrorCode() == 1146) {
                return true;
            }
        }
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e, String index) {
        return testExceptionIndexUniqueKey(e);
    }

    public boolean testExceptionIndexUniqueKey(Exception e) {
        if (e instanceof SQLException) {
            SQLException exception = (SQLException) e;
            log.error("Error code: " + exception.getErrorCode());
            log.error("getSQLState : " + exception.getSQLState());
            if (exception.getErrorCode() == 1062) {
                return true;
            }
        }
        return false;
    }

    public boolean testExceptionTableExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == 1050)
                return true;
        }
        return false;
    }

    public boolean testExceptionViewExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == 2714)
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
}