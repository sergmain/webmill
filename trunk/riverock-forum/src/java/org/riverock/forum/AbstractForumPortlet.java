package org.riverock.forum;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.forum.util.Constants;
import org.riverock.module.exception.ActionException;
import org.riverock.module.exception.ModuleException;
import org.riverock.module.factory.ActionFactory;
import org.riverock.module.factory.WebmillPortletActionFactoryImpl;
import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.web.config.PortletModuleConfigImpl;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.request.WebmillPortletModuleRequestImpl;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.response.PortletModuleResponseImpl;

/**
 * @author SMaslyukov
 *         Date: 24.03.2005
 *         Time: 16:03:52
 *         $Id$
 */
public abstract class AbstractForumPortlet implements Portlet {
    private final static Logger log = Logger.getLogger(AbstractForumPortlet.class);

    protected ActionFactory actionFactory = null;
    protected ModuleConfig moduleConfig = null;

    public void init(PortletConfig portletConfig) throws PortletException {
        this.moduleConfig = new PortletModuleConfigImpl(portletConfig);
        try {
            actionFactory = new WebmillPortletActionFactoryImpl(moduleConfig, Constants.ACTION_CONFIG_PARAM_NAME);
        }
        catch (ActionException e) {
            String es = "Error init action";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    public void destroy() {
        if (actionFactory!=null) {
            actionFactory.destroy();
        }
        moduleConfig = null;
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {

        ModuleRequest moduleRequest = new WebmillPortletModuleRequestImpl(renderRequest);
        ModuleResponse moduleResponse = new PortletModuleResponseImpl(renderResponse);
        ResourceBundle bundle = moduleConfig.getResourceBundle(renderRequest.getLocale());
        String forwardPage = (String)renderRequest.getAttribute( Constants.FORWARD_PAGE_ACTION );
        moduleRequest.setAttribute(Constants.PRINCIPAL_BEAN, moduleRequest.getUser() );

        if (log.isDebugEnabled()) {
            log.debug("forward attr: " + forwardPage );
            log.debug("actionMessage attr: " +  renderRequest.getAttribute( Constants.ACTION_MESSAGE ));
        }

        // forward page
        Throwable th = null;
        try {
            if (!StringTools.isEmpty(forwardPage)) {
                moduleConfig.getContext().getRequestDispatcher( forwardPage ).include( moduleRequest, moduleResponse );
            }
        }
        catch (Throwable e) {
            th = e;
        }
        if (th!=null) {
            forwardPage = ForumError.systemError(moduleRequest, bundle );
            try {
                moduleConfig.getContext().getRequestDispatcher( forwardPage ).include( moduleRequest, moduleResponse );
            }
            catch (ModuleException e) {
                throw new PortletException("Error include context", e);
            }
        }
    }
}
