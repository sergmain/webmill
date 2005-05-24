package org.riverock.module.web.response;

import javax.portlet.PortletResponse;
import java.io.IOException;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:39:30
 *         $Id$
 */
public interface ModuleResponse {
    public Object getOriginResponse();
    public void sendRedirect(String newUrl) throws IOException;
}
