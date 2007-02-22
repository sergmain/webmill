package org.riverock.webmill.admin.servlet;

import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.db.DatabaseStructureManager;
import org.riverock.dbrevision.db.DbConnectionProvider;
import org.riverock.dbrevision.db.factory.ORAconnect;
import org.riverock.dbrevision.offline.DbRevisionConfig;
import org.riverock.dbrevision.annotation.schema.db.DbSchema;
import org.riverock.dbrevision.annotation.schema.db.DbTable;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.dbrevision.exception.DbRevisionException;
import org.riverock.dbrevision.system.DbStructureImport;
import org.riverock.webmill.utils.HibernateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.sql.Connection;

/**
 * User: SMaslyukov
 * Date: 22.02.2007
 * Time: 15:15:14
 */
public class DbExportServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(DbExportServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        OutputStream os = response.getOutputStream();
        
        String family;
        String name = "java:comp/env/dbFamily";
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
        DatabaseAdapter db  = DbConnectionProvider.openConnect(connection, family);

        export(db, os, true);

        session.getTransaction().commit();

        os.flush();
        os.close();
        os = null;
    }

    public static void export(DatabaseAdapter db, OutputStream outputStream, boolean isData) {
        try {
            DbSchema schema = DatabaseManager.getDbStructure(db);
            for (DbTable table : schema.getTables()) {
                if (isData) {
                    table.setData(DatabaseStructureManager.getDataTable(db.getConnection(), table, db.getFamily()));
                }
            }
            Utils.writeObjectAsXml(schema, outputStream, "utf-8");
        } catch (Exception e) {
            log.error("Error export db", e);
            throw new DbRevisionException(e);
        }
    }
}
