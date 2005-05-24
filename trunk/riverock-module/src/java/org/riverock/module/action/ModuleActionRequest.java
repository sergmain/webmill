package org.riverock.module.action;

import java.util.ResourceBundle;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.url.UrlProvider;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 14:24:04
 *         $Id$
 */
public interface ModuleActionRequest {
    public ModuleConfig getConfig();

    public ResourceBundle getResourceBundle();

    public ModuleRequest getRequest();

    public ModuleResponse getResponse();

    public UrlProvider getUrlProvider();
    
    public String getActionName();

    public void setStatus(String status);

    public String getStatus();
}
