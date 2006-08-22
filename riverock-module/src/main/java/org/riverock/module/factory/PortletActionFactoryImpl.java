/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 *
 */
package org.riverock.module.factory;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.config.ActionConfig;
import org.riverock.common.tools.StringTools;

import java.io.File;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:54:12
 *         $Id$
 */
public abstract class PortletActionFactoryImpl extends ActionFactoryImpl {

    protected abstract File getConfigFile(String actionConfigFile);

    public ActionConfig getActionConfig(ModuleConfig moduleConfig, String factoryCode) throws ActionException {
        String actionConfigFile = moduleConfig.getInitParameter( factoryCode );
        if (StringTools.isEmpty( actionConfigFile ) ) {
            throw new ActionException("Factory code "+factoryCode+ " not found in init parameter of portlet");
        }

        File configFile = getConfigFile( actionConfigFile );
        if (configFile==null || !configFile.exists()) {
            throw new ActionException("File "+actionConfigFile+" not found");
        }

        ActionConfig actionConfig = ActionConfig.getInstance( configFile );

        return actionConfig;
    }

}