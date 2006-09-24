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

import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.portlet.GenericWebmillPortlet;

/**
 * User: SergeMaslyukov
 * Date: 18.12.2004
 * Time: 18:21:10
 * $Id$
 */
public final class NewsPortlet extends GenericWebmillPortlet {
    public NewsPortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        
        String newsType = renderRequest.getParameter( NewsSite.NEWS_TYPE );
        PortletResultObject resultObject ;
        if (newsType==null || !NewsSite.NEWS_TYPE_ITEM.equals( newsType ) )
            resultObject = new NewsSite();
        else
            resultObject = new NewsItemSimple();

        doRender( renderRequest, renderResponse, resultObject );
    }
}
