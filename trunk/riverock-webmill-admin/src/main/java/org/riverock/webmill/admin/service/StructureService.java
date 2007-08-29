package org.riverock.webmill.admin.service;

import java.io.Serializable;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import org.hibernate.Session;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.db.DatabaseFactory;
import org.riverock.dbrevision.manager.DbRevisionManager;
import org.riverock.webmill.admin.AdminConstants;
import org.riverock.webmill.portal.dao.HibernateUtils;

/**
 * User: SergeMaslyukov
 * Date: 05.08.2007
 * Time: 18:15:17
 */
public class StructureService implements Serializable {
    private static Logger log = Logger.getLogger(StructureService.class);

    private DbRevisionManager manager=null;

    public StructureService() {
        init();
    }

    public DbRevisionManager getManager() {
        return manager;
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
        Database database = DatabaseFactory.getInstance(connection, family);

        String dbrevisionPath = PropertiesProvider.getApplicationPath() + AdminConstants.DBREVISION_PATH;
        log.debug("structure file name: " + dbrevisionPath);

        manager = new DbRevisionManager(database, dbrevisionPath);

        if (log.isDebugEnabled()) {
            log.debug("Modules:\n" + manager.getModules());
        }
    }
}
