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
