/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.forum;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.forum.util.Constants;
import org.riverock.module.action.ActionNameProvider;
import org.riverock.module.action.WebmillPortletActionNameProviderImpl;
import org.riverock.module.exception.ActionException;
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
 *         $Id: AbstractForumPortlet.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public abstract class AbstractForumPortlet implements Portlet {
    private final static Logger log = Logger.getLogger(AbstractForumPortlet.class);

    protected ActionFactory actionFactory = null;
    protected ModuleConfig moduleConfig = null;

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
        process(actionRequest, actionResponse);
	}

    public void init(PortletConfig portletConfig) throws PortletException {
        this.moduleConfig = new PortletModuleConfigImpl(portletConfig);
        try {
            actionFactory = new WebmillPortletActionFactoryImpl(moduleConfig, Constants.ACTION_CONFIG_PARAM_NAME);
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

    public abstract void process(PortletRequest request, PortletResponse response) throws PortletException, IOException;

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {

        process(renderRequest, renderResponse);

        renderRequest.setAttribute( Constants.REQUEST_LOCALE_VALUE, renderRequest.getLocale() );
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
            String forwardName = ForumError.systemError(moduleRequest, bundle );
            ModuleRequest request = new WebmillPortletModuleRequestImpl(renderRequest);
            ActionNameProvider actionNameProvider = new WebmillPortletActionNameProviderImpl(request);
            try {
                forwardPage = actionFactory.getForwardPath( actionNameProvider.getActionName(), forwardName );
                moduleConfig.getContext().getRequestDispatcher( forwardPage ).include( moduleRequest, moduleResponse );
            }
            catch (Exception e) {
                throw new PortletException("Error include context", e);
            }
        }
    }
}
