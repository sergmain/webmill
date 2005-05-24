package org.riverock.module.web.response;

import javax.portlet.PortletResponse;
import javax.portlet.ActionResponse;
import java.io.IOException;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 15:26:22
 *         $Id$
 */
public class PortletModuleResponseImpl implements ModuleResponse {
    private PortletResponse portletResponse = null;

    public PortletModuleResponseImpl(PortletResponse portletResponse) {
        this.portletResponse = portletResponse;
    }

    public Object getOriginResponse() {
        return portletResponse;
    }

    public void sendRedirect(String newUrl) throws IOException {
        if (portletResponse instanceof ActionResponse) {
            ((ActionResponse)portletResponse).sendRedirect(newUrl);
        }
        else {
            throw new IllegalStateException("sendRedirect not supported by "+portletResponse.getClass().getName());
        }
    }
}
