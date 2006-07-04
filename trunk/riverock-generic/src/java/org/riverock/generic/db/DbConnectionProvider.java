package org.riverock.generic.db;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.generic.db.factory.ORAconnect;
import org.riverock.generic.db.factory.MYSQLconnect;
import org.riverock.common.tools.MainTools;

/**
 * @author Sergei Maslyukov
 *         Date: 04.07.2006
 *         Time: 12:03:41
 */
public class DbConnectionProvider {
    private final static Logger log = Logger.getLogger(DbConnectionProvider.class);

    private static Map<String, String> famalyMap = new HashMap<String, String>();
    static {
        famalyMap.put("oracle", ORAconnect.class.getName());
        famalyMap.put("mysql", MYSQLconnect.class.getName());
    }

    static DatabaseAdapter openConnect(final DatabaseConnectionType dc)
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
}
