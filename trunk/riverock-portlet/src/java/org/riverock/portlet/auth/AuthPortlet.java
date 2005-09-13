package org.riverock.portlet.auth;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.portlet.portlets.AbstractPortlet;
import org.riverock.portlet.portlets.PortletErrors;
import org.riverock.portlet.portlets.bean.GenericBean;
import org.riverock.portlet.main.Constants;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.request.WebmillPortletModuleRequestImpl;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.response.PortletModuleResponseImpl;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.url.WebmillPortletUrlProviderImpl;
import org.riverock.module.action.ActionNameProvider;
import org.riverock.module.action.WebmillPortletActionNameProviderImpl;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.ModuleActionRequestImpl;
import org.riverock.module.exception.ActionException;
import org.riverock.webmill.container.tools.PortletMetadataService;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 14:29:18
 *         $Id$
 */
public class AuthPortlet extends AbstractPortlet {
    private static final Log log = LogFactory.getLog(AuthPortlet.class);

    public String processSystemError(ModuleRequest request, ResourceBundle resourceBundle) {
        return PortletErrors.systemError(request, resourceBundle);
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {

        if (log.isDebugEnabled()) {
            Enumeration en = actionRequest.getParameterNames();
            while(en.hasMoreElements()){
                String key = (String)en.nextElement();
                log.debug("key: "+key+", value: "+actionRequest.getParameter(key));
            }
        }
        String forwardPage = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ModuleRequest request = new WebmillPortletModuleRequestImpl(actionRequest);
            ModuleResponse response = new PortletModuleResponseImpl(actionResponse);
            UrlProvider urlProvider = new WebmillPortletUrlProviderImpl(request, response);
            ActionNameProvider actionNameProvider = new WebmillPortletActionNameProviderImpl(request);

            ModuleActionRequest moduleActionRequest = new ModuleActionRequestImpl(
                request, response, moduleConfig, urlProvider, actionNameProvider
            );

            initGenericBean( moduleActionRequest );

            if (request.getUser()==null){
                forwardPage = AuthError.notLoggedError(moduleActionRequest);
            }
            else {
                try {
                    forwardPage = actionFactory.doAction(moduleActionRequest);
                } catch (ActionException ex) {
                    log.error("error execute action", ex);
                    forwardPage = PortletErrors.systemError(moduleActionRequest.getRequest(), moduleActionRequest.getResourceBundle());
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

        actionRequest.setAttribute(Constants.FORWARD_PAGE_ACTION, forwardPage);
    }

    private GenericBean initGenericBean(ModuleActionRequest moduleActionRequest ) {
        GenericBean genericBean = new GenericBean();

        genericBean.setLoginUrl( PortletMetadataService.getMetadata( (PortletRequest)moduleActionRequest.getRequest().getOriginRequest(), Constants.LOGIN_URL_METADATA ) );
        genericBean.setLogoutUrl( PortletMetadataService.getMetadata( (PortletRequest)moduleActionRequest.getRequest().getOriginRequest(), Constants.LOGOUT_URL_METADATA ) );
        genericBean.setRegisterUrl( PortletMetadataService.getMetadata( (PortletRequest)moduleActionRequest.getRequest().getOriginRequest(), Constants.REGISTER_URL_METADATA ) );
        genericBean.setMembersUrl( PortletMetadataService.getMetadata( (PortletRequest)moduleActionRequest.getRequest().getOriginRequest(), Constants.MEMBERS_URL_METADATA ) );
        genericBean.setBaseModuleUrl(
            PortletService.ctxStringBuffer((PortletRequest)moduleActionRequest.getRequest().getOriginRequest(), "riverock.auth" ).
            append("?a=1")
        );
        genericBean.setRemoteAddr( moduleActionRequest.getRequest().getRemoteAddr() );
        genericBean.setUserAgent( moduleActionRequest.getRequest().getUserAgent() );

        moduleActionRequest.getRequest().setAttribute( Constants.GENERIC_BEAN, genericBean );
        return genericBean;
    }
}
