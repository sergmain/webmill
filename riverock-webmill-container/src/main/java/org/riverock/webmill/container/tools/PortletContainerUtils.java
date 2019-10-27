package org.riverock.webmill.container.tools;

import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import java.io.File;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2006
 * Time: 17:50:48
 * <p/>
 * $Id$
 */
public class PortletContainerUtils {
    public static String getDeployedInPath(ServletConfig servletConfig) {
        return new File(servletConfig.getServletContext().getRealPath("/")).getParent();
    }
}
