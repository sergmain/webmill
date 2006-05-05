package org.riverock.generic.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.schema.sql.SqlNameType;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.parser.Parser;

/**
 * @author SergeMaslyukov
 *         Date: 20.12.2005
 *         Time: 1:33:38
 *         $Id$
 */
public class DatabaseRuntimeService {
    private final static Logger log = Logger.getLogger(DatabaseRuntimeService.class);

    public static PreparedStatement prepareStatement(Connection conn, DatabaseConnectionType dc, Map<String, String> tables1, final String sql_) throws SQLException {

        try {
            if (Boolean.TRUE.equals(dc.getIsSupportCache())) {
                Parser parser = SqlStatement.parseSql(sql_);

                if (log.isDebugEnabled())
                    log.debug("parser.typeStatement!=Parser.SELECT - " + (parser.typeStatement != Parser.SELECT));

                if (parser.typeStatement != Parser.SELECT) {
                    for (int i = 0; i < parser.depend.getTarget().getItemCount(); i++) {
                        String name = parser.depend.getTarget().getItem(i).getOriginName();

                        if (log.isDebugEnabled())
                            log.debug("Name lookung table - " + name);

                        String nameTable = tables1.get(name);
                        if (log.isDebugEnabled())
                            log.debug("searching table " + name + " in hash - " + nameTable);

                        if (nameTable == null)
                            tables1.put(name, name);
                    }
                }
            }
        }
        catch (Exception e) {
            final String es = "Error prepareStatement, SQL: " + sql_;
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }

        try {
            return conn.prepareStatement(sql_);
        }
        catch (SQLException e) {
            final String es = "Error prepareStatement, SQL: " + sql_;
            log.error(es, e);
            throw e;
        }
    }

    private final static Object syncCommit = new Object();
    public static void commit(Connection conn, DatabaseConnectionType dc, Map<String, String> tables1) throws SQLException {
        try {
            conn.commit();
        }
        catch (SQLException ex) {
            final String es = "Error reinit cache";
            log.error(es, ex);
            throw ex;
        }

        try {
            if (Boolean.TRUE.equals(dc.getIsSupportCache())) {

                if (log.isDebugEnabled()) log.debug("Start sync cache. DB action - COMMIT");

                synchronized (syncCommit) {
                    if (log.isDebugEnabled()) log.debug("Count of changed tables - " + tables1.size());

                    for (String tableName : tables1.keySet()) {

                        if (log.isDebugEnabled()) {
                            log.debug("process cache for table " + tableName);
                            log.debug("count of class in hash " + SqlStatement.classHash.size());
                        }

                        for (String className : SqlStatement.classHash.keySet()) {
                            Object obj = SqlStatement.classHash.get(className);

                            if (log.isDebugEnabled()) {
                                log.debug("-- Start new check for class - " + className + ", obj " + obj);
                            }

                            boolean isDependent = false;
                            if (obj == null)
                                continue;

                            if (obj instanceof List) {
                                for (Object o : (List) obj) {
                                    Parser checkParser = (Parser) o;
                                    isDependent = checkDependence(checkParser, tableName);
                                    if (isDependent)
                                        break;
                                }
                            } else if (obj instanceof Parser)
                                isDependent = checkDependence((Parser) obj, tableName);
                            else {
                                String errorString = "Object in hash is " + obj.getClass().getName() + ", but expected Parser or List";
                                log.error(errorString);
                                throw new Exception(errorString);
                            }

                            if (log.isDebugEnabled()) log.debug("isDependent - " + isDependent);

                            if (isDependent)
                                reinitClass(className);
                        }
                    }
                    tables1.clear();
                }
            }
        }
        catch (Exception ex) {
            final String es = "Error reinit cache";
            log.error(es, ex);
            throw new IllegalStateException(es, ex);
        }
    }

    private final static Object syncRollback = new Object();
    public static void rollback(Connection conn, DatabaseConnectionType dc, Map<String, String> tables) throws SQLException {
        conn.rollback();
        if (Boolean.TRUE.equals(dc.getIsSupportCache())) {
            synchronized (syncRollback) {
                tables.clear();
            }
        }
    }

    public static boolean checkDependence(final Parser checkParser, final String name) {
        if (checkParser != null) {
            for (Object o : checkParser.depend.getSource().getItemAsReference()) {
                SqlNameType checkName = (SqlNameType) o;

                if (checkName.getIsNameQuoted()) {
                    if (name.equals(checkName.getOriginName()))
                        return true;
                } else {
                    if (name.equalsIgnoreCase(checkName.getOriginName()))
                        return true;
                }
            }
        }
        return false;
    }

    private static void reinitClass(final String className) {
        if (log.isDebugEnabled()) {
            log.debug("reinit class - " + className);
            Long maxMemory = Runtime.getRuntime().maxMemory();

            log.debug(
                "free memory " + Runtime.getRuntime().freeMemory() +
                    " total memory " + Runtime.getRuntime().totalMemory() +
                    (maxMemory != null ? " max memory " + maxMemory : "")
            );
        }
        try {
            Object objTemp = MainTools.createCustomObject(className);
            Method method1 = objTemp.getClass().getMethod("reinit", (Class[]) null);

            if (log.isDebugEnabled())
                log.debug("#2.2.009  method is " + method1);

            if (method1 != null) {
                method1.invoke(objTemp, (Object[]) null);

                if (log.isDebugEnabled())
                    log.debug("#2.2.010 ");
            }
            reinitRelateClass(className);
        }
        catch (Throwable e) {
            final String es = "Error in reinitClass " + className;
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    private static void reinitRelateClass(final String className) {
        Object relateObject = SqlStatement.classRelateHash.get(className);

        if (log.isDebugEnabled())
            log.debug("relate class for class - " + className + " is " + relateObject);

        if (relateObject == null)
            return;

        if (relateObject instanceof List) {
            for (Object o : (List)relateObject ) {
                String relateClassName = (String)o;
                reinitClass(relateClassName);
            }
        } else {
            if (log.isDebugEnabled())
                log.debug("call reinitClass() with object " + relateObject.getClass().getName());

            reinitClass((String) relateObject);
        }
    }

}
