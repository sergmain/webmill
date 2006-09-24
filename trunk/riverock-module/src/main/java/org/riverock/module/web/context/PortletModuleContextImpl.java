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
package org.riverock.module.web.context;

import javax.portlet.PortletContext;

import org.riverock.module.web.dispatcher.ModuleRequestDispatcher;
import org.riverock.module.web.dispatcher.PortletModuleRequestDispatcherImpl;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 15:31:00
 *         $Id$
 */
public class PortletModuleContextImpl implements ModuleContext {
    private PortletContext portletContext = null;

    public PortletModuleContextImpl(PortletContext portletContext){
        this.portletContext = portletContext;
    }


    public ModuleRequestDispatcher getRequestDispatcher(String url) {
        return new PortletModuleRequestDispatcherImpl(portletContext.getRequestDispatcher(url));
    }
}
