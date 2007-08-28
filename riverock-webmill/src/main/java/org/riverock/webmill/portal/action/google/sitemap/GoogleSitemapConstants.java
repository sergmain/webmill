package org.riverock.webmill.portal.action.google.sitemap;

import java.io.File;

/**
 * User: SMaslyukov
 * Date: 28.08.2007
 * Time: 16:04:11
 */
public class GoogleSitemapConstants {
    static final String SITEMAP_DIR = "WEB-INF" + File.separatorChar+ "webmill-data" + File.separatorChar+ "sitemap" + File.separatorChar;
    static final String SITEMAP_XML = "sitemap.xml.gz";
    static final int BUFFER_SIZE = 1024;
    static final String APPLICATION_X_GZIP_CONTENT_TYPE = "application/x-gzip";
}
