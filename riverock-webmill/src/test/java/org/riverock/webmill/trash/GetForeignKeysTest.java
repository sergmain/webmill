package org.riverock.webmill.trash;

import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hibernate.Session;

import org.riverock.dbrevision.db.DatabaseStructureManager;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseAdapterProvider;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.annotation.schema.db.DbSchema;
import org.riverock.dbrevision.annotation.schema.db.DbTable;
import org.riverock.dbrevision.annotation.schema.db.DbForeignKey;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.common.exception.DatabaseException;
import org.riverock.webmill.utils.HibernateUtilsTest;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * User: SMaslyukov
 * Date: 03.08.2007
 * Time: 20:59:52
 */
public class GetForeignKeysTest {
    
    public static void main(String[] args) throws DatabaseException, SQLException, FileNotFoundException, JAXBException {

        HibernateUtilsTest.prepareSession();


        Session session = HibernateUtils.getSession();
        DatabaseAdapter adapter = DatabaseAdapterProvider.getInstance(session.connection(), DatabaseAdapter.Family.MYSQL);
        DatabaseMetaData metaData = adapter.getConnection().getMetaData();
        String dbSchema = metaData.getUserName();
        System.out.println("dbSchema = " + dbSchema);

        List<DbForeignKey> fks = DatabaseStructureManager.getForeignKeys(adapter, dbSchema, "aaa");

        if (fks.isEmpty()) {
            throw new RuntimeException("fk is empty");
        }

        if (fks.size()>1) {
            throw new RuntimeException("fk is not one");
        }

        FileOutputStream outputStream = new FileOutputStream("fk.xml");

        Utils.writeObjectAsXml(fks.get(0), outputStream, "DbForeignKey", "utf-8");

        session.close();
        

    }
}