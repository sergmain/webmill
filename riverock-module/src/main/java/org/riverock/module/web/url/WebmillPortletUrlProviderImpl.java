/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.module.web.url;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.WebmillPortletConstants;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 17:03:09
 *         $Id$
 */
public class WebmillPortletUrlProviderImpl implements UrlProvider {
    private static final Logger log = Logger.getLogger(WebmillPortletUrlProviderImpl.class);

    private ModuleRequest moduleRequest = null;
    private ModuleResponse moduleResponse = null;
    private Map<String, List<String>> params = new HashMap<String, List<String>>();

    public WebmillPortletUrlProviderImpl(ModuleRequest moduleRequest, ModuleResponse moduleResponse){
        this.moduleRequest = moduleRequest;
        this.moduleResponse = moduleResponse;
    }

    public WebmillPortletUrlProviderImpl(ModuleActionRequest moduleActionRequest){
        this.moduleRequest = moduleActionRequest.getRequest();
        this.moduleResponse = moduleActionRequest.getResponse();
    }

    public String getUrl(String moduleName, String actionName) {
        if (log.isDebugEnabled()) {
            log.debug("request class: " + moduleRequest.getClass().getName());
            log.debug("response class: " + moduleResponse.getClass().getName());
        }
        return
            PortletService.url( moduleName, (PortletRequest)moduleRequest.getOriginRequest(), (PortletResponse)moduleResponse.getOriginResponse() ) +
            WebmillPortletConstants.ACTION_NAME_PARAM + '=' + actionName + '&';
    }

    public StringBuilder getUrlStringBuilder(String moduleName, String actionName) {
        if (log.isDebugEnabled()) {
            log.debug("request class: " + moduleRequest.getOriginRequest().getClass().getName());
            log.debug("response class: " + moduleResponse.getOriginResponse().getClass().getName());
        }
        return
            PortletService.urlStringBuilder(
                moduleName,  
		(PortletRequest)moduleRequest.getOriginRequest(), 
		(PortletResponse)moduleResponse.getOriginResponse(),
                (String)((PortletRequest)moduleRequest.getOriginRequest()).getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE )
            ).
                append( WebmillPortletConstants.ACTION_NAME_PARAM ).
                append( '=' ).
                append( actionName ).
                append( '&' );
    }

    public void setParameter(String key, String value) {
        List<String> list = params.get(key);
        if (list==null) {
            list = new ArrayList<String>();
            list.add(key);
            params.put(key, list);
        }
        else {
            list.add(value);
        }
    }

    public String getActionUrl(){
        if (log.isDebugEnabled()) {
            log.debug("rorigin request: " +moduleResponse.getOriginResponse());
        }
        if (!(moduleResponse.getOriginResponse() instanceof RenderResponse)) {
            return null;
        }
        PortletURL url = ((RenderResponse) moduleResponse.getOriginResponse()).createActionURL();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            for (String s : entry.getValue()) {
                url.setParameter(entry.getKey(), s);
            }
        }
        return url.toString();
    }

    public String getRenderUrl(){
        if (!(moduleResponse.getOriginResponse() instanceof RenderResponse)) {
            return null;
        }
        PortletURL url = ((RenderResponse) moduleResponse.getOriginResponse()).createRenderURL();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            for (String s : entry.getValue()) {
                url.setParameter(entry.getKey(), s);
            }
        }
        return url.toString();
    }
}
