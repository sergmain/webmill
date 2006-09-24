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
