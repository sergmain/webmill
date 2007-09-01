package org.riverock.webmill.utils;

import org.riverock.webmill.portal.action.google.sitemap.GoogleSitemapConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * User: SergeMaslyukov
 * Date: 01.09.2007
 * Time: 22:10:06
 */
public class ServletUtils {
    private static final int BUFFER_SIZE = 0x200;

    @SuppressWarnings({"UnusedAssignment"})
    public static void outputFileToResponse(HttpServletResponse response, File sitemap, String contentType) throws IOException {
        InputStream is = new FileInputStream(sitemap);
        if (contentType!=null) {
            response.setContentType(contentType);
        }
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[BUFFER_SIZE];
        int count;
        while ((count=is.read(bytes))!=-1) {
            os.write(bytes, 0, count);
        }
        os.flush();
        os.close();
        os=null;
        is.close();
        is=null;
    }
}
