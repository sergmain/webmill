package org.riverock.webmill.container.portlet;

import javax.servlet.ServletConfig;
import java.io.File;

/**
 * User: SergeMaslyukov
 * Date: 26.11.2006
 * Time: 0:57:08
 * <p/>
 * $Id$
 */
class WaitDigestFile {
    File file = null;
    ServletConfig servletConfig = null;
    ClassLoader classLoader = null;
    String uniqueName = null;
    String portalPath = null;

    WaitDigestFile(String uniqueName, File file, ServletConfig servletConfig, ClassLoader classLoader, String portalPath) {
        this.file = file;
        this.servletConfig = servletConfig;
        this.classLoader = classLoader;
        this.uniqueName = uniqueName;
        this.portalPath = portalPath;
    }
}
