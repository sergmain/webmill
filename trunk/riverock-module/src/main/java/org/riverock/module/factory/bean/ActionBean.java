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
package org.riverock.module.factory.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:54:09
 *         $Id$
 */
public class ActionBean {
    private String name;
    private String type;
    private String path;
    private String defaultForward;
    private Map<String, ForwardBean> forwards = new HashMap<String, ForwardBean>();
    private List<String> roles = new ArrayList<String>();
    private boolean isRedirect = false;

    public void addForwards( ForwardBean forwardBean ) {
        forwards.put( forwardBean.getName(), forwardBean );
    }

    public Map<String, ForwardBean> getForwards() {
        return forwards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDefaultForward() {
        return defaultForward;
    }

    public void setDefaultForward(String defaultForward) {
        this.defaultForward = defaultForward;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add( role );
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }
}
