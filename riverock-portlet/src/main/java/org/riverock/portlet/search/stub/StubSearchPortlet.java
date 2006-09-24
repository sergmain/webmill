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
package org.riverock.portlet.search.stub;

import org.riverock.webmill.container.ContainerConstants;
import org.riverock.portlet.dao.PortletDaoFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.portlet.*;
import java.io.IOException;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 23:38:11
 * <p/>
 * $Id$
 */
public class StubSearchPortlet implements Portlet {
    private static Logger log = Logger.getLogger(StubSearchPortlet.class);

    private PortletConfig portletConfig=null;

    public void init(PortletConfig config) throws PortletException {
        this.portletConfig=config;
    }

    public void destroy() {
        portletConfig=null;
    }
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        Long siteId = new Long( request.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        log.debug("Start processAction()");

        String searchWord = request.getParameter(StubSearchConstants.SEARCH_WORD);
        PortletSession portletSession = request.getPortletSession(true);
        portletSession.setAttribute(StubSearchConstants.SEARCH_WORD, searchWord);

        if (StringUtils.isNotBlank(searchWord)) {
            searchWord = searchWord.trim();
            if (searchWord.length()>3) {
                PortletDaoFactory.getSearchDao().storeRequest(siteId, searchWord );
                portletSession.setAttribute(StubSearchConstants.SEARCH_RESULT, "Content for '"+searchWord+"' not found.");
                return;
            }
        }
        portletSession.setAttribute(StubSearchConstants.SEARCH_RESULT, "Search string '"+searchWord+"' too short.");
    }

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PortletSession portletSession = request.getPortletSession(true);
        Object word = portletSession.getAttribute(StubSearchConstants.SEARCH_WORD);
        if (word ==null) {
            portletConfig.getPortletContext().getRequestDispatcher(StubSearchConstants.RIVEROCK_SEARCH_STUB_INDEX_JSP).include(request, response);
        }
        else {
            request.setAttribute(StubSearchConstants.SEARCH_WORD, word);
            portletSession.removeAttribute(StubSearchConstants.SEARCH_WORD);
            portletConfig.getPortletContext().getRequestDispatcher(StubSearchConstants.RIVEROCK_SEARCH_STUB_RESULT_SEARCH_JSP).include(request, response);
        }
    }
}
