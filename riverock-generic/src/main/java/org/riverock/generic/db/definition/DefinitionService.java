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
package org.riverock.generic.db.definition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.DataDefinitionActionDataListType;
import org.riverock.generic.schema.db.DataDefinitionActionDataType;
import org.riverock.generic.schema.db.DataDefinitionActionListType;
import org.riverock.generic.schema.db.DataDefinitionActionType;
import org.riverock.generic.schema.db.DataDefinitionListType;
import org.riverock.generic.schema.db.DataDefinitionTableListType;
import org.riverock.generic.schema.db.DataDefinitionType;
import org.riverock.generic.schema.db.DataDefinitionTypeChoice;
import org.riverock.generic.schema.db.structure.DbFieldType;
import org.riverock.generic.schema.db.structure.DbImportedKeyListType;
import org.riverock.generic.schema.db.structure.DbPrimaryKeyType;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbSequenceListType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.types.ActionTypeType;
import org.riverock.generic.tools.XmlTools;

/**
 * User: Admin
 * Date: May 15, 2003
 * Time: 11:15:35 PM
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class DefinitionService {
    private static Logger log = Logger.getLogger(DefinitionService.class);

    private static boolean isDefinitionProcessed = false;
    private static Map<String, Object> definitionRelateHash = null;
    private static Map<String, DataDefinitionType> definitionHash = null;
    private static Map<String, String> dbHash = null;
    private static DataDefinitionListType definitionList = new DataDefinitionListType();

    public synchronized static void registerRelateDefinitionDown(String definitionMain, String definitionTarget) {
        MainTools.putKey(definitionRelateHash, definitionMain, definitionTarget);
    }

    private final static Object syncDebug = new Object();

    public synchronized static void validateDatabaseStructure(DatabaseAdapter db_) throws Exception {

        if (!isDefinitionProcessed || DataDefinitionManager.isNeedReload()) {
            // in any case (with or without exception) process is complete
            isDefinitionProcessed = true;
            DataDefinitionManager.init();

            definitionRelateHash = new HashMap<String, Object>();
            definitionHash = new HashMap<String, DataDefinitionType>();

            // получаем из менеджера все файлы с определениями в виде массива
            DataDefinitionFile[] defFileArray = DataDefinitionManager.getDefinitionFileArray();

            for (DataDefinitionFile defFile : defFileArray) {
                if (defFile.definitionList == null) {
                    log.warn("Definition file '" + defFile.getFile() + "' is empty");
                    continue;
                }
                for (int j = 0; j < defFile.definitionList.getDefinitionCount(); j++) {
                    DataDefinitionType defItem = defFile.definitionList.getDefinition(j);
                    Object defTemp = definitionHash.get(defItem.getNameDef());
                    if (defTemp == null) {
                        definitionHash.put(defItem.getNameDef(), defItem);
                        for (int k = 0; k < defItem.getPreviousNameDefCount(); k++) {
                            String prevDef = defItem.getPreviousNameDef(k);
                            registerRelateDefinitionDown(defItem.getNameDef(), prevDef);
                        }
                    } else {
                        log.warn("Found duplicate key '" + defItem.getNameDef() + "' definition in file " + defFile.getFile());
                    }
                }
            }

            try {
                getProcessedDefinition(db_);
            }
            catch (Exception e) {
                log.error("Error get definition from db", e);
                return;
            }

            for (String key : definitionHash.keySet()) {
                walk(key);
            }
        }

        if (log.isDebugEnabled()) {
            synchronized (syncDebug) {
                try {
                    XmlTools.writeToFile(definitionList, GenericConfig.getGenericDebugDir() + "def-debug.xml");
                } catch (Throwable e) {
                    log.error("Error create debug file " + GenericConfig.getGenericDebugDir() + "def-debug.xml", e);
                }
            }
        }

        try {
            processDefinitionList(db_);
            db_.commit();
        }
        catch (Throwable e) {
            try {
                if (db_ != null)
                    db_.rollback();
            }
            catch (Throwable e1) {
                // catch rollback error
            }
        }
    }

    private synchronized static void processDefinitionList(DatabaseAdapter db_)
        throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("definitionList " + definitionList);
            if (definitionList != null)
                log.debug("definitionList.getDefinitionCount() " + definitionList.getDefinitionCount());
        }

        for (int i = 0; i < definitionList.getDefinitionCount(); i++) {
            DataDefinitionType defItem = definitionList.getDefinition(i);

            try {
                if (log.isInfoEnabled())
                    log.info("process definition " + defItem.getNameDef());

                if (log.isDebugEnabled())
                    log.debug("processTable ");
                processTable(db_, defItem);

                if (log.isDebugEnabled())
                    log.debug("processPrimaryKey ");
                processPrimaryKey(db_, defItem);

                if (log.isDebugEnabled())
                    log.debug("processImportedKeys ");
                processImportedKeys(db_, defItem);

                if (log.isDebugEnabled())
                    log.debug("processSequences ");
                processSequences(db_, defItem);

                if (log.isDebugEnabled())
                    log.debug("processAction ");
                processAction(db_, defItem);

                if (log.isDebugEnabled())
                    log.debug("store info about processed definition");

                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName("SEQ_WM_DB_DEFINITION");
                seq.setTableName("WM_DB_DEFINITION");
                seq.setColumnName("ID_DB_DEFINITION");

                MainDbDefinitionItemType item = new MainDbDefinitionItemType();
                item.setIdDbDefinition(db_.getSequenceNextValue(seq));
                item.setNameDefinition(defItem.getNameDef());
                item.setAplayDate(new Timestamp(System.currentTimeMillis()));

                String sql_ =
                    "insert into WM_DB_DEFINITION " +
                        "(ID_DB_DEFINITION, NAME_DEFINITION, APLAY_DATE)" +
                        "values" +
                        "( ?,  ?,  " + db_.getNameDateBind() + ")";

                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = db_.prepareStatement(sql_);

                    ps.setLong(1, item.getIdDbDefinition());
                    ps.setString(2, item.getNameDefinition());
                    db_.bindDate(ps, 3, new java.sql.Timestamp(item.getAplayDate().getTime()));

                    int countInsertRecord = ps.executeUpdate();

                    if (log.isDebugEnabled())
                        log.debug("Count of inserted records - " + countInsertRecord);

                }
                catch (Exception e) {
                    log.error("Item getIdDbDefinition(), value - " + item.getIdDbDefinition());
                    log.error("Item getNameDefinition(), value - " + item.getNameDefinition());
                    log.error("Item getAplayDate(), value - " + item.getAplayDate());
                    log.error("Error update db", e);
                    throw e;
                }
                finally {
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;
                }

            }
            catch (Exception e) {
                log.error("Error process definition '" + defItem.getNameDef() + "' ", e);
                return;
            }
        }
    }

    public static String getString(DataDefinitionActionDataListType actionList, String nameParam, String defValue)
        throws IllegalArgumentException {
        String value = getString(actionList, nameParam);
        if (value == null)
            return defValue;

        return value;
    }

    public synchronized static String getString(DataDefinitionActionDataListType actionList, String nameParam) {
        if (actionList == null || nameParam == null || nameParam.length() == 0)
            return null;

        for (int i = 0; i < actionList.getParameterCount(); i++) {
            DataDefinitionActionDataType action = actionList.getParameter(i);
            if (action.getName().equals(nameParam))
                return action.getData();
        }
        return null;
    }

    public static Double getDouble(DataDefinitionActionDataListType actionList, String nameParam, double defValue)
        throws IllegalArgumentException {
        Double value = getDouble(actionList, nameParam);
        if (value == null)
            return defValue;

        return value;
    }

    public synchronized static Double getDouble(DataDefinitionActionDataListType actionList, String nameParam)
        throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0)
            return null;

        for (int i = 0; i < actionList.getParameterCount(); i++) {
            DataDefinitionActionDataType action = actionList.getParameter(i);
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                Double doubleValue = null;
                try {
                    doubleValue = new Double(value);
                }
                catch (Exception e) {
                    String errorString = "Error convert String to Double from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return doubleValue;
            }
        }
        return null;
    }

    public static Long getLong(DataDefinitionActionDataListType actionList, String nameParam, long defValue)
        throws IllegalArgumentException {
        Long value = getLong(actionList, nameParam);
        if (value == null)
            return defValue;

        return value;
    }

    public synchronized static Long getLong(DataDefinitionActionDataListType actionList, String nameParam)
        throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0)
            return null;

        for (int i = 0; i < actionList.getParameterCount(); i++) {
            DataDefinitionActionDataType action = actionList.getParameter(i);
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                Long longValue = null;
                try {
                    longValue = new Long(value);
                }
                catch (Exception e) {
                    String errorString = "Error convert String to Long from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return longValue;
            }
        }
        return null;
    }

    public static Integer getInteger(DataDefinitionActionDataListType actionList, String nameParam, int defValue)
        throws IllegalArgumentException {
        Integer value = getInteger(actionList, nameParam);
        if (value == null)
            return defValue;

        return value;
    }

    public static Integer getInteger(DataDefinitionActionDataListType actionList, String nameParam)
        throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0)
            return null;

        for (int i = 0; i < actionList.getParameterCount(); i++) {
            DataDefinitionActionDataType action = actionList.getParameter(i);
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                Integer intValue = null;
                try {
                    intValue = new Integer(value);
                }
                catch (Exception e) {
                    String errorString = "Error convert String to Integer from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return intValue;
            }
        }
        return null;
    }

    public static Boolean getBoolean(DataDefinitionActionDataListType actionList, String nameParam, boolean defValue)
        throws IllegalArgumentException {
        Boolean value = getBoolean(actionList, nameParam);
        if (value == null)
            return defValue;

        return value;
    }

    public synchronized static Boolean getBoolean(DataDefinitionActionDataListType actionList, String nameParam)
        throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0)
            return null;

        for (int i = 0; i < actionList.getParameterCount(); i++) {
            DataDefinitionActionDataType action = actionList.getParameter(i);
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                if (value == null)
                    value = "false";

                if (value.equals("1"))
                    value = "true";

                Boolean booleanValue = null;
                try {
                    booleanValue = Boolean.valueOf(value);
                }
                catch (Exception e) {
                    String errorString = "Error convert String to Boolean from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return booleanValue;
            }
        }
        return null;
    }

    ////////////////////////

    private synchronized static void processTable(DatabaseAdapter db_, DataDefinitionType defItem)
        throws Exception {
        if (defItem == null)
            return;

        for (int j = 0; j < defItem.getDataDefinitionTypeChoiceCount(); j++) {
            DataDefinitionTypeChoice choice = defItem.getDataDefinitionTypeChoice(j);

            for (int k = 0; k < choice.getDataDefinitionTypeChoiceItemCount(); k++) {
                DataDefinitionTableListType tableList = choice.getDataDefinitionTypeChoiceItem(k).getTableList();
//            DataDefinitionTableListType tableList = choice.getDataDefinitionTypeChoiceItem().getTableList();

                try {
                    if (tableList != null) {
                        for (int i = 0; i < tableList.getTableCount(); i++) {
                            DbTableType table = tableList.getTable(i);
                            db_.createTable(table);
                        }
                    }
                }
                catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.getIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.getIsSilensAction()))
                        throw e;
                }
            }
        }
    }

    private synchronized static void processPrimaryKey(DatabaseAdapter db_, DataDefinitionType defItem)
        throws Exception {
        if (defItem == null)
            return;

        for (int j = 0; j < defItem.getDataDefinitionTypeChoiceCount(); j++) {
            DataDefinitionTypeChoice choice = defItem.getDataDefinitionTypeChoice(j);

            for (int k = 0; k < choice.getDataDefinitionTypeChoiceItemCount(); k++) {
                DbPrimaryKeyType pk = choice.getDataDefinitionTypeChoiceItem(k).getPrimaryKey();
//            DbPrimaryKeyType pk = choice.getDataDefinitionTypeChoiceItem().getPrimaryKey();

                try {
                    if (pk != null && pk.getColumnsCount() > 0) {
                        DbSchemaType schema = DatabaseManager.getDbStructure(db_);
                        DbTableType table = DatabaseManager.getTableFromStructure(schema, pk.getColumns(0).getTableName());
                        DatabaseManager.addPrimaryKey(db_, table, pk);
                    }
                }
                catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.getIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.getIsSilensAction()))
                        throw e;
                }
            }
        }
    }

    private synchronized static void processImportedKeys(DatabaseAdapter db_, DataDefinitionType defItem)
        throws Exception {
        if (defItem == null)
            return;

        for (int j = 0; j < defItem.getDataDefinitionTypeChoiceCount(); j++) {
            DataDefinitionTypeChoice choice = defItem.getDataDefinitionTypeChoice(j);

            for (int k = 0; k < choice.getDataDefinitionTypeChoiceItemCount(); k++) {
                DbImportedKeyListType fkList = choice.getDataDefinitionTypeChoiceItem(k).getImportedKeys();
//            DbImportedKeyListType fkList = choice.getDataDefinitionTypeChoiceItem().getImportedKeys();
                try {
                    if (fkList != null && fkList.getKeysCount() > 0) {
                        DbSchemaType schema = DatabaseManager.getDbStructure(db_);
                        // Todo: and what we do with this table?
                        DbTableType table =
                            DatabaseManager.getTableFromStructure(
                                schema,
                                fkList.getKeys(0).getPkTableName()
                            );

                        DatabaseStructureManager.createForeignKey(db_, fkList);
                    }
                }
                catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.getIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.getIsSilensAction()))
                        throw e;
                }
            }
        }
    }

    private synchronized static void processSequences(DatabaseAdapter db_, DataDefinitionType defItem)
        throws Exception {
        if (defItem == null)
            return;

        for (int j = 0; j < defItem.getDataDefinitionTypeChoiceCount(); j++) {
            DataDefinitionTypeChoice choice = defItem.getDataDefinitionTypeChoice(j);

            for (int k = 0; k < choice.getDataDefinitionTypeChoiceItemCount(); k++) {
                DbSequenceListType seqList = choice.getDataDefinitionTypeChoiceItem(k).getSequenceList();
//            DbSequenceListType seqList = choice.getDataDefinitionTypeChoiceItem().getSequenceList();
                try {
                    if (seqList != null) {
                        for (int i = 0; i < seqList.getSequencesCount(); i++) {
                            DbSequenceType seq = seqList.getSequences(i);
                            db_.createSequence(seq);
                        }
                    }
                }
                catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.getIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.getIsSilensAction()))
                        throw e;
                }
            }
        }
    }

    private synchronized static void processAction(DatabaseAdapter db_, DataDefinitionType defItem)
        throws Exception {
        if (defItem == null)
            return;

        if (log.isDebugEnabled())
            log.debug("defItem.getDataDefinitionTypeChoiceCount() " + defItem.getDataDefinitionTypeChoiceCount());

        for (int j = 0; j < defItem.getDataDefinitionTypeChoiceCount(); j++) {
            DataDefinitionTypeChoice choice = defItem.getDataDefinitionTypeChoice(j);

            for (int k = 0; k < choice.getDataDefinitionTypeChoiceItemCount(); k++) {
                DataDefinitionActionListType actionList = choice.getDataDefinitionTypeChoiceItem(k).getActionList();
//            DataDefinitionActionListType actionList = choice.getDataDefinitionTypeChoiceItem().getActionList();

                if (actionList != null) {
                    if (log.isDebugEnabled())
                        log.debug("actionList.getActionCount() " + actionList.getActionCount());

                    for (int i = 0; i < actionList.getActionCount(); i++) {
                        DataDefinitionActionType action = actionList.getAction(i);
                        try {

                            switch (action.getActionType().getType()) {
                                case ActionTypeType.ADD_FOREIGN_KEY_TYPE: {

                                }
                                break;

                                case ActionTypeType.ADD_PRIMARY_KEY_TYPE: {

                                }
                                break;

                                case ActionTypeType.ADD_TABLE_COLUMN_TYPE: {
                                    if (log.isDebugEnabled())
                                        log.debug("process action ADD_TABLE_COLUMN_TYPE");

                                    DbFieldType field = new DbFieldType();

                                    field.setName(getString(action.getActionParameters(), "column_name"));
                                    field.setJavaType(
                                        DatabaseManager.sqlTypesMapping(
                                            getString(action.getActionParameters(), "column_type")
                                        )
                                    );
                                    field.setSize(getInteger(action.getActionParameters(), "column_size", 0));
                                    field.setDecimalDigit(getInteger(action.getActionParameters(), "column_decimal_digit", 0));
                                    field.setDefaultValue(getString(action.getActionParameters(), "column_default_value"));
                                    field.setNullable(getInteger(action.getActionParameters(), "column_nullable", 0));

                                    DatabaseStructureManager.addColumn(db_, getString(action.getActionParameters(), "table_name"), field);
                                }
                                break;

                                case ActionTypeType.CLONE_COLUMN_TYPE: {

                                }
                                break;

                                case ActionTypeType.COPY_COLUMN_TYPE: {

                                }
                                break;

                                case ActionTypeType.CREATE_SEQUENCE_TYPE: {
                                    DbSequenceType seq = new DbSequenceType();
                                    seq.setCacheSize(getInteger(action.getActionParameters(), "sequence_cache_size", 0));
                                    seq.setIncrementBy(getInteger(action.getActionParameters(), "sequence_increment", 1));
                                    seq.setIsCycle(getBoolean(action.getActionParameters(), "sequence_is_cycle", false));
                                    seq.setIsOrder(getBoolean(action.getActionParameters(), "sequence_is_order", false));
                                    seq.setLastNumber(getLong(action.getActionParameters(), "sequence_last_number", 0));
                                    seq.setMaxValue(getString(action.getActionParameters(), "sequence_max_value", "0"));
                                    seq.setMinValue(getInteger(action.getActionParameters(), "sequence_min_value", 0));
                                    seq.setName(getString(action.getActionParameters(), "sequence_name"));

                                    db_.createSequence(seq);
                                }
                                break;

                                case ActionTypeType.CREATE_TABLE_TYPE: {

                                }
                                break;

                                case ActionTypeType.CUSTOM_CLASS_ACTION_TYPE: {
                                    String className = getString(action.getActionParameters(), "class_name");
                                    if (className == null)
                                        throw new Exception("Definition - " + defItem.getNameDef() + ", action '" + ActionTypeType.CUSTOM_CLASS_ACTION.toString() + "' must have parameter 'class_name'");

                                    Object obj = MainTools.createCustomObject(className);
                                    if (obj == null)
                                        throw new Exception("Definition - " + defItem.getNameDef() + ", action '" + ActionTypeType.CUSTOM_CLASS_ACTION.toString() + "', obj is null");

                                    ((DefinitionProcessingInterface) obj).processAction(db_, action.getActionParameters());
                                }
                                break;

                                case ActionTypeType.CUSTOM_SQL_TYPE: {
                                    String sql = getString(action.getActionParameters(), "sql");
                                    if (log.isDebugEnabled()) {
                                        log.debug("Action type " + action.getActionType().toString());
                                        log.debug("Custom sql " + sql);
                                    }
                                    Statement st = null;
                                    try {
                                        st = db_.createStatement();
                                        st.execute(sql);
                                    }
                                    catch (Exception e) {
                                        log.error("Exception exceute statement " + sql, e);
                                        throw e;
                                    }
                                    catch (Error e) {
                                        log.error("Error exceute statement " + sql, e);
                                        throw e;
                                    }
                                    finally {
                                        DatabaseManager.close(st);
                                        st = null;
                                    }
                                }
                                break;

                                case ActionTypeType.DELETE_BEFORE_FK_TYPE: {

                                }
                                break;

                                case ActionTypeType.DROP_FOREIGN_KEY_TYPE: {

                                }
                                break;

                                case ActionTypeType.DROP_PRIMARY_KEY_TYPE: {

                                }
                                break;

                                case ActionTypeType.DROP_TABLE_TYPE: {
                                    String nameTable = getString(action.getActionParameters(), "name_table");
                                    if (nameTable != null) {
                                        db_.dropTable(nameTable);
                                        db_.commit();
                                    } else
                                        log.error("Definition - " + defItem.getNameDef() + ", action '" + ActionTypeType.DROP_TABLE.toString() + "' must have parameter 'name_table'");

                                }
                                break;

                                case ActionTypeType.DROP_TABLE_COLUMN_TYPE: {

                                }
                                break;

                                case ActionTypeType.DROP_SEQUENCE_TYPE: {
                                    String nameSeq = getString(action.getActionParameters(), "name_sequence");
                                    if (nameSeq != null) {
                                        db_.dropSequence(nameSeq);
                                    } else
                                        log.error("Definition - " + defItem.getNameDef() + ", action '" + ActionTypeType.DROP_TABLE.toString() + "' must have parameter 'name_sequence'");
                                }
                                break;
                                default:
                                    String errorString = "Unknown type of action - " + action.getActionType().getType();
                                    log.error(errorString);
                                    throw new Exception(errorString);

                            }
                        }
                        catch (Exception e) {
                            log.error("IsSilensAction - " + defItem.getIsSilensAction() + ", Definition - " + defItem.getNameDef() + ", action '" + action.getActionType().toString() + "'", e);
                            if (Boolean.FALSE.equals(defItem.getIsSilensAction()))
                                throw e;
                        }
                    }
                }
            }
        }
    }

    private synchronized static void walk(String key) {
        Object obj = definitionRelateHash.get(key);
        if (obj != null) {
            if (obj instanceof String) {
                String nameDef = (String) obj;
                walk(nameDef);
            } else if (obj instanceof List) {
                List v = (List) obj;
                walkList(v);
            } else {
                throw new IllegalStateException("Wrong type of element in list, valid String and List, current: " + obj.getClass().getName());
            }
        }
        Object value = dbHash.get(key);
        boolean flag = isInQueue(key);
        if (value == null && !flag)
            definitionList.addDefinition(definitionHash.get(key));
    }

    private synchronized static void walkList(List v) {
        for (Object obj : v) {
            if (obj==null) {
                throw new IllegalStateException("Element in list is null");
            }
            if (obj instanceof String) {
                String nameDef = (String) obj;
                walk(nameDef);
            } else if (obj instanceof List) {
                walkList((List) obj);
            } else {
                throw new IllegalStateException("Wrong type of element in list, valid String and List, current: " + obj.getClass().getName());
            }
        }
    }

    private synchronized static boolean isInQueue(String nameDef) {
        for (int i = 0; i < definitionList.getDefinitionCount(); i++) {
            DataDefinitionType def = definitionList.getDefinition(i);
            if (def.getNameDef().equals(nameDef))
                return true;
        }

        return false;
    }

    private synchronized static void getProcessedDefinition(DatabaseAdapter db_)
        throws Exception {
        if (dbHash != null) {
            dbHash.clear();
            dbHash = null;
        }
        dbHash = new HashMap<String, String>();

        // получаем из базы данных список определений, которые были обработаны
        MainDbDefinitionListType mainDef = null;
        try {
            String sql_ =
                "select ID_DB_DEFINITION from WM_DB_DEFINITION ";

            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = db_.prepareStatement(sql_);

                rs = ps.executeQuery();
                while (rs.next()) {
                    if (mainDef == null)
                        mainDef = new MainDbDefinitionListType();

                    GetMainDbDefinitionItem tempItem = GetMainDbDefinitionItem.getInstance(db_, RsetTools.getLong(rs, "ID_DB_DEFINITION"));
                    mainDef.addMainDbDefinition(tempItem.item);
                }
            }
            catch (Exception e) {
                if (db_.testExceptionTableNotFound(e)) {
                    log.warn("WM_DB_DEFINITION table not found. Assumed this first connect to empty DB schema. Start create new structure");
                    return;
                }

                if (e instanceof SQLException)
                    log.error("Error get data from WM_DB_DEFINITION, sql code " + ((SQLException) e).getErrorCode(), e);
                else
                    log.error("Error get data from WM_DB_DEFINITION", e);

                throw e;
            }
            finally {
                org.riverock.generic.db.DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }

        }
        catch (Exception e) {
            log.error("Error get new instance for GetMainDbDefinitionFullList", e);
            throw e;
        }

        if (mainDef != null) {
            // проходим по определениям из базы
            for (int j = 0; j < mainDef.getMainDbDefinitionCount(); j++) {
                MainDbDefinitionItemType tempDbItem = mainDef.getMainDbDefinition(j);
                dbHash.put(tempDbItem.getNameDefinition(), tempDbItem.getNameDefinition());
            }
        }
    }

}
