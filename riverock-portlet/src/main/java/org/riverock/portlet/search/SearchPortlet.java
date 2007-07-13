package org.riverock.portlet.search;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.search.PortalIndexer;
import org.riverock.interfaces.portal.search.PortalSearchParameter;
import org.riverock.interfaces.portal.search.PortalSearchResult;

/**
 * User: SMaslyukov
 * Date: 13.07.2007
 * Time: 20:44:07
 */
public class SearchPortlet implements Portlet {
    private static Logger log = Logger.getLogger(SearchPortlet.class);

    private PortletConfig portletConfig=null;

    public void init(PortletConfig config) throws PortletException {
        this.portletConfig=config;
    }

    public void destroy() {
        portletConfig=null;
    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
    }

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        final String query = request.getParameter(SearchConstants.SEARCH_QUERY);
        PortalSearchParameter p = new PortalSearchParameter() {
            public String getQuery() {
                return query;
            }

            public Integer getResultPerPage() {
                return null;
            }

            public Integer getStartPage() {
                return null;  
            }
        };

        PortalIndexer portalIndexer = (PortalIndexer) request.getAttribute( ContainerConstants.PORTAL_PORTAL_INDEXER_ATTRIBUTE );
        PortalSearchResult result = portalIndexer.search(p);

        request.setAttribute("result", result);
        request.setAttribute("query", query);

        portletConfig.getPortletContext().getRequestDispatcher(SearchConstants.RIVEROCK_SEARCH_INDEX_JSP).include(request, response);
    }
}
