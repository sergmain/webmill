/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.module.factory;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.config.ActionConfigInstance;
import org.riverock.common.config.PropertiesProvider;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:15:01
 *         $Id: WebmillPortletActionFactoryImpl.java 1439 2007-09-27 14:23:59Z serg_main $
 */
public class WebmillPortletActionFactoryImpl extends PortletActionFactoryImpl {
    private static final Logger log = Logger.getLogger(WebmillPortletActionFactoryImpl.class);

    public WebmillPortletActionFactoryImpl(ModuleConfig moduleConfig, String factoryCode) throws ActionException {
        ActionConfigInstance actionConfigInstance = getActionConfig( moduleConfig, factoryCode );
        this.actionConfigInstance = actionConfigInstance;
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
