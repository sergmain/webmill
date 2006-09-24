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
package org.riverock.module.web.request;

import java.util.Locale;

import org.riverock.module.exception.ModuleException;
import org.riverock.module.web.session.ModuleSession;
import org.riverock.module.web.user.ModuleUser;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:39:17
 *         $Id$
 */
public interface ModuleRequest {

    public Object getOriginRequest();
    public Locale getLocale();

    public Integer getInt(String key);
    public Integer getInt(String key, Integer defValue);
    public String getString(String key);
    public String getString(String key, String defValue);
    public Long getLong(String key);
    public Long getLong(String key, Long defValue);
    public Double getDouble(String key);
    public Double getDouble(String key, Double defValue);

    public String getParameter(String key);
    public void setAttribute(String key, Object value);

    public ModuleSession getSession();
    public ModuleSession getSession(boolean isCreate);

    public ModuleUser getUser();

    public String getServerName();
    public String getRemoteAddr();
    public String getUserAgent();

    public boolean isUserInRole(String role);

    public Long getSiteId() throws ModuleException;

    public Object getAttribute(String key);
}
