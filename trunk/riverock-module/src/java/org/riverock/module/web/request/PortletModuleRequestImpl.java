package org.riverock.module.web.request;

import java.util.Locale;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.module.web.session.ModuleSession;
import org.riverock.module.web.session.PortletModuleSessionImpl;
import org.riverock.webmill.portlet.PortletTools;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 15:23:20
 *         $Id$
 */
public abstract class PortletModuleRequestImpl implements ModuleRequest {
    static private final Log log = LogFactory.getLog(PortletModuleRequestImpl.class);

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
        return PortletTools.getInt(portletRequest, key);
    }

    public Integer getInt(String key, Integer defValue) {
        return PortletTools.getInt(portletRequest, key, defValue);
    }

    public String getString(String key) {
        if (log.isDebugEnabled()){
            log.debug("key: "+key + ", value: "+PortletTools.getString(portletRequest, key));
        }
        return PortletTools.getString(portletRequest, key);
    }

    public String getString(String key, String defValue) {
        return PortletTools.getString(portletRequest, key, defValue);
    }

    public Long getLong(String key) {
        return PortletTools.getLong(portletRequest, key);
    }

    public Long getLong(String key, Long defValue) {
        return PortletTools.getLong(portletRequest, key, defValue);
    }

    public Double getDouble(String key) {
        return PortletTools.getDouble(portletRequest, key);
    }

    public Double getDouble(String key, Double defValue) {
        return PortletTools.getDouble(portletRequest, key, defValue);
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
