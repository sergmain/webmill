package org.riverock.webmill.admin.servlet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.riverock.dbrevision.annotation.schema.db.DbSchema;
import org.riverock.dbrevision.annotation.schema.db.DbTable;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.db.DatabaseStructureManager;
import org.riverock.dbrevision.db.DbConnectionProvider;
import org.riverock.dbrevision.exception.DbRevisionException;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.webmill.utils.HibernateUtils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.Connection;
import java.util.zip.GZIPOutputStream;

/**
 * User: SMaslyukov
 * Date: 22.02.2007
 * Time: 15:15:14
 */
public class DbExportServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(DbExportServlet.class);
    private static final int BUFFER_SIZE = 2048;
    private static final String APPLICATION_X_GZIP_CONTENT_TYPE = "application/x-gzip";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        File tempDir = (File) getServletConfig().getServletContext().getAttribute("javax.servlet.context.tempdir");
        File tempFile = File.createTempFile("db-struct", ".xml", tempDir);
        System.out.println("Output xml data to tempFile: " + tempFile);
        FileOutputStream fos = new FileOutputStream(tempFile);
        export(db, fos, true);
        session.getTransaction().commit();

        fos.flush();
        fos.close();
        fos = null;

        System.out.println("Finish marshaling XML data");

        File gzipFile = outputToGzip(tempFile);
        System.out.println("Finish output to .gz file");

        FileInputStream fis = new FileInputStream(gzipFile);
        response.setContentType(APPLICATION_X_GZIP_CONTENT_TYPE);
        OutputStream os = response.getOutputStream();

        int count = 0;
        byte[] bytes = new byte[BUFFER_SIZE];
        while( (count=fis.read())!=-1 ) {
            os.write(bytes, 0, count);
        }
        fis.close();
        fis = null;

        os.flush();
        os.close();
        os=null;


        System.out.println(
                "Output xml data to\n" +
                        "    tempFile: " + tempFile + "\n" +
                        "    gzipFile: " + gzipFile + "\n" +
                "\nFinish output data to http response."
        );
    }

    private static File outputToGzip(File tempFile) throws IOException {

        File gzipFile = File.createTempFile("db-struct", ".gz", tempFile.getParentFile());

        FileInputStream fis = new FileInputStream(tempFile);

        FileOutputStream fileOutputStream = new FileOutputStream(gzipFile);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);

        int count = 0;
        byte[] bytes = new byte[BUFFER_SIZE];
        while( (count=fis.read())!=-1 ) {
            gzipOutputStream.write(bytes, 0, count);
        }
        fis.close();
        fis = null;

        gzipOutputStream.flush();
        gzipOutputStream.close();
        gzipOutputStream=null;
        
        fileOutputStream.flush();
        fileOutputStream.close();
        fileOutputStream=null;

        return gzipFile;
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
