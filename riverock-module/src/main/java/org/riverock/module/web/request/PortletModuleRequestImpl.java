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

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.module.web.session.ModuleSession;
import org.riverock.module.web.session.PortletModuleSessionImpl;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 15:23:20
 *         $Id$
 */
public abstract class PortletModuleRequestImpl implements ModuleRequest {
    static private final Logger log = Logger.getLogger(PortletModuleRequestImpl.class);

    protected PortletRequest portletRequest = null;

    public Object getOriginRequest() {
        return portletRequest;
    }

    public Locale getLocale() {
        return portletRequest.getLocale();
    }

    public String getServerName() {
        return portletRequest.getServerName();
    }

    public Integer getInt(String key) {
        return PortletService.getInt(portletRequest, key);
    }

    public Integer getInt(String key, Integer defValue) {
        return PortletService.getInt(portletRequest, key, defValue);
    }

    public String getString(String key) {
        if (log.isDebugEnabled()){
            log.debug("key: "+key + ", value: "+PortletService.getString(portletRequest, key, null));
        }
        return PortletService.getString(portletRequest, key, null);
    }

    public String getString(String key, String defValue) {
        return PortletService.getString(portletRequest, key, defValue);
    }

    public Long getLong(String key) {
        return PortletService.getLong(portletRequest, key);
    }

    public Long getLong(String key, Long defValue) {
        return PortletService.getLong(portletRequest, key, defValue);
    }

    public Double getDouble(String key) {
        return PortletService.getDouble(portletRequest, key);
    }

    public Double getDouble(String key, Double defValue) {
        return PortletService.getDouble(portletRequest, key, defValue);
    }

    public String getParameter(String key) {
        return portletRequest.getParameter(key);
    }

    public void setAttribute(String key, Object value) {
        portletRequest.setAttribute(key, value);
    }

    public ModuleSession getSession() {
        return getSession(true);
    }

    public ModuleSession getSession(boolean isCreate) {
        return new PortletModuleSessionImpl( portletRequest.getPortletSession(isCreate) );
    }

    public boolean isUserInRole(String role) {
        return portletRequest.isUserInRole( role );
    }
}