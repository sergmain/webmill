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
package org.riverock.module.factory;

import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.module.Constants;
import org.riverock.module.action.ActionInstance;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.config.schema.Action;
import org.riverock.module.config.schema.Forward;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionConfigurationBean;
import org.riverock.module.factory.config.ActionConfigInstance;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.user.ModuleUser;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:31:15
 *         $Id: ActionFactoryImpl.java 1445 2007-09-28 15:57:11Z serg_main $
 */
public abstract class ActionFactoryImpl implements ActionFactory {
    private static final Logger log = Logger.getLogger(WebmillPortletActionFactoryImpl.class);

    protected ActionConfigInstance actionConfigInstance = null;

    public String doAction(ModuleActionRequest moduleActionRequest) throws ActionException {
        ModuleUser user = moduleActionRequest.getRequest().getUser();
        if (user != null) {
            moduleActionRequest.getRequest().setAttribute(Constants.PRINCIPAL_BEAN, user);
        }

        if (log.isDebugEnabled()) {
            log.debug("user: " + user);
            log.debug("actionString: " + moduleActionRequest.getActionName());
        }

        ActionConfigurationBean actionConfigurationBean = null;
        if (moduleActionRequest.getActionName()!=null) {
            actionConfigurationBean = getAction(moduleActionRequest.getActionName());
        }
        if (log.isDebugEnabled()) {
            log.debug("actionConfigurationBean: " + actionConfigurationBean);
        }
        if (actionConfigurationBean == null) {
            actionConfigurationBean = getDefaultAction();
        }
        if (log.isDebugEnabled()) {
            log.debug("result actionConfigurationBean: " + actionConfigurationBean);
        }

        Action actionBean = actionConfigurationBean.getActionBean();
        // check global roles
        String forwardPath = checkAccess(actionConfigInstance.getConfigBean().getRoles(), actionBean, moduleActionRequest.getRequest());
        if (forwardPath!=null){
            return forwardPath;
        }

        // check action specific roles
        forwardPath = checkAccess(actionBean.getRoles(), actionBean, moduleActionRequest.getRequest());
        if (forwardPath!=null){
            return forwardPath;
        }

        Forward forward;
        String forwardName = actionConfigurationBean.getAction().execute(moduleActionRequest);
        if (log.isDebugEnabled()) {
            log.debug("forwardName after execute action: " + forwardName);
            log.debug("actionBean.isRedirect(): " + actionBean.isRedirect());
        }
        if (forwardName == null) {
            if (actionBean.isRedirect()) {
                return null;
            }
            if (actionBean.getDefaultForward() == null) {
                if (log.isDebugEnabled()) {
                    log.debug("forwardName is null defaultForward for action '" + moduleActionRequest.getActionName() + "' is null");
                }
                forward = getForwardPath(actionBean, ActionInstance.DEFAULT_FORWARD_ERROR);
                if (forward == null) {
                    throw new IllegalStateException();
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Return defaultForward for action '" + moduleActionRequest.getActionName() + "': " + actionBean.getDefaultForward());
            }
            return actionBean.getDefaultForward();
        }

        forward = getForwardPath(actionBean, forwardName);
        if (forward == null) {
            if (actionBean.getDefaultForward()==null) {
                throw new IllegalStateException("Forward with name '"+forwardName+"' not found");
            }
            else {
                return actionBean.getDefaultForward();
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Forward path: " + forward.getPath());
        }
        return forward.getPath();
    }

    public String getForwardPath(String actionName, String forwardName) {
        ActionConfigurationBean actionConfigurationBean = null;
        if (actionName!=null) {
            actionConfigurationBean = getAction(actionName);
        }
        if (actionConfigurationBean==null) {
            actionConfigurationBean = getDefaultAction();
        }
        if (actionConfigurationBean==null) {
            throw new IllegalStateException("actionConfigurationBean not created. actionName: " + actionName);
        }

        Action actionBean = actionConfigurationBean.getActionBean();

        Forward forward = getForwardPath(actionBean, forwardName);
        if (forward == null) {
            throw new IllegalStateException("Forward with name '"+forwardName+"' not found");
        }
        if (log.isDebugEnabled()) {
            log.debug("Forward path: " + forward.getPath());
        }
        return forward.getPath();
    }

    private String checkAccess(List<String> roles, Action actionBean, ModuleRequest moduleRequest) {
        Forward forward;
        if (!roles.isEmpty()) {
            log.debug("Check roles");
            // check role.
            for (String role : roles) {
                if (log.isDebugEnabled()) {
                    log.debug("   role: " + role + ", user has this role: " + moduleRequest.isUserInRole(role));
                }
                if (moduleRequest.isUserInRole(role)) {
                    return null;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("isDenied");
            }
            if (log.isDebugEnabled()) {
                log.debug("Start search forward '" + ActionInstance.DEFAULT_FORWARD_ACCESS_DENIED + "'");
            }
            forward = getForwardPath(actionBean, ActionInstance.DEFAULT_FORWARD_ACCESS_DENIED);

            if (forward == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Start search forward '" + ActionInstance.DEFAULT_FORWARD_ERROR + "'");
                }
                forward = getForwardPath(actionBean, ActionInstance.DEFAULT_FORWARD_ERROR);
            }

            if (forward == null) {
                throw new IllegalStateException();
            }

            if (log.isDebugEnabled()) {
                log.debug("Result forward path: " + forward.getPath());
            }
            return forward.getPath();
        }
        return null;
    }

    private Forward getForwardPath(Action actionBean, String name) {
        Forward forward=null;
        for (Forward f : actionBean.getForwards()) {
            if (f.getName().equals(name)) {
                forward=f;
                break;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("ForwardBean for name '" + name + "' is " + forward);
        }
        if (forward == null) {
            for (Forward f : actionConfigInstance.getConfigBean().getForwards()) {
                if (f.getName().equals(name)) {
                    forward=f;
                    break;
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Result forwardBean for name '" + name + "' is " + forward);
        }
        return forward;
    }

    public void destroy() {
        if (actionConfigInstance != null) {
            actionConfigInstance.destroy();
        }
    }

    public ActionConfigurationBean getAction(String actionName) {
        return actionConfigInstance.getActionConfiguredBean(actionName);
    }

    public ActionConfigurationBean getDefaultAction() {
        return actionConfigInstance.getDefaultAction();
    }
}
