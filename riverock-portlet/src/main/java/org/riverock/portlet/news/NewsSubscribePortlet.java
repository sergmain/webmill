/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.news;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;



import org.apache.log4j.Logger;

import org.riverock.webmill.container.portlet.extend.GenericWebmillPortlet;

/**
 * User: SMaslyukov
 * Date: 19.03.2005
 * Time: 15:03:05
 * $Id$
 */
public final class NewsSubscribePortlet extends GenericWebmillPortlet {

    private final static Logger log = Logger.getLogger( NewsSubscribePortlet.class );

    static final String PORTLET_NAME = "mill.news_subscribe";
    static final String META_SUBSCRIBED_ON_NEWS = "subscribed-on-news";
    static final String CHECKBOX_NAME = "subscribe";
    static final String CHANGE_STATUS_PARAM = "change-status";

    public NewsSubscribePortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {
        if ( actionRequest.getParameter( CHANGE_STATUS_PARAM )==null ) {
            return;
        }

        try {
            boolean isSubscribe = actionRequest.getParameter( CHECKBOX_NAME )!=null;
            NewsSubscribe.setSubscribeStatus(actionRequest, isSubscribe);
        } catch (Exception e) {
            String es = "Error change status of subscriprion";
            log.error( es, e );
            throw new PortletException(es, e);
        }
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        NewsSubscribe subscribe = new NewsSubscribe();
        doRender( renderRequest, renderResponse, subscribe );
    }
}
