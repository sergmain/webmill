package org.riverock.webmill.admin.servlet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.riverock.dbrevision.annotation.schema.db.DbSchema;
import org.riverock.dbrevision.annotation.schema.db.DbTable;
import org.riverock.dbrevision.annotation.schema.db.DbDataTable;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.db.DatabaseStructureManager;
import org.riverock.dbrevision.db.DbConnectionProvider;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.webmill.utils.HibernateUtils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.Connection;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

/**
 * User: SMaslyukov
 * Date: 22.02.2007
 * Time: 15:15:14
 */
public class DbExportServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(DbExportServlet.class);
    private static final int BUFFER_SIZE = 2048;
    private static final String APPLICATION_ZIP_CONTENT_TYPE = "application/zip";
    private static final String DB_ZIP = "db.zip";
    
    public static final String DB_SCHEMA_XML = "schema.xml";
    public static final String JAVA_COMP_ENV_DB_FAMILY = "java:comp/env/dbFamily";
    public static final String DB_FILE_PREFIX = "db-";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String family;
        family = getDbFamily();

        try {
            processDB(family, response);
        } catch (Exception e) {
            throw new RuntimeException("Error export db", e);
        }
    }

    public static String getDbFamily() {
        String family;
        String name = JAVA_COMP_ENV_DB_FAMILY;
        try {
            InitialContext ic = new InitialContext();
            family = (String) ic.lookup(name);
        }
        catch (NamingException e) {
            String es = "Error get value of DB family from JNDI. JNDI env: " + name;
            log.error(es, e);
            throw new IllegalArgumentException(es, e);
        }
        return family;
    }

    private void processDB(String family, HttpServletResponse response) throws Exception {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Connection connection = session.connection();
        DatabaseAdapter db  = DbConnectionProvider.openConnect(connection, family);

        File tempDir = (File) getServletConfig().getServletContext().getAttribute("javax.servlet.context.tempdir");
        File resultFile = File.createTempFile("db-struct-", ".zip", tempDir);

        System.out.println("Output xml data to resultFile: " + resultFile);

        DbSchema schema = DatabaseManager.getDbStructure(db);

        FileOutputStream fileOutputStream = new FileOutputStream(resultFile);
        ZipOutputStream out = new ZipOutputStream(fileOutputStream);

        zipFile(out, schema, DB_SCHEMA_XML, null);
        for (DbTable table : schema.getTables()) {
            if (isSkipTable(table.getName()))  {
                continue;
            }
            DbDataTable data = DatabaseStructureManager.getDataTable(db, table);
            table.setData(data);
            zipFile(out, table, DB_FILE_PREFIX +table.getName()+".xml", "TableData");
            data = null;
            table.setData(null);
        }
        out.flush();
        out.close();
        out = null;
        session.getTransaction().commit();

        System.out.println("Finish output to .zip file");

        FileInputStream fis = new FileInputStream(resultFile);
        response.setContentType(APPLICATION_ZIP_CONTENT_TYPE);
        OutputStream os = response.getOutputStream();

        int count;
        byte[] bytes = new byte[BUFFER_SIZE];
        while( (count=fis.read(bytes))!=-1 ) {
            os.write(bytes, 0, count);
        }
        fis.close();
        fis = null;

        os.flush();
        os.close();
        os=null;


        System.out.println(
                "Output xml data to\n" +
                        "    tempFile: " + tempDir + "\n" +
                        "  resultFile: " + resultFile + "\n" +
                "\nFinish output data to http response."
        );
    }

    private boolean isSkipTable(String name) {
        if (name==null) {
            return true;
        }

        String n = name.toLowerCase();
        if (n.startsWith("wm_price") ||
                n.startsWith("wm_cash") ||
                n.startsWith("wm_portal_access") ||
                n.startsWith("wm_job") ||
                n.startsWith("wm_image")) {
            return true;
        }
        return false;
    }

    private void zipFile(ZipOutputStream out, Object obj, String entryName, String rootElement) throws Exception {

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(entryName));
        Utils.writeObjectAsXml(obj, out, rootElement, "utf-8");
        // Complete the entry
        out.flush();
        out.closeEntry();
    }

    private void writeToFile(Object obj, File dir, String fileName) throws Exception {
        File file =new File(dir, fileName);
        FileOutputStream fos = new FileOutputStream(file);

        Utils.writeObjectAsXml(obj, fos, "utf-8");

        fos.flush();
        fos.close();
        fos = null;
        file = null;
    }
}
