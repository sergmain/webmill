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

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionConfigurationBean;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:53:55
 *         $Id$
 */
public interface ActionFactory {
    public ActionConfigurationBean getAction(String actionName);
    public ActionConfigurationBean getDefaultAction();
    public void destroy();
    public String doAction(ModuleActionRequest moduleActionRequest) throws ActionException;
    public String getForwardPath(String actionName, String forwardName) throws ActionException;
}
