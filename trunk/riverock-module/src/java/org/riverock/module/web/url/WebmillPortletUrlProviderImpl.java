package org.riverock.module.web.url;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.webmill.portlet.PortletTools;
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
    private static final Log log = LogFactory.getLog(WebmillPortletUrlProviderImpl.class);

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
            PortletTools.url( moduleName, (PortletRequest)moduleRequest.getOriginRequest(), (PortletResponse)moduleResponse.getOriginResponse() ) +
            WebmillPortletConstants.ACTION_NAME_PARAM + '=' + actionName + '&';
    }

    public StringBuffer getUrlStringBuffer(String moduleName, String actionName) {
        return
            PortletTools.urlStringBuffer( moduleName,  (PortletRequest)moduleRequest.getOriginRequest(), (PortletResponse)moduleResponse.getOriginResponse() ).
            append( WebmillPortletConstants.ACTION_NAME_PARAM ).
            append( '=' ).
            append( actionName ).
            append( '&' );
    }
}
