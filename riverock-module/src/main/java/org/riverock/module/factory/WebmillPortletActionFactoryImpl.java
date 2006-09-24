/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.module.factory;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.config.ActionConfig;
import org.riverock.common.config.PropertiesProvider;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:15:01
 *         $Id$
 */
public class WebmillPortletActionFactoryImpl extends PortletActionFactoryImpl {
    private static final Logger log = Logger.getLogger(WebmillPortletActionFactoryImpl.class);

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
