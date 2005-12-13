/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
// HyperSonic database connect
package org.riverock.generic.db.factory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.structure.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Calendar;
import java.util.ArrayList;

import javax.sql.DataSource;

public class HSQLconnect extends DatabaseAdapter
{
    private static Logger log = Logger.getLogger( "org.riverock.generic.db.factory.HSQLconnect" );

    public int getFamaly()
    {
        return DatabaseManager.HSQLDB_FAMALY;
    }

    public int getVersion()
    {
        return 1;
    }

    public int getSubVersion()
    {
        return 7;
    }

    public HSQLconnect()
    {
        super();
    }

//    public boolean isNeedUpdateBracket = false;

    public boolean getIsClosed()
            throws SQLException
    {
        if (conn == null)
            return true;
        return conn.isClosed();
    }

    public int getMaxLengthStringField()
    {
        return 1000000;
    }

    protected DataSource createDataSource() throws SQLException {
        return null;
    }

    public String getDriverClass() {
        return "org.hsqldb.jdbcDriver";
    }

    protected void finalize() throws Throwable
    {
        if (isDynamicConnect)
        {
            try
            {
                conn.close();
                conn = null;
            }
            catch (Exception e)
            {
            }
        }
        super.finalize();
    }

    public boolean getIsBatchUpdate()
    {
        return false;
    }

    public boolean getIsNeedUpdateBracket()
    {
        return false;
    }

    public boolean getIsByteArrayInUtf8()
    {
        return false;
    }

    public String getClobField(ResultSet rs, String nameField)
            throws SQLException
    {
        return getClobField(rs, nameField, 20000);
    }

    public String getBlobField( ResultSet rs, String nameField, int maxLength ) throws Exception
    {
        return null;
    }

    public void createTable(DbTableType table) throws Exception
    {
        if (table==null || table.getFieldsCount()==0)
            return;

        String sql = "create table \"" + table.getName() + "\"\n"+
                "(";

        boolean isFirst = true;

        for (int i=0; i<table.getFieldsCount(); i++)
        {
            DbFieldType field = table.getFields(i);
            if (!isFirst)
                sql += ",";
            else
                isFirst = !isFirst;

            sql += "\n\"" + field.getName() + "\"";
            switch ( field.getJavaType().intValue() )
            {

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
                case Types.VARCHAR:
                    sql += " VARCHAR";
                    break;

                case Types.DATE:
                case Types.TIMESTAMP:
                    sql += " TIMESTAMP";
                    break;

                case Types.LONGVARCHAR:
                    // Oracle 'long' fields type
                    sql += " LONGVARCHAR";
                    break;

                case Types.LONGVARBINARY:
                    // Oracle 'long raw' fields type
                    sql += " LONGVARBINARY";
                    break;

                default:
                    field.setJavaStringType( "unknown field type field - "+field.getName()+" javaType - " + field.getJavaType() );
                    System.out.println( "unknown field type field - "+field.getName()+" javaType - " + field.getJavaType() );
            }

            if (field.getDefaultValue() != null)
            {
                String val = field.getDefaultValue().trim();

//                if (!val.equalsIgnoreCase("null"))
//                    val = "'"+val+"'";

                sql += (" DEFAULT "+val);
            }

            if (field.getNullable().intValue() == DatabaseMetaData.columnNoNulls )
            {
                sql += " NOT NULL ";
            }
        }
        if ( table.getPrimaryKey()!=null && table.getPrimaryKey().getColumnsCount() !=0 )
        {
            DbPrimaryKeyType pk = table.getPrimaryKey();

            String namePk = pk.getColumns(0).getPkName();

//            constraintDefinition:
//            [ CONSTRAINT name ]
//            UNIQUE ( column [,column...] ) |
//            PRIMARY KEY ( column [,column...] ) |

            sql += ",\nCONSTRAINT "+namePk+" PRIMARY KEY (\n";

            int seq = Integer.MIN_VALUE;
            isFirst = true;
            for ( int i=0; i<pk.getColumnsCount();i++ )
            {
                DbPrimaryKeyColumnType column = null;
                int seqTemp = Integer.MAX_VALUE;
                for ( int k=0; k<pk.getColumnsCount(); k++ )
                {
                    DbPrimaryKeyColumnType columnTemp = pk.getColumns(k);
                    if (seq < columnTemp.getKeySeq().intValue() && columnTemp.getKeySeq().intValue() < seqTemp )
                    {
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

        Statement ps = null;
        try
        {
            ps = this.conn.createStatement();
            ps.execute( sql );
        }
        catch(SQLException e)
        {
//            System.out.println( "code "+e.getErrorCode() );
//            System.out.println( "state "+e.getSQLState() );
//            System.out.println( "message "+e.getMessage() );
//            System.out.println( "string "+e.toString() );
            throw e;
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }

    }

    public void dropTable(DbTableType table) throws Exception
    {
        dropTable(table.getName());
    }

    public void dropTable(String nameTable) throws Exception
    {
        if (nameTable==null)
            return;

        String sql = "drop table \"" + nameTable + "\"\n";

//        System.out.println( sql );

//        Statement ps = null;
//        try
//        {
//            ps = this.conn.createStatement();
//            ps.execute( sql );
//        }
        PreparedStatement ps = null;
        try
        {
            ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
//            System.out.println( "code "+e.getErrorCode() );
//            System.out.println( "state "+e.getSQLState() );
//            System.out.println( "message "+e.getMessage() );
//            System.out.println( "string "+e.toString() );
            throw e;
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void dropSequence( String nameSequence ) throws Exception
    {
    }

    public void dropConstraint(DbImportedPKColumnType impPk) throws Exception
    {
        if ( impPk == null )
            return;

        String sql = "ALTER TABLE "+impPk.getPkTableName()+" DROP CONSTRAINT "+impPk.getPkName();

//        System.out.println( sql );

        PreparedStatement ps = null;
        try
        {
            ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
//            System.out.println( "code "+e.getErrorCode() );
//            System.out.println( "state "+e.getSQLState() );
//            System.out.println( "message "+e.getMessage() );
//            System.out.println( "string "+e.toString() );
            throw e;
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void addColumn(DbTableType table, DbFieldType field) throws Exception
    {
        String sql = "alter table \""+table.getName()+"\" add column "+field.getName()+" ";

        switch ( field.getJavaType().intValue() )
        {

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
            case Types.VARCHAR:
                sql += " VARCHAR";
                break;

            case Types.TIMESTAMP:
            case Types.DATE:
                sql += " TIMESTAMP";
                break;

            case Types.LONGVARCHAR:
                // Oracle 'long' fields type
                sql += " LONGVARCHAR";
                break;

            case Types.LONGVARBINARY:
                // Oracle 'long raw' fields type
                sql += " LONGVARBINARY";
                break;

            default:
                String errorString  = "unknown field type field - "+field.getName()+" javaType - " + field.getJavaType();
                log.error( errorString );
                throw new Exception( errorString );
        }

        if (field.getDefaultValue() != null)
        {
            String val = field.getDefaultValue().trim();

//                if (!val.equalsIgnoreCase("null"))
//                    val = "'"+val+"'";

            sql += (" DEFAULT "+val);
        }

        if (field.getNullable().intValue() == DatabaseMetaData.columnNoNulls )
        {
            sql += " NOT NULL ";
        }


        if (log.isDebugEnabled())
            log.debug("addColumn sql - \n"+sql);

        PreparedStatement ps = null;
        try
        {
            ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

    public String getNameDateBind()
    {
        return "?";
    }

    public String getOnDeleteSetNull()
    {
        return "";
    }

    public void bindDate( PreparedStatement ps, int idx, Timestamp stamp ) throws SQLException
    {
        ps.setTimestamp(idx, stamp);
    }

    public String getDefaultTimestampValue()
    {
        return "current_timestamp";
    }

    public void setDefaultValue( DbTableType originTable, DbFieldType originField )
    {
    }

    public ArrayList getViewList(String schemaPattern, String tablePattern) throws Exception
    {
        return DatabaseManager.getViewList(conn, schemaPattern, tablePattern);
    }

    public ArrayList getSequnceList( String schemaPattern ) throws Exception
    {
        return null;
    }

    public String getViewText(DbViewType view) throws Exception
    {
        return null;
    }

    public void createView(DbViewType view)
            throws Exception
    {
        if ( view == null ||
                view.getName()==null || view.getName().length()==0 ||
                view.getText()==null || view.getText().length()==0
        )
            return;

        String sql_ = "create VIEW "+view.getName()+" as " + view.getText();
        PreparedStatement ps = null;
        try
        {
            ps = this.conn.prepareStatement(sql_);
            ps.executeUpdate();
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close( ps );
            ps = null;
        }
    }

    public void createSequence( DbSequenceType seq ) throws Exception
    {
    }

    public void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
            throws SQLException
    {
        ps.setNull(index, Types.LONGVARBINARY);
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
            throws SQLException
    {
        ps.setNull(index, Types.LONGVARCHAR);
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength)
                throws SQLException
        {return null; }
/*
            CLOB clob = ((OracleResultSet) rs).getCLOB(nameField);

            if (clob == null)
                return null;

            return clob.getSubString(1, maxLength);
        }
*/

    public long getSequenceNextValue(String s)
            throws SQLException
    {
        throw new SQLException("public long getSequenceNextValue(String s) mot supported");
    }

    /**
     * ���������� �������� ��������(������������������) ��� ������� ����� ������������������.
     * ��� ������ ��������� � ������ ����� ������ ����� ���� ������ �� �������.
     * @param sequence - String. ��� ����������������� ��� ��������� ���������� ��������.
     * @return long - ��������� �������� ��� ����� �� ������������������
     * @throws SQLException
     */
    public long getSequenceNextValue(CustomSequenceType sequence)
        throws SQLException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement(
                "select max("+sequence.getColumnName()+") max_id from "+sequence.getTableName() );

            rs = ps.executeQuery();

            if (rs.next())
                return rs.getLong(1)+1;
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        return 1;
    }

    public String getFirstValueString(String t, String f, String w, String o)
            throws SQLException
    {

        String id_ = null;

        String v_s = "select " + f + " from " + t;

        if (o != null)
        {
            v_s += (w == null)? "": " " + w;
            v_s += (" order by " + o);
        }
        else
        {
            v_s += (w == null)?
                    "":
                    " " + w + " and rownum<2 ";
        }

//		db.aM(v_s);


        PreparedStatement prepStatement = null;
        ResultSet rset = null;
        try
        {
            prepStatement = this.conn.prepareStatement(v_s);

            rset = prepStatement.executeQuery();

            if (rset.next())
                id_ = rset.getString(1);

        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(rset, prepStatement);
            rset = null;
            prepStatement = null;
        }

        return id_;

//		return fromDB(id_);
    }

    public Calendar getFirstValueCalendar(String t, String f, String w, String o)
            throws SQLException
    {
        Calendar id_ = null;

        String v_s = "select " + f + " from " + t;

        if (o != null)
        {
            v_s += (w == null)? "": " " + w;
            v_s += (" order by " + o);
        }
        else
        {
            v_s += (w == null)?
                    "":
                    " " + w + " and rownum<2 ";
        }

//		db.aM(v_s);


        PreparedStatement prepStatement = null;
        ResultSet rset = null;
        try
        {
            prepStatement = this.conn.prepareStatement(v_s);

            rset = prepStatement.executeQuery();

            if (rset.next())
            {
                String columnName = RsetTools.getColumnName(rset, 1);
                id_ = RsetTools.getCalendar(rset, columnName);
            }

        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(rset, prepStatement);
            rset = null;
            prepStatement = null;
        }

        return id_;
    }

    public long getFirstValue(String t, String f, String w, String o)
            throws SQLException
    {

        long id_ = -1;

        String v_s = "select " + f + " from " + t;

        if (o != null)
        {
            v_s += (w == null)? "": " " + w;
            v_s += (" order by " + o);
        }
        else
        {
            v_s += (w == null)?
                    "":
                    " " + w + " and rownum<2 ";
        }

//		db.aM(v_s);


        PreparedStatement prepStatement = null;
        ResultSet rset = null;
        try
        {
            prepStatement = this.conn.prepareStatement(v_s);

            rset = prepStatement.executeQuery();

            if (rset.next())
                id_ = rset.getLong(1);
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(rset, prepStatement);
            rset = null;
            prepStatement = null;
        }

        return id_;
    }

    public Long getFirstLongValue(String t, String f, String w, String o) throws SQLException {
        throw new SQLException("not implemented");  
    }

    public boolean testExceptionTableNotFound(Exception e)
    {
        if (e==null)
            return false;

        if (e instanceof SQLException)
        {
            if (((SQLException)e).getErrorCode() == -(org.hsqldb.Trace.TABLE_NOT_FOUND))
                return true;
        }
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e, String index)
    {
        if (e==null)
            return false;

        if (e instanceof SQLException)
        {
            if (((SQLException)e).getErrorCode() == -(org.hsqldb.Trace.VIOLATION_OF_UNIQUE_INDEX) &&
                (e.toString().indexOf(index) != -1))
                return true;
        }
        return false;
    }

    public boolean testExceptionIndexUniqueKey( Exception e )
    {
        if (e==null)
            return false;

        if (e instanceof SQLException)
        {
            if (((SQLException)e).getErrorCode() == -(org.hsqldb.Trace.VIOLATION_OF_UNIQUE_INDEX))
                return true;
        }
        return false;
    }

    public boolean testExceptionTableExists(Exception e)
    {
        if (e==null)
            return false;

        if (e instanceof SQLException)
        {
            if (((SQLException)e).getErrorCode() == -(org.hsqldb.Trace.TABLE_ALREADY_EXISTS))
                return true;
        }
        return false;
    }

    public boolean testExceptionViewExists(Exception e)
    {
        if (e==null)
            return false;

        if (e instanceof SQLException)
        {
            if (((SQLException)e).getErrorCode() == -(org.hsqldb.Trace.VIEW_ALREADY_EXISTS))
                return true;
        }
        return false;
    }

    public boolean testExceptionSequenceExists( Exception e )
    {
        return false;
    }

    public boolean testExceptionConstraintExists(Exception e)
    {
        if (e==null)
            return false;

        if (e instanceof SQLException)
        {
            if (((SQLException)e).getErrorCode() == -(org.hsqldb.Trace.CONSTRAINT_ALREADY_EXISTS))
                return true;
        }
        return false;
    }

    public void nop()
    {
        int i = 0;
    }
}