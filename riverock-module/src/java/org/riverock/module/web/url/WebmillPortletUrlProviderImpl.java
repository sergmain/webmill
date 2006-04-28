package org.riverock.module.web.url;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

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
        return
            PortletService.urlStringBuilder(
                moduleName,  (PortletRequest)moduleRequest.getOriginRequest(), (PortletResponse)moduleResponse.getOriginResponse(),
                (String)((PortletRequest)moduleRequest).getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE )
            ).
                append( WebmillPortletConstants.ACTION_NAME_PARAM ).
                append( '=' ).
                append( actionName ).
                append( '&' );
    }
}
