package org.riverock.module.web.session;

import javax.portlet.PortletSession;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:30:32
 *         $Id$
 */
public class PortletModuleSessionImpl implements ModuleSession {
    private PortletSession portletSession = null;
    public PortletModuleSessionImpl(PortletSession portletSession) {
        this.portletSession = portletSession;
    }

    public Object getAttribute(String key) {
        return portletSession.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        portletSession.setAttribute(key, value);
    }
}
