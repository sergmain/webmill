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
package org.riverock.module.factory.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import org.riverock.common.xml.EntityResolverImpl;
import org.riverock.module.action.Action;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionBean;
import org.riverock.module.factory.bean.ActionConfigBean;
import org.riverock.module.factory.bean.ActionConfigurationBean;
import org.riverock.module.factory.bean.ForwardBean;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:20:58
 *         $Id$
 */
public class ActionConfig {
    static private final Logger log = Logger.getLogger(ActionConfig.class);

    private ActionConfigBean configBean = null;
    private ActionConfigurationBean defaultAction = null;
    private Map<String, ActionConfigurationBean> actions = new HashMap<String, ActionConfigurationBean>();

    public void destroy() {
        configBean = null;
        defaultAction = null;
        if (actions!=null) {
            actions.clear();
            actions = null;
        }
    }

    private ActionConfig(ActionConfigBean configBean) throws ActionException {
        this.configBean = configBean;

        if (log.isDebugEnabled()) {
            log.debug("configBean: " + configBean);
            if (configBean!=null) {
                log.debug("Default action:" + configBean.getDefaultAction());
            }
        }
        try {
            Iterator it = configBean.getActions().iterator();
            while (it.hasNext()) {
                ActionBean actionBean = (ActionBean) it.next();
                final Object o = Class.forName(actionBean.getType()).newInstance();
                Action action = (Action) o;
                ActionConfigurationBean configurationBean = new ActionConfigurationBean();
                configurationBean.setAction( action );
                configurationBean.setActionBean( actionBean );

                actions.put( actionBean.getPath(), configurationBean );
            }
            this.defaultAction = (ActionConfigurationBean)actions.get(configBean.getDefaultAction());
            if (defaultAction==null) {
                throw new ActionException("Default action not defined");
            }

        } catch (InstantiationException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException(es,e);
        } catch (IllegalAccessException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException(es,e);
        } catch (ClassNotFoundException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException(es,e);
        }
    }

    public static ActionConfig getInstance(File configFile) throws ActionException {

        ActionConfigBean bean = null;
        try {
            bean = digisterConfigFile(configFile);
        }
        catch (IOException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException( es, e);
        }
        catch (SAXException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException( es, e);
        }

        return new ActionConfig(bean);

    }

    private static ActionConfigBean digisterConfigFile(File configFile) throws IOException, SAXException {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (log.isDebugEnabled()) {
            log.debug("Start digest file: " + configFile.getName() );
            try {
                log.debug("classloader: " + cl +"\nhashCode: " + cl.hashCode() );
                Class c = cl.loadClass("org.riverock.module.factory.bean.ActionConfigBean");
                log.debug("result of loading class: " + c);
            }
            catch (ClassNotFoundException e) {
                log.error("Error load class ActionConfigBean", e);
            }
        }

        Digester digester = new Digester();
        digester.setClassLoader( cl );
        digester.setEntityResolver( new EntityResolverImpl() );
        digester.setValidating(false);
        ActionConfigBean bean = null;

        digester.addObjectCreate("action-config", ActionConfigBean.class);

        digester.addObjectCreate("action-config/default-forward", ForwardBean.class);
        digester.addCallMethod("action-config/role","addRole", 0);
        digester.addSetNext("action-config/default-forward", "addForwards");
        digester.addSetProperties("action-config/default-forward", "name", "name");
        digester.addSetProperties("action-config/default-forward", "path", "path");

        digester.addObjectCreate("action-config/action", ActionBean.class);
        digester.addSetNext("action-config/action", "addActions");
        digester.addCallMethod("action-config/default-action","setDefaultAction", 0);

        digester.addSetProperties("action-config/action", "name", "name");
        digester.addSetProperties("action-config/action", "path", "path");
        digester.addSetProperties("action-config/action", "type", "type");
        digester.addSetProperties("action-config/action", "default-forward", "defaultForward");
        digester.addSetProperties("action-config/action", "is-redirect", "redirect");

        digester.addCallMethod("action-config/action/role","addRole", 0);

        digester.addObjectCreate("action-config/action/forward", ForwardBean.class);
        digester.addSetNext("action-config/action/forward", "addForwards");
        digester.addSetProperties("action-config/action/forward", "name", "name");
        digester.addSetProperties("action-config/action/forward", "path", "path");

        bean = (ActionConfigBean) digester.parse(configFile);
        if (bean==null ) {
            // Tofo decide return null or throw exception
            return null;
        }
        return bean;

    }

    public ActionConfigurationBean getDefaultAction() {
        return defaultAction;
    }

    public ActionConfigurationBean getActionConfiguredBean(String actionName) {
        return (ActionConfigurationBean)actions.get(actionName);
    }

    public ActionConfigBean getConfigBean() {
        return configBean;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("forum-action.xml");
        ActionConfigBean c = digisterConfigFile( file );

        file = new File("forum-action-manage-list.xml ");
        c = digisterConfigFile( file );

        boolean isFound = c!=null;
        System.out.println("isFound = " + isFound);
    }
}
