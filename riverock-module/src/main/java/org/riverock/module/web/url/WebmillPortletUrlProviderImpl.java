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

import org.riverock.interfaces.ContainerConstants;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.WebmillPortletConstants;
import org.riverock.common.utils.PortletUtils;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 17:03:09
 *         $Id: WebmillPortletUrlProviderImpl.java 1254 2007-07-18 13:45:32Z serg_main $
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
            PortletUtils.url( moduleName, (PortletRequest)moduleRequest.getOriginRequest(), (PortletResponse)moduleResponse.getOriginResponse() ) +
            WebmillPortletConstants.ACTION_NAME_PARAM + '=' + actionName + '&';
    }

    public StringBuilder getUrlStringBuilder(String moduleName, String actionName) {
        if (log.isDebugEnabled()) {
            log.debug("request class: " + moduleRequest.getOriginRequest().getClass().getName());
            log.debug("response class: " + moduleResponse.getOriginResponse().getClass().getName());
        }
        return
            PortletUtils.urlStringBuilder(
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
