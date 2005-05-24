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
    private Map forwards = new HashMap();
    private List roles = new ArrayList();
    private boolean isRedirect = false;

    public void addForwards( ForwardBean forwardBean ) {
        forwards.put( forwardBean.getName(), forwardBean );
    }

    public Map getForwards() {
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

    public List getRoles() {
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
