/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.portlet.register;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.module.exception.ActionException;
import org.riverock.module.exception.ModuleException;
import org.riverock.module.factory.ActionFactory;
import org.riverock.module.factory.WebmillPortletActionFactoryImpl;
import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.web.config.PortletModuleConfigImpl;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.request.WebmillPortletModuleRequestImpl;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.response.PortletModuleResponseImpl;

/**
 * @author SMaslyukov
 *         Date: 24.03.2005
 *         Time: 16:03:52
 *         $Id$
 */
public abstract class AbstractPortlet implements Portlet {
    private final static Logger log = Logger.getLogger(AbstractPortlet.class);

    public static final String ACTION_CONFIG_PARAM_NAME = "action-config";

    protected ActionFactory actionFactory = null;
    protected ModuleConfig moduleConfig = null;

    public abstract String processSystemError(ModuleRequest request, ResourceBundle resourceBundle);
    public abstract void process(PortletRequest actionRequest, PortletResponse actionResponse) throws PortletException, IOException;

    public void init(PortletConfig portletConfig) throws PortletException {
        this.moduleConfig = new PortletModuleConfigImpl(portletConfig);
        try {
            actionFactory = new WebmillPortletActionFactoryImpl(moduleConfig, ACTION_CONFIG_PARAM_NAME);
        }
        catch (ActionException e) {
            String es = "Error init action";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    public void destroy() {
        if (actionFactory!=null) {
            actionFactory.destroy();
        }
        moduleConfig = null;
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {

        process(renderRequest, renderResponse);

        ModuleRequest moduleRequest = new WebmillPortletModuleRequestImpl(renderRequest);
        ModuleResponse moduleResponse = new PortletModuleResponseImpl(renderResponse);
        ResourceBundle bundle = moduleConfig.getResourceBundle(renderRequest.getLocale());
        String forwardPage = (String)renderRequest.getAttribute( Constants.FORWARD_PAGE_ACTION );
        moduleRequest.setAttribute(Constants.PRINCIPAL_BEAN, moduleRequest.getUser() );

        if (log.isDebugEnabled()) {
            log.debug("forward attr: " + forwardPage );
            log.debug("actionMessage attr: " +  renderRequest.getAttribute( Constants.ACTION_MESSAGE ));
        }

        // forward page
        Throwable th = null;
        try {
            if (StringUtils.isNotBlank(forwardPage)) {
                moduleConfig.getContext().getRequestDispatcher( forwardPage ).include( moduleRequest, moduleResponse );
            }
        }
        catch (Throwable e) {
            th = e;
        }
        if (th!=null) {
            forwardPage = processSystemError(moduleRequest, bundle );
            try {
                moduleConfig.getContext().getRequestDispatcher( forwardPage ).include( moduleRequest, moduleResponse );
            }
            catch (ModuleException e) {
                throw new PortletException("Error include context", e);
            }
        }
    }
}
