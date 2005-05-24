package org.riverock.module.action;

import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.WebmillPortletConstants;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 13:26:15
 *         $Id$
 */
public class WebmillPortletActionNameProviderImpl implements ActionNameProvider {

    private ModuleRequest request = null;
    public WebmillPortletActionNameProviderImpl(ModuleRequest request) {
        this.request = request;
    }

    public String getActionName() {
        return request.getParameter( WebmillPortletConstants.ACTION_NAME_PARAM );
    }
}
