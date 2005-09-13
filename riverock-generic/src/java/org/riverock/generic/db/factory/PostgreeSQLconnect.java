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
 * Класс ORAconnect прденазначен для коннекта к оракловской базе данных.
 *
 * $Id$
 */

package org.riverock.generic.db.factory;

import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sql.DataSource;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
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

public class PostgreeSQLconnect extends DatabaseAdapter
{
    private static Logger log = Logger.getLogger( PostgreeSQLconnect.class );

    public boolean getIsClosed()
            throws SQLException
    {
        if (conn == null)
            return true;
        return conn.isClosed();
    }

    public int getMaxLengthStringField()
    {
        return 4000;
    }

    protected DataSource createDataSource() throws SQLException {
        return null;
    }

    public String getDriverClass() {
        if (true) throw new IllegalStateException("Not implemented");
        return null;
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
        return true;
    }

    public boolean getIsNeedUpdateBracket()
    {
        return true;
    }

    public boolean getIsByteArrayInUtf8()
    {
        return true;
    }

    public String getClobField(ResultSet rs, String nameField)
            throws SQLException
    {
        return getClobField(rs, nameField, 20000);
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
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.INTEGER:
                    if (field.getDecimalDigit().intValue()==0)
                        sql += " NUMBER";
                    else
                        sql += " NUMBER("+field.getSize()+","+field.getDecimalDigit()+")";
                    break;

                case Types.CHAR:
                    sql += " VARCHAR2(1)";
                    break;

                case Types.VARCHAR:
                    if (field.getSize().intValue()<this.getMaxLengthStringField() )
                        sql += " VARCHAR2("+field.getSize()+")";
                    else
                        sql += ( " VARCHAR2("+this.getMaxLengthStringField()+")" );
                    break;

                case Types.DATE:
                case Types.TIMESTAMP:
                    sql += " DATE";
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

                if ( DatabaseManager.checkDefaultTimestamp(val) )
                    val = "SYSDATE";

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

        PreparedStatement ps = null;
        try
        {
            ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            if (!testExceptionTableExists(e))
            {
                System.out.println( "sql "+sql);
                System.out.println( "code "+e.getErrorCode() );
                System.out.println( "state "+e.getSQLState() );
                System.out.println( "message "+e.getMessage() );
                System.out.println( "string "+e.toString() );
            }
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }

    }

    /*
ALTER TABLE a_test_1
ADD CONSTRAINT a_test_1_fk FOREIGN KEY (id, id_test)
REFERENCES a_test (id_test,id_lang) ON DELETE SET NULL
/

ALTER TABLE a_test_1
ADD CONSTRAINT a_test_1_fk2 FOREIGN KEY (text1, id_text)
REFERENCES a_test_2 (text2,text_id) ON DELETE CASCADE
DEFERRABLE INITIALLY DEFERRED
/
    */

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
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public void dropSequence( String nameSequence ) throws Exception
    {
        if (nameSequence==null)
            return;

        String sql = "drop sequence  " + nameSequence;
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
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public void dropConstraint(DbImportedPKColumnType impPk) throws Exception
    {
        throw new Exception("not implemented");
    }

    public void addColumn(DbTableType table, DbFieldType field) throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("addColumn(DbTableType table, DbFieldType field)");

        String sql = "alter table "+table.getName()+" add ( "+field.getName()+" ";

        switch ( field.getJavaType().intValue() )
        {
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.INTEGER:
                if (field.getDecimalDigit().intValue()==0)
                    sql += " NUMBER";
                else
                    sql += " NUMBER("+field.getSize()+","+field.getDecimalDigit()+")";
                break;

            case Types.CHAR:
                sql += " VARCHAR2(1)";
                break;

            case Types.VARCHAR:
                if (field.getSize().intValue()<this.getMaxLengthStringField() )
                    sql += " VARCHAR2("+field.getSize()+")";
                else
                    sql += ( " VARCHAR2("+this.getMaxLengthStringField()+")" );
                break;

            case Types.DATE:
            case Types.TIMESTAMP:
                sql += " DATE";
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
            if ( DatabaseManager.checkDefaultTimestamp(val) )
                val = "current_timestamp";

            sql += (" DEFAULT "+val);
        }

        if (field.getNullable().intValue() == DatabaseMetaData.columnNoNulls )
        {
            sql += " NOT NULL ";
        }
        sql += ")";

        if (log.isDebugEnabled())
            log.debug("Oracle addColumn sql - "+sql);

        Statement ps = null;
        try
        {
            ps = this.conn.createStatement();
            ps.executeUpdate( sql );
            this.conn.commit();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    /**
     * in some DB (Oracle8.0) setTimestamp not work and we need work around
     * @return String
     */
    public String getNameDateBind()
    {
        return "?";
    }

    public String getOnDeleteSetNull()
    {
        return "ON DELETE SET NULL";
    }

    /**
     * bind Timestamp value
     * @param ps
     * @param stamp @see java.sql.Timestamp
     * @throws java.sql.SQLException
     */
    public void bindDate( PreparedStatement ps, int idx, Timestamp stamp ) throws SQLException
    {
        ps.setTimestamp(idx, stamp);
    }

    public String getDefaultTimestampValue()
    {
        return "SYSDATE";
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
        String sql_ =
            "select SEQUENCE_NAME, MIN_VALUE, TO_CHAR(MAX_VALUE) MAX_VALUE, "+
            "INCREMENT_BY, CYCLE_FLAG, ORDER_FLAG, CACHE_SIZE, LAST_NUMBER "+
            "from SYS.ALL_SEQUENCES "+
            "where SEQUENCE_OWNER=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList v = new ArrayList();
        try
        {
            ps = this.conn.prepareStatement(sql_);

            ps.setString(1, schemaPattern );
            rs = ps.executeQuery();

            while (rs.next())
            {
                DbSequenceType seq = new DbSequenceType();
                seq.setName( RsetTools.getString(rs, "SEQUENCE_NAME"));
                seq.setMinValue( RsetTools.getInt(rs, "MIN_VALUE") );
                seq.setMaxValue( RsetTools.getString(rs, "MAX_VALUE") );
                seq.setIncrementBy( RsetTools.getInt(rs, "INCREMENT_BY") );
                seq.setIsCycle( RsetTools.getString(rs, "CYCLE_FLAG").equals("Y")?Boolean.TRUE:Boolean.FALSE);
                seq.setIsOrder( RsetTools.getString(rs, "ORDER_FLAG").equals("Y")?Boolean.TRUE:Boolean.FALSE);
                seq.setCacheSize( RsetTools.getInt(rs, "CACHE_SIZE") );
                seq.setLastNumber( RsetTools.getLong(rs, "LAST_NUMBER") );
                v.add( seq );
            }
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        if (v.size()>0)
            return v;
        else
            return null;
    }

    public String getViewText(DbViewType view) throws Exception
    {
        String sql_ = "select TEXT from SYS.ALL_VIEWS where OWNER=? and VIEW_NAME=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = this.conn.prepareStatement(sql_);

            ps.setString(1, view.getSchema() );
            ps.setString(2, view.getName() );
            rs = ps.executeQuery();

            if (rs.next())
            {
                if (log.isDebugEnabled())
                    log.debug("Found text of view "+view.getSchema()+"."+view.getName());

//                return getBlobField(rs, "TEXT", 0x10000);
//                return getClobField(rs, "TEXT", 0x10000);
                return getStream(rs, "TEXT", 0x10000);
//                InputStream stream=resultset.getAsciiStream(1);
//                return RsetTools.getString(rs, "TEXT", null);
            }
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
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
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public void createSequence( DbSequenceType seq ) throws Exception
    {
        if ( seq==null)
            return;
/*
        CREATE SEQUENCE MILLENNIUM.SEQ_SITE_XSLT
         START WITH  1
         INCREMENT BY  1
         MINVALUE  1
         MAXVALUE  9999999
         NOCACHE
         NOCYCLE

        CREATE SEQUENCE MILLENNIUM.DSF
         START WITH  1
         INCREMENT BY  1
         MAXVALUE  999999999999999
         CACHE 3
         CYCLE
         ORDER
*/
        String sql_ =
            "CREATE SEQUENCE "+seq.getName()+ " "+
            "START WITH "+seq.getLastNumber()+ " "+
            "INCREMENT BY "+seq.getIncrementBy()+ " "+
            "MINVALUE "+seq.getMinValue()+ " "+
            "MAXVALUE "+seq.getMaxValue()+" "+
            (seq.getCacheSize().intValue()==0?"NOCACHE":"CACHE "+seq.getCacheSize())+" "+
            (Boolean.TRUE.equals( seq.getIsCycle() )?"CYCLE":"NOCYCLE")+" "+
            (Boolean.TRUE.equals( seq.getIsOrder() )?"ORDER":"")+" ";

        PreparedStatement ps = null;

        try
        {
            ps = this.conn.prepareStatement(sql_);
            ps.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println("Error create sequence "+sql_);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
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

    public String getBlobField(ResultSet rs, String nameField, int maxLength)
            throws Exception
    {
        BLOB blob = ((OracleResultSet)rs).getBLOB( nameField );

        // Get binary output stream to retrieve blob data
        InputStream instream = blob.getBinaryStream();

        // Create temporary buffer for read
        byte[] buffer = new byte[10];

        // length of bytes read
        int length = 0;

        String ret = "";
        boolean flag = false;
        // Fetch data
        while ((length = instream.read(buffer)) != -1)
        {
            flag = true;
            ret += new String( buffer );
        }

        // Close input stream
        instream.close();

        if (flag)
            return ret;
        else
            return null;
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength)
            throws SQLException
    {
        CLOB clob = ((OracleResultSet) rs).getCLOB(nameField);

        if (clob == null)
            return null;

        return clob.getSubString(1, maxLength);
    }

    public String getStream(ResultSet rs, String nameField, int maxLength)
            throws Exception
    {

//        InputStream instream = rs.getAsciiStream(1);
        InputStream instream = rs.getBinaryStream(1);

        // Create temporary buffer for read
        byte[] buffer = new byte[maxLength];

        // length of bytes read
        int length = 0;

        String ret = "";
        boolean flag = false;
        // Fetch data
        if (( length=instream.read(buffer)) != -1 )
        {
            flag = true;
            String dbCharset = dc.getDatabaseCharset();
            if (dbCharset==null) {
                log.warn("DatabaseCharset element not defined. We will use 'utf8' charset instead");
                dbCharset="utf8";
            }

            ret = new String( buffer , 0, length,  dbCharset);

            if (log.isDebugEnabled())
                log.debug("text from stream\n"+ret);
        }

        // Close input stream
        try
        {
            instream.close();
            instream = null;
        }
        catch(Exception e)
        {
            log.warn("error close of stream", e);
        }


        if (flag)
            return ret;
        else
            return null;
    }

    public long getSequenceNextValue(String s)
            throws SQLException
    {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( s );

        return getSequenceNextValue( seq );
    }

    public long getSequenceNextValue(CustomSequenceType seq)
            throws SQLException
    {
        if (seq==null)
            return -1;

        long id_ = -1;

        String sql_ = "select " + seq.getSequenceName() + ".nextval from dual";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = this.conn.prepareStatement(sql_);

            rs = ps.executeQuery();

            if (rs.next())
                id_ = rs.getLong(1);

        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

        return id_;
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
            DatabaseManager.close( rset, prepStatement );
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
            DatabaseManager.close( rset, prepStatement );
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
            DatabaseManager.close( rset, prepStatement );
            rset = null;
            prepStatement = null;
        }

        return id_;
    }

    public Long getFirstLongValue(String t, String f, String w, String o) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean testExceptionTableNotFound(Exception e)
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) &&
                (e.toString().indexOf("ORA-00942") != -1))
            return true;
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e, String index)
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) &&
                ((e.toString().indexOf("ORA-00001") != -1) &&
                (e.toString().indexOf(index) != -1)))

            return true;

        return false;
    }

    public boolean testExceptionIndexUniqueKey( Exception e )
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) && ((e.toString().indexOf("ORA-00001") != -1)))
            return true;

        return false;
    }

    public boolean testExceptionTableExists(Exception e)
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) &&
                (e.toString().indexOf("ORA-00955") != -1))
            return true;

        return false;
    }

    public boolean testExceptionViewExists(Exception e)
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) &&
                (e.toString().indexOf("ORA-00955") != -1))
            return true;

        return false;
    }

    public boolean testExceptionSequenceExists( Exception e )
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) &&
                (e.toString().indexOf("ORA-00955") != -1))
            return true;

        return false;
    }

    public boolean testExceptionConstraintExists(Exception e)
    {
        if (e==null)
            return false;

        if ((e instanceof SQLException) &&
                (e.toString().indexOf("ORA-02275") != -1))
            return true;

        return false;
    }

    public int getFamaly()
    {
        return DatabaseManager.ORACLE_FAMALY;
    }

    public int getVersion()
    {
        return 8;
    }

    public int getSubVersion()
    {
        return 1;
    }

    public PostgreeSQLconnect()
    {
        super();
    }

}
