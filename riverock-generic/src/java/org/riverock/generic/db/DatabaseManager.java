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
package org.riverock.generic.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.structure.*;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;

/**
 * User: Admin
 * Date: Aug 30, 2003
 * Time: 5:07:17 PM
 *
 * $Id$
 */
public final class DatabaseManager {
    private static Logger log = Logger.getLogger( DatabaseManager.class );

    public final static int ORACLE_FAMALY = 1;
    public final static int MYSQL_FAMALY = 2;
    public final static int DB2_FAMALY = 3;
    public final static int MSSQL_FAMALY = 4;
    public final static int HSQLDB_FAMALY = 5;
    public final static int SAPDB_FAMALY = 6;
    public final static int INTERBASE_FAMALY = 7;


    public static void close( final DatabaseAdapter db_ ) {
        DatabaseAdapter.close(db_);
    }

    public static void close( final DatabaseAdapter db_, final ResultSet rs, final PreparedStatement ps ) {
        close(rs, ps);
        DatabaseAdapter.close(db_);
    }

    public static void close( final DatabaseAdapter db_, final PreparedStatement ps ) {
        close(ps);
        DatabaseAdapter.close(db_);
    }

    public static void close( final ResultSet rs, final Statement st ) {
        if ( rs!=null )
        {
            try
            {
                rs.close();
            }
            catch (Exception e01)
            {
            }
        }
        if ( st!=null )
        {
            try
            {
                st.close();
            }
            catch (Exception e02)
            {
            }
        }
    }

    public static void close( final Statement st )
    {
        if ( st!=null )
        {
            try
            {
                st.close();
            }
            catch (SQLException e201)
            {
            }
        }
    }

    public static void addPrimaryKey( final DatabaseAdapter db_, final DbTableType table, final DbPrimaryKeyType pk )
        throws Exception {
        if ( table == null )
        {
            String s = "Add primary key failed - table object is null";
            System.out.println( s );
            if (log.isInfoEnabled())
                log.info( s );

            return;
        }

        DbPrimaryKeyType checkPk = DatabaseStructureManager.getPrimaryKey(db_.conn, table.getSchema(), table.getName());

        if (checkPk!=null && checkPk.getColumnsCount()!=0)
        {
            String s = "primary key already exists";
            System.out.println( s );
            if (log.isInfoEnabled())
                log.info( s );

            return;
        }

        String tempTable = table.getName()+'_'+table.getName();
        duplicateTable(db_, table, tempTable);
        db_.dropTable( table );
        table.setPrimaryKey( pk );
        db_.createTable( table );
        copyData(db_, table, tempTable, table.getName());
        db_.dropTable( tempTable );
    }

    public static void copyData(
        final DatabaseAdapter db_, final DbTableType fieldsList, final String sourceTable, final String targetTableName
        )
        throws Exception
    {
        if ( fieldsList == null || fieldsList==null || targetTableName==null)
        {
            if (log.isInfoEnabled())
                log.info("copy data failed, some objects is null");

            return;
        }

        String fields = "";
        boolean isNotFirst = false;
        for (int i=0; i<fieldsList.getFieldsCount(); i++)
        {
            if (isNotFirst)
            {
                fields += ", ";
            }
            else
            {
                isNotFirst = true;
            }
            fields += fieldsList.getFields(i).getName();
        }

        String sql_ =
            "insert into " + targetTableName +
            "("+fields+")"+
            (db_.getIsNeedUpdateBracket()?"(":"")+
            "select "+fields+" from "+sourceTable+
            (db_.getIsNeedUpdateBracket()?")":"");

        Statement ps = null;
        try
        {
            ps = db_.createStatement();
            ps.execute( sql_ );
        }
        catch(SQLException e)
        {
            String errorString = "Error copy data from table '"+sourceTable+
                "' to '"+targetTableName+"' "+e.getErrorCode()+"\nsql - "+sql_;

            log.error(errorString, e);
            System.out.println( errorString );
            throw e;
        }
        finally
        {
            close( ps );
            ps = null;
        }
    }

    public static void copyFieldData(
        final DatabaseAdapter db_, final DbTableType table, final DbFieldType sourceField, final DbFieldType targetField
        )
        throws Exception
    {
        if ( table == null || sourceField==null || targetField==null)
        {
            if (log.isInfoEnabled())
                log.info("copy field data failed, some objects is null");

            return;
        }

        String sql_ =
            "update " + table.getName() + ' ' +
            "SET "+targetField.getName() + '='+sourceField.getName();

        Statement ps = null;
        try
        {
            ps = db_.createStatement();
            ps.execute( sql_ );
        }
        catch(SQLException e)
        {
            String errorString = "Error copy data from field '"+table.getName()+'.'+sourceField.getName()+
                "' to '"+table.getName()+'.'+targetField.getName()+"' "+e.getErrorCode()+"\nsql - "+sql_;

            log.error(errorString, e);
            System.out.println( errorString );
            throw e;
        }
        finally
        {
            close( ps );
            ps = null;
        }
    }

    public static void duplicateTable( final DatabaseAdapter db_, final DbTableType srcTable, final String targetTableName )
        throws Exception
    {
        if ( srcTable == null )
        {
            log.error("duplicate table failed, source table object is null");
            return;
        }

        DbTableType tempTable = cloneDescriptionTable( srcTable );
        tempTable.setName( targetTableName );
        tempTable.setPrimaryKey(null);
        tempTable.setImportedKeys( new ArrayList(0) );
        tempTable.setData(null);

        db_.createTable( tempTable );
        copyData(db_, tempTable, srcTable.getName(), targetTableName);
    }

    public static DbPrimaryKeyColumnType
        cloneDescriptionPrimaryKeyColumn( final DbPrimaryKeyColumnType srcCol )
    {
        DbPrimaryKeyColumnType c = new DbPrimaryKeyColumnType();
        c.setCatalogName( srcCol.getCatalogName());
        c.setColumnName( srcCol.getColumnName());
        c.setKeySeq( srcCol.getKeySeq());
        c.setPkName( srcCol.getPkName());
        c.setSchemaName( srcCol.getSchemaName());
        c.setTableName( srcCol.getTableName());

        return c;
    }

    public static DbImportedPKColumnType cloneDescriptionFK( final DbImportedPKColumnType srcFk )
    {
        if (srcFk==null)
            return null;

        DbImportedPKColumnType f = new DbImportedPKColumnType();
        f.setDeferrability( srcFk.getDeferrability());
        f.setDeleteRule( srcFk.getDeleteRule());
        f.setFkColumnName( srcFk.getFkColumnName());
        f.setFkName( srcFk.getFkName());
        f.setFkTableName( srcFk.getFkTableName());
        f.setFkSchemaName( srcFk.getFkSchemaName() );
        f.setKeySeq( srcFk.getKeySeq());
        f.setPkColumnName( srcFk.getPkColumnName());
        f.setPkName( srcFk.getPkName());
        f.setPkTableName( srcFk.getPkTableName());
        f.setPkSchemaName( srcFk.getPkSchemaName() );
        f.setUpdateRule( srcFk.getUpdateRule());

        return f;
    }

    public static DbPrimaryKeyType cloneDescriptionPK( final DbPrimaryKeyType srcPk )
    {
        if (srcPk==null)
            return null;

        DbPrimaryKeyType pk = new DbPrimaryKeyType();
        for (int i=0; i<srcPk.getColumnsCount(); i++)
        {
            DbPrimaryKeyColumnType col = srcPk.getColumns(i);
            pk.addColumns( cloneDescriptionPrimaryKeyColumn(col) );
        }

        return pk;
    }

    public static DbFieldType cloneDescriptionField( final DbFieldType srcField )
    {
        if (srcField==null)
            return null;

        DbFieldType f = new DbFieldType();
        f.setApplType( srcField.getApplType());
        f.setComment( srcField.getComment());
        f.setDataType( srcField.getDataType());
        f.setDecimalDigit( srcField.getDecimalDigit());
        f.setDefaultValue( srcField.getDefaultValue());
        f.setJavaStringType( srcField.getJavaStringType());
        f.setJavaType( srcField.getJavaType());
        f.setName( srcField.getName());
        f.setNullable( srcField.getNullable());
        f.setSize( srcField.getSize());

        return f;
    }

    /**
     * Clone description of table. Data not cloned
     * @param srcTable
     * @return DbTableType
     */
    public static DbTableType cloneDescriptionTable( final DbTableType srcTable )
    {
        if (srcTable==null)
            return null;

        DbTableType r = new DbTableType();

        r.setSchema( srcTable.getSchema());
        r.setName( srcTable.getName());
        r.setType( srcTable.getType());

        DbPrimaryKeyType pk = cloneDescriptionPK( srcTable.getPrimaryKey());
        r.setPrimaryKey( pk );

        for (int k = 0; k < srcTable.getFieldsCount(); k++)
        {
            DbFieldType srcField = srcTable.getFields( k );
            DbFieldType f = cloneDescriptionField( srcField );
            r.addFields( f );
        }

        for (int k = 0; k < srcTable.getImportedKeysCount(); k++)
        {
            DbImportedPKColumnType srcField = srcTable.getImportedKeys(k);
            DbImportedPKColumnType f = cloneDescriptionFK( srcField );
            r.addImportedKeys( f );
        }

        return r;
    }

    public static DbFieldType getFieldFromStructure( final DbSchemaType schema, final String tableName, final String fieldName )
    {
        if (schema==null || tableName==null || fieldName==null)
            return null;

        for (int k = 0; k < schema.getTablesCount(); k++)
        {
            DbTableType checkTable = schema.getTables( k );
            if (tableName.equalsIgnoreCase( checkTable.getName()))
            {
                for (int i = 0; i < checkTable.getFieldsCount(); i++)
                {
                    DbFieldType checkField = checkTable.getFields( i );
                    if (fieldName.equalsIgnoreCase( checkField.getName()))
                        return checkField;
                }
            }
        }
        return null;
    }

    // check is 'tableName' is table or view
    public static DbTableType getTableFromStructure( final DbSchemaType schema, final String tableName )
    {
        if (schema==null || tableName==null)
            return null;

        for (int k = 0; k < schema.getTablesCount(); k++)
        {
            DbTableType checkTable = schema.getTables( k );
            if (tableName.equalsIgnoreCase( checkTable.getName()))
                return checkTable;
        }
        return null;
    }

    public static DbViewType getViewFromStructure( final DbSchemaType schema, final String viewName )
    {
        if (schema==null || viewName==null)
            return null;

        for (int k = 0; k < schema.getViewsCount(); k++)
        {
            DbViewType checkView = schema.getViews( k );
            if (viewName.equalsIgnoreCase( checkView.getName()))
                return checkView;
        }
        return null;
    }

    public static boolean isFieldForeignKey( final DbTableType table, final DbFieldType field )
    {
        if (table==null || field==null)
            return false;

        for (int k = 0; k<table.getImportedKeysCount(); k++)
        {
            DbImportedPKColumnType column = table.getImportedKeys(k);
            if (table.getName().equalsIgnoreCase( column.getFkTableName()) &&
                field.getName().equalsIgnoreCase( column.getFkColumnName()) )
                return true;
        }
        return false;
    }

    public static boolean isFieldPrimaryKey( final DbTableType table, final DbFieldType field )
    {
        if (table==null || field==null)
            return false;

        DbPrimaryKeyType pk = table.getPrimaryKey();
        for (int k = 0; k<pk.getColumnsCount() ; k++)
        {
            DbPrimaryKeyColumnType column = pk.getColumns(k);
            if (field.getName().equalsIgnoreCase( column.getColumnName()))
                return true;
        }
        return false;
    }

    public static boolean isFieldExists( final DbSchemaType schema, final DbTableType table, final DbFieldType field )
    {
        if (schema==null || table==null || field==null)
            return false;

        for (int k = 0; k < schema.getTablesCount(); k++)
        {
            DbTableType checkTable = schema.getTables( k );
            if (table.getName().equalsIgnoreCase( checkTable.getName()))
            {
                for (int i = 0; i < checkTable.getFieldsCount(); i++)
                {
                    DbFieldType checkField = checkTable.getFields( i );
                    if (field.getName().equalsIgnoreCase( checkField.getName()))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isTableExists( final DbSchemaType schema, final DbTableType table )
    {
        if (schema==null || table==null)
            return false;

        for (int i = 0; i < schema.getTablesCount(); i++)
        {
            DbTableType checkTable = schema.getTables( i );
            if (table.getName().equalsIgnoreCase( checkTable.getName()))
                return true;
        }
        return false;
    }

    public static DbSchemaType getDbStructure( final DatabaseAdapter db_ )
        throws Exception
    {
        DbSchemaType schema = new DbSchemaType();

        DatabaseMetaData db = db_.getConnection().getMetaData();
        String dbSchema = db.getUserName();

        ArrayList list = DatabaseStructureManager.getTableList(db_, db_.conn, dbSchema, "%");
        final int initialCapacity = list.size();
        ArrayList target = new ArrayList(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            DbTableType table = (DbTableType)list.get(i);
            if ( table.getName().startsWith("BIN$") ) {
                continue;
            }
            target.add( table );
        }
        schema.setTables( target );


        ArrayList viewVector = db_.getViewList( dbSchema, "%");
        if (viewVector!=null)
            schema.setViews( viewVector );

        ArrayList seqVector = db_.getSequnceList( dbSchema );
        if (seqVector!=null)
            schema.setSequences( seqVector );

        for (int i = 0; i < schema.getTablesCount(); i++)
        {
            DbTableType table = schema.getTables(i);
//            System.out.println( "Table - " + table.getName() );

            table.setFields(DatabaseStructureManager.getFieldsList(db_, db_.conn, table.getSchema(), table.getName()));
            table.setPrimaryKey(DatabaseStructureManager.getPrimaryKey(db_.conn, table.getSchema(), table.getName()));
            table.setImportedKeys( DatabaseStructureManager.getImportedKeys(db_.conn, table.getSchema(), table.getName()) );
        }

        for (int i = 0; i < schema.getViewsCount(); i++)
        {
            DbViewType view = schema.getViews(i);
//            System.out.println("View - " + view.getName());
            view.setText( db_.getViewText( view ));
        }

        return schema;
    }

    public static void createWithReplaceAllView( final DatabaseAdapter db_, final DbSchemaType millSchema )
        throws Exception
    {
        boolean[] idx = new boolean[millSchema.getViewsCount()];
        for (int i=0; i < idx.length; i++)
            idx[i]=false;

        for (int j=0; j < idx.length; j++)
        {
            if (idx[j])
                continue;

            for (int i=0; i < idx.length; i++)
            {
                if (idx[i])
                    continue;

                DbViewType view = millSchema.getViews(i);
                try
                {
                    db_.createView( view );
                    idx[i] = true;
                }
                catch (Exception e)
                {
                    if (db_.testExceptionViewExists(e))
                    {
                        try
                        {
                            DatabaseStructureManager.dropView(db_, view);
                        }
                        catch(Exception e1)
                        {
                            log.error("Error drop view", e1);
                            throw e1;
                        }

                        try
                        {
                            db_.createView(view);
                            idx[i] = true;
                        }
                        catch(Exception e1)
                        {
                            log.error("Error create view", e1);
//                            throw e1;
                        }
                    }
                }
            }
        }
    }

    public static ArrayList getViewList( final Connection conn, final String schemaPattern, final String tablePattern )
    {
        String[] types = {"VIEW"};

        ResultSet meta = null;
        ArrayList v = new ArrayList();
        try
        {
            DatabaseMetaData dbMeta = conn.getMetaData();

            meta = dbMeta.getTables(
                null,
                schemaPattern,
                tablePattern,
                types
            );

            while (meta.next())
            {

                DbViewType table = new DbViewType();

                table.setSchema(meta.getString("TABLE_SCHEM"));
                table.setName(meta.getString("TABLE_NAME"));
                table.setType(meta.getString("TABLE_TYPE"));
                table.setRemark(meta.getString("REMARKS"));

                if (log.isDebugEnabled())
                    log.debug("View - " + table.getName() + "  remak - " + table.getRemark());

                v.add(table);
            }
        }
        catch (Exception e)
        {
        }
        return v;
    }

    public static boolean isSkipTable( final String table )
    {
        if (table==null)
            return false;

        String s = table.trim();

        String fullCheck[] = { "SQLN_EXPLAIN_PLAN", "DBG", "CHAINED_ROWS" };
        for (int i=0; i<fullCheck.length; i++)
        {
            if (fullCheck[i].equalsIgnoreCase(s))
                return true;
        }

        String startCheck[] = { "F_D_", "FOR_DEL_", "F_DEL_", "FOR_D_" };
        for (int i=0; i<startCheck.length; i++)
        {
            if (s.toLowerCase().startsWith( startCheck[i].toLowerCase() ))
                return true;
        }

        return false;
    }

    public static boolean isMultiColumnFk( final DbTableType table, final DbImportedPKColumnType key )
    {
        for (int i=0; i< table.getImportedKeysCount(); i++)
        {
            DbImportedPKColumnType checkKey = table.getImportedKeys(i);

            if (checkKey.getFkColumnName().equals( key.getFkColumnName()) &&
                checkKey.getKeySeq()!=key.getKeySeq()
                )
                return true;
        }
        return false;
    }

    /**
     * Прверяет не является ли значение поля по молчанию текущим временем.
     * Например для Oracle это SYSDATE
     * @param val
     * @return true - значение является датой, иначе false
     */
    public static boolean checkDefaultTimestamp( final String val )
    {
        if (val==null)
            return false;

        String s = val.trim().toLowerCase();
        String check[] =
                { "sysdate",
//                  'now', 'today',
                  "current_timestamp", "current_time", "current_date"
                };
        for (int i=0; i<check.length; i++)
        {
            if (check[i].equals(s))
                return true;
        }
        return false;
    }

    /**
     *
     * @param dbDyn
     * @param idRec - value of PK in main table
     * @param pkName - name PK in main table
     * @param pkType - type of PK in main table
     * @param nameTargetTable  - name of slave table
     * @param namePkTargetTable - name of PK in slave table
     * @param nameTargetField - name of filed with BigText data in slave table
     * @param insertString - insert string
     * @param isDelete - delete data from slave table before insert true/false
     *
     * @throws Exception
     */
    public static void insertBigText(
        final DatabaseAdapter dbDyn, final Object idRec, final String pkName,
        final PrimaryKeyTypeTypeType pkType,
        final String nameTargetTable,
        final String namePkTargetTable,
        final String nameTargetField,
        final String insertString,
        final boolean isDelete
        ) throws Exception
    {
        String sql_ = null;
        try
        {

            if (log.isDebugEnabled()) log.debug("First delete data flag - " + isDelete);

            if (isDelete)
                deleteFromBigTable(dbDyn, nameTargetTable, pkName, pkType, idRec);

            PreparedStatement ps1 = null;
            try
            {

                log.debug("Start insert dta in bigtext field ");

                int pos = 0;
                int prevPos = 0;
                int maxByte = dbDyn.getMaxLengthStringField();

                sql_ =
                    "insert into " + nameTargetTable +
                    "(" + namePkTargetTable + "," + pkName + "," + nameTargetField + ")" +
                    "values" +
                    "(?,?,?)";

                if (log.isDebugEnabled()) log.debug("insert bigtext. sql 2 - " + sql_);

                byte b[] = StringTools.getBytesUTF( insertString );

                ps1 = dbDyn.prepareStatement(sql_);
                while ((pos = StringTools.getStartUTF(b, maxByte, pos)) != -1)
                {
                    if (log.isDebugEnabled()) log.debug("Name sequence - " + "seq_" + nameTargetTable);

                    CustomSequenceType seq = new CustomSequenceType();
                    seq.setSequenceName("seq_" + nameTargetTable);
                    seq.setTableName( nameTargetTable );
                    seq.setColumnName( namePkTargetTable );
                    long idSeq = dbDyn.getSequenceNextValue( seq );

                    if (log.isDebugEnabled()) log.debug("Bind param #1" + idSeq);

                    ps1.setLong(1, idSeq);

                    if (pkType.getType() == PrimaryKeyTypeTypeType.NUMBER_TYPE)
                    {

                        if (log.isDebugEnabled()) log.debug("Bind param #2 " + ((Long) idRec).longValue());

                        ps1.setLong(2, ((Long) idRec).longValue());

                    }
                    else if (pkType.getType() == PrimaryKeyTypeTypeType.STRING_TYPE)
                    {
                        if (log.isDebugEnabled()) log.debug("Bind param #2 " + (String) idRec);

                        ps1.setString(2, (String) idRec);
                    }
                    else if ( pkType.getType() == PrimaryKeyTypeTypeType.DATE_TYPE)
                    {
/*
                        if (content.getQueryArea().getPrimaryKeyMask()==null ||
                            content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
                            throw new Exception("date mask for primary key not specified");

                        primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                            content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
*/
                        throw new Exception("Type of PK 'date' for big_text not implemented");
                    }
                    else
                        throw new Exception("Wrong type of primary key");


                    String s = new String(b, prevPos, pos - prevPos, "utf-8");

                    if (log.isDebugEnabled()) log.debug("Bind param #3 " + s);

                    ps1.setString(3, s);

                    int count = ps1.executeUpdate();

                    if (log.isDebugEnabled())log.debug("number of updated records - "+count);

                    ps1.clearParameters();
                    prevPos = pos;

                } // while ( (pos=StringTools.getStartUTF ...
            }
            finally
            {
                close( ps1 );
                ps1 = null;
            }
        }
        catch (Exception e)
        {
            log.error("Error insert dat in bigText field.\nSql:\n" + sql_, e);
            throw e;
        }
    }

    public static void deleteFromBigTable( final DatabaseAdapter dbDyn, final String nameTargetTable, final String pkName, final PrimaryKeyTypeTypeType pkType, final Object idRec ) throws Exception {
        PreparedStatement ps = null;
        try
        {
            String sql_ = "delete from " + nameTargetTable +" where " + pkName + "=?";
            if (log.isDebugEnabled()) log.debug("#13.07.01 " + sql_);

            ps = dbDyn.prepareStatement(sql_);

            if (pkType.getType() == PrimaryKeyTypeTypeType.NUMBER_TYPE){
                if (log.isDebugEnabled()) log.debug("#88.01.02 " + idRec.getClass().getName());

                ps.setLong(1, ((Long) idRec).longValue());

            }
            else if (pkType.getType() == PrimaryKeyTypeTypeType.STRING_TYPE){
                ps.setString(1, (String) idRec);
            }
            else if ( pkType.getType() == PrimaryKeyTypeTypeType.DATE_TYPE){
/*
                        if (content.getQueryArea().getPrimaryKeyMask()==null ||
                            content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
                            throw new Exception("date mask for primary key not specified");

                        primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                            content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
*/
                throw new Exception("Type of PK 'date' for big_text not implemented");
            }
            else
                throw new Exception("Wrong type of primary key");

            int count = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("number of updated records - "+count);

        }
        finally
        {
            close( ps );
            ps = null;
        }
    }

    public static String getBigTextField( final DatabaseAdapter db_, final Long id_,
        final String field_,
        final String table_,
        final String idx_field_,
        final String order_field_
        )
        throws SQLException, DatabaseException
    {
        if ( id_==null )
            return "";

        PreparedStatement ps = null;
        ResultSet rset = null;
        String sql_ = "select "+field_+" from "+table_+" where "+idx_field_+"= ? order by "+order_field_+" ASC";

        if ( log.isDebugEnabled() )
        {
            log.debug( "ID: "+id_ );
            log.debug( "SQL: "+sql_ );
        }

        String text = "";
        try
        {
            ps = db_.prepareStatement( sql_ );

            if ( log.isDebugEnabled() )
                log.debug( "11.03.01" );

            RsetTools.setLong( ps, 1, id_ );
            rset = ps.executeQuery();

            if ( log.isDebugEnabled() )
                log.debug( "11.03.01" );

            while ( rset.next() )
            {
                if ( log.isDebugEnabled() )
                    log.debug( "11.03.01 "+text );

                text += RsetTools.getString( rset, field_ );

                if ( log.isDebugEnabled() )
                    log.debug( "11.03.01" );
            }
        } finally
        {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }
        return text;
    }

    public static String getRelateString( final DbImportedPKColumnType column )
    {
        return (
            column.getFkSchemaName()==null || column.getFkSchemaName().length()==0?
            "":
            column.getFkSchemaName()+'.'
            )+
            column.getFkTableName()+'.'+
            column.getFkColumnName()+
            "->"+
            (
            column.getPkSchemaName()==null || column.getPkSchemaName().length()==0?
            "":
            column.getPkSchemaName()+'.'
            )+
            column.getPkTableName()+'.'+
            column.getFkColumnName()
            ;
    }

    public static Hashtable getFkNames( final ArrayList keys )
    {
        Hashtable hash = new Hashtable();
        for (int i=0; i<keys.size(); i++)
        {
            DbImportedPKColumnType column = (DbImportedPKColumnType)keys.get(i);
            String search = getRelateString( column );
            Object obj = hash.get( search );
            if (obj==null)
                hash.put( search, column );

        }
        return hash;
/*
        DbImportedPKColumnType[] result = new DbImportedPKColumnType[hash.size()];

        int i=0;
        for (Enumeration e = hash.elements(); e.hasMoreElements() ;i++)
        {
            result[i] = (DbImportedPKColumnType)e.nextElement();
        }
        if (log.isDebugEnabled())
        {
            for (i=0; i<result.length; i++)
                log.debug("key: "+result[i] );
        }
        return result;
*/
    }

    public static int sqlTypesMapping( final String type )
    {
        if (type==null)
            return Types.OTHER;

        if ("BIT".equals(type)) return Types.BIT;
        else if ("TINYINT".equals(type)) return Types.TINYINT;
        else if ("SMALLINT".equals(type)) return Types.SMALLINT;
        else if ("INTEGER".equals(type)) return Types.INTEGER;
        else if ("BIGINT".equals(type)) return Types.BIGINT;
        else if ("FLOAT".equals(type)) return Types.FLOAT;
        else if ("REAL".equals(type)) return Types.REAL;
        else if ("DOUBLE".equals(type)) return Types.DOUBLE;
        else if ("NUMERIC".equals(type)) return Types.NUMERIC;
        else if ("DECIMAL".equals(type)) return Types.DECIMAL;
        else if ("CHAR".equals(type)) return Types.CHAR;
        else if ("VARCHAR".equals(type)) return Types.VARCHAR;
        else if ("LONGVARCHAR".equals(type)) return Types.LONGVARCHAR;
        else if ("DATE".equals(type)) return Types.DATE;
        else if ("TIME".equals(type)) return Types.TIME;
        else if ("TIMESTAMP".equals(type)) return Types.TIMESTAMP;
        else if ("BINARY".equals(type)) return Types.BINARY;
        else if ("VARBINARY".equals(type)) return Types.VARBINARY;
        else if ("LONGVARBINARY".equals(type)) return Types.LONGVARBINARY;
        else if ("NULL".equals(type)) return Types.NULL;
        else if ("OTHER".equals(type)) return Types.OTHER;
        else if ("JAVA_OBJECT".equals(type)) return Types.JAVA_OBJECT;
        else if ("DISTINCT".equals(type)) return Types.DISTINCT;
        else if ("STRUCT".equals(type)) return Types.STRUCT;
        else if ("ARRAY".equals(type)) return Types.ARRAY;
        else if ("BLOB".equals(type)) return Types.BLOB;
        else if ("CLOB".equals(type)) return Types.CLOB;
        else if ("REF".equals(type)) return Types.REF;
        else return Types.OTHER;

    }

    public static void fixBigTextTable( final DbTableType table, final ArrayList bigTables, final int maxLengthTextField )
    {
        if (bigTables==null || table==null)
            return;

        for (int i=0; i<bigTables.size(); i++)
        {
            DbBigTextTableType big = (DbBigTextTableType)bigTables.get(i);
            if (big.getSlaveTable().equals(table.getName()))
            {
                for (int k=0; k<table.getFieldsCount(); k++)
                {
                    DbFieldType field = table.getFields(k);
                    if (big.getStorageField().equals( field.getName() ))
                        field.setSize( new Integer(maxLengthTextField) );
                }
            }
        }
    }

    public static DbBigTextTableType getBigTextTableDesc( final DbTableType table, final ArrayList bigTables )
    {
        if (bigTables==null || table==null)
            return null;

        for (int i=0; i<bigTables.size(); i++)
        {
            DbBigTextTableType big = (DbBigTextTableType)bigTables.get(i);
            if (big.getSlaveTable().equals(table.getName()))
                return big;
        }
        return null;
    }

    public static void createDbStructure( final DatabaseAdapter db_, final DbSchemaType millSchema ) throws Exception
    {
        int i;
        // create sequences
        for (i=0; i < millSchema.getSequencesCount(); i++)
        {
            DbSequenceType seq = millSchema.getSequences(i);
            try
            {
                if (log.isDebugEnabled())
                    log.debug("create sequence " + seq.getName());

                db_.createSequence( seq );
            }
            catch (Exception e)
            {
                if (db_.testExceptionSequenceExists(e))
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("sequence " + seq.getName() + " already exists");
                        log.debug("drop sequence " + seq.getName());
                    }
                    db_.dropSequence(seq.getName());

                    if (log.isDebugEnabled())
                        log.debug("create sequence " + seq.getName());

                    try
                    {
                        db_.createSequence(seq);
                    }
                    catch(Exception e1)
                    {
                        log.error("Error create sequence - ", e1);
                        throw e;
                    }
                }
                else
                {
                    log.error("Error create sequence - ", e );
                    throw e;
                }
            }
        }

        for (i=0; i<millSchema.getTablesCount(); i++)
        {
            DbTableType table = millSchema.getTables(i);

            fixBigTextTable(
                table,
                millSchema.getBigTextTableAsReference(),
                db_.getMaxLengthStringField()
            );

            if (!isSkipTable(table.getName()))
            {
                try
                {
                    if (log.isDebugEnabled())
                        log.debug("create table " + table.getName());

                    db_.createTable(table);
                }
                catch (SQLException e)
                {
                    if (db_.testExceptionTableExists(e))
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("table " + table.getName() + " already exists");
                            log.debug("drop table " + table.getName());
                        }
                        db_.dropTable(table);
                        db_.commit();

                        if (log.isDebugEnabled())
                            log.debug("create table " + table.getName());

                        db_.createTable(table);
                        db_.commit();
                    }
                    else
                    {
                        log.error("Error create table " + table.getName(), e);
                        throw e;
                    }
                }
                DatabaseStructureManager.setDataTable(db_, table, millSchema.getBigTextTableAsReference());
                db_.commit();
            }
            else
            {
                if (log.isDebugEnabled())
                    log.debug("skip table " + table.getName());
            }
        }

        createWithReplaceAllView( db_, millSchema );
    }

    public static DbKeyActionRuleType decodeUpdateRule( final ResultSet rs )
    {
        try
        {
            Object obj = rs.getObject("UPDATE_RULE");
            if (obj == null)
                return null;

            DbKeyActionRuleType rule = new DbKeyActionRuleType();
            rule.setRuleType( RsetTools.getInt( rs, "UPDATE_RULE" ) );

            switch (rule.getRuleType().intValue())
            {
                case DatabaseMetaData.importedKeyNoAction:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyNoAction");
                    break;

                case DatabaseMetaData.importedKeyCascade:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyCascade");
                    break;

                case DatabaseMetaData.importedKeySetNull:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeySetNull");
                    break;

                case DatabaseMetaData.importedKeySetDefault:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeySetDefault");
                    break;

                case DatabaseMetaData.importedKeyRestrict:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyRestrict");
                    break;

                default:
                    rule.setRuleName("unknown UPDATE_RULE(" + rule.getRuleType() + ")");
                    System.out.println("unknown UPDATE_RULE(" + rule.getRuleType() + ")");
                    break;
            }
            return rule;
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public static DbKeyActionRuleType decodeDeleteRule( final ResultSet rs )
    {
        try
        {
            Object obj = rs.getObject("DELETE_RULE");
            if (obj == null)
                return null;

            DbKeyActionRuleType rule = new DbKeyActionRuleType();
            rule.setRuleType(RsetTools.getInt(rs, "DELETE_RULE"));

            switch (rule.getRuleType().intValue())
            {
                case DatabaseMetaData.importedKeyNoAction:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyNoAction");
                    break;

                case DatabaseMetaData.importedKeyCascade:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyCascade");
                    break;

                case DatabaseMetaData.importedKeySetNull:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeySetNull");
                    break;

                case DatabaseMetaData.importedKeyRestrict:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyRestrict");
                    break;

                case DatabaseMetaData.importedKeySetDefault:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeySetDefault");
                    break;

                default:
                    rule.setRuleName("unknown DELETE_RULE(" + rule.getRuleType() + ")");
                    System.out.println("unknown DELETE_RULE(" + rule.getRuleType() + ")");
                    break;
            }
            return rule;
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public static DbKeyActionRuleType decodeDeferrabilityRule( final ResultSet rs )
    {
        try
        {
            Object obj = rs.getObject("DEFERRABILITY");
            if (obj == null)
                return null;

            DbKeyActionRuleType rule = new DbKeyActionRuleType();
            rule.setRuleType(RsetTools.getInt(rs, "DEFERRABILITY") );

            switch (rule.getRuleType().intValue())
            {
                case DatabaseMetaData.importedKeyInitiallyDeferred:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyInitiallyDeferred");
                    break;
                case DatabaseMetaData.importedKeyInitiallyImmediate:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyInitiallyImmediate");
                    break;
                case DatabaseMetaData.importedKeyNotDeferrable:
                    rule.setRuleName("java.sql.DatabaseMetaData.importedKeyNotDeferrable");
                    break;
                default:
                    rule.setRuleName("unknown DEFERRABILITY(" + rule.getRuleType() + ")");
                    System.out.println("unknown DEFERRABILITY(" + rule.getRuleType() + ")");
                    break;
            }
            return rule;
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public static int runSQL( final DatabaseAdapter db, final String query, final Object[] params, final int[] types )
        throws SQLException
    {
        int n = 0;
        Statement stmt = null;
        PreparedStatement pstm = null;

        try
        {
            if (params == null)
            {
                stmt = db.createStatement();
                n = stmt.executeUpdate(query);
            }
            else
            {
                pstm = db.prepareStatement(query);
                for (int i = 0; i < params.length; i++)
                {
                    if (params[i]!=null)
                        pstm.setObject(i + 1, params[i], types[i]);
                    else
                        pstm.setNull(i + 1, types[i]);
                }

                n = pstm.executeUpdate();
                stmt = pstm;
            }
        }
        catch (SQLException e)
        {
            log.error("SQL query:\n" + query);
            if (params != null)
            {
                for (int ii = 0; ii < params.length; ii++)

                    log.error("parameter #" + (ii + 1) + ": " + (params[ii] != null ? params[ii].toString() : null));
            }
            log.error("SQLException", e);
            throw e;
        }
        finally
        {
            close(stmt);
            stmt = null;
            pstm = null;
        }
        return n;
    }

    public static Long getLongValue( final DatabaseAdapter db, final String sql, final Object[] params )
        throws SQLException, DatabaseException
    {
        Statement stmt = null;
        PreparedStatement pstm;
        ResultSet rs = null;

        try
        {
            if (params == null)
            {
                stmt = db.createStatement();
                rs = stmt.executeQuery(sql);
            }
            else
            {
                pstm = db.prepareStatement(sql);
                for (int i = 0; i < params.length; i++)
                    pstm.setObject(i + 1, params[i]);

                rs = pstm.executeQuery();
                stmt = pstm;
            }

            if (rs.next())
            {
                long tempLong = rs.getLong(1);
                if (rs.wasNull())
                    return null;

                return new Long(tempLong);
            }
            return null;
        }
        catch (SQLException e)
        {
            log.error("error getting long value fron sql '" + sql + "'", e);
            throw e;
        }
        finally
        {
            close(rs, stmt);
            rs = null;
            stmt = null;
            pstm = null;
        }

    }

    public static List getIdByList( final DatabaseAdapter adapter, final String sql, final Object[] param )
        throws GenericException, DatabaseException
    {
        Statement stmt = null;
        PreparedStatement pstm;
        ResultSet rs = null;
        List list = new ArrayList();
        try
        {
            if (param == null)
            {
                stmt = adapter.createStatement();
                rs = stmt.executeQuery(sql);
            }
            else
            {
                pstm = adapter.prepareStatement(sql);
                for (int i = 0; i < param.length; i++)
                    pstm.setObject(i + 1, param[i]);

                rs = pstm.executeQuery();
                stmt = pstm;
            }

            while (rs.next())
            {
                long tempLong = rs.getLong(1);
                if (rs.wasNull())
                    continue;

                list.add( new Long(tempLong) );
            }
            return list;
        }
        catch (SQLException e)
        {
            final String es = "error getting long value fron sql '" + sql + "'";
            log.error( es, e );
            throw new GenericException( es, e );
        }
        finally
        {
            close(rs, stmt);
            rs = null;
            stmt = null;
            pstm = null;
        }
    }
}
