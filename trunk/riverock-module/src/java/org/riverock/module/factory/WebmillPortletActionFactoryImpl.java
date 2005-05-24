package org.riverock.module.factory;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.config.ActionConfig;
import org.riverock.common.config.PropertiesProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:15:01
 *         $Id$
 */
public class WebmillPortletActionFactoryImpl extends PortletActionFactoryImpl {
    private static final Log log = LogFactory.getLog(WebmillPortletActionFactoryImpl.class);

    public WebmillPortletActionFactoryImpl(ModuleConfig moduleConfig, String factoryCode) throws ActionException {
        ActionConfig actionConfig = getActionConfig( moduleConfig, factoryCode );
        this.actionConfig = actionConfig;
    }

    protected File getConfigFile(String actionConfigFile) {
        String fileName = PropertiesProvider.getApplicationPath()+actionConfigFile;
        File file = new File(fileName);
        if (log.isDebugEnabled()) {
            log.debug("Full path to config file: " + fileName);
            log.debug("file exists: " + file.exists());
        }
        return file;
    }

    
}
