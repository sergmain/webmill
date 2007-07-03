package org.riverock.webmill.admin.test.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.riverock.dbrevision.annotation.schema.db.DbSchema;
import org.riverock.dbrevision.annotation.schema.db.DbTable;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.webmill.admin.servlet.DbExportServlet;

/**
 * User: SergeMaslyukov
 * Date: 24.02.2007
 * Time: 19:53:17
 */
public class TestZipFile {
    private static final String DB_ZIP = "c:\\1\\2\\db.zip";

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(DB_ZIP);
        ZipInputStream zip = new ZipInputStream(fileInputStream);

        // search and process db structure file
        ZipEntry zipEntry;
        while ((zipEntry = zip.getNextEntry()) != null) {
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

                DbSchema schema = Utils.getObjectFromXml(DbSchema.class, inputStream);
                int i=0;
            }
        }
        zip.close();
        zip = null;
        fileInputStream.close();
        fileInputStream = null;


        fileInputStream = new FileInputStream(DB_ZIP);
        zip = new ZipInputStream(fileInputStream);
        while ((zipEntry = zip.getNextEntry()) != null) {
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
                int i=0;
            }
        }
        zip.close();
        zip = null;
        fileInputStream.close();
        fileInputStream = null;


    }
}
