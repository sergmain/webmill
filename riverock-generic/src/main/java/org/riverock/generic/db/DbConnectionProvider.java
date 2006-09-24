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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.db;

import java.util.Map;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.db.factory.ORAconnect;
import org.riverock.generic.db.factory.MYSQLconnect;
import org.riverock.generic.db.factory.HSQLDBconnect;
import org.riverock.generic.db.factory.MSSQL_JTDS_connect;
import org.riverock.generic.db.factory.PostgreeSQLconnect;
import org.riverock.generic.db.factory.IBMDB2connect;
import org.riverock.generic.db.factory.SAPconnect;
import org.riverock.common.tools.MainTools;

/**
 * @author Sergei Maslyukov
 *         Date: 04.07.2006
 *         Time: 12:03:41
 */
public class DbConnectionProvider {
    private final static Logger log = Logger.getLogger(DbConnectionProvider.class);

    private static Map<String, String> familyMap = new HashMap<String, String>();
    static {
        familyMap.put("oracle", ORAconnect.class.getName());
        familyMap.put("mysql", MYSQLconnect.class.getName());
        familyMap.put("hsqldb", HSQLDBconnect.class.getName());
        familyMap.put("mssql", MSSQL_JTDS_connect.class.getName());
        familyMap.put("postgrees", PostgreeSQLconnect.class.getName());
        familyMap.put("db2", IBMDB2connect.class.getName());
        familyMap.put("sap", SAPconnect.class.getName());
    }

    private static final Object syncObject=new Object();
    private static Map<String, String> familyClassMap = new HashMap<String, String>();

    static DatabaseAdapter openConnect(final DatabaseConnectionType dc)
        throws DatabaseException {
        if (dc == null) {
            String es = "DatabaseConnection is null.";
            log.fatal(es);
            throw new DatabaseException(es);
        }

        String className=null;
        if (log.isDebugEnabled()) {
            log.debug("dc.getFamily(): " +dc.getFamily());
        }
        if (dc.getFamily()!=null) {
            className = familyClassMap.get(dc.getFamily());
            if (log.isDebugEnabled()) {
                log.debug("className: " + className);
            }

            if (className==null) {
                synchronized(syncObject) {
                    className = familyClassMap.get(dc.getFamily());
                    if (className==null) {
                        String name = "java:comp/env/"+dc.getFamily() ;
                        try {
                            InitialContext ic = new InitialContext();
                            String familyName = (String) ic.lookup( name );
                            className = familyMap.get(familyName);
                            if (className!=null) {
                                familyClassMap.put(dc.getFamily(), className);
                            }
                        }
                        catch (NamingException e) {
                            String es = "Error get value of DB family from JNDI. JNDI env: " +dc.getFamily();
                            log.error(es, e);
                            throw new IllegalArgumentException(es, e);
                        }
                    }
                }
            }
        }
        else if (dc.getConnectionClass()!=null) {
            className = dc.getConnectionClass();
        }

        if (className==null) {
            throw new IllegalArgumentException("Error configure db connection. Neither 'family' and 'className' are defined");
        }

        DatabaseAdapter db_ = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Call for create dynamic object " + className);
            }

            db_ = (DatabaseAdapter) MainTools.createCustomObject(className);
            db_.init(dc);

            if (log.isDebugEnabled()) {
                log.debug("Success create dynamic object " + className);
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
            log.fatal("Error create instance for family " + dc.getFamily());
            log.fatal("ConnectionName - " + dc.getName());
            log.fatal("Error:", e);

            final String es = "Error create DB instance. See log for details";
            System.out.println(es);
            throw new DatabaseException(es, e);
        }
        return db_;
    }
}
