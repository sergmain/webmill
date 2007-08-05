package org.riverock.webmill.admin.service;

import java.sql.Connection;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.Session;

import org.apache.log4j.Logger;

import org.riverock.dbrevision.manager.Module;
import org.riverock.dbrevision.manager.DbRevisionManager;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseAdapterProvider;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.webmill.admin.AdminConstants;

/**
 * User: SergeMaslyukov
 * Date: 05.08.2007
 * Time: 18:15:17
 */
public class StructureService {
    private static Logger log = Logger.getLogger(StructureService.class);

    private DbRevisionManager manager=null;

    public DbRevisionManager getManager() {
        return manager;
    }

    public StructureService() {
        init();
    }

    public void init() {
        String family;
        String name = AdminConstants.JAVA_COMP_ENV_DB_FAMILY;
        try {
            InitialContext ic = new InitialContext();
            family = (String) ic.lookup(name);
        }
        catch (NamingException e) {
            String es = "Error get value of DB family from JNDI. JNDI env: " + name;
            log.error(es, e);
            throw new IllegalArgumentException(es, e);
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Connection connection = session.connection();
        DatabaseAdapter adapter = DatabaseAdapterProvider.getInstance(connection, family);

        String dbrevisionPath = PropertiesProvider.getApplicationPath() + AdminConstants.DBREVISION_PATH;
        log.debug("structure file name: " + dbrevisionPath);

        manager = new DbRevisionManager(adapter, dbrevisionPath);
    }
}
