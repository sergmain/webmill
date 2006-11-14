/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 */
package org.riverock.module.factory.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:38:33
 *         $Id$
 */
public class ActionConfigBean {
    private String defaultAction = null;
    private List<ActionBean> actions = new ArrayList<ActionBean>();
    private List<String> roles = new ArrayList<String>();
    private Map<String, ForwardBean> forwards = new HashMap<String, ForwardBean>();

    public void addForwards( ForwardBean forwardBean ) {
        forwards.put( forwardBean.getName(), forwardBean );
    }

    public Map<String, ForwardBean> getForwards() {
        return forwards;
    }

    public String getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction( String defaultAction ) {
        this.defaultAction = defaultAction;
    }

    public List<ActionBean> getActions() {
        return actions;
    }

    public void setActions(List<ActionBean> actions) {
        this.actions = actions;
    }

    public void addActions(ActionBean action) {
        this.actions.add(action);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add( role );
    }
}
