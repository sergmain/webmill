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
    private List actions = new ArrayList();
    private List roles = new ArrayList();
    private Map forwards = new HashMap();

    public void addForwards( ForwardBean forwardBean ) {
        forwards.put( forwardBean.getName(), forwardBean );
    }

    public Map getForwards() {
        return forwards;
    }

    public String getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction( String defaultAction ) {
        this.defaultAction = defaultAction;
    }

    public List getActions() {
        return actions;
    }

    public void setActions(List actions) {
        this.actions = actions;
    }

    public void addActions(ActionBean action) {
        this.actions.add(action);
    }

    public List getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add( role );
    }
}
