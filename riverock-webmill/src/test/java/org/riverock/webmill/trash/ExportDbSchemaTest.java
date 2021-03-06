package org.riverock.webmill.trash;

import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.hibernate.Session;

import org.riverock.common.exception.DatabaseException;
import org.riverock.dbrevision.schema.db.v3.DbSchema;
import org.riverock.webmill.portal.dao.HibernateUtilsTest;
import org.riverock.webmill.portal.dao.HibernateUtils;
import org.riverock.dbrevision.db.DatabaseFactory;
import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.schema.db.v3.DbTable;
import org.riverock.dbrevision.utils.Utils;

/**
 * @author Sergei Maslyukov
 *         Date: 05.10.2006
 *         Time: 20:09:19
 *         <p/>
 *         $Id$
 */
public class ExportDbSchemaTest {
    public static void main(String[] args) throws DatabaseException, SQLException, FileNotFoundException, JAXBException {

        HibernateUtilsTest.prepareSession();
        Session session = HibernateUtils.getSession();

/*
        Database adapter = DatabaseFactory.getInstance(session.connection(), Database.Family.MYSQL);

        DbSchema schema = DatabaseManager.getDbStructure(adapter, true);
        DbTable t = null;
        for (DbTable dbTable : schema.getTables()) {
            if (dbTable.getT().equalsIgnoreCase("aaa")) {
                t = dbTable;
            }
        }

        FileOutputStream outputStream = new FileOutputStream("table.xml");
        Utils.writeObjectAsXml(t, outputStream, "Table", "utf-8");
*/

        session.close();
        

    }
}
