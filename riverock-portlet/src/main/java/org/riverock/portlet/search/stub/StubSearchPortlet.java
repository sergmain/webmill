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
package org.riverock.portlet.search.stub;

import org.riverock.interfaces.ContainerConstants;
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
