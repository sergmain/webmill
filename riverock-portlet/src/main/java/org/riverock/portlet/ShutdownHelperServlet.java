package org.riverock.portlet;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;

import net.sf.ehcache.CacheManager;

/**
 * User: SMaslyukov
 * Date: 15.08.2007
 * Time: 19:38:50
 */
public class ShutdownHelperServlet extends HttpServlet {

    public void destroy() {
        LogFactory.releaseAll();
        LogManager.shutdown();
        CacheManager.getInstance().shutdown();
    }
}
