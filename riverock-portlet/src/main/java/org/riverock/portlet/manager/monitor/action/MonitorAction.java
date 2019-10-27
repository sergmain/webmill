package org.riverock.portlet.manager.monitor.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.XmlTools;
import org.riverock.portlet.manager.monitor.ServerMonitorUtils;
import org.riverock.portlet.manager.monitor.ServerMonitorConstants;
import org.riverock.portlet.manager.monitor.schema.Directory;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.ContainerConstants;

/**
 * User: SMaslyukov
 * Date: 15.05.2007
 * Time: 19:16:26
 */
public class MonitorAction {
    private final static Logger log = Logger.getLogger( MonitorAction.class );

    private static final int BUFFER_SIZE = 1024;

    private String result = null;
    private static final String WEBMILL_MONITOR = "webmill-monitor";

    public static final String[] ROLES = new String[]{"webmill.portal-manager"};

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String clearResult() {
        return WEBMILL_MONITOR;
    }

    public String createServerSizeFileAction() {
        try {
            PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

            createServerSizeFile();
        }
        catch(Throwable th) {
            String es = "Error create server size file";
            log.error(es, th);
            result = es + ' ' + th.toString();
            return WEBMILL_MONITOR;
        }
        result = "File with server size is created.";
        return WEBMILL_MONITOR;
    }

    private void createServerSizeFile() throws Exception {
        Long siteId = Long.decode(FacesTools.getPortletRequest().getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
        String serverPath = PropertiesProvider.getApplicationPath() + ServerMonitorConstants.SERVER_MONITOR_DIR + siteId;

        File path = new File(serverPath);
        if (log.isDebugEnabled()) {
            log.debug("sitemap dir: " + path);
            log.debug("sitemap dir exists: " + path.exists());
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        File serverSize = new File(path, ServerMonitorConstants.SERVER_SIZE_XML);
        if (serverSize.exists()) {
            serverSize.delete();
        }

        log.debug("Start getDirectories()");
        Directory d = ServerMonitorUtils.getDirectories(new File(File.separator));
        log.debug("Done getDirectories()");

        log.debug("Start XmlTools.getXml()");
        byte[] bytes = XmlTools.getXml(d, ServerMonitorConstants.ROOT_ELEMENT_NAME, StandardCharsets.UTF_8.toString());
        log.debug("Done XmlTools.getXml()");
        
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        FileOutputStream os = new FileOutputStream(serverSize);
        int count;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((count=is.read(buffer))!=-1) {
            os.write(buffer, 0, count);
        }
        os.flush();
        os.close();
        os = null;
        is.close();
        is = null;
        if (!serverSize.exists()) {
            String es = "Server size file not created for unknown reason";
            log.warn(es);
            throw new RuntimeException(es);
        }
    }
}
