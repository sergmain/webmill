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
import java.util.Locale;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.DateTools;
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
import org.riverock.generic.tools.CurrentTimeZone;

/**
 * InterBase database connect 
 * $Author$
 *
 * $Id$
 *
 */
public class IBconnect extends DatabaseAdapter {
    private static Logger log = Logger.getLogger( IBconnect.class );

    public int getFamily() {
        return DatabaseManager.INTERBASE_FAMALY;
    }

    public int getVersion() {
        return 6;
    }

    public int getSubVersion() {
        return 0;
    }

    public IBconnect() {
        super();
    }

    public boolean getIsClosed()
        throws SQLException {
        if (conn == null)
            return true;
        return conn.isClosed();
    }

    public int getMaxLengthStringField() {
        return 4000;
    }

    protected DataSource createDataSource() throws SQLException {
        return null;
    }

    public String getDriverClass() {
        return "org.firebirdsql.jdbc.FBDriver";
    }

    public boolean getIsBatchUpdate() {
        return false;
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

    public byte[] getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception {
        return null;
    }

    public void createTable(DbTableType table) throws Exception {
        if (table == null || table.getFieldsCount() == 0)
            return;

        String sql = "create table " + table.getName() + "\n" +
            "(";

        boolean isFirst = true;

        for (int i = 0; i < table.getFieldsCount(); i++) {
            DbFieldType field = table.getFields(i);
            if (!isFirst)
                sql += ",";
            else
                isFirst = !isFirst;

            sql += "\n" + field.getName();
            switch (field.getJavaType().intValue()) {

                case Types.NUMERIC:
                case Types.DECIMAL:
                    if (field.getDecimalDigit().intValue() != 0)
                        sql += " DECIMAL(" + (field.getSize().intValue() > 38 ? 38 : field.getSize().intValue()) + ',' + field.getDecimalDigit() + ")";
                    else {
                        if (field.getSize().intValue() == 1)
                            sql += " SMALLINT";
                        else
                            sql += " DOUBLE PRECISION";
                    }
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
                    sql += " DATETIME";
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
                    val = "current_timestamp";

                sql += (" DEFAULT " + val);
            }

            if (field.getNullable().intValue() == DatabaseMetaData.columnNoNulls) {
                sql += " NOT NULL ";
            }
        }
        if (table.getPrimaryKey() != null && table.getPrimaryKey().getColumnsCount() != 0) {
            DbPrimaryKeyType pk = table.getPrimaryKey();

            String namePk = pk.getColumns(0).getPkName();

//            constraintDefinition:
//            [ CONSTRAINT name ]
//            UNIQUE ( column [,column...] ) |
//            PRIMARY KEY ( column [,column...] ) |

            sql += ",\nCONSTRAINT " + namePk + " PRIMARY KEY (\n";

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
            sql += "\n)";
        }
        sql += "\n)";

//        System.out.println( sql );

        Statement st = null;
        try {
            st = this.conn.createStatement();
            st.execute(sql);
            int count = st.getUpdateCount();
            if (log.isDebugEnabled())
                log.debug("count of processed records " + count);
        }
        catch (SQLException e) {
            log.error("code " + e.getErrorCode());
            log.error("state " + e.getSQLState());
            log.error("sql " + sql);
            log.error("message " + e.getMessage());
            log.error("string ", e);
            throw e;
        }
        finally {
            DatabaseManager.close(st);
            st = null;
        }

    }

    public void dropTable(DbTableType table) throws Exception {
        dropTable(table.getName());
    }

    public void dropTable(String nameTable) throws Exception {
        if (nameTable == null)
            return;

        String sql = "drop table " + nameTable;

        Statement st = null;
        try {
            st = this.conn.createStatement();
            st.execute(sql);
            int count = st.getUpdateCount();
            if (log.isDebugEnabled())
                log.debug("count of deleted object " + count);
        }
        catch (SQLException e) {
            log.error("Error drop table " + nameTable, e);
//            System.out.println( "code "+e.getErrorCode() );
//            System.out.println( "state "+e.getSQLState() );
//            System.out.println( "message "+e.getMessage() );
//            System.out.println( "string "+e.toString() );
            throw e;
        }
        finally {
            DatabaseManager.close(st);
            st = null;
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
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void addColumn(DbTableType table, DbFieldType field) throws Exception {
        String sql = "alter table " + table.getName() + " add " + field.getName() + " ";

        switch (field.getJavaType().intValue()) {

            case Types.NUMERIC:
            case Types.DECIMAL:
                sql += " DOUBLE PRECISION";
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
                sql += (" VARCHAR(" + field.getSize() + ") ");
                break;

            case Types.TIMESTAMP:
            case Types.DATE:
                sql += " DATETIME";
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
                val = "current_timestamp";

            sql += (" DEFAULT " + val);
        }

        if (field.getNullable().intValue() == DatabaseMetaData.columnNoNulls) {
            sql += " NOT NULL ";
        }

        if (log.isDebugEnabled())
            log.debug("Interbase addColumn sql - \n" + sql);

        Statement ps = null;
        try {
            ps = this.conn.createStatement();
            ps.executeUpdate(sql);
            this.conn.commit();
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public String getOnDeleteSetNull() {
        return "ON DELETE NO ACTION";
    }

    /**
     * bind Timestamp value
     *
     * @param ps
     * @param stamp @see java.sql.Timestamp
     * @throws SQLException
     */
    public void bindDate(PreparedStatement ps, int idx, Timestamp stamp)
        throws SQLException {

        try {
            String stringTimestamp = null;
            DateTools.getStringDate(stamp, "yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH, CurrentTimeZone.getTZ());
            ps.setString(idx, stringTimestamp);
        }
        catch (ConfigException exc) {
            String es = "Exception in CurrentTimeZone.getTZ()";
            log.error(es, exc);
            throw new SQLException(es);
        }
        ;

    }

    /**
     * in some DB setTimestamp not work and we need work around
     *
     * @return String
     */
    public String getNameDateBind() {
        return "cast(? as datetime)";
    }

    public String getDefaultTimestampValue() {
        return "current_timestamp";
    }

    public void setDefaultValue(DbTableType originTable, DbFieldType originField) {
    }
/*
ALTER TABLE table
{ [ ALTER COLUMN column_name
    { new_data_type [ ( precision [ , scale ] ) ]
        [ COLLATE < collation_name > ]
        [ NULL | NOT NULL ]
        | {ADD | DROP } ROWGUIDCOL }
    ]
    | ADD
        { [ < column_definition > ]
        |  column_name AS computed_column_expression
        } [ ,...n ]
    | [ WITH CHECK | WITH NOCHECK ] ADD
        { < table_constraint > } [ ,...n ]
    | DROP
        { [ CONSTRAINT ] constraint_name
            | COLUMN column } [ ,...n ]
    | { CHECK | NOCHECK } CONSTRAINT
        { ALL | constraint_name [ ,...n ] }
    | { ENABLE | DISABLE } TRIGGER
        { ALL | trigger_name [ ,...n ] }
}

< column_definition > ::=
    { column_name data_type }
    [ [ DEFAULT constant_expression ] [ WITH VALUES ]
    | [ IDENTITY [ (seed , increment ) [ NOT FOR REPLICATION ] ] ]
        ]
    [ ROWGUIDCOL ]
    [ COLLATE < collation_name > ]
    [ < column_constraint > ] [ ...n ]

< column_constraint > ::=
    [ CONSTRAINT constraint_name ]
    { [ NULL | NOT NULL ]
        | [ { PRIMARY KEY | UNIQUE }
            [ CLUSTERED | NONCLUSTERED ]
            [ WITH FILLFACTOR = fillfactor ]
            [ ON { filegroup | DEFAULT } ]
            ]
        | [ [ FOREIGN KEY ]
            REFERENCES ref_table [ ( ref_column ) ]
            [ ON DELETE { CASCADE | NO ACTION } ]
            [ ON UPDATE { CASCADE | NO ACTION } ]
            [ NOT FOR REPLICATION ]
            ]
        | CHECK [ NOT FOR REPLICATION ]
            ( logical_expression )
    }

< table_constraint > ::=
    [ CONSTRAINT constraint_name ]
    { [ { PRIMARY KEY | UNIQUE }
        [ CLUSTERED | NONCLUSTERED ]
        { ( column [ ,...n ] ) }
        [ WITH FILLFACTOR = fillfactor ]
        [ ON {filegroup | DEFAULT } ]
        ]
        |    FOREIGN KEY
            [ ( column [ ,...n ] ) ]
            REFERENCES ref_table [ ( ref_column [ ,...n ] ) ]
            [ ON DELETE { CASCADE | NO ACTION } ]
            [ ON UPDATE { CASCADE | NO ACTION } ]
            [ NOT FOR REPLICATION ]
        | DEFAULT constant_expression
            [ FOR column ] [ WITH VALUES ]
        |    CHECK [ NOT FOR REPLICATION ]
            ( search_conditions )
    }

*/

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
            " AS " + StringUtils.replace(view.getText(), "||", "+");

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
        if (((SQLException) e).getErrorCode() == 208)
            return true;
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
            if (((SQLException) e).getErrorCode() == 335544351)
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