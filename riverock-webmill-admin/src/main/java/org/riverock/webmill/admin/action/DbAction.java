package org.riverock.webmill.admin.action;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.Session;
import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.db.DatabaseFactory;
import org.riverock.dbrevision.db.DatabaseStructureManager;
import org.riverock.dbrevision.schema.db.v3.DbSchema;
import org.riverock.dbrevision.schema.db.v3.DbTable;
import org.riverock.dbrevision.system.DbStructureImport;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.webmill.admin.CompanySessionBean;
import org.riverock.webmill.admin.bean.UploadedFileBean;
import org.riverock.webmill.admin.servlet.DbExportServlet;
import org.riverock.webmill.portal.dao.HibernateUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * User: SMaslyukov
 * Date: 22.02.2007
 * Time: 15:09:07
 */
public class DbAction  implements Serializable {
    private static final long serialVersionUID = 2055005501L;
    private final static Logger log = Logger.getLogger(DbAction.class);

    private CompanySessionBean companySessionBean = null;

    private UploadedFileBean uploadedFileBean = null;

    public DbAction() {
    }

    public UploadedFileBean getUploadedFileBean() {
        return uploadedFileBean;
    }

    public void setUploadedFileBean(UploadedFileBean uploadedFileBean) {
        this.uploadedFileBean = uploadedFileBean;
    }

    public CompanySessionBean getCompanySessionBean() {
        return companySessionBean;
    }

    public void setCompanySessionBean(CompanySessionBean companySessionBean) {
        this.companySessionBean = companySessionBean;
    }

    public String upload() {
        log.debug("Start upload();");
        String family = DbExportServlet.getDbFamily();
        
        Session session=null;
        try {
            UploadedFile uploadedFile = uploadedFileBean.getUploadedFile();
            log.debug("Create ZipInputStream.");
            ZipInputStream zip = new ZipInputStream(uploadedFile.getInputStream());

            log.debug("Prepare connection.");
            session = HibernateUtils.getSession();
            session.beginTransaction();

//            session.doWork(connection -> doSomething(connection));
//            Connection connection = session.connection();
            Connection connection = null;
            Database db  = DatabaseFactory.getInstance(connection, family);

            log.debug("Start loop with zipEntry for search db structure file.");
            // search and process db structure file
            ZipEntry zipEntry;
            while ((zipEntry = zip.getNextEntry())!=null) {
                log.debug("ZipEntry name: " + zipEntry.getName());
                if (zipEntry.getName().equals(DbExportServlet.DB_SCHEMA_XML)) {
                    // because InputSource close associated inputStream, we read input strem in temporary buffer
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int zipeSize = (int) zipEntry.getSize();
                    byte[] buf = new byte[zipeSize];
                    int bytesRead;
                    while ( (bytesRead = zip.read(buf))!=-1) {
                        bos.write(buf, 0, bytesRead);
                    }
                    bos.flush();
                    bos.close();
                    byte[] data = bos.toByteArray();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

                    log.debug("Marshall schema to object");
                    DbSchema schema = Utils.getObjectFromXml(DbSchema.class, inputStream);

                    log.debug("Table for processing:");
                    for (DbTable dbTable : schema.getTables()) {
                        log.debug( "    table name: "+ dbTable.getT() );
                    }

                    log.debug("Import DB structure");
                    DbStructureImport.importStructure(db, schema, false);
                }
            }
            connection.commit();
            zip.close();
            zip = null;
//            byteArrayInputStream.close();
//            byteArrayInputStream = null;


            log.debug("Start loop with zipEntry for import db table data.");
//            byteArrayInputStream = new ByteArrayInputStream(bytes);
//            zip = new ZipInputStream(byteArrayInputStream);
            zip = new ZipInputStream(uploadedFile.getInputStream());
            
            while ((zipEntry = zip.getNextEntry())!=null) {
                log.debug("ZipEntry name: " + zipEntry.getName());
                if (zipEntry.getName().startsWith(DbExportServlet.DB_FILE_PREFIX)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int zipeSize = (int) zipEntry.getSize();
                    byte[] buf = new byte[zipeSize];
                    int bytesRead;
                    while ( (bytesRead = zip.read(buf))!=-1) {
                        bos.write(buf, 0, bytesRead);
                    }
                    bos.flush();
                    bos.close();
                    byte[] data = bos.toByteArray();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

                    DbTable table = Utils.getObjectFromXml(DbTable.class, inputStream);
/*
                    if (db.getFamily()== DatabaseManager.MYSQL_FAMALY) {
                        table.setName( table.getName().toLowerCase() );
                    }
*/
                    DatabaseStructureManager.setDataTable(db, table);

                    connection.commit();
                }
            }
            zip.close();
            zip = null;
//            byteArrayInputStream.close();
//            byteArrayInputStream = null;

            log.debug("Commit all data.");

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session!=null) {
                session.getTransaction().rollback();
            }
            String es = "Erorr upload file";
            log.error(es, e);
            throw new RuntimeException(es, e);
        }

        log.debug("Forward to 'db' action");
        return "db";
    }


}

