/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.register;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.portlet.PortletMetadataService;
import org.riverock.common.utils.PortletUtils;
import org.riverock.module.action.ActionNameProvider;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.ModuleActionRequestImpl;
import org.riverock.module.action.WebmillPortletActionNameProviderImpl;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.request.WebmillPortletModuleRequestImpl;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.response.PortletModuleResponseImpl;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.url.WebmillPortletUrlProviderImpl;
import org.riverock.module.exception.ModuleException;
import org.riverock.portlet.register.bean.GenericBean;

/**
 * User: SergeMaslyukov
 * Date: 22.02.2005
 * Time: 18:21:10
 * $Id: RegisterPortlet.java 1229 2007-06-28 11:25:40Z serg_main $
 */
public class RegisterPortlet extends AbstractPortlet {
    private static final Logger log = Logger.getLogger(RegisterPortlet.class);

    public String processSystemError(ModuleRequest request, ResourceBundle resourceBundle) {
        return PortletErrors.systemError(request, resourceBundle);
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public void process(PortletRequest actionRequest, PortletResponse actionResponse) throws IOException, ModuleException {

        if (log.isDebugEnabled()) {
            Enumeration en = actionRequest.getParameterNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                log.debug("key: " + key + ", value: " + actionRequest.getParameter(key));
            }
        }

        ModuleRequest request = new WebmillPortletModuleRequestImpl(actionRequest);
        ModuleResponse response = new PortletModuleResponseImpl(actionResponse);
        UrlProvider urlProvider = new WebmillPortletUrlProviderImpl(request, response);
        ActionNameProvider actionNameProvider = new WebmillPortletActionNameProviderImpl(request);

        ModuleActionRequest moduleActionRequest = new ModuleActionRequestImpl(
            request, response, moduleConfig, urlProvider, actionNameProvider
        );

        try {
            initGenericBean(moduleActionRequest);
            String forwardPage = actionFactory.doAction(moduleActionRequest);

            if (StringUtils.isNotBlank(forwardPage)) {
                moduleConfig.getContext().getRequestDispatcher( forwardPage ).include( request, response );
            }
        }
        catch (Throwable e) {
            String es = "error process action request";
            log.error(es, e);

            String forwardName = PortletErrors.error(moduleActionRequest, e.toString());
            String forwardUrl = actionFactory.getForwardPath(moduleActionRequest.getActionName(), forwardName);

            moduleConfig.getContext().getRequestDispatcher( forwardUrl ).include( request, response );
        }
    }

    private GenericBean initGenericBean(ModuleActionRequest moduleActionRequest) {
        GenericBean genericBean = new GenericBean();

        genericBean.setLoginUrl(PortletMetadataService.getMetadata((PortletRequest) moduleActionRequest.getRequest().getOriginRequest(), Constants.LOGIN_URL_METADATA));
        genericBean.setLogoutUrl(PortletMetadataService.getMetadata((PortletRequest) moduleActionRequest.getRequest().getOriginRequest(), Constants.LOGOUT_URL_METADATA));
        genericBean.setRegisterUrl(PortletMetadataService.getMetadata((PortletRequest) moduleActionRequest.getRequest().getOriginRequest(), Constants.REGISTER_URL_METADATA));
        genericBean.setMembersUrl(PortletMetadataService.getMetadata((PortletRequest) moduleActionRequest.getRequest().getOriginRequest(), Constants.MEMBERS_URL_METADATA));
        genericBean.setActionUrl(moduleActionRequest.getUrlProvider().getActionUrl());
        genericBean.setBaseModuleUrl(
            PortletUtils.ctxStringBuilder((PortletRequest) moduleActionRequest.getRequest().getOriginRequest(), RegisterConstants.REGISTER_PORTLET).
                append("?a=1")
        );
        genericBean.setRemoteAddr(moduleActionRequest.getRequest().getRemoteAddr());
        genericBean.setUserAgent(moduleActionRequest.getRequest().getUserAgent());

        moduleActionRequest.getRequest().setAttribute(RegisterConstants.REGISTER_BEAN, genericBean);
        moduleActionRequest.getRequest().setAttribute(RegisterConstants.REQUEST_LOCALE_VALUE, moduleActionRequest.getRequest().getLocale());
        return genericBean;
    }
}
