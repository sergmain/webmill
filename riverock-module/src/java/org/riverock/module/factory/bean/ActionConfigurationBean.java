package org.riverock.module.factory.bean;

import org.riverock.module.action.Action;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 14:23:50
 *         $Id$
 */
public class ActionConfigurationBean {
    private ActionBean actionBean = null;
    private Action action = null;

    public ActionBean getActionBean() {
        return actionBean;
    }

    public void setActionBean(ActionBean actionBean) {
        this.actionBean = actionBean;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
