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
package org.riverock.module.web.config;

import java.util.ResourceBundle;
import java.util.Locale;

import org.riverock.module.web.context.ModuleContext;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:46:00
 *         $Id$
 */
public interface ModuleConfig {
    public ModuleContext getContext();

    public String getInitParameter(String key);
    public ResourceBundle getResourceBundle(Locale locale);

    public Integer getInitParameterInt( String name );
    public Integer getInitParameterInt( String name, Integer defValue );

    public Long getInitParameterLong( String name );
    public Long getInitParameterLong( String name, Long defValue );

    public Double getInitParameterDouble( String name );
    public Double getInitParameterDouble( String name, Double defValue );
}
