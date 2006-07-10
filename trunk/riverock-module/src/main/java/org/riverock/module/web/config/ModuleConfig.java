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
