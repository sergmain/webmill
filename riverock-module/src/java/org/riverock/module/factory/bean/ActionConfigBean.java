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
