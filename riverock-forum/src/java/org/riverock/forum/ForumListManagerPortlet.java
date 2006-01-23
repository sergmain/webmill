package org.riverock.forum;

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.forum.bean.GenericBean;
import org.riverock.forum.util.Constants;
import org.riverock.module.action.ActionNameProvider;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.ModuleActionRequestImpl;
import org.riverock.module.action.WebmillPortletActionNameProviderImpl;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.request.WebmillPortletModuleRequestImpl;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.response.PortletModuleResponseImpl;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.url.WebmillPortletUrlProviderImpl;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.tools.PortletMetadataService;

/**
 * @author SMaslyukov
 *         Date: 24.03.2005
 *         Time: 16:03:52
 *         $Id$
 */
public class ForumListManagerPortlet extends AbstractForumPortlet {
    private final static Logger log = Logger.getLogger(ForumListManagerPortlet.class);

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {

        if (log.isDebugEnabled()) {
            Enumeration en = actionRequest.getParameterNames();
            while(en.hasMoreElements()){
                String key = (String)en.nextElement();
                log.debug("key: "+key+", value: "+actionRequest.getParameter(key));
            }
        }
        String forwardPage = null;
        try {

            ModuleRequest request = new WebmillPortletModuleRequestImpl(actionRequest);
            ModuleResponse response = new PortletModuleResponseImpl(actionResponse);
            UrlProvider urlProvider = new WebmillPortletUrlProviderImpl(request, response);
            ActionNameProvider actionNameProvider = new WebmillPortletActionNameProviderImpl(request);

            ModuleActionRequest moduleActionRequest = new ModuleActionRequestImpl(
                request, response, moduleConfig, urlProvider, actionNameProvider
            );

            initGenericBean( moduleActionRequest );

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
        catch (Exception e) {
            String es = "error check forumId";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
        }

        if (forwardPage == null) {
            forwardPage = "";
        }

        actionRequest.setAttribute(Constants.FORWARD_PAGE_ACTION, forwardPage);
    }

    private GenericBean initGenericBean(ModuleActionRequest forumActionBean) {
        GenericBean genericBean = new GenericBean();

        genericBean.setLoginUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.LOGIN_URL_METADATA ) );
        genericBean.setLogoutUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.LOGOUT_URL_METADATA ) );
        genericBean.setRegisterUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.REGISTER_URL_METADATA ) );
        genericBean.setMembersUrl( PortletMetadataService.getMetadata( (PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.MEMBERS_URL_METADATA ) );
        genericBean.setForumHomeUrl(
            PortletService.ctxStringBuilder((PortletRequest)forumActionBean.getRequest().getOriginRequest(), Constants.WM_FORUM_LIST_MANAGER_PORTLET_NAME).
            append('?').
            append("a=1")
        );
        genericBean.setRemoteAddr( forumActionBean.getRequest().getRemoteAddr() );
        genericBean.setUserAgent( forumActionBean.getRequest().getUserAgent() );

        forumActionBean.getRequest().setAttribute( Constants.GENERIC_BEAN, genericBean );
        return genericBean;
    }
}
