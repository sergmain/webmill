package org.riverock.module.web.request;

import java.util.Locale;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.module.web.session.ModuleSession;
import org.riverock.module.web.session.PortletModuleSessionImpl;
import org.riverock.module.tools.RequestTools;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 15:23:20
 *         $Id$
 */
public abstract class PortletModuleRequestImpl implements ModuleRequest {
    static private final Logger log = Logger.getLogger(PortletModuleRequestImpl.class);

    protected PortletRequest portletRequest = null;

    public Object getOriginRequest() {
        return portletRequest;
    }

    public Locale getLocale() {
        return portletRequest.getLocale();
    }

    public String getServerName() {
        return portletRequest.getServerName();
    }

    public Integer getInt(String key) {
        return PortletService.getInt(portletRequest, key);
    }

    public Integer getInt(String key, Integer defValue) {
        return PortletService.getInt(portletRequest, key, defValue);
    }

    public String getString(String key) {
        if (log.isDebugEnabled()){
            log.debug("key: "+key + ", value: "+RequestTools.getString(portletRequest, key, null));
        }
        return RequestTools.getString(portletRequest, key, null);
    }

    public String getString(String key, String defValue) {
        return RequestTools.getString(portletRequest, key, defValue);
    }

    public Long getLong(String key) {
        return PortletService.getLong(portletRequest, key);
    }

    public Long getLong(String key, Long defValue) {
        return PortletService.getLong(portletRequest, key, defValue);
    }

    public Double getDouble(String key) {
        return PortletService.getDouble(portletRequest, key);
    }

    public Double getDouble(String key, Double defValue) {
        return PortletService.getDouble(portletRequest, key, defValue);
    }

    public String getParameter(String key) {
        return portletRequest.getParameter(key);
    }

    public void setAttribute(String key, Object value) {
        portletRequest.setAttribute(key, value);
    }

    public ModuleSession getSession() {
        return getSession(true);
    }

    public ModuleSession getSession(boolean isCreate) {
        return new PortletModuleSessionImpl( portletRequest.getPortletSession(isCreate) );
    }

    public boolean isUserInRole(String role) {
        return portletRequest.isUserInRole( role );
    }
}
