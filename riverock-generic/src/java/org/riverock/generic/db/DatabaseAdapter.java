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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.schema.config.types.DataSourceTypeType;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.structure.DbDataFieldDataType;
import org.riverock.generic.schema.db.structure.DbFieldType;
import org.riverock.generic.schema.db.structure.DbImportedPKColumnType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;

/**
 * $Revision$ $Date$
 */
public abstract class DatabaseAdapter {
    private final static Logger log = Logger.getLogger(DatabaseAdapter.class);

    protected Connection conn = null;
    protected Map<String, String> tables = new HashMap<String, String>();

    protected boolean isDriverLoaded = false;
    protected DatabaseConnectionType dc = null;
    protected DataSource dataSource = null;

    public DatabaseAdapter() {
    }

    public abstract int getFamaly();

    public abstract int getVersion();

    public abstract int getSubVersion();

    public Connection getConnection() {
        return conn;
    }

    public PreparedStatement prepareStatement(final String sql_) throws SQLException {
        return DatabaseRuntimeService.prepareStatement(conn, dc, tables, sql_);
    }

    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    public void commit() throws SQLException {
        DatabaseRuntimeService.commit(conn, dc, tables);
    }

    public void rollback() throws SQLException {
        DatabaseRuntimeService.rollback(conn, dc, tables);
    }

    protected void finalize() throws Throwable {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (Exception e) {
                //catch close connection error
            }
            conn = null;
        }
        if (tables!=null) {
            tables.clear();
            tables = null;
        }
        dc = null;

        super.finalize();
    }

    public abstract boolean getIsBatchUpdate();

    public abstract boolean getIsNeedUpdateBracket();

    public abstract boolean getIsByteArrayInUtf8();

    /**
     * return status of DB - closed or not
     *
     * @return boolean. true - connection is closed. false - connection is opened
     * @throws SQLException
     */
    public abstract boolean getIsClosed() throws SQLException;

    public abstract String getClobField(ResultSet rs, String nameFeld) throws SQLException;

    public abstract String getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception;

    public abstract void createTable(DbTableType table) throws Exception;

    public abstract void createView(DbViewType view) throws Exception;

    public abstract void createSequence(DbSequenceType seq) throws Exception;

    public abstract void dropTable(DbTableType table) throws Exception;

    public abstract void dropTable(String nameTable) throws Exception;

    public abstract void dropSequence(String nameSequence) throws Exception;

    public abstract void dropConstraint(DbImportedPKColumnType impPk) throws Exception;

    public abstract void addColumn(DbTableType table, DbFieldType field) throws Exception;

    /**
     * in some DB (Oracle8.0) setTimestamp not work and we need work around
     *
     * @return String
     */
    public abstract String getNameDateBind();

    public abstract String getOnDeleteSetNull();

    /**
     * bind Timestamp value
     *
     * @param ps
     * @param stamp @see java.sql.Timestamp
     * @throws SQLException
     */
    public abstract void bindDate(final PreparedStatement ps, final int idx, final Timestamp stamp) throws SQLException;

    public abstract String getDefaultTimestampValue();

    public abstract ArrayList getViewList(String schemaPattern, String tablePattern) throws Exception;

    public abstract ArrayList getSequnceList(String schemaPattern) throws Exception;

    public abstract String getViewText(DbViewType view) throws Exception;

    public abstract void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException;

    public abstract void setLongVarchar(PreparedStatement ps, int index, DbDataFieldDataType fieldData)
        throws SQLException;

    /**
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
     *
     * @param e - Exception
     * @return boolean. true - если ошибка возникла в результате обращения к не существующей
     *         таблице. Иначе false.
     */
    public abstract boolean testExceptionTableNotFound(Exception e);

    /**
     * Проверяет является ли ошибка результатом попытки вставки дулирующих данных в уникальный ключ
     *
     * @param e     - Exception
     * @param index - String. имя уникального ключа для проверки что ошибка возникла именно в нем.
     * @return boolean. true - если ошибка была результатом попытки вставки дулирующих данных
     *         в уникальный ключ. Иначе false.
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
     *
     * @param sequence - String. Имя последовательноти для получения следующего значения.
     * @return long - следующее значение для ключа из последовательности
     * @throws SQLException
     */
    public abstract long getSequenceNextValue(final CustomSequenceType sequence) throws SQLException;

    /**
     * @return - int. Max size of char field
     */
    public abstract int getMaxLengthStringField();

    protected abstract DataSource createDataSource() throws SQLException;

    public abstract String getDriverClass();

    private final static Object syncObject = new Object();
    protected void init(DatabaseConnectionType dc_) throws DatabaseException, SQLException {
        dc = dc_;

        try {
            if (dc == null) {
                log.fatal("DatabaseConnection not initialized");
                throw new DatabaseException("#21.001 DatabaseConnection not initialized.");
            }

            if (!isDriverLoaded) {
                synchronized (syncObject) {
                    if (!isDriverLoaded) {
                        switch (dc.getDataSourceType().getType()) {
                            case DataSourceTypeType.DRIVER_TYPE:
                                if (log.isDebugEnabled())
                                    log.debug("Start create connection pooling with driver");


                                break;
                            case DataSourceTypeType.JNDI_TYPE:

                                if (log.isDebugEnabled())
                                    log.debug("Start create connection pooling with JNDI");
                                try {
                                    Context envCtx = null;
                                    try {
                                        Context initCtx = new InitialContext();
                                        envCtx = (Context) initCtx.lookup("java:comp/env");
                                    } catch (Throwable e) {
                                        log.info("JNDI context java:comp/env not found, will try search in root context");
                                    }

                                    // Look up our datasource
                                    if (envCtx==null) {
                                        envCtx = new InitialContext();
                                        dataSource=(DataSource) envCtx.lookup(dc.getDataSourceName());
                                    }
                                    else {
                                        boolean isError=false;
                                        try {
                                            dataSource=(DataSource) envCtx.lookup(dc.getDataSourceName());
                                        }
                                        catch (NamingException e) {
                                            isError=true;
                                        }
                                        if (isError) {
                                            envCtx = new InitialContext();
                                            dataSource=(DataSource) envCtx.lookup(dc.getDataSourceName());
                                        }
                                    }
                                }
                                catch (NamingException e) {
                                    final String es = "Error get value from JDNI context";
                                    log.error(es, e);
                                    throw new DatabaseException(es, e);
                                }

                                break;

                            case DataSourceTypeType.NONE_TYPE:
                                if (log.isDebugEnabled())
                                    log.debug("Start create connection pooling with simple mnaager");
                                Class.forName(getDriverClass());
                                break;
                        }
                        isDriverLoaded = true;

                    }
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("ConnectString - " + dc.getConnectString());
                log.debug("username - " + dc.getUsername());
                log.debug("password - " + dc.getPassword());
                log.debug("isAutoCommit - " + dc.getIsAutoCommit());
            }

            switch (dc.getDataSourceType().getType()) {
                case DataSourceTypeType.DRIVER_TYPE:
                    conn = dataSource.getConnection();
                    break;
                case DataSourceTypeType.JNDI_TYPE:
                    conn = dataSource.getConnection();
                    break;

                case DataSourceTypeType.NONE_TYPE:
                    conn = DriverManager.getConnection(dc.getConnectString(), dc.getUsername(), dc.getPassword());
                    break;
            }

            conn.setAutoCommit(dc.getIsAutoCommit());
        }
        catch (Exception e) {
            final String es = "Expeption create new Connection";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
    }

    protected static DatabaseAdapter openConnect(final DatabaseConnectionType dc)
        throws DatabaseException {
        if (dc == null) {
            String es = "DatabaseConnection is null.";
            log.fatal(es);
            throw new DatabaseException(es);
        }

        DatabaseAdapter db_ = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Call for create dynamic object " + dc.getConnectionClass());
            }

            db_ = (DatabaseAdapter) MainTools.createCustomObject(dc.getConnectionClass());
            db_.init(dc);

            if (log.isDebugEnabled()) {
                log.debug("Success create dynamic object " + dc.getConnectionClass());
            }
        }
        catch (Exception e) {
            if (db_ != null && db_.conn != null) {
                try {
                    db_.conn.close();
                    db_.conn = null;
                }
                catch (Exception e02) {
                    // catch close connection error
                }
            }

            log.fatal("Error create instance for class " + dc.getConnectionClass());
            log.fatal("ConnectionName - " + dc.getName());
            log.fatal("Error:", e);

            final String es = "Error create instance for class " + dc.getConnectionClass() + ". See log for details";
            System.out.println(es);
            throw new DatabaseException(es, e);
        }
        return db_;
    }

    public static DatabaseAdapter getInstance() throws DatabaseException {
        return getInstance(GenericConfig.getDefaultConnectionName());
    }

    /**
     * @param connectionName - String. Name of connection config.
     * @return - DatabaseAdapter
     * @throws DatabaseException
     */
    public static DatabaseAdapter getInstance(final String connectionName)
        throws DatabaseException {
        if (connectionName == null) {
            log.error("Call DatabaseAdapter.getInstance(final boolean isDynamic, final String connectionName) with connectionName==null");
            return null;
        }

        DatabaseConnectionType dc = GenericConfig.getDatabaseConnection(connectionName);
        if (dc == null) {
            String es = "DatabaseConnection definition for connection name '" + connectionName + "' not found.";
            log.fatal(es);
            throw new DatabaseException(es);
        }

        DatabaseAdapter adapater = openConnect(dc);
        DatabaseStructureManager.checkDatabaseStructure(adapater, dc);

        return adapater;
    }

    /**
     * Close connect to DB. If DatabaseAdapter is not 'dynamic', connection will not be closed
     *
     * @param db_ - DatabaseAdapter.
     */
    public static void close(final DatabaseAdapter db_) {
        if (db_ == null)
            return;

        try {
            db_.rollback();
        }
        catch (Exception e) {
            // catch rollback error
        }

        try {
            db_.conn.close();
            db_.conn = null;
        }
        catch (Exception e) {
            // catch close error
        }
    }
}
