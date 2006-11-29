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
package org.riverock.generic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.riverock.generic.config.GenericConfig;
import org.riverock.common.exception.DatabaseException;
import org.riverock.generic.annotation.schema.config.DatabaseConnectionType;
import org.riverock.generic.annotation.schema.db.DbTable;
import org.riverock.generic.annotation.schema.db.DbView;
import org.riverock.generic.annotation.schema.db.DbSequence;
import org.riverock.generic.annotation.schema.db.DbImportedPKColumn;
import org.riverock.generic.annotation.schema.db.DbField;
import org.riverock.generic.annotation.schema.db.DbDataFieldData;
import org.riverock.generic.annotation.schema.db.CustomSequence;

/**
 * $Revision$ $Date$
 */
public abstract class DatabaseAdapter implements DbConnection {
    private final static Logger log = Logger.getLogger(DatabaseAdapter.class);

    protected Connection conn = null;
    protected Map<String, String> tables = new HashMap<String, String>();

    protected boolean isDriverLoaded = false;
    protected DatabaseConnectionType dc = null;
    protected DataSource dataSource = null;
    private static final String DRIVER_DATASOURCE_TIPE = "DRIVER";
    private static final String JNDI_DATASOURCE_TIPE = "JNDI";
    private static final String NONE_DATASOURCE_TIPE = "NONE";

    public DatabaseAdapter() {
    }

    public abstract int getFamily();

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

    public abstract byte[] getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception;

    public abstract void createTable(DbTable table) throws Exception;

    public abstract void createView(DbView view) throws Exception;

    public abstract void createSequence(DbSequence seq) throws Exception;

    public abstract void dropTable(DbTable table) throws Exception;

    public abstract void dropTable(String nameTable) throws Exception;

    public abstract void dropSequence(String nameSequence) throws Exception;

    public abstract void dropConstraint(DbImportedPKColumn impPk) throws Exception;

    public abstract void addColumn(DbTable table, DbField field) throws Exception;

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

    public abstract List<DbView> getViewList(String schemaPattern, String tablePattern) throws Exception;

    public abstract List<DbSequence> getSequnceList(String schemaPattern) throws Exception;

    public abstract String getViewText(DbView view) throws Exception;

    public abstract void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldData fieldData)
        throws SQLException;

    public abstract void setLongVarchar(PreparedStatement ps, int index, DbDataFieldData fieldData)
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
     * ��������� �������� �� ������ ����������� ��������� � �� ������������ � ���� �������.
     *
     * @param e - Exception
     * @return boolean. true - ���� ������ �������� � ���������� ��������� � �� ������������
     *         �������. ����� false.
     */
    public abstract boolean testExceptionTableNotFound(Exception e);

    /**
     * ��������� �������� �� ������ ����������� ������� ������� ���������� ������ � ���������� ����
     *
     * @param e     - Exception
     * @param index - String. ��� ����������� ����� ��� �������� ��� ������ �������� ������ � ���.
     * @return boolean. true - ���� ������ ���� ����������� ������� ������� ���������� ������
     *         � ���������� ����. ����� false.
     */
    public abstract boolean testExceptionIndexUniqueKey(Exception e, String index);

    public abstract boolean testExceptionIndexUniqueKey(Exception e);

    public abstract boolean testExceptionTableExists(Exception e);

    public abstract boolean testExceptionViewExists(Exception e);

    public abstract boolean testExceptionSequenceExists(Exception e);

    public abstract boolean testExceptionConstraintExists(Exception e);

    /**
     * ���������� �������� ��������(������������������) ��� ������� ����� ������������������.
     * ��� ������ ��������� � ������ ����� ������ ����� ���� ������ �� �������.
     *
     * @param sequence - String. ��� ����������������� ��� ��������� ���������� ��������.
     * @return long - ��������� �������� ��� ����� �� ������������������
     * @throws SQLException
     */
    public abstract long getSequenceNextValue(final CustomSequence sequence) throws SQLException;

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
                        if (log.isDebugEnabled()) {
                            log.debug("dc.getDataSourceType(): "+ dc.getDataSourceType() );
                        }
                        if (dc.getDataSourceType().equals(DRIVER_DATASOURCE_TIPE)) {
                                if (log.isDebugEnabled()) {
                                    log.debug("Start create connection pooling with driver");
                                }
                        }
                        else if (dc.getDataSourceType().equals(JNDI_DATASOURCE_TIPE)) {
                                if (log.isDebugEnabled()) {
                                    log.debug("Start create connection pooling with JNDI");
                                }
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

                        }
                        else if (dc.getDataSourceType().equals(NONE_DATASOURCE_TIPE)){
                                if (log.isDebugEnabled()) {
                                    log.debug("Start create connection pooling with simple mnaager");
                                }
                                Class.forName(getDriverClass());
                        }

                        isDriverLoaded = true;

                    }
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Family - " + dc.getFamily());
                log.debug("ConnectString - " + dc.getConnectString());
                log.debug("username - " + dc.getUsername());
                log.debug("password - " + dc.getPassword());
                log.debug("isAutoCommit - " + dc.isIsAutoCommit());
            }

            if (dc.getDataSourceType().equals(DRIVER_DATASOURCE_TIPE)) {
                    conn = dataSource.getConnection();
            }
            else if (dc.getDataSourceType().equals(JNDI_DATASOURCE_TIPE)) {
                    conn = dataSource.getConnection();
            }
            else if (dc.getDataSourceType().equals(NONE_DATASOURCE_TIPE)) {
                    conn = DriverManager.getConnection(dc.getConnectString(), dc.getUsername(), dc.getPassword());
            }
            if (log.isDebugEnabled()) {
                log.debug("dc: " + dc);
                log.debug("Connection: " + conn);
            }
            conn.setAutoCommit(dc.isIsAutoCommit()!=null?dc.isIsAutoCommit():false);
        }
        catch (Exception e) {
            final String es = "Expeption create new Connection";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
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

        return DbConnectionProvider.openConnect(dc);
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
            if (db_.conn!=null) {
                db_.conn.close();
                db_.conn = null;
            }
        }
        catch (Exception e) {
            // catch close error
        }
    }
}
