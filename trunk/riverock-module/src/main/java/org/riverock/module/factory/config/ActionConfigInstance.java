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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import org.riverock.common.tools.XmlTools;
import org.riverock.module.action.ActionInstance;
import org.riverock.module.config.schema.Action;
import org.riverock.module.config.schema.ActionConfig;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionConfigurationBean;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:20:58
 *         $Id$
 */
public class ActionConfigInstance {
    static private final Logger log = Logger.getLogger(ActionConfigInstance.class);

    private ActionConfig configBean = null;
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

    private ActionConfigInstance(ActionConfig configBean) throws ActionException {
        if (configBean==null) {
            throw new IllegalArgumentException("Config bean is null");
        }
        this.configBean = configBean;

        if (log.isDebugEnabled()) {
            log.debug("configBean: " + configBean);
            log.debug("Default action:" + configBean.getDefaultAction());
        }
        try {
            for (Action action : configBean.getActions()) {
                final Object o = Class.forName(action.getType()).newInstance();
                ActionInstance actionInstance = (ActionInstance) o;
                ActionConfigurationBean configurationBean = new ActionConfigurationBean();
                configurationBean.setAction(actionInstance);
                configurationBean.setActionBean( action );

                actions.put( action.getPath(), configurationBean );
            }
            this.defaultAction = actions.get(configBean.getDefaultAction());
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

    public static ActionConfigInstance getInstance(File configFile) throws ActionException {
        InputStream inputStream = null;
        try {
            inputStream= new FileInputStream(configFile);
            return getInstance(inputStream);
        }
        catch (FileNotFoundException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException( es, e);
        }
        finally{
            if (inputStream!=null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    log.warn("Error", e);
                }
            }
        }

    }
    public static ActionConfigInstance getInstance(InputStream inputStream) throws ActionException {

        ActionConfig bean = null;
        try {
            bean = XmlTools.getObjectFromXml(ActionConfig.class, inputStream);
        }
        catch (JAXBException e) {
            String es = "Error";
            log.error(es, e);
            throw new ActionException( es, e);
        }

        return new ActionConfigInstance(bean);

    }

/*
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

*/
    public ActionConfigurationBean getDefaultAction() {
        return defaultAction;
    }

    public ActionConfigurationBean getActionConfiguredBean(String actionName) {
        return actions.get(actionName);
    }

    public ActionConfig getConfigBean() {
        return configBean;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("forum-action.xml");
        ActionConfigInstance c = getInstance( file );

        file = new File("forum-action-manage-list.xml ");
        c = getInstance( file );

        boolean isFound = c!=null;
        System.out.println("isFound = " + isFound);
    }
}
