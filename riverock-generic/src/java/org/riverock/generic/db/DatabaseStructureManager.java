package org.riverock.generic.db;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Iterator;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import org.riverock.generic.schema.db.structure.*;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.db.definition.DefinitionService;
import org.riverock.common.tools.StringTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.ExceptionTools;

/**
 * @author SergeMaslyukov
 *         Date: 20.12.2005
 *         Time: 1:07:54
 *         $Id$
 */
public class DatabaseStructureManager {
    private final static Logger log = Logger.getLogger( DatabaseStructureManager.class );

    public static void createForeignKey(DatabaseAdapter adapter, DbImportedKeyListType fkList) throws Exception
    {
        if (fkList==null || fkList.getKeysCount()==0)
            return;

        Hashtable hash= DatabaseManager.getFkNames( fkList.getKeysAsReference() );

//        System.out.println("key count: "+hash.size() );
//        for (Enumeration e = hash.keys(); e.hasMoreElements();)
//            System.out.println("key: "+(String)e.nextElement() );

        int p=0;

        for (Enumeration e = hash.elements(); e.hasMoreElements(); p++)
        {
            DbImportedPKColumnType fkColumn = (DbImportedPKColumnType)e.nextElement();
            String searchCurrent = DatabaseManager.getRelateString( fkColumn );
//            System.out.println("#"+p+" fk name - "+ fkColumn.getFkName()+" ");
            String sql =
                "ALTER TABLE "+fkList.getKeys(0).getFkTableName()+" "+
                "ADD CONSTRAINT "+
                (
                fkColumn.getFkName()==null || fkColumn.getFkName().length()==0
                ?fkList.getKeys(0).getFkTableName()+p+"_fk"
                :fkColumn.getFkName()
                ) +
                " FOREIGN KEY (";

            int seq = Integer.MIN_VALUE;
            boolean isFirst = true;
            for ( int i=0; i<fkList.getKeysCount(); i++ )
            {
                DbImportedPKColumnType currFkCol = fkList.getKeys(i);
                String search = DatabaseManager.getRelateString( currFkCol );
//                System.out.println( "1.0 "+search );
                if (!searchCurrent.equals(search))
                    continue;

//                System.out.println("here");

                DbImportedPKColumnType column = null;
                DbImportedPKColumnType columnTemp = null;
                int seqTemp = Integer.MAX_VALUE;
                for ( int k=0; k<fkList.getKeysCount(); k++ )
                {
                    columnTemp = fkList.getKeys(k);
                    String searchTemp = DatabaseManager.getRelateString( columnTemp );
//                    System.out.println("here 2.0 "+ searchTemp );
                    if (!searchCurrent.equals(searchTemp))
                        continue;

//                    System.out.println("here 2.1");

                    if (seq < columnTemp.getKeySeq() && columnTemp.getKeySeq() < seqTemp )
                    {
                        seqTemp = columnTemp.getKeySeq();
                        column = columnTemp;
                    }
                }
                seq = column.getKeySeq();

                if (!isFirst)
                    sql += ",";
                else
                    isFirst = !isFirst;

                sql += column.getFkColumnName();
            }
            sql += ")\nREFERENCES "+fkColumn.getPkTableName()+" (";

            seq = Integer.MIN_VALUE;
            isFirst = true;
            for ( int i=0; i<fkList.getKeysCount();i++ )
            {
                DbImportedPKColumnType currFkCol = fkList.getKeys(i);
                String search = DatabaseManager.getRelateString( currFkCol );
                if (!searchCurrent.equals(search))
                    continue;

                DbImportedPKColumnType column = null;
                DbImportedPKColumnType columnTemp = null;
                int seqTemp = Integer.MAX_VALUE;
                for ( int k=0; k<fkList.getKeysCount(); k++ )
                {
                    columnTemp = fkList.getKeys(k);
                    String searchTemp = DatabaseManager.getRelateString( columnTemp );
                    if (!searchCurrent.equals(searchTemp))
                        continue;

                    if (seq < columnTemp.getKeySeq() && columnTemp.getKeySeq() < seqTemp )
                    {
                        seqTemp = columnTemp.getKeySeq();
                        column = columnTemp;
                    }
                }

                seq = column.getKeySeq();

                if (!isFirst)
                    sql += ",";
                else
                    isFirst = !isFirst;

                sql += column.getPkColumnName();
            }
            sql += ") ";
            switch( fkColumn.getDeleteRule().getRuleType().intValue() )
            {
                case DatabaseMetaData.importedKeyRestrict:
                    sql += adapter.getOnDeleteSetNull();
                    break;
                case DatabaseMetaData.importedKeyCascade:
                    sql += "ON DELETE CASCADE ";
                    break;

                default:
                    throw new IllegalArgumentException(" imported keys delete rule '"+
                        fkColumn.getDeleteRule().getRuleName()+"' not supported");
            }
            switch( fkColumn.getDeferrability().getRuleType().intValue() )
            {
                case DatabaseMetaData.importedKeyNotDeferrable:
                    break;
                case DatabaseMetaData.importedKeyInitiallyDeferred:
                    sql += " DEFERRABLE INITIALLY DEFERRED";
                    break;

                default:
                    throw new IllegalArgumentException(" imported keys deferred rule '"+
                        fkColumn.getDeferrability().getRuleName()+"' not supported");
            }
//            System.out.println( sql );

            PreparedStatement ps = null;
            try
            {
                ps = adapter.conn.prepareStatement(sql);
                ps.executeUpdate();
            }
            catch(SQLException exc)
            {
                if (!adapter.testExceptionTableExists(exc))
                {
                    System.out.println( "sql "+sql);
                    System.out.println( "code "+exc.getErrorCode() );
                    System.out.println( "state "+exc.getSQLState() );
                    System.out.println( "message "+exc.getMessage() );
                    System.out.println( "string "+exc.toString() );
                }
                throw exc;
            }
            finally
            {
                DatabaseManager.close( ps );
                ps = null;
            }

        }
    }

    public static void addColumn(DatabaseAdapter adapter, String tableName, DbFieldType field) throws Exception
    {
        DbTableType table = new DbTableType();
        table.setName( tableName );
        adapter.addColumn( table, field );
    }

    public static void dropColumn(DatabaseAdapter adapter, DbTableType table, DbFieldType field)
        throws Exception
    {
        if ( table == null ||
                table.getName()==null || table.getName().length()==0
        )
            return;

        if ( field == null ||
                field.getName()==null || field.getName().length()==0
        )
            return;

        String sql_ = "ALTER TABLE "+table.getName()+" DROP COLUMN "+field.getName();
        PreparedStatement ps = null;
        try
        {
            ps = adapter.conn.prepareStatement(sql_);
            ps.executeUpdate();
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void dropView(DatabaseAdapter adapter, DbViewType view)
            throws Exception
    {
        if ( view == null ||
                view.getName()==null || view.getName().length()==0
        )
            return;

        String sql_ = "drop VIEW "+view.getName();
        PreparedStatement ps = null;
        try
        {
            ps = adapter.conn.prepareStatement(sql_);
            ps.executeUpdate();
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void setDataTable(DatabaseAdapter adapter, DbTableType table)
        throws Exception
    {
        setDataTable(adapter, table, null);
    }

    public static void setDataTable(DatabaseAdapter adapter, DbTableType table, ArrayList bigTables)
        throws Exception
    {
        if (table==null || table.getData()==null|| table.getData().getRecordsCount()==0){
            System.out.println("Table is empty");
            return;
        }

        DbBigTextTableType big = DatabaseManager.getBigTextTableDesc(table, bigTables);

        if (table.getFieldsCount() == 0)
            throw new Exception("Table has zero count of fields");


        boolean isDebug = true;
        if (table.getName().equalsIgnoreCase("WM_NEWS_ITEM_TEXT"))
            isDebug = true;

        String sql_ =
            "insert into " + table.getName() +
            "(";

        boolean isFirst = true;
        for (int i=0; i<table.getFieldsCount(); i++)
        {
            DbFieldType field = table.getFields(i);
            if (isFirst)
                isFirst = false;
            else
                sql_ += ", ";

            sql_ += field.getName();
        }
        sql_ += ")values(";

        isFirst = true;
        for (int i=0; i<table.getFieldsCount(); i++)
        {
            DbFieldType field = table.getFields(i);
            if (isFirst)
                isFirst = false;
            else
                sql_ += ", ";

            if (field.getJavaType()!=Types.DATE && field.getJavaType()!=Types.TIMESTAMP )
                sql_ += " ?";
            else
                sql_ += adapter.getNameDateBind();
        }
        sql_ += ")";

        DbDataTableType tableData = table.getData();

        System.out.println(
            "table "+table.getName()+", "+
            "fields " + table.getFieldsCount()+", " +
            "records " + tableData.getRecordsCount()+", sql:\n"+sql_
        );


        if (big==null)
        {

            for (int i = 0; i < tableData.getRecordsCount(); i++)
            {
                DbDataRecordType record = tableData.getRecords(i);
                PreparedStatement ps = null;
                ResultSet rs = null;
                try
                {
                    ps = adapter.conn.prepareStatement(sql_);

                    int fieldPtr = 0;
                    for (int k = 0; k < record.getFieldsDataCount(); k++)
                    {
                        DbFieldType field = table.getFields( fieldPtr++ );
                        DbDataFieldDataType fieldData = record.getFieldsData(k);

                        if ( fieldData.getIsNull() )
                        {
                            int type = fieldData.getJavaTypeField();
                            if (fieldData.getJavaTypeField()==Types.TIMESTAMP)
                                type=Types.DATE;

                            ps.setNull(k+1, type);
                        }
                        else
                        {
                            if (isDebug)
                                System.out.println("param #"+(k+1)+", type "+fieldData.getJavaTypeField());

                            switch (fieldData.getJavaTypeField().intValue())
                            {
                                case Types.DECIMAL:
                                case Types.DOUBLE:
                                case Types.NUMERIC:
                                    if ( field.getDecimalDigit()==null ||
                                        field.getDecimalDigit()==0 )
                                    {
                                        if (isDebug)
                                            System.out.println("Types.NUMERIC as Types.INTEGER param #"+(k+1)+", " +
                                                "value "+fieldData.getNumberData().doubleValue()+", long value "+((long)fieldData.getNumberData().doubleValue())
                                            );
                                        ps.setLong(k+1, (long)fieldData.getNumberData().doubleValue());
                                    }
                                    else
                                    {
                                        if (isDebug)
                                            System.out.println("Types.NUMERIC param #"+(k+1)+", value "+fieldData.getNumberData().doubleValue());
                                        ps.setDouble(k+1, fieldData.getNumberData().doubleValue());
                                    }
                                    break;

                                case Types.INTEGER:
                                    if (isDebug)
                                        System.out.println("Types.INTEGER param #"+(k+1)+", value "+fieldData.getNumberData().doubleValue());
                                    ps.setLong(k+1, (long)fieldData.getNumberData().doubleValue());
                                    break;

                                case Types.CHAR:
                                    if (isDebug)
                                        System.out.println("param #"+(k+1)+", value "+fieldData.getStringData().substring(0, 1));
                                    ps.setString(k+1, fieldData.getStringData().substring(0, 1));
                                    break;

                                case Types.VARCHAR:
                                    if (isDebug)
                                        System.out.println("param #"+(k+1)+", value "+fieldData.getStringData());
                                    ps.setString(k+1, fieldData.getStringData());
                                    break;

                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    long timeMillis = fieldData.getDateData().getTime();
                                    Timestamp stamp = new Timestamp(timeMillis);
                                    if (isDebug)
                                        System.out.println("param #"+(k+1)+", value "+stamp);
                                    adapter.bindDate(ps, k+1, stamp);
                                    ps.setTimestamp(k+1, stamp);
                                    break;

                                case Types.LONGVARCHAR:
                                    adapter.setLongVarchar(ps, k+1, fieldData);
                                    break;

                                case Types.LONGVARBINARY:
                                    adapter.setLongVarbinary(ps, k+1, fieldData);
                                    break;

                                case 1111:
                                    if (isDebug)
                                        System.out.println("param #"+(k+1)+", value "+fieldData.getStringData());
                                    ps.setString(k+1, "");
                                    break;
                                default:
                                    System.out.println("Unknown field type.");
                            }
                        }
                    }
                    ps.executeUpdate();
                }
                catch (Exception e)
                {
                    log.error("Error get data for table " + table.getName(), e);
                    throw e;
                }
                finally
                {
                    DatabaseManager.close( rs, ps );
                    rs = null;
                    ps = null;
                }

                if ((i%200)==0 && i!=0)
                    System.out.print("count inserted records - "+i+"\n");
            }
        }
        else // process big text table
        {

            int idx = 0;
            int idxFk=0;
            int idxPk=0;
            boolean isNotFound = true;
            // ������� ������� ����� � ������
            for (int i = 0; i < table.getFieldsCount(); i++)
            {
                DbFieldType field = table.getFields(i);
                if (field.getName().equals(big.getStorageField()))
                {
                    idx = i;
                    isNotFound = false;
                }
                if (field.getName().equals(big.getSlaveFkField()))
                {
                    idxFk = i;
                }
                if (field.getName().equals(big.getSlavePkField()))
                {
                    idxPk = i;
                }
            }
            if (isNotFound)
                throw new Exception ("Storage field '"+big.getStorageField()+"' not found in table "+table.getName());

            if (isDebug)
            {
                System.out.println("pk idx "+idxPk);
                System.out.println("fk idx "+idxFk);
                System.out.println("storage idx "+idx);
            }
            Hashtable hashFk = new Hashtable( tableData.getRecordsCount() );
            // �������� ��� ��������� ������. �.�. ������������ Hashtable
            // ��� �������� � ���� ���������
            for (int i = 0; i < tableData.getRecordsCount(); i++)
            {
                DbDataRecordType record = tableData.getRecords(i);

                DbDataFieldDataType fieldFk = record.getFieldsData(idxFk);
                Long idRec = fieldFk.getNumberData().longValue();

                hashFk.put(idRec, new Object());
            }

            // ��������� ������ �������� �� ������ ��������� ������
            for (Enumeration e = hashFk.keys() ; e.hasMoreElements() ;)
            {
                Long idFk = (Long)e.nextElement();

                if (isDebug)
                    System.out.println("ID of fk "+idFk);

                TreeSet setPk = new TreeSet();

                // ������� ������ ������������� ��������� ������
                // ��� ������� ���������� �����
                for (int i = 0; i < tableData.getRecordsCount(); i++)
                {
                    DbDataRecordType record = tableData.getRecords(i);

                    DbDataFieldDataType fieldFk = record.getFieldsData(idxFk);
                    long idRec = fieldFk.getNumberData().longValue();

                    DbDataFieldDataType fieldPk = record.getFieldsData(idxPk);
                    long idPkRec = fieldPk.getNumberData().longValue();

                    if (idFk==idRec)
                        setPk.add( idPkRec );
                }

                Iterator it = setPk.iterator();
                String tempData = "";
                // �������� �� ������ ��������� ������ ������� �������������� ��������� ������
                while (it.hasNext())
                {
                    long pk = ((Long) it.next());

                    for (int i = 0; i < tableData.getRecordsCount(); i++)
                    {
                        DbDataRecordType record = tableData.getRecords(i);

                        DbDataFieldDataType fieldPk = record.getFieldsData(idxPk);
                        long pkTemp = fieldPk.getNumberData().longValue();

                        if (pkTemp==pk)
                        {
                            DbDataFieldDataType fieldData = record.getFieldsData(idx);
                            if (fieldData.getStringData()!=null)
                                tempData += fieldData.getStringData();
                        }
                    }
                }


                if (isDebug)
                    System.out.println("Big text "+tempData);

                PreparedStatement ps1 = null;
                try
                {

                    if (log.isDebugEnabled())
                        log.debug("Start insert data in bigtext field ");

                    int pos = 0;
                    int prevPos = 0;
                    int maxByte = adapter.getMaxLengthStringField();

                    sql_ =
                        "insert into " + big.getSlaveTable() +
                        '(' + big.getSlavePkField() + ',' +
                        big.getSlaveFkField() + ',' +
                        big.getStorageField() + ')' +
                        "values" +
                        "(?,?,?)";

                    if (log.isDebugEnabled())
                        log.debug("insert bigtext. sql 2 - " + sql_);

                    byte b[] = StringTools.getBytesUTF( tempData );

                    ps1 = adapter.conn.prepareStatement(sql_);
                    while ((pos = StringTools.getStartUTF(b, maxByte, pos)) != -1)
                    {
                        if (log.isDebugEnabled())
                            log.debug("Name sequence - " + big.getSequenceName());

                        CustomSequenceType seq = new CustomSequenceType();
                        seq.setSequenceName( big.getSequenceName() );
                        seq.setTableName( big.getSlaveTable() );
                        seq.setColumnName( big.getSlavePkField() );
                        long idSeq = adapter.getSequenceNextValue( seq );

                        if (log.isDebugEnabled())
                            log.debug("Bind param #1" + idSeq);

                        ps1.setLong(1, idSeq);

                        if (log.isDebugEnabled())
                            log.debug("Bind param #2 " + idFk);

                        ps1.setLong(2, idFk);


                        String s = new String(b, prevPos, pos - prevPos, "utf-8");

                        if (log.isDebugEnabled())
                            log.debug("Bind param #3 " + s+(s!=null?", len "+s.length():""));

                        ps1.setString(3, s);

                        if (log.isDebugEnabled())
                            log.debug("Bind param #3 " + s+(s!=null?", len "+s.length():""));

                        if (isDebug && s!=null && s.length()>2000)
                            System.out.println("Do executeUpdate");

                        int count = ps1.executeUpdate();

                        if (log.isDebugEnabled())
                            log.debug("number of updated records - "+count);

//                        ps1.clearParameters();

                        prevPos = pos;

                    } // while ( (pos=StringTools.getStartUTF ...
                }
                finally
                {
                    DatabaseManager.close( ps1 );
                    ps1 = null;
                }
            }
        }
    }

    /**
     *
     * @param connection
     * @param table
     * @return
     */
    public static DbDataTableType getDataTable(Connection connection, DbTableType table)
        throws Exception
    {
        System.out.println("Start get data for table " + table.getName());
        DbDataTableType tableData = new DbDataTableType();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            String sql_ = "select * from " + table.getName();

            ps = connection.prepareStatement(sql_);

            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            if (table.getFieldsCount() != meta.getColumnCount())
            {
                System.out.println("table "+table.getName());
                System.out.println("count field "+table.getFieldsCount());
                System.out.println("meta count field "+meta.getColumnCount());
                for (int k=0; k<table.getFieldsCount(); k++)
                    System.out.println("\tfield "+table.getFields(k).getName());

                throw new Exception("Count for field in ResultSet not equals in DbTableType");
            }

            System.out.println("count of fields " + table.getFieldsCount());

            int countRecords = 0;
            while (rs.next())
            {
                countRecords++;
                DbDataRecordType record = new DbDataRecordType();

                for (int i = 0; i < table.getFieldsCount(); i++)
                {
                    DbFieldType field = table.getFields(i);
                    DbDataFieldDataType fieldData = new DbDataFieldDataType();

                    Object obj = rs.getObject(field.getName());

                    fieldData.setJavaTypeField(field.getJavaType());
//                    fieldData.setSize( field.getSize() );
//                    fieldData.setDecimalDigit( field.getDecimalDigit() );

                    if (obj == null)
                    {
                        fieldData.setIsNull(Boolean.TRUE);
                    }
                    else
                    {
                        fieldData.setIsNull(Boolean.FALSE);

                        switch (field.getJavaType().intValue())
                        {

                            case Types.DECIMAL:
                            case Types.INTEGER:
                            case Types.DOUBLE:
                            case Types.NUMERIC:
                                fieldData.setNumberData(rs.getBigDecimal(field.getName()));
                                break;

                            case Types.CHAR:
                            case Types.VARCHAR:
                                fieldData.setStringData(
//                                    toDB(rs.getString(field.getName()))
                                    rs.getString(field.getName())
                                );
                                break;

                            case Types.DATE:
                            case Types.TIMESTAMP:
                                fieldData.setDateData(rs.getTimestamp(field.getName()));
                                break;

                            case Types.LONGVARCHAR:
                            case Types.LONGVARBINARY:
                                fieldData.setStringData(rs.getString(field.getName()));
                                break;
                            default:
                                System.out.println("Unknown field type. Field '" + field.getName() + "' type '" + field.getJavaStringType() + "'");
                        }
                    }
                    record.addFieldsData(fieldData);
                }
                tableData.addRecords(record);
            }
            System.out.println("count of records " + countRecords);
            return tableData;
        }
        catch (Exception e)
        {
            log.error("Error get data for table " + table.getName(), e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

    }

    /**
     * ���������� ������ ������ �� �������
     * @return java.lang.ArrayList of DbTableType
     */
    public static ArrayList getTableList(DatabaseAdapter adapter, Connection conn1, String schemaPattern, String tablePattern)
    {
        String[] types = {"TABLE"};

        ResultSet meta = null;
        ArrayList v = new ArrayList();
        try
        {
            DatabaseMetaData db = conn1.getMetaData();

            meta = db.getTables(
                null,
                schemaPattern,
                tablePattern,
                types
            );

            while (meta.next())
            {

                DbTableType table = new DbTableType();

                table.setSchema(meta.getString("TABLE_SCHEM"));
                table.setName(meta.getString("TABLE_NAME"));
                table.setType(meta.getString("TABLE_TYPE"));
                table.setRemark(meta.getString("REMARKS"));

                if (log.isDebugEnabled())
                    log.debug("Table - " + table.getName() + "  remak - " + table.getRemark());

                v.add(table);
            }
        }
        catch (Exception e)
        {
            log.error("Error get list of view", e);
//            System.out.println(e.toString());
        }
        return v;
    }

    /**
     *
     * @return
     */
    public static ArrayList getFieldsList(DatabaseAdapter adapter, Connection conn1, String schemaPattern, String tablePattern)
    {
        ArrayList v = new ArrayList();
        DatabaseMetaData db = null;
        ResultSet metaField = null;
        try
        {
            db = conn1.getMetaData();
            metaField = db.getColumns(null, schemaPattern, tablePattern, null);
            while (metaField.next())
            {
                DbFieldType field = new DbFieldType();

                field.setName(metaField.getString("COLUMN_NAME"));
                field.setDataType(metaField.getString("TYPE_NAME"));
                field.setJavaType( RsetTools.getInt(metaField, "DATA_TYPE", Integer.MIN_VALUE) );
                field.setSize( RsetTools.getInt( metaField, "COLUMN_SIZE") );
                field.setDecimalDigit( RsetTools.getInt( metaField, "DECIMAL_DIGITS"));
                field.setNullable( RsetTools.getInt( metaField, "NULLABLE") );
                String defValue = metaField.getString("COLUMN_DEF");
                field.setDefaultValue( defValue==null?null:defValue.trim() );

                switch (field.getJavaType().intValue())
                {

                    case Types.DECIMAL:
                        field.setJavaStringType("java.sql.Types.DECIMAL");
                        break;

                    case Types.NUMERIC:
                        field.setJavaStringType("java.sql.Types.NUMERIC");
                        break;

                    case Types.INTEGER:
                        field.setJavaStringType("java.sql.Types.INTEGER");
                        break;

                    case Types.DOUBLE:
                        field.setJavaStringType("java.sql.Types.DOUBLE");
                        break;

                    case Types.VARCHAR:
                        field.setJavaStringType("java.sql.Types.VARCHAR");
                        break;

                    case Types.CHAR:
                        field.setJavaStringType("java.sql.Types.CHAR");
                        break;

                    case Types.DATE:
                        field.setJavaStringType("java.sql.Types.TIMESTAMP");
                        break;

                    case Types.LONGVARCHAR:
                        // Oracle 'long' fields type
                        field.setJavaStringType("java.sql.Types.LONGVARCHAR");
                        break;

                    case Types.LONGVARBINARY:
                        // Oracle 'long raw' fields type
                        field.setJavaStringType("java.sql.Types.LONGVARBINARY");
                        break;

                    case Types.TIMESTAMP:
                        field.setJavaStringType("java.sql.Types.TIMESTAMP");
//                        System.out.println( "table - "+tablePattern+" field - "+field.getName()+" javaType - " + field.getJavaStringType() );
                        break;

                    default:
                        if (field.getDataType().equals("BLOB"))
                        {
                            field.setJavaType( Types.BLOB );
                            field.setJavaStringType("java.sql.Types.BLOB");
                            break;
                        }
                        if (field.getDataType().equals("CLOB"))
                        {
                            field.setJavaType( Types.CLOB );
                            field.setJavaStringType("java.sql.Types.CLOB");
                            break;
                        }
                        field.setJavaStringType("unknown. schema - " + schemaPattern + " table - " + tablePattern + " field - " + field.getName() + " javaType - " + field.getJavaType());
                        System.out.println("unknown. schema - " + schemaPattern + " table - " + tablePattern + " field - " + field.getName() + " javaType - " + field.getJavaType());
                }

                if (log.isDebugEnabled())
                {
                    log.debug("Field name - " + field.getName());
                    log.debug("Field dataType - " + field.getDataType());
                    log.debug("Field type - " + field.getJavaType());
                    log.debug("Field size - " + field.getSize());
                    log.debug("Field decimalDigit - " + field.getDecimalDigit());
                    log.debug("Field nullable - " + field.getNullable());

                    if (field.getNullable() == DatabaseMetaData.columnNullableUnknown)
                        log.debug("Table " + tablePattern + " field - " + field.getName() + " with unknown nullable status");

                }


//                field.setDefaultValue(metaField.getString("COLUMN_DEF"));

                v.add(field);
            }
        }
        catch (Exception e)
        {
            System.out.println("schemaPattern: " + schemaPattern + ", tablePattern: " + tablePattern);
            System.out.println(ExceptionTools.getStackTrace(e, 100));
        }
        finally
        {
            if (metaField!=null)
            {
                try
                {
                    metaField.close();
                    metaField=null;
                }
                catch(Exception e01){}
            }
        }
        return v;
    }

    /**
     * ���������� ��������� � ��������� ������ �� ������� ���� ������ �� ������� �������
     * �.�. ��� ����������� �� ������� ��������� ������ �������
     * @param connection
     * @param tableName String ��� ������� ��� ������� ������ ��� �����������
     * @return
     */
    public static ArrayList getImportedKeys(Connection connection, String schemaName, String tableName)
    {
        ArrayList v = new ArrayList();
        try
        {
            DatabaseMetaData db = connection.getMetaData();
            ResultSet columnNames = null;

            if (log.isDebugEnabled())
                log.debug("Get data from getImportedKeys");

            try
            {
                columnNames = db.getImportedKeys(null, schemaName, tableName);

                while (columnNames.next())
                {
                    DbImportedPKColumnType impPk = new DbImportedPKColumnType();

                    impPk.setPkSchemaName( columnNames.getString("PKTABLE_SCHEM") );
                    impPk.setPkTableName(columnNames.getString("PKTABLE_NAME"));
                    impPk.setPkColumnName(columnNames.getString("PKCOLUMN_NAME"));

                    impPk.setFkSchemaName( columnNames.getString("FKTABLE_SCHEM") );
                    impPk.setFkTableName(columnNames.getString("FKTABLE_NAME"));
                    impPk.setFkColumnName(columnNames.getString("FKCOLUMN_NAME"));

                    impPk.setKeySeq( RsetTools.getInt( columnNames, "KEY_SEQ") );

                    impPk.setPkName( columnNames.getString("PK_NAME") );
                    impPk.setFkName( columnNames.getString("FK_NAME") );

                    impPk.setUpdateRule(DatabaseManager.decodeUpdateRule(columnNames));
                    impPk.setDeleteRule(DatabaseManager.decodeDeleteRule(columnNames));
                    impPk.setDeferrability(DatabaseManager.decodeDeferrabilityRule(columnNames));

                    if (log.isDebugEnabled())
                    {
                        log.debug(
                            columnNames.getString("PKTABLE_CAT") + " - " +
                            columnNames.getString("PKTABLE_SCHEM") + "." +
                            columnNames.getString("PKTABLE_NAME") +
                            " - " +
                            columnNames.getString("PKCOLUMN_NAME") +
                            " >> " +
                            columnNames.getString("FKTABLE_CAT") + "." +
                            columnNames.getString("FKTABLE_SCHEM") + "." +
                            columnNames.getString("FKTABLE_NAME") +
                            "; " +
                            columnNames.getShort("KEY_SEQ") + " " +
                            columnNames.getString("UPDATE_RULE") + " " +
                            columnNames.getShort("DELETE_RULE") + " ");
                        Object obj = null;
                        int deferr;
                        obj = columnNames.getObject("DELETE_RULE");

                        if (obj == null)
                            deferr = Integer.MIN_VALUE;
                        else
                            deferr = (int) columnNames.getShort("DELETE_RULE");

                        switch (deferr)
                        {
                            case DatabaseMetaData.importedKeyNoAction:
                                log.debug("DELETE_RULE.importedKeyNoAction");
                                break;
                            case DatabaseMetaData.importedKeyCascade:
                                log.debug("DELETE_RULE.importedKeyCascade");
                                break;
                            case DatabaseMetaData.importedKeySetNull:
                                log.debug("DELETE_RULE.importedKeySetNull");
                                break;
                            case DatabaseMetaData.importedKeyRestrict:
                                log.debug("DELETE_RULE.importedKeyRestrict");
                                break;
                            case DatabaseMetaData.importedKeySetDefault:
                                log.debug("DELETE_RULE.importedKeySetDefault");
                                break;
                            default:
                                log.debug("unknown DELETE_RULE(" + deferr + ")");
                                break;
                        }
                        log.debug("obj: " + obj.getClass().getName() + " ");

                        log.debug("Foreign key name: " + columnNames.getString("FK_NAME") + " ");
                        log.debug("Primary key name: " + columnNames.getString("PK_NAME") + " ");

                        obj = columnNames.getObject("DEFERRABILITY");
                        if (obj == null)
                            deferr = -1;
                        else
                            deferr = (int) columnNames.getShort("DEFERRABILITY");

                        switch (deferr)
                        {
                            case DatabaseMetaData.importedKeyInitiallyDeferred:
                                log.debug("importedKeyInitiallyDeferred");
                                break;
                            case DatabaseMetaData.importedKeyInitiallyImmediate:
                                log.debug("importedKeyInitiallyImmediate");
                                break;
                            case DatabaseMetaData.importedKeyNotDeferrable:
                                log.debug("importedKeyNotDeferrable");
                                break;
                            default:
                                log.debug("unknown DEFERRABILITY(" + deferr + ")");
                                break;
                        }

                    }
                    v.add(impPk);
                }
                columnNames.close();
                columnNames = null;

            }
            catch (Exception e1)
            {
                log.debug("Method getImportedKeys(null, null, tableName) not supported", e1);
            }
            log.debug("Done  data from getImportedKeys");

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return v;
    }

    public static DbPrimaryKeyType getPrimaryKey(Connection connection, String schemaPattern, String tablePattern)
    {

        DbPrimaryKeyType pk = new DbPrimaryKeyType();
        ArrayList v = new ArrayList();

        if (log.isDebugEnabled())
            log.debug("Get data from getPrimaryKeys");

        try
        {
            DatabaseMetaData db = connection.getMetaData();
            ResultSet metaData = null;
            metaData = db.getPrimaryKeys(null, schemaPattern, tablePattern);
            while (metaData.next())
            {
                DbPrimaryKeyColumnType pkColumn = new DbPrimaryKeyColumnType();

                pkColumn.setCatalogName(metaData.getString("TABLE_CAT"));
                pkColumn.setSchemaName(metaData.getString("TABLE_SCHEM"));
                pkColumn.setTableName(metaData.getString("TABLE_NAME"));
                pkColumn.setColumnName(metaData.getString("COLUMN_NAME"));
                pkColumn.setKeySeq( RsetTools.getInt( metaData, "KEY_SEQ"));
                pkColumn.setPkName(metaData.getString("PK_NAME"));

                if (log.isDebugEnabled())
                {
                    log.debug(
                        pkColumn.getCatalogName() + "." +
                        pkColumn.getSchemaName() + "." +
                        pkColumn.getTableName() +
                        " - " +
                        pkColumn.getColumnName() +
                        " " +
                        pkColumn.getKeySeq() + " " +
                        pkColumn.getPkName() + " " +
                        ""
                    );
                }
                v.add(pkColumn);
            }
            metaData.close();
            metaData = null;
        }
        catch (Exception e1)
        {
            log.warn("Method db.getPrimaryKeys(null, null, tableName) not supported", e1);
        }

        if (log.isDebugEnabled())
            log.debug("Done data from getPrimaryKeys");

        if (log.isDebugEnabled())
        {
            if (v.size() > 1)
            {
                log.debug("Table with multicolumn PK.");

                for (int i=0; i<v.size(); i++)
                {
                    DbPrimaryKeyColumnType pkColumn = (DbPrimaryKeyColumnType)v.get(i);
                    log.debug(
                        pkColumn.getCatalogName() + "." +
                        pkColumn.getSchemaName() + "." +
                        pkColumn.getTableName() +
                        " - " +
                        pkColumn.getColumnName() +
                        " " +
                        pkColumn.getKeySeq() + " " +
                        pkColumn.getPkName() + " " +
                        ""
                    );
                }
            }
        }
        pk.setColumns(v);

        return pk;
    }

    /**
     * ���������� ������ ������ �� �������
     * ������ schemaPattern ��� ��� �����
     * ����� �������� ���� dc.username.toUpperCase()
     * tablePattern == "%" ���������� ��� ������� ��� �������
     * @return java.lang.Vector of DbTableType
     * @param adapter
     * @param conn
     * @param dc1
     */
    public static ArrayList getTableList(DatabaseAdapter adapter, Connection conn, DatabaseConnectionType dc1)
    {
        return getTableList(adapter, conn, dc1.getUsername().toUpperCase(), "%");
    }

    public static void setDefaultValueTimestamp(DatabaseAdapter adapter, DbTableType originTable, DbFieldType originField)
        throws Exception
    {
        DbFieldType tempField = DatabaseManager.cloneDescriptionField(originField);
        tempField.setName( tempField.getName()+'1');
        adapter.addColumn(originTable, tempField);
        DatabaseManager.copyFieldData( adapter, originTable, originField, tempField);
        dropColumn(adapter, originTable, originField);
        adapter.addColumn(originTable, originField);
        DatabaseManager.copyFieldData( adapter, originTable, tempField, originField );
        dropColumn(adapter, originTable, tempField);
    }

    private static Object syncObj = new Object();
    public static void checkDatabaseStructure(DatabaseAdapter adapater, DatabaseConnectionType dc) throws DatabaseException {
        if (log.isDebugEnabled()) {
            log.debug( "dc.getIsCheckStructure(): " + dc.getIsCheckStructure());
        }

        if (adapater!=null && Boolean.TRUE.equals(dc.getIsCheckStructure()))
        {
            synchronized( syncObj )  {
                if (!Boolean.TRUE.equals(dc.getIsCheckStructure())) {
                    return;
                }

                try {
                    DefinitionService.validateDatabaseStructure( adapater );
                    dc.setIsCheckStructure( Boolean.FALSE );
                }
                catch (FileNotFoundException exception) {
                    log.error("Exception ", exception);
                }
                catch (Exception exception) {
                    final String es = "Exception applay definition to DB";
                    log.error( es, exception);
                    throw new DatabaseException( es, exception );
                }
            }
        }
    }

}
