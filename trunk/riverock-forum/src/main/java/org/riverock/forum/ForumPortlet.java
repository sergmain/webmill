/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
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
package org.riverock.forum;

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;

import org.riverock.forum.bean.GenericBean;
import org.riverock.forum.schema.core.WmForumItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.module.action.ActionNameProvider;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.WebmillPortletActionNameProviderImpl;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.request.WebmillPortletModuleRequestImpl;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.response.PortletModuleResponseImpl;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.url.WebmillPortletUrlProviderImpl;
import org.riverock.webmill.container.tools.PortletMetadataService;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author SMaslyukov
 *         Date: 24.03.2005
 *         Time: 16:03:52
 *         $Id$
 */
public class ForumPortlet extends AbstractForumPortlet {
    private final static Logger log = Logger.getLogger(ForumPortlet.class);

    public void process(PortletRequest portletRequest, PortletResponse actionResponse) throws PortletException, IOException {
        long timeStart = System.currentTimeMillis();

        Long forumId = PortletService.getLong(portletRequest, Constants.NAME_FORUM_ID);
        if (log.isDebugEnabled()) {
            log.debug("forumId: "+forumId);
            Enumeration en = portletRequest.getParameterNames();
            while(en.hasMoreElements()){
                String key = (String)en.nextElement();
                log.debug("key: "+key+", value: "+portletRequest.getParameter(key));
            }
        }
        String forwardPage = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ModuleRequest request = new WebmillPortletModuleRequestImpl(portletRequest);
            ModuleResponse response = new PortletModuleResponseImpl(actionResponse);
            UrlProvider urlProvider = new WebmillPortletUrlProviderImpl(request, response);
            ActionNameProvider actionNameProvider = new WebmillPortletActionNameProviderImpl(request);

            ModuleActionRequest moduleActionRequest = new ForumActionBean(
                request, response, moduleConfig, urlProvider, forumId, adapter, actionNameProvider
            );

            WmForumItemType forum = CommonUtils.checkForumId(adapter, forumId, portletRequest.getServerName() );
            if (forum==null){
                log.error("Forum not found for forumId: " + forumId+", serverName: " +portletRequest.getServerName());
                forwardPage = ForumError.noSuchForumError(moduleActionRequest);
            }
            else {
                initGenericBean( moduleActionRequest, forum );
                try {
                    forwardPage = actionFactory.doAction(moduleActionRequest);
                } catch (ActionException ex) {
                    log.error("error execute action", ex);
                    forwardPage = ForumError.systemError(moduleActionRequest);
                }
                if (log.isDebugEnabled()) {
                    log.debug("forwardPage: "+forwardPage);
                }
            }
        }
        catch (Exception e) {
            String es = "error check forumId";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            DatabaseAdapter.close( adapter );
            adapter = null;
        }

        if (forwardPage == null) {
            forwardPage = "";
        }

        portletRequest.setAttribute(Constants.FORWARD_PAGE_ACTION, forwardPage);

        Long processTime = System.currentTimeMillis() - timeStart;
        portletRequest.setAttribute(Constants.PAGE_PROCESS_TIME_ATTRIBUTE, processTime);
    }

    private GenericBean initGenericBean(ModuleActionRequest forumActionBean, WmForumItemType forum) {
        GenericBean genericBean = new GenericBean();

        genericBean.setLoginUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.LOGIN_URL_METADATA ) );
        genericBean.setLogoutUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.LOGOUT_URL_METADATA ) );
        genericBean.setRegisterUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.REGISTER_URL_METADATA ) );
        genericBean.setMembersUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.MEMBERS_URL_METADATA ) );
        genericBean.setActionUrl(forumActionBean.getUrlProvider().getActionUrl());
        genericBean.setForumHomeUrl(
            PortletService.ctxStringBuilder((PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.WM_FORUM_PORTLET_NAME).
            append('?').
            append(Constants.NAME_FORUM_ID).
            append('=').
            append( forum.getForumId() )
        );
        genericBean.setRemoteAddr( forumActionBean.getRequest().getRemoteAddr() );
        genericBean.setUserAgent( forumActionBean.getRequest().getUserAgent() );
        genericBean.setForumName( forum.getForumName() );
        genericBean.setForumId( forum.getForumId() );

        forumActionBean.getRequest().setAttribute( Constants.GENERIC_BEAN, genericBean );
        return genericBean;
    }

    public static int getMessagesPerPage(ModuleConfig moduleConfig) {
        return moduleConfig.getInitParameterInt(Constants.MESSAGES_PER_PAGE_INIT_PARAM, 20);
    }

    public static int getTopicsPerPage(ModuleConfig moduleConfig) {
        return moduleConfig.getInitParameterInt(Constants.TOPICS_PER_PAGE_INIT_PARAM, 50);
    }

    public static int getFloodTime(ModuleConfig moduleConfig) {
        return moduleConfig.getInitParameterInt(Constants.FLOOD_TIME_INIT_PARAM, 5);
    }
}
