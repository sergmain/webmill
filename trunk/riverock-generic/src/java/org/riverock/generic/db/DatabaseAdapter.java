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

 * Класс содержащий описания методов для работы с базой данных.

 *

 * $Revision$ $Date$

 */

package org.riverock.generic.db;



import org.riverock.generic.config.GenericConfig;

import org.riverock.generic.schema.config.DatabaseConnectionType;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.generic.schema.db.structure.*;

import org.riverock.generic.db.definition.DefinitionService;

import org.riverock.generic.exception.DatabaseException;

import org.riverock.sql.parser.Parser;

import org.riverock.common.tools.MainTools;

import org.riverock.common.tools.StringTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.config.ConfigException;

import org.riverock.schema.sql.SqlNameType;



import java.sql.*;

import java.util.Calendar;

import java.util.ArrayList;

import java.util.Hashtable;

import java.util.Enumeration;

import java.util.TreeSet;

import java.util.Iterator;

import java.lang.reflect.Method;

import java.lang.reflect.InvocationTargetException;

import java.io.FileNotFoundException;



import org.apache.log4j.Logger;



public abstract class DatabaseAdapter

{

    private static Logger log = Logger.getLogger("org.riverock.generic.db.DatabaseAdapter");



//    private static DatabaseAdapter currentAdapter = null;

//

    public abstract int getFamaly();

    public abstract int getVersion();

    public abstract int getSubVersion();

//

//    public static boolean isNeedValidateStructure = true;

//    public abstract Connection getConnection();

//

//

//    public DatabaseAdapter(){}

//

//    public abstract PreparedStatement prepareStatement(String sql_) throws SQLException;

//    public abstract Statement createStatement()throws SQLException;

//    public abstract void commit() throws SQLException;

//    public abstract void rollback() throws SQLException;

//

//    protected void finalize() throws Throwable

//    {

//        super.finalize();

//    }

//

//    public abstract boolean getIsBatchUpdate();

//    public abstract boolean getIsNeedUpdateBracket();

//    public abstract boolean getIsByteArrayInUtf8();

//

//    /**

//     * Возвращает значение закрыта ли база дынных или нет.

//     * @return boolean. true - коннект к базе закрыт. false - коннект существует.

//     * @throws SQLException

//     */

//    public abstract boolean getIsClosed() throws SQLException;

//    public abstract String getClobField(ResultSet rs, String nameFeld) throws SQLException;

//    public abstract String getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception;

//    public abstract void createTable(DbTableType table) throws Exception;

//    /*

//ALTER TABLE a_test_1

//ADD CONSTRAINT a_test_1_fk FOREIGN KEY (id, id_test)

//REFERENCES a_test (id_test,id_lang) ON DELETE SET NULL

///

//

//ALTER TABLE a_test_1

//ADD CONSTRAINT a_test_1_fk2 FOREIGN KEY (text1, id_text)

//REFERENCES a_test_2 (text2,text_id) ON DELETE CASCADE

//DEFERRABLE INITIALLY DEFERRED

///

//    */

//    public abstract void createForeignKey( DbImportedKeyListType fkList ) throws Exception;

//    public abstract void createView(DbViewType view) throws Exception;

//    public abstract void createSequence(DbSequenceType seq) throws Exception;

//

//    public abstract void dropTable(DbTableType table) throws Exception;

//    public abstract void dropTable(String nameTable) throws Exception;

//    public abstract void dropSequence(String nameSequence) throws Exception;

//

//    public abstract void dropConstraint(DbImportedPKColumnType impPk) throws Exception;

//

//    public abstract void addColumn(String tableName, DbFieldType field) throws Exception;

//    public abstract void addColumn(DbTableType table, DbFieldType field) throws Exception;

//

//    /**

//     * in some DB (Oracle8.0) setTimestamp not work and we need work around

//     * @return String

//     */

//    public abstract String getNameDateBind();

//    public abstract String getOnDeleteSetNull();

//    /**

//     * bind Timestamp value

//     * @param ps

//     * @param stamp @see java.sql.Timestamp

//     * @throws SQLException

//     */

//    public abstract void bindDate(PreparedStatement ps, int idx, Timestamp stamp) throws SQLException;

//    public abstract String getDefaultTimestampValue();

//    public abstract ArrayList getViewList(String schemaPattern, String tablePattern) throws Exception;

//    public abstract ArrayList getSequnceList(String schemaPattern ) throws Exception;

//    public abstract String getViewText( DbViewType view ) throws Exception;

//

//    public abstract void setDefaultValueTimestamp( DbTableType originTable, DbFieldType originField ) throws Exception;

//    public abstract void dropColumn( DbTableType table, DbFieldType field ) throws Exception;

//    public abstract void dropView(DbViewType view) throws Exception;

//    public abstract void setDataTable(DbTableType table) throws Exception;

//    public abstract void setDataTable(DbTableType table, ArrayList bigTables) throws Exception;

//    public abstract void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldDataType fieldData) throws SQLException;

//    public abstract void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData) throws SQLException;

//

//    /**

//     *

//     * @param table

//     * @return

//     */

//    public abstract DbDataTableType getDataTable(DbTableType table) throws Exception;

//

//    /**

//     * Возвращает список таблиц по фильтру

//     * @return java.lang.Vector of DbTableType

//     */

//    public abstract ArrayList getTableList(String schemaPattern, String tablePattern);

//

//    /**

//     *

//     * @return

//     */

//    public abstract ArrayList getFieldsList(String schemaPattern, String tablePattern);

//

//    /**

//     * Возвращает информаци о первичных ключах на которые есть ссылки из текущей таблицы

//     * т.е. все справочники на которые ссылается данная таблица

//     * @param tableName String имя таблицы для которой ищутся все стравочники

//     * @return

//     */

//    public abstract ArrayList getImportedKeys(String schemaName, String tableName);

//    public abstract DbPrimaryKeyType getPrimaryKey(String schemaPattern, String tablePattern);

//

//    /**

//     * Возвращает список таблиц по фильтру

//     * обычно schemaPattern это имя юзера

//     * можно получить типа dc.username.toUpperCase()

//     * tablePattern == "%" обозначает что выбрать все таблицы

//     * @return java.lang.Vector of DbTableType

//     */

//    public abstract ArrayList getTableList();

//

//    /**

//     *

//     * Возвращает список таблиц по фильтру

//     * @param schemaPattern

//     * @param tablePattern

//     * @return java.lang.Vector of DbTableType

//     */

////    public abstract Vector getTableList(String schemaPattern, String tablePattern);

//

//    /**

//     *

//     * @return

//     */

////    public abstract Vector getFieldsList(String schemaPattern, String tablePattern);

//

////    public abstract Vector getImportedKeys(String schemaName, String tableName);

//

////    public abstract DbPrimaryKeyType getPrimaryKey(String schemaPattern, String tablePattern );

//

//    /**

//     *

//     * @param rs

//     * @param nameFeld

//     * @param maxLength

//     * @return

//     * @throws SQLException

//     */

//    public abstract String getClobField(ResultSet rs, String nameFeld, int maxLength)

//        throws SQLException;

//

//    /**

//     * Проверяет является ли ошибка результатом ображения к не существующей в базе таблице.

//     * @param e - Exception

//     * @return boolean. true - если ошибка возникла в результате обращения к не существующей

//     * таблице. Иначе false.

//     */

//    public abstract boolean testExceptionTableNotFound(Exception e);

//

//    /**

//     * Проверяет является ли ошибка результатом попытки вставки дулирующих данных в уникальный ключ

//     * @param e - Exception

//     * @param index - String. имя уникального ключа для проверки что ошибка возникла именно в нем.

//     * @return boolean. true - если ошибка была результатом попытки вставки дулирующих данных

//     * в уникальный ключ. Иначе false.

//     */

//    public abstract boolean testExceptionIndexUniqueKey(Exception e, String index);

//    public abstract boolean testExceptionIndexUniqueKey(Exception e);

//    public abstract boolean testExceptionTableExists(Exception e);

//    public abstract boolean testExceptionViewExists(Exception e);

//    public abstract boolean testExceptionSequenceExists(Exception e);

//    public abstract boolean testExceptionConstraintExists(Exception e);

//

//    /**

//     * Возвращает значение сиквенса(последовательности) для данного имени последовательности.

//     * Для разных коннектов к разным базам данных может быть решена по разному.

//     * @param sequence - String. Имя последовательноти для получения следующего значения.

//     * @return long - следующее значение для ключа из последовательности

//     * @throws SQLException

//     */

//    public abstract long getSequenceNextValue(CustomSequenceType sequence)

//        throws SQLException;

//

//    /**

//     * Конструирует и выполняет запрос к базе базе данных. Если выборка из базы содержит

//     * более одной записи, возвращается значение первой записи в ResultSet.

//     * @param t - String. Строка для 'FROM' выражения

//     * @param f - String. Строка содержащая имя поля, содержащее значение для возврата. Тип

//     * поля в базе данных 'numeric'

//     * @param w - String. Строка для 'WHERE' выражения

//     * @param o - String. Строка для 'ORDER BY' выражения

//     * @return - long.

//     * @throws SQLException

//     */

//    public abstract long getFirstValue(String t, String f, String w, String o)

//        throws SQLException;

//

//    /**

//     * Конструирует и выполняет запрос к базе базе данных. Если выборка из базы содержит

//     * более одной записи, возвращается значение первой записи в ResultSet.

//     * @param t - String. Строка для 'FROM' выражения

//     * @param f - String. Строка содержащая имя поля, содержащее значение для возврата. Тип

//     * поля в базе данных 'numeric'

//     * @param w - String. Строка для 'WHERE' выражения

//     * @param o - String. Строка для 'ORDER BY' выражения

//     * @return - String.

//     * @throws SQLException

//     */

//    public abstract String getFirstValueString(String t, String f, String w, String o)

//        throws SQLException;

//

//    /**

//     * Конструирует и выполняет запрос к базе базе данных. Если выборка из базы содержит

//     * более одной записи, возвращается значение первой записи в ResultSet.

//     * @param t - String. Строка для 'FROM' выражения

//     * @param f - String. Строка содержащая имя поля, содержащее значение для возврата. Тип

//     * поля в базе данных 'numeric'

//     * @param w - String. Строка для 'WHERE' выражения

//     * @param o - String. Строка для 'ORDER BY' выражения

//     * @return - Calendar.

//     * @throws SQLException

//     */

//    public abstract Calendar getFirstValueCalendar(String t, String f, String w, String o)

//        throws SQLException;

//

//    /**

//     * @return - int. Максимальная длина символьного поля

//     */

//    public abstract int getMaxLengthStringField();

//

//    protected abstract void init(DatabaseConnectionType dc_) throws Exception;

//

//    /**

//     * Является ил текущий коннект динамическим или статическим.

//     * @return - boolean. true - коннект динамический. Для корректной работы приложений требуется

//     * принудительное закрытие данного коннекта.

//     *

//     * Пример:

//     * DatabaseAdapter dbDyn = DatabaseAdapter.getInstance( true );

//     * .

//     * DatabaseAdapter.close( dbDyn );

//     * dbDyn = null; // обязательно требуется.

//     */

//    public boolean isDynamic()

//    {

//        return currentAdapter.getIsDynamicConnect();

//    }

//

//    public abstract boolean getIsDynamicConnect();

//

//    /**

//     * Создает новый коннект к базе данных. Если параметер getIsCssDynamic == true, то открывается

//     * новый коннект к базе. Иначе используется статический коннект. При использовании

//     * статического коннекта, jdbc драйвер должен создавать треад-сейв коннекты.

//     * Для создания коннекта используются данные из DatabaseConnection определенные по умолчанию

//     * @param isDynamic - boolean. Нужно создать динамический коннект или статический.

//     * @return - DatabaseAdapter

//     * @throws Exception

//     * @see DatabaseConnectionType

//     */

//    public static DatabaseAdapter getInstance(boolean isDynamic)

//        throws Exception

//    {

//        return currentAdapter.getInstance(isDynamic, GenericConfig.getDefaultConnectionName());

//    }

//

//    /**

//     * Создает новый коннект к базе данных. Если параметер getIsCssDynamic == true, то открывается

//     * новый коннект к базе. Иначе используется статический коннект. При использовании

//     * статического коннекта, jdbc драйвер должен создавать треад-сейв коннекты.

//     * Для создания коннекта используются данные из @link DatabaseConnection.

//     * @param isDynamic - boolean. Нужно создать динамический коннект или статический.

//     * @param connectionName - String. Имя данных конфигурации коннекта к базе.

//     * @return - DatabaseAdapter

//     * @throws Exception

//     */

//    public static DatabaseAdapter getInstance(boolean isDynamic, String connectionName)

//        throws Exception

//    {

//        return currentAdapter.getInstance(isDynamic, connectionName);

//    }

//

//    /**

//     * Закрывает существующий коннект к базе данных, если данный коннект является динамическим.

//     * @param db_ - DatabaseAdapter. Коннект к базе для закрытия.

//     */

//    public static void close(DatabaseAdapter db_)

//    {

//        currentAdapter.close(db_);

//    }





    protected static Hashtable connectHashtable = new Hashtable();



    protected static Boolean initFlag = new Boolean(false);



    public boolean isDBOk = false;

    public static boolean isNeedValidateStructure = true;



    public Connection conn = null;



    public Connection getConnection()

    {

        return conn;

    }



    protected Hashtable tables = new Hashtable();

    public DatabaseAdapter(){}



    public PreparedStatement prepareStatement(String sql_)

        throws SQLException

    {

        try

        {

            Parser parser = org.riverock.sql.cache.SqlStatement.parseSql(sql_);

            if (!isDynamicConnect && parser.typeStatement!=Parser.SELECT)

                throw new Exception("INSERT/UPDATE/DELETE statement can not be processed with static DatabaseAdapter. You must use DatabaseAdapter.getInstance(true)");



            if (log.isDebugEnabled())

                log.debug("parser.typeStatement!=Parser.SELECT - "+(parser.typeStatement!=Parser.SELECT));



            if (parser.typeStatement!=Parser.SELECT)

            {

                for (int i=0; i<parser.depend.getTarget().getItemCount();i++)

                {

                    String name = parser.depend.getTarget().getItem(i).getOriginName();



                    if (log.isDebugEnabled())

                        log.debug("Name lookung table - "+name);



                    String nameTable = (String)tables.get( name );

                    if (log.isDebugEnabled())

                        log.debug("searching table "+name+" in hash - "+nameTable);



                    if (nameTable==null)

                        tables.put(name, name);

                }

            }

        }

        catch(Exception e)

        {

            log.error("Error parse SQL "+sql_, e);

//            throw new SQLException(e.toString());

        }

        return conn.prepareStatement(sql_);

    }



    public Statement createStatement()

        throws SQLException

    {

        return conn.createStatement();

    }



    private static Object syncCommit = new Object();

    public void commit()

        throws SQLException

    {

        conn.commit();

        if (log.isDebugEnabled())

            log.debug("Start sync cache. DB action - COMMIT");



        synchronized(syncCommit)

        {

            try

            {

                if (log.isDebugEnabled())

                    log.debug("Count of changed tables - "+tables.size());



                for (Enumeration e = tables.keys() ; e.hasMoreElements() ;)

                {

                    String tableName = (String)e.nextElement();



                    if (log.isDebugEnabled())

                    {

                        log.debug("process cache for table "+tableName);

                        log.debug("count of class in hash "+org.riverock.sql.cache.SqlStatement.classHash.size());

                    }



                    for (Enumeration e1 = org.riverock.sql.cache.SqlStatement.classHash.keys() ; e1.hasMoreElements() ;)

                    {

                        String className = (String)e1.nextElement();

                        Object obj = org.riverock.sql.cache.SqlStatement.classHash.get(className);



                        if (log.isDebugEnabled())

                        {

                            log.debug("-- Start new check");

                            log.debug("class - "+className+", obj "+obj);

                        }



                        boolean isDependent = false;

                        if (obj==null)

                            continue;



                        if (obj instanceof ArrayList)

                        {

                            for (int j=0; j<((ArrayList)obj).size(); j++)

                            {

                                Parser checkParser = (Parser)((ArrayList)obj).get(j);

                                isDependent = checkDependence( checkParser, tableName );

                                if (isDependent)

                                    break;

                            }

                        }

                        else if (obj instanceof ArrayList)

                        {

                            for (int j=0; j<((ArrayList)obj).size(); j++)

                            {

                                Parser checkParser = (Parser)((ArrayList)obj).get(j);

                                isDependent = checkDependence( checkParser, tableName );

                                if (isDependent)

                                    break;

                            }

                        }

                        else if (obj instanceof Parser)

                            isDependent = checkDependence( (Parser)obj, tableName );

                        else

                        {

                            String errorString = "Object in hash is "+obj.getClass().getName();

                            log.error(errorString);

                            throw new Exception(errorString);

                        }



                        if (log.isDebugEnabled())

                            log.debug("isDependent - "+isDependent);



                        if (isDependent)

                            reinitClass( className );

                    }

                }

                tables.clear();

            }

            catch(Exception ex)

            {

                log.error("Error reinit cache", ex);

                throw new SQLException(ex.toString());

            }

        }

    }



    private static void reinitRelateClass( String className ) throws Exception

    {

        try

        {

            Object relateObject = org.riverock.sql.cache.SqlStatement.classRelateHash.get( className );



            if (log.isDebugEnabled())

                log.debug("relate class for class - "+className+" is "+relateObject);



            if (relateObject==null)

                return;



            if (relateObject instanceof ArrayList)

            {

                for (int j=0; j<((ArrayList)relateObject).size(); j++)

                {

                    String relateClassName = (String)((ArrayList)relateObject).get(j);

                    reinitClass( relateClassName );

                }

            }

            else if (relateObject instanceof ArrayList)

            {

                for (int j=0; j<((ArrayList)relateObject).size(); j++)

                {

                    String relateClassName = (String)((ArrayList)relateObject).get(j);

                    reinitClass( relateClassName );

                }

            }

            else

            {

                if (log.isDebugEnabled() && relateObject!=null)

                    log.debug("call reinitClass() with object "+relateObject.getClass().getName());



                reinitClass( (String)relateObject );

            }

        }

        catch(Exception ex)

        {

            log.error("Error in reinitRelateClass", ex);

            throw new SQLException(ex.toString());

        }

    }



    private static void reinitClass( String className ) throws Exception

    {

        if (log.isDebugEnabled())

        {

            log.debug("reinit class - "+className);

            Long maxMemory = MainTools.getMaxMemory();



            log.debug(

                "free memory " + Runtime.getRuntime().freeMemory()+

                " total memory "+ Runtime.getRuntime().totalMemory()+

                (maxMemory!=null?" max memory "+maxMemory:"")

            );

        }

        try

        {

            Object objTemp = MainTools.createCustomObject( className );

            Method method1 = objTemp.getClass().getMethod("reinit", null);



            if (log.isDebugEnabled())

                log.debug("#2.2.009  method1 is " + method1);



            if (method1 != null)

            {

                method1.invoke(objTemp, null);



                if (log.isDebugEnabled())

                    log.debug("#2.2.010 ");

            }

            reinitRelateClass( className );

        }

        catch(NoSuchMethodException noSuchExc)

        {

            log.error("Error in reinitClass "+className, noSuchExc);

        }

        catch(InvocationTargetException envE)

        {

            log.error("Error in reinitClass "+className, envE);

        }

        catch(Exception ex)

        {

            log.error("Error in reinitClass "+className, ex);

            throw new SQLException(ex.toString());

        }

    }



    private boolean checkDependence( Parser checkParser, String name )

    {

        if (checkParser!=null)

        {

            for (int k=0; k<checkParser.depend.getSource().getItemCount(); k++)

            {

                SqlNameType checkName = checkParser.depend.getSource().getItem(k);



                if (checkName.getIsNameQuoted())

                {

                    if (name.equals(checkName.getOriginName()))

                        return true;

                }

                else

                {

                    if (name.equalsIgnoreCase(checkName.getOriginName()))

                        return true;

                }

            }

        }

        return false;

    }





    private static Object syncRollback = new Object();

    public void rollback()

        throws SQLException

    {

        conn.rollback();

        synchronized(syncRollback)

        {

            tables.clear();

        }

    }



    protected boolean isDriverLoaded = false;



    public DatabaseConnectionType dc = null;



    protected void finalize() throws Throwable

    {

        if (conn != null)

        {

            try

            {

                conn.close();

            }

            catch (Exception e)

            {

            }

            conn = null;

        }

        dc = null;



        super.finalize();

    }



//    non dynamic connect used for 'select' operation

//    dynamic used for 'update,delete,insert' operation

    public boolean getIsDynamicConnect()

    {

        return isDynamicConnect;

    }



    public void setIsDynamicConnect(boolean dynamicConnect)

    {

        isDynamicConnect = dynamicConnect;

    }



    protected boolean isDynamicConnect = false;



    public abstract boolean getIsBatchUpdate();

    public abstract boolean getIsNeedUpdateBracket();

    public abstract boolean getIsByteArrayInUtf8();



    /**

     * Возвращает значение закрыта ли база дынных или нет.

     * @return boolean. true - коннект к базе закрыт. false - коннект существует.

     * @throws SQLException

     */

    public abstract boolean getIsClosed() throws SQLException;

    public abstract String getClobField(ResultSet rs, String nameFeld) throws SQLException;

    public abstract String getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception;





    public abstract void createTable(DbTableType table) throws Exception;

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

    public void createForeignKey( DbImportedKeyListType fkList ) throws Exception

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



                sql += column.getPkColumnName();

            }

            sql += ") ";

            switch( fkColumn.getDeleteRule().getRuleType().intValue() )

            {

                case DatabaseMetaData.importedKeyRestrict:

                    sql += getOnDeleteSetNull();

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

                ps = this.conn.prepareStatement(sql);

                ps.executeUpdate();

            }

            catch(SQLException exc)

            {

                if (!testExceptionTableExists(exc))

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

    public abstract void createView(DbViewType view) throws Exception;

    public abstract void createSequence(DbSequenceType seq) throws Exception;



    public abstract void dropTable(DbTableType table) throws Exception;

    public abstract void dropTable(String nameTable) throws Exception;

    public abstract void dropSequence(String nameSequence) throws Exception;



    public abstract void dropConstraint(DbImportedPKColumnType impPk) throws Exception;



    public void addColumn(String tableName, DbFieldType field) throws Exception

    {

        DbTableType table = new DbTableType();

        table.setName( tableName );

        addColumn( table, field );

    }

    public abstract void addColumn(DbTableType table, DbFieldType field) throws Exception;



    /**

     * in some DB (Oracle8.0) setTimestamp not work and we need work around

     * @return String

     */

    public abstract String getNameDateBind();

    public abstract String getOnDeleteSetNull();

    /**

     * bind Timestamp value

     * @param ps

     * @param stamp @see java.sql.Timestamp

     * @throws SQLException

     */

    public abstract void bindDate(PreparedStatement ps, int idx, Timestamp stamp) throws SQLException;

    public abstract String getDefaultTimestampValue();

    public abstract ArrayList getViewList(String schemaPattern, String tablePattern) throws Exception;

    public abstract ArrayList getSequnceList(String schemaPattern ) throws Exception;

    public abstract String getViewText( DbViewType view ) throws Exception;



    public void setDefaultValueTimestamp( DbTableType originTable, DbFieldType originField )

        throws Exception

    {

        DbFieldType tempField = DatabaseManager.cloneDescriptionField(originField);

        tempField.setName( tempField.getName()+'1');

        addColumn(originTable, tempField);

        DatabaseManager.copyFieldData( this, originTable, originField, tempField);

        dropColumn(originTable, originField);

        addColumn(originTable, originField);

        DatabaseManager.copyFieldData( this, originTable, tempField, originField );

        dropColumn(originTable, tempField);

    }



    public void dropColumn( DbTableType table, DbFieldType field )

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

            ps = this.conn.prepareStatement(sql_);

            ps.executeUpdate();

        }

        finally

        {

            DatabaseManager.close( ps );

            ps = null;

        }

    }



    public void dropView(DbViewType view)

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

            ps = this.conn.prepareStatement(sql_);

            ps.executeUpdate();

        }

        finally

        {

            DatabaseManager.close( ps );

            ps = null;

        }

    }



    public void setDataTable(DbTableType table)

        throws Exception

    {

        setDataTable(table, null);

    }



    public void setDataTable(DbTableType table, ArrayList bigTables)

        throws Exception

    {

        DbBigTextTableType big = DatabaseManager.getBigTextTableDesc(table, bigTables);



        if (table.getFieldsCount() == 0)

            throw new Exception("Table has zero count of fields");





        boolean isDebug = true;

        if (table.getName().equalsIgnoreCase("MAIN_NEWS_TEXT"))

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



            if (field.getJavaType().intValue()!=Types.DATE && field.getJavaType().intValue()!=Types.TIMESTAMP )

                sql_ += " ?";

            else

                sql_ += getNameDateBind();

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

                    ps = conn.prepareStatement(sql_);



                    for (int k = 0; k < record.getFieldsDataCount(); k++)

                    {

                        DbDataFieldDataType fieldData = record.getFieldsData(k);



                        if (fieldData.getIsNull().booleanValue() )

                        {

                            int type = fieldData.getJavaTypeField().intValue();

                            if (fieldData.getJavaTypeField().intValue()==Types.TIMESTAMP)

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

                                    if ( fieldData.getDecimalDigit().intValue()==0 )

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

                                    bindDate(ps, k+1, stamp);

                                    ps.setTimestamp(k+1, stamp);

                                    break;



                                case Types.LONGVARCHAR:

                                    setLongVarchar(ps, k+1, fieldData);

                                    break;



                                case Types.LONGVARBINARY:

                                    setLongVarbinary(ps, k+1, fieldData);

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

            // находим индексы полей в списке

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

            // получаем хеш вторичных ключей. Т.к. используется Hashtable

            // все значения в хеше уникальны

            for (int i = 0; i < tableData.getRecordsCount(); i++)

            {

                DbDataRecordType record = tableData.getRecords(i);



                DbDataFieldDataType fieldFk = record.getFieldsData(idxFk);

                Long idRec = new Long(fieldFk.getNumberData().longValue());



                hashFk.put(idRec, new Object());

            }



            // вставляем записи двигаясь по списку вторичных ключей

            for (Enumeration e = hashFk.keys() ; e.hasMoreElements() ;)

            {

                Long idFk = (Long)e.nextElement();



                if (isDebug)

                    System.out.println("ID of fk "+idFk);



                TreeSet setPk = new TreeSet();



                // Создаем список упорядоченных первичных ключей

                // для данного вторичного ключа

                for (int i = 0; i < tableData.getRecordsCount(); i++)

                {

                    DbDataRecordType record = tableData.getRecords(i);



                    DbDataFieldDataType fieldFk = record.getFieldsData(idxFk);

                    long idRec = fieldFk.getNumberData().longValue();



                    DbDataFieldDataType fieldPk = record.getFieldsData(idxPk);

                    long idPkRec = fieldPk.getNumberData().longValue();



                    if (idFk.longValue()==idRec)

                        setPk.add( new Long( idPkRec ) );

                }



                Iterator it = setPk.iterator();

                String tempData = "";

                // двигаясь по списку первичных ключей создаем результирующий строковый объект

                while (it.hasNext())

                {

                    long pk = ((Long)it.next()).longValue();



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

                    int maxByte = getMaxLengthStringField();



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



                    ps1 = conn.prepareStatement(sql_);

                    while ((pos = StringTools.getStartUTF(b, maxByte, pos)) != -1)

                    {

                        if (log.isDebugEnabled())

                            log.debug("Name sequence - " + big.getSequenceName());



                        CustomSequenceType seq = new CustomSequenceType();

                        seq.setSequenceName( big.getSequenceName() );

                        seq.setTableName( big.getSlaveTable() );

                        seq.setColumnName( big.getSlavePkField() );

                        long idSeq = getSequenceNextValue( seq );



                        if (log.isDebugEnabled())

                            log.debug("Bind param #1" + idSeq);



                        ps1.setLong(1, idSeq);



                        if (log.isDebugEnabled())

                            log.debug("Bind param #2 " + idFk.longValue());



                        ps1.setLong(2, idFk.longValue());





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



    public abstract void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldDataType fieldData)

            throws SQLException;



    public abstract void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData)

            throws SQLException;



    /**

     *

     * @param table

     * @return

     */

    public DbDataTableType getDataTable(DbTableType table)

        throws Exception

    {

        System.out.println("Start get data for table " + table.getName());

        DbDataTableType tableData = new DbDataTableType();



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            String sql_ = "select * from " + table.getName();



            ps = conn.prepareStatement(sql_);



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



            while (rs.next())

            {

                DbDataRecordType record = new DbDataRecordType();



                for (int i = 0; i < table.getFieldsCount(); i++)

                {

                    DbFieldType field = table.getFields(i);

                    DbDataFieldDataType fieldData = new DbDataFieldDataType();



                    Object obj = rs.getObject(field.getName());



                    fieldData.setJavaTypeField(field.getJavaType());

                    fieldData.setSize( field.getSize() );

                    fieldData.setDecimalDigit( field.getDecimalDigit() );



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

                                break;

                            default:

                                System.out.println("Unknown field type. Field '" + field.getName() + "' type '" + field.getJavaStringType() + "'");

                        }

                    }

                    record.addFieldsData(fieldData);

                }

                tableData.addRecords(record);

            }

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

     * Возвращает список таблиц по фильтру

     * @return java.lang.Vector of DbTableType

     */

    public ArrayList getTableList(String schemaPattern, String tablePattern)

    {

        String[] types = {"TABLE"};



        ResultSet meta = null;

        ArrayList v = new ArrayList();

        try

        {

            DatabaseMetaData db = conn.getMetaData();



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



/*

    private Vector getAbstractTableList(String schemaPattern, String tablePattern, String[] types)

    {



        ResultSet meta = null;

        Vector v = new Vector();

        try

        {

            DatabaseMetaData db = conn.getMetaData();



            meta = db.getTables(

                null,

                schemaPattern,

                tablePattern,

                types

            );



            while (meta.next())

            {



//                DbTableType table = new DbTableType();

                DbAbstractTableType table = new DbAbstractTableType();



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

        }

        return v;

    }

*/



    /**

     *

     * @return

     */

    public ArrayList getFieldsList(String schemaPattern, String tablePattern)

    {

        ArrayList v = new ArrayList();

        DatabaseMetaData db = null;

        ResultSet metaField = null;

        try

        {

            db = conn.getMetaData();

            metaField = db.getColumns(null, schemaPattern, tablePattern, null);

            while (metaField.next())

            {

                DbFieldType field = new DbFieldType();



                field.setName(metaField.getString("COLUMN_NAME"));

                field.setDataType(metaField.getString("TYPE_NAME"));

                field.setJavaType( RsetTools.getInt(metaField, "DATA_TYPE", new Integer(Integer.MIN_VALUE)) );

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

                            field.setJavaType( new Integer(Types.BLOB) );

                            field.setJavaStringType("java.sql.Types.BLOB");

                            break;

                        }

                        if (field.getDataType().equals("CLOB"))

                        {

                            field.setJavaType( new Integer(Types.CLOB) );

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



                    if (field.getNullable().intValue() == DatabaseMetaData.columnNullableUnknown)

                        log.debug("Table " + tablePattern + " field - " + field.getName() + " with unknown nullable status");



                }





//                field.setDefaultValue(metaField.getString("COLUMN_DEF"));



                v.add(field);

            }

        }

        catch (Exception e)

        {

            System.out.println(ExceptionTools.getStackTrace(e, 30));

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

     * Возвращает информаци о первичных ключах на которые есть ссылки из текущей таблицы

     * т.е. все справочники на которые ссылается данная таблица

     * @param tableName String имя таблицы для которой ищутся все стравочники

     * @return

     */

    public ArrayList getImportedKeys(String schemaName, String tableName)

    {

        ArrayList v = new ArrayList();

        try

        {

            DatabaseMetaData db = conn.getMetaData();

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



                    impPk.setUpdateRule(decodeUpdateRule(columnNames));

                    impPk.setDeleteRule(decodeDeleteRule(columnNames));

                    impPk.setDeferrability(decodeDeferrabilityRule(columnNames));



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



    public DbPrimaryKeyType getPrimaryKey(String schemaPattern, String tablePattern)

    {



        DbPrimaryKeyType pk = new DbPrimaryKeyType();

        ArrayList v = new ArrayList();



        if (log.isDebugEnabled())

            log.debug("Get data from getPrimaryKeys");



        try

        {

            DatabaseMetaData db = conn.getMetaData();

            ResultSet metaData = null;

            metaData = db.getPrimaryKeys(null, schemaPattern, tablePattern);

/*

                rsmd = columnNames.getMetaData();

                numberOfColumns = rsmd.getColumnCount();

                for (int j = 1; j <= numberOfColumns; j++)

                {

                    log.debug("" + j + ". " + rsmd.getColumnName(j));

                }

*/

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

     * Возвращает список таблиц по фильтру

     * обычно schemaPattern это имя юзера

     * можно получить типа dc.username.toUpperCase()

     * tablePattern == "%" обозначает что выбрать все таблицы

     * @return java.lang.Vector of DbTableType

     */

    public ArrayList getTableList()

    {

        return getTableList(dc.getUsername().toUpperCase(), "%");

    }



    /**

     *

     * Возвращает список таблиц по фильтру

     * @param schemaPattern

     * @param tablePattern

     * @return java.lang.Vector of DbTableType

     */

//    public abstract Vector getTableList(String schemaPattern, String tablePattern);



    /**

     *

     * @return

     */

//    public abstract Vector getFieldsList(String schemaPattern, String tablePattern);



//    public abstract Vector getImportedKeys(String schemaName, String tableName);



//    public abstract DbPrimaryKeyType getPrimaryKey(String schemaPattern, String tablePattern );



    protected static DbKeyActionRuleType decodeUpdateRule(ResultSet rs)

    {

        try

        {

            Object obj = rs.getObject("UPDATE_RULE");

            if (obj == null)

                return null;



            DbKeyActionRuleType rule = new DbKeyActionRuleType();

            rule.setRuleType( RsetTools.getInt( rs, "UPDATE_RULE"));



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



    protected static DbKeyActionRuleType decodeDeleteRule(ResultSet rs)

    {

        try

        {

            Object obj = rs.getObject("DELETE_RULE");

            if (obj == null)

                return null;



            DbKeyActionRuleType rule = new DbKeyActionRuleType();

            rule.setRuleType( RsetTools.getInt(rs, "DELETE_RULE"));



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



    protected static DbKeyActionRuleType decodeDeferrabilityRule(ResultSet rs)

    {

        try

        {

            Object obj = rs.getObject("DEFERRABILITY");

            if (obj == null)

                return null;



            DbKeyActionRuleType rule = new DbKeyActionRuleType();

            rule.setRuleType(RsetTools.getInt(rs, "DEFERRABILITY"));



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





    /**

     *

     * @param rs

     * @param nameFeld

     * @param maxLength

     * @return

     * @throws SQLException

     */

    public abstract String getClobField(ResultSet rs, String nameFeld, int maxLength)

        throws SQLException;



    /**

     * Проверяет является ли ошибка результатом ображения к не существующей в базе таблице.

     * @param e - Exception

     * @return boolean. true - если ошибка возникла в результате обращения к не существующей

     * таблице. Иначе false.

     */

    public abstract boolean testExceptionTableNotFound(Exception e);



    /**

     * Проверяет является ли ошибка результатом попытки вставки дулирующих данных в уникальный ключ

     * @param e - Exception

     * @param index - String. имя уникального ключа для проверки что ошибка возникла именно в нем.

     * @return boolean. true - если ошибка была результатом попытки вставки дулирующих данных

     * в уникальный ключ. Иначе false.

     */

    public abstract boolean testExceptionIndexUniqueKey(Exception e, String index);

    public abstract boolean testExceptionIndexUniqueKey(Exception e);

    public abstract boolean testExceptionTableExists(Exception e);

    public abstract boolean testExceptionViewExists(Exception e);

    public abstract boolean testExceptionSequenceExists(Exception e);

    public abstract boolean testExceptionConstraintExists(Exception e);



    /**

     * Возвращает значение сиквенса(последовательности) для данного имени последовательности.

     * Для разных коннектов к разным базам данных может быть решена по разному.

     * @param sequence - String. Имя последовательноти для получения следующего значения.

     * @return long - следующее значение для ключа из последовательности

     * @throws SQLException

     */

    public abstract long getSequenceNextValue(CustomSequenceType sequence)

        throws SQLException;



    /**

     * Конструирует и выполняет запрос к базе базе данных. Если выборка из базы содержит

     * более одной записи, возвращается значение первой записи в ResultSet.

     * @param t - String. Строка для 'FROM' выражения

     * @param f - String. Строка содержащая имя поля, содержащее значение для возврата. Тип

     * поля в базе данных 'numeric'

     * @param w - String. Строка для 'WHERE' выражения

     * @param o - String. Строка для 'ORDER BY' выражения

     * @return - long.

     * @throws SQLException

     */

    public abstract long getFirstValue(String t, String f, String w, String o)

        throws SQLException;



    /**

     * Конструирует и выполняет запрос к базе базе данных. Если выборка из базы содержит

     * более одной записи, возвращается значение первой записи в ResultSet.

     * @param t - String. Строка для 'FROM' выражения

     * @param f - String. Строка содержащая имя поля, содержащее значение для возврата. Тип

     * поля в базе данных 'numeric'

     * @param w - String. Строка для 'WHERE' выражения

     * @param o - String. Строка для 'ORDER BY' выражения

     * @return - String.

     * @throws SQLException

     */

    public abstract String getFirstValueString(String t, String f, String w, String o)

        throws SQLException;



    /**

     * Конструирует и выполняет запрос к базе базе данных. Если выборка из базы содержит

     * более одной записи, возвращается значение первой записи в ResultSet.

     * @param t - String. Строка для 'FROM' выражения

     * @param f - String. Строка содержащая имя поля, содержащее значение для возврата. Тип

     * поля в базе данных 'numeric'

     * @param w - String. Строка для 'WHERE' выражения

     * @param o - String. Строка для 'ORDER BY' выражения

     * @return - Calendar.

     * @throws SQLException

     */

    public abstract Calendar getFirstValueCalendar(String t, String f, String w, String o)

        throws SQLException;



    /**

     * @return - int. Максимальная длина символьного поля

     */

    public abstract int getMaxLengthStringField();



    protected abstract void init(DatabaseConnectionType dc_) throws SQLException, ClassNotFoundException;





//    public static void setDBconnectClassName(String s)

//    {

//        DBconnectClass_ = s;

//    }



    protected static String getDBconnectClassName(String connectionName)

        throws ConfigException

    {

        return GenericConfig.getDBconnectClassName(connectionName);



//        if (DBconnectClass_ == null)

//            DBconnectClass_ = GenericConfig.getDBconnectClassName(connectionName);



//        return DBconnectClass_;

    }



    /**

     * Является ил текущий коннект динамическим или статическим.

     * @return - boolean. true - коннект динамический. Для корректной работы приложений требуется

     * принудительное закрытие данного коннекта.

     *

     * Пример:

     * DatabaseAdapter dbDyn = DatabaseAdapter.getInstance( true );

     * .

     * DatabaseAdapter.close( dbDyn );

     * dbDyn = null; // обязательно требуется.

     */

    public boolean isDynamic()

    {

        return isDynamicConnect;

    }





    /**

     * Закрытие текущего коннекта.

     */

/*

    public synchronized static void terminateConnection()

    {

        if (_db_ != null && _db_.conn != null)

        {

            try

            {

                _db_.conn.close();

                _db_.conn = null;

            }

            catch (SQLException e)

            {

            }

        }

        _db_ = null;

    }

*/



    protected static DBconnect openDynamicConnect(String connectionName)

        throws DatabaseException, ConfigException

    {

        DBconnect db_ = null;



        String connName = null;

        try

        {

            connName = getDBconnectClassName(connectionName);

            if (log.isDebugEnabled())

            {

                System.out.println("Call for create dynamic object " + connName);

                log.debug("Call for create dynamic object " + connName);

            }



            db_ = (DBconnect) MainTools.createCustomObject(connName);

            db_.init(GenericConfig.getDatabaseConnection(connectionName));

            db_.conn.setAutoCommit(false);

            db_.isDBOk = true;

            db_.isDynamicConnect = true;



            if (log.isDebugEnabled())

            {

                System.out.println("Success create dynamic object " + connName);

                log.debug("Success create dynamic object " + connName);

            }

        }

        catch (ConfigException e)

        {

            log.error("ConfigException while get new connection()", e);

            throw e;

        }

        catch (Exception e)

        {

            if (db_ != null && db_.conn != null)

            {

                try

                {

                    db_.conn.close();

                    db_.conn = null;

                }

                catch (Exception e02)

                {

                }

            }

            db_ = null;



            log.fatal("Error create instance for class " + connName);

            log.fatal("ConnectionName - " + connectionName);

            log.fatal("Error:", e);



            System.out.println("\nError create instance for class " + connName + "\nSee log for details");

            throw new DatabaseException("Exception in create connection "+e.toString());

        }

        return db_;

    }



    private static DBconnect openConnectPrivate(String connectionName )

            throws DatabaseException, ConfigException

    {

        String connName = getDBconnectClassName(connectionName);



        synchronized (initFlag)

        {



            DBconnect _db_ = null;

            if (!initFlag.booleanValue())

            {

                initFlag = new Boolean(true);

                try

                {



                    for (int i = 0; i < 3; i++)

                    {

                        try

                        {

                            if (log.isDebugEnabled())

                            {

                                System.out.println("Call for create static object " + connName);

                                log.debug("Call for create static object " + connName);

                            }



                            _db_ = (DBconnect) MainTools.createCustomObject(connName);

                            _db_.init(GenericConfig.getDatabaseConnection(connectionName));

                            _db_.isDBOk = true;

                            _db_.isDynamicConnect = false;



                            if (log.isDebugEnabled())

                            {

                                System.out.println("Success create static object " + connName);

                                log.debug("Success create static object " + connName);

                            }

                            break;

                        }

                        catch (Exception e)

                        {

                            log.fatal("Error create instance for class " + connName);

                            log.fatal("ConnectionName - " + connectionName);

                            log.fatal("Error:", e);



                            System.out.println("\nError create instance for class " + connName + "\nSee log for details");

                            System.out.println("\nconnectionName - " + connectionName);

                            if (_db_ != null && _db_.conn != null)

                            {

                                try

                                {

                                    _db_.conn.close();

                                    _db_.conn = null;

                                }

                                catch (Exception e02){}

                            }

                            _db_ = null;

                        }

                    }

                } // try

                finally

                {

                    initFlag = new Boolean(false);

                }

            } // if (!initFlag)



            if (_db_!=null && isNeedValidateStructure)

            {

                try

                {

                    DefinitionService.validateDatabaseStructure( _db_ );

                }

                catch (FileNotFoundException exception)

                {

                    log.error("Exception ", exception);

                }

                catch (Exception exception)

                {

                    log.error("Exception in ", exception);

                    throw new DatabaseException("Exception applay definition to DB"+exception.toString());

                }

            }



            return _db_;

        } // synchronized (initFlag)

    }



    protected synchronized static DBconnect openConnect(String connectionName)

        throws DatabaseException, ConfigException

    {

        if (connectionName==null)

        {

            log.error("Call DatabaseAdapter.openConnect(String connectionName) with connectionName==null");

            return null;

        }



        DBconnect __db__ = (DBconnect)connectHashtable.get(connectionName);



        boolean isNew = true;

        if (__db__ == null)

        {

            __db__ = openConnectPrivate(connectionName);

        }

        else

        {

            try

            {

                // проверяем что данный коннект к базе не закрыт

                // если закрыт, то удаляем текущий объект и создаем новый

                if (__db__.getIsClosed())

                {

                    try

                    {

                        __db__.conn.close();

                        __db__.conn = null;

                        __db__ = null;

                    }

                    catch (Exception e01){}



                    __db__ = openConnectPrivate(connectionName);

                }

                else

                    isNew = false;

            }

            catch (SQLException e1)

            {

                connectHashtable.remove( connectionName );

                if (__db__ != null && __db__.conn != null)

                {

                    try

                    {

                        __db__.conn.close();

                        __db__.conn = null;

                    }

                    catch (Exception e01){}

                }

                __db__ = null;

            }

        }



        if (isNew && __db__ != null)

            connectHashtable.put(connectionName, __db__);



        return __db__;

    }



    /**

     * Создает новый коннект к базе данных. Если параметер getIsCssDynamic == true, то открывается

     * новый коннект к базе. Иначе используется статический коннект. При использовании

     * статического коннекта, jdbc драйвер должен создавать треад-сейв коннекты.

     * Для создания коннекта используются данные из DatabaseConnection определенные по умолчанию

     * @param isDynamic - boolean. Нужно создать динамический коннект или статический.

     * @return - DatabaseAdapter

     * @throws DatabaseException

     * @throws ConfigException

     * @see DatabaseConnectionType

     */

    public static DatabaseAdapter getInstance(boolean isDynamic)

        throws DatabaseException, ConfigException

    {

        return getInstance(isDynamic, GenericConfig.getDefaultConnectionName());

    }



    /**

     * Создает новый коннект к базе данных. Если параметер getIsCssDynamic == true, то открывается

     * новый коннект к базе. Иначе используется статический коннект. При использовании

     * статического коннекта, jdbc драйвер должен создавать треад-сейв коннекты.

     * Для создания коннекта используются данные из @link DatabaseConnection.

     * @param isDynamic - boolean. Нужно создать динамический коннект или статический.

     * @param connectionName - String. Имя данных конфигурации коннекта к базе.

     * @return - DatabaseAdapter

     * @throws DatabaseException

     * @throws ConfigException

     */

    public static DatabaseAdapter getInstance(boolean isDynamic, String connectionName)

        throws DatabaseException, ConfigException

    {

        if (isDynamic)

            return openDynamicConnect(connectionName);

        else

            return openConnect(connectionName);

    }



    /**

     * Закрывает существующий коннект к базе данных, если данный коннект является динамическим.

     * @param db_ - DatabaseAdapter. Коннект к базе для закрытия.

     */

    public static void close(DatabaseAdapter db_)

    {

        if (db_ == null)

            return;



        if (db_.isDynamicConnect)

        {

            try

            {

                db_.conn.close();

                db_.conn = null;

            }

            catch (Exception e)

            {

            }

        }

    }



}

